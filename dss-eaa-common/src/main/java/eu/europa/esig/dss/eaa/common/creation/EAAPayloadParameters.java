package eu.europa.esig.dss.eaa.common.creation;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;

/**
 * Contains configuration of the claims to be incorporated within an EAA Payload
 *
 */
public interface EAAPayloadParameters {

    /**
     * Digest algorithm used to compute hashes for selectively disclosable claims.
     * Default : DigestAlgorithm.SHA256
     *
     * @return {@link DigestAlgorithm}
     */
    DigestAlgorithm getDigestAlgorithm();

    /**
     * Gets the number of decoy digest to generate
     *
     * @return the number of decoy digest to generate
     */
    int getDecoyDigestNumber();

    /**
     * Gets whether the digests of the selectively disclosable claims are to be shuffled
     *
     * @return TRUE if the hashes of the selectively disclosable claims are to be shuffled, FALSE otherwise
     */
    boolean isShuffleHashes();

}
