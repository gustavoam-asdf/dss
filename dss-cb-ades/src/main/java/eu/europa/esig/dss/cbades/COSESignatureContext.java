package eu.europa.esig.dss.cbades;

import java.util.Objects;

/**
 * Identifies the context of the signature. E.g. a COSE_Sign or COSE_Sign1 signature.
 *
 */
public enum COSESignatureContext {

    /** For signatures using the COSE_Signature structure */
    COSE_SIGN("Signature", 98L),

    /** For signatures using the COSE_Sign1 structure */
    COSE_SIGN1("Signature1", 18L),

    /** For signatures using the COSE_Signature structure */
    COSE_SIGNATURE(null, null),

    /** For full counter-signatures */
    COSE_COUNTER_SIGNATURE("CounterSignature", 19L, true, false),

    /** For abbreviated counter-signatures0 */
    COSE_COUNTER_SIGNATURE0("CounterSignature0", null, true, false),

    /** For full counter-signatures with other_fields present */
    COSE_COUNTER_SIGNATURE_V2("CounterSignatureV2", 19L, true, true),

    /** For abbreviated counter-signatures0 with other_fields present */
    COSE_COUNTER_SIGNATURE0_V2("CounterSignature0V2", null, true, true);

    /** The context label used as a part of a DTBS computation */
    private final String context;

    /** The tag label code of the corresponding structure */
    private final Long tag;

    /** Defines if the signature context corresponds to a counter signature */
    private final boolean counterSignature;

    /** Defines if the signature context corresponds to an RFC 9338 counter signature V2 */
    private final boolean counterSignatureV2;

    /**
     * Default constructor
     *
     * @param context {@link String} context label
     * @param tag long value of the tag label key
     */
    COSESignatureContext(final String context, final Long tag) {
        this(context, tag, false, false);
    }

    /**
     * Default constructor
     *
     * @param context {@link String} context label
     * @param tag long value of the tag label key
     * @param counterSignature whether the context corresponds to the counter signature
     * @param counterSignatureV2 whether the context corresponds to the counter signature V2
     */
    COSESignatureContext(final String context, final Long tag, final boolean counterSignature, final boolean counterSignatureV2) {
        this.context = context;
        this.tag = tag;
        this.counterSignature = counterSignature;
        this.counterSignatureV2 = counterSignatureV2;
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
        Objects.requireNonNull(tag, String.format("The tag is not available for COSESignatureContext '%s'", this.name()));
        return tag;
    }

    /**
     * Gets if the context corresponds to a counter signature
     *
     * @return TRUE if the context corresponds to a counter signature type
     */
    public boolean isCounterSignature() {
        return counterSignature;
    }

    /**
     * Gets if the context corresponds to an RFC 9338 counter signature V2
     *
     * @return TRUE if the context corresponds to a RFC 9338 counter signature V2 type
     */
    public boolean isCounterSignatureV2() {
        return counterSignatureV2;
    }

}
