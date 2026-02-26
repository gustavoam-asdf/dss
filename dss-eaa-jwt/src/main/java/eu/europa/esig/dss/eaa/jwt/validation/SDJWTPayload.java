package eu.europa.esig.dss.eaa.jwt.validation;

import eu.europa.esig.dss.eaa.jwt.SDJWTConstants;
import eu.europa.esig.dss.eaa.jwt.SDJWTUtils;
import eu.europa.esig.dss.eaa.jwt.claim.SDJWTClaimAddress;
import eu.europa.esig.dss.eaa.jwt.claim.SDJWTClaimIntegrity;
import eu.europa.esig.dss.eaa.jwt.claim.SDJWTClaimPlaceOfBirth;
import eu.europa.esig.dss.eaa.jwt.claim.SDJWTClaimStatus;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.jades.DSSJsonUtils;
import eu.europa.esig.dss.model.eaa.DisclosureValidation;
import eu.europa.esig.dss.model.eaa.claim.Claim;
import eu.europa.esig.dss.model.eaa.claim.ClaimAddress;
import eu.europa.esig.dss.model.eaa.claim.ClaimArray;
import eu.europa.esig.dss.model.eaa.claim.ClaimBinaries;
import eu.europa.esig.dss.model.eaa.claim.ClaimBoolean;
import eu.europa.esig.dss.model.eaa.claim.ClaimDate;
import eu.europa.esig.dss.model.eaa.claim.ClaimIntegrity;
import eu.europa.esig.dss.model.eaa.claim.ClaimMap;
import eu.europa.esig.dss.model.eaa.claim.ClaimNumber;
import eu.europa.esig.dss.model.eaa.claim.ClaimPlaceOfBirth;
import eu.europa.esig.dss.model.eaa.claim.ClaimStatus;
import eu.europa.esig.dss.model.eaa.claim.ClaimString;
import eu.europa.esig.dss.spi.eaa.EAAPayload;
import eu.europa.esig.dss.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class implements a user-friendly access to the EAA payload elements of the SD-JWT token
 *
 */
public class SDJWTPayload implements EAAPayload {

    private static final long serialVersionUID = -4552799683587409954L;

    private static final Logger LOG = LoggerFactory.getLogger(SDJWTPayload.class);

    /** Represents a map of objects defined within EAA payload */
    private final Map<String, Claim> payloadMap;

    /** List of disclosure validations */
    private final List<DisclosureValidation> disclosureValidations;

    /**
     * Default constructor
     *
     * @param payloadMap map of payload map headers and corresponding extracted values
     */
    public SDJWTPayload(final Map<String, Claim> payloadMap) {
        this(payloadMap, Collections.emptyList());
    }

    /**
     * Constructor with provided disclosure validations
     *
     * @param payloadMap map of payload map headers and corresponding extracted values
     * @param disclosureValidations a list of {@link DisclosureValidation}s
     */
    public SDJWTPayload(final Map<String, Claim> payloadMap, final List<DisclosureValidation> disclosureValidations) {
        this.payloadMap = payloadMap;
        this.disclosureValidations = disclosureValidations;
    }


    @Override
    public List<ClaimBinaries> getSelectiveDisclosableClaims() {
        Claim payloadAsClaim = Claim.create(payloadMap);
        return SDJWTUtils.getNestedSelectivelyDisclosableClaims(payloadAsClaim);
    }

    @Override
    public DigestAlgorithm getSelectiveDisclosableClaimDigestAlgorithm() {
        ClaimString digestAlgoName = getValueAsString(SDJWTConstants._SD_ALG);
        if (digestAlgoName != null) {
            return SDJWTUtils.getDigestAlgorithmForSdJwtId(digestAlgoName.getStringValue());
        }
        /*
         * 4.2.3. Hashing Disclosures (draft-ietf-oauth-selective-disclosure-jwt-22)
         *
         * For embedding references to the Disclosures in the SD-JWT, each Disclosure is hashed
         * using the hash algorithm specified in the _sd_alg claim described in Section 4.1.1,
         * or SHA-256 if no algorithm is specified.
         */
        return DigestAlgorithm.SHA256;
    }

