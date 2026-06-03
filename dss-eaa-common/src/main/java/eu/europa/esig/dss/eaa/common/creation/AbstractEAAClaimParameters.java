package eu.europa.esig.dss.eaa.common.creation;

import eu.europa.esig.dss.eaa.common.creation.claim.EAAClaim;
import eu.europa.esig.dss.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Contains common parameters used within SD-JWT VC and mdoc implementations
 *
 */
public abstract class AbstractEAAClaimParameters<C extends EAAClaim> {

    /* OpenID Connect Core 1.0 */

    /**
     * The user's first or given name information
     */
    private String givenName;

    /**
     * The user's last name or surname information
     */
    private String familyName;

    /**
     * The user's birthdate
     */
    private Date birthdate;

    /**
     * User's nationalities using ICAO 3-letter codes
     */
    private List<String> nationalities;

    /**
     * The user's preferred email address
     */
    private String email;

    /**
     * The user's preferred telephone number
     */
    private String phoneNumber;

    /* Address */

    /**
     * The place where the mDL holder resides and/or may be contacted
     */
    private String addressFull;

    /**
     * The house number where the user currently resides
     */
    private String addressHouseNumber;

    /**
     * The name of the street where the user currently resides
     */
    private String addressStreet;

    /**
     * The city where the user currently resides
     */
    private String addressCity;

    /**
     * The state/province/district where the user currently resides
     */
    private String addressState;

    /**
     * The postal code where the user currently resides
     */
    private String addressPostalCode;

    /**
     * The country where the user currently resides
     */
    private String addressCountry;

    /* OpenID Connect for Identity Assurance Claims Registration 1.0 */

    /**
     * User's place of birth country (PID Rulebook)
     */
    private String placeOfBirthCountry;

    /**
     * User's place of birth region (PID Rulebook)
     */
    private String placeOfBirthRegion;

    /**
     * User's place of birth locality (PID Rulebook)
     */
    private String placeOfBirthLocality;

    /**
     * User's first or given name when they were born
     */
    private String birthGivenName;

    /**
     * User's family or last name when they were born
     */
    private String birthFamilyName;

    /**
     * User's title, e.g., "Dr"
     */
    private String title;

    /**
     * User's mobile phone number
     */
    private String mobilePhoneNumber;

    /**
     * User's stage name, religious name or any other type of alias/pseudonym
     */
    private String pseudonym;

    /* PID Rulebook claims */

    /**
     * An audit control number assigned by the issuing authority
     */
    private String personalAdministrativeNumber;

    /**
     * The user's gender
     */
    private Integer sex;

    /**
     * Alpha-2 country code, as defined in ISO 3166-1, of the issuing authority’s country or territory
     */
    private String issuingCountry;

    /**
     * Issuing authority name
     */
    private String issuingAuthority;

    /**
     * Country subdivision code of the jurisdiction that issued the mDL
     */
    private String issuingJurisdiction;

    /**
     * The number assigned or calculated by the issuing authority
     */
    private String documentNumber;

    /**
     * The age of the mDL holder
     */
    private Integer ageInYears;

    /**
     * The year when the mDL holder was born
     */
    private Integer ageBirthYear;

    /**
     * URL at which a machine-readable version of the trust anchor can be found
     */
    private String trustAnchor;

    /**
     * Age attestation identifiers
     */
    private Map<Integer, Boolean> ageOverNN;

    /* ETSI TS 119 472-1 qualified claims */

    /**
     * The registration identifier of the legal entity on whose behalf the EAA has been issued
     */
    private String issuingAuthorityRegistrationIdentifier;

    /**
     * The date when the data (e.g. a PID) was issued
     */
    private Date administrativeIssuanceDate;

    /**
     * The date when the data (e.g. a PID) will expire
     */
    private Date administrativeExpirationDate;

    /**
     * The family name of the attribute subject
     */
    private String attestedAttributesSubjectFamilyName;

    /**
     * The given name of the attribute subject
     */
    private String attestedAttributesSubjectGivenName;

    /**
     * The number of the personal identification data assigned to the attribute subject
     */
    private String attestedAttributesSubjectDocumentNumber;

