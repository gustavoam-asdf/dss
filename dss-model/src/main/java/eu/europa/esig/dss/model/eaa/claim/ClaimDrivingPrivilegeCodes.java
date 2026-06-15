package eu.europa.esig.dss.model.eaa.claim;

import java.util.List;

/**
 * Represents an array of Code's of the DrivingPrivilege element, as defined in
 * "7.2.4 Categories of vehicles/restrictions/conditions" of ISO/IEC 18013-5.
 *
 */
public interface ClaimDrivingPrivilegeCodes extends Claim {

    /**
     * Gets a list of codes
     *
     * @return a list of {@link ClaimDrivingPrivilegeCode}s
     */
    List<ClaimDrivingPrivilegeCode> getCodes();

}
