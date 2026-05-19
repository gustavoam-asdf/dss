package eu.europa.esig.dss.eaa.jwt.creation;

import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.EAAWrapper;
import eu.europa.esig.dss.diagnostic.claim.ClaimWrapper;
import eu.europa.esig.dss.eaa.common.creation.DefaultEAASaltGenerator;
import eu.europa.esig.dss.eaa.common.creation.EAASaltGenerator;
import eu.europa.esig.dss.eaa.jwt.validation.AbstractSDJWTEAAPresentationTestValidation;
import eu.europa.esig.dss.enumerations.JWSSerializationType;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.jades.JAdESSignatureParameters;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.SignatureValue;
import eu.europa.esig.dss.model.ToBeSigned;

import org.junit.jupiter.api.BeforeEach;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SDJWTCompactEAAPresentationSimpleTest extends AbstractSDJWTEAAPresentationTestValidation {

    private Date issuanceDate;
    private Date expiration;
    private SDJWTEAAClaim claim;

    @BeforeEach
    void init() {
        issuanceDate = new Date();
        expiration = new Date(issuanceDate.getTime() + 3600 * 1000);

        EAASaltGenerator saltGenerator = new DefaultEAASaltGenerator();
        claim = new SDJWTEAAClaim("test-key", "test-value", true, saltGenerator.generateSaltString());
    }

    @Override
    protected DSSDocument getSignedDocument() {
        SDJWTPayloadBuilder payloadBuilder = new SDJWTPayloadBuilder();
        payloadBuilder.setIssuanceDate(issuanceDate);
        payloadBuilder.setExpirationDate(expiration);
        payloadBuilder.setIssuer("https://issuer.example.com");

        payloadBuilder.addClaim(claim);

        JAdESSignatureParameters signatureParameters = new JAdESSignatureParameters();
        signatureParameters.setSigningCertificate(getSigningCert());
        signatureParameters.setCertificateChain(getCertificateChain());
        signatureParameters.setSignatureLevel(SignatureLevel.JAdES_BASELINE_B);
        signatureParameters.setSignaturePackaging(SignaturePackaging.ENVELOPING);
        signatureParameters.setJwsSerializationType(JWSSerializationType.COMPACT_SERIALIZATION);
        signatureParameters.setX509Url("http://nowina.lu/pki-factory/good-cert");

        SDJWTEAAService service = new SDJWTEAAService(getOfflineCertificateVerifier());

        ToBeSigned dataToSign = service.getDataToBeSigned(payloadBuilder, signatureParameters);
        SignatureValue signatureValue = getToken().sign(dataToSign, signatureParameters.getDigestAlgorithm(), getPrivateKeyEntry());
        DSSDocument signedDocument = service.signEAA(payloadBuilder, signatureParameters, signatureValue);
        List<SDJWTEAADisclosure> disclosures = service.getDisclosures(Collections.singletonList(claim), payloadBuilder);
        return service.issuePresentation(signedDocument, disclosures);
    }

    @Override
    protected void checkClaims(final DiagnosticData diagnosticData) {
        super.checkClaims(diagnosticData);

        EAAWrapper eaa = diagnosticData.getEAAs().get(0);
        assertEquals("https://issuer.example.com", eaa.getEAAIssuer());
        assertEquals(expiration.toInstant().getEpochSecond(), eaa.getEAAExpiration().toInstant().getEpochSecond());
        assertEquals(issuanceDate.toInstant().getEpochSecond(), eaa.getEAAIssuedAt().toInstant().getEpochSecond());

        List<ClaimWrapper> payloadClaims = eaa.getAllEAAPayloadClaims();
        assertNotNull(payloadClaims);

        boolean claimFound = false;
        for (ClaimWrapper disclosableClaim : payloadClaims) {
            if (claim.getName().equals(disclosableClaim.getName())) {
                assertTrue(disclosableClaim.isText());
                assertEquals(claim.getValue(), disclosableClaim.getText());
                assertEquals(claim.getValue(), disclosableClaim.getDisplayValue());
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
