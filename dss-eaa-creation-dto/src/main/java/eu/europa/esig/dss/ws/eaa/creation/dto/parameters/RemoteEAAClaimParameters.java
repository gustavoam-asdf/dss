package eu.europa.esig.dss.ws.eaa.creation.dto.parameters;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * DTO containing claims that may be made selectively disclosable
 * 
 */
public class RemoteEAAClaimParameters implements Serializable {

    private static final long serialVersionUID = -5502634016096583321L;

    /* Common claims */
    
    /** The user's first or given name information */
    private String givenName;
    /** The user's last name or surname information */
    private String familyName;
    /** The user's birthdate */
    private Date birthdate;
    /** User's nationalities using ICAO 3-letter codes */
    private List<String> nationalities;
    /** The user's preferred email address */
    private String email;
    /** The user's preferred telephone number */
    private String phoneNumber;

    /** The place where the mDL holder resides and/or may be contacted */
    private String postalAddress;
    /** The house number where the user currently resides */
    private String addressHouseNumber;
    /** The name of the street where the user currently resides */
    private String addressStreet;
    /** The city where the user currently resides */
    private String addressCity;
    /** The state/province/district where the user currently resides */
    private String addressState;
    /** The postal code where the user currently resides */
    private String addressPostalCode;
    /** The country where the user currently resides */
    private String addressCountry;

    /** User's place of birth country (PID Rulebook) */
    private String placeOfBirthCountry;
    /** User's place of birth region (PID Rulebook) */
    private String placeOfBirthRegion;
    /** User's place of birth locality (PID Rulebook) */
    private String placeOfBirthLocality;
    /** User's first or given name when they were born */
    private String birthGivenName;
    /** User's family or last name when they were born */
    private String birthFamilyName;
    /** User's title, e.g., "Dr" */
    private String title;
    /** User's mobile phone number */
    private String mobilePhoneNumber;
    /** User's stage name, religious name or any other type of alias/pseudonym */
    private String pseudonym;

    /** An audit control number assigned by the issuing authority */
    private String personalAdministrativeNumber;
    /** The user's gender */
    private Integer sex;
    /** Alpha-2 country code, as defined in ISO 3166-1, of the issuing authority’s country or territory */
    private String issuingCountry;
    /** Issuing authority name */
    private String issuingAuthority;
    /** Country subdivision code of the jurisdiction that issued the mDL */
    private String issuingJurisdiction;
    /** The number assigned or calculated by the issuing authority */
    private String documentNumber;
    /** The age of the mDL holder */
    private Integer ageInYears;
    /** The year when the mDL holder was born */
    private Integer ageBirthYear;
    /** URL at which a machine-readable version of the trust anchor can be found */
    private String trustAnchor;
    /** Age attestation identifiers */
    private List<AgeOverNNDTO> ageOverNN;

    /** The registration identifier of the legal entity on whose behalf the EAA has been issued */
    private String issuingAuthorityRegistrationIdentifier;
    /** The date when the data (e.g. a PID) was issued */
    private Date administrativeIssuanceDate;
    /** The date when the data (e.g. a PID) will expire */
    private Date administrativeExpirationDate;

    /* SD-JWT VC attributes */

    /** URL of the End-User's profile picture. */
    private String picture;
    /** Casual or informal name by which the End-User wishes to be referred to. */
    private String nickname;
    /** Preferred shorthand name or nickname of the End-User. */
    private String preferredNickname;
    /** Full name of the End-User in displayable form. */
    private String name;
    /** Middle name(s) of the End-User. */
    private String middleName;
    /** URL of the End-User's profile page. */
    private String profile;
    /** URL of the End-User's personal website or blog. */
    private String website;
    /** Indicates whether the End-User's email address has been verified. */
    private Boolean emailVerified;
    /** End-User's gender. */
    private String gender;
    /** End-User's time zone, represented as an IANA time zone identifier. */
    private String zoneinfo;
    /** End-User's locale, represented as a BCP47 language tag. */
    private String locale;
    /** Indicates whether the End-User's phone number has been verified. */
    private Boolean phoneNumberVerified;
    /** Time when the End-User's information was last updated. */
    private Date updatedAt;

    /** Middle name(s) assigned to the End-User at birth. */
    private String birthMiddleName;
    /** Salutation or honorific used when addressing the End-User (e.g. Mr., Ms., Dr.). */
    private String salutation;

    /** Expiration date of the identity document or credential. */
    private Date dateOfExpiry;
    /** Issuance date of the identity document or credential. */
    private Date dateOfIssuance;

    /** The subject attribute identifier */
    private String attestedAttributesSubjectIdentifier;
    /** The subject attribute pseudonym*/
    private String attestedAttributesSubjectPseudonym;
    /** The list of attributes associated with the attribute subject */
    private List<String> attestedAttributes;
    
    /* Mdoc attributes */

