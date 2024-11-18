package eu.europa.esig.dss.cbades.extension;

import eu.europa.esig.dss.alert.SilentOnStatusAlert;
import eu.europa.esig.dss.cbades.COSEConstants;
import eu.europa.esig.dss.cbades.cbor.CBORArray;
import eu.europa.esig.dss.cbades.cbor.CBORByteString;
import eu.europa.esig.dss.cbades.cbor.CBORMap;
import eu.europa.esig.dss.cbades.cbor.CBORObject;
import eu.europa.esig.dss.cbades.cbor.CBORUtils;
import eu.europa.esig.dss.cbades.signature.CBAdESService;
import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.spi.validation.CertificateVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CBAdESExtensionLTToLTUpdateTest extends AbstractCBAdESTestExtension {

    @Override
    protected SignatureLevel getOriginalSignatureLevel() {
        return SignatureLevel.CB_AdES_BASELINE_LT;
    }

    @Override
    protected SignatureLevel getFinalSignatureLevel() {
        return SignatureLevel.CB_AdES_BASELINE_LT;
    }

    @Override
    protected CBAdESService getSignatureServiceToSign() {
        CertificateVerifier certificateVerifier = getCompleteCertificateVerifier();
        certificateVerifier.setCrlSource(null);
        certificateVerifier.setAlertOnMissingRevocationData(new SilentOnStatusAlert());

        CBAdESService service = new CBAdESService(certificateVerifier);
        service.setTspSource(getUsedTSPSourceAtSignatureTime());
        return service;
    }

    @Override
    protected void checkOriginalLevel(DiagnosticData diagnosticData) {
        // no complete revocation data
        assertEquals(SignatureLevel.CB_AdES_BASELINE_T, diagnosticData.getSignatureFormat(diagnosticData.getFirstSignatureId()));
    }

    @Override
    protected DSSDocument extendSignature(DSSDocument signedDocument) throws Exception {
        DSSDocument extendedDocument = super.extendSignature(signedDocument);
        assertTrue(CBORUtils.isCbor(extendedDocument));

        byte[] binaries = DSSUtils.toByteArray(extendedDocument);
        CBORObject cborObject = CBORUtils.parseCbor(binaries);
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

        boolean xValsFound = false;
        boolean rValsFound = false;

        boolean crlValsFound = false;
        boolean ocspValsFound = false;

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
                CBORMap valDataMap = (CBORMap) valData;
                if (valDataMap.getAsArray(COSEConstants.VAL_DATA_X_VALS) != null) {
                    xValsFound = true;
                }
                CBORMap rVals = valDataMap.getAsMap(COSEConstants.VAL_DATA_R_VALS);
                assertNotNull(rVals);
                rValsFound = true;

                if (rVals.getAsArray(COSEConstants.R_VALS_CRL_VALS) != null) {
                    crlValsFound = true;
                }
                if (rVals.getAsArray(COSEConstants.R_VALS_OCSP_VALS) != null) {
                    ocspValsFound = true;
                }

                ++valDataCounter;
            }
            CBORObject arcTst  = uHeaderObjectMap.getHeader(COSEConstants.ARC_TST);
            if (arcTst != null) {
                ++arcTstCounter;
            }
        }

        assertEquals(1, sigTstCounter);
        assertEquals(1, valDataCounter);
        assertEquals(0, arcTstCounter);

        assertTrue(xValsFound);
        assertTrue(rValsFound);
        assertTrue(crlValsFound);
        assertTrue(ocspValsFound);

        return extendedDocument;
    }

    @Override
    protected String getSigningAlias() {
        return GOOD_USER_WITH_CRL_AND_OCSP;
    }

}
