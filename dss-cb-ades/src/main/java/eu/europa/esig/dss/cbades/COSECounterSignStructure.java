package eu.europa.esig.dss.cbades;

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

}
