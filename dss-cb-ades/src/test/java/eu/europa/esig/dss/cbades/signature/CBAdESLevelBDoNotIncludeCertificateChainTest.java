package eu.europa.esig.dss.cbades.signature;

import eu.europa.esig.dss.diagnostic.CertificateRefWrapper;
import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.RelatedCertificateWrapper;
import eu.europa.esig.dss.diagnostic.SignatureWrapper;
import eu.europa.esig.dss.enumerations.CertificateRefOrigin;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.signature.DocumentSignatureService;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import eu.europa.esig.dss.spi.x509.KidCertificateSource;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.validation.SignedDocumentValidator;
import org.junit.jupiter.api.BeforeEach;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CBAdESLevelBDoNotIncludeCertificateChainTest extends AbstractCBAdESTestSignature {

    private DocumentSignatureService<CBAdESSignatureParameters, CBAdESTimestampParameters> service;
    private DSSDocument documentToSign;
    private CBAdESSignatureParameters signatureParameters;

    @BeforeEach
    void init() {
        service = new CBAdESService(getCompleteCertificateVerifier());
        documentToSign = new InMemoryDocument("Hello World!".getBytes(), "doc.txt");
        signatureParameters = new CBAdESSignatureParameters();
        signatureParameters.bLevel().setSigningDate(new Date());
        signatureParameters.setSigningCertificate(getSigningCert());
        signatureParameters.setCertificateChain(getCertificateChain());
        signatureParameters.setSignaturePackaging(SignaturePackaging.ENVELOPING);
        signatureParameters.setSignatureLevel(SignatureLevel.CB_AdES_BASELINE_B);

        signatureParameters.setIncludeCertificateChain(false);
    }

    @Override
    protected SignedDocumentValidator getValidator(DSSDocument signedDocument) {
        SignedDocumentValidator validator = super.getValidator(signedDocument);
        validator.setCertificateVerifier(getCompleteCertificateVerifier());
        KidCertificateSource signingCertificateSource = new KidCertificateSource();
        signingCertificateSource.addCertificate(getSigningCert());
        validator.setSigningCertificateSource(signingCertificateSource);
        return validator;
    }

    @Override
    protected void verifySourcesAndDiagnosticData(List<AdvancedSignature> advancedSignatures,
                                                  DiagnosticData diagnosticData) {
        AdvancedSignature advancedSignature = advancedSignatures.get(0);
        assertEquals(1, advancedSignature.getCertificates().size());

        SignatureWrapper signatureWrapper = diagnosticData.getSignatureById(diagnosticData.getFirstSignatureId());

        List<RelatedCertificateWrapper> relatedCertificates = signatureWrapper.foundCertificates().getRelatedCertificates();
        assertEquals(3, relatedCertificates.size());

        RelatedCertificateWrapper relatedCertificateWrapper = relatedCertificates.get(0);
        assertTrue(Utils.isCollectionEmpty(relatedCertificateWrapper.getOrigins()));

        boolean signCertFound = false;
        boolean keyIdentifierFound = false;
        for (CertificateRefWrapper certificateRefWrapper : relatedCertificateWrapper.getReferences()) {
            if (CertificateRefOrigin.SIGNING_CERTIFICATE.equals(certificateRefWrapper.getOrigin())) {
                signCertFound = true;
            } else if (CertificateRefOrigin.KEY_IDENTIFIER.equals(certificateRefWrapper.getOrigin())) {
                keyIdentifierFound = true;
            }
        }
        assertTrue(signCertFound);
        assertTrue(keyIdentifierFound);

        assertEquals(3, signatureWrapper.foundCertificates().getRelatedCertificatesByRefOrigin(CertificateRefOrigin.SIGNING_CERTIFICATE).size());
        assertEquals(1, signatureWrapper.foundCertificates().getRelatedCertificatesByRefOrigin(CertificateRefOrigin.KEY_IDENTIFIER).size());

        assertNotNull(signatureWrapper.getSigningCertificate());
        assertTrue(Utils.isCollectionNotEmpty(signatureWrapper.getCertificateChain()));
    }

    @Override
    protected void checkNoDuplicateCompleteCertificates(DiagnosticData diagnosticData) {
        // do nothing
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
