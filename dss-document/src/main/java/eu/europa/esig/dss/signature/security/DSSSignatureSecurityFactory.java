package eu.europa.esig.dss.signature.security;

import eu.europa.esig.dss.spi.security.DSSSecurityFactory;

import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Signature;

/**
 * This factory is used to create a {@code java.security.Signature} instance based on the signature algorithm's name.
 *
 */
public class DSSSignatureSecurityFactory extends DSSSecurityFactory<String, Signature> {

    /**
     * Instance of the factory to initialize a Signature object
     */
    public static final DSSSignatureSecurityFactory INSTANCE = new DSSSignatureSecurityFactory();

    /**
     * Default constructor
     */
    private DSSSignatureSecurityFactory() {
        // empty
    }

    @Override
    protected String getFactoryClassName() {
        return Signature.class.getSimpleName();
    }

    @Override
    protected String toString(String input) {
        return input;
    }

    @Override
    protected Signature buildWithProvider(String input, Provider securityProvider) throws NoSuchAlgorithmException {
        return Signature.getInstance(input, securityProvider);
    }

}
