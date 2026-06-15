package eu.europa.esig.dss.eaa.mdoc.claim;

import eu.europa.esig.dss.eaa.mdoc.MdocConstants;
import eu.europa.esig.dss.model.eaa.claim.ClaimIdentifierList;
import eu.europa.esig.dss.model.eaa.claim.ClaimMap;
import eu.europa.esig.dss.model.eaa.claim.ClaimNumber;
import eu.europa.esig.dss.model.eaa.claim.ClaimStatus;
import eu.europa.esig.dss.model.eaa.claim.ClaimStatusList;
import eu.europa.esig.dss.model.eaa.claim.ClaimString;

/**
 * Mdoc implementation of a Status structure as defined in
 * https://www.ietf.org/archive/id/draft-ietf-oauth-status-list-19.html
 *
 */
public class MdocClaimStatus extends MdocClaimMap implements ClaimStatus {

    private static final long serialVersionUID = 8165315191811986745L;

    /**
     * Constructor to initialize MdocClaimStatus from a ClaimMap
     *
     * @param value {@link ClaimMap}
     */
    public MdocClaimStatus(ClaimMap value) {
        super(value.getName(), value.getNamespace(), value.getMapValue(), value.isSelectivelyDisclosable(), value.getParent());
    }

    @Override
    public ClaimStatusList getStatusList() {
        ClaimMap statusList = getAsMap(MdocConstants.STATUS_LIST);
        if (statusList != null) {
            return new MdocClaimStatusList(statusList);
        }
        return null;
    }

    @Override
    public ClaimIdentifierList getIdentifierList() {
        ClaimMap statusList = getAsMap(MdocConstants.IDENTIFIER_LIST);
        if (statusList != null) {
            return new MdocClaimIdentifierList(statusList);
        }
        return null;
    }

    @Override
    public ClaimNumber getIndex() {
        return getAsNumber(MdocConstants.STATUS_INDEX);
    }

    @Override
    public ClaimString getUri() {
        return getAsString(MdocConstants.STATUS_URI);
    }

    @Override
    public ClaimString getType() {
        return getAsString(MdocConstants.STATUS_TYPE);
    }

    @Override
    public ClaimString getPurpose() {
        return getAsString(MdocConstants.STATUS_PURPOSE);
    }

}
