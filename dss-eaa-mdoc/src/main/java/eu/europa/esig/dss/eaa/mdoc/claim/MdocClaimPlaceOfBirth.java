package eu.europa.esig.dss.eaa.mdoc.claim;

import eu.europa.esig.dss.eaa.mdoc.EUDIPIDHeaders;
import eu.europa.esig.dss.model.eaa.claim.ClaimMap;
import eu.europa.esig.dss.model.eaa.claim.ClaimPlaceOfBirth;
import eu.europa.esig.dss.model.eaa.claim.ClaimString;

/**
 * Represents an mdoc place of birth, as defined in PID Rulebook "3.1.6 Attribute place_of_birth".
 *
 */
public class MdocClaimPlaceOfBirth extends MdocClaimMap implements ClaimPlaceOfBirth {

    private static final long serialVersionUID = 8034900938724415602L;

    /**
     * Default constructor
     *
     * @param value {@link ClaimMap}
     */
    public MdocClaimPlaceOfBirth(ClaimMap value) {
        super(value.getName(), value.getNamespace(), value.getMapValue(), value.isSelectivelyDisclosable(), value.getParent());
    }
    
    @Override
    public ClaimString getCountry() {
        return getAsString(EUDIPIDHeaders.PLACE_OF_BIRTH_COUNTRY);
    }

    @Override
    public ClaimString getStateOrProvince() {
        return getAsString(EUDIPIDHeaders.PLACE_OF_BIRTH_REGION);
    }

    @Override
    public ClaimString getCity() {
        return getAsString(EUDIPIDHeaders.PLACE_OF_BIRTH_LOCALITY);
    }
    
}
