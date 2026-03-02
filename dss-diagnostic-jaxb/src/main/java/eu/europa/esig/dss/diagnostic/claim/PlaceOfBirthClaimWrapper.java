package eu.europa.esig.dss.diagnostic.claim;

import eu.europa.esig.dss.diagnostic.jaxb.XmlClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlPlaceOfBirthClaim;

/**
 * Wraps an {@code eu.europa.esig.dss.diagnostic.jaxb.XmlPlaceOfBirthClaim}
 *
 */
public class PlaceOfBirthClaimWrapper extends ClaimWrapper {

    /**
     * Default constructor
     *
     * @param wrapped {@link XmlPlaceOfBirthClaim}
     */
    public PlaceOfBirthClaimWrapper(final XmlPlaceOfBirthClaim wrapped) {
        super(wrapped);
    }

    /**
     * Constructor with a parent claim provided
     *
     * @param wrapped {@link XmlClaim}
     * @param parent {@link ClaimWrapper}
     */
    public PlaceOfBirthClaimWrapper(final XmlPlaceOfBirthClaim wrapped, final ClaimWrapper parent) {
        super(wrapped, parent);
    }

    /**
     * Gets the user's city or locality address, when present.
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getCity() {
        XmlClaim city = getWrapped().getCity();
        if (city != null) {
            return new ClaimWrapper(city, this);
        }
        return null;
    }

    /**
     * Gets the user's zip code or postal code address, when present.
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getRegion() {
        XmlClaim region = getWrapped().getRegion();
        if (region != null) {
            return new ClaimWrapper(region, this);
        }
        return null;
    }

    /**
     * Gets the user's country address, when present.
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getCountry() {
        XmlClaim country = getWrapped().getCountry();
        if (country != null) {
            return new ClaimWrapper(country, this);
        }
        return null;
    }

    @Override
    public XmlPlaceOfBirthClaim getWrapped() {
        return (XmlPlaceOfBirthClaim) super.getWrapped();
    }

}