    @Override
    public ClaimString getIdentifier() {
        return getValueAsString(SDJWTConstants.JWT_ID);
    }

    @Override
    public ClaimString getIssuer() {
        return getValueAsString(SDJWTConstants.ISSUER);
    }

    @Override
    public ClaimString getSubject() {
        return getValueAsString(SDJWTConstants.SUBJECT);
    }

    @Override
    public ClaimArray getAudience() {
        return getValueAsArray(SDJWTConstants.AUDIENCE);
    }

    @Override
    public ClaimDate getExpirationTime() {
        return getValueAsDateTime(SDJWTConstants.EXPIRATION_TIME);
    }

    @Override
    public ClaimDate getNotBeforeTime() {
        return getValueAsDateTime(SDJWTConstants.NOT_BEFORE);
    }

    @Override
    public ClaimDate getIssuedAtTime() {
        return getValueAsDateTime(SDJWTConstants.ISSUED_AT);
    }

    @Override
    public ClaimDate getUpdatedAtTime() {
        return getValueAsDateTime(SDJWTConstants.UPDATED_AT);
    }

    @Override
    public ClaimString getCategory() {
        return getValueAsString(SDJWTConstants.CATEGORY);
    }

    @Override
    public ClaimString getMetadataType() {
        return getValueAsString(SDJWTConstants.VERIFIABLE_CREDENTIALS_TYPE);
    }

    @Override
    public ClaimIntegrity getMetadataIntegrity() {
        ClaimString metadataIntegrity = getValueAsString(SDJWTConstants.VERIFIABLE_CREDENTIALS_INTEGRITY);
        if (metadataIntegrity != null) {
            return new SDJWTClaimIntegrity(metadataIntegrity);
        }
        return null;
    }

    @Override
    public ClaimStatus getStatus() {
        ClaimMap statusClaim = getValueAsMap(SDJWTConstants.STATUS);
        if (statusClaim != null) {
            return new SDJWTClaimStatus(statusClaim);
        }
        return null;
    }

    @Override
    public ClaimString getNonce() {
        return getValueAsString(SDJWTConstants.NONCE);
    }

    @Override
    public ClaimString getFullName() {
        return getValueAsString(SDJWTConstants.USER_NAME);
    }

    @Override
    public ClaimString getFirstName() {
        return getValueAsString(SDJWTConstants.USER_GIVEN_NAME);
    }

    @Override
    public ClaimString getLastName() {
        return getValueAsString(SDJWTConstants.USER_FAMILY_NAME);
    }

    @Override
    public ClaimString getMiddleName() {
        return getValueAsString(SDJWTConstants.USER_MIDDLE_NAME);
    }

    @Override
    public ClaimString getNickname() {
        return getValueAsString(SDJWTConstants.USER_NICKNAME);
    }

    @Override
    public ClaimString getShortName() {
        return getValueAsString(SDJWTConstants.USER_PREFERRED_NICKNAME);
    }

    @Override
    public ClaimString getProfileUrl() {
        return getValueAsString(SDJWTConstants.USER_PROFILE);
    }

    @Override
    public ClaimString getPictureUrl() {
        return getValueAsString(SDJWTConstants.USER_PICTURE);
    }

    @Override
    public ClaimString getWebsiteUrl() {
        return getValueAsString(SDJWTConstants.USER_WEBSITE);
    }

    @Override
    public ClaimString getEmail() {
        return getValueAsString(SDJWTConstants.USER_EMAIL);
    }

    @Override
    public ClaimBoolean getEmailVerified() {
        return getValueAsBoolean(SDJWTConstants.USER_EMAIL_VERIFIED);
    }

    @Override
    public ClaimString getGender() {
        return getValueAsString(SDJWTConstants.USER_GENDER);
    }

    @Override
    public ClaimDate getBirthdate() {
        return getValueAsDate(SDJWTConstants.USER_BIRTHDATE);
    }

