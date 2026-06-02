package eu.europa.esig.dss.model.eaa.claim;

/**
 * Represents an ISO/IEC 23220-2:2026 "6.3.1.3 Date of birth structure" data element
 *
 */
public interface ClaimBirthDate extends Claim {

    /**
     * Gets day, month and year on which the holder was born. Unknown parts (i.e., year, month, day) are masked with 1.
     *
     * @return {@link ClaimDate}
     */
    ClaimDate getBirthDate();

    /**
     * Gets an 8 digit flag to denote the location of the mask in YYYYMMDD format. 1 denotes mask.
     *
     * @return {@link ClaimString}
     */
    ClaimString getApproximateMask();

}
