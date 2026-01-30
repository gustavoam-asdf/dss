package eu.europa.esig.dss.jaxb.parsers;

import eu.europa.esig.dss.enumerations.LoTEServiceTypeIdentifier;

public class LoTEServiceTypeIdentifierParser {

    /**
     * Default constructor
     */
    private LoTEServiceTypeIdentifierParser() {
        // empty
    }

    /**
     * Parses the value and returns {@code LoTEServiceTypeIdentifier}
     *
     * @param v {@link String} to parse
     * @return {@link LoTEServiceTypeIdentifier}
     */
    public static LoTEServiceTypeIdentifier parse(String v) {
        return LoTEServiceTypeIdentifier.fromUri(v);
    }

    /**
     * Gets a text name of the value
     *
     * @param v {@link LoTEServiceTypeIdentifier}
     * @return {@link String}
     */
    public static String print(LoTEServiceTypeIdentifier v) {
        return v.getUri();
    }

}
