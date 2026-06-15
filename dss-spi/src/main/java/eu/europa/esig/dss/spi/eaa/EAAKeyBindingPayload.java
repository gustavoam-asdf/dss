package eu.europa.esig.dss.spi.eaa;

import eu.europa.esig.dss.model.eaa.claim.Claim;
import eu.europa.esig.dss.model.eaa.claim.ClaimDate;
import eu.europa.esig.dss.model.eaa.claim.ClaimString;

/**
 * Represents a key binding payload
 *
 */
public interface EAAKeyBindingPayload extends Claim {

    ClaimString getNonce();

    ClaimDate getIssuedAt();

    ClaimString getAudience();

    ClaimString getSdHash();
}
