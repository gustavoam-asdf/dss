package eu.europa.esig.dss.eaa.jwt.creation;

import eu.europa.esig.dss.eaa.jwt.SDJWTConstants;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Default implementation of {@link SDJWTEAAClaimBuilder}
 */
public class DefaultSDJWTEAAClaimBuilder implements SDJWTEAAClaimBuilder {

    @Override
    public List<SDJWTEAAClaim> buildClaims(final SDJWTEAAPayloadParameters payloadParameters) {
        final SDJWTClaimParameters nonSd = payloadParameters.nonSelectivelyDisclosable();
        final SDJWTClaimParameters sd = payloadParameters.selectivelyDisclosable();

        final List<SDJWTEAAClaim> nonSelectivelyDisclosableClaims = buildClaims(nonSd, false);
        final List<SDJWTEAAClaim> selectivelyDisclosableClaims = buildClaims(sd, true);

        ensureNoDuplicateClaimNames(nonSelectivelyDisclosableClaims, selectivelyDisclosableClaims);

        final List<SDJWTEAAClaim> claims = new ArrayList<>();
        claims.addAll(buildTechnicalClaims(payloadParameters));
        claims.addAll(payloadParameters.getClaims());
        claims.addAll(nonSelectivelyDisclosableClaims);
        claims.addAll(selectivelyDisclosableClaims);

        return claims;
    }

    protected List<SDJWTEAAClaim> buildTechnicalClaims(final SDJWTEAAPayloadParameters payloadParameters) {
        final List<SDJWTEAAClaim> claims = new ArrayList<>();

        addIfNotNull(claims, buildIssuerClaim(payloadParameters.getIssuer()));
        addIfNotNull(claims, buildIssuedAtClaim(payloadParameters.getIssuanceDate()));
        addIfNotNull(claims, buildNotBeforeClaim(payloadParameters.getNotBeforeDate()));
        addIfNotNull(claims, buildExpirationTimeClaim(payloadParameters.getExpirationDate()));
        addIfNotNull(claims, buildSubjectClaim(payloadParameters.getSubject()));
        addIfNotNull(claims, buildOneTimeClaim(payloadParameters.isOneTime()));
        addIfNotNull(claims, buildShortLivedClaim(payloadParameters.isShortLived()));

        return claims;
    }

