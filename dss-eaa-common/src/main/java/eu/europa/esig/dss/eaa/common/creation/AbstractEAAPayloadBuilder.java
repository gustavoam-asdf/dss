package eu.europa.esig.dss.eaa.common.creation;

import eu.europa.esig.dss.eaa.common.key.DefaultPublicKeyInfoFactory;
import eu.europa.esig.dss.eaa.common.key.PublicKeyInfoFactory;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.spi.random.DSSSecureRandomProvider;
import eu.europa.esig.dss.spi.random.SecureRandomProvider;

import java.security.SecureRandom;
import java.util.Objects;

/**
 * Abstract implementation of an EAA payload builder.
 *
 * @param <P> implementation of {@link EAAPayloadParameters} for the EAA format
 * @param <D> implementation of {@link EAADisclosure} for the EAA format
 */
public abstract class AbstractEAAPayloadBuilder<P extends EAAPayloadParameters, D extends EAADisclosure> implements EAAPayloadBuilder<P, D> {

    /**
     * Provides a SecureRandom for salt computation
     */
    private SecureRandomProvider secureRandomProvider = new DSSSecureRandomProvider(DigestAlgorithm.SHA256);

    /**
     * The factory is used to build a representation of a public key from a {@code java.security.PublicKey}
     * Default : {@code DefaultPublicKeyInfoFactory}
     */
    private PublicKeyInfoFactory publicKeyInfoFactory = new DefaultPublicKeyInfoFactory();

    /**
     * Default constructor
     */
    protected AbstractEAAPayloadBuilder() {
        // empty
    }

    /**
     * Sets a SecureRandomProvider used for a deterministic SecureRandom production
     *
     * @param secureRandomProvider {@link SecureRandomProvider}
     */
    public void setSecureRandomProvider(SecureRandomProvider secureRandomProvider) {
        Objects.requireNonNull(secureRandomProvider, "SecureRandomProvider cannot be null!");
        this.secureRandomProvider = secureRandomProvider;
    }

    /**
     * Gets the PublicKeyInfoFactory
     *
     * @return {@link PublicKeyInfoFactory}
     */
    protected PublicKeyInfoFactory getPublicKeyInfoFactory() {
        return publicKeyInfoFactory;
    }

    /**
     * (Optional) allows modifying the default behavior for a COSE_Key computation from a {@code java.security.PublicKey}.
     * Default : an instance of {@code PublicKeyInfoFactory} is used, relying on JDK 8 and BouncyCastle utility methods.
     *
     * @param publicKeyInfoFactory {@link PublicKeyInfoFactory}
     */
    public void setPublicKeyInfoFactory(PublicKeyInfoFactory publicKeyInfoFactory) {
        Objects.requireNonNull(publicKeyInfoFactory, "PublicKeyInfoFactory cannot be null!");
        this.publicKeyInfoFactory = publicKeyInfoFactory;
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
