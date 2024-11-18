package eu.europa.esig.dss.cbades.signature.clearUHeaders;

import eu.europa.esig.dss.cbades.signature.CBAdESLevelTCoseSignTest;
import eu.europa.esig.dss.cbades.signature.CBAdESSignatureParameters;

class CBAdESLevelTCoseSignClearEtsiUTest extends CBAdESLevelTCoseSignTest {

    @Override
    protected CBAdESSignatureParameters getSignatureParameters() {
        CBAdESSignatureParameters signatureParameters = super.getSignatureParameters();
        signatureParameters.setCborBtsrWrappedComponents(false);
        return signatureParameters;
    }

}