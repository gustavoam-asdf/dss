package eu.europa.esig.dss.diagnostic.claim;

import eu.europa.esig.dss.diagnostic.jaxb.XmlAgeOverNNClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlClaim;

/**
 * Wraps an {@code eu.europa.esig.dss.diagnostic.jaxb.XmlAgeOverNNClaim}
 *
 */
public class AgeOverNNClaimWrapper extends ClaimWrapper {

    /**
     * Default constructor
     *
     * @param wrapped {@link XmlClaim}
     */
    public AgeOverNNClaimWrapper(final XmlAgeOverNNClaim wrapped) {
        super(wrapped);
    }

    /**
     * Constructor with a parent claim provided
     *
     * @param wrapped {@link XmlClaim}
     * @param parent {@link ClaimWrapper}
     */
    public AgeOverNNClaimWrapper(final XmlAgeOverNNClaim wrapped, final ClaimWrapper parent) {
        super(wrapped, parent);
    }

    /**
     * Gets the age value used for a verification within the claim
     *
     * @return {@link Integer}
     */
    public Integer getAge() {
        return getWrapped().getAge();
    }

    @Override
    public XmlAgeOverNNClaim getWrapped() {
        return (XmlAgeOverNNClaim) super.getWrapped();
    }
    
}