    /** The user's birthdate approximate mask */
    private String birthdateApproximateMask;
    /** User's place of birth (ISO/IEC 18013-5) */
    private String placeOfBirth;
    /** User's nationality as a two letter country code (alpha-2 code) defined in ISO 3166-1 */
    private String nationality;
    /** A reproduction of the mDL holder’s portrait */
    private byte[] portrait;
    /** Driving privileges of the mDL holder */
    private List<DrivingPrivilegeDTO> drivingPrivileges;
    /** The distinguishing sign of the issuing country according to ISO/IEC 18013-1:2018, Annex F */
    private String distinguishingSign;
    /** The holder’s height in centimetres */
    private Integer height;
    /** The holder’s weight in kilograms */
    private Integer weight;
    /** The mDL holder’s eye colour */
    private String eyeColour;
    /** The mDL holder’s hair colour */
    private String hairColour;
    /** The date when portrait was taken */
    private Date portraitCaptureDate;
    /** Biometric information of the mDL holder */
    private List<BiometricTemplateNNDTO> biometricTemplate;
    /** Face ID biometric information of the mDL holder */
    private byte[] biometricTemplateFace;
    /** An image of the signature or usual mark of the mDL holder */
    private byte[] signatureUsualMark;
    /** A reproduction of the holder’s fingerprint data */
    private byte[] fingerprint;
    /** A business name of the holder */
    private String businessName;
    /** A name of legal person */
    private String organizationName;
    /** The name(s) which holder was born */
    private String birthFullName;
    /** The profession of the holder */
    private String profession;
    /** The father of the holder */
    private String relationshipFather;
    /** The mother of the holder */
    private String relationshipMother;
    /** The parent of the holder */
    private String relationshipParent;
    /** The son of the holder */
    private String relationshipSon;
    /** The daughter of the holder */
    private String relationshipDaughter;
    /** The brother of the holder */
    private String relationshipBrother;
    /** The sister of the holder */
    private String relationshipSister;
    /** The sibling of the holder */
    private String relationshipSibling;
    /** The spouse of the holder */
    private String relationshipSpouse;
    /** The father-in-law of the holder */
    private String relationshipFatherInLaw;
    /** The mother-in-law of the holder */
    private String relationshipMotherInLaw;
    /** The parent-in-law of the holder */
    private String relationshipParentInLaw;
    /** The son-in-law of the holder */
    private String relationshipSonInLaw;
    /** The daughter-in-law of the holder */
    private String relationshipDaughterInLaw;
    /** The child-in-law of the holder */
    private String relationshipChildInLaw;
    /** The parental authority of the holder */
    private String relationshipParentalAuthority;
    /** The legal representative of the holder */
    private String relationshipLegalRepresentative;
    /** The voluntary agent of the holder */
    private String relationshipAgent;
    /** The document type */
    private String documentType;
    /** The family name of the attribute subject */
    private String attestedAttributesSubjectFamilyName;
    /** The given name of the attribute subject */
    private String attestedAttributesSubjectGivenName;
    /** The number of the personal identification data assigned to the attribute subject */
    private String attestedAttributesSubjectDocumentNumber;

    /* Custom claims */

    /** List of custom claims */
    private List<ClaimDTO> otherClaims;

    /**
     * Default constructor
     */
    public RemoteEAAClaimParameters() {
        // empty
    }

    /**
     * Gets the given name
     *
     * @return {@link String}
     */
    public String getGivenName() {
        return givenName;
    }

    /**
     * Sets the given name
     *
     * @param givenName {@link String}
     */
    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    /**
     * Gets the family name
     *
     * @return {@link String}
     */
    public String getFamilyName() {
        return familyName;
    }

    /**
     * Sets the family name
     *
     * @param familyName {@link String}
     */
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    /**
     * Gets the birthdate
     *
     * @return {@link Date}
     */
    public Date getBirthdate() {
        return birthdate;
    }

    /**
     * Sets the birthdate
     *
     * @param birthdate {@link Date}
     */
    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    /**
     * Gets the nationalities
     *
     * @return {@link List<String>}
     */
    public List<String> getNationalities() {
        return nationalities;
    }

    /**
     * Sets the nationalities
     *
     * @param nationalities {@link List<String>}
     */
    public void setNationalities(List<String> nationalities) {
        this.nationalities = nationalities;
    }

    /**
     * Gets the email
     *
     * @return {@link String}
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email
     *
     * @param email {@link String}
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the phone number
     *
     * @return {@link String}
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number
     *
     * @param phoneNumber {@link String}
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the postal address
     *
     * @return {@link String}
     */
    public String getPostalAddress() {
        return postalAddress;
    }

    /**
     * Sets the postal address
     *
     * @param postalAddress {@link String}
     */
    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    /**
     * Gets the address house number
     *
     * @return {@link String}
     */
    public String getAddressHouseNumber() {
        return addressHouseNumber;
    }

    /**
     * Sets the address house number
     *
     * @param addressHouseNumber {@link String}
     */
    public void setAddressHouseNumber(String addressHouseNumber) {
        this.addressHouseNumber = addressHouseNumber;
    }

    /**
     * Gets the address street
     *
     * @return {@link String}
     */
    public String getAddressStreet() {
        return addressStreet;
    }

    /**
     * Sets the address street
     *
     * @param addressStreet {@link String}
     */
    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    /**
     * Gets the address city
     *
     * @return {@link String}
     */
    public String getAddressCity() {
        return addressCity;
    }

    /**
     * Sets the address city
     *
     * @param addressCity {@link String}
     */
    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    /**
     * Gets the address state
     *
     * @return {@link String}
     */
    public String getAddressState() {
        return addressState;
    }

    /**
     * Sets the address state
     *
     * @param addressState {@link String}
     */
    public void setAddressState(String addressState) {
        this.addressState = addressState;
    }

    /**
     * Gets the address postal code
     *
     * @return {@link String}
     */
    public String getAddressPostalCode() {
        return addressPostalCode;
    }

