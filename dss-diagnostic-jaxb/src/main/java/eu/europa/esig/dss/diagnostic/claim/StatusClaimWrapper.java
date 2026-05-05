package eu.europa.esig.dss.diagnostic.claim;

import eu.europa.esig.dss.diagnostic.jaxb.XmlClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlStatusClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlStatusListClaim;

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
     * Gets the status list
     *
     * @return {@link ClaimWrapper}
     */
    public StatusListClaimWrapper getStatusList() {
        XmlStatusListClaim statusList = getWrapped().getStatusList();
        if (statusList != null) {
            return new StatusListClaimWrapper(statusList, this);
        }
        return null;
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

    /**
     * Gets the status's type
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getType() {
        XmlClaim type = getWrapped().getType();
        if (type != null) {
            return new ClaimWrapper(type, this);
        }
        return null;
    }

    /**
     * Gets the status's purpose
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getPurpose() {
        XmlClaim purpose = getWrapped().getPurpose();
        if (purpose != null) {
            return new ClaimWrapper(purpose, this);
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
        ClaimWrapper index = getIndex();
        if (index != null) {
            result.put(index.getName(), index);
        }
        ClaimWrapper uri = getUri();
        if (uri != null) {
            result.put(uri.getName(), uri);
        }
        ClaimWrapper type = getType();
        if (type != null) {
            result.put(type.getName(), type);
        }
        ClaimWrapper purpose = getPurpose();
        if (purpose != null) {
            result.put(purpose.getName(), purpose);
        }
        return result;
    }

    @Override
    public XmlStatusClaim getWrapped() {
        return (XmlStatusClaim) super.getWrapped();
    }

}
