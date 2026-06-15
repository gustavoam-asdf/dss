package eu.europa.esig.dss.jaxb.parsers;

import eu.europa.esig.dss.enumerations.EAAStatus;

/**
 * Parses the {@code eu.europa.esig.dss.enumerations.EAAStatus}
 *
 */
public class EAAStatusParser {

    /**
     * Default constructor
     */
    private EAAStatusParser() {
        // empty
    }

    /**
     * Parses the value and returns {@code EAAStatus}
     *
     * @param v {@link String} to parse
     * @return {@link EAAStatus}
     */
    public static EAAStatus parse(String v) {
        if (v != null) {
            return EAAStatus.valueOf(v);
        }
        return null;
    }

    /**
     * Gets a text name of the value
     *
     * @param v {@link EAAStatus}
     * @return {@link String}
     */
    public static String print(EAAStatus v) {
        if (v != null) {
            return v.name();
        }
        return null;
    }
    
}