    /**
     * Sets the address postal code
     *
     * @param addressPostalCode {@link String}
     */
    public void setAddressPostalCode(String addressPostalCode) {
        this.addressPostalCode = addressPostalCode;
    }

    /**
     * Gets the address country
     *
     * @return {@link String}
     */
    public String getAddressCountry() {
        return addressCountry;
    }

    /**
     * Sets the address country
     *
     * @param addressCountry {@link String}
     */
    public void setAddressCountry(String addressCountry) {
        this.addressCountry = addressCountry;
    }

    /**
     * Gets the place of birth country
     *
     * @return {@link String}
     */
    public String getPlaceOfBirthCountry() {
        return placeOfBirthCountry;
    }

    /**
     * Sets the place of birth country
     *
     * @param placeOfBirthCountry {@link String}
     */
    public void setPlaceOfBirthCountry(String placeOfBirthCountry) {
        this.placeOfBirthCountry = placeOfBirthCountry;
    }

    /**
     * Gets the place of birth region
     *
     * @return {@link String}
     */
    public String getPlaceOfBirthRegion() {
        return placeOfBirthRegion;
    }

    /**
     * Sets the place of birth region
     *
     * @param placeOfBirthRegion {@link String}
     */
    public void setPlaceOfBirthRegion(String placeOfBirthRegion) {
        this.placeOfBirthRegion = placeOfBirthRegion;
    }

    /**
     * Gets the place of birth locality
     *
     * @return {@link String}
     */
    public String getPlaceOfBirthLocality() {
        return placeOfBirthLocality;
    }

    /**
     * Sets the place of birth locality
     *
     * @param placeOfBirthLocality {@link String}
     */
    public void setPlaceOfBirthLocality(String placeOfBirthLocality) {
        this.placeOfBirthLocality = placeOfBirthLocality;
    }

    /**
     * Gets the birth given name
     *
     * @return {@link String}
     */
    public String getBirthGivenName() {
        return birthGivenName;
    }

    /**
     * Sets the birth given name
     *
     * @param birthGivenName {@link String}
     */
    public void setBirthGivenName(String birthGivenName) {
        this.birthGivenName = birthGivenName;
    }

    /**
     * Gets the birth family name
     *
     * @return {@link String}
     */
    public String getBirthFamilyName() {
        return birthFamilyName;
    }

    /**
     * Sets the birth family name
     *
     * @param birthFamilyName {@link String}
     */
    public void setBirthFamilyName(String birthFamilyName) {
        this.birthFamilyName = birthFamilyName;
    }

    /**
     * Gets the title
     *
     * @return {@link String}
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title
     *
     * @param title {@link String}
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the mobile phone number
     *
     * @return {@link String}
     */
    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    /**
     * Sets the mobile phone number
     *
     * @param mobilePhoneNumber {@link String}
     */
    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    /**
     * Gets the pseudonym
     *
     * @return {@link String}
     */
    public String getPseudonym() {
        return pseudonym;
    }

    /**
     * Sets the pseudonym
     *
     * @param pseudonym {@link String}
     */
    public void setPseudonym(String pseudonym) {
        this.pseudonym = pseudonym;
    }

    /**
     * Gets the personal administrative number
     *
     * @return {@link String}
     */
    public String getPersonalAdministrativeNumber() {
        return personalAdministrativeNumber;
    }

    /**
     * Sets the personal administrative number
     *
     * @param personalAdministrativeNumber {@link String}
     */
    public void setPersonalAdministrativeNumber(String personalAdministrativeNumber) {
        this.personalAdministrativeNumber = personalAdministrativeNumber;
    }

    /**
     * Gets the sex
     *
     * @return {@link Integer}
     */
    public Integer getSex() {
        return sex;
    }

    /**
     * Sets the sex
     *
     * @param sex {@link Integer}
     */
    public void setSex(Integer sex) {
        this.sex = sex;
    }

    /**
     * Gets the issuing country
     *
     * @return {@link String}
     */
    public String getIssuingCountry() {
        return issuingCountry;
    }

    /**
     * Sets the issuing country
     *
     * @param issuingCountry {@link String}
     */
    public void setIssuingCountry(String issuingCountry) {
        this.issuingCountry = issuingCountry;
    }

    /**
     * Gets the issuing authority
     *
     * @return {@link String}
     */
    public String getIssuingAuthority() {
        return issuingAuthority;
    }

    /**
     * Sets the issuing authority
     *
     * @param issuingAuthority {@link String}
     */
    public void setIssuingAuthority(String issuingAuthority) {
        this.issuingAuthority = issuingAuthority;
    }

    /**
     * Gets the issuing jurisdiction
     *
     * @return {@link String}
     */
    public String getIssuingJurisdiction() {
        return issuingJurisdiction;
    }

    /**
     * Sets the issuing jurisdiction
     *
     * @param issuingJurisdiction {@link String}
     */
    public void setIssuingJurisdiction(String issuingJurisdiction) {
        this.issuingJurisdiction = issuingJurisdiction;
    }

    /**
     * Gets the document number
     *
     * @return {@link String}
     */
    public String getDocumentNumber() {
        return documentNumber;
    }

    /**
     * Sets the document number
     *
     * @param documentNumber {@link String}
     */
    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    /**
     * Gets the age in years
     *
     * @return {@link Integer}
     */
    public Integer getAgeInYears() {
        return ageInYears;
    }

    /**
     * Sets the age in years
     *
     * @param ageInYears {@link Integer}
     */
    public void setAgeInYears(Integer ageInYears) {
        this.ageInYears = ageInYears;
    }

