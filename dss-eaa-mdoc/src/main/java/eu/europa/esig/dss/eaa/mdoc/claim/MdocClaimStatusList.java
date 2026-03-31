package eu.europa.esig.dss.eaa.mdoc.claim;

import eu.europa.esig.dss.eaa.mdoc.MdocConstants;
import eu.europa.esig.dss.model.eaa.claim.ClaimMap;
import eu.europa.esig.dss.model.eaa.claim.ClaimNumber;
import eu.europa.esig.dss.model.eaa.claim.ClaimStatusList;
import eu.europa.esig.dss.model.eaa.claim.ClaimString;

/**
 * Mdoc implementation of a status_list structure as defined in
 * https://www.ietf.org/archive/id/draft-ietf-oauth-status-list-19.html
 * 
 */
public class MdocClaimStatusList extends MdocClaimMap implements ClaimStatusList {

    private static final long serialVersionUID = 8267815192474246983L;

    /**
     * Constructor to initialize MdocClaimStatus from a ClaimMap
     *
     * @param value {@link ClaimMap}
     */
    public MdocClaimStatusList(ClaimMap value) {
        super(value.getName(), value.getNamespace(), value.getMapValue(), value.isSelectivelyDisclosable(), value.getParent());
    }

    @Override
    public ClaimNumber getIndex() {
        return getAsNumber(MdocConstants.STATUS_INDEX);
    }

    @Override
    public ClaimString getUri() {
        return getAsString(MdocConstants.STATUS_URI);
    }

}
