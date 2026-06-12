package eu.europa.esig.dss.model.eaa.claim;

/**
 * Represents an EAA Status List claim
 *
 */
public interface ClaimStatusList extends ClaimRevocationList {

    /**
     * Gets the EAA's Status index value, when present
     *
     * @return {@link ClaimNumber}
     */
    ClaimNumber getIndex();

}
