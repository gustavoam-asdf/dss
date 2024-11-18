package eu.europa.esig.dss.cbades.signature.clearUHeaders;

import eu.europa.esig.dss.cbades.signature.CBAdESLevelLTCoseSignTest;
import eu.europa.esig.dss.cbades.signature.CBAdESSignatureParameters;

class CBAdESLevelLTCoseSignClearEtsiUTest extends CBAdESLevelLTCoseSignTest {

    @Override
    protected CBAdESSignatureParameters getSignatureParameters() {
        CBAdESSignatureParameters signatureParameters = super.getSignatureParameters();
        signatureParameters.setCborBtsrWrappedComponents(false);
        return signatureParameters;
    }

}