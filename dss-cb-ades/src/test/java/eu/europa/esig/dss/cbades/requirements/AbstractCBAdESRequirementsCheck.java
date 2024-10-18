package eu.europa.esig.dss.cbades.requirements;

import eu.europa.esig.dss.cbades.cbor.CBORArray;
import eu.europa.esig.dss.cbades.cbor.CBORByteString;
import eu.europa.esig.dss.cbades.cbor.CBORMap;
import eu.europa.esig.dss.cbades.cbor.CBORObject;
import eu.europa.esig.dss.cbades.cbor.CBORSimpleObject;
import eu.europa.esig.dss.cbades.cbor.CBORUtils;
import eu.europa.esig.dss.cbades.signature.AbstractCBAdESTestSignature;
import eu.europa.esig.dss.cbades.signature.CBAdESService;
import eu.europa.esig.dss.cbades.signature.CBAdESSignatureParameters;
import eu.europa.esig.dss.cbades.signature.CBAdESTimestampParameters;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.MimeType;
import eu.europa.esig.dss.enumerations.SigDMechanism;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.model.x509.CertificateToken;
import eu.europa.esig.dss.signature.DocumentSignatureService;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.utils.Utils;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public abstract class AbstractCBAdESRequirementsCheck extends AbstractCBAdESTestSignature {

    private CBAdESService service;
    private DSSDocument documentToSign;
    private CBAdESSignatureParameters signatureParameters;

    @BeforeEach
    public void init() throws Exception {
        service = new CBAdESService(getCompleteCertificateVerifier());
        service.setTspSource(getGoodTsa());

        documentToSign = new InMemoryDocument("Hello world!".getBytes(), "doc.txt");

        signatureParameters = new CBAdESSignatureParameters();
        signatureParameters.bLevel().setSigningDate(new Date());
        signatureParameters.setSigningCertificate(getSigningCert());
        signatureParameters.setCertificateChain(getCertificateChain());
        signatureParameters.setSignaturePackaging(SignaturePackaging.ENVELOPING);
    }

    @Override
    protected void onDocumentSigned(byte[] byteArray)  {
        super.onDocumentSigned(byteArray);

        try {
            CBORByteString payload = getPayload(byteArray);
            checkPayload(payload);

            CBORByteString protectedHeader = getProtectedHeader(byteArray);
            checkProtectedHeader(protectedHeader);

            CBORByteString signatureValue = getSignatureValue(byteArray);
            checkSignatureValue(signatureValue);

            CBORMap unprotectedHeader = getUnprotectedHeader(byteArray);
            checkUnprotectedHeader(unprotectedHeader);

        } catch (Exception e) {
            fail(e);
        }
    }

    protected abstract CBORByteString getPayload(byte[] byteArray) throws Exception;

    protected abstract CBORByteString getProtectedHeader(byte[] byteArray) throws Exception;

    protected abstract CBORByteString getSignatureValue(byte[] byteArray) throws Exception;

    protected abstract CBORMap getUnprotectedHeader(byte[] byteArray) throws Exception;

    protected void checkPayload(CBORByteString payload) {
        assertNotNull(payload);
        assertTrue(Utils.isArrayNotEmpty(payload.getBytes()));
    }

    protected void checkProtectedHeader(CBORByteString protectedHeader) {
        assertNotNull(protectedHeader);
        assertTrue(Utils.isArrayNotEmpty(protectedHeader.getBytes()));

        CBORObject protectedHeaderObject = CBORUtils.parseCbor(protectedHeader.getBytes());
        assertTrue(protectedHeaderObject.isMap());

        CBORMap protectedHeaderMap = (CBORMap) protectedHeaderObject;
        assertFalse(protectedHeaderMap.isEmpty());

        checkSigningCertificate(protectedHeaderMap);
        checkCertificateChain(protectedHeaderMap);
        checkSigningTime(protectedHeaderMap);
        checkContentType(protectedHeaderMap);
        checkCrit(protectedHeaderMap);
    }

    protected void checkSigningCertificate(CBORMap protectedHeaderMap) {
        CBORArray x5t = protectedHeaderMap.getAsArray(34L);
        CBORArray x5ts = protectedHeaderMap.getAsArray(101L);
        assertTrue(x5t != null ^ x5ts != null);

        if (x5t != null) {
            Long hashAlg = x5t.getAsLong(0);
            assertNotNull(hashAlg);
            assertNotNull(DigestAlgorithm.forCOSE(hashAlg));

            byte[] hashVal = x5t.getAsBinaries(1);
            assertNotNull(hashVal);
            assertTrue(Utils.isArrayNotEmpty(hashVal));
        }

        if (x5ts != null) {
            assertFalse(x5ts.isEmpty());

            for (CBORObject cborObject : x5ts.getItems()) {
                assertTrue(cborObject.isArray());
                CBORArray x5tItem = (CBORArray) cborObject;

                Long hashAlg = x5tItem.getAsLong(0);
                assertNotNull(hashAlg);
                assertNotNull(DigestAlgorithm.forCOSE(hashAlg));

                byte[] hashVal = x5tItem.getAsBinaries(1);
                assertNotNull(hashVal);
                assertTrue(Utils.isArrayNotEmpty(hashVal));
            }
        }
    }

    private void checkCertificateChain(CBORMap protectedHeaderMap) {
        CBORArray x5chain = protectedHeaderMap.getAsArray(33L);
        assertNotNull(x5chain);
        assertFalse(x5chain.isEmpty());
        for (CBORObject certObject : x5chain.getItems()) {
            assertNotNull(certObject);
            assertTrue(certObject.isByteString());

            CBORByteString cert = (CBORByteString) certObject;
            assertTrue(Utils.isArrayNotEmpty(cert.getBytes()));
            CertificateToken certificateToken = DSSUtils.loadCertificate(cert.getBytes());
            assertNotNull(certificateToken);
        }
    }

    protected void checkSigningTime(CBORMap protectedHeaderMap) {
        Long sigT = protectedHeaderMap.getAsLong(102L);
        assertNotNull(sigT);

        Date date = new Date(sigT * 1000L);
        assertNotNull(date);
        assertEquals(signatureParameters.bLevel().getSigningDate().getTime() / 1000L, sigT);
    }

    protected void checkContentType(CBORMap protectedHeaderMap) {
        String contentType = protectedHeaderMap.getAsString(3L);
        CBORMap sigD = protectedHeaderMap.getAsMap(111L);
        assertTrue(contentType != null ^ sigD != null);

        if (contentType != null) {
            assertTrue(Utils.isStringNotEmpty(contentType));
            assertNotNull(MimeType.fromMimeTypeString(contentType));
        }
        if (sigD != null) {
            String mId = sigD.getAsString(1);
            assertNotNull(mId);
            SigDMechanism sigDMechanism = SigDMechanism.forCBAdESUri(mId);
            assertNotNull(sigDMechanism);

            CBORArray pars = sigD.getAsArray(2);
            assertNotNull(pars);
            assertFalse(pars.isEmpty());
            for (CBORObject par : pars.getItems()) {
                assertTrue(par.isUnicodeString());
                assertTrue(Utils.isStringNotEmpty(((CBORSimpleObject) par).getValueAsString()));
            }

            if (SigDMechanism.OBJECT_ID_BY_URI == sigDMechanism) {
                Long hashM = sigD.getAsLong(3);
                assertNull(hashM);

                CBORArray hashV = sigD.getAsArray(4);
                assertNull(hashV);

            } else if (SigDMechanism.OBJECT_ID_BY_URI_HASH == sigDMechanism) {
                Long hashM = sigD.getAsLong(3);
                assertNotNull(hashM);
                assertNotNull(DigestAlgorithm.forCOSE(hashM));

                CBORArray hashV = sigD.getAsArray(4);
                assertNotNull(hashV);
                assertFalse(hashV.isEmpty());
                for (CBORObject par : hashV.getItems()) {
                    assertTrue(par.isByteString());
                    assertTrue(Utils.isArrayNotEmpty(((CBORByteString) par).getBytes()));
                }

            }

            CBORArray ctys = sigD.getAsArray(5);
            assertNotNull(ctys);
            assertFalse(ctys.isEmpty());
            for (CBORObject cty : ctys.getItems()) {
                assertTrue(cty.isUnicodeString());
                assertTrue(Utils.isStringNotEmpty(((CBORSimpleObject) cty).getValueAsString()));
            }

        }
    }

    private void checkCrit(CBORMap protectedHeaderMap) {
        List<Long> includedHeaders = Collections.singletonList(111L); // sigD

        List<Long> presentHeaders = new ArrayList<>();
        for (Long protectedHeaderKey : protectedHeaderMap.getKeys()) {
            if (includedHeaders.contains(protectedHeaderKey)) {
                presentHeaders.add(protectedHeaderKey);
            }
        }

        CBORArray crit = protectedHeaderMap.getAsArray(2L);
        if (Utils.isCollectionNotEmpty(presentHeaders)) {
            assertNotNull(crit);
            assertFalse(crit.isEmpty());

            for (CBORObject critEntry : crit.getItems()) {
                assertNotNull(critEntry);
                assertTrue(critEntry.isUnsignedInteger() || critEntry.isNegativeInteger());
                Long critEntryId = ((CBORSimpleObject) critEntry).getValueAsLong();
                assertTrue(includedHeaders.contains(critEntryId));
            }
        }
    }

    protected void checkSignatureValue(CBORByteString signatureValue) {
        assertNotNull(signatureValue);
        assertTrue(Utils.isArrayNotEmpty(signatureValue.getBytes()));
    }

    protected void checkUnprotectedHeader(CBORMap unprotectedHeaderMap) throws Exception {
        assertNotNull(unprotectedHeaderMap);
        // TODO : to be implemented
    }

    @Override
    protected DSSDocument getDocumentToSign() {
        return documentToSign;
    }

    @Override
    protected DocumentSignatureService<CBAdESSignatureParameters, CBAdESTimestampParameters> getService() {
        return service;
    }

    @Override
    protected CBAdESSignatureParameters getSignatureParameters() {
        return signatureParameters;
    }

    @Override
    protected String getSigningAlias() {
        return GOOD_USER;
    }

}
