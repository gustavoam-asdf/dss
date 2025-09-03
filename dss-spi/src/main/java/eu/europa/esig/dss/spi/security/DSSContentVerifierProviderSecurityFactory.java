package eu.europa.esig.dss.spi.security;

import org.bouncycastle.operator.ContentVerifierProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentVerifierProviderBuilder;

import java.security.Provider;
import java.security.PublicKey;

/**
 * This factory is used to create a {@code org.bouncycastle.operator.ContentVerifierProvider} instance based on the PublicKey algorithm's name.
 *
 */
public class DSSContentVerifierProviderSecurityFactory extends DSSSecurityFactory<PublicKey, ContentVerifierProvider> {

    /**
     * Instance of the factory to initialize a ContentVerifierProvider object
     */
    public static final DSSContentVerifierProviderSecurityFactory INSTANCE = new DSSContentVerifierProviderSecurityFactory();

    /**
     * Default constructor
     */
    private DSSContentVerifierProviderSecurityFactory() {
        // empty
    }

    @Override
    protected String getFactoryClassName() {
        return ContentVerifierProvider.class.getSimpleName();
    }

    @Override
    protected String toString(PublicKey input) {
        return String.format("PublicKey with algorithm '%s'",  input != null ? input.getAlgorithm() : null);
    }

    @Override
    protected ContentVerifierProvider buildWithProvider(PublicKey input, Provider securityProvider) throws OperatorCreationException {
        JcaContentVerifierProviderBuilder jcaContentVerifierProviderBuilder = new JcaContentVerifierProviderBuilder();
        jcaContentVerifierProviderBuilder.setProvider(securityProvider);
        return jcaContentVerifierProviderBuilder.build(input);
    }

}
