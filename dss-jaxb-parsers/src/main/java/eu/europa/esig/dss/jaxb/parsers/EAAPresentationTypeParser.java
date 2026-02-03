package eu.europa.esig.dss.jaxb.parsers;

import eu.europa.esig.dss.enumerations.EAAPresentationType;

/**
 * Parses and creates a {@code eu.europa.esig.dss.enumerations.EAAPresentationType}
 *
 */
public class EAAPresentationTypeParser {

    /**
     * Default constructor
     */
    private EAAPresentationTypeParser() {
        // empty
    }

    /**
     * Parses the value and returns {@code EAAPresentationType}
     *
     * @param v {@link String} to parse
     * @return {@link EAAPresentationType}
     */
    public static EAAPresentationType parse(String v) {
        if (v != null) {
            return EAAPresentationType.valueOf(v);
        }
        return null;
    }

    /**
     * Gets a text name of the value
     *
     * @param v {@link EAAPresentationType}
     * @return {@link String}
     */
    public static String print(EAAPresentationType v) {
        if (v != null) {
            return v.name();
        }
        return null;
    }

}
