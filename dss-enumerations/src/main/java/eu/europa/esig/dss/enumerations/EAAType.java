package eu.europa.esig.dss.enumerations;

/**
 * Defines a list of EAA types known by the current implementation.
 * NOTE: This type relates to a format of an EAA signed token.
 *
 */
public enum EAAType {

    /**
     * Realization of EAA that implements EAA as a JSON Web Signature as specified in IETF RFC 7515,
     * built on IETF SD-JWT VC, which further profiles a Selective Disclosure JSON Web Token as
     * specified in IETF RFC 9901 "Selective Disclosure for JSON Web Tokens".
     */
    SD_JWT_VC,

    /**
     * Realization of EAA that implements EAA built on the data structures defined in ISO/IEC 18013-5.
     */
    ISO_IEC_MDOC,

    /**
     * Realization of EAA based on the JSON-LD (specified in W3C Recommendation:
     * "JSON-LD 1.1. A JSON-based Serialization for Linked Data") serialization of W3C
     * Recommendation (15 May 2025): "Verifiable Credentials Data Model v2.0".
     */
    W3C_VC,

    /**
     * Realization of EAA based on X.509 Attribute certificates as specified in IETF RFC 5755.
     */
    X509_AC

}
