package eu.europa.esig.dss.eaa.mdoc.validation;

import java.util.Map;

import eu.europa.esig.dss.eaa.mdoc.claim.MdocClaimMap;
import eu.europa.esig.dss.model.eaa.claim.ClaimDate;
import eu.europa.esig.dss.model.eaa.claim.ClaimString;
import eu.europa.esig.dss.spi.eaa.EAAKeyBindingPayload;

public class MdocKeyBindingPayload extends MdocClaimMap implements EAAKeyBindingPayload {

    public MdocKeyBindingPayload(final Map<?, ?> payload) {
        super(payload);
    }

    @Override
    public ClaimString getNonce() {
        // Not present in mdoc
        return null;
    }

    @Override
    public ClaimDate getIssuedAt() {
        // Not present in mdoc
        return null;
    }

    @Override
    public ClaimString getAudience() {
        // Not present in mdoc
        return null;
    }

    @Override
    public ClaimString getSdHash() {
        // Not present in mdoc
        return null;
    }
}
