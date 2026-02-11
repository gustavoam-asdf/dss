package eu.europa.esig.dss.eaa.jwt.validation;

import eu.europa.esig.dss.enumerations.JWSSerializationType;
import eu.europa.esig.dss.enumerations.MimeTypeEnum;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.jades.JAdESSignatureParameters;
import eu.europa.esig.dss.jades.signature.JAdESService;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.model.SignatureValue;
import eu.europa.esig.dss.model.ToBeSigned;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.validation.SignedDocumentValidator;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.fail;

class SDJWTCompactEAAPresentationWithNestedDisclosuresValidationTest extends AbstractSDJWTEAAPresentationTestValidation {

    private static DSSDocument originalDocument;
    private static DSSDocument disclosuresDocument;

    static {
        String payload = "{\n" +
                "  \"_sd\": [\n" +
                "    \"5G1srw3RG5W4pVTwSsYxeOWosRBbzd18ZoWKkC-hBL4\",\n" +
                "  ],\n" +
                "  \"iss\": \"https://issuer.example.com\",\n" +
                "  \"iat\": 1683000000,\n" +
                "  \"exp\": 1883000000,\n" +
                "  \"sub\": \"user_42\",\n" +
                "  \"_sd_alg\": \"sha-256\",\n" +
                "  \"cnf\": {\n" +
                "    \"jwk\": {\n" +
                "      \"kty\": \"EC\",\n" +
                "      \"crv\": \"P-256\",\n" +
                "      \"x\": \"TCAER19Zvu3OHF4j4W4vfSVoHIP1ILilDls7vCeGemc\",\n" +
                "      \"y\": \"ZxjiWWbZMQGHVWKVQ4hbSIirsVfuecCE6t4jT9F2HZQ\"\n" +
                "    }\n" +
                "  }\n" +
                "}";
        originalDocument = new InMemoryDocument(payload.getBytes());
        originalDocument.setMimeType(MimeTypeEnum.JSON);

        String disclosures = "~WyIxNl9tQWQwR2l3YVpva1UyNl8waTBoIiwiREUiXQ~WyI0ZHJmZVR" +
                "0U1VLM2FZXy1QRjEyZ2NYIiwibmF0aW9uYWxpdGllcyIsCiAgICBbCiAgICAgICAgeyAiL" +
                "i4uIjogIlBtbmxyUmpoTGN3Zjh6VERkSzE1SFZHd0h0UFlqZGR2RDM2MldqQkx3cm8iIH0" +
                "sCiAgICAgICAgeyAiLi4uIjogInI4MjNIRk42QmFfbHBTQU5ZdFhxcUNCQUgtVHNRbEl6Z" +
                "k9LMGxSQUZMQ00iIH0sCiAgICAgICAgeyAiLi4uIjogIm5QNUdZandoRm02RVNsQWVDNE5" +
                "DYUlsaVc0dHowaFRyVWVvSkIzbGI1VEEiIH0KICAgIF0KXQ~WyJmbjlmTjByRC1mRnMy" +
                "bjMwM1pJLTBjIiwiRlIiXQ~WyJZSUtlc3FPa1hYTnpNUXRzWF8tX2x3IiwiVUsiXQ~";
        disclosuresDocument = new InMemoryDocument(disclosures.getBytes());
    }

    @Override
    protected DSSDocument getSignedDocument() {
        JAdESSignatureParameters signatureParameters = new JAdESSignatureParameters();
        signatureParameters.setSigningCertificate(getSigningCert());
        signatureParameters.setCertificateChain(getCertificateChain());
        signatureParameters.setSignatureLevel(SignatureLevel.JAdES_BASELINE_B);
        signatureParameters.setSignaturePackaging(SignaturePackaging.ENVELOPING);
        signatureParameters.setJwsSerializationType(JWSSerializationType.COMPACT_SERIALIZATION);
        signatureParameters.setX509Url("http://nowina.lu/pki-factory/good-cert");

        JAdESService service = new JAdESService(getOfflineCertificateVerifier());
        ToBeSigned dataToSign = service.getDataToSign(originalDocument, signatureParameters);
        SignatureValue signatureValue = getToken().sign(dataToSign, signatureParameters.getDigestAlgorithm(), getPrivateKeyEntry());
        DSSDocument signedDocument = service.signDocument(originalDocument, signatureParameters, signatureValue);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Utils.write(DSSUtils.toByteArray(signedDocument), baos);
            Utils.write(DSSUtils.toByteArray(disclosuresDocument), baos);
            return new InMemoryDocument(baos.toByteArray(), "sd-jwt.jwt");

        } catch (Exception e) {
            fail(e);
            return null;
        }
    }

    @Override
    protected SignedDocumentValidator getValidator(DSSDocument signedDocument) {
        SignedDocumentValidator validator = super.getValidator(signedDocument);
        validator.setCertificateVerifier(getCompleteCertificateVerifier());
        return validator;
    }

    @Override
    protected boolean keyBindingPresent() {
        return false;
    }

    @Override
    protected String getSigningAlias() {
        return GOOD_USER;
    }

}
