package eu.europa.esig.dss.diagnostic.claim;

import eu.europa.esig.dss.diagnostic.jaxb.XmlClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlStatusClaim;

/**
 * Wraps an {@code eu.europa.esig.dss.diagnostic.jaxb.XmlStatusClaim}
 *
 */
public class StatusClaimWrapper extends ClaimWrapper {

    /**
     * Default constructor
     *
     * @param wrapped {@link XmlStatusClaim}
     */
    public StatusClaimWrapper(final XmlStatusClaim wrapped) {
        super(wrapped);
    }

    /**
     * Gets the status's unique index identifier
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getIndex() {
        XmlClaim index = getWrapped().getIndex();
        if (index != null) {
            return new ClaimWrapper(index, this);
        }
        return null;
    }

    /**
     * Gets the status's uri
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getUri() {
        XmlClaim index = getWrapped().getUri();
        if (index != null) {
            return new ClaimWrapper(index, this);
        }
        return null;
    }

    @Override
    public XmlStatusClaim getWrapped() {
        return (XmlStatusClaim) super.getWrapped();
    }

}
