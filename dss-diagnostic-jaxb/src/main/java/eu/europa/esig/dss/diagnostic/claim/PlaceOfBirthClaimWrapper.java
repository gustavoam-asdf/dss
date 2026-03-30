package eu.europa.esig.dss.diagnostic.claim;

import eu.europa.esig.dss.diagnostic.jaxb.XmlClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlPlaceOfBirthClaim;

import java.util.HashMap;
import java.util.Map;

/**
 * Wraps an {@code eu.europa.esig.dss.diagnostic.jaxb.XmlPlaceOfBirthClaim}
 *
 */
public class PlaceOfBirthClaimWrapper extends ClaimWrapper {

    /**
     * Default constuctor
     *
     * @param wrapped {@link XmlClaim}
     */
    public PlaceOfBirthClaimWrapper(final XmlClaim wrapped) {
        super(wrapped);
    }

    /**
     * Constructor with a parent claim provided
     *
     * @param wrapped {@link XmlClaim}
     * @param parent {@link ClaimWrapper}
     */
    public PlaceOfBirthClaimWrapper(final XmlClaim wrapped, final ClaimWrapper parent) {
        super(wrapped, parent);
    }

    /**
     * Gets the user's city or locality address, when present.
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getCity() {
        XmlClaim wrapped = getWrapped();
        if (wrapped instanceof XmlPlaceOfBirthClaim) {
            XmlClaim city = ((XmlPlaceOfBirthClaim) wrapped).getCity();
            if (city != null) {
                return new ClaimWrapper(city, this);
            }
        }
        return null;
    }

    /**
     * Gets the user's zip code or postal code address, when present.
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getRegion() {
        XmlClaim wrapped = getWrapped();
        if (wrapped instanceof XmlPlaceOfBirthClaim) {
            XmlClaim region = ((XmlPlaceOfBirthClaim) wrapped).getRegion();
            if (region != null) {
                return new ClaimWrapper(region, this);
            }
        }
        return null;
    }

    /**
     * Gets the user's country address, when present.
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getCountry() {
        XmlClaim wrapped = getWrapped();
        if (wrapped instanceof XmlPlaceOfBirthClaim) {
            XmlClaim country = ((XmlPlaceOfBirthClaim) wrapped).getCountry();
            if (country != null) {
                return new ClaimWrapper(country, this);
            }
        }
        return null;
    }

    @Override
    public boolean isMap() {
        XmlClaim wrapped = getWrapped();
        if (wrapped instanceof XmlPlaceOfBirthClaim) {
            return true;
        }
        return super.isMap();
    }

    @Override
    public Map<String, ClaimWrapper> getMap() {
        XmlClaim wrapped = getWrapped();
        if (wrapped instanceof XmlPlaceOfBirthClaim) {
            final Map<String, ClaimWrapper> result = new HashMap<>();
            ClaimWrapper city = getCity();
            if (city != null) {
                result.put(city.getName(), city);
            }
            ClaimWrapper region = getRegion();
            if (region != null) {
                result.put(region.getName(), region);
            }
            ClaimWrapper country = getCountry();
            if (country != null) {
                result.put(country.getName(), country);
            }
            return result;
        }
        return super.getMap();
    }

}
