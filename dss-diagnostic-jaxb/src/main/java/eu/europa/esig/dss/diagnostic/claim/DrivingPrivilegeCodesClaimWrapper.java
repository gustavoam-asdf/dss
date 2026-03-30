package eu.europa.esig.dss.diagnostic.claim;

import eu.europa.esig.dss.diagnostic.jaxb.XmlDrivingPrivilegeCodeClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDrivingPrivilegeCodesClaim;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents an array of codes information for the corresponding driving privilege
 *
 */
public class DrivingPrivilegeCodesClaimWrapper extends ClaimWrapper {

    /**
     * Default constructor
     *
     * @param wrapped {@link eu.europa.esig.dss.diagnostic.jaxb.XmlDrivingPrivilegeCodesClaim}
     */
    public DrivingPrivilegeCodesClaimWrapper(final XmlDrivingPrivilegeCodesClaim wrapped) {
        super(wrapped);
    }

    /**
     * Constructor with a parent provided
     *
     * @param wrapped {@link XmlDrivingPrivilegeCodesClaim}
     * @param parent {@link ClaimWrapper}
     */
    public DrivingPrivilegeCodesClaimWrapper(final XmlDrivingPrivilegeCodesClaim wrapped, final ClaimWrapper parent) {
        super(wrapped, parent);
    }

    /**
     * Gets a list of codes information for the given driving privilege
     *
     * @return a lis of {@link DrivingPrivilegeCodeClaimWrapper}s
     */
    public List<DrivingPrivilegeCodeClaimWrapper> getCodes() {
        List<XmlDrivingPrivilegeCodeClaim> xmlDrivingPrivilegeCodeClaims = getWrapped().getCode();
        if (xmlDrivingPrivilegeCodeClaims != null && !xmlDrivingPrivilegeCodeClaims.isEmpty()) {
            return xmlDrivingPrivilegeCodeClaims.stream().filter(Objects::nonNull)
                    .map(x -> new DrivingPrivilegeCodeClaimWrapper(x, this)).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public boolean isList() {
        return true;
    }

    @Override
    public List<ClaimWrapper> getList() {
        List<DrivingPrivilegeCodeClaimWrapper> codes = getCodes();
        if (codes != null && !codes.isEmpty()) {
            return codes.stream().map(c -> (ClaimWrapper) c).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public XmlDrivingPrivilegeCodesClaim getWrapped() {
        return (XmlDrivingPrivilegeCodesClaim) super.getWrapped();
    }

}
