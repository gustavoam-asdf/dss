package eu.europa.esig.dss.model.eaa.claim;

/**
 * Represents an "address" claim.
 *
 */
public interface ClaimAddress extends Claim {

    /**
     * Gets the user's full postal or mailing address, formatted, when present
     *
     * @return {@link ClaimString}
     */
    ClaimString getPostalAddress();

    /**
     * Gets the user's street address, when present.
     * The component may include a house number, street name, Post Office Box, and multi-line
     * extended street address information.
     *
     * @return {@link ClaimString}
     */
    ClaimString getStreetAddress();

    /**
     * Gets the user's city or locality address, when present.
     *
     * @return {@link ClaimString}
     */
    ClaimString getCity();

    /**
     * Gets the user's state or region address, when present.
     *
     * @return {@link ClaimString}
     */
    ClaimString getStateOrProvince();

    /**
     * Gets the user's zip code or postal code address, when present.
     *
     * @return {@link ClaimString}
     */
    ClaimString getPostalCode();

    /**
     * Gets the user's country address, when present.
     *
     * @return {@link ClaimString}
     */
    ClaimString getCountry();

    /* ARF PID Rulebook claims */

    /**
     * Gets The house number where the user to whom the person identification data relates currently resides,
     * including any affix or suffix, when present.
     *
     * @return {@link ClaimString}
     */
    ClaimString getHouseNumber();

}