    protected List<SDJWTEAAClaim> buildClaims(final SDJWTClaimParameters parameters,
                                              final boolean selectivelyDisclosable) {
        final List<SDJWTEAAClaim> claims = new ArrayList<>();
        addIfNotNull(claims, buildFamilyNameClaim(parameters.getFamilyName(), selectivelyDisclosable));
        addIfNotNull(claims, buildGivenNameClaim(parameters.getGivenName(), selectivelyDisclosable));
        addIfNotNull(claims, buildBirthDateClaim(parameters.getBirthDate(), selectivelyDisclosable));
        addIfNotNull(claims, buildNationalitiesClaim(parameters.getNationalities(), selectivelyDisclosable));
        addIfNotNull(claims, buildAddressClaim(parameters.getAddressFormatted(), parameters.getAddressStreetAddress(), parameters.getAddressLocality(),
                parameters.getAddressRegion(), parameters.getAddressPostalCode(), parameters.getAddressCountry(), parameters.getAddressHouseNumber(), selectivelyDisclosable));
        addIfNotNull(claims, buildEmailClaim(parameters.getEmail(), selectivelyDisclosable));
        addIfNotNull(claims, buildPhoneNumberClaim(parameters.getPhoneNumber(), selectivelyDisclosable));
        addIfNotNull(claims, buildPictureClaim(parameters.getPicture(), selectivelyDisclosable));
        addIfNotNull(claims, buildNicknameClaim(parameters.getNickname(), selectivelyDisclosable));
        addIfNotNull(claims, buildPreferredNicknameClaim(parameters.getPreferredNickname(), selectivelyDisclosable));
        addIfNotNull(claims, buildNameClaim(parameters.getName(), selectivelyDisclosable));
        addIfNotNull(claims, buildMiddleNameClaim(parameters.getMiddleName(), selectivelyDisclosable));
        addIfNotNull(claims, buildProfileClaim(parameters.getProfile(), selectivelyDisclosable));
        addIfNotNull(claims, buildWebsiteClaim(parameters.getWebsite(), selectivelyDisclosable));
        addIfNotNull(claims, buildEmailVerifiedClaim(parameters.getEmailVerified(), selectivelyDisclosable));
        addIfNotNull(claims, buildGenderClaim(parameters.getGender(), selectivelyDisclosable));
        addIfNotNull(claims, buildZoneinfoClaim(parameters.getZoneinfo(), selectivelyDisclosable));
        addIfNotNull(claims, buildLocaleClaim(parameters.getLocale(), selectivelyDisclosable));
        addIfNotNull(claims, buildPhoneNumberVerifiedClaim(parameters.getPhoneNumberVerified(), selectivelyDisclosable));
        addIfNotNull(claims, buildUpdatedAtClaim(parameters.getUpdatedAt(), selectivelyDisclosable));
        addIfNotNull(claims, buildPlaceOfBirthClaim(parameters.getPlaceOfBirthCountry(), parameters.getPlaceOfBirthRegion(), parameters.getPlaceOfBirthLocality(), selectivelyDisclosable));
        addIfNotNull(claims, buildBirthFamilyNameClaim(parameters.getBirthFamilyName(), selectivelyDisclosable));
        addIfNotNull(claims, buildBirthGivenNameClaim(parameters.getBirthGivenName(), selectivelyDisclosable));
        addIfNotNull(claims, buildBirthMiddleNameClaim(parameters.getBirthMiddleName(), selectivelyDisclosable));
        addIfNotNull(claims, buildSalutationClaim(parameters.getSalutation(), selectivelyDisclosable));
        addIfNotNull(claims, buildTitleClaim(parameters.getTitle(), selectivelyDisclosable));
        addIfNotNull(claims, buildMobilePhoneNumberClaim(parameters.getMobilePhoneNumber(), selectivelyDisclosable));
        addIfNotNull(claims, buildPseudonymClaim(parameters.getPseudonym(), selectivelyDisclosable));
        addIfNotNull(claims, buildDateOfExpiryClaim(parameters.getDateOfExpiry(), selectivelyDisclosable));
        addIfNotNull(claims, buildDateOfIssuanceClaim(parameters.getDateOfIssuance(), selectivelyDisclosable));
        addIfNotNull(claims, buildPersonalAdministrativeNumberClaim(parameters.getPersonalAdministrativeNumber(), selectivelyDisclosable));
        addIfNotNull(claims, buildSexClaim(parameters.getSex(), selectivelyDisclosable));
        addIfNotNull(claims, buildIssuingAuthorityClaim(parameters.getIssuingAuthority(), selectivelyDisclosable));
        addIfNotNull(claims, buildIssuingCountryClaim(parameters.getIssuingCountry(), selectivelyDisclosable));
        addIfNotNull(claims, buildDocumentNumberClaim(parameters.getDocumentNumber(), selectivelyDisclosable));
        addIfNotNull(claims, buildIssuingJurisdictionClaim(parameters.getIssuingJurisdiction(), selectivelyDisclosable));
        addIfNotNull(claims, buildAgeInYearsClaim(parameters.getAgeInYears(), selectivelyDisclosable));
        addIfNotNull(claims, buildAgeBirthYearClaim(parameters.getAgeBirthYear(), selectivelyDisclosable));
        addIfNotNull(claims, buildTrustAnchorClaim(parameters.getTrustAnchor(), selectivelyDisclosable));
        addIfNotNull(claims, buildAgeEqualOrOverClaim(parameters.getAgeEqualOrOver(), selectivelyDisclosable));
        addIfNotNull(claims, buildVerifiableCredentialsTypeClaim(parameters.getVerifiableCredentialsType(), selectivelyDisclosable));
        addIfNotNull(claims, buildVerifiableCredentialsIntegrityClaim(parameters.getVerifiableCredentialsIntegrity(), selectivelyDisclosable));
        addIfNotNull(claims, buildCategoryClaim(parameters.getCategory(), selectivelyDisclosable));
        addIfNotNull(claims, buildIssuingRegistrationIdentifierClaim(parameters.getIssuingRegistrationIdentifier(), selectivelyDisclosable));
        addIfNotNull(claims, buildAdministrativeValidityNotBeforeClaim(parameters.getAdministrativeValidityNotBefore(), selectivelyDisclosable));
        addIfNotNull(claims, buildAdministrativeValidityExpiryClaim(parameters.getAdministrativeValidityExpiry(), selectivelyDisclosable));
        return claims;
    }

