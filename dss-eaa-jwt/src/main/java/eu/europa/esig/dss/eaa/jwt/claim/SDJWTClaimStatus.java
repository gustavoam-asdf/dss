package eu.europa.esig.dss.eaa.jwt.claim;

import eu.europa.esig.dss.eaa.jwt.SDJWTConstants;
import eu.europa.esig.dss.model.eaa.claim.ClaimMap;
import eu.europa.esig.dss.model.eaa.claim.ClaimNumber;
import eu.europa.esig.dss.model.eaa.claim.ClaimStatus;
import eu.europa.esig.dss.model.eaa.claim.ClaimStatusList;
import eu.europa.esig.dss.model.eaa.claim.ClaimString;

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

    @Override
    public ClaimStatusList getIdentifierList() {
        // not defined
        return null;
    }

    @Override
    public ClaimNumber getIndex() {
        return getAsNumber(SDJWTConstants.STATUS_INDEX);
    }

    @Override
    public ClaimString getUri() {
        return getAsString(SDJWTConstants.STATUS_URI);
    }

    @Override
    public ClaimString getType() {
        return getAsString(SDJWTConstants.STATUS_TYPE);
    }

    @Override
    public ClaimString getPurpose() {
        return getAsString(SDJWTConstants.STATUS_PURPOSE);
    }

}
