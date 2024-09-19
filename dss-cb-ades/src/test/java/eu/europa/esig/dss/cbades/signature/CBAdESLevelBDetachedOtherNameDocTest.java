package eu.europa.esig.dss.cbades.signature;

import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.SignatureWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDigestMatcher;
import eu.europa.esig.dss.diagnostic.jaxb.XmlSignatureScope;
import eu.europa.esig.dss.enumerations.DigestMatcherType;
import eu.europa.esig.dss.enumerations.SigDMechanism;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.enumerations.SignatureScopeType;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.signature.DocumentSignatureService;
import org.junit.jupiter.api.BeforeEach;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CBAdESLevelBDetachedOtherNameDocTest extends AbstractCBAdESTestSignature {

    private DocumentSignatureService<CBAdESSignatureParameters, CBAdESTimestampParameters> service;
    private DSSDocument documentToSign;
    private Date signingDate;

    @BeforeEach
    void init() throws Exception {
        service = new CBAdESService(getCompleteCertificateVerifier());
        service.setTspSource(getGoodTsa());
        documentToSign = new InMemoryDocument("Hello World!".getBytes(), "doc.txt");
        signingDate = new Date();
    }

    @Override
    protected CBAdESSignatureParameters getSignatureParameters() {
        CBAdESSignatureParameters signatureParameters = new CBAdESSignatureParameters();
        signatureParameters.bLevel().setSigningDate(signingDate);
        signatureParameters.setSigningCertificate(getSigningCert());
        signatureParameters.setCertificateChain(getCertificateChain());
        signatureParameters.setSignaturePackaging(SignaturePackaging.DETACHED);
        signatureParameters.setSigDMechanism(SigDMechanism.OBJECT_ID_BY_URI_HASH);
        signatureParameters.setSignatureLevel(SignatureLevel.CB_AdES_BASELINE_B);
        return signatureParameters;
    }

    @Override
    protected List<DSSDocument> getDetachedContents() {
        InMemoryDocument detachedDocument = new InMemoryDocument("Hello World!".getBytes(), "helloWorld");
        return List.of(detachedDocument);
    }

    @Override
    protected void checkDigestMatchers(DiagnosticData diagnosticData) {
        SignatureWrapper signatureWrapper = diagnosticData.getSignatureById(diagnosticData.getFirstSignatureId());
        boolean detachedDocFound = false;
        for (XmlDigestMatcher digestMatcher : signatureWrapper.getDigestMatchers()) {
            if (DigestMatcherType.SIG_D_ENTRY == digestMatcher.getType() && "helloWorld".equals(digestMatcher.getDocumentName())) {
                assertEquals("doc.txt", digestMatcher.getUri());
                detachedDocFound = true;
            }
            assertTrue(digestMatcher.isDataFound());
            assertTrue(digestMatcher.isDataIntact());
        }
        assertTrue(detachedDocFound);
    }

    @Override
    protected void checkSignatureScopes(DiagnosticData diagnosticData) {
        super.checkSignatureScopes(diagnosticData);

        SignatureWrapper signature = diagnosticData.getSignatureById(diagnosticData.getFirstSignatureId());
        assertNotNull(signature);

        List<XmlSignatureScope> signatureScopes = signature.getSignatureScopes();
        assertEquals(1, signatureScopes.size());

        XmlSignatureScope signatureScope = signatureScopes.get(0);
        assertNotNull(signatureScope.getSignerData());
        assertEquals(SignatureScopeType.FULL, signatureScope.getScope());
        assertEquals("helloWorld", signatureScope.getName());
        assertEquals("Full document", signatureScope.getDescription());
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
