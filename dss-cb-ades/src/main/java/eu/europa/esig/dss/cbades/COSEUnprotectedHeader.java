package eu.europa.esig.dss.cbades;

import eu.europa.esig.dss.cbades.cbor.CBORMap;

/**
 * Represents a COSE unprotected header structure
 *
 */
public class COSEUnprotectedHeader extends CBORMap {

    /**
     * Creates an empty unprotected header
     */
    public COSEUnprotectedHeader() {
        super();
    }

    /**
     * Creates an unprotected header from a defined {@code CBORMap}
     *
     * @param headerMap {@link CBORMap}
     */
    public COSEUnprotectedHeader(final CBORMap headerMap) {
        super(headerMap.toDataItem());
    }

}
