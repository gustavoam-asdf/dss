package eu.europa.esig.dss.diagnostic.claim;

import eu.europa.esig.dss.diagnostic.jaxb.XmlClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlStatusListClaim;

import java.util.HashMap;
import java.util.Map;

/**
 * Wraps an {@code eu.europa.esig.dss.diagnostic.jaxb.XmlStatusListClaim}
 *
 */
public class StatusListClaimWrapper extends ClaimWrapper {

    /**
     * Default constructor
     *
     * @param wrapped {@link XmlStatusListClaim}
     */
    public StatusListClaimWrapper(final XmlStatusListClaim wrapped) {
        super(wrapped);
    }

    /**
     * Constructor with a parent provided
     *
     * @param wrapped {@link XmlStatusListClaim}
     * @param parent {@link ClaimWrapper}
     */
    public StatusListClaimWrapper(final XmlStatusListClaim wrapped, final ClaimWrapper parent) {
        super(wrapped, parent);
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
        XmlClaim uri = getWrapped().getUri();
        if (uri != null) {
            return new ClaimWrapper(uri, this);
        }
        return null;
    }

    @Override
    public boolean isMap() {
        return true;
    }

    @Override
    public Map<String, ClaimWrapper> getMap() {
        final Map<String, ClaimWrapper> result = new HashMap<>(super.getMap());
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
    public XmlStatusListClaim getWrapped() {
        return (XmlStatusListClaim) super.getWrapped();
    }

}
