package eu.europa.esig.dss.jaxb.parsers;

import eu.europa.esig.dss.enumerations.EAAStatusOrigin;

/**
 * Parses the {@code eu.europa.esig.dss.enumerations.EAAStatusOrigin}
 *
 */
public class EAAStatusOriginParser {

    /**
     * Default constructor
     */
    private EAAStatusOriginParser() {
        // empty
    }

    /**
     * Parses the value and returns {@code EAAStatusOrigin}
     *
     * @param v {@link String} to parse
     * @return {@link EAAStatusOrigin}
     */
    public static EAAStatusOrigin parse(String v) {
        if (v != null) {
            return EAAStatusOrigin.valueOf(v);
        }
        return null;
    }

    /**
     * Gets a text name of the value
     *
     * @param v {@link EAAStatusOrigin}
     * @return {@link String}
     */
    public static String print(EAAStatusOrigin v) {
        if (v != null) {
            return v.name();
        }
        return null;
    }

}
