package eu.europa.esig.dss.cbades.extension;

import eu.europa.esig.dss.enumerations.SignatureLevel;

public class CBAdESExtensionBToTTest extends AbstractCBAdESTestExtension {

    @Override
    protected SignatureLevel getOriginalSignatureLevel() {
        return SignatureLevel.CB_AdES_BASELINE_B;
    }

    @Override
    protected SignatureLevel getFinalSignatureLevel() {
        return SignatureLevel.CB_AdES_BASELINE_T;
    }

}