    /**
     * The subject attribute pseudonym
     */
    private String attestedAttributesSubjectPseudonym;

    /**
     * Contains a list of other arbitrary provided claims
     */
    private final List<C> otherClaims = new ArrayList<>();

    /**
     * Default constructor
     */
    protected AbstractEAAClaimParameters() {
        // empty
    }

    /**
     * Gets the user's first or given name information
     *
     * @return {@link String}
     */
    public String getGivenName() {
        return givenName;
    }

    /**
     * Sets the user's first or given name information
     *
     * @param givenName {@link String}
     */
    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    /**
     * Gets the user's last name or surname information
     *
     * @return {@link String}
     */
    public String getFamilyName() {
        return familyName;
    }

    /**
     * Sets the user's last name or surname information
     *
     * @param familyName {@link String}
     */
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    /**
     * Gets the user's preferred email address
     *
     * @return {@link String}
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's preferred email address
     *
     * @param email {@link String}
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the user's gender
     *
     * @return {@link Integer}
     */
    public Integer getSex() {
        return sex;
    }

    /**
     * Sets the user's gender.
     * The value is represented by an integer, and defined in ISO/IEC 18013-1 and ISO/IEC 18013-2.
     *
     * @param sex {@link Integer}
     */
    public void setSex(Integer sex) {
        this.sex = sex;
    }

    /**
     * Gets the user's birthdate
     *
     * @return {@link Date}
     */
    public Date getBirthdate() {
        return birthdate;
    }

    /**
     * Sets the user's birthdate
     *
     * @param birthdate {@link Date}
     */
    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    /**
     * Gets the user's preferred telephone number
     *
     * @return {@link String}
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the user's preferred telephone number
     *
     * @param phoneNumber {@link String}
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the place where the mDL holder resides and/or may be contacted (street/house number, municipality etc.).
     * The value shall only use latin1 characters and shall have a maximum length of 150 characters.
     *
     * @return {@link String}
     */
    public String getAddressFull() {
        return addressFull;
    }

    /**
     * Sets the place where the mDL holder resides and/or may be contacted (street/house number, municipality etc.).
     * The value shall only use latin1 characters and shall have a maximum length of 150 characters.
     *
     * @param addressFull {@link String}
     */
    public void setAddressFull(String addressFull) {
        this.addressFull = addressFull;
    }

    /**
     * Gets the name of the street where the user to whom the person identification data relates currently resides.
     *
     * @return {@link String}
     */
    public String getAddressStreet() {
        return addressStreet;
    }

    /**
     * Sets the name of the street where the user to whom the person identification data relates currently resides.
     *
     * @param addressStreet {@link String}
     */
    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    /**
     * Gets the house number where the user to whom the person identification data relates currently resides,
     * including any affix or suffix.
     *
     * @return {@link String}
     */
    public String getAddressHouseNumber() {
        return addressHouseNumber;
    }

    /**
     * Sets the house number where the user to whom the person identification data relates currently resides,
     * including any affix or suffix.
     *
     * @param addressHouseNumber {@link String}
     */
    public void setAddressHouseNumber(String addressHouseNumber) {
        this.addressHouseNumber = addressHouseNumber;
    }

    /**
     * Gets the city where the mDL holder lives.
     *
     * @return {@link String}
     */
    public String getAddressCity() {
        return addressCity;
    }

    /**
     * Sets the city where the mDL holder lives.
     *
     * @param addressCity {@link String}
     */
    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    /**
     * Gets the state/province/district where the mDL holder lives.
     *
     * @return {@link String}
     */
    public String getAddressState() {
        return addressState;
    }

    /**
     * Sets the state/province/district where the mDL holder lives.
     *
     * @param addressState {@link String}
     */
    public void setAddressState(String addressState) {
        this.addressState = addressState;
    }

    /**
     * Gets the postal code of the mDL holder.
     *
     * @return {@link String}
     */
    public String getAddressPostalCode() {
        return addressPostalCode;
    }

