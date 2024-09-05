package eu.europa.esig.dss.cbades;

/**
 * Identifies the context of the signature. E.g. a COSE_Sign or COSE_Sign1 signature.
 *
 */
public enum COSESignatureContext {

    /** For signatures using the COSE_Signature structure */
    COSE_SIGN("Signature", 98),

    /** For signatures using the COSE_Sign1 structure */
    COSE_SIGN1("Signature1", 18),

    /** For counter-signatures */
    COSE_COUNTER_SIGN("CounterSignature", 7);

    /** The context label used as a part of a DTBS computation */
    private final String context;

    /** The tag label code of the corresponding structure */
    private final long tag;

    /**
     * Default constructor
     *
     * @param context {@link String} context label
     * @param tag long value of the tag label key
     */
    COSESignatureContext(final String context, final long tag) {
        this.context = context;
        this.tag = tag;
    }

    /**
     * Gets the context text string identifying the context of the signature.
     * The value is used for DTBS computation.
     *
     * @return {@link String}
     */
    public String getContext() {
        return context;
    }

    /**
     * Gets the tag of the corresponding signature structure
     *
     * @return long value of the tag label key
     */
    public long getTag() {
        return tag;
    }

}
