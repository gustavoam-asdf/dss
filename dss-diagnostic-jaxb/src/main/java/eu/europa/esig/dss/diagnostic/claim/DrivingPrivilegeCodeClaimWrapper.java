package eu.europa.esig.dss.diagnostic.claim;

import eu.europa.esig.dss.diagnostic.jaxb.XmlClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDrivingPrivilegeCodeClaim;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a code information of a driving privilege
 *
 */
public class DrivingPrivilegeCodeClaimWrapper extends ClaimWrapper {

    /**
     * Default constructor
     *
     * @param wrapped {@link XmlDrivingPrivilegeCodeClaim}
     */
    public DrivingPrivilegeCodeClaimWrapper(final XmlDrivingPrivilegeCodeClaim wrapped) {
        super(wrapped);
    }

    /**
     * Constructor with a parent provided
     *
     * @param wrapped {@link XmlDrivingPrivilegeCodeClaim}
     * @param parent {@link ClaimWrapper}
     */
    public DrivingPrivilegeCodeClaimWrapper(final XmlDrivingPrivilegeCodeClaim wrapped, final ClaimWrapper parent) {
        super(wrapped, parent);
    }

    /**
     * Gets the code
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getCode() {
        XmlClaim code = getWrapped().getCode();
        if (code != null) {
            return new ClaimWrapper(code, this);
        }
        return null;
    }

    /**
     * Gets the sign
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getSign() {
        XmlClaim sign = getWrapped().getSign();
        if (sign != null) {
            return new ClaimWrapper(sign, this);
        }
        return null;
    }

    /**
     * Gets the value
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getValue() {
        XmlClaim value = getWrapped().getValue();
        if (value != null) {
            return new ClaimWrapper(value, this);
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
        ClaimWrapper code = getCode();
        if (code != null) {
            result.put(code.getName(), code);
        }
        ClaimWrapper sign = getSign();
        if (sign != null) {
            result.put(sign.getName(), sign);
        }
        ClaimWrapper value = getValue();
        if (value != null) {
            result.put(value.getName(), value);
        }
        return result;
    }

    @Override
    public XmlDrivingPrivilegeCodeClaim getWrapped() {
        return (XmlDrivingPrivilegeCodeClaim) super.getWrapped();
    }

}
