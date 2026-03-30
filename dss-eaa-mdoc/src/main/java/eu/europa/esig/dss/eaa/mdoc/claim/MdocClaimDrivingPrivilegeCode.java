package eu.europa.esig.dss.eaa.mdoc.claim;

import eu.europa.esig.dss.eaa.mdoc.ISO180135Headers;
import eu.europa.esig.dss.model.eaa.claim.ClaimDrivingPrivilegeCode;
import eu.europa.esig.dss.model.eaa.claim.ClaimMap;
import eu.europa.esig.dss.model.eaa.claim.ClaimString;

/**
 * Represents an mdoc implementation of a driving privilege code, as defined in
 * "7.2.4 Categories of vehicles/restrictions/conditions" of ISO/IEC 18013-5.
 *
 */
public class MdocClaimDrivingPrivilegeCode extends MdocClaimMap implements ClaimDrivingPrivilegeCode {

    private static final long serialVersionUID = -4531064601793382331L;

    /**
     * Constructor to initialize MdocClaimCode from a ClaimMap
     *
     * @param value {@link ClaimMap}
     */
    public MdocClaimDrivingPrivilegeCode(ClaimMap value) {
        super(value.getName(), value.getNamespace(), value.getMapValue(), value.isSelectivelyDisclosable(), value.getParent());
    }

    @Override
    public ClaimString getCode() {
        return getAsString(ISO180135Headers.DRIVING_PRIVILEGES_CODE_CODE);
    }

    @Override
    public ClaimString getSign() {
        return getAsString(ISO180135Headers.DRIVING_PRIVILEGES_CODE_SIGN);
    }

    @Override
    public ClaimString getValue() {
        return getAsString(ISO180135Headers.DRIVING_PRIVILEGES_CODE_VALUE);
    }

}
