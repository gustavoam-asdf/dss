package eu.europa.esig.dss.eaa.jwt;

import eu.europa.esig.dss.model.eaa.SelectivelyDisclosableClaim;
import eu.europa.esig.dss.jades.DSSJsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class contains utility methods for processing SD-JWT tokens
 *
 */
public final class SDJWTUtils {

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
     * @param claimValue {@link Object} to parse
     * @return a list of {@link SelectivelyDisclosableClaim}s
     */
    public static List<SelectivelyDisclosableClaim> getNestedSelectivelyDisclosableClaims(String claimName, Object claimValue) {
        final List<SelectivelyDisclosableClaim> result = new ArrayList<>();
        if (claimValue instanceof List<?>) {
            List<?> list = (List<?>) claimValue;
            for (Object listItem : list) {
                if (listItem instanceof Map<?, ?>) {
                    Map<?, ?> mapItem = (Map<?, ?>) listItem;
                    if (mapItem.size() == 1) {
                        Map.Entry<?, ?> mapEntry = mapItem.entrySet().iterator().next();
                        if (SDJWTConstants.HASH.equals(mapEntry.getKey()) && mapEntry.getValue() instanceof String) {
                            String hashValue = (String) mapEntry.getValue();
                            if (DSSJsonUtils.isBase64UrlEncoded(hashValue)) {
                                SelectivelyDisclosableClaim sdClaim = new SelectivelyDisclosableClaim();
                                sdClaim.setClaimName(claimName);
                                sdClaim.setDigestValue(DSSJsonUtils.fromBase64Url(hashValue));
                                result.add(sdClaim);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

}
