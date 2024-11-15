package eu.europa.esig.dss.cbades.signature.clearUHeaders;

import eu.europa.esig.dss.cbades.COSEConstants;
import eu.europa.esig.dss.cbades.COSECounterSignStructure;
import eu.europa.esig.dss.cbades.COSECounterSignature;
import eu.europa.esig.dss.cbades.COSECounterSignatureParser;
import eu.europa.esig.dss.cbades.COSEParser;
import eu.europa.esig.dss.cbades.COSEProtectedHeader;
import eu.europa.esig.dss.cbades.COSESign1;
import eu.europa.esig.dss.cbades.COSESignStructure;
import eu.europa.esig.dss.cbades.COSESignatureContext;
import eu.europa.esig.dss.cbades.COSEUnprotectedHeader;
import eu.europa.esig.dss.cbades.cbor.CBORArray;
import eu.europa.esig.dss.cbades.cbor.CBORMap;
import eu.europa.esig.dss.cbades.cbor.CBORObject;
import eu.europa.esig.dss.cbades.signature.CBAdESCounterSignatureParameters;
import eu.europa.esig.dss.cbades.signature.CBAdESLevelBCounterSignatureCoseSign1Test;
import eu.europa.esig.dss.model.InMemoryDocument;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CBAdESLevelBCounterSignatureCoseSign1ClearUHeadersTest extends CBAdESLevelBCounterSignatureCoseSign1Test {

    @Override
    protected CBAdESCounterSignatureParameters getCounterSignatureParameters() {
        CBAdESCounterSignatureParameters counterSignatureParameters = super.getCounterSignatureParameters();
        counterSignatureParameters.setCborBtsrWrappedComponents(false);
        return counterSignatureParameters;
    }

    @Override
    protected void onDocumentSigned(byte[] byteArray) {
        InMemoryDocument signedDocument = new InMemoryDocument(byteArray);
        assertTrue(COSEParser.isSupported(signedDocument));

        COSEParser coseParser = COSEParser.fromDocument(signedDocument);
        COSESignStructure coseSignStructure = coseParser.parse();
        assertNotNull(coseSignStructure);
        checkCOSESignStructure(coseSignStructure);

        assertEquals(COSESignatureContext.COSE_SIGN1, coseSignStructure.getContext());
        COSESign1 coseSign1 = assertInstanceOf(COSESign1.class, coseSignStructure);

        COSEUnprotectedHeader unprotectedHeader = coseSign1.getUnprotectedHeader();
        assertNotNull(unprotectedHeader);
        assertFalse(unprotectedHeader.isEmpty());
        assertEquals(1, unprotectedHeader.getSize());

        CBORArray uHeaders = unprotectedHeader.getAsArray(COSEConstants.U_HEADERS);
        assertNotNull(uHeaders);
        assertEquals(1, uHeaders.getSize());

        List<CBORObject> items = uHeaders.getItems();
        assertEquals(1, items.size());

        CBORObject countersignatureObject = items.get(0);
        assertTrue(countersignatureObject.isMap());
        CBORMap cborMap = assertInstanceOf(CBORMap.class, countersignatureObject);
        assertEquals(1, cborMap.getSize());

        CBORObject counterSignatureV2Object = cborMap.getHeader(COSEConstants.COUNTER_SIGNATURE_V2);
        assertNotNull(counterSignatureV2Object);
        assertTrue(counterSignatureV2Object.isArray());

        COSECounterSignStructure coseCounterSignStructure = COSECounterSignatureParser.fromCBORObject(counterSignatureV2Object)
                .setContext(COSESignatureContext.COSE_COUNTER_SIGNATURE_V2)
                .setMasterSignature(coseSign1)
                .parse();
        assertNotNull(coseCounterSignStructure);
        assertEquals(COSESignatureContext.COSE_COUNTER_SIGNATURE_V2, coseCounterSignStructure.getContext());

        COSECounterSignature coseCounterSignature = assertInstanceOf(COSECounterSignature.class, coseCounterSignStructure);
        assertNotNull(coseCounterSignature.getProtectedHeader());
        assertNotNull(coseCounterSignature.getUnprotectedHeader());
        assertNotNull(coseCounterSignature.getSignature());
        assertNotNull(coseCounterSignature.getMasterSignature());

        COSEProtectedHeader protectedHeader = coseCounterSignature.getProtectedHeader();

        CBORObject cty = protectedHeader.getHeader(COSEConstants.CONTENT_TYPE);
        assertNull(cty);
    }

}
