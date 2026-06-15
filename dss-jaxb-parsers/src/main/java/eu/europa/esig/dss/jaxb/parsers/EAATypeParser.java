package eu.europa.esig.dss.jaxb.parsers;

import eu.europa.esig.dss.enumerations.EAAType;

/**
 * Parses and creates a {@code eu.europa.esig.dss.enumerations.EAAType}
 *
 */
public class EAATypeParser {

    /**
     * Default constructor
     */
    private EAATypeParser() {
        // empty
    }

    /**
     * Parses the value and returns {@code EAAPresentationType}
     *
     * @param v {@link String} to parse
     * @return {@link EAAType}
     */
    public static EAAType parse(String v) {
        if (v != null) {
            return EAAType.valueOf(v);
        }
        return null;
    }

    /**
     * Gets a text name of the value
     *
     * @param v {@link EAAType}
     * @return {@link String}
     */
    public static String print(EAAType v) {
        if (v != null) {
            return v.name();
        }
        return null;
    }

}
