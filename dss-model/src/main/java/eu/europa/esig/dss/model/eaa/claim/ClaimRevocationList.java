package eu.europa.esig.dss.model.eaa.claim;

/**
 * Represents a generic EAA revocation list
 *
 */
public interface ClaimRevocationList extends Claim {

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
