package eu.europa.esig.dss.eaa.jwt.creation;

import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.EAAWrapper;
import eu.europa.esig.dss.enumerations.JWSSerializationType;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.jades.JAdESSignatureParameters;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SDJWTCompactOneTimeCreationTest extends AbstractSDJWTEAAPresentationTestIssuance {

    private SDJWTEAAPayloadParameters parameters;
    private JAdESSignatureParameters signatureParameters;

    private String signer;

    @BeforeEach
    void init() {
        signer = ECDSA_USER;

        parameters = new SDJWTEAAPayloadParameters();
        parameters.setIssuer("https://issuer.example.com");
        parameters.setSubject(getSigningCert().getSubject().getPrettyPrintRFC2253());
        parameters.nonSelectivelyDisclosable().setIssuingAuthority("Public body");
        parameters.nonSelectivelyDisclosable().setIssuingCountry("LU");
        parameters.nonSelectivelyDisclosable().setIssuingAuthorityRegistrationIdentifier("XX12345");
        parameters.nonSelectivelyDisclosable().setGivenName("Alice");
        parameters.nonSelectivelyDisclosable().setFamilyName("Doe");

        parameters.setOneTime(true);

        parameters.setDeviceKey(getSigningCert().getPublicKey());

        signer = GOOD_USER;

        signatureParameters = new JAdESSignatureParameters();
        signatureParameters.setSigningCertificate(getSigningCert());
        signatureParameters.setCertificateChain(getCertificateChain());
        signatureParameters.setSignatureLevel(SignatureLevel.JAdES_BASELINE_B);
        signatureParameters.setSignaturePackaging(SignaturePackaging.ENVELOPING);
        signatureParameters.setJwsSerializationType(JWSSerializationType.COMPACT_SERIALIZATION);
        signatureParameters.setIncludeKeyIdentifier(false);
        signatureParameters.setSignatureType("dc+sd-jwt");
    }

    @Override
    protected SDJWTEAAPayloadParameters getPayloadParameters() {
        return parameters;
    }

    @Override
    protected JAdESSignatureParameters getSignatureParameters() {
        return signatureParameters;
    }

    @Override
    protected JAdESSignatureParameters getKeyBindingSignatureParameters() {
        return null;
    }

    @Override
    protected SDJWTKeyBindingParameters getKeyBindingParameters() {
        return null;
    }

    @Override
    protected void checkClaims(final DiagnosticData diagnosticData) {
        super.checkClaims(diagnosticData);

        EAAWrapper eaa = diagnosticData.getEAAs().get(0);
        assertTrue(eaa.getOneTimeUse());
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
        return signer;
    }

}
