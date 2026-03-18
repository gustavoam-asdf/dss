package eu.europa.esig.dss.cbades.validation;

import eu.europa.esig.dss.cbades.cbor.CBORObject;
import eu.europa.esig.dss.spi.validation.SignatureAttribute;

import java.util.Objects;

/**
 * Represents the CB-AdES header
 *
 */
public class CBAdESAttribute implements SignatureAttribute {

    private static final long serialVersionUID = 4718268120160172246L;

    /** Id of the header */
    protected CBORObject headerId;

    /** The header's value */
    protected CBORObject value;

    /** Identifies the instance */
    protected CBAdESAttributeIdentifier identifier;

    /**
     * Default constructor
     *
     * @param headerId {@link CBORObject} header id
     * @param value {@link CBORObject} value
     */
    public CBAdESAttribute(CBORObject headerId, CBORObject value) {
        this.headerId = headerId;
        this.value = value;
    }

    /**
     * Gets the header's id
     *
     * @return {@link CBORObject}
     */
    public CBORObject getHeaderId() {
        return headerId;
    }

    /**
     * Gets the value
     *
     * @return value
     */
    public CBORObject getValue() {
        return value;
    }

    /**
     * Gets the attribute identifier
     *
     * @return {@link CBAdESAttributeIdentifier}
     */
    @Override
    public CBAdESAttributeIdentifier getIdentifier() {
        if (identifier == null) {
            identifier = CBAdESAttributeIdentifier.build(headerId, value);
        }
        return identifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CBAdESAttribute that = (CBAdESAttribute) o;

        return Objects.equals(getIdentifier(), that.getIdentifier());
    }

    @Override
    public int hashCode() {
        return getIdentifier() != null ? getIdentifier().hashCode() : 0;
    }

}
