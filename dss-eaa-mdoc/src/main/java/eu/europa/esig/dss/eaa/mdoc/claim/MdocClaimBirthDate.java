package eu.europa.esig.dss.eaa.mdoc.claim;

import eu.europa.esig.dss.eaa.mdoc.ISO232202Headers;
import eu.europa.esig.dss.model.eaa.claim.ClaimBirthDate;
import eu.europa.esig.dss.model.eaa.claim.ClaimDate;
import eu.europa.esig.dss.model.eaa.claim.ClaimMap;
import eu.europa.esig.dss.model.eaa.claim.ClaimString;

/**
 * Mdoc implementation of the ISO/IEC 23220-2:2026 "6.3.1.3 Date of birth structure" data element
 *
 */
public class MdocClaimBirthDate extends MdocClaimMap implements ClaimBirthDate {

    private static final long serialVersionUID = -7090594892082602908L;

    /**
     * Constructor to initialize MdocClaimBirthDate from a ClaimMap
     *
     * @param value {@link ClaimMap}
     */
    public MdocClaimBirthDate(ClaimMap value) {
        super(value.getName(), value.getNamespace(), value.getMapValue(), value.isSelectivelyDisclosable(), value.getParent());
    }

    @Override
    public ClaimDate getBirthDate() {
        return getAsDate(ISO232202Headers.BIRTH_DATE);
    }

    @Override
    public ClaimString getApproximateMask() {
        return getAsString(ISO232202Headers.APPROXIMATE_MASK);
    }

}
