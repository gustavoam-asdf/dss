package eu.europa.esig.dss.eaa.jwt.claim;

import eu.europa.esig.dss.eaa.jwt.SDJWTConstants;
import eu.europa.esig.dss.model.eaa.claim.ClaimAddress;
import eu.europa.esig.dss.model.eaa.claim.ClaimMap;
import eu.europa.esig.dss.model.eaa.claim.ClaimString;

/**
 * SD-JWT VC token representation of a user address claim, defined in OpenID Connect Core 1.0 "5.1.1. Address Claim".
 *
 */
public class SDJWTClaimAddress extends SDJWTClaimMap implements ClaimAddress {

    private static final long serialVersionUID = 4589801086719909382L;

    /**
     * Constructor to initialize SDJWTClaimAddress from a ClaimMap
     *
     * @param value {@link ClaimMap}
     */
    public SDJWTClaimAddress(ClaimMap value) {
        super(value.getName(), value.getMapValue(), value.isSelectivelyDisclosable(), value.getParent());
    }

    @Override
    public ClaimString getPostalAddress() {
        return getAsString(SDJWTConstants.USER_ADDRESS_FORMATTED);
    }

    @Override
    public ClaimString getStreetAddress() {
        return getAsString(SDJWTConstants.USER_ADDRESS_STREET_ADDRESS);
    }

    @Override
    public ClaimString getCity() {
        return getAsString(SDJWTConstants.USER_ADDRESS_LOCALITY);
    }

    @Override
    public ClaimString getStateOrProvince() {
        return getAsString(SDJWTConstants.USER_ADDRESS_REGION);
    }

    @Override
    public ClaimString getPostalCode() {
        return getAsString(SDJWTConstants.USER_ADDRESS_POSTAL_CODE);
    }

    @Override
    public ClaimString getCountry() {
        return getAsString(SDJWTConstants.USER_ADDRESS_COUNTRY);
    }

}
