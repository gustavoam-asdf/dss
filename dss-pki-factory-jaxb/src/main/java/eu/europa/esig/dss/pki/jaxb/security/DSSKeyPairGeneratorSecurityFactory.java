package eu.europa.esig.dss.pki.jaxb.security;

import eu.europa.esig.dss.spi.security.DSSSecurityFactory;

import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.KeyPairGenerator;

/**
 * This factory is used to create a {@code java.security.KeyPairGenerator} instance based on the algorithm's name.
 *
 */
public class DSSKeyPairGeneratorSecurityFactory extends DSSSecurityFactory<String, KeyPairGenerator> {

    /**
     * Instance of the factory to initialize a KeyPairGenerator object
     */
    public static final DSSKeyPairGeneratorSecurityFactory INSTANCE = new DSSKeyPairGeneratorSecurityFactory();

    /**
     * Default constructor
     */
    private DSSKeyPairGeneratorSecurityFactory() {
        // empty
    }

    @Override
    protected String getFactoryClassName() {
        return KeyPairGenerator.class.getSimpleName();
    }

    @Override
    protected String toString(String input) {
        return input;
    }

    @Override
    protected KeyPairGenerator buildWithProvider(String input, Provider securityProvider) throws NoSuchAlgorithmException {
        return KeyPairGenerator.getInstance(input, securityProvider);
    }

}