    @Override
    public ClaimString getTimezone() {
        return getValueAsString(SDJWTConstants.USER_ZONEINFO);
    }

    @Override
    public ClaimString getLocale() {
        return getValueAsString(SDJWTConstants.USER_LOCALE);
    }

    @Override
    public ClaimAddress getAddress() {
        ClaimMap claimAddress = getValueAsMap(SDJWTConstants.USER_ADDRESS);
        if (claimAddress != null) {
            return new SDJWTClaimAddress(claimAddress);
        }
        return null;
    }

    @Override
    public ClaimString getPhoneNumber() {
        return getValueAsString(SDJWTConstants.USER_PHONE_NUMBER);
    }

    @Override
    public ClaimBoolean getPhoneNumberVerified() {
        return getValueAsBoolean(SDJWTConstants.USER_PHONE_NUMBER_VERIFIED);
    }

    @Override
    public ClaimPlaceOfBirth getPlaceOfBirth() {
        ClaimMap claimPlaceOfBirth = getValueAsMap(SDJWTConstants.USER_PLACE_OF_BIRTH);
        if (claimPlaceOfBirth != null) {
            return new SDJWTClaimPlaceOfBirth(claimPlaceOfBirth);
        }
        return null;
    }

    @Override
    public ClaimArray getNationalities() {
        return getValueAsArray(SDJWTConstants.USER_NATIONALITIES);
    }

    @Override
    public ClaimString getBirthFirstName() {
        return getValueAsString(SDJWTConstants.USER_BIRTH_GIVEN_NAME);
    }

    @Override
    public ClaimString getBirthLastName() {
        return getValueAsString(SDJWTConstants.USER_BIRTH_FAMILY_NAME);
    }

    @Override
    public ClaimString getBirthMiddleName() {
        return getValueAsString(SDJWTConstants.USER_BIRTH_MIDDLE_NAME);
    }

    @Override
    public ClaimString getSalutation() {
        return getValueAsString(SDJWTConstants.USER_SALUTATION);
    }

    @Override
    public ClaimString getTitle() {
        return getValueAsString(SDJWTConstants.USER_TITLE);
    }

    @Override
    public ClaimString getMobilePhoneNumber() {
        return getValueAsString(SDJWTConstants.USER_MOBILE_PHONE_NUMBER);
    }

    @Override
    public ClaimString getPseudonym() {
        return getValueAsString(SDJWTConstants.USER_PSEUDONYM);
    }

    @Override
    public Map<String, Claim> getClaimMap() {
        Map<String, Claim> result = new HashMap<>(payloadMap);
        result.remove(SDJWTConstants._SD);
        result.remove(SDJWTConstants._SD_ALG);
        for (String key : payloadMap.keySet()) {
            Claim claim = payloadMap.get(key);
            if (claim.isMapValueType()) {
                Map<String, Claim> claimMap = claim.getMapValue();
                if (Utils.mapSize(claimMap) == 1 && claimMap.get(SDJWTConstants._SD) != null) {
                    result.remove(key);
                }
            }
        }
        if (Utils.isCollectionNotEmpty(disclosureValidations)) {
            for (DisclosureValidation disclosureValidation : disclosureValidations) {
                if (disclosureValidation.getClaimName() != null) {
                    result.put(disclosureValidation.getClaimName(), disclosureValidation.getProcessedValue());
                }
            }
        }
        return result;
    }

    /**
     * Gets value of a header with name {@code headerName} as ClaimString.
     * Returns NULL if no value is provided or the Claim is of a different type.
     *
     * @param headerName {@link String} to get header value from the payload
     * @return {@link ClaimString}
     */
    protected ClaimString getValueAsString(String headerName) {
        Claim claim = getClaim(headerName);
        if (claim != null && claim.isStringValueType()) {
            return (ClaimString) claim;
        }
        return null;
    }

