package eu.europa.esig.dss.eaa.mdoc.claim;

import eu.europa.esig.dss.eaa.mdoc.ISO180135Headers;
import eu.europa.esig.dss.model.eaa.claim.ClaimAgeOverNN;
import eu.europa.esig.dss.model.eaa.claim.ClaimBoolean;
import eu.europa.esig.dss.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mdoc representation of an "age_over_NN" claim as defined in "7.2 mDL data" of ISO/IEC 18013-5.
 * 
 */
public class MdocClaimAgeOverNN extends ClaimBoolean implements ClaimAgeOverNN {

    private static final long serialVersionUID = -6005690209140831298L;

    private static final Logger LOG = LoggerFactory.getLogger(MdocClaimAgeOverNN.class);

    /**
     * Constructor to initialize MdocClaimAgeOverNN from a ClaimBoolean
     *
     * @param value {@link ClaimBoolean}
     */
    public MdocClaimAgeOverNN(ClaimBoolean value) {
        super(value.getName(), value.getNamespace(), value.getBooleanValue(), value.isSelectivelyDisclosable(), value.getParent());
    }
    
    @Override
    public Integer getAge() {
        String name = getName();
        String nnAge = Utils.substringAfter(name, ISO180135Headers.AGE_OVER_NN);
        if (Utils.isStringDigits(nnAge)) {
            return Integer.parseInt(nnAge);
        }
        LOG.warn("Unable to determine age from the header with name '{}'!", name);
        return null;
    }
    
}
