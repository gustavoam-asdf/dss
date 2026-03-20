package eu.europa.esig.dss.eaa.jwt.claim;

import eu.europa.esig.dss.eaa.jwt.SDJWTConstants;
import eu.europa.esig.dss.model.eaa.claim.ClaimMap;
import eu.europa.esig.dss.model.eaa.claim.ClaimNumber;
import eu.europa.esig.dss.model.eaa.claim.ClaimStatus;
import eu.europa.esig.dss.model.eaa.claim.ClaimString;

/**
 * SD-JWT VC token representation of a "status" header. See draft-ietf-oauth-status-list-13.
 *
 */
public class SDJWTClaimStatus extends SDJWTClaimMap implements ClaimStatus {

    private static final long serialVersionUID = 2273453140105479397L;

    /**
     * Constructor to initialize SDJWTClaimStatus from a ClaimMap
     *
     * @param value {@link ClaimMap}
     */
    public SDJWTClaimStatus(ClaimMap value) {
        super(value.getName(), value.getMapValue(), value.isSelectivelyDisclosable(), value.getParent());
    }

    @Override
    public ClaimNumber getIndex() {
        ClaimMap statusList = getAsMap(SDJWTConstants.STATUS_LIST);
        if (statusList != null) {
            return statusList.getAsNumber(SDJWTConstants.STATUS_INDEX);
        }
        return null;
    }

    @Override
    public ClaimString getUri() {
        ClaimMap statusList = getAsMap(SDJWTConstants.STATUS_LIST);
        if (statusList != null) {
            return statusList.getAsString(SDJWTConstants.STATUS_URI);
        }
        return null;
    }

}
