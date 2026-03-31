package eu.europa.esig.dss.diagnostic.claim;

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
        StatusListClaimWrapper statusList = getStatusList();
        if (statusList != null) {
            return statusList.getIndex();
        }
        return null;
    }

    /**
     * Gets the status's uri
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getUri() {
        StatusListClaimWrapper statusList = getStatusList();
        if (statusList != null) {
            return statusList.getUri();
        }
        return null;
    }

    /**
     * Gets the status list
     *
     * @return {@link StatusListClaimWrapper}
     */
    public StatusListClaimWrapper getStatusList() {
        if (getWrapped().getStatusList() != null) {
            return new StatusListClaimWrapper(getWrapped().getStatusList());
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
        StatusListClaimWrapper statusList = getStatusList();
        if (statusList != null) {
            result.put(statusList.getName(), statusList);
        }
        return result;
    }

    @Override
    public XmlStatusClaim getWrapped() {
        return (XmlStatusClaim) super.getWrapped();
    }

}
