package eu.europa.esig.dss.diagnostic.claim;

import eu.europa.esig.dss.diagnostic.jaxb.XmlAddressClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDisclosableClaim;

/**
 * Wraps an {@code XmlAddressClaim}
 * 
 */
public class AddressClaimWrapper extends ClaimWrapper {
    
    /**
     * Default constructor
     *
     * @param wrapped {@link XmlDisclosableClaim}
     */
    public AddressClaimWrapper(final XmlAddressClaim wrapped) {
        super(wrapped);
    }

    /**
     * Gets the user's full postal or mailing address, formatted, when present
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getPostalAddress() {
        XmlDisclosableClaim postalAddress = getWrapped().getPostalAddress();
        if (postalAddress != null) {
            return new ClaimWrapper(postalAddress);
        }
        return null;
    }

    /**
     * Gets the user's street address, when present.
     * The component may include a house number, street name, Post Office Box, and multi-line
     * extended street address information.
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getStreetAddress() {
        XmlDisclosableClaim streetAddress = getWrapped().getStreetAddress();
        if (streetAddress != null) {
            return new ClaimWrapper(streetAddress);
        }
        return null;
    }

    /**
     * Gets the user's city or locality address, when present.
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getCity() {
        XmlDisclosableClaim city = getWrapped().getCity();
        if (city != null) {
            return new ClaimWrapper(city);
        }
        return null;
    }

    /**
     * Gets the user's state or region address, when present.
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getStateOrProvince() {
        XmlDisclosableClaim streetAddress = getWrapped().getStateOrProvince();
        if (streetAddress != null) {
            return new ClaimWrapper(streetAddress);
        }
        return null;
    }

    /**
     * Gets the user's zip code or postal code address, when present.
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getPostalCode() {
        XmlDisclosableClaim streetAddress = getWrapped().getPostalCode();
        if (streetAddress != null) {
            return new ClaimWrapper(streetAddress);
        }
        return null;
    }

    /**
     * Gets the user's country address, when present.
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getCountry() {
        XmlDisclosableClaim streetAddress = getWrapped().getCountryName();
        if (streetAddress != null) {
            return new ClaimWrapper(streetAddress);
        }
        return null;
    }

    @Override
    protected XmlAddressClaim getWrapped() {
        return (XmlAddressClaim) super.getWrapped();
    }
    
}
