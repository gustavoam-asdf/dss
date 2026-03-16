package eu.europa.esig.dss.cbades;

import eu.europa.esig.dss.cbades.cbor.CBORByteString;
import eu.europa.esig.dss.cbades.cbor.CBORMap;

/**
 * Represents a COSE protected header structure
 *
 */
public class COSEProtectedHeader extends CBORMap {

    /**
     * Constructor to create an empty protected header
     */
    public COSEProtectedHeader() {
        super();
    }

    /**
     * Creates a COSEProtectedHeader from a serialized map
     *
     * @param cborByteString {@link CBORByteString} containing an empty or serialized map
     */
    public COSEProtectedHeader(final CBORByteString cborByteString) {
        super(cborByteString);
    }

}
