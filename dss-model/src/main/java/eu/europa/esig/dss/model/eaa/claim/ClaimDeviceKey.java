package eu.europa.esig.dss.model.eaa.claim;

import java.security.PublicKey;

/**
 * Represents a device key used for creating a key-binding signature.
 *
 */
public interface ClaimDeviceKey extends Claim {

    /**
     * Gets the public key
     *
     * @return {@link PublicKey}
     */
    PublicKey getPublicKey();

}
