package eu.europa.esig.dss.cbades;

public enum COSESignatureContext {

    COSE_SIGN("Signature", 98),

    COSE_SIGN1("Signature1", 18),

    COSE_COUNTER_SIGN("CounterSignature", 7);

    private final String context;

    private final long tag;

    COSESignatureContext(final String context, final long tag) {
        this.context = context;
        this.tag = tag;
    }

    public String getContext() {
        return context;
    }

    public long getTag() {
        return tag;
    }
}
