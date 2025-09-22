package eu.europa.esig.dss.cbades.validation;

import eu.europa.esig.dss.cbades.cbor.CBORMap;
import eu.europa.esig.dss.cbades.cbor.CBORObject;
import eu.europa.esig.dss.cbades.cbor.CBORUtils;

/**
 * Represents an item of the 'uHeaders' header array
 *
 */
public class CBAdESUHeadersComponent extends CBAdESAttribute {

    private static final long serialVersionUID = 468968604999660231L;

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

    /**
     * Builds the {@code EtsiUComponent} from the given parameters
     *
     * @param headerKey {@link Long} name of the 'uHeaders' array component
     * @param value {@link CBORObject} represents the value of the component
     * @param base64UrlEncoded defines if the components is stored in base64url encoding
     * @param identifier {@link CBAdESAttributeIdentifier}
     * @return {@link CBAdESUHeadersComponent}
     */
    public static CBAdESUHeadersComponent build(Long headerKey, CBORObject value, boolean base64UrlEncoded,
                                                CBAdESAttributeIdentifier identifier) {
        CBORObject component = createUHeadersComponent(headerKey, value, base64UrlEncoded);
        return new CBAdESUHeadersComponent(component, headerKey, value, identifier);
    }

    /**
     * Returns an 'uHeaders' component in the defined representation
     *
     * @param key              {@link Long} header name
     * @param value            {@link CBORObject} object
     * @param cborBtsrWrapped  TRUE if base64Url encoded representation, FALSE otherwise
     * @return {@link CBORObject} 'uHeaders' component
     */
    private static CBORObject createUHeadersComponent(Long key, CBORObject value, boolean cborBtsrWrapped) {
        CBORMap cborMap = new CBORMap();
        cborMap.put(key, value);
        return cborBtsrWrapped ? cborMap.getByteString() : cborMap;
    }

    /**
     * Gets the current component in {@code CBORObject} representation
     *
     * @return {@link CBORObject}
     */
    public CBORObject getComponent() {
        return component;
    }

    /**
     * Gets if the component is CBOR Byte String encoded
     *
     * @return TRUE if the component is CBOR Byte String encoded, FALSE otherwise
     */
    public boolean isCborBtsrWrapped() {
        return component.isByteString();
    }

}
