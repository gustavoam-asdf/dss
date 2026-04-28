package eu.europa.esig.dss.eaa.jwt.creation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;

import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.EAAPresentationWrapper;
import eu.europa.esig.dss.diagnostic.claim.ClaimWrapper;
import eu.europa.esig.dss.eaa.jwt.creation.claim.SDJWTPresentableClaim;
import eu.europa.esig.dss.eaa.jwt.validation.AbstractSDJWTEAAPresentationTestValidation;
import eu.europa.esig.dss.enumerations.JWSSerializationType;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.jades.JAdESSignatureParameters;
import eu.europa.esig.dss.jades.signature.JAdESService;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.SignatureValue;
import eu.europa.esig.dss.model.ToBeSigned;

class SDJWTJsonSerializationEAAPresentationSimpleTest extends AbstractSDJWTEAAPresentationTestValidation {

    private Date issuanceDate;
    private Date expiration;
    private SDJWTPresentableClaim claim;

    @BeforeEach
    void init() {
        issuanceDate = new Date();
        expiration = new Date(issuanceDate.getTime() + 3600 * 1000);

        SDJWTClaimBuilder claimBuilder = new SDJWTClaimBuilder();
        claim = claimBuilder.createStringClaim("test-key", "test-value", true);
    }

    @Override
    protected DSSDocument getSignedDocument() {
        SDJWTEAAParameters eaaParameters = new SDJWTEAAParameters();
        eaaParameters.setIssuanceDate(issuanceDate);
        eaaParameters.setExpirationDate(expiration);
        eaaParameters.setIssuer("https://issuer.example.com");

        eaaParameters.addClaim(claim);

        JAdESSignatureParameters signatureParameters = new JAdESSignatureParameters();
        signatureParameters.setSigningCertificate(getSigningCert());
        signatureParameters.setCertificateChain(getCertificateChain());
        signatureParameters.setSignatureLevel(SignatureLevel.JAdES_BASELINE_B);
        signatureParameters.setSignaturePackaging(SignaturePackaging.ENVELOPING);
        signatureParameters.setJwsSerializationType(JWSSerializationType.JSON_SERIALIZATION);
        signatureParameters.setX509Url("http://nowina.lu/pki-factory/good-cert");

        JAdESService jadesService = new JAdESService(getOfflineCertificateVerifier());
        SDJWTEAAService service = new SDJWTEAAService(jadesService);

        ToBeSigned dataToSign = service.getDataToBeSigned(eaaParameters, signatureParameters);
        SignatureValue signatureValue = getToken().sign(dataToSign, signatureParameters.getDigestAlgorithm(), getPrivateKeyEntry());
        DSSDocument signedDocument = service.signEAA(eaaParameters, signatureParameters, signatureValue);
        List<String> disclosures = service.getDisclosures(Collections.singletonList(claim), eaaParameters);
        return service.issuePresentation(signedDocument, disclosures);
    }

    @Override
    protected void checkClaims(final DiagnosticData diagnosticData) {
        super.checkClaims(diagnosticData);

        EAAPresentationWrapper eaaPresentation = diagnosticData.getEAAPresentations().get(0);
        assertEquals("https://issuer.example.com", eaaPresentation.getEAAIssuer());
        assertEquals(expiration.toInstant().getEpochSecond(), eaaPresentation.getEAAExpirationTime().toInstant().getEpochSecond());
        assertEquals(issuanceDate.toInstant().getEpochSecond(), eaaPresentation.getEAAIssuedAt().toInstant().getEpochSecond());

        List<ClaimWrapper> payloadClaims = eaaPresentation.getAllEAAPayloadClaims();
        assertNotNull(payloadClaims);

        boolean claimFound = false;
        for (ClaimWrapper disclosableClaim : payloadClaims) {
            if (claim.getName().equals(disclosableClaim.getName())) {
                assertTrue(disclosableClaim.isText());
                assertEquals(claim.getValueAsString(), disclosableClaim.getText());
                assertEquals(claim.getValueAsString(), disclosableClaim.getDisplayValue());
                assertTrue(disclosableClaim.isSelectivelyDisclosable());
                claimFound = true;

            }
        }
        assertTrue(claimFound);
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
