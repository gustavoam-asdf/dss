package eu.europa.esig.dss.model.eaa.claim;

/**
 * Represents an EAA Status claim
 *
 */
public interface ClaimStatus extends Claim {

    /**
     * Gets the embedded status_list claim value
     *
     * @return {@link ClaimStatusList}
     */
    ClaimStatusList getStatusList();

}
