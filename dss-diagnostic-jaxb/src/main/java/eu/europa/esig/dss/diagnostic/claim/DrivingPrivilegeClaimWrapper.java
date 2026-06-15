package eu.europa.esig.dss.diagnostic.claim;

import eu.europa.esig.dss.diagnostic.jaxb.XmlClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDrivingPrivilegeClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDrivingPrivilegeCodesClaim;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides user-friendly access to the information present within a driving privilege claim
 *
 */
public class DrivingPrivilegeClaimWrapper extends ClaimWrapper {

    /**
     * Default constructor
     *
     * @param wrapped {@link XmlDrivingPrivilegeClaim}
     */
    public DrivingPrivilegeClaimWrapper(final XmlDrivingPrivilegeClaim wrapped) {
        super(wrapped);
    }

    /**
     * Constructor with a parent provided
     *
     * @param wrapped {@link XmlDrivingPrivilegeClaim}
     * @param parent {@link ClaimWrapper}
     */
    public DrivingPrivilegeClaimWrapper(final XmlDrivingPrivilegeClaim wrapped, final ClaimWrapper parent) {
        super(wrapped, parent);
    }

    /**
     * Gets the vehicle category code
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getVehicleCategoryCode() {
        XmlClaim vehicleCategoryCode = getWrapped().getVehicleCategoryCode();
        if (vehicleCategoryCode != null) {
            return new ClaimWrapper(vehicleCategoryCode, this);
        }
        return null;
    }

    /**
     * Gets the issuance date of the driving privilege
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getIssueDate() {
        XmlClaim issueDate = getWrapped().getIssueDate();
        if (issueDate != null) {
            return new ClaimWrapper(issueDate, this);
        }
        return null;
    }

    /**
     * Gets the expiration date of the driving privilege
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getExpiryDate() {
        XmlClaim expiryDate = getWrapped().getExpiryDate();
        if (expiryDate != null) {
            return new ClaimWrapper(expiryDate, this);
        }
        return null;
    }

    /**
     * Gets the vehicle category code
     *
     * @return {@link ClaimWrapper}
     */
    public DrivingPrivilegeCodesClaimWrapper getCodes() {
        XmlDrivingPrivilegeCodesClaim xmlDrivingPrivilegeCodesClaim = getWrapped().getCodes();
        if (xmlDrivingPrivilegeCodesClaim != null) {
            return new DrivingPrivilegeCodesClaimWrapper(xmlDrivingPrivilegeCodesClaim, this);
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
        ClaimWrapper vehicleCategoryCode = getVehicleCategoryCode();
        if (vehicleCategoryCode != null) {
            result.put(vehicleCategoryCode.getName(), vehicleCategoryCode);
        }
        ClaimWrapper issueDate = getIssueDate();
        if (issueDate != null) {
            result.put(issueDate.getName(), issueDate);
        }
        ClaimWrapper expiryDate = getExpiryDate();
        if (expiryDate != null) {
            result.put(expiryDate.getName(), expiryDate);
        }
        ClaimWrapper codes = getCodes();
        if (codes != null) {
            result.put(codes.getName(), codes);
        }
        return result;
    }

    @Override
    public XmlDrivingPrivilegeClaim getWrapped() {
        return (XmlDrivingPrivilegeClaim) super.getWrapped();
    }

}