    protected void ensureNoDuplicateClaimNames(final List<SDJWTEAAClaim> nonSelectivelyDisclosableClaims,
                                               final List<SDJWTEAAClaim> selectivelyDisclosableClaims) {
        final Set<String> nonSelectivelyDisclosableClaimNames = new HashSet<>();
        for (SDJWTEAAClaim claim : nonSelectivelyDisclosableClaims) {
            if (claim.getName() != null) {
                nonSelectivelyDisclosableClaimNames.add(claim.getName());
            }
        }

        for (SDJWTEAAClaim claim : selectivelyDisclosableClaims) {
            final String claimName = claim.getName();
            if (claimName != null && nonSelectivelyDisclosableClaimNames.contains(claimName)) {
                throw new DSSException(String.format("The claim '%s' cannot be both selectively disclosable and non-selectively disclosable", claimName));
            }
        }
    }

    protected SDJWTEAAClaim buildIssuerClaim(final String issuer) {
        if (issuer == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.ISSUER, issuer, false);
    }

    protected SDJWTEAAClaim buildIssuedAtClaim(final Date issuanceDate) {
        if (issuanceDate == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.ISSUED_AT, DSSUtils.getTimeValueInSeconds(issuanceDate.getTime()), false);
    }

    protected SDJWTEAAClaim buildNotBeforeClaim(final Date notBeforeDate) {
        if (notBeforeDate == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.NOT_BEFORE, DSSUtils.getTimeValueInSeconds(notBeforeDate.getTime()), false);
    }

    protected SDJWTEAAClaim buildExpirationTimeClaim(final Date expirationDate) {
        if (expirationDate == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.EXPIRATION_TIME, DSSUtils.getTimeValueInSeconds(expirationDate.getTime()), false);
    }

    protected SDJWTEAAClaim buildSubjectClaim(final String subject) {
        if (subject == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.SUBJECT, subject, false);
    }

    protected SDJWTEAAClaim buildOneTimeClaim(final boolean oneTime) {
        if (!oneTime) {
            return null;
        }
        return buildClaim(SDJWTConstants.ONE_TIME, null, false);
    }

    protected SDJWTEAAClaim buildShortLivedClaim(final boolean shortLived) {
        if (!shortLived) {
            return null;
        }
        return buildClaim(SDJWTConstants.SHORT_LIVED, null, false);
    }

    protected SDJWTEAAClaim buildFamilyNameClaim(final String familyName, final boolean selectivelyDisclosable) {
        if (familyName == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.USER_FAMILY_NAME, familyName, selectivelyDisclosable);
    }

    protected SDJWTEAAClaim buildGivenNameClaim(final String givenName, final boolean selectivelyDisclosable) {
        if (givenName == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.USER_GIVEN_NAME, givenName, selectivelyDisclosable);
    }

    protected SDJWTEAAClaim buildBirthDateClaim(final Date birthDate, final boolean selectivelyDisclosable) {
        if (birthDate == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.USER_BIRTHDATE, DSSUtils.formatDateToISO8601(birthDate), selectivelyDisclosable);
    }

    protected SDJWTEAAClaimArray buildNationalitiesClaim(final List<String> nationalities, final boolean selectivelyDisclosable) {
        if (nationalities == null) {
            return null;
        }
        SDJWTEAAClaimArray claim = new SDJWTEAAClaimArray(SDJWTConstants.USER_NATIONALITIES, selectivelyDisclosable, null);
        nationalities.forEach(nationality -> claim.addElement(SDJWTEAAClaim.create(nationality)));
        return claim;
    }

    protected SDJWTEAAClaimObject buildAddressClaim(final String formatted, final String streetAddress, final String locality,
                                                    final String region, final String postalCode, final String country,
                                                    final String houseNumber, final boolean selectivelyDisclosable) {
        if (Utils.areAllStringsEmpty(formatted, streetAddress, locality, region, postalCode, country, houseNumber)) {
            return null;
        }

        SDJWTEAAClaimObject claim = new SDJWTEAAClaimObject(SDJWTConstants.USER_ADDRESS, selectivelyDisclosable);
        if (Utils.isStringNotBlank(formatted)) {
            claim.addChild(SDJWTEAAClaim.create(SDJWTConstants.USER_ADDRESS_FORMATTED, formatted));
        }
        if (Utils.isStringNotBlank(streetAddress)) {
            claim.addChild(SDJWTEAAClaim.create(SDJWTConstants.USER_ADDRESS_STREET_ADDRESS, streetAddress));
        }
        if (Utils.isStringNotBlank(locality)) {
            claim.addChild(SDJWTEAAClaim.create(SDJWTConstants.USER_ADDRESS_LOCALITY, locality));
        }
        if (Utils.isStringNotBlank(region)) {
            claim.addChild(SDJWTEAAClaim.create(SDJWTConstants.USER_ADDRESS_REGION, region));
        }
        if (Utils.isStringNotBlank(postalCode)) {
            claim.addChild(SDJWTEAAClaim.create(SDJWTConstants.USER_ADDRESS_POSTAL_CODE, postalCode));
        }
        if (Utils.isStringNotBlank(country)) {
            claim.addChild(SDJWTEAAClaim.create(SDJWTConstants.USER_ADDRESS_COUNTRY, country));
        }
        if (Utils.isStringNotBlank(houseNumber)) {
            claim.addChild(SDJWTEAAClaim.create(SDJWTConstants.USER_ADDRESS_HOUSE_NUMBER, houseNumber));
        }
        return claim;
    }

