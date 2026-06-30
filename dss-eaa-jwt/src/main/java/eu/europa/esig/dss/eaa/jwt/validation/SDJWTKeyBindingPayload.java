package eu.europa.esig.dss.eaa.jwt.validation;

import java.util.Map;

import eu.europa.esig.dss.eaa.jwt.SDJWTConstants;
import eu.europa.esig.dss.eaa.jwt.claim.SDJWTClaimMap;
import eu.europa.esig.dss.model.eaa.claim.ClaimDate;
import eu.europa.esig.dss.model.eaa.claim.ClaimString;
import eu.europa.esig.dss.spi.eaa.EAAKeyBindingPayload;

/**
 * Implementation of {@link EAAKeyBindingPayload} for SD-JWT EAA
 */
public class SDJWTKeyBindingPayload extends SDJWTClaimMap implements EAAKeyBindingPayload {

    SDJWTKeyBindingPayload(final Map<String, Object> payload) {
        super(payload);
    }

    @Override
    public ClaimString getNonce() {
        return getAsString(SDJWTConstants.NONCE);
    }

    @Override
    public ClaimDate getIssuedAt() {
        return getAsDateTime(SDJWTConstants.ISSUED_AT);
    }

    @Override
    public ClaimString getAudience() {
        return getAsString(SDJWTConstants.AUDIENCE);
    }

    @Override
    public ClaimString getSdHash() {
        return getAsString(SDJWTConstants.SD_HASH);
    }

}
