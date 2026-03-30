package eu.europa.esig.dss.eaa.mdoc.claim;

import eu.europa.esig.dss.eaa.mdoc.ISO180135Headers;
import eu.europa.esig.dss.model.eaa.claim.ClaimArray;
import eu.europa.esig.dss.model.eaa.claim.ClaimDate;
import eu.europa.esig.dss.model.eaa.claim.ClaimDrivingPrivilegeCodes;
import eu.europa.esig.dss.model.eaa.claim.ClaimDrivingPrivilege;
import eu.europa.esig.dss.model.eaa.claim.ClaimMap;
import eu.europa.esig.dss.model.eaa.claim.ClaimString;

/**
 * Represents an mdoc implementation of a driving privilege, as defined in
 * "7.2.4 Categories of vehicles/restrictions/conditions" of ISO/IEC 18013-5.
 *
 */
public class MdocClaimDrivingPrivilege extends MdocClaimMap implements ClaimDrivingPrivilege {

    private static final long serialVersionUID = 4937039974596393841L;

    /**
     * Constructor to initialize MdocDrivingPrivilege from a ClaimMap
     *
     * @param value {@link ClaimMap}
     */
    public MdocClaimDrivingPrivilege(ClaimMap value) {
        super(value.getName(), value.getNamespace(), value.getMapValue(), value.isSelectivelyDisclosable(), value.getParent());
    }

    @Override
    public ClaimString getVehicleCategoryCode() {
        return getAsString(ISO180135Headers.DRIVING_PRIVILEGES_VEHICLE_CATEGORY_CODE);
    }

    @Override
    public ClaimDate getIssueDate() {
        return getAsDate(ISO180135Headers.DRIVING_PRIVILEGES_ISSUE_DATE);
    }

    @Override
    public ClaimDate getExpiryDate() {
        return getAsDate(ISO180135Headers.DRIVING_PRIVILEGES_EXPIRY_DATE);
    }

    @Override
    public ClaimDrivingPrivilegeCodes getCodes() {
        ClaimArray codesArray = getAsArray(ISO180135Headers.DRIVING_PRIVILEGES_CODES);
        if (codesArray != null) {
            return new MdocClaimDrivingPrivilegeCodes(codesArray);
        }
        return null;
    }

}
