package eu.europa.esig.dss.eaa.jwt.claim;

import eu.europa.esig.dss.eaa.jwt.SDJWTConstants;
import eu.europa.esig.dss.model.eaa.claim.ClaimMap;
import eu.europa.esig.dss.model.eaa.claim.ClaimPlaceOfBirth;
import eu.europa.esig.dss.model.eaa.claim.ClaimString;

/**
 * Represents an SD JWT VC user's place of birth, as defined in
 * OpenID Connect for Identity Assurance Claims Registration 1.0 "4.1. Additional claims about end-users".
 *
 */
public class SDJWTClaimPlaceOfBirth extends SDJWTClaimMap implements ClaimPlaceOfBirth {

    private static final long serialVersionUID = 2338450733613706116L;

    /**
     * Default constructor
     *
     * @param value {@link ClaimMap}
     */
    public SDJWTClaimPlaceOfBirth(ClaimMap value) {
        super(value.getName(), value.getMapValue(), value.isSelectivelyDisclosable(), value.getParent());
    }

    @Override
    public ClaimString getCountry() {
        return getAsString(SDJWTConstants.USER_PLACE_OF_BIRTH_COUNTRY);
    }

    @Override
    public ClaimString getStateOrProvince() {
        return getAsString(SDJWTConstants.USER_PLACE_OF_BIRTH_REGION);
    }

    @Override
    public ClaimString getCity() {
        return getAsString(SDJWTConstants.USER_PLACE_OF_BIRTH_LOCALITY);
    }

}
