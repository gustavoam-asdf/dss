package eu.europa.esig.dss.jaxb.parsers;

import eu.europa.esig.dss.enumerations.ListType;

public class ListTypeParser {

    /**
     * Default constructor
     */
    private ListTypeParser() {
        // empty
    }

    /**
     * Parses the value and returns {@code ListType}
     *
     * @param v {@link String} to parse
     * @return {@link ListType}
     */
    public static ListType parse(String v) {
        return ListType.fromUri(v);
    }

    /**
     * Gets a text name of the value
     *
     * @param v {@link ListType}
     * @return {@link String}
     */
    public static String print(ListType v) {
        return v.getUri();
    }

}