    /**
     * Sets the postal code of the mDL holder.
     *
     * @param addressPostalCode {@link String}
     */
    public void setAddressPostalCode(String addressPostalCode) {
        this.addressPostalCode = addressPostalCode;
    }

    /**
     * Gets the country where the mDL holder lives as a two letter country code (alpha-2 code)
     * defined in ISO 3166-1.
     *
     * @return {@link String}
     */
    public String getAddressCountry() {
        return addressCountry;
    }

    /**
     * Sets the country where the mDL holder lives as a two letter country code (alpha-2 code)
     * defined in ISO 3166-1.
     *
     * @param addressCountry {@link String}
     */
    public void setAddressCountry(String addressCountry) {
        this.addressCountry = addressCountry;
    }

    /**
     * Gets user's place of birth country
     *
     * @return {@link String}
     */
    public String getPlaceOfBirthCountry() {
        return placeOfBirthCountry;
    }

    /**
     * Sets user's place of birth country
     *
     * @param placeOfBirthCountry {@link String}
     */
    public void setPlaceOfBirthCountry(String placeOfBirthCountry) {
        this.placeOfBirthCountry = placeOfBirthCountry;
    }

    /**
     * Gets user's place of birth region
     *
     * @return {@link String}
     */
    public String getPlaceOfBirthRegion() {
        return placeOfBirthRegion;
    }

    /**
     * Sets user's place of birth region
     *
     * @param placeOfBirthRegion {@link String}
     */
    public void setPlaceOfBirthRegion(String placeOfBirthRegion) {
        this.placeOfBirthRegion = placeOfBirthRegion;
    }

    /**
     * Gets user's place of birth locality
     *
     * @return {@link String}
     */
    public String getPlaceOfBirthLocality() {
        return placeOfBirthLocality;
    }

    /**
     * Sets user's place of birth locality
     *
     * @param placeOfBirthLocality {@link String}
     */
    public void setPlaceOfBirthLocality(String placeOfBirthLocality) {
        this.placeOfBirthLocality = placeOfBirthLocality;
    }

    /**
     * Gets user's nationalities using ICAO 3-letter codes
     *
     * @return a list of {@link String}s
     */
    public List<String> getNationalities() {
        return nationalities;
    }

    /**
     * Sets user's nationalities using ICAO 3-letter codes.
     * This type of nationality providing is used within EAA documents conformant to PID Rulebook.
     *
     * @param nationalities an array of of {@link String}s
     */
    public void setNationalities(String... nationalities) {
        if (Utils.isArrayNotEmpty(nationalities)) {
            this.nationalities = Arrays.asList(nationalities);
        } else {
            this.nationalities = null;
        }
    }

    /**
     * Sets user's nationalities using ICAO 3-letter codes.
     * This type of nationality providing is used within EAA documents conformant to PID Rulebook.
     *
     * @param nationalities a list of {@link String}s
     */
    public void setNationalities(List<String> nationalities) {
        this.nationalities = nationalities;
    }

    /**
     * Gets user's first or given name when they were born
     *
     * @return {@link String}
     */
    public String getBirthGivenName() {
        return birthGivenName;
    }

    /**
     * Sets user's first or given name when they were born
     *
     * @param birthGivenName {@link String}
     */
    public void setBirthGivenName(String birthGivenName) {
        this.birthGivenName = birthGivenName;
    }

    /**
     * Gets user's family or last name when they were born
     *
     * @return {@link String}
     */
    public String getBirthFamilyName() {
        return birthFamilyName;
    }

    /**
     * Sets user's family or last name when they were born
     *
     * @param birthFamilyName {@link String}
     */
    public void setBirthFamilyName(String birthFamilyName) {
        this.birthFamilyName = birthFamilyName;
    }

    /**
     * Gets user's title, e.g., "Dr"
     *
     * @return {@link String}
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets user's title, e.g., "Dr"
     *
     * @param title {@link String}
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets user's mobile phone number
     *
     * @return {@link String}
     */
    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    /**
     * Sets user's mobile phone number
     *
     * @param mobilePhoneNumber {@link String}
     */
    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    /**
     * Gets user's stage name, religious name or any other type of alias/pseudonym
     *
     * @return {@link String}
     */
    public String getPseudonym() {
        return pseudonym;
    }

