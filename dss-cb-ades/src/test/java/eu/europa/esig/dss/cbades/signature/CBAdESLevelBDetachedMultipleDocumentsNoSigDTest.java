package eu.europa.esig.dss.cbades.signature;

import eu.europa.esig.dss.enumerations.SigDMechanism;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.FileDocument;
import eu.europa.esig.dss.signature.MultipleDocumentsSignatureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CBAdESLevelBDetachedMultipleDocumentsNoSigDTest extends AbstractCBAdESMultipleDocumentSignatureTest {

    private CBAdESSignatureParameters signatureParameters;
    private List<DSSDocument> documentToSigns;
    private CBAdESService service;

    @BeforeEach
    void init() {
        DSSDocument documentOne = new FileDocument("src/test/resources/ObjectIdByURIHash-1.html");
        DSSDocument documentTwo = new FileDocument("src/test/resources/ObjectIdByURIHash-2.html");
        documentToSigns = Arrays.asList(documentOne, documentTwo);

        service = new CBAdESService(getCompleteCertificateVerifier());
        signatureParameters = new CBAdESSignatureParameters();
        signatureParameters.bLevel().setSigningDate(new Date());
        signatureParameters.setSigningCertificate(getSigningCert());
        signatureParameters.setCertificateChain(getCertificateChain());
        signatureParameters.setSignatureLevel(SignatureLevel.CB_AdES_BASELINE_B);
        signatureParameters.setSignaturePackaging(SignaturePackaging.DETACHED);
    }

    @Override
    @Test
    public void signAndVerify() {
        Exception exception = assertThrows(IllegalArgumentException.class, this::sign);
        assertEquals("The SigDMechanism is not defined for a detached signature! " +
                "Please use CBAdESSignatureParameters.setSigDMechanism(sigDMechanism) method.", exception.getMessage());

        signatureParameters.setSigDMechanism(SigDMechanism.NO_SIG_D);

        exception = assertThrows(IllegalArgumentException.class, this::sign);
        assertEquals("NO_SIG_D mechanism is not allowed for multiple documents!", exception.getMessage());
    }

    @Override
    protected MultipleDocumentsSignatureService<CBAdESSignatureParameters, CBAdESTimestampParameters> getService() {
        return service;
    }

    @Override
    protected CBAdESSignatureParameters getSignatureParameters() {
        return signatureParameters;
    }

    @Override
    protected List<DSSDocument> getDocumentsToSign() {
        return documentToSigns;
    }

    @Override
    protected String getSigningAlias() {
        return GOOD_USER;
    }

}
