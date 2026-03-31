package eu.europa.esig.dss.eaa.jwt.claim;

import eu.europa.esig.dss.eaa.jwt.SDJWTConstants;
import eu.europa.esig.dss.model.eaa.claim.ClaimMap;
import eu.europa.esig.dss.model.eaa.claim.ClaimStatus;
import eu.europa.esig.dss.model.eaa.claim.ClaimStatusList;

/**
 * SD-JWT VC token representation of a "status" header. See draft-ietf-oauth-status-list-13.
 *
 */
public class SDJWTClaimStatus extends SDJWTClaimMap implements ClaimStatus {

    private static final long serialVersionUID = 8165315191811986745L;

    /**
     * Constructor to initialize SDJWTClaimStatus from a ClaimMap
     *
     * @param value {@link ClaimMap}
     */
    public SDJWTClaimStatus(ClaimMap value) {
        super(value.getName(), value.getMapValue(), value.isSelectivelyDisclosable(), value.getParent());
    }

    @Override
    public ClaimStatusList getStatusList() {
        ClaimMap statusList = getAsMap(SDJWTConstants.STATUS_LIST);
        if (statusList != null) {
            return new SDJWTClaimStatusList(statusList);
        }
        return null;
    }

}
