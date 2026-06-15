package eu.europa.esig.dss.enumerations;

/**
 * Defines a list of format types on which supported EAA presentations may be based.
 * NOTE: This type relates to a format of an EAA presentation document.
 *
 */
public enum EAAPresentationType {

    /**
     * Represents an IETF RFC 9901 "Selective Disclosure for JSON Web Tokens" token.
     */
    SD_JWT,

    /**
     * Represents a DeviceResponse mdoc structure as per ISO/IEC 18013-5 "8.3.2.1.2.2 Device retrieval mdoc response"
     */
    MDOC_DEVICE_RESPONSE,

    /**
     * Represents an IssuerSigned mdoc structure as per ISO/IEC 18013-5 "8.3.2.1.2.2 Device retrieval mdoc response"
     */
    MDOC_ISSUER_SIGNED,

    /**
     * Represents a JOSE token, as defined in IETF RFC 7515 "JSON Web Signature (JWS)"
     */
    JWS,

    /**
     * Realization of EAA based on X.509 Attribute certificates as specified in IETF RFC 5755.
     */
    X509_AC

}
