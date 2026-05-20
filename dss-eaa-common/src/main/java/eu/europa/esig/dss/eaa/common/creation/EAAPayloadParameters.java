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

}
