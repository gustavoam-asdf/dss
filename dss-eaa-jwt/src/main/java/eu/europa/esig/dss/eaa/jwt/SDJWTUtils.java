package eu.europa.esig.dss.eaa.jwt;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