    /**
     * Gets the age birth year
     *
     * @return {@link Integer}
     */
    public Integer getAgeBirthYear() {
        return ageBirthYear;
    }

    /**
     * Sets the age birth year
     *
     * @param ageBirthYear {@link Integer}
     */
    public void setAgeBirthYear(Integer ageBirthYear) {
        this.ageBirthYear = ageBirthYear;
    }

    /**
     * Gets the trust anchor
     *
     * @return {@link String}
     */
    public String getTrustAnchor() {
        return trustAnchor;
    }

    /**
     * Sets the trust anchor
     *
     * @param trustAnchor {@link String}
     */
    public void setTrustAnchor(String trustAnchor) {
        this.trustAnchor = trustAnchor;
    }

    /**
     * Gets the age attestation identifiers
     *
     * @return {@link List<AgeOverNNDTO>}
     */
    public List<AgeOverNNDTO> getAgeOverNN() {
        return ageOverNN;
    }

    /**
     * Sets the age attestation identifiers
     *
     * @param ageOverNN {@link List<AgeOverNNDTO>}
     */
    public void setAgeOverNN(List<AgeOverNNDTO> ageOverNN) {
        this.ageOverNN = ageOverNN;
    }

    /**
     * Gets the issuing authority registration identifier
     *
     * @return {@link String}
     */
    public String getIssuingAuthorityRegistrationIdentifier() {
        return issuingAuthorityRegistrationIdentifier;
    }

    /**
     * Sets the issuing authority registration identifier
     *
     * @param issuingAuthorityRegistrationIdentifier {@link String}
     */
    public void setIssuingAuthorityRegistrationIdentifier(String issuingAuthorityRegistrationIdentifier) {
        this.issuingAuthorityRegistrationIdentifier = issuingAuthorityRegistrationIdentifier;
    }

    /**
     * Gets the administrative issuance date
     *
     * @return {@link Date}
     */
    public Date getAdministrativeIssuanceDate() {
        return administrativeIssuanceDate;
    }

    /**
     * Sets the administrative issuance date
     *
     * @param administrativeIssuanceDate {@link Date}
     */
    public void setAdministrativeIssuanceDate(Date administrativeIssuanceDate) {
        this.administrativeIssuanceDate = administrativeIssuanceDate;
    }

    /**
     * Gets the administrative expiration date
     *
     * @return {@link Date}
     */
    public Date getAdministrativeExpirationDate() {
        return administrativeExpirationDate;
    }

    /**
     * Sets the administrative expiration date
     *
     * @param administrativeExpirationDate {@link Date}
     */
    public void setAdministrativeExpirationDate(Date administrativeExpirationDate) {
        this.administrativeExpirationDate = administrativeExpirationDate;
    }

    /**
     * Gets the picture (SD-JWT VC only)
     *
     * @return {@link String}
     */
    public String getPicture() {
        return picture;
    }

    /**
     * Sets the picture (SD-JWT VC only)
     *
     * @param picture {@link String}
     */
    public void setPicture(String picture) {
        this.picture = picture;
    }

    /**
     * Gets the nickname (SD-JWT VC only)
     *
     * @return {@link String}
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Sets the nickname (SD-JWT VC only)
     *
     * @param nickname {@link String}
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Gets the preferred nickname (SD-JWT VC only)
     *
     * @return {@link String}
     */
    public String getPreferredNickname() {
        return preferredNickname;
    }

    /**
     * Sets the preferred nickname (SD-JWT VC only)
     *
     * @param preferredNickname {@link String}
     */
    public void setPreferredNickname(String preferredNickname) {
        this.preferredNickname = preferredNickname;
    }

    /**
     * Gets the name (SD-JWT VC only)
     *
     * @return {@link String}
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name (SD-JWT VC only)
     *
     * @param name {@link String}
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the middle name (SD-JWT VC only)
     *
     * @return {@link String}
     */
    public String getMiddleName() {
        return middleName;
    }

    /**
     * Sets the middle name (SD-JWT VC only)
     *
     * @param middleName {@link String}
     */
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    /**
     * Gets the profile URL (SD-JWT VC only)
     *
     * @return {@link String}
     */
    public String getProfile() {
        return profile;
    }

    /**
     * Sets the profile URL (SD-JWT VC only)
     *
     * @param profile {@link String}
     */
    public void setProfile(String profile) {
        this.profile = profile;
    }

    /**
     * Gets the website URL (SD-JWT VC only)
     *
     * @return {@link String}
     */
    public String getWebsite() {
        return website;
    }

    /**
     * Sets the website URL (SD-JWT VC only)
     *
     * @param website {@link String}
     */
    public void setWebsite(String website) {
        this.website = website;
    }

    /**
     * Gets whether the email has been verified (SD-JWT VC only)
     *
     * @return {@link Boolean}
     */
    public Boolean getEmailVerified() {
        return emailVerified;
    }

    /**
     * Sets whether the email has been verified (SD-JWT VC only)
     *
     * @param emailVerified {@link Boolean}
     */
    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    /**
     * Gets the gender (SD-JWT VC only)
     *
     * @return {@link String}
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the gender (SD-JWT VC only)
     *
     * @param gender {@link String}
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Gets the zone information (SD-JWT VC only)
     *
     * @return {@link String}
     */
    public String getZoneinfo() {
        return zoneinfo;
    }