    /**
     * Sets user's stage name, religious name or any other type of alias/pseudonym
     *
     * @param pseudonym {@link String}
     */
    public void setPseudonym(String pseudonym) {
        this.pseudonym = pseudonym;
    }

    /**
     * Gets alpha-2 country code, as defined in ISO 3166-1, of the issuing authority’s country or territory
     *
     * @return {@link String}
     */
    public String getIssuingCountry() {
        return issuingCountry;
    }

    /**
     * Sets alpha-2 country code, as defined in ISO 3166-1, of the issuing authority’s country or territory
     *
     * @param issuingCountry {@link String}
     */
    public void setIssuingCountry(String issuingCountry) {
        this.issuingCountry = issuingCountry;
    }

    /**
     * Gets issuing authority name.
     * The value shall only use latin1 characters and shall have a maximum length of 150 characters.
     *
     * @return {@link String}
     */
    public String getIssuingAuthority() {
        return issuingAuthority;
    }

    /**
     * Sets issuing authority name.
     * The value shall only use latin1 characters and shall have a maximum length of 150 characters.
     *
     * @param issuingAuthority {@link String}
     */
    public void setIssuingAuthority(String issuingAuthority) {
        this.issuingAuthority = issuingAuthority;
    }

    /**
     * Gets the number assigned or calculated by the issuing authority.
     * The value shall only use latin1 characters and shall have a maximum length of 150 characters.
     *
     * @return {@link String}
     */
    public String getDocumentNumber() {
        return documentNumber;
    }

    /**
     * Sets the number assigned or calculated by the issuing authority.
     * The value shall only use latin1 characters and shall have a maximum length of 150 characters.
     *
     * @param documentNumber {@link String}
     */
    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    /**
     * An audit control number assigned by the issuing authority.
     * The value shall only use latin1 characters and shall have a maximum length of 150 characters.
     *
     * @return {@link String}
     */
    public String getPersonalAdministrativeNumber() {
        return personalAdministrativeNumber;
    }

    /**
     * Sets an audit control number assigned by the issuing authority.
     * The value shall only use latin1 characters and shall have a maximum length of 150 characters.
     *
     * @param personalAdministrativeNumber {@link String}
     */
    public void setPersonalAdministrativeNumber(String personalAdministrativeNumber) {
        this.personalAdministrativeNumber = personalAdministrativeNumber;
    }

    /**
     * Gets the date the age of the mDL holder
     *
     * @return {@link Number}
     */
    public Integer getAgeInYears() {
        return ageInYears;
    }

    /**
     * Sets the date the age of the mDL holder
     *
     * @param ageInYears {@link Number}
     */
    public void setAgeInYears(Integer ageInYears) {
        this.ageInYears = ageInYears;
    }

    /**
     * Gets the year when the mDL holder was born
     *
     * @return {@link Number}
     */
    public Integer getAgeBirthYear() {
        return ageBirthYear;
    }

    /**
     * Sets the year when the mDL holder was born
     *
     * @param ageBirthYear {@link Number}
     */
    public void setAgeBirthYear(Integer ageBirthYear) {
        this.ageBirthYear = ageBirthYear;
    }

    /**
     * Gets a list of elements is used to convey to an mDL verifier, in a data-minimized fashion, if the mDL holder
     * is as old or older than a specified age, or if the mDL holder is younger than a specified age.
     *
     * @return a map between {@link Integer} age and {@link Boolean} values
     */
    public Map<Integer, Boolean> getAgeOverNN() {
        return ageOverNN;
    }

    /**
     * Sets a list of elements is used to convey to an mDL verifier, in a data-minimized fashion, if the mDL holder
     * is as old or older than a specified age, or if the mDL holder is younger than a specified age.
     *
     * @param age {@link Integer} representing an age
     * @param isOver {@link Boolean} defining whether the equal or over the defined age
     */
    public void setAgeOverNN(Integer age, Boolean isOver) {
        if (ageOverNN == null) {
            this.ageOverNN = new LinkedHashMap<>();
        }
        this.ageOverNN.put(age, isOver);
    }

