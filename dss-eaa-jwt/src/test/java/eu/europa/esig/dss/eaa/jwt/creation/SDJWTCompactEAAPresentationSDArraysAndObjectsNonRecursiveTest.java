package eu.europa.esig.dss.eaa.jwt.creation;

import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.EAAWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDigestMatcher;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.jades.JAdESSignatureParameters;
import eu.europa.esig.dss.model.Digest;
import eu.europa.esig.dss.spi.DSSASN1Utils;
import eu.europa.esig.dss.spi.DSSUtils;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SDJWTCompactEAAPresentationSDArraysAndObjectsNonRecursiveTest extends AbstractSDJWTEAAPresentationTestIssuance {

    private SDJWTEAAPayloadParameters payloadParameters;
    private JAdESSignatureParameters signatureParameters;

    @BeforeEach
    void init() {
        payloadParameters = new SDJWTEAAPayloadParameters();
        payloadParameters.setIssuer("EAA provider");
        payloadParameters.setSubject(DSSASN1Utils.getSubjectCommonName(getSigningCert()));
        payloadParameters.setDeviceKey(getSigningCert().getPublicKey());

        payloadParameters.setVerifiableCredentialsType("urn:eudi:eaa:1");
        Digest digest = new Digest(DigestAlgorithm.SHA256, DSSUtils.digest(DigestAlgorithm.SHA256, "vct".getBytes()));
        payloadParameters.setVerifiableCredentialsTypeIntegrity(digest);

        payloadParameters.selectivelyDisclosable().setGivenName("John");
        payloadParameters.selectivelyDisclosable().setFamilyName("Doe");
        payloadParameters.selectivelyDisclosable().setIssuingAuthority("TEST Authority");
        payloadParameters.selectivelyDisclosable().setIssuingCountry("LU");
        payloadParameters.selectivelyDisclosable().setIssuingAuthorityRegistrationIdentifier("VATLU-123456");

        SDJWTEAAClaimArray nationalities = SDJWTEAAClaim.createArray("pets");
        nationalities.addElement(SDJWTEAAClaim.createSelectivelyDisclosable("dog"));
        nationalities.addElement(SDJWTEAAClaim.createSelectivelyDisclosable("cat"));
        payloadParameters.nonSelectivelyDisclosable().addClaim(nationalities);

        signatureParameters = new JAdESSignatureParameters();
        signatureParameters.setDigestAlgorithm(DigestAlgorithm.SHA256);
        signatureParameters.setSigningCertificate(getSigningCert());
        signatureParameters.setCertificateChain(getCertificateChain());
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
    protected void checkEAADigestMatchers(DiagnosticData diagnosticData) {
        super.checkEAADigestMatchers(diagnosticData);

        EAAWrapper eaa = diagnosticData.getEAAs().get(0);
        List<XmlDigestMatcher> digestMatchers = eaa.getDigestMatchers();
        assertEquals(7, digestMatchers.size());

        boolean familyNameSDFound = false;
        boolean givenNameSDFound = false;
        boolean issuingCountrySDFound = false;
        boolean issuingAuthoritySDFound = false;
        boolean issuingAuthorityRegNumberSDFound = false;
        boolean petsSDFound = false;
        boolean dogSDFound = false;
        boolean catSDFound = false;
        for (XmlDigestMatcher xmlDigestMatcher : digestMatchers) {
            assertNotNull(xmlDigestMatcher.getDisclosableClaim());
            if ("family_name".equals(xmlDigestMatcher.getDisclosableClaim().getName())) {
                assertEquals("Doe", xmlDigestMatcher.getDisclosableClaim().getValue());
                familyNameSDFound = true;
            } else if ("given_name".equals(xmlDigestMatcher.getDisclosableClaim().getName())) {
                assertEquals("John", xmlDigestMatcher.getDisclosableClaim().getValue());
                givenNameSDFound = true;
            } else if ("issuing_country".equals(xmlDigestMatcher.getDisclosableClaim().getName())) {
                assertEquals("LU", xmlDigestMatcher.getDisclosableClaim().getValue());
                issuingCountrySDFound = true;
            } else if ("issuing_authority".equals(xmlDigestMatcher.getDisclosableClaim().getName())) {
                assertEquals("TEST Authority", xmlDigestMatcher.getDisclosableClaim().getValue());
                issuingAuthoritySDFound = true;
            } else if ("iss_reg_id".equals(xmlDigestMatcher.getDisclosableClaim().getName())) {
                assertEquals("VATLU-123456", xmlDigestMatcher.getDisclosableClaim().getValue());
                issuingAuthorityRegNumberSDFound = true;
            } else if ("pets".equals(xmlDigestMatcher.getDisclosableClaim().getName())) {
                assertNotNull(xmlDigestMatcher.getDisclosableClaim().getValue());
                petsSDFound = true;
            } else if ("dog".equals(xmlDigestMatcher.getDisclosableClaim().getValue())) {
                assertNull(xmlDigestMatcher.getDisclosableClaim().getName());
                dogSDFound = true;
            } else if ("cat".equals(xmlDigestMatcher.getDisclosableClaim().getValue())) {
                assertNull(xmlDigestMatcher.getDisclosableClaim().getName());
                catSDFound = true;
            }
        }
        assertTrue(familyNameSDFound);
        assertTrue(givenNameSDFound);
        assertTrue(issuingCountrySDFound);
        assertTrue(issuingAuthoritySDFound);
        assertTrue(issuingAuthorityRegNumberSDFound);
        assertFalse(petsSDFound);
        assertTrue(dogSDFound);
        assertTrue(catSDFound);
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