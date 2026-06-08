package eu.europa.esig.dss.eaa.jwt.creation;

import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.EAAWrapper;
import eu.europa.esig.dss.eaa.common.creation.EAARevocationList;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.JWSSerializationType;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.jades.JAdESSignatureParameters;
import eu.europa.esig.dss.model.Digest;
import eu.europa.esig.dss.spi.DSSASN1Utils;
import eu.europa.esig.dss.spi.DSSUtils;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SDJWTCompactStatusAndAttestedAttributesCreationTest extends AbstractSDJWTEAAPresentationTestIssuance {

    private SDJWTEAAPayloadParameters payloadParameters;
    private JAdESSignatureParameters signatureParameters;

    @BeforeEach
    void init() {
        Date issuanceDate = new Date();

        payloadParameters = new SDJWTEAAPayloadParameters();
        payloadParameters.setIssuer("https://issuer.example.com");
        payloadParameters.setIssuanceDate(issuanceDate);
        payloadParameters.setExpirationDate(new Date(issuanceDate.getTime() + 3600 * 1000));
        payloadParameters.nonSelectivelyDisclosable().setIssuingAuthority("Public body");
        payloadParameters.nonSelectivelyDisclosable().setGivenName("Alice");
        payloadParameters.nonSelectivelyDisclosable().setFamilyName("Doe");
        payloadParameters.setVerifiableCredentialsType("https://nowina.lu/eaa/metadata");
        Digest digest = new Digest(DigestAlgorithm.SHA256, DSSUtils.digest(DigestAlgorithm.SHA256, "Hello World".getBytes()));
        payloadParameters.setVerifiableCredentialsTypeIntegrity(digest);

        SDJWTEAAClaimObject status = SDJWTEAAClaim.createObject("status");
        status.addChild(SDJWTEAAClaim.create("type", "TokenStatusList"));
        status.addChild(SDJWTEAAClaim.create("purpose", "revocation"));
        status.addChild(SDJWTEAAClaim.create("index", 0));
        status.addChild(SDJWTEAAClaim.create("uri", "https://nowina.lu/pki-factory/status"));
        payloadParameters.nonSelectivelyDisclosable().addClaim(status);

        payloadParameters.nonSelectivelyDisclosable().setAttestedAttributesSubjectIdentifier(
                DSSASN1Utils.getSubjectCommonName(getSigningCert()), Arrays.asList("given_name", "family_name")
        );

        payloadParameters.nonSelectivelyDisclosable().setPlaceOfBirthCountry("LU");

        signatureParameters = new JAdESSignatureParameters();
        signatureParameters.bLevel().setSigningDate(issuanceDate);
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
        return payloadParameters;
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
    protected void checkClaims(final DiagnosticData diagnosticData) {
        super.checkClaims(diagnosticData);

        EAAWrapper eaa = diagnosticData.getEAAs().get(0);

        assertEquals("TokenStatusList", eaa.getEAAStatusType());
        assertEquals("revocation", eaa.getEAAStatusPurpose());
        assertEquals(0, eaa.getEAAStatusIndex().intValue());
        assertEquals("https://nowina.lu/pki-factory/status", eaa.getEAAStatusUri());

        assertEquals("good-user", eaa.getAttestedAttributesSubjectId());
        assertNull(eaa.getAttestedAttributesSubjectFamilyName());
        assertNull(eaa.getAttestedAttributesSubjectGivenName());
        assertNull(eaa.getAttestedAttributesSubjectDocumentNumber());
        assertNull(eaa.getAttestedAttributesSubjectPseudonym());
        assertEquals(Arrays.asList("given_name", "family_name"), eaa.getAttestedAttributes());

        assertEquals("LU", eaa.getHolderPlaceOfBirthCountry());
    }

    @Override
    protected void assertStatusListEqual(EAARevocationList statusList, EAAWrapper eaa) {
        // skip
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