    /**
     * Gets a country subdivision code of the jurisdiction that issued the mDL as defined in
     * ISO 3166-2:2020, Clause 8.
     *
     * @return {@link String}
     */
    public String getIssuingJurisdiction() {
        return issuingJurisdiction;
    }

    /**
     * Sets a country subdivision code of the jurisdiction that issued the mDL as defined in
     * ISO 3166-2:2020, Clause 8.
     *
     * @param issuingJurisdiction {@link String}
     */
    public void setIssuingJurisdiction(String issuingJurisdiction) {
        this.issuingJurisdiction = issuingJurisdiction;
    }

    /**
     * Gets the URL at which a machine-readable version of the trust anchor to be used for
     * verifying the PID can be found or looked up.
     *
     * @return {@link String}
     */
    public String getTrustAnchor() {
        return trustAnchor;
    }

    /**
     * Sets the URL at which a machine-readable version of the trust anchor to be used for
     * verifying the PID can be found or looked up.
     *
     * @param trustAnchor {@link String}
     */
    public void setTrustAnchor(String trustAnchor) {
        this.trustAnchor = trustAnchor;
    }

    /**
     * Gets the registration identifier of the legal entity on whose behalf the EAA has been issued.
     *
     * @return {@link String}
     */
    public String getIssuingAuthorityRegistrationIdentifier() {
        return issuingAuthorityRegistrationIdentifier;
    }

    /**
     * Sets the registration identifier of the legal entity on whose behalf the EAA has been issued.
     *
     * @param issuingAuthorityRegistrationIdentifier {@link String}
     */
    public void setIssuingAuthorityRegistrationIdentifier(String issuingAuthorityRegistrationIdentifier) {
        this.issuingAuthorityRegistrationIdentifier = issuingAuthorityRegistrationIdentifier;
    }

    /**
     * Gets the date when the data (e.g. a PID) was issued
     *
     * @return {@link Date}
     */
    public Date getAdministrativeIssuanceDate() {
        return administrativeIssuanceDate;
    }

    /**
     * Sets the date when the data (e.g. a PID) was issued
     *
     * @param administrativeIssuanceDate {@link Date}
     */
    public void setAdministrativeIssuanceDate(Date administrativeIssuanceDate) {
        this.administrativeIssuanceDate = administrativeIssuanceDate;
    }

    /**
     * Gets the date when the data (e.g. a PID) will expire
     *
     * @return {@link Date}
     */
    public Date getAdministrativeExpirationDate() {
        return administrativeExpirationDate;
    }

    /**
     * Sets the date when the data (e.g. a PID) will expire
     *
     * @param administrativeExpirationDate {@link Date}
     */
    public void setAdministrativeExpirationDate(Date administrativeExpirationDate) {
        this.administrativeExpirationDate = administrativeExpirationDate;
    }

    /**
     * Gets the family name of the attribute subject
     *
     * @return {@link String}
     */
    public String getAttestedAttributesSubjectFamilyName() {
        return attestedAttributesSubjectFamilyName;
    }

    /**
     * Gets the given name of the attribute subject
     *
     * @return {@link String}
     */
    public String getAttestedAttributesSubjectGivenName() {
        return attestedAttributesSubjectGivenName;
    }

    /**
     * Gets the document number of the attribute subject
     *
     * @return {@link String}
     */
    public String getAttestedAttributesSubjectDocumentNumber() {
        return attestedAttributesSubjectDocumentNumber;
    }

    /**
     * Sets the claim for associating a set of attributes to one entity different than the EAA subject,
     * when no pseudonym is used.
     *
     * @param familyName {@link String} the family name of the attribute subject
     * @param givenName {@link String} the given name of the attribute subject
     * @param documentNumber {@link String} the number of the personal identification data assigned to the attribute subject
     */
    public void setAttestedAttributesSubject(String familyName, String givenName, String documentNumber) {
        Objects.requireNonNull(familyName, "Attested Attributes Subject family name cannot be null!");
        Objects.requireNonNull(givenName, "Attested Attributes Subject given name cannot be null!");
        Objects.requireNonNull(documentNumber, "Attested Attributes Subject document number cannot be null!");
        this.attestedAttributesSubjectFamilyName = familyName;
        this.attestedAttributesSubjectGivenName = givenName;
        this.attestedAttributesSubjectDocumentNumber = documentNumber;
    }

