package eu.europa.esig.dss.cbades.signature;

import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.SignerDataWrapper;
import eu.europa.esig.dss.diagnostic.TimestampWrapper;
import eu.europa.esig.dss.enumerations.COSEStructureType;
import eu.europa.esig.dss.enumerations.SigDMechanism;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.enumerations.TimestampType;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.signature.DocumentSignatureService;
import eu.europa.esig.dss.spi.x509.tsp.TimestampToken;
import org.junit.jupiter.api.BeforeEach;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CBAdESLevelLTADetachedByUriWithContentTstTest extends AbstractCBAdESTestSignature {

    private DocumentSignatureService<CBAdESSignatureParameters, CBAdESTimestampParameters> service;
    private DSSDocument documentToSign;
    private Date signingDate;

    @BeforeEach
    void init() throws Exception {
        service = new CBAdESService(getCompleteCertificateVerifier());
        service.setTspSource(getGoodTsa());
        documentToSign = new InMemoryDocument("Hello world!".getBytes(), "helloWorld");
        signingDate = new Date();
    }

    @Override
    protected CBAdESSignatureParameters getSignatureParameters() {
        CBAdESSignatureParameters signatureParameters = new CBAdESSignatureParameters();
        signatureParameters.bLevel().setSigningDate(signingDate);
        signatureParameters.setSigningCertificate(getSigningCert());
        signatureParameters.setCertificateChain(getCertificateChain());
        signatureParameters.setSignaturePackaging(SignaturePackaging.DETACHED);
        signatureParameters.setSignatureLevel(SignatureLevel.CB_AdES_BASELINE_LTA);
        signatureParameters.setCoseStructureType(COSEStructureType.COSE_SIGN1);

        signatureParameters.setSigDMechanism(SigDMechanism.OBJECT_ID_BY_URI);

        TimestampToken contentTimestamp = service.getContentTimestamp(documentToSign, signatureParameters);
        signatureParameters.setContentTimestamps(Collections.singletonList(contentTimestamp));

        return signatureParameters;
    }

    @Override
    protected void checkTimestamps(DiagnosticData diagnosticData) {
        super.checkTimestamps(diagnosticData);

        List<TimestampWrapper> timestampList = diagnosticData.getTimestampList();
        assertEquals(3, timestampList.size());

        boolean contentTstFound = false;
        boolean archiveTstFound = false;
        for (TimestampWrapper timestampWrapper : timestampList) {
            if (TimestampType.CONTENT_TIMESTAMP.equals(timestampWrapper.getType())) {
                assertEquals(1, timestampWrapper.getTimestampedSignedData().size());
                contentTstFound = true;
            } else if (TimestampType.ARCHIVE_TIMESTAMP.equals(timestampWrapper.getType())) {
                assertEquals(1, timestampWrapper.getTimestampedSignedData().size());
                archiveTstFound = true;
            }
        }
        assertTrue(contentTstFound);
        assertTrue(archiveTstFound);
    }

    @Override
    protected void checkSignatureScopes(DiagnosticData diagnosticData) {
        super.checkSignatureScopes(diagnosticData);

        List<SignerDataWrapper> originalSignerDocuments = diagnosticData.getOriginalSignerDocuments();
        assertEquals(1, originalSignerDocuments.size());

        SignerDataWrapper signerDataWrapper = originalSignerDocuments.get(0);
        assertEquals(documentToSign.getName(), signerDataWrapper.getReferencedName());
        assertNotNull(signerDataWrapper.getId());
        assertNotNull(signerDataWrapper.getDigestAlgoAndValue());
        assertNotNull(signerDataWrapper.getDigestAlgoAndValue().getDigestMethod());
        assertNotNull(signerDataWrapper.getDigestAlgoAndValue().getDigestValue());
    }

    @Override
    protected List<DSSDocument> getDetachedContents() {
        return Collections.singletonList(documentToSign);
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
