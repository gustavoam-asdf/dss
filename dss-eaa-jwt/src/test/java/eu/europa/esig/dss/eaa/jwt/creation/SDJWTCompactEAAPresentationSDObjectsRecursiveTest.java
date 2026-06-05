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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SDJWTCompactEAAPresentationSDObjectsRecursiveTest extends AbstractSDJWTEAAPresentationTestIssuance {

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

        SDJWTEAAClaimObject father = SDJWTEAAClaim.createObject("father");
        father.addChild(SDJWTEAAClaim.createSelectivelyDisclosable("given_name", "Ben"));
        father.addChild(SDJWTEAAClaim.createSelectivelyDisclosable("family_name", "Doe"));
        SDJWTEAAClaimArray nationalities = SDJWTEAAClaim.createArraySelectivelyDisclosable("nationalities");
        nationalities.addElement(SDJWTEAAClaim.createSelectivelyDisclosable("FR"));
        nationalities.addElement(SDJWTEAAClaim.createSelectivelyDisclosable("LU"));
        father.addChild(nationalities);

        SDJWTEAAClaimObject mother = SDJWTEAAClaim.createObject("mother");
        mother.addChild(SDJWTEAAClaim.createSelectivelyDisclosable("given_name", "Alice"));
        mother.addChild(SDJWTEAAClaim.createSelectivelyDisclosable("family_name", "Doe Doeg"));
        nationalities = SDJWTEAAClaim.createArraySelectivelyDisclosable("nationalities");
        nationalities.addElement(SDJWTEAAClaim.createSelectivelyDisclosable("FR"));
        nationalities.addElement(SDJWTEAAClaim.createSelectivelyDisclosable("DE"));
        mother.addChild(nationalities);

        payloadParameters.selectivelyDisclosable().addClaim(father);
        payloadParameters.selectivelyDisclosable().addClaim(mother);

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
    protected SDJWTKeyBindingParameters getKeyBindingParameters() {
        return null;
    }

    @Override
    protected void checkEAADigestMatchers(DiagnosticData diagnosticData) {
        super.checkEAADigestMatchers(diagnosticData);

        EAAWrapper eaa = diagnosticData.getEAAs().get(0);
        List<XmlDigestMatcher> digestMatchers = eaa.getDigestMatchers();
        assertEquals(12, digestMatchers.size());

        boolean fatherSDFound = false;
        boolean fatherGivenNameSDFound = false;
        boolean fatherFamilyNameSDFound = false;
        boolean motherSDFound = false;
        boolean motherGivenNameSDFound = false;
        boolean motherFamilyNameSDFound = false;

        int nationalitiesSDFound = 0;
        int nationalitiesFRSDFound = 0;
        int nationalitiesLUSDFound = 0;
        int nationalitiesDESDFound = 0;

        for (XmlDigestMatcher xmlDigestMatcher : digestMatchers) {
            assertNotNull(xmlDigestMatcher.getDisclosableClaim());
            if ("father".equals(xmlDigestMatcher.getDisclosableClaim().getName())) {
                assertNotNull(xmlDigestMatcher.getDisclosableClaim().getValue());
                fatherSDFound = true;
            } else if ("Ben".equals(xmlDigestMatcher.getDisclosableClaim().getValue())) {
                assertEquals("given_name", xmlDigestMatcher.getDisclosableClaim().getName());
                fatherGivenNameSDFound = true;
            } else if ("Doe".equals(xmlDigestMatcher.getDisclosableClaim().getValue())) {
                assertEquals("family_name", xmlDigestMatcher.getDisclosableClaim().getName());
                fatherFamilyNameSDFound = true;
            } else if ("nationalities".equals(xmlDigestMatcher.getDisclosableClaim().getName())) {
                assertNotNull(xmlDigestMatcher.getDisclosableClaim().getValue());
                ++nationalitiesSDFound;
            } else if ("FR".equals(xmlDigestMatcher.getDisclosableClaim().getValue())) {
                assertNull(xmlDigestMatcher.getDisclosableClaim().getName());
                ++nationalitiesFRSDFound;
            } else if ("LU".equals(xmlDigestMatcher.getDisclosableClaim().getValue())) {
                assertNull(xmlDigestMatcher.getDisclosableClaim().getName());
                ++nationalitiesLUSDFound;
            } else if ("DE".equals(xmlDigestMatcher.getDisclosableClaim().getValue())) {
                assertNull(xmlDigestMatcher.getDisclosableClaim().getName());
                ++nationalitiesDESDFound;
            } else if ("mother".equals(xmlDigestMatcher.getDisclosableClaim().getName())) {
                assertNotNull(xmlDigestMatcher.getDisclosableClaim().getValue());
                motherSDFound = true;
            } else if ("Alice".equals(xmlDigestMatcher.getDisclosableClaim().getValue())) {
                assertEquals("given_name", xmlDigestMatcher.getDisclosableClaim().getName());
                motherGivenNameSDFound = true;
            } else if ("Doe Doeg".equals(xmlDigestMatcher.getDisclosableClaim().getValue())) {
                assertEquals("family_name", xmlDigestMatcher.getDisclosableClaim().getName());
                motherFamilyNameSDFound = true;
            }
        }
        assertTrue(fatherSDFound);
        assertTrue(fatherGivenNameSDFound);
        assertTrue(fatherFamilyNameSDFound);
        assertTrue(motherSDFound);
        assertTrue(motherGivenNameSDFound);
        assertTrue(motherFamilyNameSDFound);
        assertEquals(2, nationalitiesSDFound);
        assertEquals(2, nationalitiesFRSDFound);
        assertEquals(1, nationalitiesLUSDFound);
        assertEquals(1, nationalitiesDESDFound);
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