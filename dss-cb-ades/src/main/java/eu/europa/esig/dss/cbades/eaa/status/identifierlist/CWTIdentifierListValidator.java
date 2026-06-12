package eu.europa.esig.dss.cbades.eaa.status.identifierlist;

import eu.europa.esig.dss.cbades.COSEParser;
import eu.europa.esig.dss.cbades.cbor.CBORByteString;
import eu.europa.esig.dss.cbades.cbor.CBORMap;
import eu.europa.esig.dss.cbades.cbor.CBORObject;
import eu.europa.esig.dss.cbades.validation.CBAdESSignature;
import eu.europa.esig.dss.cbades.validation.COSEDocumentAnalyzer;
import eu.europa.esig.dss.enumerations.COSESignatureType;
import eu.europa.esig.dss.enumerations.EAAStatus;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.spi.eaa.EAAStatusToken;
import eu.europa.esig.dss.spi.eaa.StatusTokenBinary;
import eu.europa.esig.dss.spi.eaa.status.identifierlist.EAAIdentifierListToken;
import eu.europa.esig.dss.spi.eaa.status.identifierlist.IdentifierListPayload;
import eu.europa.esig.dss.spi.eaa.status.identifierlist.IdentifierListValidator;
import eu.europa.esig.dss.spi.exception.IllegalInputException;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import eu.europa.esig.dss.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Validates an Identifier List token as defined in ISO/IEC 18013-5
 *
 */
public class CWTIdentifierListValidator implements IdentifierListValidator {

    private static final Logger LOG = LoggerFactory.getLogger(CWTIdentifierListValidator.class);
    
    /** Binaries of the retrieved status list */
    protected byte[] identifierListDocument;

    /** Cached instance of a signature used to sign the token */
    private AdvancedSignature tokenSignature;

    /**
     * Empty constructor
     */
    public CWTIdentifierListValidator() {
        // empty
    }

    /**
     * Empty constructor
     */
    public CWTIdentifierListValidator(final byte[] identifierListDocument) {
        this.identifierListDocument = identifierListDocument;
    }

    @Override
    public boolean isSupported(byte[] identifierListDocument) {
        return COSEParser.isSupported(identifierListDocument);
    }

    /**
     * Builds a signature of the token
     *
     * @return {@link AdvancedSignature}
     */
    protected AdvancedSignature buildTokenSignature() {
        COSEDocumentAnalyzer documentAnalyzer = new COSEDocumentAnalyzer(new InMemoryDocument(identifierListDocument));
        List<AdvancedSignature> signatures = documentAnalyzer.getSignatures();
        if (Utils.collectionSize(signatures) == 1) {
            CBAdESSignature signature = toCBAdESSignature(signatures.get(0));
            if (COSESignatureType.COSE_SIGN1 != signature.getCOSESignatureType()) {
                LOG.warn("The signature of the CWT-encoded Identifier List shall be a COSE_Sign1 object!");
                return null;
            }
            return signature;

        } else {
            LOG.warn("One and only one signature shall be present within JWT Status List body! " +
                    "Found : {} signatures", Utils.collectionSize(signatures));
        }
        return null;
    }

    /**
     * Gets the representation of the Identifier List Payload signed by the {@code signature}
     *
     * @param signature {@link AdvancedSignature}
     * @return {@link IdentifierListPayload}
     */
    protected IdentifierListPayload getPayload(AdvancedSignature signature) {
        try {
            CBAdESSignature cbadesSignature = toCBAdESSignature(signature);
            CBORObject cborPayload = cbadesSignature.getCoseSignature().getPayload();

            if (!cborPayload.isByteString()) {
                throw new IllegalInputException("COSE payload shall be encoded as a CBOR byte string!");
            }
            try {
                CBORByteString payloadByteString = (CBORByteString) cborPayload;
                CBORMap cborMap = new CBORMap(payloadByteString);
                return new CWTIdentifierListPayload(cborMap);

            } catch (Exception e) {
                throw new IllegalInputException(String.format(
                        "An error occurred on CWT token processing : %s", e.getMessage()), e);
            }

        } catch (Exception e) {
            throw new DSSException(String.format("Unable to extract CWT payload : %s", e.getMessage()));
        }
    }

    private CBAdESSignature toCBAdESSignature(AdvancedSignature signature) {
        if (signature instanceof CBAdESSignature) {
            return (CBAdESSignature) signature;
        } else {
            throw new IllegalStateException("CBAdESSignature is expected!");
        }
    }


    @Override
    public EAAStatusToken getStatusToken(byte[] identifier) {
        Objects.requireNonNull(identifierListDocument, "Identifier List Document cannot be null!");

        /*
         * 8.2. Status List Response
         *
         * The body of such an HTTP response contains the raw Status List Token,
         * that means the binary encoding as defined in Section 9.2.1 of [RFC8392] for
         * a Status List Token in CWT format and the JWS Compact Serialization form for
         * a Status List Token in JWT format.
         */
        AdvancedSignature signature = getTokenSignature();
        if (signature != null) {
            IdentifierListPayload identifierListPayload = getPayload(signature);
            return EAAIdentifierListToken.initBuilder()
                    .setBinary(new StatusTokenBinary(identifierListDocument))
                    .setSignature(signature)
                    .setPayload(identifierListPayload)
                    .setStatus(getEAAStatus(identifierListPayload, identifier))
                    .build();
        }
        return null;
    }

    /**
     * Gets the token signature. If already built, returns the cached value.
     *
     * @return {@link AdvancedSignature}
     */
    protected AdvancedSignature getTokenSignature() {
        if (tokenSignature == null) {
            tokenSignature = buildTokenSignature();
        }
        return tokenSignature;
    }

    /**
     * Gets the EAA Status for the given {@code eaa} based on the information retrieved from {@code identifierListPayload}
     *
     * @param identifierListPayload {@link IdentifierListPayload} of the retrieved token
     * @return {@link EAAStatus}
     */
    protected EAAStatus getEAAStatus(IdentifierListPayload identifierListPayload, byte[] identifier) {
        List<byte[]> identifierListIdentifiers = identifierListPayload.getIdentifierListIdentifiers();
        if (Utils.isCollectionNotEmpty(identifierListIdentifiers)) {
            if (identifierListIdentifiers.stream().anyMatch(i -> Arrays.equals(identifier, i))) {
                return EAAStatus.INVALID;
            }
        }
        return EAAStatus.VALID;
    }
    
}
