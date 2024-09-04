package eu.europa.esig.dss.cbades;

/**
 * Represents a common interface for COSE signature structure objects defined in RFC 9052 "4. Signing Objects"
 */
public interface COSESignStructure {

    /**
     * Builds an RFC 9052 COSE signature structure
     *
     * @return serialized byte array
     */
    byte[] serialize();

    /**
     * Gets the COSE signature context
     *
     * @return {@link COSESignatureContext}
     */
    COSESignatureContext getContext();

}
