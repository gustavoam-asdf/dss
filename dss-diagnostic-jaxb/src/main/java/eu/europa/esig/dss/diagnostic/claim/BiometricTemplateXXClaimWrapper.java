package eu.europa.esig.dss.diagnostic.claim;

import eu.europa.esig.dss.diagnostic.jaxb.XmlBiometricTemplateXXClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlClaim;

/**
 * Wraps an {@code eu.europa.esig.dss.diagnostic.jaxb.XmlBiometricTemplateXXClaim}
 *
 */
public class BiometricTemplateXXClaimWrapper extends ClaimWrapper {

    /**
     * Default constructor
     *
     * @param wrapped {@link XmlClaim}
     */
    public BiometricTemplateXXClaimWrapper(final XmlBiometricTemplateXXClaim wrapped) {
        super(wrapped);
    }

    /**
     * Constructor with a parent claim provided
     *
     * @param wrapped {@link XmlClaim}
     * @param parent {@link ClaimWrapper}
     */
    public BiometricTemplateXXClaimWrapper(final XmlBiometricTemplateXXClaim wrapped, final ClaimWrapper parent) {
        super(wrapped, parent);
    }

    /**
     * Gets the type of the corresponding biometric template information as defined in the claim
     *
     * @return {@link String}
     */
    public String getType() {
        return getWrapped().getType();
    }

    @Override
    public XmlBiometricTemplateXXClaim getWrapped() {
        return (XmlBiometricTemplateXXClaim) super.getWrapped();
    }

}
