package eu.europa.esig.dss.cbades.requirements;

import co.nstant.in.cbor.CborException;
import eu.europa.esig.dss.cbades.cbor.CBORArray;
import eu.europa.esig.dss.cbades.cbor.CBORByteString;
import eu.europa.esig.dss.cbades.cbor.CBORMap;
import eu.europa.esig.dss.cbades.cbor.CBORObject;
import eu.europa.esig.dss.cbades.cbor.CBORUtils;
import eu.europa.esig.dss.utils.Utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public abstract class AbstractCBAdESCoseSignRequirementsCheck extends AbstractCBAdESRequirementsCheck {

    @Override
    protected void onDocumentSigned(byte[] byteArray) {
        super.onDocumentSigned(byteArray);

        try {
            CBORByteString protectedHeader = getBodyProtectedHeader(byteArray);
            checkBodyProtectedHeader(protectedHeader);

            CBORMap unprotectedHeader = getBodyUnprotectedHeader(byteArray);
            checkBodyUnprotectedHeader(unprotectedHeader);

        } catch (Exception e) {
            fail(e);
        }
    }

    @Override
    protected CBORByteString getPayload(byte[] byteArray) throws Exception {
        CBORArray cose = getCose(byteArray);
        CBORObject payloadObject = cose.getItems().get(2);
        assertTrue(payloadObject.isByteString());

        return (CBORByteString) payloadObject;
    }

    protected CBORByteString getBodyProtectedHeader(byte[] byteArray) throws Exception {
        CBORArray cose = getCose(byteArray);
        CBORObject protectedHeader = cose.getItems().get(0);
        assertTrue(protectedHeader.isByteString());

        return (CBORByteString) protectedHeader;
    }

    @Override
    protected CBORByteString getProtectedHeader(byte[] byteArray) throws Exception {
        CBORArray cose = getCose(byteArray);
        CBORArray coseSignature = getCoseSignature(cose);
        CBORObject protectedHeader = coseSignature.getItems().get(0);
        assertTrue(protectedHeader.isByteString());

        return (CBORByteString) protectedHeader;
    }

    @Override
    protected CBORByteString getSignatureValue(byte[] byteArray) throws Exception {
        CBORArray cose = getCose(byteArray);
        CBORArray coseSignature = getCoseSignature(cose);
        CBORObject signature = coseSignature.getItems().get(2);
        assertTrue(signature.isByteString());

        return (CBORByteString) signature;
    }

    protected CBORMap getBodyUnprotectedHeader(byte[] byteArray) throws Exception {
        CBORArray cose = getCose(byteArray);
        CBORObject unprotectedHeader = cose.getItems().get(1);
        assertTrue(unprotectedHeader.isMap());

        return (CBORMap) unprotectedHeader;
    }

    @Override
    protected CBORMap getUnprotectedHeader(byte[] byteArray) throws Exception {
        CBORArray cose = getCose(byteArray);
        CBORArray coseSignature = getCoseSignature(cose);
        CBORObject unprotectedHeader = coseSignature.getItems().get(1);
        assertTrue(unprotectedHeader.isMap());

        return (CBORMap) unprotectedHeader;
    }

    private CBORArray getCose(byte[] byteArray) throws Exception {
        CBORObject cborObject = CBORUtils.parseCbor(byteArray);
        assertTrue(cborObject.isArray());
        return (CBORArray) cborObject;
    }

    private CBORArray getCoseSignature(CBORArray cose) {
        CBORObject signatures = cose.getItems().get(3);
        assertTrue(signatures.isArray());

        CBORArray signaturesArray = (CBORArray) signatures;
        assertEquals(1, signaturesArray.getSize());

        CBORArray signature = signaturesArray.getAsArray(0);
        assertNotNull(signature);
        return signature;
    }

    protected void checkBodyProtectedHeader(CBORByteString bodyProtectedHeader) throws CborException {
        assertNotNull(bodyProtectedHeader);
        assertTrue(Utils.isArrayNotEmpty(bodyProtectedHeader.getBytes()));

        CBORObject protectedHeaderObject = CBORUtils.parseCbor(bodyProtectedHeader.getBytes());
        assertTrue(protectedHeaderObject.isMap());

        CBORMap protectedHeaderMap = (CBORMap) protectedHeaderObject;
        assertTrue(protectedHeaderMap.isEmpty());
    }

    protected void checkBodyUnprotectedHeader(CBORMap bodyUnprotectedHeader) {
        assertNotNull(bodyUnprotectedHeader);
        assertTrue(bodyUnprotectedHeader.isEmpty());
    }

}
