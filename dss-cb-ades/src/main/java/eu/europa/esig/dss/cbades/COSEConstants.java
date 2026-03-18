package eu.europa.esig.dss.cbades;

/**
 * Utils class containing RFC 9052 and ETSI TS 119 152 constants
 *
 */
public final class COSEConstants {

    /* RFC 9052 COSE structure */

    /** COSE_Sign. CBOR Tag '98' */
    public static final long COSE_SIGN_TAG = 98;

    /** COSE_Sign1. CBOR Tag '18' */
    public static final long COSE_SIGN1_TAG = 18;

    /* RFC 9338 Header parameters */

    /** COSE_Countersignature. CBOR Tag '19' */
    public static final long COSE_COUNTERSIGNATURE_TAG = 19;

    /* Subtype keys */

    /** NotCertifiedItem mediaType: String identifying the type of claimed attributes or signed assertions. Array position '0' */
    public static final int NOT_CERTIFIED_ITEM_MEDIA_TYPE = 0;

    /** NotCertifiedItem encoding: String identifying the encoding of claimed attributes or signed assertions. Array position '1' */
    public static final int NOT_CERTIFIED_ITEM_ENCODING = 1;

    /** NotCertifiedItem qVals: Array with the claimed attributes or signed assertions. Array position '2' */
    public static final int NOT_CERTIFIED_ITEM_QVALS = 2;

    /** DigAlgVal hashAlg. Array position '0' */
    public static final int DIG_ALG_VAL_HASH_ALG = 0;

    /** DigAlgVal hashValue. Array position '1' */
    public static final int DIG_ALG_VAL_HASH_VALUE = 1;

    /** COSE_CertHash hashAlg. Array position '0' */
    public static final int COSE_CERT_HASH_ALG = 0;

    /** COSE_CertHash hashValue. Array position '1' */
    public static final int COSE_CERT_HASH_VALUE = 1;

    /**
     * Singleton
     */
    private COSEConstants() {
        // empty
    }

}
