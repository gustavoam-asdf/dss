package eu.europa.esig.dss.cbades.cbor;

import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.ByteString;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.NegativeInteger;
import co.nstant.in.cbor.model.SimpleValue;
import co.nstant.in.cbor.model.SimpleValueType;
import co.nstant.in.cbor.model.Tag;
import co.nstant.in.cbor.model.UnicodeString;
import co.nstant.in.cbor.model.UnsignedInteger;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Contains common util methods for working with CBOR content
 *
 */
public final class CBORUtils {

    /** An empty btsr value */
    public static final ByteString EMPTY_BYTE_STRING = new ByteString(DSSUtils.EMPTY_BYTE_ARRAY);

    /** An empty map value */
    public static final co.nstant.in.cbor.model.Map EMPTY_MAP = new co.nstant.in.cbor.model.Map();

    /** An empty btsr serialized map */
    public static final ByteString EMPTY_SERIALIZED_MAP;

    /** The binary content encoding (RFC 2045) */
    public static final String CONTENT_ENCODING_BINARY = "binary";

    static {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            co.nstant.in.cbor.model.Map map = new co.nstant.in.cbor.model.Map();
            CborEncoder cborEncoder = new CborEncoder(baos);
            cborEncoder.encode(map);
            EMPTY_SERIALIZED_MAP = new ByteString(baos.toByteArray());
        } catch (IOException | CborException e) {
            throw new DSSException(String.format("An error occurred : %s", e.getMessage()), e);
        }
    }

    /**
     * Utils class
     */
    private CBORUtils() {
        // empty
    }

    /**
     * This method creates a Tag object from the given Long value
     *
     * @param longValue {@link Long} value to convert
     * @return {@link Tag}
     */
    public static Tag toTag(Long longValue) {
        if (longValue == null) {
            return null;
        }
        return new Tag(longValue);
    }

    /**
     * Instantiates a new CBOR object from the given {@code DataItem}
     *
     * @param dataItem {@link DataItem}
     * @return {@link CBORObject}
     */
    public static CBORObject toCBORObject(DataItem dataItem) {
        if (dataItem == null) {
            return null;
        }
        switch (dataItem.getMajorType()) {
            case MAP:
                return new CBORMap((co.nstant.in.cbor.model.Map) dataItem);
            case ARRAY:
                return new CBORArray((co.nstant.in.cbor.model.Array) dataItem);
            case BYTE_STRING:
                return new CBORByteString((co.nstant.in.cbor.model.ByteString) dataItem);
            case TAG:
                return new CBORTag((co.nstant.in.cbor.model.Tag) dataItem);
            case SPECIAL:
                if (dataItem instanceof SimpleValue) {
                    SimpleValue simpleValue = (SimpleValue) dataItem;
                    if (SimpleValueType.NULL == simpleValue.getSimpleValueType()) {
                        return new CBORNull(simpleValue);
                    }
                }
            default:
                return new CBORSimpleObject(dataItem);
        }
    }

    /**
     * This method coverts the given object to a DataItem instance, corresponding to the object's format
     *
     * @param object to be converted
     */
    public static DataItem toDataItem(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof DataItem) {
            return (DataItem) object;

        } else if (object instanceof CBORObject) {
            CBORObject cborObject = (CBORObject) object;
            return cborObject.toDataItem();

        } else if (object instanceof Long) {
            long longNumber = (Long) object;
            return longNumber > 0 ? new UnsignedInteger(longNumber) : new NegativeInteger(longNumber);

        } else if (object instanceof String) {
            String str = (String) object;
            return new UnicodeString(str);

        } else if (object instanceof byte[]) {
            byte[] byteArray = (byte[]) object;
            return new ByteString(byteArray);

        } else if (object instanceof Boolean) {
            boolean value = (boolean) object;
            SimpleValueType simpleValueType = value ? SimpleValueType.TRUE : SimpleValueType.FALSE;
            return new SimpleValue(simpleValueType);

        } else {
            throw new UnsupportedOperationException(
                    String.format("The object of class '%s' is not yet supported!", object.getClass().getName()));
        }
    }

    /**
     * This method parses a {@code DSSDocument} and returns a {@code CBORObject},
     * representing the CBOR object structure
     *
     * @param document {@link DSSDocument} to parse
     * @return {@link CBORObject}
     * @throws CborException if an error occurs on document parsing
     */
    public static CBORObject parseCbor(DSSDocument document) throws CborException {
        try (InputStream is = document.openStream()) {
            CborDecoder cborDecoder = new CborDecoder(is);
            List<DataItem> dataItems = cborDecoder.decode();
            if (Utils.collectionSize(dataItems) == 1) {
                return toCBORObject(dataItems.iterator().next());
            } else {
                return new CBORArray(dataItems);
            }
        } catch (IOException e) {
            throw new DSSException(String.format("Unable to read document with name '%s' : %s", document.getName(), e.getMessage()), e);
        }
    }

    /**
     * This method parses a byte array and returns a list of {@code DataItem}s,
     * representing the CBOR object structure
     *
     * @param bytes byte array to parse
     * @return a list of {@link DataItem}s
     * @throws CborException if an error occurs on byte array parsing
     */
    public static List<DataItem> parseCbor(byte[] bytes) throws CborException {
        return CborDecoder.decode(bytes);
    }

    /**
     * Checks if the given document is of CBOR format
     *
     * @param document {@link DSSDocument} to check
     * @return TRUE if the document is CBOR, FALSE otherwise
     */
    public static boolean isCbor(DSSDocument document) {
        try (InputStream is = document.openStream()) {
            return new CborDecoder(is).decode() != null;
        } catch (CborException e) {
            return false;
        } catch (IOException e) {
            throw new DSSException(String.format("Unable to read document with name '%s' : %s", document.getName(), e.getMessage()), e);
        }
    }

    /**
     * Serialized a given CBOR object to a byte array
     *
     * @param cborObject {@link CBORObject} to be serialized
     * @return serialized byte array
     */
    public static byte[] serializeCborObject(CBORObject cborObject) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            CborEncoder cborEncoder = new CborEncoder(baos);
            cborEncoder.encode(cborObject.toDataItem());
            return baos.toByteArray();
        } catch (CborException | IOException e) {
            throw new DSSException(String.format("Unable to serialize CBOR object : %s", e.getMessage()), e);
        }
    }

}
