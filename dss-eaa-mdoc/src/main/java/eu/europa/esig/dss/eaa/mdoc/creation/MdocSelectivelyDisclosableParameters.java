package eu.europa.esig.dss.eaa.mdoc.creation;

import eu.europa.esig.dss.eaa.mdoc.creation.claim.MdocEAAClaim;
import eu.europa.esig.dss.eaa.mdoc.model.MdocDrivingPrivilege;
import eu.europa.esig.dss.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Contains a list of selectively disclosable claims
 *
 */
public class MdocSelectivelyDisclosableParameters {

    /**
     * The user's first or given name information
     */
    private String firstName;

    /**
     * The user's last name or surname information
     */
    private String lastName;

    /**
     * The user's preferred email address
     */
    private String email;

    /**
     * The user's gender
     */
    private Integer gender;

    /**
     * The user's birthdate
     */
    private Date birthdate;

    /**
     * The user's preferred telephone number
     */
    private String phoneNumber;

    /**
     * User's place of birth (ISO/IEC 18013-5)
     */
    private String placeOfBirth;

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
     * User's nationality as a two letter country code (alpha-2 code) defined in ISO 3166-1
     */
    private String nationality;

    /**
     * User's nationalities using ICAO 3-letter codes
     */
    private List<String> nationalities;

    /**
     * User's first or given name when they were born
     */
    private String birthFirstName;

    /**
     * User's family or last name when they were born
     */
    private String birthLastName;

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

    /**
     * Alpha-2 country code, as defined in ISO 3166-1, of the issuing authority’s country or territory
     */
    private String issuingCountry;

    /**
     * Issuing authority name
     */
    private String issuingAuthority;

    /**
     * The number assigned or calculated by the issuing authority
     */
    private String documentNumber;

    /**
     * A reproduction of the mDL holder’s portrait
     */
    private byte[] portrait;

    /**
     * Driving privileges of the mDL holder
     */
    private List<MdocDrivingPrivilege> drivingPrivileges;

    /**
     * The distinguishing sign of the issuing country according to ISO/IEC 18013-1:2018, Annex F
     */
    private String distinguishingSign;

    /**
     * An audit control number assigned by the issuing authority
     */
    private String administrativeNumber;

    /**
     * The holder’s height in centimetres
     */
    private Integer height;

    /**
     * The holder’s weight in kilograms
     */
    private Integer weight;

    /**
     * The mDL holder’s eye colour
     */
    private String eyeColour;

    /**
     * The mDL holder’s hair colour
     */
    private String hairColour;

    /**
     * The place where the mDL holder resides and/or may be contacted
     */
    private String residentAddress;

    /**
     * The date when portrait was taken
     */
    private Date portraitCaptureDate;

    /**
     * The age of the mDL holder
     */
    private Integer ageInYears;

    /**
     * The year when the mDL holder was born
     */
    private Integer ageBirthYear;

    /**
     * Age attestation identifiers
     */
    private Map<Integer, Boolean> ageOverNN;

    /**
     * Country subdivision code of the jurisdiction that issued the mDL
     */
    private String issuingJurisdiction;

    /**
     * The city where the mDL holder lives
     */
    private String residentCity;

    /**
     * The state/province/district where the mDL holder lives
     */
    private String residentState;

    /**
     * The postal code of the mDL holder
     */
    private String residentPostalCode;

    /**
     * The country where the mDL holder lives
     */
    private String residentCountry;

    /**
     * Biometric information of the mDL holder
     */
    private Map<String, byte[]> biometricTemplate;

    /**
     * Face ID biometric information of the mDL holder
     */
    private byte[] biometricTemplateFace;

    /**
     * An image of the signature or usual mark of the mDL holder
     */
    private byte[] signatureUsualMark;

    /**
     * A reproduction of the holder’s fingerprint data
     */
    private byte[] fingerprint;

    /**
     * A business name of the holder
     */
    private String businessName;

    /**
     * A name of legal person
     */
    private String organizationName;

    /**
     * The name(s) which holder was born
     */
    private String birthFullName;

    /**
     * The profession of the holder
     */
    private String profession;

    /**
     * The father of the holder
     */
    private String relationshipFather;

