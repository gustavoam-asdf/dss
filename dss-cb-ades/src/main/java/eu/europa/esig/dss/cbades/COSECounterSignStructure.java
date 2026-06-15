package eu.europa.esig.dss.cbades;

import eu.europa.esig.dss.cbades.cbor.CBORObject;

/**
 * Represents a common interface for COSE counter-signature structure objects defined in RFC 9338
 *
 */
public interface COSECounterSignStructure extends COSEStructure {

    /**
     * Gets whether the signature structure is encoded as tagged
     *
     * @return TRUE if the signature structure is encoded as tagged, FALSE in case of untagged
     */
    boolean isTagged();

    /**
     * Gets the master signature structure
     *
     * @return {@link COSEStructure}
     */
    COSEStructure getMasterSignature();

    /**
     * Serialized the current COSE counter signature structure to a CBOR Object
     *
     * @return {@link CBORObject}
     */
    CBORObject toCBORObject();

}
