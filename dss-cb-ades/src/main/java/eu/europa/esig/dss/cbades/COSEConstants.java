package eu.europa.esig.dss.cbades;

/**
 * Utils class containing RFC 8152 and ETSI TS 119 152 constants
 *
 */
public final class COSEConstants {

    /** COSE_Sign. CBOR Tag '98' */
    public static final long COSE_SIGN_TAG = 98;

    /** COSE_Sign1. CBOR Tag '18' */
    public static final long COSE_SIGN1_TAG = 18;

    public static final long ALG = 1;

    /**
     * Singleton
     */
    private COSEConstants() {
        // empty
    }

}
