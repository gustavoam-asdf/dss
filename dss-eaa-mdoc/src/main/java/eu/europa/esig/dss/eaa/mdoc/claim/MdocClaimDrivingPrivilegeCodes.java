package eu.europa.esig.dss.eaa.mdoc.claim;

import eu.europa.esig.dss.model.eaa.claim.Claim;
import eu.europa.esig.dss.model.eaa.claim.ClaimArray;
import eu.europa.esig.dss.model.eaa.claim.ClaimDrivingPrivilegeCode;
import eu.europa.esig.dss.model.eaa.claim.ClaimDrivingPrivilegeCodes;
import eu.europa.esig.dss.model.eaa.claim.ClaimMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an mdoc implementation of driving privilege codes, as defined in
 * "7.2.4 Categories of vehicles/restrictions/conditions" of ISO/IEC 18013-5.
 *
 */
public class MdocClaimDrivingPrivilegeCodes extends MdocClaimArray implements ClaimDrivingPrivilegeCodes {

    private static final long serialVersionUID = -6790765343355329691L;

    /**
     * Constructor to initialize MdocClaimCodes from a ClaimArray
     *
     * @param value {@link ClaimArray}
     */
    public MdocClaimDrivingPrivilegeCodes(ClaimArray value) {
        super(value.getName(), value.getNamespace(), value.getListValue(), value.isSelectivelyDisclosable(), value.getParent());
    }

    @Override
    public List<ClaimDrivingPrivilegeCode> getCodes() {
        final List<ClaimDrivingPrivilegeCode> result = new ArrayList<>();
        for (Claim claim : getListValue()) {
            if (claim.isMapValueType()) {
                result.add(new MdocClaimDrivingPrivilegeCode((ClaimMap) claim));
            }
        }
        return result;
    }

}