    /**
     * Gets value of a header with name {@code headerName} as ClaimArray.
     * Returns NULL if no value is provided or the Claim is of a different type.
     *
     * @param headerName {@link String} to get header value from the payload
     * @return {@link ClaimArray}
     */
    protected ClaimArray getValueAsArray(String headerName) {
        Claim claim = getClaim(headerName);
        if (claim != null && claim.isArrayValueType()) {
            return (ClaimArray) claim;
        }
        return null;
    }

    /**
     * Gets value of a header with name {@code headerName} as ClaimMap.
     * Returns NULL if no value is provided or the Claim is of a different type.
     *
     * @param headerName {@link String} to get header value from the payload
     * @return {@link ClaimMap}
     */
    protected ClaimMap getValueAsMap(String headerName) {
        Claim claim = getClaim(headerName);
        if (claim != null && claim.isMapValueType()) {
            return (ClaimMap) claim;
        }
        return null;
    }

    /**
     * Gets value of a header with name {@code headerName} as ClaimDate.
     * Returns NULL if no value is provided or the Claim is of a different type.
     *
     * @param headerName {@link String} to get header value from the payload
     * @return {@link ClaimDate}
     */
    protected ClaimDate getValueAsDate(String headerName) {
        ClaimString claimString = getValueAsString(headerName);
        if (claimString != null) {
            Date date = DSSJsonUtils.getIsoDate(claimString.getStringValue());
            if (date != null) {
                return new ClaimDate(headerName, date, claimString.isSelectivelyDisclosable());
            }
        }
        return null;
    }

    /**
     * Gets value of a header with name {@code headerName} as ClaimDate.
     * Returns NULL if no value is provided or the Claim is of a different type.
     *
     * @param headerName {@link String} to get header value from the payload
     * @return {@link ClaimDate}
     */
    protected ClaimDate getValueAsDateTime(String headerName) {
        ClaimString claimString = getValueAsString(headerName);
        if (claimString != null) {
            Date date = DSSJsonUtils.getDate(claimString.getStringValue());
            if (date != null) {
                return new ClaimDate(headerName, date, claimString.isSelectivelyDisclosable());
            }
        }
        ClaimNumber claimNumber = getValueAsNumber(headerName);
        if (claimNumber != null) {
            long timeValueInMilliseconds = DSSJsonUtils.getTimeValueInMilliseconds(claimNumber.getNumberValue().longValue());
            Date date = DSSJsonUtils.getDate(timeValueInMilliseconds);
            return new ClaimDate(headerName, date, claimNumber.isSelectivelyDisclosable());
        }
        return null;
    }

    /**
     * Gets value of a header with name {@code headerName} as ClaimNumber.
     * Returns NULL if no value is provided or the Claim is of a different type.
     *
     * @param headerName {@link String} to get header value from the payload
     * @return {@link ClaimNumber}
     */
    protected ClaimNumber getValueAsNumber(String headerName) {
        Claim claim = getClaim(headerName);
        if (claim != null && claim.isNumberValueType()) {
            return (ClaimNumber) claim;
        }
        return null;
    }

    /**
     * Gets value of a header with name {@code headerName} as ClaimBoolean.
     * Returns NULL if no value is provided or the Claim is of a different type.
     *
     * @param headerName {@link String} to get header value from the payload
     * @return {@link ClaimBoolean}
     */
    protected ClaimBoolean getValueAsBoolean(String headerName) {
        Claim claim = getClaim(headerName);
        if (claim != null && claim.isBooleanValueType()) {
            return (ClaimBoolean) claim;
        }
        return null;
    }

    /**
     * Gets a claim with the given {@code headerName}
     *
     * @param headerName {@link String} to get a corresponding value of the claim for
     * @return {@link Claim}
     */
    protected Claim getClaim(String headerName) {
        Claim claim = payloadMap.get(headerName);
        if (claim != null && !claim.isArrayValueType()) {
            return claim;
        }
        if (Utils.isCollectionNotEmpty(disclosureValidations)) {
            for (DisclosureValidation disclosureValidation : disclosureValidations) {
                if (headerName.equals(disclosureValidation.getClaimName())) {
                    return disclosureValidation.getProcessedValue();
                }
            }
        }
        return null;
    }

}
