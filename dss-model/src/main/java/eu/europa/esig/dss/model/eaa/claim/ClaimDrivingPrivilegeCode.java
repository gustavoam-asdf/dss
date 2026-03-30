package eu.europa.esig.dss.model.eaa.claim;

/**
 * Represents a single Code element entry of the "codes" array as defined in
 * the "7.2.4 Categories of vehicles/restrictions/conditions" of ISO/IEC 18013-5.
 *
 */
public interface ClaimDrivingPrivilegeCode extends Claim {

    /**
     * Gets a code as per ISO/IEC 18013-2 Annex A
     *
     * @return {@link ClaimString}
     */
    ClaimString getCode();

    /**
     * Gets a sign as per ISO/IEC 18013-2 Annex A
     *
     * @return {@link ClaimString}
     */
    ClaimString getSign();

    /**
     * Gets a value as per ISO/IEC 18013-2 Annex A
     *
     * @return {@link ClaimString}
     */
    ClaimString getValue();

}
