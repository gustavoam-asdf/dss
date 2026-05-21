package eu.europa.esig.dss.eaa.common.creation;

import eu.europa.esig.dss.eaa.common.creation.claim.EAAClaim;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.spi.random.DSSFixedSecureRandomProvider;
import eu.europa.esig.dss.spi.random.SecureRandomProvider;

import java.security.SecureRandom;

/**
 *
 */
public abstract class AbstractEAAPayloadBuilder<P extends EAAPayloadParameters, C extends EAAClaim, D extends EAADisclosure> implements EAAPayloadBuilder<P, C, D> {

    private SecureRandomProvider secureRandomProvider = new DSSFixedSecureRandomProvider(DigestAlgorithm.SHA256);

    public void setSecureRandomProvider(SecureRandomProvider secureRandomProvider) {
        this.secureRandomProvider = secureRandomProvider;
    }

    /**
     * Creates a new SecureRandom using the {@code payloadParameters} for the initial seed computation.
     * NOTE: this method is intended to provide a deterministic behavior.
     *
     * @param payloadParameters {@link EAAPayloadParameters}
     * @return {@link SecureRandom}
     */
    protected SecureRandom secureRandom(EAAPayloadParameters payloadParameters) {
        return secureRandomProvider.getSecureRandom(payloadParameters.toString().getBytes());
    }

    /**
     * This method generates the next random salt using the {@code secureRandom}
     * By default, the method generates a 128-bit length salt.
     *
     * @return byte array containing the salt
     */
    protected byte[] nextRandomSalt(SecureRandom secureRandom) {
        return secureRandom.generateSeed(16); // 16 * 8 = 128 bits
    }

}
