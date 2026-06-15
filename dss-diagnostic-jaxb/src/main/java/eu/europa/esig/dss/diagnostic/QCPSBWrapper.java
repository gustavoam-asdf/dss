package eu.europa.esig.dss.diagnostic;

import eu.europa.esig.dss.diagnostic.jaxb.XmlQcPSB;

/**
 * The class provides a user-friendly API for dealing with {@code eu.europa.esig.dss.diagnostic.jaxb.XmlQcPSB}
 *
 */
public class QCPSBWrapper {

    /** Wrapped object */
    private final XmlQcPSB wrapped;

    /**
     * Default constructor
     *
     * @param xmlQcPSB {@link XmlQcPSB}
     */
    public QCPSBWrapper(XmlQcPSB xmlQcPSB) {
        this.wrapped = xmlQcPSB;
    }

    /**
     * Gets the two-letter code of the legislation country (ISO 3166 alpha-2 country codes or 'EU')
     *
     * @return {@link String}
     */
    public String getCountryOfLegislation() {
        return wrapped.getCountryOfLegislation();
    }

    /**
     * Gets the unique identification of authentic source
     *
     * @return {@link String}
     */
    public String getAuthSourceIdentification() {
        return wrapped.getAuthSourceIdentification();
    }

    /**
     * Gets the legislation identification
     *
     * @return {@link String}
     */
    public String getLegislationIdentification() {
        return wrapped.getLegislationIdentification();
    }

}