    protected SDJWTEAAClaim buildEmailClaim(final String email, final boolean selectivelyDisclosable) {
        if (email == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.USER_EMAIL, email, selectivelyDisclosable);
    }

    protected SDJWTEAAClaim buildPhoneNumberClaim(final String phoneNumber, final boolean selectivelyDisclosable) {
        if (phoneNumber == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.USER_PHONE_NUMBER, phoneNumber, selectivelyDisclosable);
    }

    protected SDJWTEAAClaim buildPictureClaim(final String picture, final boolean selectivelyDisclosable) {
        if (picture == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.USER_PICTURE, picture, selectivelyDisclosable);
    }

    protected SDJWTEAAClaim buildNicknameClaim(final String nickname, final boolean selectivelyDisclosable) {
        if (nickname == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.USER_NICKNAME, nickname, selectivelyDisclosable);
    }

    protected SDJWTEAAClaim buildPreferredNicknameClaim(final String preferredNickname, final boolean selectivelyDisclosable) {
        if (preferredNickname == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.USER_PREFERRED_NICKNAME, preferredNickname, selectivelyDisclosable);
    }

    protected SDJWTEAAClaim buildNameClaim(final String name, final boolean selectivelyDisclosable) {
        if (name == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.USER_NAME, name, selectivelyDisclosable);
    }

    protected SDJWTEAAClaim buildMiddleNameClaim(final String middleName, final boolean selectivelyDisclosable) {
        if (middleName == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.USER_MIDDLE_NAME, middleName, selectivelyDisclosable);
    }

    protected SDJWTEAAClaim buildProfileClaim(final String profile, final boolean selectivelyDisclosable) {
        if (profile == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.USER_PROFILE, profile, selectivelyDisclosable);
    }

    protected SDJWTEAAClaim buildWebsiteClaim(final String website, final boolean selectivelyDisclosable) {
        if (website == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.USER_WEBSITE, website, selectivelyDisclosable);
    }

    protected SDJWTEAAClaim buildEmailVerifiedClaim(final Boolean emailVerified, final boolean selectivelyDisclosable) {
        if (emailVerified == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.USER_EMAIL_VERIFIED, emailVerified, selectivelyDisclosable);
    }

    protected SDJWTEAAClaim buildGenderClaim(final String gender, final boolean selectivelyDisclosable) {
        if (gender == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.USER_GENDER, gender, selectivelyDisclosable);
    }

    protected SDJWTEAAClaim buildZoneinfoClaim(final String zoneinfo, final boolean selectivelyDisclosable) {
        if (zoneinfo == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.USER_ZONEINFO, zoneinfo, selectivelyDisclosable);
    }

    protected SDJWTEAAClaim buildLocaleClaim(final String locale, final boolean selectivelyDisclosable) {
        if (locale == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.USER_LOCALE, locale, selectivelyDisclosable);
    }

    protected SDJWTEAAClaim buildPhoneNumberVerifiedClaim(final Boolean phoneNumberVerified, final boolean selectivelyDisclosable) {
        if (phoneNumberVerified == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.USER_PHONE_NUMBER_VERIFIED, phoneNumberVerified, selectivelyDisclosable);
    }

    protected SDJWTEAAClaim buildUpdatedAtClaim(final Date updatedAt, final boolean selectivelyDisclosable) {
        if (updatedAt == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.UPDATED_AT, DSSUtils.getTimeValueInSeconds(updatedAt.getTime()), selectivelyDisclosable);
    }