    /**
     * Sets the zone information (SD-JWT VC only)
     *
     * @param zoneinfo {@link String}
     */
    public void setZoneinfo(String zoneinfo) {
        this.zoneinfo = zoneinfo;
    }

    /**
     * Gets the locale (SD-JWT VC only)
     *
     * @return {@link String}
     */
    public String getLocale() {
        return locale;
    }

    /**
     * Sets the locale (SD-JWT VC only)
     *
     * @param locale {@link String}
     */
    public void setLocale(String locale) {
        this.locale = locale;
    }

    /**
     * Gets whether the phone number has been verified (SD-JWT VC only)
     *
     * @return {@link Boolean}
     */
    public Boolean getPhoneNumberVerified() {
        return phoneNumberVerified;
    }

    /**
     * Sets whether the phone number has been verified (SD-JWT VC only)
     *
     * @param phoneNumberVerified {@link Boolean}
     */
    public void setPhoneNumberVerified(Boolean phoneNumberVerified) {
        this.phoneNumberVerified = phoneNumberVerified;
    }

    /**
     * Gets the updated at date (SD-JWT VC only)
     *
     * @return {@link Date}
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets the updated at date (SD-JWT VC only)
     *
     * @param updatedAt {@link Date}
     */
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Gets the birth middle name (SD-JWT VC only)
     *
     * @return {@link String}
     */
    public String getBirthMiddleName() {
        return birthMiddleName;
    }

    /**
     * Sets the birth middle name (SD-JWT VC only)
     *
     * @param birthMiddleName {@link String}
     */
    public void setBirthMiddleName(String birthMiddleName) {
        this.birthMiddleName = birthMiddleName;
    }

    /**
     * Gets the salutation (SD-JWT VC only)
     *
     * @return {@link String}
     */
    public String getSalutation() {
        return salutation;
    }

    /**
     * Sets the salutation (SD-JWT VC only)
     *
     * @param salutation {@link String}
     */
    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    /**
     * Gets the date of expiry (SD-JWT VC only)
     *
     * @return {@link Date}
     */
    public Date getDateOfExpiry() {
        return dateOfExpiry;
    }

    /**
     * Sets the date of expiry (SD-JWT VC only)
     *
     * @param dateOfExpiry {@link Date}
     */
    public void setDateOfExpiry(Date dateOfExpiry) {
        this.dateOfExpiry = dateOfExpiry;
    }

    /**
     * Gets the date of issuance (SD-JWT VC only)
     *
     * @return {@link Date}
     */
    public Date getDateOfIssuance() {
        return dateOfIssuance;
    }

    /**
     * Sets the date of issuance (SD-JWT VC only)
     *
     * @param dateOfIssuance {@link Date}
     */
    public void setDateOfIssuance(Date dateOfIssuance) {
        this.dateOfIssuance = dateOfIssuance;
    }

    /**
     * Gets the attested attributes subject identifier (SD-JWT VC only)
     *
     * @return {@link String}
     */
    public String getAttestedAttributesSubjectIdentifier() {
        return attestedAttributesSubjectIdentifier;
    }

    /**
     * Sets the attested attributes subject identifier (SD-JWT VC only)
     *
     * @param attestedAttributesSubjectIdentifier {@link String}
     */
    public void setAttestedAttributesSubjectIdentifier(String attestedAttributesSubjectIdentifier) {
        this.attestedAttributesSubjectIdentifier = attestedAttributesSubjectIdentifier;
    }

    /**
     * Gets the attested attributes subject pseudonym (SD-JWT VC and mdoc)
     *
     * @return {@link String}
     */
    public String getAttestedAttributesSubjectPseudonym() {
        return attestedAttributesSubjectPseudonym;
    }

    /**
     * Sets the attested attributes subject pseudonym (SD-JWT VC and mdoc)
     *
     * @param attestedAttributesSubjectPseudonym {@link String}
     */
    public void setAttestedAttributesSubjectPseudonym(String attestedAttributesSubjectPseudonym) {
        this.attestedAttributesSubjectPseudonym = attestedAttributesSubjectPseudonym;
    }

    /**
     * Gets the attested attributes (SD-JWT VC only)
     *
     * @return {@link List<String>}
     */
    public List<String> getAttestedAttributes() {
        return attestedAttributes;
    }

    /**
     * Sets the attested attributes (SD-JWT VC only)
     *
     * @param attestedAttributes {@link List<String>}
     */
    public void setAttestedAttributes(List<String> attestedAttributes) {
        this.attestedAttributes = attestedAttributes;
    }

    /**
     * Gets the birthdate approximate mask (Mdoc only)
     *
     * @return {@link String}
     */
    public String getBirthdateApproximateMask() {
        return birthdateApproximateMask;
    }

    /**
     * Sets the birthdate approximate mask (Mdoc only)
     *
     * @param birthdateApproximateMask {@link String}
     */
    public void setBirthdateApproximateMask(String birthdateApproximateMask) {
        this.birthdateApproximateMask = birthdateApproximateMask;
    }

    /**
     * Gets the place of birth (Mdoc only)
     *
     * @return {@link String}
     */
    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    /**
     * Sets the place of birth (Mdoc only)
     *
     * @param placeOfBirth {@link String}
     */
    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    /**
     * Gets the nationality (Mdoc only)
     *
     * @return {@link String}
     */
    public String getNationality() {
        return nationality;
    }

    /**
     * Sets the nationality (Mdoc only)
     *
     * @param nationality {@link String}
     */
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    /**
     * Gets the portrait (Mdoc only)
     *
     * @return byte[]
     */
    public byte[] getPortrait() {
        return portrait;
    }

