package eu.europa.esig.dss.jaxb.parsers;

import eu.europa.esig.dss.enumerations.COSESignatureType;

/**
 * Parses the {@code eu.europa.esig.dss.enumerations.COSESignatureType}
 *
 */
public class COSESignatureTypeParser {

    /**
     * Default constructor
     */
    private COSESignatureTypeParser() {
        // empty
    }

    /**
     * Parses the value and returns {@code COSESignatureType}
     *
     * @param v {@link String} to parse
     * @return {@link COSESignatureType}
     */
    public static COSESignatureType parse(String v) {
        if (v != null) {
            return COSESignatureType.forLabel(v);
        }
        return null;
    }

    /**
     * Gets a text name of the value
     *
     * @param v {@link COSESignatureType}
     * @return {@link String}
     */
    public static String print(COSESignatureType v) {
        if (v != null) {
            return v.getLabel();
        }
        return null;
    }
    
}
