package eu.europa.esig.dss.cbades.requirements;

import eu.europa.esig.dss.cbades.COSEHeaderParameter;
import eu.europa.esig.dss.cbades.cbor.CBORMap;
import eu.europa.esig.dss.cbades.cbor.CBORObject;
import eu.europa.esig.dss.cbades.signature.CBAdESSignatureParameters;
import eu.europa.esig.dss.enumerations.COSEStructureType;
import eu.europa.esig.dss.enumerations.SignatureLevel;

import static org.junit.jupiter.api.Assertions.assertNull;

class CBAdESBaselineLTCoseSignTest extends AbstractCBAdESCoseSignRequirementsCheck {

    @Override
    protected CBAdESSignatureParameters getSignatureParameters() {
        CBAdESSignatureParameters signatureParameters = super.getSignatureParameters();
        signatureParameters.setCoseStructureType(COSEStructureType.COSE_SIGN);
        signatureParameters.setSignatureLevel(SignatureLevel.CB_AdES_BASELINE_LT);
        return signatureParameters;
    }

    @Override
    protected void checkArchiveTimestamp(CBORMap unprotectedHeaderMap) {
        CBORObject arcTst = getUHeadersElement(unprotectedHeaderMap, COSEHeaderParameter.ARC_TST.cbor());
        assertNull(arcTst);
    }

}
