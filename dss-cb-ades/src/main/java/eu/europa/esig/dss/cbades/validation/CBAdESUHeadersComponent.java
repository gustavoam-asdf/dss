package eu.europa.esig.dss.cbades.validation;

import eu.europa.esig.dss.cbades.cbor.CBORMap;
import eu.europa.esig.dss.cbades.cbor.CBORObject;
import eu.europa.esig.dss.cbades.cbor.CBORUtils;

/**
 * Represents an item of the 'uHeaders' header array
 *
 */
public class CBAdESUHeadersComponent extends CBAdESAttribute {

    /** The component in its original representation */
    private final CBORObject component;

    /**
     * Default constructor
     *
     * @param component  {@link CBORObject} original representation of the component
     * @param headerId   {@link Long} header id
     * @param value      {@link CBORObject} value
     * @param identifier {@link CBAdESAttributeIdentifier}
     */
    CBAdESUHeadersComponent(CBORObject component, Long headerId, CBORObject value, CBAdESAttributeIdentifier identifier) {
        super(headerId, value);
        this.component = component;
        this.identifier = identifier;
    }

    /**
     * Builds {@code CBAdESUHeadersComponent} from the 'uHeaders' array entry
     *
     * @param component represents the component of the 'uHeaders' array
     * @param order defines the position number of the component in the 'uHeaders' array
     * @return {@link CBAdESUHeadersComponent}
     */
    public static CBAdESUHeadersComponent build(CBORObject component, int order) {
        CBORMap cborMap = CBORUtils.parseUHeadersEntry(component);
        if (cborMap != null && !cborMap.isEmpty()) {
            // one entry is expected
            Long key = cborMap.getKeys().iterator().next();
            CBORObject value = cborMap.getHeader(key);
            CBAdESAttributeIdentifier identifier = CBAdESAttributeIdentifier.build(key, value, order);
            return new CBAdESUHeadersComponent(component, key, value, identifier);
        }
        return null;
    }

}