    /**
     * Gets the pseudonym of the attribute subject
     *
     * @return {@link String}
     */
    public String getAttestedAttributesSubjectPseudonym() {
        return attestedAttributesSubjectPseudonym;
    }

    /**
     * Sets the claim for associating a set of attributes to one entity different than the EAA subject,
     * when pseudonym is used.
     *
     * @param pseudonym {@link String}  the subject attribute pseudonym
     */
    public void setAttestedAttributesSubjectPseudonym(String pseudonym) {
        Objects.requireNonNull(pseudonym, "Attested Attributes Subject pseudonym cannot be null!");
        this.attestedAttributesSubjectPseudonym = pseudonym;
    }

    /**
     * Adds a new claim.
     * A hash will be computed for the claim, if applicable.
     *
     * @param claim {@link EAAClaim} to add
     */
    public void addClaim(C claim) {
        if (claim != null) {
            otherClaims.add(claim);
        }
    }

    /**
     * Gets a list of other arbitrary provided claims
     *
     * @return a list of other claims
     */
    public List<C> getOtherClaims() {
        return otherClaims;
    }

    @Override
    public String toString() {
        return "AbstractEAAClaimParameters [" +
                "givenName='" + givenName + '\'' +
                ", familyName='" + familyName + '\'' +
                ", birthdate=" + birthdate +
                ", nationalities=" + nationalities +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", addressFull='" + addressFull + '\'' +
                ", addressHouseNumber='" + addressHouseNumber + '\'' +
                ", addressStreet='" + addressStreet + '\'' +
                ", addressCity='" + addressCity + '\'' +
                ", addressState='" + addressState + '\'' +
                ", addressPostalCode='" + addressPostalCode + '\'' +
                ", addressCountry='" + addressCountry + '\'' +
                ", placeOfBirthCountry='" + placeOfBirthCountry + '\'' +
                ", placeOfBirthRegion='" + placeOfBirthRegion + '\'' +
                ", placeOfBirthLocality='" + placeOfBirthLocality + '\'' +
                ", birthGivenName='" + birthGivenName + '\'' +
                ", birthFamilyName='" + birthFamilyName + '\'' +
                ", title='" + title + '\'' +
                ", mobilePhoneNumber='" + mobilePhoneNumber + '\'' +
                ", pseudonym='" + pseudonym + '\'' +
                ", personalAdministrativeNumber='" + personalAdministrativeNumber + '\'' +
                ", sex=" + sex +
                ", issuingCountry='" + issuingCountry + '\'' +
                ", issuingAuthority='" + issuingAuthority + '\'' +
                ", issuingJurisdiction='" + issuingJurisdiction + '\'' +
                ", documentNumber='" + documentNumber + '\'' +
                ", ageInYears=" + ageInYears +
                ", ageBirthYear=" + ageBirthYear +
                ", trustAnchor='" + trustAnchor + '\'' +
                ", ageOverNN=" + ageOverNN +
                ", issuingAuthorityRegistrationIdentifier='" + issuingAuthorityRegistrationIdentifier + '\'' +
                ", administrativeIssuanceDate=" + administrativeIssuanceDate +
                ", administrativeExpirationDate=" + administrativeExpirationDate +
                ", attestedAttributesSubjectFamilyName='" + attestedAttributesSubjectFamilyName + '\'' +
                ", attestedAttributesSubjectGivenName='" + attestedAttributesSubjectGivenName + '\'' +
                ", attestedAttributesSubjectDocumentNumber='" + attestedAttributesSubjectDocumentNumber + '\'' +
                ", attestedAttributesSubjectPseudonym='" + attestedAttributesSubjectPseudonym + '\'' +
                ", otherClaims=" + otherClaims +
                ']';
    }

}
