package eu.europa.esig.dss.cbades.requirements;

import eu.europa.esig.dss.cbades.cbor.CBORMap;
import eu.europa.esig.dss.cbades.signature.CBAdESSignatureParameters;
import eu.europa.esig.dss.enumerations.COSEStructureType;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.SignatureLevel;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CBAdESBaselineBCoseSignTest extends AbstractCBAdESCoseSignRequirementsCheck {

    @Override
    protected CBAdESSignatureParameters getSignatureParameters() {
        CBAdESSignatureParameters signatureParameters = super.getSignatureParameters();
        signatureParameters.setCoseStructureType(COSEStructureType.COSE_SIGN);
        signatureParameters.setSignatureLevel(SignatureLevel.CB_AdES_BASELINE_B);
        signatureParameters.setSigningCertificateDigestMethod(DigestAlgorithm.SHA512);
        return signatureParameters;
    }

    @Override
    protected void checkUnprotectedHeader(CBORMap unprotectedHeaderMap) {
        assertNotNull(unprotectedHeaderMap);
        assertTrue(unprotectedHeaderMap.isEmpty());
    }

}
