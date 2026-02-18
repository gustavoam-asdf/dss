package eu.europa.esig.dss.eaa.jwt.validation;

import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.EAAPresentationWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDisclosableClaim;
import eu.europa.esig.dss.enumerations.JWSSerializationType;
import eu.europa.esig.dss.enumerations.MimeTypeEnum;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.jades.DSSJsonUtils;
import eu.europa.esig.dss.jades.JAdESSignatureParameters;
import eu.europa.esig.dss.jades.JWSJsonSerializationGenerator;
import eu.europa.esig.dss.jades.JWSJsonSerializationObject;
import eu.europa.esig.dss.jades.JWSJsonSerializationParser;
import eu.europa.esig.dss.jades.signature.JAdESService;
import eu.europa.esig.dss.jades.validation.JWS;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.model.SignatureValue;
import eu.europa.esig.dss.model.ToBeSigned;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.validation.SignedDocumentValidator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

class SDJWTFlattenedEAAPresentationWithDisclosuresTest extends AbstractSDJWTEAAPresentationTestValidation {

    private static DSSDocument originalDocument;
    private static List<String> disclosures;

    static {
        String payloadB64Url = "eyJfc2QiOiBbIjRIQm42YUlZM1d0dUdHV1R4LXFVajZjZGs2V0JwWn" +
                "lnbHRkRmF2UGE3TFkiLCAiOHNtMVFDZjAyMXBObkhBQ0k1c1A0bTRLWmd5Tk9PQV" +
                "ljVGo5SE5hQzF3WSIsICJjZ0ZkaHFQbzgzeFlObEpmYWNhQ2FhN3VQOVJDUjUwVk" +
                "U1UjRMQVE5aXFVIiwgImpNQ1hWei0tOWI4eDM3WWNvRGZYUWluencxd1pjY2NmRl" +
                "JCQ0ZHcWRHMm8iXSwgImlzcyI6ICJodHRwczovL2lzc3Vlci5leGFtcGxlLmNvbS" +
                "IsICJpYXQiOiAxNjgzMDAwMDAwLCAiZXhwIjogMTg4MzAwMDAwMCwgIl9zZF9hbG" +
                "ciOiAic2hhLTI1NiIsICJjbmYiOiB7Imp3ayI6IHsia3R5IjogIkVDIiwgImNydi" +
                "I6ICJQLTI1NiIsICJ4IjogIlRDQUVSMTladnUzT0hGNGo0VzR2ZlNWb0hJUDFJTG" +
                "lsRGxzN3ZDZUdlbWMiLCAieSI6ICJaeGppV1diWk1RR0hWV0tWUTRoYlNJaXJzVm" +
                "Z1ZWNDRTZ0NGpUOUYySFpRIn19fQ";
        originalDocument = new InMemoryDocument(DSSJsonUtils.fromBase64Url(payloadB64Url));
        originalDocument.setMimeType(MimeTypeEnum.JSON);

        disclosures = Arrays.asList(
                "WyIyR0xDNDJzS1F2ZUNmR2ZyeU5STjl3IiwgInN1YiIsICJqb2huX2RvZV80MiJd",
                "WyJlbHVWNU9nM2dTTklJOEVZbnN4QV9BIiwgImdpdmVuX25hbWUiLCAiSm9obiJd",
                "WyI2SWo3dE0tYTVpVlBHYm9TNXRtdlZBIiwgImZhbWlseV9uYW1lIiwgIkRvZSJd",
                "WyJlSThaV205UW5LUHBOUGVOZW5IZGhRIiwgImJpcnRoZGF0ZSIsICIxOTQwLTAxLTAxIl0"
        );
    }

    @Override
    protected DSSDocument getSignedDocument() {
        JAdESSignatureParameters signatureParameters = new JAdESSignatureParameters();
        signatureParameters.setSigningCertificate(getSigningCert());
        signatureParameters.setCertificateChain(getCertificateChain());
        signatureParameters.setSignatureLevel(SignatureLevel.JAdES_BASELINE_B);
        signatureParameters.setSignaturePackaging(SignaturePackaging.ENVELOPING);
        signatureParameters.setJwsSerializationType(JWSSerializationType.FLATTENED_JSON_SERIALIZATION);
        signatureParameters.setX509Url("http://nowina.lu/pki-factory/good-cert");

        JAdESService service = new JAdESService(getOfflineCertificateVerifier());
        ToBeSigned dataToSign = service.getDataToSign(originalDocument, signatureParameters);
        SignatureValue signatureValue = getToken().sign(dataToSign, signatureParameters.getDigestAlgorithm(), getPrivateKeyEntry());
        DSSDocument signedDocument = service.signDocument(originalDocument, signatureParameters, signatureValue);

        JWSJsonSerializationObject jsonSerializationObject = new JWSJsonSerializationParser(signedDocument).parse();
        JWS jws = jsonSerializationObject.getSignatures().get(0);

        Map<String, Object> header = new HashMap<>();
        header.put("disclosures", disclosures);
        jws.setUnprotected(header);

        return new JWSJsonSerializationGenerator(jsonSerializationObject, JWSSerializationType.FLATTENED_JSON_SERIALIZATION).generate();
    }

    @Override
    protected void checkClaims(DiagnosticData diagnosticData) {
        super.checkClaims(diagnosticData);

        EAAPresentationWrapper eaaPresentation = diagnosticData.getEAAPresentations().get(0);
        assertEquals("https://issuer.example.com", eaaPresentation.getEAAIssuer());
        assertEquals("john_doe_42", eaaPresentation.getEAASubject());
        assertEquals(DSSJsonUtils.getDate("2029-09-01T23:33:20Z"), eaaPresentation.getEAAExpirationTime());
        assertEquals(DSSJsonUtils.getDate("2023-05-02T04:00:00Z"), eaaPresentation.getEAAIssuedAt());

        assertEquals("John", eaaPresentation.getUserFirstName());
        assertEquals("Doe", eaaPresentation.getUserLastName());
        assertEquals(DSSJsonUtils.getDate("1940-01-01T00:00:00Z"), eaaPresentation.getUserBirthdate());

        assertNull(eaaPresentation.getUserEmail());
        assertNull(eaaPresentation.getUserEmailVerified());
        assertNull(eaaPresentation.getUserAddressCity());
        assertNull(eaaPresentation.getUserAddressStateOrProvince());
        assertNull(eaaPresentation.getUserAddressCountry());
        assertNull(eaaPresentation.getUserStreetAddress());
        assertNull(eaaPresentation.getUserPhoneNumber());
        assertNull(eaaPresentation.getUserPhoneNumberVerified());
        assertFalse(Utils.isCollectionNotEmpty(eaaPresentation.getUserNationalities()));

        List<XmlDisclosableClaim> selectivelyDisclosableClaims = eaaPresentation.getSelectivelyDisclosableClaims();
        assertEquals(4, selectivelyDisclosableClaims.size());
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
