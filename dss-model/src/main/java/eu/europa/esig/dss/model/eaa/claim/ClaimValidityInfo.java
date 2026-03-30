package eu.europa.esig.dss.model.eaa.claim;

/**
 * Represents a structure containing information related to the validity of the MSO and its signature.
 * The structure corresponds to the definition of "ValidityInfo" per
 * "9.1.2.4 Signing method and structure for MSO" of ISO/IEC 18013-5.
 *
 */
public interface ClaimValidityInfo extends Claim {

    /**
     * Gets the timestamp at which the MSO signature was created.
     *
     * @return {@link ClaimDate}
     */
    ClaimDate getSigned();

    /**
     * Gets the timestamp before which the MSO is not yet valid.
     *
     * @return {@link ClaimDate}
     */
    ClaimDate getValidFrom();

    /**
     * Gets the timestamp after which the MSO is no longer valid.
     *
     * @return {@link ClaimDate}
     */
    ClaimDate getValidUntil();

    /**
     * Gets the timestamp at which the issuing authority infrastructure expects to re-sign the MSO.
     *
     * @return {@link ClaimDate}
     */
    ClaimDate getExpectedUpdate();

}
