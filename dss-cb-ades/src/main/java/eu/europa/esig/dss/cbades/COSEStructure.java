package eu.europa.esig.dss.cbades;

/**
 * Represents a shared interface for COSE signature structure objects defined in RFC 9052 and
 * COSE counter signatures defined in RFC 9338
 */
public interface COSEStructure {

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
