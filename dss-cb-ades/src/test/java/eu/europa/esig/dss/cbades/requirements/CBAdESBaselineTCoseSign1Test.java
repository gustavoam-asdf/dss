package eu.europa.esig.dss.cbades.requirements;

import eu.europa.esig.dss.cbades.COSEHeaderParameters;
import eu.europa.esig.dss.cbades.cbor.CBORMap;
import eu.europa.esig.dss.cbades.cbor.CBORObject;
import eu.europa.esig.dss.cbades.signature.CBAdESSignatureParameters;
import eu.europa.esig.dss.enumerations.COSEStructureType;
import eu.europa.esig.dss.enumerations.SignatureLevel;

import static org.junit.jupiter.api.Assertions.assertNull;

class CBAdESBaselineTCoseSign1Test extends AbstractCBAdESCoseSign1RequirementsCheck {

    @Override
    protected CBAdESSignatureParameters getSignatureParameters() {
        CBAdESSignatureParameters signatureParameters = super.getSignatureParameters();
        signatureParameters.setCoseStructureType(COSEStructureType.COSE_SIGN1);
        signatureParameters.setSignatureLevel(SignatureLevel.CB_AdES_BASELINE_T);
        return signatureParameters;
    }

    @Override
    protected void checkValidationData(CBORMap unprotectedHeaderMap) {
        CBORObject valData = getUHeadersElement(unprotectedHeaderMap, COSEHeaderParameters.VAL_DATA.cbor());
        assertNull(valData);
    }

    @Override
    protected void checkArchiveTimestamp(CBORMap unprotectedHeaderMap) {
        CBORObject arcTst = getUHeadersElement(unprotectedHeaderMap, COSEHeaderParameters.ARC_TST.cbor());
        assertNull(arcTst);
    }

}
