package eu.europa.esig.dss.cbades.validation;

import eu.europa.esig.dss.cbades.COSECounterSignature;
import eu.europa.esig.dss.cbades.COSECounterSignatureArray;
import eu.europa.esig.dss.cbades.COSEHeaderParameter;
import eu.europa.esig.dss.cbades.COSESign;
import eu.europa.esig.dss.cbades.COSESignature;
import eu.europa.esig.dss.cbades.COSESignatureContext;
import eu.europa.esig.dss.cbades.COSEStructure;
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
        CBAdESSignature cbadesSignature = (CBAdESSignature) signature;
        CBAdESSignature cbadesMasterSignature = (CBAdESSignature) masterSignature;
        CBAdESUHeadersComponent masterCSigAttribute = cbadesSignature.getMasterCounterSignatureComponent();

        int counter = 0;
        if (masterCSigAttribute != null) {
            for (AdvancedSignature counterSignature : cbadesMasterSignature.getCounterSignatures()) {
                CBAdESSignature jadesCounterSignature = (CBAdESSignature) counterSignature;
                if (masterCSigAttribute.hashCode() == jadesCounterSignature.getMasterCounterSignatureComponent().hashCode()) {
                    break;
                }
                ++counter;
            }
        }

        return counter;
    }

    @Override
    protected Integer getSignaturePosition() {
        CBAdESSignature cbadesSignature = (CBAdESSignature) signature;
        CBORSignature cose = cbadesSignature.getCoseSignature();
        COSEStructure coseStructure = cose.getCoseSignStructure();
        COSEStructure currentSigner = cose.getSignerSignature();

        int counter = 0;
        if (coseStructure != null) {
            if (COSESignatureContext.COSE_SIGN == coseStructure.getContext()) {
                COSESign coseSign = (COSESign) coseStructure;
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

    @Override
    protected String getPositionId() {
        final String positionId = super.getPositionId();
        if (!signature.isCounterSignature()) {
            return positionId;
        }

        CBAdESSignature cbadesSignature = (CBAdESSignature) signature;

        StringBuilder stringBuilder = new StringBuilder(positionId);
        stringBuilder.append(cbadesSignature.getCOSESignatureContext().getLabel());

        if (cbadesSignature.getMasterCounterSignatureComponent() != null) {
            stringBuilder.append(COSEHeaderParameter.U_HEADERS);
        }

        COSEStructure coseSignStructure = cbadesSignature.getCoseSignature().getCoseSignStructure();
        if (coseSignStructure instanceof COSECounterSignatureArray) {
            COSECounterSignatureArray coseCounterSignatureArray = (COSECounterSignatureArray) coseSignStructure;
            int counter = 0;
            for (COSECounterSignature coseCounterSignature : coseCounterSignatureArray.getCoseCounterSignatureList()) {
                if (cbadesSignature.getCoseSignature().getSignerSignature().hashCode() == coseCounterSignature.hashCode()) {
                    break;
                }
                ++counter;
            }
            stringBuilder.append(counter);
        }
        return stringBuilder.toString();
    }

}
