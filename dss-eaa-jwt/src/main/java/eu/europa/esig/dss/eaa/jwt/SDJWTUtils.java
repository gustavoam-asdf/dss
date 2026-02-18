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
     * when applicable
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
            LOG.warn("Unable to find a corresponding DigestAlgortihm for value '{}'!", sdJwtId);
            return null;
        }
    }

}
