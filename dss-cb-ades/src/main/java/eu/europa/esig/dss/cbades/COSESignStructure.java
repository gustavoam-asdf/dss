package eu.europa.esig.dss.cbades;

import eu.europa.esig.dss.cbades.cbor.CBORObject;

/**
 * Represents a common interface for COSE signature structure objects defined in RFC 9052 "4. Signing Objects"
 */
public interface COSESignStructure extends COSEStructure {

    /**
     * Gets whether the signature structure is encoded as tagged
     *
     * @return TRUE if the signature structure is encoded as tagged, FALSE in case of untagged
     */
    boolean isTagged();

    /**
     * Gets the content to be signed.
     * Instantiates a nil value, when absent.
     *
     * @return {@link CBORObject}
     */
    CBORObject getPayload();

}
