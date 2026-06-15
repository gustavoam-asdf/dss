package eu.europa.esig.dss.cbades.extension;

import eu.europa.esig.dss.enumerations.SignatureLevel;

class CBAdESExtensionTToLTTest extends AbstractCBAdESTestExtension {

    @Override
    protected SignatureLevel getOriginalSignatureLevel() {
        return SignatureLevel.CB_AdES_BASELINE_T;
    }

    @Override
    protected SignatureLevel getFinalSignatureLevel() {
        return SignatureLevel.CB_AdES_BASELINE_LT;
    }

}