    /**
     * The mother of the holder
     */
    private String relationshipMother;

    /**
     * The parent of the holder
     */
    private String relationshipParent;

    /**
     * The son of the holder
     */
    private String relationshipSon;

    /**
     * The daughter of the holder
     */
    private String relationshipDaughter;

    /**
     * The brother of the holder
     */
    private String relationshipBrother;

    /**
     * The sister of the holder
     */
    private String relationshipSister;

    /**
     * The sibling of the holder
     */
    private String relationshipSibling;

    /**
     * The spouse of the holder
     */
    private String relationshipSpouse;

    /**
     * The father-in-law of the holder
     */
    private String relationshipFatherInLaw;

    /**
     * The mother-in-law of the holder
     */
    private String relationshipMotherInLaw;

    /**
     * The parent-in-law of the holder
     */
    private String relationshipParentInLaw;

    /**
     * The son-in-law of the holder
     */
    private String relationshipSonInLaw;

    /**
     * The daughter-in-law of the holder
     */
    private String relationshipDaughterInLaw;

    /**
     * The child-in-law of the holder
     */
    private String relationshipChildInLaw;

    /**
     * The parental authority of the holder
     */
    private String relationshipParentalAuthority;

    /**
     * The legal representative of the holder
     */
    private String relationshipLegalRepresentative;

    /**
     * The voluntary agent of the holder
     */
    private String relationshipAgent;

    /**
     * The document type
     */
    private String documentType;

    /**
     * The date when the data (e.g. a PID) was issued
     */
    private Date administrativeIssuanceDate;

    /**
     * The date when the data (e.g. a PID) will expire
     */
    private Date administrativeExpirationDate;

    /**
     * The name of the street where the user currently resides
     */
    private String residentStreet;

    /**
     * The house number where the user currently resides
     */
    private String residentHouseNumber;

    /**
     * URL at which a machine-readable version of the trust anchor can be found
     */
    private String trustAnchor;

    /**
     * The registration identifier of the legal entity on whose behalf the EAA has been issued
     */
    private String issuingAuthorityRegistrationIdentifier;

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
    private final List<MdocEAAClaim> otherClaims = new ArrayList<>();

    /**
     * Default constructor
     */
    public MdocSelectivelyDisclosableParameters() {
        // empty
    }

    /**
     * Gets the user's first or given name information
     *
     * @return {@link String}
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the user's first or given name information
     *
     * @param firstName {@link String}
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the user's last name or surname information
     *
     * @return {@link String}
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the user's last name or surname information
     *
     * @param lastName {@link String}
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
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
     * @return {@link Object}
     */
    public Object getGender() {
        return gender;
    }

