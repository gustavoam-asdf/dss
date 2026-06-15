package eu.europa.esig.dss.diagnostic.claim;

import eu.europa.esig.dss.diagnostic.jaxb.XmlDrivingPrivilegeClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDrivingPrivilegesClaim;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Provides user-friendly access to the information present within driving privileges claim
 *
 */
public class DrivingPrivilegesClaimWrapper extends ClaimWrapper {

    /**
     * Default constructor
     *
     * @param wrapped {@link XmlDrivingPrivilegesClaim}
     */
    public DrivingPrivilegesClaimWrapper(final XmlDrivingPrivilegesClaim wrapped) {
        super(wrapped);
    }

    /**
     * Constructor with a parent provided
     *
     * @param wrapped {@link XmlDrivingPrivilegesClaim}
     * @param parent {@link ClaimWrapper}
     */
    public DrivingPrivilegesClaimWrapper(final XmlDrivingPrivilegesClaim wrapped, final ClaimWrapper parent) {
        super(wrapped, parent);
    }

    /**
     * Gets a list of all driving privileges defined within the claim
     *
     * @return a list oif {@link DrivingPrivilegeClaimWrapper}s
     */
    public List<DrivingPrivilegeClaimWrapper> getDrivingPrivileges() {
        List<XmlDrivingPrivilegeClaim> xmlDrivingPrivileges = getWrapped().getDrivingPrivilege();
        if (xmlDrivingPrivileges != null && !xmlDrivingPrivileges.isEmpty()) {
            return xmlDrivingPrivileges.stream().filter(Objects::nonNull)
                    .map(x -> new DrivingPrivilegeClaimWrapper(x, this)).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public boolean isList() {
        return true;
    }

    @Override
    public List<ClaimWrapper> getList() {
        List<DrivingPrivilegeClaimWrapper> drivingPrivileges = getDrivingPrivileges();
        if (drivingPrivileges != null && !drivingPrivileges.isEmpty()) {
            return drivingPrivileges.stream().map(c -> (ClaimWrapper) c).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public XmlDrivingPrivilegesClaim getWrapped() {
        return (XmlDrivingPrivilegesClaim) super.getWrapped();
    }

}
