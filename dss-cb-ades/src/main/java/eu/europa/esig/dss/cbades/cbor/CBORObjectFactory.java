package eu.europa.esig.dss.cbades.cbor;

import co.nstant.in.cbor.model.ByteString;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.DoublePrecisionFloat;
import co.nstant.in.cbor.model.NegativeInteger;
import co.nstant.in.cbor.model.SimpleValue;
import co.nstant.in.cbor.model.SimpleValueType;
import co.nstant.in.cbor.model.SinglePrecisionFloat;
import co.nstant.in.cbor.model.UnicodeString;
import co.nstant.in.cbor.model.UnsignedInteger;

/**
 * Factory to create a {@code eu.europa.esig.dss.cbades.cbor.CBORObject} from a value
 *
 */
public class CBORObjectFactory {

    /**
     * Default constructor
     */
    private CBORObjectFactory() {
        // empty
    }

    /**
     * Instantiates a new CBOR object from the given {@code DataItem}
     *
     * @param object {@link DataItem}
     * @return {@link CBORObject}
     */
    public static CBORObject toCBORObject(Object object) {
        if (object == null) {
            return null;
        }
        DataItem dataItem;
        if (object instanceof DataItem) {
            dataItem = (DataItem) object;
        } else {
            dataItem = toDataItem(object);
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

        } else if (object instanceof Double) {
            double doubleNumber = (Double) object;
            return new DoublePrecisionFloat(doubleNumber);

        } else if (object instanceof Float) {
            float floatNumber = (Float) object;
            return new SinglePrecisionFloat(floatNumber);

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

}
