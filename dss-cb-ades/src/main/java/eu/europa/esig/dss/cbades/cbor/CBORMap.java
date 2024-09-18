package eu.europa.esig.dss.cbades.cbor;

import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.MajorType;
import eu.europa.esig.dss.spi.exception.IllegalInputException;
import eu.europa.esig.dss.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Internal wrapper of a CBOR Map object
 *
 */
public class CBORMap extends AbstractCBORObject<co.nstant.in.cbor.model.Map> {

    private static final Logger LOG = LoggerFactory.getLogger(CBORMap.class);

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
        return toDataItem().getKeys().contains(CBORUtils.toDataItem(key));
    }

    /**
     * Returns header value for the given key
     *
     * @param key long value of a key to get a map value for
     * @return {@link CBORObject}
     */
    public CBORObject getHeader(long key) {
        DataItem value = toDataItem().get(CBORUtils.toDataItem(key));
        if (value != null) {
            return CBORUtils.toCBORObject(value);
        }
        return null;
    }

    /**
     * Returns a set of entries from the current map
     *
     * @return a set of map entries, with a {@link Long} key and {@link CBORObject} value
     */
    public Set<Map.Entry<Long, CBORObject>> entrySet() {
        return toMap().entrySet();
    }

    private Map<Long, CBORObject> toMap() {
        final Map<Long, CBORObject> result = new HashMap<>();
        for (Long key : getKeys()) {
            CBORObject value = getHeader(key);
            result.put(key, value);
        }
        return result;
    }

    private Set<Long> getKeys() {
        final Set<Long> keys = new HashSet<>();
        for (DataItem keyDataItem : toDataItem().getKeys()) {
            CBORObject keyObject = CBORUtils.toCBORObject(keyDataItem);
            if (keyObject.isUnsignedInteger() || keyObject.isNegativeInteger()) {
                keys.add(((CBORSimpleObject) keyObject).getValueAsLong());
            } else {
                LOG.warn("Unsupported key of type '{}' : {}.", keyObject.getClass().getSimpleName(), keyObject);
            }
        }
        return keys;
    }

    /**
     * Gets a header value as {@code Boolean}. Returns NULL if the header is not found for the given key or
     * its value is not of Boolean type
     *
     * @param key long value of the key
     * @return {@link Boolean} value of the header
     */
    public Boolean getAsBoolean(long key) {
        CBORObject cborObject = getHeader(key);
        if (cborObject != null && (cborObject.isBoolean())) {
            return ((CBORSimpleObject) cborObject).getValueAsBoolean();
        }
        return null;
    }

    /**
     * Gets a header value as {@code Long}. Returns NULL if the header is not found for the given key or
     * its value is not of UnsignedInteger or NegativeInteger type
     *
     * @param key long value of the key
     * @return {@link Long} value of the header
     */
    public Long getAsLong(long key) {
        CBORObject cborObject = getHeader(key);
        if (cborObject != null && (cborObject.isUnsignedInteger() || cborObject.isNegativeInteger())) {
            return ((CBORSimpleObject) cborObject).getValueAsLong();
        }
        return null;
    }

    /**
     * Gets a header value as {@code String}. Returns NULL if the header is not found for the given key or
     * its value is not of UnicodeString type
     *
     * @param key long value of the key
     * @return {@link String} value of the header
     */
    public String getAsString(long key) {
        CBORObject cborObject = getHeader(key);
        if (cborObject != null && (cborObject.isUnicodeString())) {
            return ((CBORSimpleObject) cborObject).getValueAsString();
        }
        return null;
    }

    /**
     * Gets a header value as a byte array. Returns NULL if the header is not found for the given key or
     * its value is not of ByteString type
     *
     * @param key long value of the key
     * @return a byte array value of the header
     */
    public byte[] getAsBinaries(long key) {
        CBORObject cborObject = getHeader(key);
        if (cborObject != null && cborObject.isByteString()) {
            return ((CBORByteString) cborObject).getBytes();
        }
        return null;
    }

    /**
     * Gets a header value as {@code CBORArray}. Returns NULL if the header is not found for the given key or
     * its value is not of Array type
     *
     * @param key long value of the key
     * @return {@link CBORArray} value of the header
     */
    public CBORArray getAsArray(long key) {
        CBORObject cborObject = getHeader(key);
        if (cborObject != null && cborObject.isArray()) {
            return ((CBORArray) cborObject);
        }
        return null;
    }

    /**
     * Gets a header value as {@code CBORMap}. Returns NULL if the header is not found for the given key or
     * its value is not of Map type
     *
     * @param key long value of the key
     * @return {@link CBORMap} value of the header
     */
    public CBORMap getAsMap(long key) {
        CBORObject cborObject = getHeader(key);
        if (cborObject != null && cborObject.isMap()) {
            return ((CBORMap) cborObject);
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
        toDataItem().put(CBORUtils.toDataItem(key), CBORUtils.toDataItem(value));
        clearSerializedBytes();
    }

    private void clearSerializedBytes() {
        serializedMap = null;
    }

    /**
     * Checks if the current map object is empty
     *
     * @return TRUE if the map is empty, FALSE otherwise
     */
    public boolean isEmpty() {
        return Utils.isCollectionEmpty(toDataItem().getKeys());
    }

}
