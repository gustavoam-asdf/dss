package eu.europa.esig.dss.eaa.jwt.validation;

import eu.europa.esig.dss.eaa.common.validation.EAAPayloadVerifier;
import eu.europa.esig.dss.eaa.jwt.SDJWTConstants;
import eu.europa.esig.dss.eaa.jwt.SDJWTUtils;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.DigestMatcherType;
import eu.europa.esig.dss.jades.DSSJsonUtils;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.model.Digest;
import eu.europa.esig.dss.model.eaa.Disclosure;
import eu.europa.esig.dss.model.eaa.DisclosureValidation;
import eu.europa.esig.dss.model.eaa.claim.Claim;
import eu.europa.esig.dss.model.eaa.claim.ClaimArray;
import eu.europa.esig.dss.model.eaa.claim.ClaimMap;
import eu.europa.esig.dss.model.eaa.claim.ClaimString;
import eu.europa.esig.dss.spi.exception.IllegalInputException;
import eu.europa.esig.dss.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This class verifies selectively disclosable claims, when provided, and computes the combined version of
 * the EAA payload, which includes the non-selectively disclosable claims as well as disclosed claims.
 * This class requires execution of {@code #verify} method before accessing the validation results.
 *
 */
public class SDJWTPayloadVerifier extends EAAPayloadVerifier {

    private static final Logger LOG = LoggerFactory.getLogger(SDJWTPayloadVerifier.class);

    /**
     * Payload map to be verified
     */
    private final String jsonPayload;

    /**
     * Default constructor
     *
     * @param jsonPayload {@link String} JSON payload to be verified
     */
    public SDJWTPayloadVerifier(final String jsonPayload) {
        Objects.requireNonNull(jsonPayload, "Payload cannot be null!");
        this.jsonPayload = jsonPayload;
    }

    /**
     * This method performs the verification process for the provided payload and disclosures
     * NOTE: The process can be executed only once
     */
    @Override
    public void verify() {
        ClaimMap originalPayloadMap = parseJsonPayload();
        this.disclosureValidations = new ArrayList<>();
        this.sdDigestAlgorithm = getSDDigestAlgorithm(originalPayloadMap);
        ClaimMap verifiedPayloadMap = (ClaimMap) buildClaimWithDisclosures(originalPayloadMap);
        this.verifiedPayload = new SDJWTPayload(verifiedPayloadMap);
    }

    private ClaimMap parseJsonPayload() {
        try {
            Map<String, Object> payloadMap = DSSJsonUtils.parseJsonStringToMap(jsonPayload);
            Claim payloadClaim = Claim.create(payloadMap);
            if (payloadClaim.isMapValueType()) {
                return (ClaimMap) payloadClaim;
            } else {
                throw new IllegalInputException("SD-JWT Payload shall be of a JSON Map type!");
            }
        } catch (Exception e) {
            throw new DSSException(String.format("An error occurred on reading SD-JWT Payload : %s", e.getMessage()), e);
        }
    }

    private DigestAlgorithm getSDDigestAlgorithm(ClaimMap payloadMap) {
        ClaimString _sd_alg = payloadMap.getAsString(SDJWTConstants._SD_ALG);
        if (_sd_alg != null) {
            return SDJWTUtils.getDigestAlgorithmForSdJwtId(_sd_alg.getStringValue());
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

    private Claim buildClaimWithDisclosures(Claim originalClaim) {
        // re-build to ensure original is not modified
        if (originalClaim.isMapValueType()) {
            return buildClaimMap((ClaimMap) originalClaim);
        } else if (originalClaim.isArrayValueType()) {
            return buildClaimArray((ClaimArray) originalClaim);
        }
        // in other cases, keep the original
        return originalClaim;
    }

    private Claim buildClaimMap(ClaimMap originalClaimMap) {
        final Map<String, Claim> result = new HashMap<>(); // TODO : LinkedHashMap ?
        for (Map.Entry<String, Claim> entry : originalClaimMap.getMapValue().entrySet()) {
            String headerName = entry.getKey();
            Claim claimValue = entry.getValue();
            if (SDJWTConstants._SD.equals(headerName)) {
                List<Claim> claims = buildSelectivelyDisclosableClaimsForArray(claimValue);
                for (Claim claim : claims) {
                    claim = buildClaimWithDisclosures(claim);
                    if (claim != null) {
                        if (claim.getName() != null) {
                            result.put(claim.getName(), claim);
                        } else {
                            LOG.warn("No claim name is present for the disclosure when matching an '{}' value!", SDJWTConstants._SD);
                        }
                    }
                }

            } else if (SDJWTConstants._SD_ALG.equals(headerName)) {
                // skip _sd_alg values
                continue;

            } else {
                claimValue = buildClaimWithDisclosures(claimValue);
                if (claimValue != null) {
                    result.put(headerName, claimValue);
                }
            }

        }
        return Claim.create(originalClaimMap.getName(), originalClaimMap.getParent(), result, originalClaimMap.isSelectivelyDisclosable());
    }

    private Claim buildClaimArray(ClaimArray originalClaimArray) {
        final List<Claim> result = new ArrayList<>();
        for (Claim claimItem : originalClaimArray.getListValue()) {
            // skip plain hashes
            if (isSDClaimHashItem(claimItem)) {
                ClaimString hashClaim = ((ClaimMap) claimItem).getAsString(SDJWTConstants.HASH);
                claimItem = buildSelectivelyDisclosableClaim(hashClaim);
            } else {
                claimItem = buildClaimWithDisclosures(claimItem);
            }
            if (claimItem != null) {
                result.add(claimItem);
            }
        }
        return Claim.create(originalClaimArray.getName(), originalClaimArray.getParent(), result, originalClaimArray.isSelectivelyDisclosable());
    }

    private boolean isSDClaimHashItem(Claim claim) {
        if (claim.isMapValueType()) {
            ClaimMap claimMap = (ClaimMap) claim;
            if (claimMap.getSize() == 1 && claimMap.getAsString(SDJWTConstants.HASH) != null) {
                return true;
            }
        }
        return false;
    }

    private List<Claim> buildSelectivelyDisclosableClaimsForArray(Claim _sdClaim) {
        if (!_sdClaim.isArrayValueType()) {
            LOG.warn("_sd header shall be of type of JSON array!");
            return Collections.emptyList();
        }

        final List<Claim> result = new ArrayList<>();

        List<Claim> sdClaims = _sdClaim.getListValue();
        for (Claim sdClaim : sdClaims) {
            Claim claim = buildSelectivelyDisclosableClaim(sdClaim);
            if (claim != null) {
                result.add(claim);
            }
        }

        return result;
    }

    private Claim buildSelectivelyDisclosableClaim(Claim sdHashClaim) {
        byte[] sdHashBytes = sdClaimToBinary(sdHashClaim);
        DisclosureValidation disclosureValidation = getSelectivelyDisclosableClaimValidation(sdHashBytes);
        if (disclosureValidation != null) {
            disclosureValidations.add(disclosureValidation);
            if (disclosureValidation.isFound() && disclosureValidation.isIntact() && disclosureValidation.getDisclosure() != null) {
                return disclosureValidation.getDisclosure().getClaimValue();
            }
        }
        return null;
    }

    private byte[] sdClaimToBinary(Claim sdClaim) {
        if (!sdClaim.isStringValueType()) {
            LOG.warn("Selective disclosure hash claim value shall be of String type!");
            return null;
        }
        String sdB64Url = sdClaim.getStringValue();
        if (!DSSJsonUtils.isBase64UrlEncoded(sdB64Url)) {
            LOG.warn("Selective disclosure hash claim value shall be base64url encoded!");
            return null;
        }
        try {
            return DSSJsonUtils.fromBase64Url(sdB64Url);

        } catch (Exception e) {
            String errorMessage = "An error occurred on selective disclosure hash decoding : {}";
            if (LOG.isDebugEnabled()) {
                LOG.warn(errorMessage, e.getMessage(), e);
            } else {
                LOG.warn(errorMessage, e.getMessage());
            }
            return null;
        }
    }

    private DisclosureValidation getSelectivelyDisclosableClaimValidation(byte[] sdHash) {
        if (sdHash == null) {
            return null;
        }
        DisclosureValidation disclosureValidation;
        Disclosure disclosure = getDisclosureForClaimHash(sdHash);
        if (disclosure != null) {
            disclosureValidation = new DisclosureValidation(disclosure);
            disclosureValidation.setType(DigestMatcherType.EAA_DISCLOSURE);
            disclosureValidation.setDigest(new Digest(sdDigestAlgorithm, sdHash));
            disclosureValidation.setFound(true);
            disclosureValidation.setIntact(true);

        } else {
            disclosureValidation = new DisclosureValidation();
            disclosureValidation.setType(DigestMatcherType.EAA_ORPHAN_SELECTIVELY_DISCLOSABLE_CLAIM);
            disclosureValidation.setDigest(new Digest(sdDigestAlgorithm, sdHash));
        }
        return disclosureValidation;
    }

    private Disclosure getDisclosureForClaimHash(byte[] sdHash) {
        if (Utils.isCollectionEmpty(disclosures)) {
            LOG.debug("No disclosures has been provided. Unable to validate a selectively disclosable claim.");
            return null;
        }
        for (Disclosure disclosure : disclosures) {
            Digest disclosureDigest = disclosure.getDigest(sdDigestAlgorithm);
            if (disclosureDigest != null && !disclosureDigest.isEmpty() && Arrays.equals(sdHash, disclosureDigest.getValue())) {
                return disclosure;
            }
        }
        return null;
    }

}
