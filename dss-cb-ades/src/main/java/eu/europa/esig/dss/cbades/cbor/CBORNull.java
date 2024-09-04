package eu.europa.esig.dss.cbades.cbor;

import co.nstant.in.cbor.model.SimpleValue;
import co.nstant.in.cbor.model.SimpleValueType;

/**
 * Identifies a nullable CBOR value
 */
public class CBORNull extends AbstractCBORObject {

    /**
     * Creates a new CBOR nullable object
     */
    public CBORNull() {
        super(new SimpleValue(SimpleValueType.NULL));
    }

    /**
     * Creates a new CBOR nullable object from the given {@code simpleValue}
     *
     * @param simpleValue {@link SimpleValue}
     */
    public CBORNull(SimpleValue simpleValue) {
        super(simpleValue);
    }

}
