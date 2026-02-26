package eu.europa.esig.dss.eaa.jwt;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.jades.DSSJsonUtils;
import eu.europa.esig.dss.model.eaa.claim.Claim;
import eu.europa.esig.dss.model.eaa.claim.ClaimBinaries;
import eu.europa.esig.dss.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This class contains utility methods for processing SD-JWT tokens
 *
 */
public final class SDJWTUtils {

    private static final Logger LOG = LoggerFactory.getLogger(SDJWTUtils.class);

    /**
     * Singleton
     */
    private SDJWTUtils() {
        // empty
    }

    /**
     * This method verifies the format of the object and returns the nested selectively disclosable claims,
     * when applicable.
     * This method allows nested disclosures extraction without knowing a name of the parent claim (e.g. for an EAA Payload)
     *
     * @param claim {@link Claim} representing the selectively disclosable claim value
     * @return a list of {@link ClaimBinaries}s
     */
    public static List<ClaimBinaries> getNestedSelectivelyDisclosableClaims(Claim claim) {
        return getNestedSelectivelyDisclosableClaims(null, claim);
    }

    /**
     * This method verifies the format of the object and returns the nested selectively disclosable claims,
     * when applicable.
     * This method provides a possibility to specify the name of the processing discosure.
     *
     * @param claimName {@link String} representing the name of the selectively disclosable claim
     * @param claim {@link Claim} representing the selectively disclosable claim value
     * @return a list of {@link ClaimBinaries}s
     */
    public static List<ClaimBinaries> getNestedSelectivelyDisclosableClaims(String claimName, Claim claim) {
        if (claim == null) {
            return Collections.emptyList();
        }

        final List<ClaimBinaries> result = new ArrayList<>();

        if (claim.isMapValueType()) {
            Map<String, Claim> claimMap = claim.getMapValue();
            // 1. Extract _sd header hashes
            Claim _sdClaim = claimMap.get(SDJWTConstants._SD);
            if (_sdClaim == null || !_sdClaim.isArrayValueType() || _sdClaim.isNullOrEmpty()) {
                return Collections.emptyList();
            }

            for (Claim sdHashObject : _sdClaim.getListValue()) {
                String sdHash = sdHashObject.getStringValue();
                if (sdHash == null) {
                    LOG.warn("Value of the '{}' shall be represented by a String!", SDJWTConstants._SD);
                    continue;
                }
                if (!DSSJsonUtils.isBase64UrlEncoded(sdHash)) {
                    LOG.warn("Value of the '{}' shall be base64url encoded!", SDJWTConstants._SD);
                    continue;
                }
                result.add(new ClaimBinaries(DSSJsonUtils.fromBase64Url(sdHash)));
            }

            // 2. Look for selectively disclosable array items
            for (Map.Entry<String, Claim> entry : claimMap.entrySet()) {
                String headerName = entry.getKey();
                /*
                 * 4.2.1. Disclosures for Object Properties (RFC 9901)
                 *
                 * 2. The claim name, or key, as it would be used in a regular JWT payload.
                 * It MUST be a string and MUST NOT be _sd, ..., or a claim name existing in
                 * the object as a permanently disclosed claim.
                 */
                if (SDJWTConstants._SD.equals(headerName) || SDJWTConstants.HASH.equals(headerName)) {
                    continue;
                }

                /*
                 * 4.2.4.1. Object Properties  (RFC 9901)
                 *
                 * Digests of Disclosures for object properties are added to an array under
                 * the new key _sd in the object. The _sd key MUST refer to an array of strings,
                 * each string being a digest of a Disclosure or a decoy digest as described in
                 * Section 4.2.5. An _sd key can be present at any level of the JSON object hierarchy,
                 * including at the top-level, nested deeper as described in Section 6, or in
                 * recursive Disclosures as described in Section 4.2.6.
                 */
                result.addAll(getNestedSelectivelyDisclosableClaims(headerName, entry.getValue()));
            }

        } else if (claim.isArrayValueType()) {
            List<Claim> claimArray = claim.getListValue();
            if (Utils.isCollectionNotEmpty(claimArray)) {
                for (Claim arrayItem : claimArray) {
                    if (arrayItem.isMapValueType()) {
                        Map<String, Claim> mapItem = arrayItem.getMapValue();
                        if (mapItem.size() == 1) {
                            Map.Entry<String, Claim> mapEntry = mapItem.entrySet().iterator().next();
                            if (SDJWTConstants.HASH.equals(mapEntry.getKey()) && mapEntry.getValue().isStringValueType()) {
                                String hashValue = mapEntry.getValue().getStringValue();
                                if (DSSJsonUtils.isBase64UrlEncoded(hashValue)) {
                                    result.add(new ClaimBinaries(claimName, DSSJsonUtils.fromBase64Url(hashValue)));
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * Gets a DigestAlgorithm value for the given {@code sdJwtId} in a secure way (no exception)
     *
     * @param sdJwtId {@link String} to get a corresponding digest algorithm for
     * @return {@link DigestAlgorithm}
     */
    public static DigestAlgorithm getDigestAlgorithmForSdJwtId(String sdJwtId) {
        try {
            return DigestAlgorithm.forSdJwtId(sdJwtId);
        } catch (IllegalArgumentException e) {
            LOG.warn("Unable to find a corresponding DigestAlgortihm for SD-JWT claim for value '{}'!", sdJwtId);
            return null;
        }
    }

    /**
     * Gets a DigestAlgorithm value for the given {@code srIntegrityId} in a secure way (no exception)
     *
     * @param srIntegrityId {@link String} to get a corresponding digest algorithm for
     * @return {@link DigestAlgorithm}
     */
    public static DigestAlgorithm getDigestAlgorithmForIntegrityClaimId(String srIntegrityId) {
        try {
            return DigestAlgorithm.forSrIntegrityId(srIntegrityId);
        } catch (IllegalArgumentException e) {
            LOG.warn("Unable to find a corresponding DigestAlgortihm for integrity claim for value '{}'!", srIntegrityId);
            return null;
        }
    }

}
