package eu.europa.esig.dss.eaa.jwt.claim;

import eu.europa.esig.dss.model.eaa.claim.ClaimAgeOverNN;
import eu.europa.esig.dss.model.eaa.claim.ClaimBoolean;
import eu.europa.esig.dss.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SD-JWT VC claim representing an age_over_NN for a given value
 *
 */
public class SDJWTClaimAgeOverNN extends ClaimBoolean implements ClaimAgeOverNN {

    private static final long serialVersionUID = -1770354162483216734L;

    private static final Logger LOG = LoggerFactory.getLogger(SDJWTClaimAgeOverNN.class);

    /**
     * Constructor to initialize SDJWTClaimAgeOverNN from a ClaimBoolean
     *
     * @param value {@link ClaimBoolean}
     */
    public SDJWTClaimAgeOverNN(ClaimBoolean value) {
        super(value.getName(), value.getBooleanValue(), value.isSelectivelyDisclosable(), value.getParent());
    }

    @Override
    public Integer getAge() {
        String name = getName();
        if (Utils.isStringDigits(name)) {
            return Integer.parseInt(name);
        }
        LOG.warn("Unable to determine age from the header with name '{}'!", name);
        return null;
    }

}
