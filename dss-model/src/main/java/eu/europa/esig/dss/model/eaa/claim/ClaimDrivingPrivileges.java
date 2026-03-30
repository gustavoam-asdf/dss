package eu.europa.esig.dss.model.eaa.claim;

import java.util.List;

/**
 * Represents a "driving_privileges" claim as defined in
 * "7.2.4 Categories of vehicles/restrictions/conditions" of ISO/IEC 18013-5.
 *
 */
public interface ClaimDrivingPrivileges extends Claim {

    /**
     * Gets a list of driving privilege claims
     *
     * @return a list of {@link ClaimDrivingPrivilege}s
     */
    List<ClaimDrivingPrivilege> getDrivingPrivileges();

}
