package eu.europa.esig.dss.eaa.jwt.claim;

import eu.europa.esig.dss.eaa.jwt.SDJWTConstants;
import eu.europa.esig.dss.model.eaa.claim.ClaimAddress;
import eu.europa.esig.dss.model.eaa.claim.ClaimArray;
import eu.europa.esig.dss.model.eaa.claim.ClaimBoolean;
import eu.europa.esig.dss.model.eaa.claim.ClaimCredentialSubject;
import eu.europa.esig.dss.model.eaa.claim.ClaimDate;
import eu.europa.esig.dss.model.eaa.claim.ClaimMap;
import eu.europa.esig.dss.model.eaa.claim.ClaimPlaceOfBirth;
import eu.europa.esig.dss.model.eaa.claim.ClaimString;

/**
 * SD-JWT implementation of a "4.8 Credential Subject" claim defined in W3C Verifiable Credentials Data Model v2.0.
 *
 */
public class SDJWTClaimCredentialSubject extends SDJWTClaimMap implements ClaimCredentialSubject {

    private static final long serialVersionUID = -4959653550379591495L;

    /**
     * Default constructor
     *
     * @param value {@link ClaimMap}
     */
    public SDJWTClaimCredentialSubject(ClaimMap value) {
        super(value.getName(), value.getMapValue(), value.isSelectivelyDisclosable(), value.getParent());
    }

    @Override
    public ClaimString getFullName() {
        return getAsString(SDJWTConstants.USER_NAME);
    }

    @Override
    public ClaimString getFirstName() {
        return getAsString(SDJWTConstants.USER_GIVEN_NAME);
    }

    @Override
    public ClaimString getLastName() {
        return getAsString(SDJWTConstants.USER_FAMILY_NAME);
    }

    @Override
    public ClaimString getMiddleName() {
        return getAsString(SDJWTConstants.USER_MIDDLE_NAME);
    }

    @Override
    public ClaimString getNickname() {
        return getAsString(SDJWTConstants.USER_NICKNAME);
    }

    @Override
    public ClaimString getShortName() {
        return getAsString(SDJWTConstants.USER_PREFERRED_NICKNAME);
    }

    @Override
    public ClaimString getProfileUrl() {
        return getAsString(SDJWTConstants.USER_PROFILE);
    }

    @Override
    public ClaimString getPictureUrl() {
        return getAsString(SDJWTConstants.USER_PICTURE);
    }

    @Override
    public ClaimString getWebsiteUrl() {
        return getAsString(SDJWTConstants.USER_WEBSITE);
    }

    @Override
    public ClaimString getEmail() {
        return getAsString(SDJWTConstants.USER_EMAIL);
    }

    @Override
    public ClaimBoolean getEmailVerified() {
        return getAsBoolean(SDJWTConstants.USER_EMAIL_VERIFIED);
    }

    @Override
    public ClaimString getGender() {
        return getAsString(SDJWTConstants.USER_GENDER);
    }

    @Override
    public ClaimDate getBirthdate() {
        return getAsDate(SDJWTConstants.USER_BIRTHDATE);
    }

    @Override
    public ClaimString getTimezone() {
        return getAsString(SDJWTConstants.USER_ZONEINFO);
    }

    @Override
    public ClaimString getLocale() {
        return getAsString(SDJWTConstants.USER_LOCALE);
    }

    @Override
    public ClaimAddress getAddress() {
        ClaimMap claimAddress = getAsMap(SDJWTConstants.USER_ADDRESS);
        if (claimAddress != null) {
            return new SDJWTClaimAddress(claimAddress);
        }
        return null;
    }

    @Override
    public ClaimString getPhoneNumber() {
        return getAsString(SDJWTConstants.USER_PHONE_NUMBER);
    }

    @Override
    public ClaimBoolean getPhoneNumberVerified() {
        return getAsBoolean(SDJWTConstants.USER_PHONE_NUMBER_VERIFIED);
    }

    @Override
    public ClaimPlaceOfBirth getPlaceOfBirth() {
        ClaimMap claimPlaceOfBirth = getAsMap(SDJWTConstants.USER_PLACE_OF_BIRTH);
        if (claimPlaceOfBirth != null) {
            return new SDJWTClaimPlaceOfBirth(claimPlaceOfBirth);
        }
        return null;
    }

    @Override
    public ClaimArray getNationalities() {
        return getAsArray(SDJWTConstants.USER_NATIONALITIES);
    }

    @Override
    public ClaimString getBirthFirstName() {
        return getAsString(SDJWTConstants.USER_BIRTH_GIVEN_NAME);
    }

    @Override
    public ClaimString getBirthLastName() {
        return getAsString(SDJWTConstants.USER_BIRTH_FAMILY_NAME);
    }

    @Override
    public ClaimString getBirthMiddleName() {
        return getAsString(SDJWTConstants.USER_BIRTH_MIDDLE_NAME);
    }

    @Override
    public ClaimString getSalutation() {
        return getAsString(SDJWTConstants.USER_SALUTATION);
    }

    @Override
    public ClaimString getTitle() {
        return getAsString(SDJWTConstants.USER_TITLE);
    }

    @Override
    public ClaimString getMobilePhoneNumber() {
        return getAsString(SDJWTConstants.USER_MOBILE_PHONE_NUMBER);
    }

    @Override
    public ClaimString getPseudonym() {
        return getAsString(SDJWTConstants.USER_PSEUDONYM);
    }

}
