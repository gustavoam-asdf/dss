package eu.europa.esig.dss.eaa.jwt.validation;

import eu.europa.esig.dss.diagnostic.claim.ClaimWrapper;
import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.EAAPresentationWrapper;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.JWSSerializationType;
import eu.europa.esig.dss.enumerations.MimeTypeEnum;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.jades.DSSJsonUtils;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class SDJWTCompactEAAPresentationWithMetadataValidationTest extends AbstractSDJWTEAAPresentationTestValidation {

    private static DSSDocument originalDocument;

    static {
        String payload = "{\n" +
                "  \"iss\": \"https://issuer.example.com\",\n" +
                "  \"iat\": 1683000000,\n" +
                "  \"exp\": 1883000000,\n" +
                "  \"sub\": \"user_42\",\n" +
                "  \"cnf\": {\n" +
                "    \"jwk\": {\n" +
                "      \"kty\": \"EC\",\n" +
                "      \"crv\": \"P-256\",\n" +
                "      \"x\": \"TCAER19Zvu3OHF4j4W4vfSVoHIP1ILilDls7vCeGemc\",\n" +
                "      \"y\": \"ZxjiWWbZMQGHVWKVQ4hbSIirsVfuecCE6t4jT9F2HZQ\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"vct\": \"urn:eudi:pid:1\",\n" +
                "  \"vct#integrity\": \"sha256-1odmyxoVQCuQx8SAym8rWHXba41fM/Iv/V1H8VHGN00=\",\n" +
                "  \"unknown#integrity\": \"sha384-H8BRh8j48O9oYatfu5AZzq6A9RINhZO5H16dQZngK7T62em8MUt1FLm52t\",\n" +
                "}";
        originalDocument = new InMemoryDocument(payload.getBytes());
        originalDocument.setMimeType(MimeTypeEnum.JSON);
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
            baos.write('~');
            return new InMemoryDocument(baos.toByteArray(), "simple-sd-jwt.jwt");

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
    protected void checkClaims(DiagnosticData diagnosticData) {
        super.checkClaims(diagnosticData);

        EAAPresentationWrapper eaaPresentation = diagnosticData.getEAAPresentations().get(0);
        assertEquals("https://issuer.example.com", eaaPresentation.getEAAIssuer());
        assertEquals("user_42", eaaPresentation.getEAASubject());
        assertEquals(DSSUtils.parseRFCDate("2029-09-01T23:33:20Z"), eaaPresentation.getEAAExpirationTime());
        assertEquals(DSSUtils.parseRFCDate("2023-05-02T04:00:00Z"), eaaPresentation.getEAAIssuedAt());
        assertEquals("urn:eudi:pid:1", eaaPresentation.getEAAMetadataUri());
        assertEquals(DigestAlgorithm.SHA256, eaaPresentation.getEAAMetadataIntegrityDigestAlgorithm());
        assertEquals("1odmyxoVQCuQx8SAym8rWHXba41fM/Iv/V1H8VHGN00=", Utils.toBase64(eaaPresentation.getEAAMetadataIntegrityBytes()));

        List<ClaimWrapper> payloadClaims = eaaPresentation.getAllEAAPayloadClaims();
        assertNotNull(payloadClaims);

        boolean metadataClaimFound = false;
        boolean metadataIntegrityClaimFound = false;
        boolean unknownIntegrityClaimFound = false;
        for (ClaimWrapper disclosableClaim : payloadClaims) {
            if ("vct".equals(disclosableClaim.getName())) {
                assertEquals("urn:eudi:pid:1", disclosableClaim.getText());
                assertEquals("urn:eudi:pid:1", disclosableClaim.getDisplayValue());
                assertFalse(disclosableClaim.isSelectivelyDisclosable());
                metadataClaimFound = true;

            } else if ("vct#integrity".equals(disclosableClaim.getName())) {
                assertEquals("sha256-1odmyxoVQCuQx8SAym8rWHXba41fM/Iv/V1H8VHGN00=", disclosableClaim.getText());
                assertEquals("sha256-1odmyxoVQCuQx8SAym8rWHXba41fM/Iv/V1H8VHGN00=", disclosableClaim.getDisplayValue());
                assertFalse(disclosableClaim.isSelectivelyDisclosable());
                metadataIntegrityClaimFound = true;

            } else if ("unknown#integrity".equals(disclosableClaim.getName())) {
                assertEquals("sha384-H8BRh8j48O9oYatfu5AZzq6A9RINhZO5H16dQZngK7T62em8MUt1FLm52t", disclosableClaim.getText());
                assertEquals("sha384-H8BRh8j48O9oYatfu5AZzq6A9RINhZO5H16dQZngK7T62em8MUt1FLm52t", disclosableClaim.getDisplayValue());
                assertFalse(disclosableClaim.isSelectivelyDisclosable());
                unknownIntegrityClaimFound = true;
            }
        }
        assertTrue(metadataClaimFound);
        assertTrue(metadataIntegrityClaimFound);
        assertTrue(unknownIntegrityClaimFound);
    }

    @Override
    protected boolean disclosuresPresent() {
        return false;
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
