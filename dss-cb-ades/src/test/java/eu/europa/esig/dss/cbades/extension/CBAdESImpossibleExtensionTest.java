package eu.europa.esig.dss.cbades.extension;

import eu.europa.esig.dss.cbades.signature.CBAdESService;
import eu.europa.esig.dss.cbades.signature.CBAdESSignatureParameters;
import eu.europa.esig.dss.enumerations.COSEStructureType;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.SigDMechanism;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.DigestDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.model.SignatureValue;
import eu.europa.esig.dss.model.ToBeSigned;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.test.PKIFactoryAccess;
import eu.europa.esig.dss.utils.Utils;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CBAdESImpossibleExtensionTest extends PKIFactoryAccess {

    @Test
    void digestDocumentWithLTALevelTest() {
        DSSDocument doc = new InMemoryDocument("Hello world!".getBytes());
        DigestDocument digestDocument = new DigestDocument(DigestAlgorithm.SHA512,
                Utils.toBase64(DSSUtils.digest(DigestAlgorithm.SHA512, doc)), "sample");

        CBAdESService service = new CBAdESService(getCompleteCertificateVerifier());
        service.setTspSource(getGoodTsa());

        CBAdESSignatureParameters parameters = new CBAdESSignatureParameters();
        parameters.setSigningCertificate(getSigningCert());
        parameters.setCertificateChain(getCertificateChain());
        parameters.setCoseStructureType(COSEStructureType.COSE_SIGN1);
        parameters.setSignaturePackaging(SignaturePackaging.DETACHED);
        parameters.setSigDMechanism(SigDMechanism.OBJECT_ID_BY_URI_HASH);
        parameters.setSignatureLevel(SignatureLevel.CB_AdES_BASELINE_LT);

        ToBeSigned dataToSign = service.getDataToSign(digestDocument, parameters);
        SignatureValue signatureValue = getToken().sign(dataToSign, parameters.getDigestAlgorithm(), getPrivateKeyEntry());
        DSSDocument signedDocument = service.signDocument(digestDocument, parameters, signatureValue);

        CBAdESSignatureParameters extensionParameters = new CBAdESSignatureParameters();
        extensionParameters.setSignatureLevel(SignatureLevel.CB_AdES_BASELINE_LTA);
        extensionParameters.setDetachedContents(Collections.singletonList(digestDocument));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.extendDocument(signedDocument, extensionParameters));
        assertEquals("CB-AdES-BASELINE-LTA requires complete binaries of signed documents! " +
                "Extension with a DigestDocument is not possible.", exception.getMessage());
    }

    @Override
    protected String getSigningAlias() {
        return GOOD_USER;
    }

}