    protected SDJWTEAAClaimObject buildPlaceOfBirthClaim(final String country, final String region, final String locality,
                                                         final boolean selectivelyDisclosable) {
        if (Utils.areAllStringsEmpty(country, region, locality)) {
            return null;
        }

        SDJWTEAAClaimObject claim = new SDJWTEAAClaimObject(SDJWTConstants.USER_PLACE_OF_BIRTH, selectivelyDisclosable);
        if (Utils.isStringNotBlank(country)) {
            claim.addChild(SDJWTEAAClaim.create(SDJWTConstants.USER_PLACE_OF_BIRTH_COUNTRY, country));
        }
        if (Utils.isStringNotBlank(region)) {
            claim.addChild(SDJWTEAAClaim.create(SDJWTConstants.USER_PLACE_OF_BIRTH_REGION, region));
        }
        if (Utils.isStringNotBlank(locality)) {
            claim.addChild(SDJWTEAAClaim.create(SDJWTConstants.USER_PLACE_OF_BIRTH_LOCALITY, locality));
        }
        return claim;
    }

    protected SDJWTEAAClaim buildBirthFamilyNameClaim(final String birthFamilyName, final boolean selectivelyDisclosable) {
        if (birthFamilyName == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.USER_BIRTH_FAMILY_NAME, birthFamilyName, selectivelyDisclosable);
    }

    protected SDJWTEAAClaim buildBirthGivenNameClaim(final String birthGivenName, final boolean selectivelyDisclosable) {
        if (birthGivenName == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.USER_BIRTH_GIVEN_NAME, birthGivenName, selectivelyDisclosable);
    }

    protected SDJWTEAAClaim buildBirthMiddleNameClaim(final String birthMiddleName, final boolean selectivelyDisclosable) {
        if (birthMiddleName == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.USER_BIRTH_MIDDLE_NAME, birthMiddleName, selectivelyDisclosable);
    }

    protected SDJWTEAAClaim buildSalutationClaim(final String salutation, final boolean selectivelyDisclosable) {
        if (salutation == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.USER_SALUTATION, salutation, selectivelyDisclosable);
    }

    protected SDJWTEAAClaim buildTitleClaim(final String title, final boolean selectivelyDisclosable) {
        if (title == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.USER_TITLE, title, selectivelyDisclosable);
    }

    protected SDJWTEAAClaim buildMobilePhoneNumberClaim(final String mobilePhoneNumber, final boolean selectivelyDisclosable) {
        if (mobilePhoneNumber == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.USER_MOBILE_PHONE_NUMBER, mobilePhoneNumber, selectivelyDisclosable);
    }

    protected SDJWTEAAClaim buildPseudonymClaim(final String pseudonym, final boolean selectivelyDisclosable) {
        if (pseudonym == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.USER_PSEUDONYM, pseudonym, selectivelyDisclosable);
    }

    protected SDJWTEAAClaim buildAdministrativeValidityNotBeforeClaim(final Date administrativeValidityNotBefore,
                                                                      final boolean selectivelyDisclosable) {
        if (administrativeValidityNotBefore == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.ADMINISTRATIVE_VALIDITY_NOT_BEFORE,
                DSSUtils.formatDateToISO8601(administrativeValidityNotBefore), selectivelyDisclosable);
    }

    protected SDJWTEAAClaim buildAdministrativeValidityExpiryClaim(final Date administrativeValidityExpiry,
                                                                   final boolean selectivelyDisclosable) {
        if (administrativeValidityExpiry == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.ADMINISTRATIVE_VALIDITY_EXPIRY,
                DSSUtils.formatDateToISO8601(administrativeValidityExpiry), selectivelyDisclosable);
    }

    protected SDJWTEAAClaim buildDateOfExpiryClaim(final Date dateOfExpiry, final boolean selectivelyDisclosable) {
        if (dateOfExpiry == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.EXPIRY_DATE, DSSUtils.formatDateToISO8601(dateOfExpiry), selectivelyDisclosable);
    }

    protected SDJWTEAAClaim buildDateOfIssuanceClaim(final Date dateOfIssuance, final boolean selectivelyDisclosable) {
        if (dateOfIssuance == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.ISSUANCE_DATE, DSSUtils.formatDateToISO8601(dateOfIssuance), selectivelyDisclosable);
    }

    protected SDJWTEAAClaim buildPersonalAdministrativeNumberClaim(final String personalAdministrativeNumber, final boolean selectivelyDisclosable) {
        if (personalAdministrativeNumber == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.PERSONAL_ADMINISTRATIVE_NUMBER, personalAdministrativeNumber, selectivelyDisclosable);
    }

