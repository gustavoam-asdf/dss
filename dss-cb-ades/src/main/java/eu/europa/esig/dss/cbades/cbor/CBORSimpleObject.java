package eu.europa.esig.dss.cbades.cbor;

import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.NegativeInteger;
import co.nstant.in.cbor.model.SimpleValue;
import co.nstant.in.cbor.model.SimpleValueType;
import co.nstant.in.cbor.model.UnicodeString;
import co.nstant.in.cbor.model.UnsignedInteger;

/**
 * A wrapper for a one-dimensional CBOR object
 *
 */
public class CBORSimpleObject extends AbstractCBORObject<DataItem> {

    /**
     * Creates a new CBOR object from the given {@code dataItem}
     *
     * @param dataItem {@link DataItem}
     */
    public CBORSimpleObject(DataItem dataItem) {
        super(dataItem);
    }

    /**
     * Returns a {@code Long} value of the object, if supported
     *
     * @return {@link Long}
     */
    public Long getValueAsLong() {
        if (isUnsignedInteger()) {
            return ((UnsignedInteger) toDataItem()).getValue().longValue();
        } else if (isNegativeInteger()) {
            return ((NegativeInteger) toDataItem()).getValue().longValue();
        }
        return null;
    }

    /**
     * Returns a {@code String} value of the object, if supported
     *
     * @return {@link String}
     */
    public String getValueAsString() {
        if (isUnicodeString()) {
            return ((UnicodeString) toDataItem()).getString();
        }
        return null;
    }

    /**
     * Returns a {@code Boolean} value of the object, if supported
     *
     * @return {@link Boolean}
     */
    public Boolean getValueAsBoolean() {
        if (isBoolean()) {
            SimpleValueType simpleValueType = ((SimpleValue) toDataItem()).getSimpleValueType();
            switch (simpleValueType) {
                case TRUE:
                    return true;
                case FALSE:
                    return false;
                default:
                    return null;
            }
        }
        return null;
    }

}
