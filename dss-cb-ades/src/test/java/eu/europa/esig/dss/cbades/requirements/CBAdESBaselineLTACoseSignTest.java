package eu.europa.esig.dss.cbades.requirements;

import eu.europa.esig.dss.cbades.signature.CBAdESSignatureParameters;
import eu.europa.esig.dss.enumerations.COSEStructureType;
import eu.europa.esig.dss.enumerations.SignatureLevel;

class CBAdESBaselineLTACoseSignTest extends AbstractCBAdESCoseSignRequirementsCheck {

    @Override
    protected CBAdESSignatureParameters getSignatureParameters() {
        CBAdESSignatureParameters signatureParameters = super.getSignatureParameters();
        signatureParameters.setCoseStructureType(COSEStructureType.COSE_SIGN);
        signatureParameters.setSignatureLevel(SignatureLevel.CB_AdES_BASELINE_LTA);
        return signatureParameters;
    }

}
