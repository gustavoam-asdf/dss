package eu.europa.esig.dss.eaa.jwt.validation;

import eu.europa.esig.dss.eaa.jwt.SDJWTConstants;
import eu.europa.esig.dss.eaa.jwt.claim.SDJWTClaimAddress;
import eu.europa.esig.dss.eaa.jwt.claim.SDJWTClaimCredentialSubject;
import eu.europa.esig.dss.eaa.jwt.claim.SDJWTClaimDeviceKey;
import eu.europa.esig.dss.eaa.jwt.claim.SDJWTClaimIntegrity;
import eu.europa.esig.dss.eaa.jwt.claim.SDJWTClaimMap;
import eu.europa.esig.dss.eaa.jwt.claim.SDJWTClaimPlaceOfBirth;
import eu.europa.esig.dss.eaa.jwt.claim.SDJWTClaimStatus;
import eu.europa.esig.dss.model.eaa.claim.Claim;
import eu.europa.esig.dss.model.eaa.claim.ClaimAddress;
import eu.europa.esig.dss.model.eaa.claim.ClaimAgeOverNN;
import eu.europa.esig.dss.model.eaa.claim.ClaimArray;
import eu.europa.esig.dss.model.eaa.claim.ClaimBiometricTemplateXX;
import eu.europa.esig.dss.model.eaa.claim.ClaimBoolean;
import eu.europa.esig.dss.model.eaa.claim.ClaimByteString;
import eu.europa.esig.dss.model.eaa.claim.ClaimCredentialSubject;
import eu.europa.esig.dss.model.eaa.claim.ClaimDate;
import eu.europa.esig.dss.model.eaa.claim.ClaimDeviceKey;
import eu.europa.esig.dss.model.eaa.claim.ClaimDrivingPrivileges;
import eu.europa.esig.dss.model.eaa.claim.ClaimIntegrity;
import eu.europa.esig.dss.model.eaa.claim.ClaimMap;
import eu.europa.esig.dss.model.eaa.claim.ClaimNumber;
import eu.europa.esig.dss.model.eaa.claim.ClaimPlaceOfBirth;
import eu.europa.esig.dss.model.eaa.claim.ClaimStatus;
import eu.europa.esig.dss.model.eaa.claim.ClaimString;
import eu.europa.esig.dss.model.eaa.claim.ClaimValidityInfo;
import eu.europa.esig.dss.spi.eaa.EAAPayload;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class implements a user-friendly access to the EAA payload elements of the SD-JWT token
 *
 */
public class SDJWTPayload extends SDJWTClaimMap implements EAAPayload {

    private static final long serialVersionUID = -4552799683587409954L;

    /**
     * Constructor with a verified payload map, containing the attached disclosures, when applicable
     *
     * @param verifiedPayloadMap {@link String} json payload
     */
    public SDJWTPayload(final ClaimMap verifiedPayloadMap) {
        super(verifiedPayloadMap.getMapValue());
    }
    
    @Override
    public ClaimString getIdentifier() {
        return getAsString(SDJWTConstants.JWT_ID);
    }

    @Override
    public ClaimString getIssuer() {
        return getAsString(SDJWTConstants.ISSUER);
    }

    @Override
    public ClaimString getSubject() {
        return getAsString(SDJWTConstants.SUBJECT);
    }

    @Override
    public ClaimArray getAudience() {
        return getAsArray(SDJWTConstants.AUDIENCE);
    }

    @Override
    public ClaimDate getExpirationTime() {
        return getAsDateTime(SDJWTConstants.EXPIRATION_TIME);
    }

    @Override
    public ClaimDate getNotBeforeTime() {
        return getAsDateTime(SDJWTConstants.NOT_BEFORE);
    }

    @Override
    public ClaimDate getIssuedAtTime() {
        return getAsDateTime(SDJWTConstants.ISSUED_AT);
    }

    @Override
    public ClaimDate getUpdatedAtTime() {
        return getAsDateTime(SDJWTConstants.UPDATED_AT);
    }

    @Override
    public ClaimDeviceKey getDeviceKey() {
        ClaimMap cnf = getAsMap(SDJWTConstants.CNF);
        if (cnf != null) {
            return new SDJWTClaimDeviceKey(cnf);
        }
        return null;
    }

    @Override
    public ClaimString getCategory() {
        return getAsString(SDJWTConstants.CATEGORY);
    }

    @Override
    public ClaimString getMetadataType() {
        return getAsString(SDJWTConstants.VERIFIABLE_CREDENTIALS_TYPE);
    }

    @Override
    public ClaimIntegrity getMetadataIntegrity() {
        ClaimString metadataIntegrity = getAsString(SDJWTConstants.VERIFIABLE_CREDENTIALS_INTEGRITY);
        if (metadataIntegrity != null) {
            return new SDJWTClaimIntegrity(metadataIntegrity);
        }
        return null;
    }

    @Override
    public ClaimStatus getStatus() {
        ClaimMap statusClaim = getAsMap(SDJWTConstants.STATUS);
        if (statusClaim != null) {
            return new SDJWTClaimStatus(statusClaim);
        }
        return null;
    }

