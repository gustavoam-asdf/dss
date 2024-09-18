package eu.europa.esig.dss.cbades.validation;

import eu.europa.esig.dss.cbades.COSESign;
import eu.europa.esig.dss.cbades.COSESignStructure;
import eu.europa.esig.dss.cbades.COSESignature;
import eu.europa.esig.dss.cbades.COSESignatureContext;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import eu.europa.esig.dss.spi.signature.identifier.AbstractSignatureIdentifierBuilder;

/**
 * Build the DSS identifier for a CB-AdES signature
 * 
 */
public class CBAdESSignatureIdentifierBuilder extends AbstractSignatureIdentifierBuilder {

    /**
     * Default constructor
     *
     * @param signature {@link CBAdESSignature} to get an identifier for
     */
    public CBAdESSignatureIdentifierBuilder(CBAdESSignature signature) {
        super(signature);
    }

    @Override
    protected Integer getCounterSignaturePosition(AdvancedSignature masterSignature) {
        // TODO : to be implemented
        return 0;
    }

    @Override
    protected Integer getSignaturePosition() {
        CBAdESSignature cbadesSignature = (CBAdESSignature) signature;
        CBORSignature cose = cbadesSignature.getCoseSignature();
        COSESignStructure coseSignStructure = cose.getCoseSignStructure();
        COSESignature currentSigner = cose.getSignerSignature();

        int counter = 0;
        if (coseSignStructure != null) {
            // TODO : counter-signatures ?
            if (COSESignatureContext.COSE_SIGN == coseSignStructure.getContext()) {
                COSESign coseSign = (COSESign) coseSignStructure;
                for (COSESignature coseSignature : coseSign.getSignatures()) {
                    if (currentSigner == coseSignature) {
                        break;
                    }
                    ++counter;
                }
            }
            // COSE_Sign1 has only one signature
        }

        return counter;
    }

}
