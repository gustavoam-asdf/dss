package eu.europa.esig.dss.cbades.signature;

import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.SignatureWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDigestMatcher;
import eu.europa.esig.dss.enumerations.COSEStructureType;
import eu.europa.esig.dss.enumerations.DigestMatcherType;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.signature.DocumentSignatureService;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.validationreport.jaxb.SignersDocumentType;
import org.junit.jupiter.api.BeforeEach;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CBAdESLevelBCoseSignWrongExternalAadProvidedTest extends AbstractCBAdESTestSignature {

    private DocumentSignatureService<CBAdESSignatureParameters, CBAdESTimestampParameters> service;
    private DSSDocument documentToSign;
    private CBAdESSignatureParameters signatureParameters;

    private DSSDocument externallySuppliedData;

    @BeforeEach
    void init() throws Exception {
        service = new CBAdESService(getCompleteCertificateVerifier());
        service.setTspSource(getGoodTsa());

        documentToSign = new InMemoryDocument("Hello world!".getBytes());
        externallySuppliedData = new InMemoryDocument("Bye World!".getBytes());

        signatureParameters = new CBAdESSignatureParameters();
        signatureParameters.bLevel().setSigningDate(new Date());
        signatureParameters.setSigningCertificate(getSigningCert());
        signatureParameters.setCertificateChain(getCertificateChain());
        signatureParameters.setSignaturePackaging(SignaturePackaging.ENVELOPING);
        signatureParameters.setSignatureLevel(SignatureLevel.CB_AdES_BASELINE_B);
        signatureParameters.setCoseStructureType(COSEStructureType.COSE_SIGN);
        signatureParameters.setExternallySuppliedData(externallySuppliedData);
    }

    @Override
    protected DSSDocument getExternallySuppliedData() {
        return documentToSign;
    }

    @Override
    protected void checkBLevelValid(DiagnosticData diagnosticData) {
        SignatureWrapper signature = diagnosticData.getSignatureById(diagnosticData.getFirstSignatureId());
        assertFalse(signature.isSignatureIntact());
        assertFalse(signature.isSignatureValid());
        assertFalse(diagnosticData.isBLevelTechnicallyValid(signature.getId()));

        List<XmlDigestMatcher> digestMatchers = signature.getDigestMatchers();
        assertEquals(1, digestMatchers.size());

        XmlDigestMatcher xmlDigestMatcher = digestMatchers.get(0);
        assertEquals(DigestMatcherType.COSE_SIG_STRUCTURE, xmlDigestMatcher.getType());
        assertTrue(xmlDigestMatcher.isDataFound());
        assertFalse(xmlDigestMatcher.isDataIntact());
    }

    @Override
    protected void checkSignatureScopes(DiagnosticData diagnosticData) {
        SignatureWrapper signature = diagnosticData.getSignatureById(diagnosticData.getFirstSignatureId());
        assertTrue(Utils.isCollectionEmpty(signature.getSignatureScopes())); // not valid DTBS
    }

    @Override
    protected void validateETSISignersDocument(SignersDocumentType signersDocument) {
        assertNull(signersDocument);
    }

    @Override
    protected CBAdESSignatureParameters getSignatureParameters() {
        return signatureParameters;
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
    protected String getSigningAlias() {
        return GOOD_USER;
    }

}
