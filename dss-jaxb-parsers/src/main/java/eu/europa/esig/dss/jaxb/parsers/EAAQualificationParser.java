package eu.europa.esig.dss.jaxb.parsers;

import eu.europa.esig.dss.enumerations.EAAQualification;

/**
 * Parses the {@code eu.europa.esig.dss.enumerations.EAAQualification}
 */
public class EAAQualificationParser {

    /**
     * Default constructor
     */
    private EAAQualificationParser() {
        // empty
    }

    /**
     * Parses the value and returns {@code EAAQualification}
     *
     * @param v {@link String} to parse
     * @return {@link EAAQualification}
     */
    public static EAAQualification parse(String v) {
        return EAAQualification.fromReadable(v);
    }

    /**
     * Gets a text name of the value
     *
     * @param v {@link EAAQualification}
     * @return {@link String}
     */
    public static String print(EAAQualification v) {
        if (v != null) {
            return v.getReadable();
        }
        return null;
    }

}
