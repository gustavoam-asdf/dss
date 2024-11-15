package eu.europa.esig.dss.cbades.requirements;

import eu.europa.esig.dss.cbades.cbor.CBORArray;
import eu.europa.esig.dss.cbades.cbor.CBORByteString;
import eu.europa.esig.dss.cbades.cbor.CBORMap;
import eu.europa.esig.dss.cbades.cbor.CBORObject;
import eu.europa.esig.dss.cbades.cbor.CBORUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class AbstractCBAdESCoseSign1RequirementsCheck extends AbstractCBAdESRequirementsCheck {
    
    @Override
    protected CBORByteString getPayload(byte[] byteArray) throws Exception {
        CBORArray cose = getCose(byteArray);
        CBORObject payloadObject = cose.getItems().get(2);
        assertTrue(payloadObject.isByteString());

        return (CBORByteString) payloadObject;
    }

    @Override
    protected CBORByteString getProtectedHeader(byte[] byteArray) throws Exception {
        CBORArray cose = getCose(byteArray);
        CBORObject protectedHeader = cose.getItems().get(0);
        assertTrue(protectedHeader.isByteString());

        return (CBORByteString) protectedHeader;
    }

    @Override
    protected CBORByteString getSignatureValue(byte[] byteArray) throws Exception {
        CBORArray cose = getCose(byteArray);
        CBORObject signature = cose.getItems().get(3);
        assertTrue(signature.isByteString());

        return (CBORByteString) signature;
    }

    @Override
    protected CBORMap getUnprotectedHeader(byte[] byteArray) throws Exception {
        CBORArray cose = getCose(byteArray);
        CBORObject unprotectedHeader = cose.getItems().get(1);
        assertTrue(unprotectedHeader.isMap());

        return (CBORMap) unprotectedHeader;
    }

    private CBORArray getCose(byte[] byteArray) {
        CBORObject cborObject = CBORUtils.parseCbor(byteArray);
        assertTrue(cborObject.isArray());
        CBORArray cborArray = (CBORArray) cborObject;
        assertEquals(4, cborArray.getSize());
        return cborArray;
    }
    
}
