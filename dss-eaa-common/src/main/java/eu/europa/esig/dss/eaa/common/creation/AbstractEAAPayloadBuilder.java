package eu.europa.esig.dss.eaa.common.creation;

import eu.europa.esig.dss.eaa.common.creation.claim.EAAClaim;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.spi.random.DSSFixedSecureRandomProvider;
import eu.europa.esig.dss.spi.random.SecureRandomProvider;

/**
 *
 */
public abstract class AbstractEAAPayloadBuilder<P extends EAAPayloadParameters, C extends EAAClaim, D extends EAADisclosure> implements EAAPayloadBuilder<P, C, D> {

    protected SecureRandomProvider secureRandomProvider = new DSSFixedSecureRandomProvider(DigestAlgorithm.SHA1);

    public void setSecureRandomProvider(SecureRandomProvider secureRandomProvider) {
        this.secureRandomProvider = secureRandomProvider;
    }

}
