package eu.europa.esig.dss.cbades.validation;

import eu.europa.esig.dss.cbades.cbor.CBORObject;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.spi.validation.identifier.SignatureAttributeIdentifier;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Represents an identifier of a CB-AdES Attribute (or 'uHeaders' component)
 *
 */
public class CBAdESAttributeIdentifier extends SignatureAttributeIdentifier {

    private static final long serialVersionUID = -1421464221784448021L;

    /**
     * Default constructor
     *
     * @param data byte array
     */
    CBAdESAttributeIdentifier(byte[] data) {
        super(data);
    }

    /**
     * Builds a CB-AdES Attribute identifier
     *
     * @param headerId {@link Long} id of the 'uHeaders' component
     * @param value {@link CBORObject} represent the value of the 'uHeaders' component
     * @return {@link CBAdESAttributeIdentifier}
     */
    public static CBAdESAttributeIdentifier build(Long headerId, CBORObject value) {
        return build(headerId, value, null);
    }

    /**
     * Builds the identifier for an 'uHeaders' component
     *
     * @param headerId {@link Long} id of the 'uHeaders' component
     * @param value {@link CBORObject} represent the value of the 'uHeaders' component
     * @param order the order of the component within the 'uHeaders' array
     * @return {@link CBAdESAttributeIdentifier}
     */
    public static CBAdESAttributeIdentifier build(Long headerId, CBORObject value, Integer order) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); DataOutputStream dos = new DataOutputStream(baos)) {
            if (headerId != null) {
                dos.writeLong(headerId);
            }
            if (value != null) {
                dos.writeChars(value.toString());
            }
            if (order != null) {
                dos.writeInt(order);
            }
            dos.flush();

            return new CBAdESAttributeIdentifier(baos.toByteArray());

        } catch (IOException e) {
            throw new DSSException(String.format("Unable to build a CBAdESAttributeIdentifier. Reason : %s", e.getMessage()), e);
        }
    }

}
