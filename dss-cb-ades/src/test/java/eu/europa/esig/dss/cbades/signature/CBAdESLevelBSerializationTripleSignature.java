package eu.europa.esig.dss.cbades.signature;

import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.enumerations.COSEStructureType;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.signature.DocumentSignatureService;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import org.junit.jupiter.api.BeforeEach;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CBAdESLevelBSerializationTripleSignature extends AbstractCBAdESTestSignature {

    private DocumentSignatureService<CBAdESSignatureParameters, CBAdESTimestampParameters> service;
    private DSSDocument originalDocument;
    private CBAdESSignatureParameters signatureParameters;

    private DSSDocument documentToSign;

    @BeforeEach
    public void init() throws Exception {
        service = new CBAdESService(getCompleteCertificateVerifier());
        originalDocument = new InMemoryDocument("Hello World!".getBytes(), "doc.txt");
        signatureParameters = new CBAdESSignatureParameters();
        signatureParameters.bLevel().setSigningDate(new Date());
        signatureParameters.setSigningCertificate(getSigningCert());
        signatureParameters.setCertificateChain(getCertificateChain());
        signatureParameters.setSignaturePackaging(SignaturePackaging.ENVELOPING);
        signatureParameters.setSignatureLevel(SignatureLevel.CB_AdES_BASELINE_B);

        signatureParameters.setCoseStructureType(COSEStructureType.COSE_SIGN);
    }

    @Override
    protected DSSDocument sign() {
        documentToSign = originalDocument;
        DSSDocument signedDocument = super.sign();
        documentToSign = signedDocument;
        DSSDocument doubleSignedDocument = super.sign();
        documentToSign = doubleSignedDocument;
        DSSDocument tripleSignedDocument = super.sign();
        documentToSign = originalDocument;
        return tripleSignedDocument;
    }

    @Override
    protected void checkAdvancedSignatures(List<AdvancedSignature> signatures) {
        super.checkAdvancedSignatures(signatures);
        assertEquals(3, signatures.size());

        assertNotEquals(signatures.get(0).getId(), signatures.get(1).getId());
        assertNotEquals(signatures.get(1).getId(), signatures.get(2).getId());
        assertNotEquals(signatures.get(0).getId(), signatures.get(2).getId());
    }

    @Override
    protected void checkNumberOfSignatures(DiagnosticData diagnosticData) {
        assertEquals(3, diagnosticData.getSignatures().size());
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
