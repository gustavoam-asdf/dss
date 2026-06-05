package eu.europa.esig.dss.eaa.jwt.creation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;

import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.EAAWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDigestMatcher;
import eu.europa.esig.dss.enumerations.JWSSerializationType;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.jades.JAdESSignatureParameters;

class SDJWTCompactEAAPresentationWithKBSignatureTest extends AbstractSDJWTEAAPresentationTestIssuance {

    private SDJWTEAAPayloadParameters payloadParameters;
    private JAdESSignatureParameters signatureParameters;

    @BeforeEach
    void init() {
        payloadParameters = new SDJWTEAAPayloadParameters();
        payloadParameters.setIssuer("https://issuer.example.com");
        payloadParameters.selectivelyDisclosable().setGivenName("John");
        payloadParameters.selectivelyDisclosable().setFamilyName("Doe");
        payloadParameters.setDeviceKey(getSigningCert().getPublicKey());
        payloadParameters.setDeviceKeyType("RSA");

        signatureParameters = new JAdESSignatureParameters();
        signatureParameters.setSigningCertificate(getSigningCert());
        signatureParameters.setCertificateChain(getCertificateChain());
        signatureParameters.setSignatureLevel(SignatureLevel.JAdES_BASELINE_B);
        signatureParameters.setSignaturePackaging(SignaturePackaging.ENVELOPING);
        signatureParameters.setJwsSerializationType(JWSSerializationType.COMPACT_SERIALIZATION);
        signatureParameters.setX509Url("http://nowina.lu/pki-factory/good-cert");
    }

    @Override
    protected SDJWTEAAPayloadParameters getPayloadParameters() {
        return payloadParameters;
    }

    @Override
    protected JAdESSignatureParameters getSignatureParameters() {
        return signatureParameters;
    }

    @Override
    protected JAdESSignatureParameters getKeyBindingSignatureParameters() {
        JAdESSignatureParameters keyBindingSignatureParameters = new JAdESSignatureParameters();
        keyBindingSignatureParameters.setSigningCertificate(getSigningCert());
        keyBindingSignatureParameters.setCertificateChain(getCertificateChain());
        keyBindingSignatureParameters.setJwsSerializationType(JWSSerializationType.COMPACT_SERIALIZATION);
        keyBindingSignatureParameters.setIncludeKeyIdentifier(false);
        keyBindingSignatureParameters.setIncludeCertificateChain(false);
        return keyBindingSignatureParameters;
    }

    @Override
    protected SDJWTKeyBindingParameters getKeyBindingParameters() {
        SDJWTKeyBindingParameters keyBindingParameters = new SDJWTKeyBindingParameters();
        keyBindingParameters.setIssuanceTime(new Date());
        keyBindingParameters.setAudience("https://verifier.example.org");
        keyBindingParameters.setNonce("1234567890");
        return keyBindingParameters;
    }

    @Override
    protected void checkEAADigestMatchers(DiagnosticData diagnosticData) {
        super.checkEAADigestMatchers(diagnosticData);

        EAAWrapper eaa = diagnosticData.getEAAs().get(0);
        List<XmlDigestMatcher> digestMatchers = eaa.getDigestMatchers();
        assertEquals(2, digestMatchers.size());

        boolean givenNameSDFound = false;
        boolean familyNameSDFound = false;
        for (XmlDigestMatcher xmlDigestMatcher : digestMatchers) {
            assertNotNull(xmlDigestMatcher.getDisclosableClaim());
            if ("given_name".equals(xmlDigestMatcher.getDisclosableClaim().getName())) {
                givenNameSDFound = true;
            } else if ("family_name".equals(xmlDigestMatcher.getDisclosableClaim().getName())) {
                familyNameSDFound = true;
            }
        }
        assertTrue(givenNameSDFound);
        assertTrue(familyNameSDFound);
    }

    @Override
    protected void checkClaims(final DiagnosticData diagnosticData) {
        super.checkClaims(diagnosticData);

        EAAWrapper eaa = diagnosticData.getEAAs().get(0);
        assertEquals("https://issuer.example.com", eaa.getEAAIssuer());
        assertEquals("John", eaa.getHolderGivenName());
        assertEquals("Doe", eaa.getHolderFamilyName());
    }

    @Override
    protected String getSigningAlias() {
        return GOOD_USER;
    }

}
