package eu.europa.esig.dss.eaa.revocation.cwt.validation.statuslist;

import eu.europa.esig.dss.cbades.COSEParser;
import eu.europa.esig.dss.cbades.cbor.CBORByteString;
import eu.europa.esig.dss.cbades.cbor.CBORMap;
import eu.europa.esig.dss.cbades.cbor.CBORObject;
import eu.europa.esig.dss.eaa.revocation.cwt.model.statuslist.CWTStatusListPayload;
import eu.europa.esig.dss.cbades.validation.CBAdESSignature;
import eu.europa.esig.dss.cbades.validation.COSEDocumentAnalyzer;
import eu.europa.esig.dss.eaa.revocation.validation.statuslist.AbstractEAAStatusListValidator;
import eu.europa.esig.dss.enumerations.COSESignatureType;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.spi.exception.IllegalInputException;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import eu.europa.esig.dss.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Performs validation of the Token Status List encoded as RFC 8392 CWT
 *
 */
public class CWTStatusListValidator extends AbstractEAAStatusListValidator {

    private static final Logger LOG = LoggerFactory.getLogger(CWTStatusListValidator.class);

    /**
     * Empty constructor
     */
    public CWTStatusListValidator() {
        super();
    }

    /**
     * Constructor with the status list
     *
     * @param statusListDocument byte array of the status list document
     */
    public CWTStatusListValidator(final byte[] statusListDocument) {
        super(statusListDocument);
    }

    @Override
    public boolean isSupported(byte[] statusListDocument) {
        return COSEParser.isSupported(statusListDocument);
    }

    @Override
    protected AdvancedSignature buildTokenSignature() {
        COSEDocumentAnalyzer documentAnalyzer = new COSEDocumentAnalyzer(new InMemoryDocument(statusListDocument));
        List<AdvancedSignature> signatures = documentAnalyzer.getSignatures();
        if (Utils.collectionSize(signatures) == 1) {
            CBAdESSignature signature = toCBAdESSignature(signatures.get(0));
            if (COSESignatureType.COSE_SIGN1 != signature.getCOSESignatureType() || !signature.isTagged()) {
                // NOTE: COSE_Mac0_Tagged is not supported by the implementation
                LOG.warn("The signature of the CWT-encoded Token Status List shall the tagged COSE_Sign1_Tagged (18)!");
                return null;
            }
            return signature;

        } else {
            LOG.warn("One and only one signature shall be present within JWT Status List body! " +
                    "Found : {} signatures", Utils.collectionSize(signatures));
        }
        return null;
    }

    @Override
    protected CWTStatusListPayload getPayload(AdvancedSignature signature) {
        try {
            CBAdESSignature cbadesSignature = toCBAdESSignature(signature);
            CBORObject cborPayload = cbadesSignature.getCoseSignature().getPayload();

            if (!cborPayload.isByteString()) {
                throw new IllegalInputException("COSE payload shall be encoded as a CBOR byte string!");
            }
            try {
                CBORByteString payloadByteString = (CBORByteString) cborPayload;
                CBORMap cborMap = new CBORMap(payloadByteString);
                return new CWTStatusListPayload(cborMap);

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

}
