package eu.europa.esig.dss.cbades.validation.timestamp;

import eu.europa.esig.dss.cbades.cbor.CBORArray;
import eu.europa.esig.dss.cbades.cbor.CBORByteString;
import eu.europa.esig.dss.cbades.cbor.CBORObject;
import eu.europa.esig.dss.cbades.cbor.CBORUtils;
import eu.europa.esig.dss.cbades.validation.CBAdESAttribute;
import eu.europa.esig.dss.cbades.validation.CBAdESSignature;
import eu.europa.esig.dss.cbades.validation.CBAdESUHeaders;
import eu.europa.esig.dss.cbades.validation.CBAdESUHeadersComponent;
import eu.europa.esig.dss.cbades.validation.CBORSignature;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.SigDMechanism;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.DSSMessageDigest;
import eu.europa.esig.dss.spi.DSSMessageDigestCalculator;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.spi.validation.timestamp.TimestampMessageDigestBuilder;
import eu.europa.esig.dss.spi.x509.tsp.TimestampToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

/**
 * Builds the message-imprint digest for CB-AdES timestamps
 *
 */
public class CBAdESTimestampMessageDigestBuilder implements TimestampMessageDigestBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(CBAdESTimestampMessageDigestBuilder.class);

    /** The error message to be thrown in case of a message-imprint build error */
    private static final String MESSAGE_IMPRINT_ERROR = "Unable to compute message-imprint for TimestampToken. Reason : %s";

    /** The error message to be thrown in case of a message-imprint build error for a timestamp */
    private static final String MESSAGE_IMPRINT_ERROR_WITH_ID = "Unable to compute message-imprint for TimestampToken with Id '%s'. Reason : %s";

    /** The signature */
    private final CBAdESSignature signature;

    /** The digest algorithm to be used for message-imprint digest computation */
    private DigestAlgorithm digestAlgorithm;

    /** Timestamp token to compute message-digest for */
    private TimestampToken timestampToken;

    /** The canonicalization algorithm to be used for message-imprint computation */
    private String canonicalizationAlgorithm;

    /** The signature element containing the time-stamp token */
    private CBAdESAttribute timestampAttribute;

    /**
     * The constructor to compute message-imprint for timestamps related to the {@code signature}
     *
     * @param signature {@link CBAdESSignature} to create timestamps for
     * @param digestAlgorithm {@link DigestAlgorithm} to be used for message-imprint digest computation
     */
    public CBAdESTimestampMessageDigestBuilder(final CBAdESSignature signature, final DigestAlgorithm digestAlgorithm) {
        this(signature);
        Objects.requireNonNull(digestAlgorithm, "DigestAlgorithm cannot be null!");
        this.digestAlgorithm = digestAlgorithm;
    }

    /**
     * The constructor to compute message-imprint for timestamps related to the {@code signature}
     *
     * @param signature {@link CBAdESSignature} containing timestamps
     * @param timestampToken {@link TimestampToken} to compute message-digest for
     */
    public CBAdESTimestampMessageDigestBuilder(final CBAdESSignature signature, final TimestampToken timestampToken) {
        this(signature);
        Objects.requireNonNull(timestampToken, "TimestampToken cannot be null!");
        this.timestampToken = timestampToken;
        this.digestAlgorithm = timestampToken.getDigestAlgorithm();
        this.canonicalizationAlgorithm = timestampToken.getCanonicalizationMethod();
    }

    /**
     * Default constructor
     *
     * @param signature {@link CBAdESSignature}
     */
    private CBAdESTimestampMessageDigestBuilder(final CBAdESSignature signature) {
        Objects.requireNonNull(signature, "Signature cannot be null!");
        this.signature = signature;
    }

    /**
     * Sets the canonicalization algorithm to be used for message-digest computation
     *
     * @param canonicalizationAlgorithm {@link String}
     * @return this {@code CBAdESTimestampMessageDigestBuilder}
     */
    public CBAdESTimestampMessageDigestBuilder setCanonicalizationAlgorithm(String canonicalizationAlgorithm) {
        this.canonicalizationAlgorithm = canonicalizationAlgorithm;
        return this;
    }

    /**
     * Sets a signature attribute identifying the time-stamp token
     *
     * @param timestampAttribute {@link CBAdESAttribute}
     * @return this {@code CBAdESTimestampMessageDigestBuilder}
     */
    public CBAdESTimestampMessageDigestBuilder setTimestampAttribute(CBAdESAttribute timestampAttribute) {
        this.timestampAttribute = timestampAttribute;
        return this;
    }

    @Override
    public DSSMessageDigest getContentTimestampMessageDigest() {
        try {
            DSSMessageDigestCalculator digestCalculator = new DSSMessageDigestCalculator(digestAlgorithm);
            writeSignedDataBinaries(digestCalculator);
            return digestCalculator.getMessageDigest();

        } catch (Exception e) {
            String errorMessage = timestampToken == null ? String.format(MESSAGE_IMPRINT_ERROR, e.getMessage()) :
                    String.format(MESSAGE_IMPRINT_ERROR_WITH_ID, timestampToken.getDSSIdAsString(), e.getMessage());
            if (LOG.isDebugEnabled()) {
                LOG.warn(errorMessage, e);
            } else {
                LOG.warn(errorMessage);
            }
        }
        return DSSMessageDigest.createEmptyDigest();
    }

    private void writeSignedDataBinaries(DSSMessageDigestCalculator digestCalculator) throws IOException {
        SigDMechanism sigDMechanism = signature.getSigDMechanism();
        if (sigDMechanism != null) {
            writeSigDReferencedOctets(digestCalculator, sigDMechanism);
        } else {
            writePayloadValue(digestCalculator);
        }
    }

    private void writePayloadValue(DSSMessageDigestCalculator digestCalculator) {
        byte[] payload = signature.getCoseSignature().getPayloadBytes();
        digestCalculator.update(payload);
    }

    private void writeSigDReferencedOctets(DSSMessageDigestCalculator digestCalculator, SigDMechanism sigDMechanism) throws IOException {
        List<DSSDocument> documentList;
        switch (sigDMechanism) {
            case OBJECT_ID_BY_URI:
            case OBJECT_ID_BY_URI_HASH:
                documentList = signature.getSignedDocumentsForObjectIdByUriMechanism();
                for (DSSDocument document : documentList) {
                    try (InputStream is = document.openStream()) {
                        digestCalculator.update(is);
                    }
                }
                break;
            default:
                LOG.warn("Unsupported SigDMechanism '{}' has been found!", sigDMechanism);
        }
    }

    @Override
    public DSSMessageDigest getSignatureTimestampMessageDigest() {
        try {
            /*
             * The input of the message imprint computation for the time-stamp tokens
             * encapsulated by sigTst CBOR map shall be the COSE signature value present
             * within the CB-AdES signature.
             * NOTE: This is the same as the content encapsulated within the signature
             *       CBOR byte string member of instances of COSE_Signature type specified
             *       in IETF RFC 9052 [2] clause 4.1.
             */
            // TODO : review the message-imprint computation -> should be CBOR encoded?
            byte[] signatureTimestampData = getSignatureValue();
            return new DSSMessageDigest(digestAlgorithm, DSSUtils.digest(digestAlgorithm, signatureTimestampData));

        } catch (Exception e) {
            String errorMessage = timestampToken == null ? String.format(MESSAGE_IMPRINT_ERROR, e.getMessage()) :
                    String.format(MESSAGE_IMPRINT_ERROR_WITH_ID, timestampToken.getDSSIdAsString(), e.getMessage());
            if (LOG.isDebugEnabled()) {
                LOG.warn(errorMessage, e);
            } else {
                LOG.warn(errorMessage);
            }
        }
        return DSSMessageDigest.createEmptyDigest();
    }

    private byte[] getSignatureValue() {
        return signature.getSignatureValue();
    }

    @Override
    public DSSMessageDigest getTimestampX1MessageDigest() {
        return null;
    }

    @Override
    public DSSMessageDigest getTimestampX2MessageDigest() {
        return null;
    }

    @Override
    public DSSMessageDigest getArchiveTimestampMessageDigest() {
        // TODO : arcTst message-imprint computation algorithm may be not finalized yet
        try {
            if (LOG.isTraceEnabled()) {
                LOG.trace("--->Get 'arcTst' timestamp data : {}", timestampToken == null ? "--> CREATION" : "--> VALIDATION");
            }

            CBORSignature cose = signature.getCoseSignature();

            /*
             * 5.3.6.2.1 Computation of message-imprint for uHeaders with CBOR-bstr-wrapped
             *
             * For computing the input to the message imprint computation, performing step 2) in clause 5.3.6.2,
             * when the CB-AdES signature uses CBOR-bstr-wrapped incorporation for incorporating elments in
             * the uHeaders CBOR array, the steps listed below shall be performed:
             *
             * 1) Initialize the final octet stream to an empty stream.
             */
            final DSSMessageDigestCalculator digestCalculator = new DSSMessageDigestCalculator(digestAlgorithm);

            /*
             * 2) If the sigD header parameter is absent, then:
             *  - If the payload field is present, then concatenate the bytes encapsulated within
             *    the bit string of the payload field.
             *  - Else if the payload field is absent (COSE Payload is detached, and not explicitly referenced
             *    by the sigD header parameter), then retrieve the bytes of the COSE Payload.
             *
             * 3) If the sigD header parameter is present, then concatenate the bytes resulting from processing
             * the contents of its pars member as specified in clause 5.2.9.2.2 of the present document.
             */
            writeSignedDataBinaries(digestCalculator);

            /*
             * 4) Concatenate the CBOR-encoded protected headers map, wrapped within a CBOR byte string.
             */
            // TODO : use the signature's protected header, no clear definition
            switch (signature.getCOSESignatureContext()) {
                case COSE_SIGN:
                case COSE_COUNTER_SIGNATURE:
                case COSE_COUNTER_SIGNATURE_V2:
                    digestCalculator.update(cose.getSignerProtectedHeader().getByteString().getBytes());
                    break;
                case COSE_SIGN1:
                    digestCalculator.update(cose.getBodyProtectedHeader().getByteString().getBytes());
                    break;
                default:
                    throw new UnsupportedOperationException(String.format("The COSE signature context '%s' is " +
                            "not supported for 'arcTst' message-imprint computation!", signature.getCOSESignatureContext()));
            }

            /*
             * 5) Concatenate the value of the bytes of the COSE signature value.
             */
            digestCalculator.update(getSignatureValue());

            /*
             * 6) Concatenate the components present in uHeaders CBOR array, that preced (appear BEFORE)
             * the arcTst CBOR map that contains the time-stamp token that is being validated, in the order
             * they appear within the uHeaders CBOR array, into the final octet stream.
             */
            CBORArray uHeadersArray = signature.getCoseSignature().getUHeaders();
            if (CBORUtils.checkComponentsUnicity(uHeadersArray)) {

                CBAdESUHeaders uHeaders = signature.getUHeaders();
                for (CBAdESUHeadersComponent uHeaderComponent : uHeaders.getAttributes()) {
                    if (timestampAttribute != null && timestampAttribute.equals(uHeaderComponent)) {
                        // the timestamp is reached, stop the iteration
                        break;
                    }

                    digestCalculator.update(getUHeadersComponentValue(uHeaderComponent, canonicalizationAlgorithm));
                }

            } else {
                LOG.warn("Unable to process 'uHeaders' entries for an 'arcTst' timestamp. "
                        + "The 'uHeaders' components shall have a common format (CBOR Byte String or CBOR Map)!");
            }

            final DSSMessageDigest messageDigest = digestCalculator.getMessageDigest();
            if (LOG.isTraceEnabled()) {
                LOG.trace("The 'arcTst' timestamp message-imprint : {}", messageDigest);
            }
            return messageDigest;

        } catch (Exception e) {
            String errorMessage = timestampToken == null ? String.format(MESSAGE_IMPRINT_ERROR, e.getMessage()) :
                    String.format(MESSAGE_IMPRINT_ERROR_WITH_ID, timestampToken.getDSSIdAsString(), e.getMessage());
            if (LOG.isDebugEnabled()) {
                LOG.warn(errorMessage, e);
            } else {
                LOG.warn(errorMessage);
            }
        }
        return DSSMessageDigest.createEmptyDigest();
    }

    private byte[] getUHeadersComponentValue(CBAdESUHeadersComponent uHeaderComponent, String canonicalizationMethod) {
        CBORObject component = uHeaderComponent.getComponent();
        if (uHeaderComponent.isCborBtsrWrapped()) {
            return ((CBORByteString) component).getBytes();
        } else {
            return getCanonicalizedValue(component, canonicalizationMethod);
        }
    }

    private byte[] getCanonicalizedValue(CBORObject cborObject, String canonicalizationMethod) {
        // TODO: canonicalization is not supported yet
        LOG.warn("Canonicalization is not supported in the current version. "
                + "The message imprint computation can lead to an unexpected result");
        // temporary solution
        return CBORUtils.serializeCborObject(cborObject);
    }
    
}
