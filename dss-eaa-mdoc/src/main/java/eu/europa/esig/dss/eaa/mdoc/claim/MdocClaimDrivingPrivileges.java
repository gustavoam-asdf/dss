package eu.europa.esig.dss.eaa.mdoc.claim;

import eu.europa.esig.dss.model.eaa.claim.Claim;
import eu.europa.esig.dss.model.eaa.claim.ClaimArray;
import eu.europa.esig.dss.model.eaa.claim.ClaimDrivingPrivilege;
import eu.europa.esig.dss.model.eaa.claim.ClaimDrivingPrivileges;
import eu.europa.esig.dss.model.eaa.claim.ClaimMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an mdoc implementation of driving privileges, as defined in
 * "7.2.4 Categories of vehicles/restrictions/conditions" of ISO/IEC 18013-5.
 *
 */
public class MdocClaimDrivingPrivileges extends MdocClaimArray implements ClaimDrivingPrivileges {

    private static final long serialVersionUID = -8130304027679306126L;

    /**
     * Constructor to initialize MdocDrivingPrivileges from a ClaimArray
     *
     * @param value {@link ClaimArray}
     */
    public MdocClaimDrivingPrivileges(ClaimArray value) {
        super(value.getName(), value.getNamespace(), value.getListValue(), value.isSelectivelyDisclosable(), value.getParent());
    }

    @Override
    public List<ClaimDrivingPrivilege> getDrivingPrivileges() {
        final List<ClaimDrivingPrivilege> result = new ArrayList<>();
        for (Claim claim : getListValue()) {
            if (claim.isMapValueType()) {
                result.add(new MdocClaimDrivingPrivilege((ClaimMap) claim));
            }
        }
        return result;
    }

}
