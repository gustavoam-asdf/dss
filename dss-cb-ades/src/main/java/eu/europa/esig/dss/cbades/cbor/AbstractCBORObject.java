package eu.europa.esig.dss.cbades.cbor;

import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.MajorType;
import co.nstant.in.cbor.model.SimpleValue;
import co.nstant.in.cbor.model.SimpleValueType;
import co.nstant.in.cbor.model.Tag;

/**
 * An abstract implementation of a CBOR object wrapper
 *
 */
public abstract class AbstractCBORObject implements CBORObject {

    /** A CBOR object implementation */
    private final DataItem dataItem;

    /**
     * Creates a new CBOR object from the given {@code dataItem}
     *
     * @param dataItem {@link DataItem}
     */
    protected AbstractCBORObject(final DataItem dataItem) {
        this.dataItem = dataItem;
    }

    @Override
    public boolean isTagged() {
        return dataItem.hasTag();
    }

    @Override
    public CBORTag getTag() {
        return new CBORTag(dataItem.getTag());
    }

    @Override
    public void setTag(long value) {
        dataItem.setTag(new Tag(value));
    }

    @Override
    public DataItem toDataItem() {
        return dataItem;
    }

    @Override
    public boolean isUnsignedInteger() {
        return MajorType.UNSIGNED_INTEGER == dataItem.getMajorType();
    }

    @Override
    public boolean isNegativeInteger() {
        return MajorType.NEGATIVE_INTEGER == dataItem.getMajorType();
    }

    @Override
    public boolean isByteString() {
        return MajorType.BYTE_STRING == dataItem.getMajorType();
    }

    @Override
    public boolean isUnicodeString() {
        return MajorType.UNICODE_STRING == dataItem.getMajorType();
    }

    @Override
    public boolean isBoolean() {
        if (isSimpleValue()) {
            SimpleValue simpleValue = (SimpleValue) dataItem;
            return SimpleValueType.TRUE == simpleValue.getSimpleValueType() || SimpleValueType.FALSE == simpleValue.getSimpleValueType();
        }
        return false;
    }

    @Override
    public boolean isNull() {
        if (isSimpleValue()) {
            SimpleValue simpleValue = (SimpleValue) dataItem;
            return SimpleValueType.NULL == simpleValue.getSimpleValueType();
        }
        return false;
    }

    private boolean isSimpleValue() {
        return MajorType.SPECIAL == dataItem.getMajorType() && dataItem instanceof SimpleValue;
    }

    @Override
    public boolean isMap() {
        return MajorType.MAP == dataItem.getMajorType();
    }

    @Override
    public boolean isArray() {
        return MajorType.ARRAY == dataItem.getMajorType();
    }

}
