package eu.europa.esig.dss.jaxb.parsers;

import eu.europa.esig.dss.enumerations.EAARevocationOrigin;

/**
 * Parses the {@code eu.europa.esig.dss.enumerations.EAARevocationOrigin}
 *
 */
public class EAARevocationOriginParser {

    /**
     * Default constructor
     */
    private EAARevocationOriginParser() {
        // empty
    }

    /**
     * Parses the value and returns {@code EAARevocationOrigin}
     *
     * @param v {@link String} to parse
     * @return {@link EAARevocationOrigin}
     */
    public static EAARevocationOrigin parse(String v) {
        if (v != null) {
            return EAARevocationOrigin.valueOf(v);
        }
        return null;
    }

    /**
     * Gets a text name of the value
     *
     * @param v {@link EAARevocationOrigin}
     * @return {@link String}
     */
    public static String print(EAARevocationOrigin v) {
        if (v != null) {
            return v.name();
        }
        return null;
    }

}
