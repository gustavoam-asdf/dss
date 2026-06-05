package eu.europa.esig.dss.eaa.jwt.claim;

import eu.europa.esig.dss.eaa.jwt.SDJWTConstants;
import eu.europa.esig.dss.model.eaa.claim.ClaimByteString;
import eu.europa.esig.dss.model.eaa.claim.ClaimMap;
import eu.europa.esig.dss.model.eaa.claim.ClaimNumber;
import eu.europa.esig.dss.model.eaa.claim.ClaimStatusList;
import eu.europa.esig.dss.model.eaa.claim.ClaimString;

/**
 * SD-JWT VC token representation of a "status_list" header. See draft-ietf-oauth-status-list-13.
 *
 */
public class SDJWTClaimStatusList extends SDJWTClaimMap implements ClaimStatusList {

    private static final long serialVersionUID = 2273453140105479397L;

    /**
     * Constructor to initialize SDJWTClaimStatus from a ClaimMap
     *
     * @param value {@link ClaimMap}
     */
    public SDJWTClaimStatusList(ClaimMap value) {
        super(value.getName(), value.getMapValue(), value.isSelectivelyDisclosable(), value.getParent());
    }

    @Override
    public ClaimNumber getIndex() {
        return getAsNumber(SDJWTConstants.STATUS_LIST_IDX);
    }

    @Override
    public ClaimString getUri() {
        return getAsString(SDJWTConstants.STATUS_LIST_URI);
    }

    @Override
    public ClaimByteString getCertificate() {
        // not defined (updated with draft-ietf-oauth-status-list-20)
        return null;
    }

}
