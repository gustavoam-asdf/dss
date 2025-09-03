package eu.europa.esig.dss.spi.security;

import eu.europa.esig.dss.model.x509.CertificateToken;
import eu.europa.esig.dss.utils.Utils;
import org.bouncycastle.cms.SignerInformationVerifier;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.operator.OperatorCreationException;

import java.security.Provider;
import java.security.PublicKey;

/**
 * This factory is used to create a {@code org.bouncycastle.cms.SignerInformationVerifier} from various input types,
 * such as a CertificateToken or a PublicKey.
 *
 * @param <I> input type
 */
public abstract class DSSSignerInformationVerifierSecurityFactory<I> extends DSSSecurityFactory<I, SignerInformationVerifier> {

    /**
     * Default constructor
     */
    private DSSSignerInformationVerifierSecurityFactory() {
        // empty
    }

    /**
     * This class builds a SignerInformationVerifier for the provided CertificateToken
     */
    public static final DSSSignerInformationVerifierSecurityFactory<CertificateToken> CERTIFICATE_TOKEN_INSTANCE = new DSSSignerInformationVerifierSecurityFactory<CertificateToken>(){

        @Override
        protected String toString(CertificateToken input) {
            return input != null ? Utils.toBase64(input.getEncoded()) : null;
        }

        @Override
        protected SignerInformationVerifier buildWithProvider(CertificateToken input, Provider securityProvider) throws OperatorCreationException {
            JcaSimpleSignerInfoVerifierBuilder jcaSimpleSignerInfoVerifierBuilder = new JcaSimpleSignerInfoVerifierBuilder();
            jcaSimpleSignerInfoVerifierBuilder.setProvider(securityProvider);
            return jcaSimpleSignerInfoVerifierBuilder.build(input.getCertificate());
        }

    };

    /**
     * This class builds a SignerInformationVerifier for the provided PublicKey
     */
    public static final DSSSignerInformationVerifierSecurityFactory<PublicKey> PUBLIC_TOKEN_INSTANCE = new DSSSignerInformationVerifierSecurityFactory<PublicKey>(){

        @Override
        protected String toString(PublicKey input) {
            return String.format("PublicKey with algorithm '%s'",  input != null ? input.getAlgorithm() : null);
        }

        @Override
        protected SignerInformationVerifier buildWithProvider(PublicKey input, Provider securityProvider) throws OperatorCreationException {
            JcaSimpleSignerInfoVerifierBuilder jcaSimpleSignerInfoVerifierBuilder = new JcaSimpleSignerInfoVerifierBuilder();
            jcaSimpleSignerInfoVerifierBuilder.setProvider(securityProvider);
            return jcaSimpleSignerInfoVerifierBuilder.build(input);
        }

    };

    @Override
    protected String getFactoryClassName() {
        return SignerInformationVerifier.class.getSimpleName();
    }

}
