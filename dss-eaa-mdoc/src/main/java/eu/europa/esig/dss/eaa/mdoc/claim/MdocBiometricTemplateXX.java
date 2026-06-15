package eu.europa.esig.dss.eaa.mdoc.claim;

import eu.europa.esig.dss.eaa.mdoc.ISO180135Headers;
import eu.europa.esig.dss.model.eaa.claim.ClaimBiometricTemplateXX;
import eu.europa.esig.dss.model.eaa.claim.ClaimBoolean;
import eu.europa.esig.dss.model.eaa.claim.ClaimByteString;
import eu.europa.esig.dss.utils.Utils;

/**
 * Mdoc implementation of a biometric template as defined in "7.2.6 Biometric template" of ISO/IEC 18013-5.
 *
 */
public class MdocBiometricTemplateXX extends ClaimByteString implements ClaimBiometricTemplateXX {

    private static final long serialVersionUID = -6005690209140831298L;

    /**
     * Constructor to initialize MdocClaimAgeOverNN from a ClaimByteString
     *
     * @param value {@link ClaimBoolean}
     */
    public MdocBiometricTemplateXX(ClaimByteString value) {
        super(value.getName(), value.getNamespace(), value.getBinaryValue(), value.isSelectivelyDisclosable(), value.getParent());
    }

    @Override
    public String getType() {
        String name = getName();
        return Utils.substringAfter(name, ISO180135Headers.BIOMETRIC_TEMPLATE_XX);
    }

}
