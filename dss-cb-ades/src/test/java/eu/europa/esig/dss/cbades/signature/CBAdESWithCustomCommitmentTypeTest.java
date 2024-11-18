package eu.europa.esig.dss.cbades.signature;

import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.SignatureWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlCommitmentTypeIndication;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.model.CommonCommitmentType;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.signature.DocumentSignatureService;
import org.junit.jupiter.api.BeforeEach;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CBAdESWithCustomCommitmentTypeTest extends AbstractCBAdESTestSignature {

    private DocumentSignatureService<CBAdESSignatureParameters, CBAdESTimestampParameters> service;
    private DSSDocument documentToSign;

    private Date signingDate;

    @BeforeEach
    void init() throws Exception {
        service = new CBAdESService(getCompleteCertificateVerifier());
        service.setTspSource(getGoodTsa());
        documentToSign = new InMemoryDocument("Hello world!".getBytes(), "HelloWorld");
        signingDate = new Date();
    }

    @Override
    protected CBAdESSignatureParameters getSignatureParameters() {
        CBAdESSignatureParameters signatureParameters = new CBAdESSignatureParameters();
        signatureParameters.bLevel().setSigningDate(signingDate);
        signatureParameters.setSigningCertificate(getSigningCert());
        signatureParameters.setCertificateChain(getCertificateChain());
        signatureParameters.setSignaturePackaging(SignaturePackaging.ENVELOPING);
        signatureParameters.setSignatureLevel(SignatureLevel.CB_AdES_BASELINE_B);

        CommonCommitmentType commitmentTypeApproval = new CommonCommitmentType();
        commitmentTypeApproval.setUri("http://nowina.lu/approved");
        commitmentTypeApproval.setDescription("Approved");
        commitmentTypeApproval.setDocumentationReferences("http://nowina.lu/approved.pdf", "https://uri.etsi.org/01903/v1.2.2/ts_101903v010202p.pdf");

        signatureParameters.bLevel().setCommitmentTypeIndications(Collections.singletonList(commitmentTypeApproval));

        return signatureParameters;
    }

    @Override
    protected void checkCommitmentTypeIndications(DiagnosticData diagnosticData) {
        super.checkCommitmentTypeIndications(diagnosticData);

        SignatureWrapper signature = diagnosticData.getSignatureById(diagnosticData.getFirstSignatureId());
        List<XmlCommitmentTypeIndication> commitmentTypeIndications = signature.getCommitmentTypeIndications();
        assertEquals(1, commitmentTypeIndications.size());
        XmlCommitmentTypeIndication commitmentTypeIndication = commitmentTypeIndications.get(0);
        assertEquals(2, commitmentTypeIndication.getDocumentationReferences().size());
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
