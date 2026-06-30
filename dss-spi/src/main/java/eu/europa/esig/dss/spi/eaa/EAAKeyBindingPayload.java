package eu.europa.esig.dss.spi.eaa;

import eu.europa.esig.dss.model.eaa.claim.Claim;
import eu.europa.esig.dss.model.eaa.claim.ClaimDate;
import eu.europa.esig.dss.model.eaa.claim.ClaimString;

/**
 * Represents a key binding payload
 *
 */
public interface EAAKeyBindingPayload extends Claim {

    /**
     * Gets the value of the nonce from the key binding payload
     *
     * @return {@link ClaimString}
     */
    ClaimString getNonce();

    /**
     * Gets the issuance date from the key binding payload
     *
     * @return {@link ClaimDate}
     */
    ClaimDate getIssuedAt();

    /**
     * Gets the value of the audience from the key binding payload
     *
     * @return {@link ClaimString}
     */
    ClaimString getAudience();

    /**
     * Gets the value of the "sd_hash" from the key binding payload
     *
     * @return {@link ClaimString}
     */
    ClaimString getSdHash();
}
