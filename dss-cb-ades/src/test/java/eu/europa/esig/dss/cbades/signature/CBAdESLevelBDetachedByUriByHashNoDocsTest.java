package eu.europa.esig.dss.cbades.signature;

import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.SignatureWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDigestMatcher;
import eu.europa.esig.dss.enumerations.DigestMatcherType;
import eu.europa.esig.dss.enumerations.SigDMechanism;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.signature.DocumentSignatureService;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.validation.SignedDocumentValidator;
import eu.europa.esig.validationreport.jaxb.SignersDocumentType;
import org.junit.jupiter.api.BeforeEach;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CBAdESLevelBDetachedByUriByHashNoDocsTest extends AbstractCBAdESTestSignature {

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
        signatureParameters.setSignatureLevel(SignatureLevel.CB_AdES_BASELINE_B);

        signatureParameters.setSigDMechanism(SigDMechanism.OBJECT_ID_BY_URI_HASH);
        return signatureParameters;
    }

    @Override
    protected void checkBLevelValid(DiagnosticData diagnosticData) {
        assertFalse(diagnosticData.isBLevelTechnicallyValid(diagnosticData.getFirstSignatureId()));

        SignatureWrapper signature = diagnosticData.getSignatureById(diagnosticData.getFirstSignatureId());
        List<XmlDigestMatcher> digestMatchers = signature.getDigestMatchers();
        assertEquals(2, digestMatchers.size());

        boolean signingInputFound = false;
        boolean sigDEntryFound = false;
        for (XmlDigestMatcher digestMatcher : digestMatchers) {
            if (DigestMatcherType.JWS_SIGNING_INPUT.equals(digestMatcher.getType())) {
                assertTrue(digestMatcher.isDataFound());
                assertTrue(digestMatcher.isDataIntact());
                signingInputFound = true;
            } else if (DigestMatcherType.SIG_D_ENTRY.equals(digestMatcher.getType())) {
                assertFalse(digestMatcher.isDataFound());
                assertFalse(digestMatcher.isDataIntact());
                sigDEntryFound = true;
            }
        }
        assertTrue(signingInputFound);
        assertTrue(sigDEntryFound);
    }

    @Override
    protected void checkSignatureScopes(DiagnosticData diagnosticData) {
        assertTrue(Utils.isCollectionEmpty(diagnosticData.getOriginalSignerDocuments()));
    }

    @Override
    protected void validateETSISignersDocument(SignersDocumentType signersDocument) {
        assertNull(signersDocument);
    }

    @Override
    protected void verifyOriginalDocuments(SignedDocumentValidator validator, DiagnosticData diagnosticData) {
        // do nothing
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