    @Override
    public ClaimString getNonce() {
        return getAsString(SDJWTConstants.NONCE);
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

    @Override
    public List<ClaimCredentialSubject> getCredentialSubjects() {
        ClaimMap claimCredentialSubjectAsMap = getAsMap(SDJWTConstants.CREDENTIAL_SUBJECT);
        if (claimCredentialSubjectAsMap != null) {
            return Collections.singletonList(new SDJWTClaimCredentialSubject(claimCredentialSubjectAsMap));
        }
        ClaimArray claimCredentialSubjectAsArray = getAsArray(SDJWTConstants.CREDENTIAL_SUBJECT);
        if (claimCredentialSubjectAsArray != null) {
            List<ClaimCredentialSubject> result = new ArrayList<>();
            for (Claim credentialSubject : claimCredentialSubjectAsArray.getListValue()) {
                if (credentialSubject.isMapValueType()) {
                    result.add(new SDJWTClaimCredentialSubject((ClaimMap) credentialSubject));
                }
            }
            return result;
        }
        return Collections.emptyList();
    }

    @Override
    public ClaimString getIssuingCountry() {
        return null;
    }

    @Override
    public ClaimString getIssuingAuthority() {
        return null;
    }

    @Override
    public ClaimString getDocumentNumber() {
        return null;
    }

    @Override
    public ClaimByteString getPortrait() {
        return null;
    }

    @Override
    public ClaimDrivingPrivileges getDrivingPrivileges() {
        return null;
    }

    @Override
    public ClaimString getUNDistinguishingSign() {
        return null;
    }

    @Override
    public ClaimString getAdministrativeNumber() {
        return null;
    }

    @Override
    public ClaimNumber getHeight() {
        return null;
    }

    @Override
    public ClaimNumber getWeight() {
        return null;
    }

    @Override
    public ClaimString getEyeColor() {
        return null;
    }

    @Override
    public ClaimString getHairColor() {
        return null;
    }

    @Override
    public ClaimString getResidentAddress() {
        return null;
    }

    @Override
    public ClaimDate getPortraitCaptureDate() {
        return null;
    }

    @Override
    public ClaimNumber getAgeInYears() {
        return null;
    }

    @Override
    public ClaimNumber getAgeBirthYear() {
        return null;
    }

    @Override
    public List<ClaimAgeOverNN> getAgeOverNN() {
        return Collections.emptyList();
    }

    @Override
    public ClaimString getIssuingJurisdiction() {
        return null;
    }

    @Override
    public ClaimString getResidentCity() {
        return null;
    }

    @Override
    public ClaimString getResidentState() {
        return null;
    }

    @Override
    public ClaimString getResidentPostalCode() {
        return null;
    }

    @Override
    public ClaimString getResidentCountry() {
        return null;
    }

    @Override
    public List<ClaimBiometricTemplateXX> getBiometricTemplate() {
        return Collections.emptyList();
    }

    @Override
    public ClaimByteString getSignatureUsualMark() {
        return null;
    }

    @Override
    public ClaimString getVersion() {
        return null;
    }

    @Override
    public ClaimString getDocType() {
        return null;
    }

    @Override
    public ClaimValidityInfo getValidityInfo() {
        return null;
    }

    @Override
    public ClaimByteString getFingerprint() {
        return null;
    }

    @Override
    public ClaimString getBusinessName() {
        return null;
    }

    @Override
    public ClaimString getOrganizationName() {
        return null;
    }

    @Override
    public ClaimString getBirthFullName() {
        return null;
    }

    @Override
    public ClaimString getProfession() {
        return null;
    }

    @Override
    public ClaimString getRelationshipFather() {
        return null;
    }

    @Override
    public ClaimString getRelationshipMother() {
        return null;
    }

    @Override
    public ClaimString getRelationshipParent() {
        return null;
    }

    @Override
    public ClaimString getRelationshipSon() {
        return null;
    }

    @Override
    public ClaimString getRelationshipDaughter() {
        return null;
    }

    @Override
    public ClaimString getRelationshipBrother() {
        return null;
    }

    @Override
    public ClaimString getRelationshipSister() {
        return null;
    }

    @Override
    public ClaimString getRelationshipSibling() {
        return null;
    }

    @Override
    public ClaimString getRelationshipSpouse() {
        return null;
    }

    @Override
    public ClaimString getRelationshipFatherInLaw() {
        return null;
    }

    @Override
    public ClaimString getRelationshipMotherInLaw() {
        return null;
    }

    @Override
    public ClaimString getRelationshipParentInLaw() {
        return null;
    }

    @Override
    public ClaimString getRelationshipSonInLaw() {
        return null;
    }

    @Override
    public ClaimString getRelationshipDaughterInLaw() {
        return null;
    }

    @Override
    public ClaimString getRelationshipChildInLaw() {
        return null;
    }

    @Override
    public ClaimString getRelationshipParentalAuthority() {
        return null;
    }

    @Override
    public ClaimString getRelationshipLegalRepresentative() {
        return null;
    }

    @Override
    public ClaimString getRelationshipAgent() {
        return null;
    }

    @Override
    public ClaimString getDocumentType() {
        return null;
    }

}
