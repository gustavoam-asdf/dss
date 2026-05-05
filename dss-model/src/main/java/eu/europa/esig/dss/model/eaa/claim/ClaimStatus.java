package eu.europa.esig.dss.model.eaa.claim;

/**
 * Represents an EAA Status claim
 *
 */
public interface ClaimStatus extends Claim {

    /* Token Status List (TSL) draft-ietf-oauth-status-list-20 */

    /**
     * Gets the embedded status_list claim value
     *
     * @return {@link ClaimStatusList}
     */
    ClaimStatusList getStatusList();

    /* ETSI TS 119 472-1 status definition */

    /**
     * Gets the EAA's Status index value, when present
     *
     * @return {@link ClaimNumber}
     */
    ClaimNumber getIndex();

    /**
     * Gets the EAA's Status URI value, when present
     *
     * @return {@link ClaimString}
     */
    ClaimString getUri();

    /**
     * Gets the EAA's Status type value, when present
     *
     * @return {@link ClaimString}
     */
    ClaimString getType();

    /**
     * Gets the EAA's Status purpose value, when present
     *
     * @return {@link ClaimString}
     */
    ClaimString getPurpose();

}
