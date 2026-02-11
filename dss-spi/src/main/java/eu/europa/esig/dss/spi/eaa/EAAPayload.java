package eu.europa.esig.dss.spi.eaa;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.model.eaa.SelectivelyDisclosableClaim;

import java.io.Serializable;
import java.util.List;

/**
 * Provides an interface for accessing the content of the EAA payload
 */
public interface EAAPayload extends Serializable {

    /**
     * Gets the EAA category URN, when present
     *
     * @return {@link String}
     */
    String getCategory();

    /**
     * Gets a list of selectively disclosable claims provided within the EAA payload
     *
     * @return a list of {@link SelectivelyDisclosableClaim}s
     */
    List<SelectivelyDisclosableClaim> getSelectiveDisclosableClaims();

    /**
     * Gets a DigestAlgorithm defined within an EAA payload used to create hashes for the selective disclosures
     *
     * @return {@link DigestAlgorithm}
     */
    DigestAlgorithm getSelectiveDisclosableClaimDigestAlgorithm();

}