    /**
     * Sets the portrait (Mdoc only)
     *
     * @param portrait byte[]
     */
    public void setPortrait(byte[] portrait) {
        this.portrait = portrait;
    }

    /**
     * Gets the driving privileges (Mdoc only)
     *
     * @return {@link List<DrivingPrivilegeDTO>}
     */
    public List<DrivingPrivilegeDTO> getDrivingPrivileges() {
        return drivingPrivileges;
    }

    /**
     * Sets the driving privileges (Mdoc only)
     *
     * @param drivingPrivileges {@link List<DrivingPrivilegeDTO>}
     */
    public void setDrivingPrivileges(List<DrivingPrivilegeDTO> drivingPrivileges) {
        this.drivingPrivileges = drivingPrivileges;
    }

    /**
     * Gets the distinguishing sign (Mdoc only)
     *
     * @return {@link String}
     */
    public String getDistinguishingSign() {
        return distinguishingSign;
    }

    /**
     * Sets the distinguishing sign (Mdoc only)
     *
     * @param distinguishingSign {@link String}
     */
    public void setDistinguishingSign(String distinguishingSign) {
        this.distinguishingSign = distinguishingSign;
    }

    /**
     * Gets the height (Mdoc only)
     *
     * @return {@link Integer}
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * Sets the height (Mdoc only)
     *
     * @param height {@link Integer}
     */
    public void setHeight(Integer height) {
        this.height = height;
    }

    /**
     * Gets the weight (Mdoc only)
     *
     * @return {@link Integer}
     */
    public Integer getWeight() {
        return weight;
    }

    /**
     * Sets the weight (Mdoc only)
     *
     * @param weight {@link Integer}
     */
    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    /**
     * Gets the eye colour (Mdoc only)
     *
     * @return {@link String}
     */
    public String getEyeColour() {
        return eyeColour;
    }

    /**
     * Sets the eye colour (Mdoc only)
     *
     * @param eyeColour {@link String}
     */
    public void setEyeColour(String eyeColour) {
        this.eyeColour = eyeColour;
    }

    /**
     * Gets the hair colour (Mdoc only)
     *
     * @return {@link String}
     */
    public String getHairColour() {
        return hairColour;
    }

    /**
     * Sets the hair colour (Mdoc only)
     *
     * @param hairColour {@link String}
     */
    public void setHairColour(String hairColour) {
        this.hairColour = hairColour;
    }

    /**
     * Gets the portrait capture date (Mdoc only)
     *
     * @return {@link Date}
     */
    public Date getPortraitCaptureDate() {
        return portraitCaptureDate;
    }

    /**
     * Sets the portrait capture date (Mdoc only)
     *
     * @param portraitCaptureDate {@link Date}
     */
    public void setPortraitCaptureDate(Date portraitCaptureDate) {
        this.portraitCaptureDate = portraitCaptureDate;
    }

    /**
     * Gets the biometric templates (Mdoc only)
     *
     * @return {@link List<BiometricTemplateNNDTO>}
     */
    public List<BiometricTemplateNNDTO> getBiometricTemplate() {
        return biometricTemplate;
    }

    /**
     * Sets the biometric templates (Mdoc only)
     *
     * @param biometricTemplate {@link List<BiometricTemplateNNDTO>}
     */
    public void setBiometricTemplate(List<BiometricTemplateNNDTO> biometricTemplate) {
        this.biometricTemplate = biometricTemplate;
    }

    /**
     * Gets the face biometric template (Mdoc only)
     *
     * @return byte[]
     */
    public byte[] getBiometricTemplateFace() {
        return biometricTemplateFace;
    }

    /**
     * Sets the face biometric template (Mdoc only)
     *
     * @param biometricTemplateFace byte[]
     */
    public void setBiometricTemplateFace(byte[] biometricTemplateFace) {
        this.biometricTemplateFace = biometricTemplateFace;
    }

    /**
     * Gets the signature or usual mark (Mdoc only)
     *
     * @return byte[]
     */
    public byte[] getSignatureUsualMark() {
        return signatureUsualMark;
    }

    /**
     * Sets the signature or usual mark (Mdoc only)
     *
     * @param signatureUsualMark byte[]
     */
    public void setSignatureUsualMark(byte[] signatureUsualMark) {
        this.signatureUsualMark = signatureUsualMark;
    }

    /**
     * Gets the fingerprint (Mdoc only)
     *
     * @return byte[]
     */
    public byte[] getFingerprint() {
        return fingerprint;
    }

    /**
     * Sets the fingerprint (Mdoc only)
     *
     * @param fingerprint byte[]
     */
    public void setFingerprint(byte[] fingerprint) {
        this.fingerprint = fingerprint;
    }

    /**
     * Gets the business name (Mdoc only)
     *
     * @return {@link String}
     */
    public String getBusinessName() {
        return businessName;
    }

    /**
     * Sets the business name (Mdoc only)
     *
     * @param businessName {@link String}
     */
    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    /**
     * Gets the organization name (Mdoc only)
     *
     * @return {@link String}
     */
    public String getOrganizationName() {
        return organizationName;
    }

    /**
     * Sets the organization name (Mdoc only)
     *
     * @param organizationName {@link String}
     */
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    /**
     * Gets the birth full name (Mdoc only)
     *
     * @return {@link String}
     */
    public String getBirthFullName() {
        return birthFullName;
    }