    protected SDJWTEAAClaim buildSexClaim(final Number sex, final boolean selectivelyDisclosable) {
        if (sex == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.SEX, sex, selectivelyDisclosable);
    }

    protected SDJWTEAAClaim buildIssuingAuthorityClaim(final String issuingAuthority, final boolean selectivelyDisclosable) {
        if (issuingAuthority == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.ISSUING_AUTHORITY, issuingAuthority, selectivelyDisclosable);
    }

    protected SDJWTEAAClaim buildIssuingCountryClaim(final String issuingCountry, final boolean selectivelyDisclosable) {
        if (issuingCountry == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.ISSUING_COUNTRY, issuingCountry, selectivelyDisclosable);
    }

    protected SDJWTEAAClaim buildDocumentNumberClaim(final String documentNumber, final boolean selectivelyDisclosable) {
        if (documentNumber == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.DOCUMENT_NUMBER, documentNumber, selectivelyDisclosable);
    }

    protected SDJWTEAAClaim buildIssuingJurisdictionClaim(final String issuingJurisdiction, final boolean selectivelyDisclosable) {
        if (issuingJurisdiction == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.ISSUING_JURISDICTION, issuingJurisdiction, selectivelyDisclosable);
    }

    protected SDJWTEAAClaim buildAgeInYearsClaim(final Number ageInYears, final boolean selectivelyDisclosable) {
        if (ageInYears == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.AGE_IN_YEARS, ageInYears, selectivelyDisclosable);
    }

    protected SDJWTEAAClaim buildAgeBirthYearClaim(final Number ageBirthYear, final boolean selectivelyDisclosable) {
        if (ageBirthYear == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.AGE_BIRTH_YEAR, ageBirthYear, selectivelyDisclosable);
    }

    protected SDJWTEAAClaim buildTrustAnchorClaim(final String trustAnchor, final boolean selectivelyDisclosable) {
        if (trustAnchor == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.TRUST_ANCHOR, trustAnchor, selectivelyDisclosable);
    }

    protected SDJWTEAAClaimObject buildAgeEqualOrOverClaim(final Map<Integer, Boolean> ageEqualOrOver, final boolean selectivelyDisclosable) {
        if (ageEqualOrOver == null) {
            return null;
        }
        SDJWTEAAClaimObject claim = new SDJWTEAAClaimObject(SDJWTConstants.AGE_EQUAL_OR_OVER, selectivelyDisclosable);
        ageEqualOrOver.forEach((age, verified) -> {
            if (age != null && verified != null) {
                claim.addChild(SDJWTEAAClaim.create(Integer.toString(age), verified));
            }
        });
        return claim;
    }

    protected SDJWTEAAClaim buildVerifiableCredentialsTypeClaim(final String verifiableCredentialsType,
                                                                final boolean selectivelyDisclosable) {
        if (verifiableCredentialsType == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.VERIFIABLE_CREDENTIALS_TYPE, verifiableCredentialsType, selectivelyDisclosable);
    }

    protected SDJWTEAAClaim buildVerifiableCredentialsIntegrityClaim(final String verifiableCredentialsIntegrity,
                                                                     final boolean selectivelyDisclosable) {
        if (verifiableCredentialsIntegrity == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.VERIFIABLE_CREDENTIALS_INTEGRITY, verifiableCredentialsIntegrity,
                selectivelyDisclosable);
    }

    protected SDJWTEAAClaim buildCategoryClaim(final String category, final boolean selectivelyDisclosable) {
        if (category == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.CATEGORY, category, selectivelyDisclosable);
    }

    protected SDJWTEAAClaim buildIssuingRegistrationIdentifierClaim(final String issuingRegistrationIdentifier,
                                                                    final boolean selectivelyDisclosable) {
        if (issuingRegistrationIdentifier == null) {
            return null;
        }
        return buildClaim(SDJWTConstants.ISSUING_REGISTRATION_IDENTIFIER,
                issuingRegistrationIdentifier, selectivelyDisclosable);
    }

    protected SDJWTEAAClaim buildClaim(final String name, final Object value, final boolean selectivelyDisclosable) {
        return selectivelyDisclosable ? SDJWTEAAClaim.createSelectivelyDisclosable(name, value) : SDJWTEAAClaim.create(name, value);
    }

    protected void addIfNotNull(final List<SDJWTEAAClaim> claims, final SDJWTEAAClaim claim) {
        if (claim != null) {
            claims.add(claim);
        }
    }
}
