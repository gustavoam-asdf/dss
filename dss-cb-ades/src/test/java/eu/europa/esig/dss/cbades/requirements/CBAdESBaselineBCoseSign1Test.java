package eu.europa.esig.dss.cbades.requirements;

import eu.europa.esig.dss.cbades.cbor.CBORMap;
import eu.europa.esig.dss.cbades.signature.CBAdESSignatureParameters;
import eu.europa.esig.dss.enumerations.COSEStructureType;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.SignatureLevel;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CBAdESBaselineBCoseSign1Test extends AbstractCBAdESCoseSign1RequirementsCheck {

    @Override
    protected CBAdESSignatureParameters getSignatureParameters() {
        CBAdESSignatureParameters signatureParameters = super.getSignatureParameters();
        signatureParameters.setCoseStructureType(COSEStructureType.COSE_SIGN1);
        signatureParameters.setSignatureLevel(SignatureLevel.CB_AdES_BASELINE_B);
        signatureParameters.setSigningCertificateDigestMethod(DigestAlgorithm.SHA512);
        return signatureParameters;
    }

    @Override
    protected void checkUnprotectedHeader(CBORMap unprotectedHeaderMap) throws Exception {
        assertNotNull(unprotectedHeaderMap);
        assertTrue(unprotectedHeaderMap.isEmpty());
    }

}
