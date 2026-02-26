package eu.europa.esig.dss.diagnostic.claim;

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
     * Gets the user's city or locality address, when present.
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getCity() {
        return new ClaimWrapper(getWrapped().getCity());
    }

    /**
     * Gets the user's zip code or postal code address, when present.
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getRegion() {
        return new ClaimWrapper(getWrapped().getRegion());
    }

    /**
     * Gets the user's country address, when present.
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getCountry() {
        return new ClaimWrapper(getWrapped().getCountry());
    }

    @Override
    protected XmlPlaceOfBirthClaim getWrapped() {
        return (XmlPlaceOfBirthClaim) super.getWrapped();
    }

}
