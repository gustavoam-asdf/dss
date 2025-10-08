package eu.europa.esig.dss.cbades.validation.timestamp;

import co.nstant.in.cbor.model.UnicodeString;
import eu.europa.esig.dss.cbades.CBAdESUtils;
import eu.europa.esig.dss.cbades.COSEProtectedHeader;
import eu.europa.esig.dss.cbades.COSESignatureContext;
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
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.model.DSSMessageDigest;
import eu.europa.esig.dss.spi.DSSMessageDigestCalculator;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.spi.validation.timestamp.TimestampMessageDigestBuilder;
import eu.europa.esig.dss.spi.x509.tsp.TimestampToken;
import eu.europa.esig.dss.utils.Utils;
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
            return digestCalculator.getMessageDigest(digestAlgorithm);

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
        digestCalculator.update(getPayload().getBytes());
    }

    private CBORByteString getPayload() {
        CBORObject payload = signature.getCoseSignature().getPayload();
        if (payload == null || !payload.isByteString()) {
            throw new DSSException("Unable to extract COSE payload or payload has an invalid type!");
        }
        return (CBORByteString) payload;
    }

    private void writeSigDReferencedOctets(DSSMessageDigestCalculator digestCalculator, SigDMechanism sigDMechanism) throws IOException {
        switch (sigDMechanism) {
            case OBJECT_ID_BY_URI:
            case OBJECT_ID_BY_URI_HASH:
                List<DSSDocument> documentList = signature.getSignedDocumentsForObjectIdByUriMechanism();
                for (DSSDocument document : documentList) {
                    try (InputStream is = document.openStream()) {
                        digestCalculator.update(is);
                    }
                }
                break;
            default:
                throw new DSSException(String.format("Unsupported SigDMechanism '%s' has been found!", sigDMechanism));
        }
    }

    private CBORByteString getSigDReferencedOctets( SigDMechanism sigDMechanism) {
        switch (sigDMechanism) {
            case OBJECT_ID_BY_URI:
            case OBJECT_ID_BY_URI_HASH:
                List<DSSDocument> documentList = signature.getSignedDocumentsForObjectIdByUriMechanism();
                byte[] documentOctets = CBAdESUtils.concatenateDSSDocuments(documentList);
                return new CBORByteString(documentOctets);
            default:
                throw new DSSException(String.format("Unsupported SigDMechanism '%s' has been found!", sigDMechanism));
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
             * 5.3.5.3	Computation of message-imprint for arcTst
             * For computing the input to the message imprint computation, indicated in step 2) in clause 5.3.5.2,
             * the steps listed below shall be performed:
             *
             *  1) Initialize an empty CBOR array.
             */
            final CBORArray array = new CBORArray();

            /*
             * 2) Add a context text string, whose value shall be either:
             *  - "Signature", if the CB-AdES signature is built on the COSE_Sign structure defined in
             *    IETF RFC 9052 [2], or
             *  - "Signature1", if the CB-AdES signature is built on the COSE_Sign1 structure defined
             *    in IETF RFC 9052 [2], or
             *  - The context text string corresponding to the structure of the CB-AdES signature if it is
             *    a counter signature, as specified in clause 3.3 of IETF RFC 9338 [6].
             */
            COSESignatureContext context = cose.getContext();
            array.add(new UnicodeString(cose.getContext().getContext()));

            /*
             * 3) Add the protected header from the body layer, encapsulated in a CBOR byte string.
             * If the body layer does not have the protected header, add a zero-length CBOR byte string.
             */
            COSEProtectedHeader bodyProtectedHeader = cose.getBodyProtectedHeader();
            if (bodyProtectedHeader != null && !bodyProtectedHeader.isEmpty()) {
                array.add(bodyProtectedHeader.getByteString());
            } else {
                array.add(CBORUtils.EMPTY_BYTE_STRING);
            }

            /*
             * 4) If the CB-AdES signature is built on the COSE_Sign structure, add the protected header
             * from the signer layer, encapsulated in a CBOR byte string. If the signer layer does not have
             * any protected header, add a zero-length CBOR byte string.
             */
            if (COSESignatureContext.COSE_SIGN == context) {
                COSEProtectedHeader signerProtectedHeader = cose.getSignerProtectedHeader();
                if (signerProtectedHeader != null && !signerProtectedHeader.isEmpty()) {
                    array.add(signerProtectedHeader.getByteString());
                } else {
                    array.add(CBORUtils.EMPTY_BYTE_STRING);
                }
            }

            /*
             * 5) Add the externally supplied data from the application, encapsulated in a CBOR byte string.
             * If no data is externally supplied to the application, add a zero-length CBOR byte string.
             */
            CBORByteString externallySuppliedData = cose.getExternallySuppliedData();
            if (externallySuppliedData != null) {
                array.add(externallySuppliedData);
            } else {
                array.add(CBORUtils.EMPTY_BYTE_STRING);
            }

            /*
             * 6) If the sigD header parameter is absent, then:
             *  - If the payload field is present, then add the CBOR byte string of the payload field.
             *  - Else if the payload field is absent (COSE Payload is detached, and not explicitly
             *    referenced by the sigD header parameter), then retrieve the bytes of the COSE Payload and
             *    add them encapsulated in a CBOR byte string.
             */
            SigDMechanism sigDMechanism = signature.getSigDMechanism();
            if (sigDMechanism == null) {
                array.add(getPayload());
            }

            /*
             * 7) If the sigD header parameter is present, retrieve the bytes resulting from processing
             * the contents of its pars member as specified in clause 5.2.8.2.2 of the present document,
             * concatenate them, encapsulate them in a CBOR byte string, and add this CBOR byte string.
             */
            else {
                array.add(getSigDReferencedOctets(sigDMechanism));
            }

            // TODO : include otherFields for CounterSignatureV2 ?
//            CBORObject otherFields = cose.getOtherFields();
//            if (context.isCounterSignatureV2() && otherFields != null) {
//                array.add(otherFields);
//            }

            /*
             * 8) Add the CBOR byte string in the signature component.
             */
            array.add(cose.getSignature());

            /*
             * 9) If the CB-AdES signature is built on the COSE_Sign structure, take the elements in
             * the uHeaders header parameter from the signer layer in the order that they appear within
             * uHeaders, and add them to the CBOR array. If the signer layer does not have the uHeaders
             * header parameter, add a zero-length CBOR byte string.
             *
             * 10) Else if the CB-AdES signature is built on the COSE_Sign1 structure, take the elements
             * in the uHeaders header parameter from the body layer in the order that they appear within
             * uHeaders and add them to the CBOR array. If the body layer does not have the uHeaders
             * header parameter, add a zero-length CBOR byte string.
             */
            CBAdESUHeaders uHeaders = signature.getUHeaders();
            for (CBAdESUHeadersComponent uHeaderComponent : uHeaders.getAttributes()) {
                if (timestampAttribute != null && timestampAttribute.equals(uHeaderComponent)) {
                    // the timestamp is reached, stop the iteration
                    break;
                }
                array.add(uHeaderComponent.getComponent());
            }

            byte[] serializedCborObject = CBORUtils.serializeCborObject(array);
            if (LOG.isTraceEnabled()) {
                LOG.trace("The 'arcTst' timestamp message-imprint : {}", Utils.toBase64(serializedCborObject));
            }
            byte[] digestValue = DSSUtils.digest(digestAlgorithm, serializedCborObject);
            return new DSSMessageDigest(digestAlgorithm, digestValue);

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
    
}
