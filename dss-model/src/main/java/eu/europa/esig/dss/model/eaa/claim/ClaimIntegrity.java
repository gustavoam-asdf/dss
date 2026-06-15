package eu.europa.esig.dss.model.eaa.claim;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;

/**
 * This claims represents a claim integrity definition, when applicable.
 * This definition is based on {@link <a href="https://www.w3.org/TR/2016/REC-SRI-20160623/">W3C Subresource Integrity</a>}
 *
 */
public interface ClaimIntegrity extends Claim {

    /**
     * Gets the Digest Algorithm used to compute claim integrity digest, when present
     *
     * @return {@link DigestAlgorithm}
     */
    DigestAlgorithm getDigestAlgorithm();

    /**
     * Gets the claim integrity digest value, when present.
     * NOTE: the digest computation depends on a claim semantics.
     *
     * @return digest value
     */
    byte[] getDigestValue();

}
