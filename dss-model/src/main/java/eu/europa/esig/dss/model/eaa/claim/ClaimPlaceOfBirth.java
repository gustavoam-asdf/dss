package eu.europa.esig.dss.model.eaa.claim;

/**
 * Represents a user's place of birth claim.
 *
 */
public interface ClaimPlaceOfBirth extends Claim {

    /**
     * Gets user's country of birth, represented by 2-letter ISO 3116-1 code, when present
     *
     * @return {@link ClaimString}
     */
    ClaimString getCountry();

    /**
     * Gets user's state, province, prefecture, or region of birth, when present
     *
     * @return {@link ClaimString}
     */
    ClaimString getStateOrProvince();

    /**
     * Gets user's city or locality of birth, when present
     *
     * @return {@link ClaimString}
     */
    ClaimString getCity();

}