    /**
     * Sets the birth full name (Mdoc only)
     *
     * @param birthFullName {@link String}
     */
    public void setBirthFullName(String birthFullName) {
        this.birthFullName = birthFullName;
    }

    /**
     * Gets the profession (Mdoc only)
     *
     * @return {@link String}
     */
    public String getProfession() {
        return profession;
    }

    /**
     * Sets the profession (Mdoc only)
     *
     * @param profession {@link String}
     */
    public void setProfession(String profession) {
        this.profession = profession;
    }

    /**
     * Gets the father's name (Mdoc only)
     *
     * @return {@link String}
     */
    public String getRelationshipFather() {
        return relationshipFather;
    }

    /**
     * Sets the father's name (Mdoc only)
     *
     * @param relationshipFather {@link String}
     */
    public void setRelationshipFather(String relationshipFather) {
        this.relationshipFather = relationshipFather;
    }

    /**
     * Gets the mother's name (Mdoc only)
     *
     * @return {@link String}
     */
    public String getRelationshipMother() {
        return relationshipMother;
    }

    /**
     * Sets the mother's name (Mdoc only)
     *
     * @param relationshipMother {@link String}
     */
    public void setRelationshipMother(String relationshipMother) {
        this.relationshipMother = relationshipMother;
    }

    /**
     * Gets the parent's name (Mdoc only)
     *
     * @return {@link String}
     */
    public String getRelationshipParent() {
        return relationshipParent;
    }

    /**
     * Sets the parent's name (Mdoc only)
     *
     * @param relationshipParent {@link String}
     */
    public void setRelationshipParent(String relationshipParent) {
        this.relationshipParent = relationshipParent;
    }

    /**
     * Gets the son's name (Mdoc only)
     *
     * @return {@link String}
     */
    public String getRelationshipSon() {
        return relationshipSon;
    }

    /**
     * Sets the son's name (Mdoc only)
     *
     * @param relationshipSon {@link String}
     */
    public void setRelationshipSon(String relationshipSon) {
        this.relationshipSon = relationshipSon;
    }

    /**
     * Gets the daughter's name (Mdoc only)
     *
     * @return {@link String}
     */
    public String getRelationshipDaughter() {
        return relationshipDaughter;
    }

    /**
     * Sets the daughter's name (Mdoc only)
     *
     * @param relationshipDaughter {@link String}
     */
    public void setRelationshipDaughter(String relationshipDaughter) {
        this.relationshipDaughter = relationshipDaughter;
    }

    /**
     * Gets the sibling's brother name (Mdoc only)
     *
     * @return {@link String}
     */
    public String getRelationshipBrother() {
        return relationshipBrother;
    }

    /**
     * Sets the sibling's brother name (Mdoc only)
     *
     * @param relationshipBrother {@link String}
     */
    public void setRelationshipBrother(String relationshipBrother) {
        this.relationshipBrother = relationshipBrother;
    }

    /**
     * Gets the sibling's sister name (Mdoc only)
     *
     * @return {@link String}
     */
    public String getRelationshipSister() {
        return relationshipSister;
    }

    /**
     * Sets the sibling's sister name (Mdoc only)
     *
     * @param relationshipSister {@link String}
     */
    public void setRelationshipSister(String relationshipSister) {
        this.relationshipSister = relationshipSister;
    }

    /**
     * Gets the sibling's name (Mdoc only)
     *
     * @return {@link String}
     */
    public String getRelationshipSibling() {
        return relationshipSibling;
    }

    /**
     * Sets the sibling's name (Mdoc only)
     *
     * @param relationshipSibling {@link String}
     */
    public void setRelationshipSibling(String relationshipSibling) {
        this.relationshipSibling = relationshipSibling;
    }

    /**
     * Gets the spouse's name (Mdoc only)
     *
     * @return {@link String}
     */
    public String getRelationshipSpouse() {
        return relationshipSpouse;
    }

    /**
     * Sets the spouse's name (Mdoc only)
     *
     * @param relationshipSpouse {@link String}
     */
    public void setRelationshipSpouse(String relationshipSpouse) {
        this.relationshipSpouse = relationshipSpouse;
    }

    /**
     * Gets the father-in-law's name (Mdoc only)
     *
     * @return {@link String}
     */
    public String getRelationshipFatherInLaw() {
        return relationshipFatherInLaw;
    }

    /**
     * Sets the father-in-law's name (Mdoc only)
     *
     * @param relationshipFatherInLaw {@link String}
     */
    public void setRelationshipFatherInLaw(String relationshipFatherInLaw) {
        this.relationshipFatherInLaw = relationshipFatherInLaw;
    }

    /**
     * Gets the mother-in-law's name (Mdoc only)
     *
     * @return {@link String}
     */
    public String getRelationshipMotherInLaw() {
        return relationshipMotherInLaw;
    }

    /**
     * Sets the mother-in-law's name (Mdoc only)
     *
     * @param relationshipMotherInLaw {@link String}
     */
    public void setRelationshipMotherInLaw(String relationshipMotherInLaw) {
        this.relationshipMotherInLaw = relationshipMotherInLaw;
    }

    /**
     * Gets the parent-in-law's name (Mdoc only)
     *
     * @return {@link String}
     */
    public String getRelationshipParentInLaw() {
        return relationshipParentInLaw;
    }

