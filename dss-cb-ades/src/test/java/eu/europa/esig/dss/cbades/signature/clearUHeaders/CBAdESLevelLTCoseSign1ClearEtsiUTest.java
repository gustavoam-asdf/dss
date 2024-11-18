package eu.europa.esig.dss.cbades.signature.clearUHeaders;

import eu.europa.esig.dss.cbades.signature.CBAdESLevelLTCoseSign1Test;
import eu.europa.esig.dss.cbades.signature.CBAdESSignatureParameters;

class CBAdESLevelLTCoseSign1ClearEtsiUTest extends CBAdESLevelLTCoseSign1Test {

    @Override
    protected CBAdESSignatureParameters getSignatureParameters() {
        CBAdESSignatureParameters signatureParameters = super.getSignatureParameters();
        signatureParameters.setCborBtsrWrappedComponents(false);
        return signatureParameters;
    }

}
