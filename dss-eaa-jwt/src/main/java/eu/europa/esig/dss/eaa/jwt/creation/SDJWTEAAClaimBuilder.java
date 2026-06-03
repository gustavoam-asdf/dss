package eu.europa.esig.dss.eaa.jwt.creation;

import java.util.List;

/**
 * Builds claims for the SD-JWT VC EAA
 *
 */
public interface SDJWTEAAClaimBuilder {

    /**
     * Creates claims for the given payload parameters
     *
     * @param payloadParameters {@link SDJWTEAAPayloadParameters}
     * @return a list of {@link SDJWTEAAClaim}s
     */
    List<SDJWTEAAClaim> buildClaims(SDJWTEAAPayloadParameters payloadParameters);

}