    /**
     * Sets the parent-in-law's name (Mdoc only)
     *
     * @param relationshipParentInLaw {@link String}
     */
    public void setRelationshipParentInLaw(String relationshipParentInLaw) {
        this.relationshipParentInLaw = relationshipParentInLaw;
    }

    /**
     * Gets the son-in-law's name (Mdoc only)
     *
     * @return {@link String}
     */
    public String getRelationshipSonInLaw() {
        return relationshipSonInLaw;
    }

    /**
     * Sets the son-in-law's name (Mdoc only)
     *
     * @param relationshipSonInLaw {@link String}
     */
    public void setRelationshipSonInLaw(String relationshipSonInLaw) {
        this.relationshipSonInLaw = relationshipSonInLaw;
    }

    /**
     * Gets the daughter-in-law's name (Mdoc only)
     *
     * @return {@link String}
     */
    public String getRelationshipDaughterInLaw() {
        return relationshipDaughterInLaw;
    }

    /**
     * Sets the daughter-in-law's name (Mdoc only)
     *
     * @param relationshipDaughterInLaw {@link String}
     */
    public void setRelationshipDaughterInLaw(String relationshipDaughterInLaw) {
        this.relationshipDaughterInLaw = relationshipDaughterInLaw;
    }

    /**
     * Gets the child-in-law's name (Mdoc only)
     *
     * @return {@link String}
     */
    public String getRelationshipChildInLaw() {
        return relationshipChildInLaw;
    }

    /**
     * Sets the child-in-law's name (Mdoc only)
     *
     * @param relationshipChildInLaw {@link String}
     */
    public void setRelationshipChildInLaw(String relationshipChildInLaw) {
        this.relationshipChildInLaw = relationshipChildInLaw;
    }

    /**
     * Gets the parental authority (Mdoc only)
     *
     * @return {@link String}
     */
    public String getRelationshipParentalAuthority() {
        return relationshipParentalAuthority;
    }

    /**
     * Sets the parental authority (Mdoc only)
     *
     * @param relationshipParentalAuthority {@link String}
     */
    public void setRelationshipParentalAuthority(String relationshipParentalAuthority) {
        this.relationshipParentalAuthority = relationshipParentalAuthority;
    }

    /**
     * Gets the legal representative (Mdoc only)
     *
     * @return {@link String}
     */
    public String getRelationshipLegalRepresentative() {
        return relationshipLegalRepresentative;
    }

    /**
     * Sets the legal representative (Mdoc only)
     *
     * @param relationshipLegalRepresentative {@link String}
     */
    public void setRelationshipLegalRepresentative(String relationshipLegalRepresentative) {
        this.relationshipLegalRepresentative = relationshipLegalRepresentative;
    }

    /**
     * Gets the voluntary agent (Mdoc only)
     *
     * @return {@link String}
     */
    public String getRelationshipAgent() {
        return relationshipAgent;
    }

    /**
     * Sets the voluntary agent (Mdoc only)
     *
     * @param relationshipAgent {@link String}
     */
    public void setRelationshipAgent(String relationshipAgent) {
        this.relationshipAgent = relationshipAgent;
    }

    /**
     * Gets the document type (Mdoc only)
     *
     * @return {@link String}
     */
    public String getDocumentType() {
        return documentType;
    }

    /**
     * Sets the document type (Mdoc only)
     *
     * @param documentType {@link String}
     */
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    /**
     * Gets the attested attributes subject family name (Mdoc only)
     *
     * @return {@link String}
     */
    public String getAttestedAttributesSubjectFamilyName() {
        return attestedAttributesSubjectFamilyName;
    }

    /**
     * Sets the attested attributes subject family name (Mdoc only)
     *
     * @param attestedAttributesSubjectFamilyName {@link String}
     */
    public void setAttestedAttributesSubjectFamilyName(String attestedAttributesSubjectFamilyName) {
        this.attestedAttributesSubjectFamilyName = attestedAttributesSubjectFamilyName;
    }

    /**
     * Gets the attested attributes subject given name (Mdoc only)
     *
     * @return {@link String}
     */
    public String getAttestedAttributesSubjectGivenName() {
        return attestedAttributesSubjectGivenName;
    }

    /**
     * Sets the attested attributes subject given name (Mdoc only)
     *
     * @param attestedAttributesSubjectGivenName {@link String}
     */
    public void setAttestedAttributesSubjectGivenName(String attestedAttributesSubjectGivenName) {
        this.attestedAttributesSubjectGivenName = attestedAttributesSubjectGivenName;
    }

    /**
     * Gets the attested attributes subject document number (Mdoc only)
     *
     * @return {@link String}
     */
    public String getAttestedAttributesSubjectDocumentNumber() {
        return attestedAttributesSubjectDocumentNumber;
    }

    /**
     * Sets the attested attributes subject document number (Mdoc only)
     *
     * @param attestedAttributesSubjectDocumentNumber {@link String}
     */
    public void setAttestedAttributesSubjectDocumentNumber(String attestedAttributesSubjectDocumentNumber) {
        this.attestedAttributesSubjectDocumentNumber = attestedAttributesSubjectDocumentNumber;
    }

    /**
     * Gets a list of other claims
     *
     * @return a list of {@link ClaimDTO}s
     */
    public List<ClaimDTO> getOtherClaims() {
        return otherClaims;
    }

    /**
     * Sets other custom claims
     *
     * @param otherClaims a list of {@link ClaimDTO}s
     */
    public void setOtherClaims(List<ClaimDTO> otherClaims) {
        this.otherClaims = otherClaims;
    }

}
