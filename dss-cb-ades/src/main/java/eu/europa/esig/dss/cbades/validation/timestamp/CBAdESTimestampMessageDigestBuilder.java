package eu.europa.esig.dss.cbades.validation.timestamp;

import eu.europa.esig.dss.cbades.validation.CBAdESAttribute;
import eu.europa.esig.dss.cbades.validation.CBAdESSignature;
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

    /** String used to print the computed message-imprint */
    private static final String MESSAGE_IMPRINT_MESSAGE = "The '{}' timestamp message-imprint : {}";

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
        return null;
    }
    
}
