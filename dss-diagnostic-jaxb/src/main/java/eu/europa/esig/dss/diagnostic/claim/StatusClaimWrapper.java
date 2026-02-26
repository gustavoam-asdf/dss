package eu.europa.esig.dss.diagnostic.claim;

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
        return new ClaimWrapper(getWrapped().getIndex());
    }

    /**
     * Gets the status's uri
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getUri() {
        return new ClaimWrapper(getWrapped().getUri());
    }

    @Override
    protected XmlStatusClaim getWrapped() {
        return (XmlStatusClaim) super.getWrapped();
    }

}
