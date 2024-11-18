package eu.europa.esig.dss.cbades.signature.clearUHeaders;

import eu.europa.esig.dss.cbades.signature.CBAdESLevelTCoseSign1Test;
import eu.europa.esig.dss.cbades.signature.CBAdESSignatureParameters;

class CBAdESLevelTCoseSign1ClearEtsiUTest extends CBAdESLevelTCoseSign1Test {

    @Override
    protected CBAdESSignatureParameters getSignatureParameters() {
        CBAdESSignatureParameters signatureParameters = super.getSignatureParameters();
        signatureParameters.setCborBtsrWrappedComponents(false);
        return signatureParameters;
    }

}
