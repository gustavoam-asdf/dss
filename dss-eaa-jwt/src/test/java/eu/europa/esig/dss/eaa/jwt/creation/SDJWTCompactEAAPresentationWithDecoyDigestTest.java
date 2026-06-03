package eu.europa.esig.dss.eaa.jwt.creation;

import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.EAAWrapper;
import eu.europa.esig.dss.diagnostic.claim.ClaimWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDigestMatcher;
import eu.europa.esig.dss.eaa.jwt.validation.AbstractSDJWTEAAPresentationTestValidation;
import eu.europa.esig.dss.enumerations.DigestMatcherType;
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

class SDJWTCompactEAAPresentationWithDecoyDigestTest extends AbstractSDJWTEAAPresentationTestValidation {

    private Date issuanceDate;
    private Date expiration;

    @BeforeEach
    void init() {
        issuanceDate = new Date();
        expiration = new Date(issuanceDate.getTime() + 3600 * 1000);
    }

    @Override
    protected DSSDocument getSignedDocument() {
        SDJWTEAAPayloadParameters payloadParameters = new SDJWTEAAPayloadParameters();
        payloadParameters.setIssuanceDate(issuanceDate);
        payloadParameters.setExpirationDate(expiration);
        payloadParameters.setIssuer("https://issuer.example.com");

        payloadParameters.setDecoyDigestNumber(1);
        payloadParameters.selectivelyDisclosable().setGivenName("John");

        JAdESSignatureParameters signatureParameters = new JAdESSignatureParameters();
        signatureParameters.setSigningCertificate(getSigningCert());
        signatureParameters.setCertificateChain(getCertificateChain());
        signatureParameters.setSignatureLevel(SignatureLevel.JAdES_BASELINE_B);
        signatureParameters.setSignaturePackaging(SignaturePackaging.ENVELOPING);
        signatureParameters.setJwsSerializationType(JWSSerializationType.COMPACT_SERIALIZATION);
        signatureParameters.setX509Url("http://nowina.lu/pki-factory/good-cert");

        SDJWTEAAService service = new SDJWTEAAService(getOfflineCertificateVerifier());

        ToBeSigned dataToSign = service.getDataToBeSigned(payloadParameters, signatureParameters);
        SignatureValue signatureValue = getToken().sign(dataToSign, signatureParameters.getDigestAlgorithm(), getPrivateKeyEntry());
        DSSDocument signedDocument = service.signEAA(payloadParameters, signatureParameters, signatureValue);
        return service.issuePresentation(signedDocument, Collections.emptyList());
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

        final List<XmlDigestMatcher> digestMatchers = eaa.getDigestMatchers();
        assertEquals(2, digestMatchers.size());

        final XmlDigestMatcher xmlDigestMatcher1 = digestMatchers.get(0);
        assertEquals(DigestMatcherType.EAA_ORPHAN_SELECTIVELY_DISCLOSABLE_CLAIM, xmlDigestMatcher1.getType());

        final XmlDigestMatcher xmlDigestMatcher2 = digestMatchers.get(0);
        assertEquals(DigestMatcherType.EAA_ORPHAN_SELECTIVELY_DISCLOSABLE_CLAIM, xmlDigestMatcher2.getType());

        // The auto generated one should be the same length as the length of the hash of the claim
        assertEquals(xmlDigestMatcher1.getDigestValue().length, xmlDigestMatcher2.getDigestValue().length);
    }

    @Override
    protected boolean keyBindingPresent() {
        return false;
    }

    @Override
    protected String getSigningAlias() {
        return GOOD_USER;
    }

    @Override
    protected boolean orphanSelectivelyDisclosableClaimsPresent() {
        return true;
    }

    @Override
    protected boolean disclosuresPresent() {
        return false;
    }
}
