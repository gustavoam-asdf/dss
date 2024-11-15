package eu.europa.esig.dss.cbades.signature;

import eu.europa.esig.dss.cbades.COSEConstants;
import eu.europa.esig.dss.cbades.cbor.CBORArray;
import eu.europa.esig.dss.cbades.cbor.CBORByteString;
import eu.europa.esig.dss.cbades.cbor.CBORMap;
import eu.europa.esig.dss.cbades.cbor.CBORObject;
import eu.europa.esig.dss.cbades.cbor.CBORUtils;
import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.SignatureWrapper;
import eu.europa.esig.dss.diagnostic.TimestampWrapper;
import eu.europa.esig.dss.enumerations.COSEStructureType;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.enumerations.TimestampType;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.signature.CounterSignatureService;
import eu.europa.esig.dss.signature.DocumentSignatureService;
import org.junit.jupiter.api.BeforeEach;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CBAdESLevelLTACoseSign1CounterSignatureTest extends AbstractCBAdESCounterSignatureTest {

    private CBAdESService service;
    private DSSDocument documentToSign;
    private CBAdESSignatureParameters signatureParameters;
    private CBAdESCounterSignatureParameters counterSignatureParameters;

    @BeforeEach
    void init() throws Exception {
        service = new CBAdESService(getCompleteCertificateVerifier());
        service.setTspSource(getGoodTsa());
        documentToSign = new InMemoryDocument("Hello world!".getBytes(), "HelloWorld");

        signatureParameters = new CBAdESSignatureParameters();
        signatureParameters.bLevel().setSigningDate(new Date());
        signatureParameters.setSigningCertificate(getSigningCert());
        signatureParameters.setCertificateChain(getCertificateChain());
        signatureParameters.setSignaturePackaging(SignaturePackaging.ENVELOPING);
        signatureParameters.setSignatureLevel(SignatureLevel.CB_AdES_BASELINE_LTA);
        signatureParameters.setCoseStructureType(COSEStructureType.COSE_SIGN1);

        counterSignatureParameters = new CBAdESCounterSignatureParameters();
        counterSignatureParameters.bLevel().setSigningDate(new Date());
        counterSignatureParameters.setSigningCertificate(getSigningCert());
        counterSignatureParameters.setCertificateChain(getCertificateChain());
        counterSignatureParameters.setSignatureLevel(SignatureLevel.CB_AdES_BASELINE_LTA);
    }

    @Override
    protected void onDocumentSigned(byte[] byteArray) {
        super.onDocumentSigned(byteArray);

        CBORObject cborObject = CBORUtils.parseCbor(byteArray);
        assertNotNull(cborObject);
        assertTrue(cborObject.isArray());

        CBORArray coseSign1 = (CBORArray) cborObject;
        assertEquals(4, coseSign1.getSize());

        CBORObject protectedHeader = coseSign1.getItem(0);
        assertTrue(protectedHeader.isByteString());

        CBORObject parsedProtectedHeader = CBORUtils.parseCbor(((CBORByteString) protectedHeader).getBytes());
        assertNotNull(parsedProtectedHeader);
        assertTrue(parsedProtectedHeader.isMap());
        assertFalse(((CBORMap) parsedProtectedHeader).isEmpty());

        CBORObject unprotectedHeader = coseSign1.getItem(1);
        assertTrue(unprotectedHeader.isMap());

        CBORMap unprotectedHeaderMap = (CBORMap) unprotectedHeader;
        assertFalse(unprotectedHeaderMap.isEmpty());
        assertEquals(1, unprotectedHeaderMap.getSize());

        CBORObject uHeaders = unprotectedHeaderMap.getHeader(COSEConstants.U_HEADERS);
        assertNotNull(uHeaders);
        assertTrue(uHeaders.isArray());

        CBORArray uHeadersArray = (CBORArray) uHeaders;
        assertFalse(uHeadersArray.isEmpty());

        int sigTstCounter = 0;
        int valDataCounter = 0;
        int arcTstCounter = 0;
        int counterSigCounter = 0;

        for (CBORObject uHeadersComponent : uHeadersArray.getItems()) {
            assertTrue(uHeadersComponent.isByteString()); // cbor btsr encoded
            CBORByteString uHeaderBtsr = (CBORByteString) uHeadersComponent;

            CBORObject uHeaderObject = CBORUtils.parseCbor(uHeaderBtsr.getBytes());
            assertNotNull(uHeaderObject);
            assertTrue(uHeaderObject.isMap());

            CBORMap uHeaderObjectMap =  (CBORMap) uHeaderObject;
            assertFalse(uHeaderObjectMap.isEmpty());
            assertEquals(1, uHeaderObjectMap.getSize());

            CBORObject sigTst  = uHeaderObjectMap.getHeader(COSEConstants.SIG_TST);
            if (sigTst != null) {
                ++sigTstCounter;

                CBORMap tstContainer = (CBORMap) sigTst;
                CBORArray tstTokens = tstContainer.getAsArray(COSEConstants.TST_CONTAINER_TST_TOKENS);
                assertEquals(1, tstTokens.getSize());
            }
            CBORObject valData  = uHeaderObjectMap.getHeader(COSEConstants.VAL_DATA);
            if (valData != null) {
                ++valDataCounter;
            }
            CBORObject arcTst  = uHeaderObjectMap.getHeader(COSEConstants.ARC_TST);
            if (arcTst != null) {
                ++arcTstCounter;

                CBORMap tstContainer = (CBORMap) arcTst;
                CBORArray tstTokens = tstContainer.getAsArray(COSEConstants.TST_CONTAINER_TST_TOKENS);
                assertEquals(1, tstTokens.getSize());
            }
            CBORObject counterSig  = uHeaderObjectMap.getHeader(COSEConstants.COUNTER_SIGNATURE_V2);
            if (counterSig != null) {
                ++counterSigCounter;

                CBORArray counterSigArray = (CBORArray) counterSig;
                assertEquals(3, counterSigArray.getSize());
            }
        }

        assertEquals(1, sigTstCounter);
        assertEquals(1, valDataCounter);
        assertEquals(1, arcTstCounter);
        assertEquals(1, counterSigCounter);
    }

    @Override
    protected void checkSignatureLevel(DiagnosticData diagnosticData) {
        super.checkSignatureLevel(diagnosticData);

        for (SignatureWrapper signatureWrapper : diagnosticData.getSignatures()) {
            assertEquals(SignatureLevel.CB_AdES_BASELINE_LTA, signatureWrapper.getSignatureFormat());
        }
    }

    @Override
    protected void checkTimestamps(DiagnosticData diagnosticData) {
        super.checkTimestamps(diagnosticData);

        List<TimestampWrapper> timestampList = diagnosticData.getTimestampList();
        assertEquals(4, diagnosticData.getTimestampList().size());

        int sigTstCounter = 0;
        int arcTstCounter = 0;
        for (TimestampWrapper timestampWrapper : timestampList) {
            assertEquals(1, timestampWrapper.getTimestampedSignatures().size());

            if (TimestampType.SIGNATURE_TIMESTAMP.equals(timestampWrapper.getType())) {
                ++sigTstCounter;
            } else if (TimestampType.ARCHIVE_TIMESTAMP.equals(timestampWrapper.getType())) {
                ++arcTstCounter;
            }
        }
        assertEquals(2, sigTstCounter);
        assertEquals(2, arcTstCounter);
    }

    @Override
    protected CBAdESSignatureParameters getSignatureParameters() {
        return signatureParameters;
    }

    @Override
    protected CBAdESCounterSignatureParameters getCounterSignatureParameters() {
        return counterSignatureParameters;
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
    protected CounterSignatureService<CBAdESCounterSignatureParameters> getCounterSignatureService() {
        return service;
    }

    @Override
    protected String getSigningAlias() {
        return GOOD_USER;
    }

}
