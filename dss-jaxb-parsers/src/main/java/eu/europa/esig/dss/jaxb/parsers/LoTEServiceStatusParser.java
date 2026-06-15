package eu.europa.esig.dss.jaxb.parsers;

import eu.europa.esig.dss.enumerations.LoTEServiceStatus;

public class LoTEServiceStatusParser {

    /**
     * Default constructor
     */
    private LoTEServiceStatusParser() {
        // empty
    }

    /**
     * Parses the value and returns {@code LoTEServiceStatus}
     *
     * @param v {@link String} to parse
     * @return {@link LoTEServiceStatus}
     */
    public static LoTEServiceStatus parse(String v) {
        return LoTEServiceStatus.fromUri(v);
    }

    /**
     * Gets a text name of the value
     *
     * @param v {@link LoTEServiceStatus}
     * @return {@link String}
     */
    public static String print(LoTEServiceStatus v) {
        return v.getUri();
    }

}
