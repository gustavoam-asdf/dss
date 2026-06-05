package eu.europa.esig.dss.eaa.jwt.claim;

import eu.europa.esig.dss.model.eaa.claim.Claim;
import eu.europa.esig.dss.model.eaa.claim.ClaimAgeOverNN;
import eu.europa.esig.dss.model.eaa.claim.ClaimAgeEqualOrOver;
import eu.europa.esig.dss.model.eaa.claim.ClaimBoolean;
import eu.europa.esig.dss.model.eaa.claim.ClaimMap;
import eu.europa.esig.dss.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Represents the "age_equal_or_over" SD-JWT VC claim.
 * NOTE: Last occurrence in PID Rulebook 2.4.0.
 *
 */
public class SDJWTClaimAgeOverNNList extends SDJWTClaimMap implements ClaimAgeEqualOrOver {

    private static final long serialVersionUID = -1770354162483216734L;

    private static final Logger LOG = LoggerFactory.getLogger(SDJWTClaimAgeOverNNList.class);

    /**
     * Constructor to initialize SDJWTClaimStatus from a ClaimMap
     *
     * @param value {@link ClaimMap}
     */
    public SDJWTClaimAgeOverNNList(ClaimMap value) {
        super(value.getName(), value.getMapValue(), value.isSelectivelyDisclosable(), value.getParent());
    }

    @Override
    public List<ClaimAgeOverNN> getAgeOverNNClaims() {
        Map<String, Claim> embeddedClaims = getMapValue();
        if (Utils.isMapEmpty(embeddedClaims)) {
            return Collections.emptyList();
        }

        final List<ClaimAgeOverNN> result = new ArrayList<>();
        for (Claim claim : embeddedClaims.values()) {
            if (claim.isBooleanValueType()) {
                result.add(new SDJWTClaimAgeOverNN((ClaimBoolean) claim));
            } else {
                LOG.warn("An item of 'age_equal_or_over' shall be of boolean type!");
            }
        }
        return result;
    }

}
