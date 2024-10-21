package eu.europa.esig.dss.cbades;

import java.util.Objects;

/**
 * Identifies the context of the signature. E.g. a COSE_Sign or COSE_Sign1 signature.
 *
 */
public enum COSESignatureContext {

    /** For signatures using the COSE_Signature structure */
    COSE_SIGN("Signature", "COSE_Sign", 98L),

    /** For signatures using the COSE_Sign1 structure */
    COSE_SIGN1("Signature1", "COSE_Sign1", 18L),

    /** For signatures using the COSE_Signature structure */
    COSE_SIGNATURE(null, null, null),

    /** For full counter-signatures */
    COSE_COUNTER_SIGNATURE("CounterSignature", "COSE_Countersignature", 19L, true, false, COSEConstants.COUNTER_SIGNATURE),

    /** For abbreviated counter-signatures0 */
    COSE_COUNTER_SIGNATURE0("CounterSignature0", "COSE_Countersignature0", null, true, false, COSEConstants.COUNTER_SIGNATURE0),

    /** For full counter-signatures with other_fields present */
    COSE_COUNTER_SIGNATURE_V2("CounterSignatureV2", "COSE_Countersignature_V2", 19L, true, true, COSEConstants.COUNTER_SIGNATURE_V2),

    /** For abbreviated counter-signatures0 with other_fields present */
    COSE_COUNTER_SIGNATURE0_V2("CounterSignature0V2", "COSE_Countersignature0_V2", null, true, true, COSEConstants.COUNTER_SIGNATURE0_V2);

    /** The context label used as a part of a DTBS computation */
    private final String context;

    /** Gets the COSE signature context's label as defined in RFC 8152 / RFC 9052 / RFC 9338 */
    private final String label;

    /** The tag label code of the corresponding structure */
    private final Long tag;

    /** Defines if the signature context corresponds to a counter signature */
    private final boolean counterSignature;

    /** Defines if the signature context corresponds to an RFC 9338 counter signature V2 */
    private final boolean counterSignatureV2;

    /** The key used to identify the counter signature header parameter */
    private final Long counterSignatureHeaderKey;

    /**
     * Default constructor
     *
     * @param context {@link String} context label
     * @param label {@link String} user-friendly label as defined in RFC
     * @param tag long value of the tag label key
     */
    COSESignatureContext(final String context, final String label, final Long tag) {
        this(context, label, tag, false, false, null);
    }

    /**
     * Default constructor
     *
     * @param context {@link String} context label
     * @param label {@link String} user-friendly label as defined in RFC
     * @param tag long value of the tag label key
     * @param counterSignature whether the context corresponds to the counter signature
     * @param counterSignatureV2 whether the context corresponds to the counter signature V2
     * @param counterSignatureHeaderKey {@link Long} key used to define a counter signature header parameter
     */
    COSESignatureContext(final String context, final String label, final Long tag, final boolean counterSignature,
                         final boolean counterSignatureV2, final Long counterSignatureHeaderKey) {
        this.context = context;
        this.label = label;
        this.tag = tag;
        this.counterSignature = counterSignature;
        this.counterSignatureV2 = counterSignatureV2;
        this.counterSignatureHeaderKey = counterSignatureHeaderKey;
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
     * Gets the RFC definition user-friendly label
     *
     * @return {@link String}
     */
    public String getLabel() {
        return label;
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

    /**
     * Gets the header key of unsigned property used to embed the counter signature in
     *
     * @return {@link Long}
     */
    public Long getCounterSignatureHeaderKey() {
        return counterSignatureHeaderKey;
    }

    /**
     * Gets the corresponding {@code COSESignatureContext} enum for the given {@code label}, if found
     *
     * @param label {@link String}
     * @return {@link COSESignatureContext}
     */
    public static COSESignatureContext forLabel(final String label) {
        Objects.requireNonNull(label, "Label string shall be defined!");
        for (COSESignatureContext coseSignatureContext : values()) {
            if (label.equals(coseSignatureContext.label)) {
                return coseSignatureContext;
            }
        }
        return null;
    }

    /**
     * This method returns a corresponding counter signature context based on the used header identifier
     *
     * @param headerKey {@link Long} the used identifier of the header enveloping the counter signature
     * @return {@link COSESignatureContext} when the header is known, NULL otherwise
     */
    public static COSESignatureContext getCounterSignatureContextByHeaderKey(Long headerKey) {
        Objects.requireNonNull(headerKey, "Header key shall be defined!");
        for (COSESignatureContext coseSignatureContext : values()) {
            if (headerKey.equals(coseSignatureContext.counterSignatureHeaderKey)) {
                return coseSignatureContext;
            }
        }
        return null;
    }

}