    /**
     * Sets the user's gender.
     * The value is represented by an integer, and defined in ISO/IEC 18013-1 and ISO/IEC 18013-2.
     *
     * @param gender {@link Integer}
     */
    public void setGender(Integer gender) {
        this.gender = gender;
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
     * Gets user's place of birth
     *
     * @return {@link String}
     */
    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    /**
     * Sets user's place of birth
     *
     * @param placeOfBirth {@link String}
     */
    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
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
     * Gets the user's nationality (used in ISO 118013-5 and ISO 23220-2)
     *
     * @return {@link String}
     */
    public String getNationality() {
        return nationality;
    }

    /**
     * Sets the user's nationality as a two letter country code (alpha-2 code) defined in ISO 3166-1.
     * This type of nationality providing is used within EAA documents conformant to ISO 118013-5 and ISO 23220-2.
     *
     * @param nationality {@link String}
     */
    public void setNationality(String nationality) {
        this.nationality = nationality;
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
    public String getBirthFirstName() {
        return birthFirstName;
    }

    /**
     * Sets user's first or given name when they were born
     *
     * @param birthFirstName {@link String}
     */
    public void setBirthFirstName(String birthFirstName) {
        this.birthFirstName = birthFirstName;
    }

    /**
     * Gets user's family or last name when they were born
     *
     * @return {@link String}
     */
    public String getBirthLastName() {
        return birthLastName;
    }

    /**
     * Sets user's family or last name when they were born
     *
     * @param birthLastName {@link String}
     */
    public void setBirthLastName(String birthLastName) {
        this.birthLastName = birthLastName;
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
     * Gets a reproduction of the mDL holder’s portrait.
     *
     * @return {@link byte[]}
     */
    public byte[] getPortrait() {
        return portrait;
    }

    /**
     * Sets a reproduction of the mDL holder’s portrait.
     *
     * @param portrait {@link byte[]}
     */
    public void setPortrait(byte[] portrait) {
        this.portrait = portrait;
    }

    /**
     * Gets driving privileges of the mDL holder.
     *
     * @return a list of {@link MdocDrivingPrivilege}s
     */
    public List<MdocDrivingPrivilege> getDrivingPrivileges() {
        return drivingPrivileges;
    }

    /**
     * Sets driving privileges of the mDL holder.
     *
     * @param drivingPrivileges an array of {@link MdocDrivingPrivilege}s
     */
    public void setDrivingPrivileges(MdocDrivingPrivilege... drivingPrivileges) {
        if (Utils.isArrayNotEmpty(drivingPrivileges)) {
            this.drivingPrivileges = Arrays.asList(drivingPrivileges);
        } else {
            this.drivingPrivileges = null;
        }
    }

    /**
     * Sets driving privileges of the mDL holder.
     *
     * @param drivingPrivileges a list of {@link MdocDrivingPrivilege}s
     */
    public void setDrivingPrivileges(List<MdocDrivingPrivilege> drivingPrivileges) {
        this.drivingPrivileges = drivingPrivileges;
    }

    /**
     * Gets the distinguishing sign of the issuing country according to ISO/IEC 18013-1:2018, Annex F.
     * If no applicable distinguishing sign is available in ISO/IEC 18013-1, an IA may
     * use an empty identifier or another identifier by which it is internationally recognized.
     * In this case the IA should ensure there is no collision with other IA’s.
     *
     * @return {@link String}
     */
    public String getDistinguishingSign() {
        return distinguishingSign;
    }

    /**
     * Sets the distinguishing sign of the issuing country according to ISO/IEC 18013-1:2018, Annex F.
     * If no applicable distinguishing sign is available in ISO/IEC 18013-1, an IA may
     * use an empty identifier or another identifier by which it is internationally recognized.
     * In this case the IA should ensure there is no collision with other IA’s.
     *
     * @param distinguishingSign {@link String}
     */
    public void setDistinguishingSign(String distinguishingSign) {
        this.distinguishingSign = distinguishingSign;
    }

    /**
     * An audit control number assigned by the issuing authority.
     * The value shall only use latin1 characters and shall have a maximum length of 150 characters.
     *
     * @return {@link String}
     */
    public String getAdministrativeNumber() {
        return administrativeNumber;
    }

    /**
     * Sets an audit control number assigned by the issuing authority.
     * The value shall only use latin1 characters and shall have a maximum length of 150 characters.
     *
     * @param administrativeNumber {@link String}
     */
    public void setAdministrativeNumber(String administrativeNumber) {
        this.administrativeNumber = administrativeNumber;
    }

    /**
     * Gets the holder’s height in centimetres
     *
     * @return {@link Number}
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * Sets the holder’s height in centimetres
     *
     * @param height {@link Number}
     */
    public void setHeight(Integer height) {
        this.height = height;
    }

    /**
     * Gets the holder’s height in centimetres
     *
     * @return {@link Number}
     */
    public Integer getWeight() {
        return weight;
    }

    /**
     * Sets the holder’s height in centimetres
     *
     * @param weight {@link Number}
     */
    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    /**
     * Gets the mDL holder’s eye colour. The value shall be one of the following: “black”, “blue”,
     * “brown”, “dichromatic”, “grey”, “green”, “hazel”, “maroon”, “pink”, “unknown”.
     *
     * @return {@link String}
     */
    public String getEyeColour() {
        return eyeColour;
    }

    /**
     * Sets the mDL holder’s eye colour. The value shall be one of the following: “black”, “blue”,
     * “brown”, “dichromatic”, “grey”, “green”, “hazel”, “maroon”, “pink”, “unknown”.
     *
     * @param eyeColour {@link String}
     */
    public void setEyeColour(String eyeColour) {
        this.eyeColour = eyeColour;
    }

    /**
     * Gets the mDL holder’s hair colour. The value shall be one of the following: “bald”, “black”,
     * “blond”, “brown”, “grey”, “red”, “auburn”, “sandy”, “white”, “unknown”.
     *
     * @return {@link String}
     */
    public String getHairColour() {
        return hairColour;
    }

    /**
     * Sets the mDL holder’s hair colour. The value shall be one of the following: “bald”, “black”,
     * “blond”, “brown”, “grey”, “red”, “auburn”, “sandy”, “white”, “unknown”.
     *
     * @param hairColour {@link String}
     */
    public void setHairColour(String hairColour) {
        this.hairColour = hairColour;
    }

    /**
     * Gets the place where the mDL holder resides and/or may be contacted (street/house number, municipality etc.).
     * The value shall only use latin1 characters and shall have a maximum length of 150 characters.
     *
     * @return {@link String}
     */
    public String getResidentAddress() {
        return residentAddress;
    }

    /**
     * Sets the place where the mDL holder resides and/or may be contacted (street/house number, municipality etc.).
     * The value shall only use latin1 characters and shall have a maximum length of 150 characters.
     *
     * @param residentAddress {@link String}
     */
    public void setResidentAddress(String residentAddress) {
        this.residentAddress = residentAddress;
    }

    /**
     * Gets the date when portrait was taken.
     *
     * @return {@link Date}
     */
    public Date getPortraitCaptureDate() {
        return portraitCaptureDate;
    }

    /**
     * Sets the date when portrait was taken.
     *
     * @param portraitCaptureDate {@link Date}
     */
    public void setPortraitCaptureDate(Date portraitCaptureDate) {
        this.portraitCaptureDate = portraitCaptureDate;
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
     * Gets the city where the mDL holder lives.
     *
     * @return {@link String}
     */
    public String getResidentCity() {
        return residentCity;
    }

    /**
     * Sets the city where the mDL holder lives.
     *
     * @param residentCity {@link String}
     */
    public void setResidentCity(String residentCity) {
        this.residentCity = residentCity;
    }

    /**
     * Gets the state/province/district where the mDL holder lives.
     *
     * @return {@link String}
     */
    public String getResidentState() {
        return residentState;
    }

    /**
     * Sets the state/province/district where the mDL holder lives.
     *
     * @param residentState {@link String}
     */
    public void setResidentState(String residentState) {
        this.residentState = residentState;
    }

    /**
     * Gets the postal code of the mDL holder.
     *
     * @return {@link String}
     */
    public String getResidentPostalCode() {
        return residentPostalCode;
    }

    /**
     * Sets the postal code of the mDL holder.
     *
     * @param residentPostalCode {@link String}
     */
    public void setResidentPostalCode(String residentPostalCode) {
        this.residentPostalCode = residentPostalCode;
    }

    /**
     * Gets the country where the mDL holder lives as a two letter country code (alpha-2 code)
     * defined in ISO 3166-1.
     *
     * @return {@link String}
     */
    public String getResidentCountry() {
        return residentCountry;
    }

    /**
     * Sets the country where the mDL holder lives as a two letter country code (alpha-2 code)
     * defined in ISO 3166-1.
     *
     * @param residentCountry {@link String}
     */
    public void setResidentCountry(String residentCountry) {
        this.residentCountry = residentCountry;
    }

    /**
     * Gets a list of elements contains optional facial, fingerprint, iris, or other biometric information of the mDL
     * holder.
     *
     * @return a map of biometric template data
     */
    public Map<String, byte[]> getBiometricTemplate() {
        return biometricTemplate;
    }

    /**
     * Sets a list of elements contains optional facial, fingerprint, iris, or other biometric information of the mDL
     * holder.
     * A biometric template identifier has the format biometric_template_xx
     * where xx shall be replaced with the corresponding “Abstract value name” found in ISO/IEC 19785
     * 3:2020, Table 7, according to the following convention: capitalized characters are replaced with their
     * lowercase equivalent and spaces or non-alphanumeric characters are replaced by underscores (_).
     *
     * @param type {@link String} representing a biometric template type
     * @param data byte array containing the data value of the v
     */
    public void setBiometricTemplate(String type, byte[] data) {
        Objects.requireNonNull(type, "BiometricTemplate type cannot be null!");
        Objects.requireNonNull(data, "BiometricTemplate data cannot be null!");
        if (biometricTemplate == null) {
            this.biometricTemplate = new LinkedHashMap<>();
        }
        this.biometricTemplate.put(type, data);
    }

    /**
     * Gets biometric face Id of the mDL holder
     *
     * @return byte array
     */
    public byte[] getBiometricTemplateFace() {
        return biometricTemplateFace;
    }

    /**
     * Sets the face image of the mDL holder (holder's portrait)
     *
     * @param biometricTemplateFace byte array
     */
    public void setBiometricTemplateFace(byte[] biometricTemplateFace) {
        this.biometricTemplateFace = biometricTemplateFace;
    }

    /**
     * Gets an image of the signature or usual mark of the mDL holder, see 7.2.7 ISO/IEC 18013-5.
     *
     * @return {@link byte[]}
     */
    public byte[] getSignatureUsualMark() {
        return signatureUsualMark;
    }

    /**
     * Sets an image of the signature or usual mark of the mDL holder, see 7.2.7 ISO/IEC 18013-5.
     *
     * @param signatureUsualMark {@link byte[]}
     */
    public void setSignatureUsualMark(byte[] signatureUsualMark) {
        this.signatureUsualMark = signatureUsualMark;
    }

    /**
     * Gets a reproduction of the holder’s fingerprint data (TBC).
     *
     * @return {@link byte[]}
     */
    public byte[] getFingerprint() {
        return fingerprint;
    }

    /**
     * Sets a reproduction of the holder’s fingerprint data (TBC).
     *
     * @param fingerprint {@link byte[]}
     */
    public void setFingerprint(byte[] fingerprint) {
        this.fingerprint = fingerprint;
    }

    /**
     * Gets a business name of the holder.
     *
     * @return {@link String}
     */
    public String getBusinessName() {
        return businessName;
    }

    /**
     * Sets a business name of the holder.
     *
     * @param businessName {@link String}
     */
    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    /**
     * Gets a name of legal person.
     *
     * @return {@link String}
     */
    public String getOrganizationName() {
        return organizationName;
    }

    /**
     * Sets a name of legal person.
     *
     * @param organizationName {@link String}
     */
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    /**
     * Gets the name(s) which holder was born.
     *
     * @return {@link String}
     */
    public String getBirthFullName() {
        return birthFullName;
    }

    /**
     * Sets the name(s) which holder was born.
     *
     * @param birthFullName {@link String}
     */
    public void setBirthFullName(String birthFullName) {
        this.birthFullName = birthFullName;
    }

    /**
     * Gets the profession of the holder.
     *
     * @return {@link String}
     */
    public String getProfession() {
        return profession;
    }

    /**
     * Sets the profession of the holder.
     *
     * @param profession {@link String}
     */
    public void setProfession(String profession) {
        this.profession = profession;
    }

    /**
     * Gets the father of the holder
     *
     * @return {@link String}
     */
    public String getRelationshipFather() {
        return relationshipFather;
    }

    /**
     * Sets the father of the holder
     *
     * @param relationshipFather {@link String}
     */
    public void setRelationshipFather(String relationshipFather) {
        this.relationshipFather = relationshipFather;
    }

    /**
     * Gets the mother of the holder
     *
     * @return {@link String}
     */
    public String getRelationshipMother() {
        return relationshipMother;
    }

    /**
     * Sets the mother of the holder
     *
     * @param relationshipMother {@link String}
     */
    public void setRelationshipMother(String relationshipMother) {
        this.relationshipMother = relationshipMother;
    }

    /**
     * Gets the parent of the holder
     *
     * @return {@link String}
     */
    public String getRelationshipParent() {
        return relationshipParent;
    }

    /**
     * Sets the parent of the holder
     *
     * @param relationshipParent {@link String}
     */
    public void setRelationshipParent(String relationshipParent) {
        this.relationshipParent = relationshipParent;
    }

    /**
     * Gets the son of the holder
     *
     * @return {@link String}
     */
    public String getRelationshipSon() {
        return relationshipSon;
    }

    /**
     * Sets the son of the holder
     *
     * @param relationshipSon {@link String}
     */
    public void setRelationshipSon(String relationshipSon) {
        this.relationshipSon = relationshipSon;
    }

    /**
     * Gets the daughter of the holder
     *
     * @return {@link String}
     */
    public String getRelationshipDaughter() {
        return relationshipDaughter;
    }

    /**
     * Sets the daughter of the holder
     *
     * @param relationshipDaughter {@link String}
     */
    public void setRelationshipDaughter(String relationshipDaughter) {
        this.relationshipDaughter = relationshipDaughter;
    }

    /**
     * Gets the brother of the holder
     *
     * @return {@link String}
     */
    public String getRelationshipBrother() {
        return relationshipBrother;
    }

    /**
     * Sets the brother of the holder
     *
     * @param relationshipBrother {@link String}
     */
    public void setRelationshipBrother(String relationshipBrother) {
        this.relationshipBrother = relationshipBrother;
    }

    /**
     * Gets the sister of the holder
     *
     * @return {@link String}
     */
    public String getRelationshipSister() {
        return relationshipSister;
    }

    /**
     * Sets the sister of the holder
     *
     * @param relationshipSister {@link String}
     */
    public void setRelationshipSister(String relationshipSister) {
        this.relationshipSister = relationshipSister;
    }

    /**
     * Gets the sibling of the holder
     *
     * @return {@link String}
     */
    public String getRelationshipSibling() {
        return relationshipSibling;
    }

    /**
     * Sets the sibling of the holder
     *
     * @param relationshipSibling {@link String}
     */
    public void setRelationshipSibling(String relationshipSibling) {
        this.relationshipSibling = relationshipSibling;
    }

    /**
     * Gets the spouse of the holder
     *
     * @return {@link String}
     */
    public String getRelationshipSpouse() {
        return relationshipSpouse;
    }

    /**
     * Sets the spouse of the holder
     *
     * @param relationshipSpouse {@link String}
     */
    public void setRelationshipSpouse(String relationshipSpouse) {
        this.relationshipSpouse = relationshipSpouse;
    }

    /**
     * Gets the father-in-law of the holder
     *
     * @return {@link String}
     */
    public String getRelationshipFatherInLaw() {
        return relationshipFatherInLaw;
    }

    /**
     * Sets the father-in-law of the holder
     *
     * @param relationshipFatherInLaw {@link String}
     */
    public void setRelationshipFatherInLaw(String relationshipFatherInLaw) {
        this.relationshipFatherInLaw = relationshipFatherInLaw;
    }

    /**
     * Gets the mother-in-law of the holder
     *
     * @return {@link String}
     */
    public String getRelationshipMotherInLaw() {
        return relationshipMotherInLaw;
    }

    /**
     * Sets the mother-in-law of the holder
     *
     * @param relationshipMotherInLaw {@link String}
     */
    public void setRelationshipMotherInLaw(String relationshipMotherInLaw) {
        this.relationshipMotherInLaw = relationshipMotherInLaw;
    }

    /**
     * Gets the parent-in-law of the holder
     *
     * @return {@link String}
     */
    public String getRelationshipParentInLaw() {
        return relationshipParentInLaw;
    }

    /**
     * Sets the parent-in-law of the holder
     *
     * @param relationshipParentInLaw {@link String}
     */
    public void setRelationshipParentInLaw(String relationshipParentInLaw) {
        this.relationshipParentInLaw = relationshipParentInLaw;
    }

    /**
     * Gets the son-in-law of the holder
     *
     * @return {@link String}
     */
    public String getRelationshipSonInLaw() {
        return relationshipSonInLaw;
    }

    /**
     * Sets the son-in-law of the holder
     *
     * @param relationshipSonInLaw {@link String}
     */
    public void setRelationshipSonInLaw(String relationshipSonInLaw) {
        this.relationshipSonInLaw = relationshipSonInLaw;
    }

    /**
     * Gets the daughter-in-law of the holder
     *
     * @return {@link String}
     */
    public String getRelationshipDaughterInLaw() {
        return relationshipDaughterInLaw;
    }

    /**
     * Sets the daughter-in-law of the holder
     *
     * @param relationshipDaughterInLaw {@link String}
     */
    public void setRelationshipDaughterInLaw(String relationshipDaughterInLaw) {
        this.relationshipDaughterInLaw = relationshipDaughterInLaw;
    }

    /**
     * Gets the child-in-law of the holder
     *
     * @return {@link String}
     */
    public String getRelationshipChildInLaw() {
        return relationshipChildInLaw;
    }

    /**
     * Sets the child-in-law of the holder
     *
     * @param relationshipChildInLaw {@link String}
     */
    public void setRelationshipChildInLaw(String relationshipChildInLaw) {
        this.relationshipChildInLaw = relationshipChildInLaw;
    }

    /**
     * Gets the parental authority of the holder
     *
     * @return {@link String}
     */
    public String getRelationshipParentalAuthority() {
        return relationshipParentalAuthority;
    }

    /**
     * Sets the parental authority of the holder
     *
     * @param relationshipParentalAuthority {@link String}
     */
    public void setRelationshipParentalAuthority(String relationshipParentalAuthority) {
        this.relationshipParentalAuthority = relationshipParentalAuthority;
    }

    /**
     * Gets the legal representative of the holder
     *
     * @return {@link String}
     */
    public String getRelationshipLegalRepresentative() {
        return relationshipLegalRepresentative;
    }

    /**
     * Sets the legal representative of the holder
     *
     * @param relationshipLegalRepresentative {@link String}
     */
    public void setRelationshipLegalRepresentative(String relationshipLegalRepresentative) {
        this.relationshipLegalRepresentative = relationshipLegalRepresentative;
    }

    /**
     * Gets the voluntary agent of the holder
     *
     * @return {@link String}
     */
    public String getRelationshipAgent() {
        return relationshipAgent;
    }

    /**
     * Sets the voluntary agent of the holder
     *
     * @param relationshipAgent {@link String}
     */
    public void setRelationshipAgent(String relationshipAgent) {
        this.relationshipAgent = relationshipAgent;
    }

    /**
     * Gets the document type.
     * NOTE: This a selectively disclosable property in comparison with {@code #getDocType}.
     *
     * @return {@link String}
     */
    public String getDocumentType() {
        return documentType;
    }

    /**
     * Sets the document type.
     * NOTE: This a selectively disclosable property in comparison with {@code #getDocType}.
     *
     * @param documentType {@link String}
     */
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
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
     * Gets the name of the street where the user to whom the person identification data relates currently resides.
     *
     * @return {@link String}
     */
    public String getResidentStreet() {
        return residentStreet;
    }

    /**
     * Sets the name of the street where the user to whom the person identification data relates currently resides.
     *
     * @param residentStreet {@link String}
     */
    public void setResidentStreet(String residentStreet) {
        this.residentStreet = residentStreet;
    }

    /**
     * Gets the house number where the user to whom the person identification data relates currently resides,
     * including any affix or suffix.
     *
     * @return {@link String}
     */
    public String getResidentHouseNumber() {
        return residentHouseNumber;
    }

    /**
     * Sets the house number where the user to whom the person identification data relates currently resides,
     * including any affix or suffix.
     *
     * @param residentHouseNumber {@link String}
     */
    public void setResidentHouseNumber(String residentHouseNumber) {
        this.residentHouseNumber = residentHouseNumber;
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
     * Adds a new selectively disclosable claim.
     * A hash will be computed for the claim.
     *
     * @param claim {@link MdocEAAClaim} to add
     */
    public void addClaim(MdocEAAClaim claim) {
        if (claim != null) {
            otherClaims.add(claim);
        }
    }

    /**
     * Adds a new selectively disclosable claim.
     * A hash will be computed for the claim.
     *
     * @param namespace {@link String}
     * @param name {@link String}
     * @param value {@link Object}
     */
    public void addClaim(final String namespace, final String name, final Object value) {
        Objects.requireNonNull(name, "Name cannot be null!");
        addClaim(MdocEAAClaim.create(namespace, name, value));
    }

    /**
     * Gets a list of other arbitrary provided claims
     *
     * @return a list of other claims
     */
    public List<MdocEAAClaim> getOtherClaims() {
        return otherClaims;
    }

}
