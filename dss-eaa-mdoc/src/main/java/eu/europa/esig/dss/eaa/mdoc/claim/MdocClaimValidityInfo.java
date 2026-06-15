package eu.europa.esig.dss.eaa.mdoc.claim;

import eu.europa.esig.dss.eaa.mdoc.MdocConstants;
import eu.europa.esig.dss.model.eaa.claim.ClaimDate;
import eu.europa.esig.dss.model.eaa.claim.ClaimMap;
import eu.europa.esig.dss.model.eaa.claim.ClaimValidityInfo;

/**
 * Mdoc implementation of a ValidityInfo element as defined in "9.1.2.4 Signing method and structure for MSO"
 * of ISO/IEC 18013-5.
 *
 */
public class MdocClaimValidityInfo extends MdocClaimMap implements ClaimValidityInfo {

    private static final long serialVersionUID = 2222443004678615851L;

    /**
     * Constructor to initialize MdocClaimValidityInfo from a ClaimMap
     *
     * @param value {@link ClaimMap}
     */
    public MdocClaimValidityInfo(ClaimMap value) {
        super(value.getName(), value.getNamespace(), value.getMapValue(), value.isSelectivelyDisclosable(), value.getParent());
    }

    @Override
    public ClaimDate getSigned() {
        return getAsDateTime(MdocConstants.SIGNED);
    }

    @Override
    public ClaimDate getValidFrom() {
        return getAsDateTime(MdocConstants.VALID_FROM);
    }

    @Override
    public ClaimDate getValidUntil() {
        return getAsDateTime(MdocConstants.VALID_UNTIL);
    }

    @Override
    public ClaimDate getExpectedUpdate() {
        return getAsDateTime(MdocConstants.EXPECTED_UPDATE);
    }

}
