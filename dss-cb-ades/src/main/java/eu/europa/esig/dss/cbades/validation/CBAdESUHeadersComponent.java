package eu.europa.esig.dss.cbades.validation;

import eu.europa.esig.dss.cbades.cbor.CBORObject;

/**
 * Represents an item of the 'uHeaders' header array
 *
 */
public class CBAdESUHeadersComponent extends CBAdESAttribute {

    /**
     * Default constructor
     *
     * @param headerId   {@link Long} header id
     * @param value      {@link CBORObject} value
     * @param identifier {@link CBAdESAttributeIdentifier}
     */
    public CBAdESUHeadersComponent(Long headerId, CBORObject value, CBAdESAttributeIdentifier identifier) {
        super(headerId, value);
        this.identifier = identifier;
    }

}
