package eu.europa.esig.dss.model.eaa.claim;

/**
 * Represents an EAA Status List claim
 *
 */
public interface ClaimStatusList extends Claim {

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
     * Gets a certificate containing the public key that signed or sealed the top-level
     * certificate in the x5chain element in the MSO revocation list structure
     *
     * @return {@link ClaimByteString}
     */
    ClaimByteString getCertificate();

}
