package eu.europa.esig.dss.model.eaa.claim;

import java.util.List;

/**
 * Contains a list of age_over_NN claims
 *
 */
public interface ClaimAgeEqualOrOver extends Claim {

    /**
     * Gets a list of age_over_NN claims
     *
     * @return a list of {@link ClaimAgeOverNN}s
     */
    List<ClaimAgeOverNN> getAgeOverNNClaims();

}
