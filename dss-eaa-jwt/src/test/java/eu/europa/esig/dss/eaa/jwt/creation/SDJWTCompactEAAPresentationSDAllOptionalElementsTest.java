package eu.europa.esig.dss.eaa.jwt.creation;

import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.EAAWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDigestMatcher;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.jades.JAdESSignatureParameters;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.spi.DSSASN1Utils;
import eu.europa.esig.dss.spi.x509.CommonX509URLCertificateSource;
import eu.europa.esig.dss.validation.SignedDocumentValidator;
import org.junit.jupiter.api.BeforeEach;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SDJWTCompactEAAPresentationSDAllOptionalElementsTest extends AbstractSDJWTEAAPresentationTestIssuance {

    private SDJWTEAAPayloadParameters payloadParameters;
    private JAdESSignatureParameters signatureParameters;

    @BeforeEach
    void init() {
        payloadParameters = new SDJWTEAAPayloadParameters();
        payloadParameters.setIssuer("EAA provider");
        payloadParameters.setSubject(DSSASN1Utils.getSubjectCommonName(getSigningCert()));
        payloadParameters.setDeviceKey(getSigningCert().getPublicKey());

        payloadParameters.selectivelyDisclosable().setGivenName("John");
        payloadParameters.selectivelyDisclosable().setFamilyName("Doe");
        payloadParameters.selectivelyDisclosable().setBirthdate(new Date(946684800000L)); // 2000-01-01
        payloadParameters.selectivelyDisclosable().setNationalities(Collections.singletonList("LUX"));
        payloadParameters.selectivelyDisclosable().setEmail("john.doe@example.com");
        payloadParameters.selectivelyDisclosable().setPhoneNumber("+352123456789");

        payloadParameters.selectivelyDisclosable().setPostalAddress("1 Main Street");
        payloadParameters.selectivelyDisclosable().setAddressHouseNumber("1");
        payloadParameters.selectivelyDisclosable().setAddressStreet("Main Street");
        payloadParameters.selectivelyDisclosable().setAddressCity("Kehlen");
        payloadParameters.selectivelyDisclosable().setAddressState("Capellen");
        payloadParameters.selectivelyDisclosable().setAddressPostalCode("L-1234");
        payloadParameters.selectivelyDisclosable().setAddressCountry("LU");

        payloadParameters.selectivelyDisclosable().setPlaceOfBirthCountry("LU");
        payloadParameters.selectivelyDisclosable().setPlaceOfBirthRegion("Luxembourg");
        payloadParameters.selectivelyDisclosable().setPlaceOfBirthLocality("Luxembourg City");

        payloadParameters.selectivelyDisclosable().setBirthGivenName("Johnny");
        payloadParameters.selectivelyDisclosable().setBirthFamilyName("Doe");
        payloadParameters.selectivelyDisclosable().setTitle("Dr");
        payloadParameters.selectivelyDisclosable().setMobilePhoneNumber("+352987654321");
        payloadParameters.selectivelyDisclosable().setPseudonym("JD");

        payloadParameters.selectivelyDisclosable().setPersonalAdministrativeNumber("ADM987654");

        payloadParameters.selectivelyDisclosable().setIssuingCountry("LU");
        payloadParameters.selectivelyDisclosable().setIssuingAuthority("Government of Luxembourg");
        payloadParameters.selectivelyDisclosable().setIssuingJurisdiction("LU-LU");
        payloadParameters.selectivelyDisclosable().setDocumentNumber("DOC123456");

        payloadParameters.selectivelyDisclosable().setAgeInYears(25);
        payloadParameters.selectivelyDisclosable().setAgeBirthYear(2000);
        payloadParameters.selectivelyDisclosable().setAgeOverNN(18, true);
        payloadParameters.selectivelyDisclosable().setAgeOverNN(21, false);

        payloadParameters.selectivelyDisclosable().setTrustAnchor("https://example.com/trust-anchor");

        payloadParameters.selectivelyDisclosable().setIssuingAuthorityRegistrationIdentifier("REG-123456");

        payloadParameters.selectivelyDisclosable().setAttestedAttributesSubjectIdentifier("SUBJ-123456");

        payloadParameters.selectivelyDisclosable().setPicture("https://example.com/john.jpg");
        payloadParameters.selectivelyDisclosable().setNickname("johnny");
        payloadParameters.selectivelyDisclosable().setPreferredNickname("jd");
        payloadParameters.selectivelyDisclosable().setName("Dr. John Doe");
        payloadParameters.selectivelyDisclosable().setMiddleName("William");

        payloadParameters.selectivelyDisclosable().setProfile("https://example.com/profile/john");
        payloadParameters.selectivelyDisclosable().setWebsite("https://johndoe.example");

        payloadParameters.selectivelyDisclosable().setEmailVerified(Boolean.TRUE);
        payloadParameters.selectivelyDisclosable().setGender("male");

        payloadParameters.selectivelyDisclosable().setZoneinfo("Europe/Luxembourg");
        payloadParameters.selectivelyDisclosable().setLocale("en-LU");

        payloadParameters.selectivelyDisclosable().setPhoneNumberVerified(Boolean.TRUE);

        payloadParameters.selectivelyDisclosable().setUpdatedAt(new Date(1711929600000L)); // 2024-04-01

        payloadParameters.selectivelyDisclosable().setBirthMiddleName("William");

        payloadParameters.selectivelyDisclosable().setSalutation("Mr.");

        payloadParameters.selectivelyDisclosable().setDateOfIssuance(new Date(1704067200000L)); // 2024-01-01
        payloadParameters.selectivelyDisclosable().setDateOfExpiry(new Date(1735689600000L));   // 2025-01-01

        SDJWTEAAClaimObject employment = SDJWTEAAClaim.createObject("employment");

        employment.addChild(SDJWTEAAClaim.create("company", "OpenAI"));
        employment.addChild(SDJWTEAAClaim.create("role", "Engineer"));

        SDJWTEAAClaimArray skills = SDJWTEAAClaim.createArray("skills");
        skills.addElement(SDJWTEAAClaim.create("Java"));
        skills.addElement(SDJWTEAAClaim.create("OAuth"));
        skills.addElement(SDJWTEAAClaim.create("SD-JWT"));

        employment.addChild(skills);

        payloadParameters.selectivelyDisclosable().getOtherClaims().add(employment);

        signatureParameters = new JAdESSignatureParameters();
        signatureParameters.setDigestAlgorithm(DigestAlgorithm.SHA256);
        signatureParameters.setSigningCertificate(getSigningCert());
        signatureParameters.setCertificateChain(getCertificateChain());

        signatureParameters.setIncludeKeyIdentifier(false);
        signatureParameters.setX509Url("https://pki.nowina.lu/eaa/pub-eaa.crt");
    }

    @Override
    protected SignedDocumentValidator getValidator(DSSDocument signedDocument) {
        SignedDocumentValidator documentValidator = super.getValidator(signedDocument);
        CommonX509URLCertificateSource x509URLCertificateSource = new CommonX509URLCertificateSource();
        x509URLCertificateSource.addCertificate("https://pki.nowina.lu/eaa/pub-eaa.crt", getSigningCert());
        documentValidator.setSigningCertificateSource(x509URLCertificateSource);
        return documentValidator;
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

        Map<String, XmlDigestMatcher> digestMatchers = eaa.getDigestMatchers().stream()
                .filter(dm -> dm.getDisclosableClaim() != null)
                .collect(Collectors.toMap(
                        dm -> dm.getDisclosableClaim().getName(),
                        Function.identity()));

        assertDigestValue(digestMatchers, "given_name", "John");
        assertDigestValue(digestMatchers, "family_name", "Doe");
        assertDigestValue(digestMatchers, "birthdate", "2000-01-01");
        assertDigestValue(digestMatchers, "email", "john.doe@example.com");
        assertDigestValue(digestMatchers, "phone_number", "+352123456789");

        assertDigestValue(digestMatchers, "address", "{\"street_address\": \"Main Street\", \"country\": \"LU\", \"formatted\": \"1 Main Street\", \"locality\": \"Kehlen\", \"house_number\": \"1\", \"region\": \"Capellen\", \"postal_code\": \"L-1234\"}");

        assertDigestValue(digestMatchers, "place_of_birth", "{\"country\": \"LU\", \"locality\": \"Luxembourg City\", \"region\": \"Luxembourg\"}");

        assertDigestValue(digestMatchers, "birth_given_name", "Johnny");
        assertDigestValue(digestMatchers, "birth_family_name", "Doe");
        assertDigestValue(digestMatchers, "title", "Dr");
        assertDigestValue(digestMatchers, "msisdn", "+352987654321");
        assertDigestValue(digestMatchers, "also_known_as", "JD");

        assertDigestValue(digestMatchers, "personal_administrative_number", "ADM987654");

        assertDigestValue(digestMatchers, "issuing_country", "LU");
        assertDigestValue(digestMatchers, "issuing_authority", "Government of Luxembourg");
        assertDigestValue(digestMatchers, "issuing_jurisdiction", "LU-LU");
        assertDigestValue(digestMatchers, "document_number", "DOC123456");

        assertDigestValue(digestMatchers, "age_in_years", "25");
        assertDigestValue(digestMatchers, "age_birth_year", "2000");
        assertDigestValue(digestMatchers, "age_equal_or_over", "{\"18\": true, \"21\": false}");

        assertDigestValue(digestMatchers, "trust_anchor", "https://example.com/trust-anchor");

        assertDigestValue(digestMatchers, "iss_reg_id", "REG-123456");

        assertDigestValue(digestMatchers, "subAttrs", "{\"sub_id\": \"SUBJ-123456\"}");

        // OpenID specific

        assertDigestValue(digestMatchers, "picture", "https://example.com/john.jpg");
        assertDigestValue(digestMatchers, "nickname", "johnny");
        assertDigestValue(digestMatchers, "preferred_username", "jd");
        assertDigestValue(digestMatchers, "name", "Dr. John Doe");
        assertDigestValue(digestMatchers, "middle_name", "William");

        assertDigestValue(digestMatchers, "profile", "https://example.com/profile/john");
        assertDigestValue(digestMatchers, "website", "https://johndoe.example");

        assertDigestValue(digestMatchers, "email_verified", "true");
        assertDigestValue(digestMatchers, "gender", "male");
        assertDigestValue(digestMatchers, "zoneinfo", "Europe/Luxembourg");
        assertDigestValue(digestMatchers, "locale", "en-LU");
        assertDigestValue(digestMatchers, "phone_number_verified", "true");

        assertDigestValue(digestMatchers, "birth_middle_name", "William");
        assertDigestValue(digestMatchers, "salutation", "Mr.");

        assertDigestValue(digestMatchers, "date_of_issuance", "2024-01-01");
        assertDigestValue(digestMatchers, "date_of_expiry", "2025-01-01");

        assertDigestValue(digestMatchers, "updated_at", "1711929600");

        // custom object claim
        assertTrue(digestMatchers.containsKey("employment"));
    }

    private void assertDigestValue(Map<String, XmlDigestMatcher> digestMatchers,
                                   String claimName,
                                   String expectedValue) {
        XmlDigestMatcher matcher = digestMatchers.get(claimName);

        assertNotNull(matcher, "Missing digest matcher for claim: " + claimName);
        assertNotNull(matcher.getDisclosableClaim(), "Claim name containing an error: " + claimName);
        assertEquals(expectedValue, matcher.getDisclosableClaim().getValue(), "Claim name containing an error: " + claimName);
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