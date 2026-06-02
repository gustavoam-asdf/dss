package eu.europa.esig.dss.eaa.jwt.creation;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SDJWTClaimParameters {

    // OpenID Connect Core 1.0

    private String familyName;
    private String givenName;
    private Date birthDate;
    private List<String> nationalities;

    // address
    private String addressFormatted;
    private String addressStreetAddress;
    private String addressLocality;
    private String addressRegion;
    private String addressPostalCode;
    private String addressCountry;
    private String addressHouseNumber;

    private String email;
    private String phoneNumber;
    private String picture;
    private String nickname;
    private String preferredNickname;
    private String name;
    private String middleName;
    private String profile;
    private String website;
    private Boolean emailVerified;
    private String gender;
    private String zoneinfo;
    private String locale;
    private Boolean phoneNumberVerified;
    private Date updatedAt;

    // OpenID Connect for Identity Assurance Claims Registration 1.0

    // place_of_birth
    private String placeOfBirthCountry;
    private String placeOfBirthRegion;
    private String placeOfBirthLocality;

    private String birthFamilyName;
    private String birthGivenName;

    // PID Rulebook claims

    private Date dateOfExpiry;
    private Date dateOfIssuance;
    private String personalAdministrativeNumber;
    private Integer sex;
    private String issuingAuthority;
    private String issuingCountry;
    private String documentNumber;
    private String issuingJurisdiction;
    private Integer ageInYears;
    private Integer ageBirthYear;
    private String trustAnchor;
    private Map<Integer, Boolean> ageEqualOrOver;

    /**
     * Gets a "family_name" claim value as defined by OpenID Connect Core 1.0
     *
     * @return {@link String} value of the family name
     */
    public String getFamilyName() {
        return familyName;
    }

    /**
     * Sets a "family_name" claim value as defined by OpenID Connect Core 1.0
     *
     * @param familyName {@link String} value of the family name
     */
    public void setFamilyName(final String familyName) {
        this.familyName = familyName;
    }

    /**
     * Gets a "given_name" claim value as defined by OpenID Connect Core 1.0
     *
     * @return {@link String} value of the given name
     */
    public String getGivenName() {
        return givenName;
    }

    /**
     * Sets a "given_name" claim value as defined by OpenID Connect Core 1.0
     *
     * @param givenName {@link String} value of the given name
     */
    public void setGivenName(final String givenName) {
        this.givenName = givenName;
    }

    /**
     * Gets a "birth_date" claim value as defined by OpenID Connect Core 1.0
     *
     * @return {@link Date} the birthdate
     */
    public Date getBirthDate() {
        return birthDate;
    }

    /**
     * Sets a "birth_date" claim value as defined by OpenID Connect Core 1.0
     *
     * @param birthDate {@link Date} the birthdate
     */
    public void setBirthDate(final Date birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * Gets a "nationalities" claim value as defined by OpenID Connect Core 1.0
     *
     * @return {@link List} list of alpha-2 country codes of nationalities
     */
    public List<String> getNationalities() {
        return nationalities;
    }

    /**
     * Sets a "nationalities" claim value as defined by OpenID Connect Core 1.0
     *
     * @param nationalities {@link List} list of alpha-2 country codes of nationalities
     */
    public void setNationalities(final List<String> nationalities) {
        this.nationalities = nationalities;
    }

    /**
     * Gets the "formatted" value of the "address" claim as described by OpenID Connect Core 1.0
     *
     * @return {@link String} the full mailing address
     */
    public String getAddressFormatted() {
        return addressFormatted;
    }

    /**
     * Sets the "formatted" value of the "address" claim as described by OpenID Connect Core 1.0
     *
     * @param addressFormatted {@link String} the full mailing address
     */
    public void setAddressFormatted(final String addressFormatted) {
        this.addressFormatted = addressFormatted;
    }

    /**
     * Gets the "street_address" value of the "address" claim as described by OpenID Connect Core 1.0
     *
     * @return {@link String} the full street address component
     */
    public String getAddressStreetAddress() {
        return addressStreetAddress;
    }

    /**
     * Sets the "street_address" value of the "address" claim as described by OpenID Connect Core 1.0
     *
     * @param addressStreetAddress {@link String} the full street address component
     */
    public void setAddressStreetAddress(final String addressStreetAddress) {
        this.addressStreetAddress = addressStreetAddress;
    }

    /**
     * Gets the "locality" value of the "address" claim as described by OpenID Connect Core 1.0
     *
     * @return {@link String} the city or locality component
     */
    public String getAddressLocality() {
        return addressLocality;
    }

    /**
     * Sets the "locality" value of the "address" claim as described by OpenID Connect Core 1.0
     *
     * @param addressLocality {@link String} the city or locality component
     */
    public void setAddressLocality(final String addressLocality) {
        this.addressLocality = addressLocality;
    }

    /**
     * Gets the "region" value of the "address" claim as described by OpenID Connect Core 1.0
     *
     * @return {@link String} the state, province, prefecture, or region component
     */
    public String getAddressRegion() {
        return addressRegion;
    }

    /**
     * Sets the "region" value of the "address" claim as described by OpenID Connect Core 1.0
     *
     * @param addressRegion {@link String} the state, province, prefecture, or region component
     */
    public void setAddressRegion(final String addressRegion) {
        this.addressRegion = addressRegion;
    }

    /**
     * Gets the "postal_code" value of the "address" claim as described by OpenID Connect Core 1.0
     *
     * @return {@link String} the zip code or postal code component
     */
    public String getAddressPostalCode() {
        return addressPostalCode;
    }

    /**
     * Sets the "postal_code" value of the "address" claim as described by OpenID Connect Core 1.0
     *
     * @param addressPostalCode {@link String} the zip code or postal code component
     */
    public void setAddressPostalCode(final String addressPostalCode) {
        this.addressPostalCode = addressPostalCode;
    }

    /**
     * Gets the "country" value of the "address" claim as described by OpenID Connect Core 1.0
     *
     * @return {@link String} the country name component
     */
    public String getAddressCountry() {
        return addressCountry;
    }

    /**
     * Sets the "country" value of the "address" claim as described by OpenID Connect Core 1.0
     *
     * @param addressCountry {@link String} the country name component
     */
    public void setAddressCountry(final String addressCountry) {
        this.addressCountry = addressCountry;
    }

    /**
     * Gets the "house_number" value of the "address" claim as described by OpenID Connect Core 1.0
     *
     * @return {@link String} the house number component
     */
    public String getAddressHouseNumber() {
        return addressHouseNumber;
    }

    /**
     * Sets the "house_number" value of the "address" claim as described by OpenID Connect Core 1.0
     *
     * @param addressHouseNumber {@link String} the house number component
     */
    public void setAddressHouseNumber(final String addressHouseNumber) {
        this.addressHouseNumber = addressHouseNumber;
    }

    /**
     * Gets an "email" claim value as defined by OpenID Connect Core 1.0
     *
     * @return {@link String} the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets an "email" claim value as defined by OpenID Connect Core 1.0
     *
     * @param email {@link String} the email address
     */
    public void setEmail(final String email) {
        this.email = email;
    }

    /**
     * Gets a "phone_number" claim value as defined by OpenID Connect Core 1.0
     *
     * @return {@link String} the phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets a "phone_number" claim value as defined by OpenID Connect Core 1.0
     *
     * @param phoneNumber {@link String} the phone number
     */
    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets a "picture" claim value as defined by OpenID Connect Core 1.0
     *
     * @return {@link String} the picture
     */
    public String getPicture() {
        return picture;
    }

    /**
     * Sets a "picture" claim value as defined by OpenID Connect Core 1.0
     *
     * @param picture {@link String} the picture
     */
    public void setPicture(final String picture) {
        this.picture = picture;
    }

    /**
     * Gets a "nickname" claim value as defined by OpenID Connect Core 1.0
     *
     * @return {@link String} the nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Sets a "nickname" claim value as defined by OpenID Connect Core 1.0
     *
     * @param nickname {@link String} the nickname
     */
    public void setNickname(final String nickname) {
        this.nickname = nickname;
    }

    /**
     * Gets a "preferred_username" claim value as defined by OpenID Connect Core 1.0
     *
     * @return {@link String} the preferred nickname
     */
    public String getPreferredNickname() {
        return preferredNickname;
    }

    /**
     * Sets a "preferred_username" claim value as defined by OpenID Connect Core 1.0
     *
     * @param preferredNickname {@link String} the preferred nickname
     */
    public void setPreferredNickname(final String preferredNickname) {
        this.preferredNickname = preferredNickname;
    }

    /**
     * Gets a "name" claim value as defined by OpenID Connect Core 1.0
     *
     * @return {@link String} the full name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets a "name" claim value as defined by OpenID Connect Core 1.0
     *
     * @param name {@link String} the full name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Gets a "middle_name" claim value as defined by OpenID Connect Core 1.0
     *
     * @return {@link String} value of the middle name
     */
    public String getMiddleName() {
        return middleName;
    }

    /**
     * Sets a "middle_name" claim value as defined by OpenID Connect Core 1.0
     *
     * @param middleName {@link String} value of the middle name
     */
    public void setMiddleName(final String middleName) {
        this.middleName = middleName;
    }

    /**
     * Gets a "profile" claim value as defined by OpenID Connect Core 1.0
     *
     * @return {@link String} the profile URL
     */
    public String getProfile() {
        return profile;
    }

    /**
     * Sets a "profile" claim value as defined by OpenID Connect Core 1.0
     *
     * @param profile {@link String} the profile URL
     */
    public void setProfile(final String profile) {
        this.profile = profile;
    }

    /**
     * Gets a "website" claim value as defined by OpenID Connect Core 1.0
     *
     * @return {@link String} the website URL
     */
    public String getWebsite() {
        return website;
    }

    /**
     * Sets a "website" claim value as defined by OpenID Connect Core 1.0
     *
     * @param website {@link String} the website URL
     */
    public void setWebsite(final String website) {
        this.website = website;
    }

    /**
     * Gets an "email_verified" claim value as defined by OpenID Connect Core 1.0
     *
     * @return {@link Boolean} whether the email has been verified
     */
    public Boolean getEmailVerified() {
        return emailVerified;
    }

    /**
     * Sets an "email_verified" claim value as defined by OpenID Connect Core 1.0
     *
     * @param emailVerified {@link Boolean} whether the email has been verified
     */
    public void setEmailVerified(final Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    /**
     * Gets a "gender" claim value as defined by OpenID Connect Core 1.0
     *
     * @return {@link String} the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets a "gender" claim value as defined by OpenID Connect Core 1.0
     *
     * @param gender {@link String} the gender
     */
    public void setGender(final String gender) {
        this.gender = gender;
    }

    /**
     * Gets a "zoneinfo" claim value as defined by OpenID Connect Core 1.0
     *
     * @return {@link String} the time zone
     */
    public String getZoneinfo() {
        return zoneinfo;
    }

    /**
     * Sets a "zoneinfo" claim value as defined by OpenID Connect Core 1.0
     *
     * @param zoneinfo {@link String} the time zone
     */
    public void setZoneinfo(final String zoneinfo) {
        this.zoneinfo = zoneinfo;
    }

    /**
     * Gets a "locale" claim value as defined by OpenID Connect Core 1.0
     *
     * @return {@link String} the locale
     */
    public String getLocale() {
        return locale;
    }

    /**
     * Sets a "locale" claim value as defined by OpenID Connect Core 1.0
     *
     * @param locale {@link String} the locale
     */
    public void setLocale(final String locale) {
        this.locale = locale;
    }

    /**
     * Gets a "phone_number_verified" claim value as defined by OpenID Connect Core 1.0
     *
     * @return {@link Boolean} whether the phone number has been verified
     */
    public Boolean getPhoneNumberVerified() {
        return phoneNumberVerified;
    }

    /**
     * Sets a "phone_number_verified" claim value as defined by OpenID Connect Core 1.0
     *
     * @param phoneNumberVerified {@link Boolean} whether the phone number has been verified
     */
    public void setPhoneNumberVerified(final Boolean phoneNumberVerified) {
        this.phoneNumberVerified = phoneNumberVerified;
    }

    /**
     * Gets an "updated_at" claim value as defined by OpenID Connect Core 1.0
     *
     * @return {@link Date} when user information was last updated
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets an "updated_at" claim value as defined by OpenID Connect Core 1.0
     *
     * @param updatedAt {@link Date} when user information was last updated
     */
    public void setUpdatedAt(final Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Gets the "country" value of the "place_of_birth" claim as defined by OpenID Connect for Identity Assurance
     * Claims Registration 1.0
     *
     * @return {@link String} the alpha-2 country code
     */
    public String getPlaceOfBirthCountry() {
        return placeOfBirthCountry;
    }

    /**
     * Sets the "country" value of the "place_of_birth" claim as defined by OpenID Connect for Identity Assurance
     * Claims Registration 1.0
     *
     * @param placeOfBirthCountry {@link String} the alpha-2 country code
     */
    public void setPlaceOfBirthCountry(final String placeOfBirthCountry) {
        this.placeOfBirthCountry = placeOfBirthCountry;
    }

    /**
     * Gets the "region" value of the "place_of_birth" claim as defined by OpenID Connect for Identity Assurance
     * Claims Registration 1.0
     *
     * @return {@link String} the name of a state, province, district, or local area
     */
    public String getPlaceOfBirthRegion() {
        return placeOfBirthRegion;
    }

    /**
     * Sets the "region" value of the "place_of_birth" claim as defined by OpenID Connect for Identity Assurance
     * Claims Registration 1.0
     *
     * @param placeOfBirthRegion {@link String} the name of a state, province, district, or local area
     */
    public void setPlaceOfBirthRegion(final String placeOfBirthRegion) {
        this.placeOfBirthRegion = placeOfBirthRegion;
    }

    /**
     * Gets the "locality" value of the "place_of_birth" claim as defined by OpenID Connect for Identity Assurance
     * Claims Registration 1.0
     *
     * @return {@link String} the name of a municipality, city, town, or village
     */
    public String getPlaceOfBirthLocality() {
        return placeOfBirthLocality;
    }

    /**
     * Sets the "locality" value of the "place_of_birth" claim as defined by OpenID Connect for Identity Assurance
     * Claims Registration 1.0
     *
     * @param placeOfBirthLocality {@link String} the name of a municipality, city, town, or village
     */
    public void setPlaceOfBirthLocality(final String placeOfBirthLocality) {
        this.placeOfBirthLocality = placeOfBirthLocality;
    }

    /**
     * Gets a "birth_family_name" claim value as defined by OpenID Connect for Identity Assurance Claims Registration 1.0
     *
     * @return {@link String} value of the birth family name
     */
    public String getBirthFamilyName() {
        return birthFamilyName;
    }

    /**
     * Sets a "birth_family_name" claim value as defined by OpenID Connect for Identity Assurance Claims Registration 1.0
     *
     * @param birthFamilyName {@link String} value of the birth family name
     */
    public void setBirthFamilyName(final String birthFamilyName) {
        this.birthFamilyName = birthFamilyName;
    }

    /**
     * Gets a "birth_given_name" claim value as defined by OpenID Connect for Identity Assurance Claims Registration 1.0
     *
     * @return {@link String} value of the birth given name
     */
    public String getBirthGivenName() {
        return birthGivenName;
    }

    /**
     * Sets a "birth_given_name" claim value as defined by OpenID Connect for Identity Assurance Claims Registration 1.0
     *
     * @param birthGivenName {@link String} value of the birth given name
     */
    public void setBirthGivenName(final String birthGivenName) {
        this.birthGivenName = birthGivenName;
    }

    /**
     * Gets a "date_of_expiry" claim value as defined by the PID Rulebook
     *
     * @return {@link Date} the date of expiry
     */
    public Date getDateOfExpiry() {
        return dateOfExpiry;
    }

    /**
     * Sets a "date_of_expiry" claim value as defined by the PID Rulebook
     *
     * @param dateOfExpiry {@link Date} the date of expiry
     */
    public void setDateOfExpiry(final Date dateOfExpiry) {
        this.dateOfExpiry = dateOfExpiry;
    }

    /**
     * Gets a "date_of_issuance" claim value as defined by the PID Rulebook
     *
     * @return {@link Date} the date of issuance
     */
    public Date getDateOfIssuance() {
        return dateOfIssuance;
    }

    /**
     * Sets a "date_of_issuance" claim value as defined by the PID Rulebook
     *
     * @param dateOfIssuance {@link Date} the date of issuance
     */
    public void setDateOfIssuance(final Date dateOfIssuance) {
        this.dateOfIssuance = dateOfIssuance;
    }

    /**
     * Gets a "personal_administrative_number" claim value as defined by the PID Rulebook
     *
     * @return {@link String} the personal administrative number
     */
    public String getPersonalAdministrativeNumber() {
        return personalAdministrativeNumber;
    }

    /**
     * Sets a "personal_administrative_number" claim value as defined by the PID Rulebook
     *
     * @param personalAdministrativeNumber {@link String} the personal administrative number
     */
    public void setPersonalAdministrativeNumber(final String personalAdministrativeNumber) {
        this.personalAdministrativeNumber = personalAdministrativeNumber;
    }

    /**
     * Gets a "sex" claim value as defined by the PID Rulebook
     *
     * @return {@link Integer} the sex value
     */
    public Integer getSex() {
        return sex;
    }

    /**
     * Sets a "sex" claim value as defined by the PID Rulebook
     *
     * @param sex {@link Integer} the sex value
     */
    public void setSex(final Integer sex) {
        this.sex = sex;
    }

    /**
     * Gets an "issuing_authority" claim value as defined by the PID Rulebook
     *
     * @return {@link String} the issuing authority
     */
    public String getIssuingAuthority() {
        return issuingAuthority;
    }

    /**
     * Sets an "issuing_authority" claim value as defined by the PID Rulebook
     *
     * @param issuingAuthority {@link String} the issuing authority
     */
    public void setIssuingAuthority(final String issuingAuthority) {
        this.issuingAuthority = issuingAuthority;
    }

    /**
     * Gets an "issuing_country" claim value as defined by the PID Rulebook
     *
     * @return {@link String} the alpha-2 issuing country code
     */
    public String getIssuingCountry() {
        return issuingCountry;
    }

    /**
     * Sets an "issuing_country" claim value as defined by the PID Rulebook
     *
     * @param issuingCountry {@link String} the alpha-2 issuing country code
     */
    public void setIssuingCountry(final String issuingCountry) {
        this.issuingCountry = issuingCountry;
    }

    /**
     * Gets a "document_number" claim value as defined by the PID Rulebook
     *
     * @return {@link String} the document number
     */
    public String getDocumentNumber() {
        return documentNumber;
    }

    /**
     * Sets a "document_number" claim value as defined by the PID Rulebook
     *
     * @param documentNumber {@link String} the document number
     */
    public void setDocumentNumber(final String documentNumber) {
        this.documentNumber = documentNumber;
    }

    /**
     * Gets an "issuing_jurisdiction" claim value as defined by the PID Rulebook
     *
     * @return {@link String} the issuing jurisdiction
     */
    public String getIssuingJurisdiction() {
        return issuingJurisdiction;
    }

    /**
     * Sets an "issuing_jurisdiction" claim value as defined by the PID Rulebook
     *
     * @param issuingJurisdiction {@link String} the issuing jurisdiction
     */
    public void setIssuingJurisdiction(final String issuingJurisdiction) {
        this.issuingJurisdiction = issuingJurisdiction;
    }

    /**
     * Gets an "age_in_years" claim value as defined by the PID Rulebook
     *
     * @return {@link Integer} the age in years
     */
    public Integer getAgeInYears() {
        return ageInYears;
    }

    /**
     * Sets an "age_in_years" claim value as defined by the PID Rulebook
     *
     * @param ageInYears {@link Integer} the age in years
     */
    public void setAgeInYears(final Integer ageInYears) {
        this.ageInYears = ageInYears;
    }

    /**
     * Gets an "age_birth_year" claim value as defined by the PID Rulebook
     *
     * @return {@link Integer} the birth year
     */
    public Integer getAgeBirthYear() {
        return ageBirthYear;
    }

    /**
     * Sets an "age_birth_year" claim value as defined by the PID Rulebook
     *
     * @param ageBirthYear {@link Integer} the birth year
     */
    public void setAgeBirthYear(final Integer ageBirthYear) {
        this.ageBirthYear = ageBirthYear;
    }

    /**
     * Gets a "trust_anchor" claim value as defined by the PID Rulebook
     *
     * @return {@link String} the trust anchor
     */
    public String getTrustAnchor() {
        return trustAnchor;
    }

    /**
     * Sets a "trust_anchor" claim value as defined by the PID Rulebook
     *
     * @param trustAnchor {@link String} the trust anchor
     */
    public void setTrustAnchor(final String trustAnchor) {
        this.trustAnchor = trustAnchor;
    }

    /**
     * Gets an "age_equal_or_over" claim value as defined by the PID Rulebook
     *
     * @return {@link Map} age threshold map where key is age and value indicates whether subject is equal or over
     */
    public Map<Integer, Boolean> getAgeEqualOrOver() {
        return ageEqualOrOver;
    }

    /**
     * Adds a threshold entry to an "age_equal_or_over" claim as defined by the PID Rulebook
     *
     * @param ageThreshold {@code int} the age threshold (e.g., 13, 18, 21)
     * @param verified {@code boolean} whether the age threshold is verified
     */
    public void addAgeEqualOrOverEntry(final int ageThreshold, final boolean verified) {
        if (ageEqualOrOver == null) {
            ageEqualOrOver = new HashMap<>();
        }
        ageEqualOrOver.put(ageThreshold, verified);
    }

}
