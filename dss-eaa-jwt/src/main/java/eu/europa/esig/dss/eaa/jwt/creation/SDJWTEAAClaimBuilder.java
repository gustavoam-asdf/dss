package eu.europa.esig.dss.eaa.jwt.creation;

import eu.europa.esig.dss.eaa.common.key.PublicKeyInfoFactory;

import java.util.List;

/**
 * Builds claims for the SD-JWT VC EAA
 *
 */
public interface SDJWTEAAClaimBuilder {

    /**
     * Sets factory for building a representation of a device public key
     *
     * @param publicKeyInfoFactory {@link PublicKeyInfoFactory}
     */
    void setPublicKeyInfoFactory(PublicKeyInfoFactory publicKeyInfoFactory);

    /**
     * Creates claims for the given payload parameters
     *
     * @param payloadParameters {@link SDJWTEAAPayloadParameters}
     * @return a list of {@link SDJWTEAAClaim}s
     */
    List<SDJWTEAAClaim> buildClaims(SDJWTEAAPayloadParameters payloadParameters);

}
