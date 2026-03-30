package eu.europa.esig.dss.model.eaa.claim;

/**
 * Represents a single item of the "driving_privileges" claim array, as defined in
 * "7.2.4 Categories of vehicles/restrictions/conditions" of ISO/IEC 18013-5.
 *
 */
public interface ClaimDrivingPrivilege extends Claim {

    /**
     * Gets a vehicle category code as per ISO/IEC 18013-1 Annex B
     *
     * @return {@link ClaimString}
     */
    ClaimString getVehicleCategoryCode();

    /**
     * Gets a date of issue encoded as full-date
     *
     * @return {@link ClaimDate}
     */
    ClaimDate getIssueDate();

    /**
     * Gets a date of expiry encoded as full-date
     *
     * @return {@link ClaimDate}
     */
    ClaimDate getExpiryDate();

    /**
     * Gets an array of code info
     *
     * @return {@link ClaimArray}
     */
    ClaimDrivingPrivilegeCodes getCodes();

}
