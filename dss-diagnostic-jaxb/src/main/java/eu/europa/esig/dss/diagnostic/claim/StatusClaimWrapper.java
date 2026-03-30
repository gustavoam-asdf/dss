package eu.europa.esig.dss.diagnostic.claim;

import eu.europa.esig.dss.diagnostic.jaxb.XmlClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlStatusClaim;

import java.util.HashMap;
import java.util.Map;

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
    public boolean isMap() {
        return true;
    }

    @Override
    public Map<String, ClaimWrapper> getMap() {
        final Map<String, ClaimWrapper> result = new HashMap<>();
        ClaimWrapper index = getIndex();
        if (index != null) {
            result.put(index.getName(), index);
        }
        ClaimWrapper uri = getUri();
        if (uri != null) {
            result.put(uri.getName(), uri);
        }
        return result;
    }

    @Override
    public XmlStatusClaim getWrapped() {
        return (XmlStatusClaim) super.getWrapped();
    }

}
