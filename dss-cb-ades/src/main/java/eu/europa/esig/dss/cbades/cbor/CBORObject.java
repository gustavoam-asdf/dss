package eu.europa.esig.dss.cbades.cbor;

import co.nstant.in.cbor.model.DataItem;

/**
 * This interface represents a wrapper for CBOR objects
 *
 */
public interface CBORObject {

    /**
     * Returns whether the object is tagged
     *
     * @return TRUE if the object has a Tag, FALSE otherwise
     */
    boolean isTagged();

    /**
     * Gets associated CBOR Tag, when present
     *
     * @return {@link CBORTag}
     */
    CBORTag getTag();

    /**
     * Sets the tag value for the current CBOR Object
     *
     * @param value long value of the tag
     */
    void setTag(long value);

    /**
     * Returns a wrapped implementation of the COSE object
     *
     * @return {@link DataItem}
     */
    DataItem toDataItem();

    /**
     * Returns whether the current CBOR object is of UnsignedInteger type
     *
     * @return TRUE if the current CBOR object is of UnsignedInteger type, FALSE otherwise
     */
    boolean isUnsignedInteger();

    /**
     * Returns whether the current CBOR object is of NegativeInteger type
     *
     * @return TRUE if the current CBOR object is of NegativeInteger type, FALSE otherwise
     */
    boolean isNegativeInteger();

    /**
     * Returns whether the current CBOR object is of ByteString type
     *
     * @return TRUE if the current CBOR object is of ByteString type, FALSE otherwise
     */
    boolean isByteString();

    /**
     * Returns whether the current CBOR object is of UnicodeString type
     *
     * @return TRUE if the current CBOR object is of UnicodeString type, FALSE otherwise
     */
    boolean isUnicodeString();

    /**
     * Returns whether the current CBOR object is of Boolean type
     *
     * @return TRUE if the current CBOR object is of Boolean type, FALSE otherwise
     */
    boolean isBoolean();

    /**
     * Returns whether the current CBOR object is of Null type
     *
     * @return TRUE if the current CBOR object is of Null type, FALSE otherwise
     */
    boolean isNull();

    /**
     * Returns whether the current CBOR object is of Map type
     *
     * @return TRUE if the current CBOR object is of Map type, FALSE otherwise
     */
    boolean isMap();

    /**
     * Returns whether the current CBOR object is of Array type
     *
     * @return TRUE if the current CBOR object is of Array type, FALSE otherwise
     */
    boolean isArray();

}
