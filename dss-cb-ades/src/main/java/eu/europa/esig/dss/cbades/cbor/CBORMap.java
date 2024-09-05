package eu.europa.esig.dss.cbades.cbor;

import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.MajorType;
import eu.europa.esig.dss.spi.exception.IllegalInputException;
import eu.europa.esig.dss.utils.Utils;

import java.util.List;

/**
 * Internal wrapper of a CBOR Map object
 *
 */
public class CBORMap extends AbstractCBORObject {

    /** Implementation of a CBOR Map object */
    private final co.nstant.in.cbor.model.Map map;

    /** Serialized map */
    private CBORByteString serializedMap;

    /**
     * Constructor to create an empty map
     */
    public CBORMap() {
        this(new co.nstant.in.cbor.model.Map());
    }

    /**
     * Constructor to create a CBORMap from a Map implementation
     *
     * @param map {@link co.nstant.in.cbor.model.Map}
     */
    public CBORMap(final co.nstant.in.cbor.model.Map map) {
        super(map);
        this.map = map;
    }

    /**
     * Creates a CBORMap from a serialized map
     *
     * @param serializedMap {@link CBORByteString} containing an empty or serialized map
     */
    public CBORMap(final CBORByteString serializedMap) {
        this(parseByteStringHeader(serializedMap));
        this.serializedMap = serializedMap;
    }

    private static co.nstant.in.cbor.model.Map parseByteStringHeader(CBORByteString cborByteString) {
        try {
            List<DataItem> dataItems = CBORUtils.parseCbor(cborByteString.getBytes());
            if (Utils.collectionSize(dataItems) == 0) {
                return new co.nstant.in.cbor.model.Map();
            } else if (Utils.collectionSize(dataItems) > 1) {
                throw new IllegalInputException("Protected header root shall consist of one data object!");
            }
            DataItem dataItem = dataItems.iterator().next();
            if (MajorType.MAP != dataItem.getMajorType()) {
                throw new IllegalInputException("Protected header shall be of Map type!");
            }
            return (co.nstant.in.cbor.model.Map) dataItem;

        } catch (CborException e) {
            throw new IllegalInputException(String.format("Unable to parse protected header: %s", e.getMessage()), e);
        }
    }

    /**
     * Returns a serialized value of the protected header map
     *
     * @return {@link CBORByteString}
     */
    public CBORByteString getByteString() {
        if (serializedMap == null) {
            serializedMap = new CBORByteString(CBORUtils.serializeCborObject(this));
        }
        return serializedMap;
    }

    /**
     * Checks whether the map contains a header value for the given {@code key}
     *
     * @param key long value of the header key
     * @return TRUE if the map contains the header for the given key, FALSE otherwise
     */
    public boolean containsKey(long key) {
        return map.getKeys().contains(CBORUtils.toDataItem(key));
    }

    /**
     * Returns header value for the given key
     *
     * @param key long value of a key to get a map value for
     * @return {@link DataItem}
     */
    public DataItem getHeader(long key) {
        return map.get(CBORUtils.toDataItem(key));
    }

    /**
     * Gets a header value as {@code Long}. Returns NULL if the header is not found for teh given key or
     * its value is not of UnsignedInteger or NegativeInteger type
     *
     * @param key long value of the key
     * @return {@link Long} value of the header
     */
    public Long getHeaderAsLong(long key) {
        DataItem dataItem = getHeader(key);
        if (dataItem != null && (MajorType.UNSIGNED_INTEGER == dataItem.getMajorType() || MajorType.NEGATIVE_INTEGER == dataItem.getMajorType())) {
            return ((co.nstant.in.cbor.model.Number) dataItem).getValue().longValue();
        }
        return null;
    }

    /**
     * Adds a new entry to the map with a given {@code key} and {@code value}.
     * Transforms the {@code value} to the required type (if supported)
     *
     * @param key long value of the key
     * @param value {@link Object} representing the header value
     */
    public void put(long key, Object value) {
        map.put(CBORUtils.toDataItem(key), CBORUtils.toDataItem(value));
    }

    /**
     * Checks if the current map object is empty
     *
     * @return TRUE if the map is empty, FALSE otherwise
     */
    public boolean isEmpty() {
        return Utils.isCollectionEmpty(map.getKeys());
    }

    @Override
    public DataItem toDataItem() {
        return map;
    }

}
