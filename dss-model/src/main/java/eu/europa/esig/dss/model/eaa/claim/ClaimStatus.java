package eu.europa.esig.dss.model.eaa.claim;

/**
 * Represents an EAA Status claim
 *
 */
public interface ClaimStatus extends Claim {

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

}
