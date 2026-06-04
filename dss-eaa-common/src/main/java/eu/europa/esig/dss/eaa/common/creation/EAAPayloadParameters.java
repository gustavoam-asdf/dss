package eu.europa.esig.dss.eaa.common.creation;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;

import java.security.PublicKey;
import java.util.Date;

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
     * Gets the EAA issuance date
     *
     * @return {@link Date}
     */
    Date getIssuanceDate();

    /**
     * Gets the EAA notBefore date
     *
     * @return {@link Date}
     */
    Date getNotBeforeDate();

    /**
     * Gets the EAA expiration date
     *
     * @return {@link Date}
     */
    Date getExpirationDate();

    /**
     * Gets the public part of the key pair used for mdoc authentication.
     *
     * @return {@link PublicKey}
     */
    PublicKey getDeviceKey();

    /**
     * Gets the identifier_list
     *
     * @return {@link EAARevocationList}
     */
    EAARevocationList getIdentifierList();

    /**
     * Gets the status_list
     *
     * @return {@link EAARevocationList}
     */
    EAARevocationList getStatusList();

    /**
     * Gets the EAA category URN
     *
     * @return {@link String}
     */
    String getCategory();

    /**
     * Gets whether the EAA is short-lived (no EAA status check applies)
     *
     * @return whether the EAA is short-lived
     */
    boolean isShortLived();

    /**
     * Gets whether the EAA is for one time use
     *
     * @return whether the EAA is for one time use
     */
    boolean isOneTime();

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
