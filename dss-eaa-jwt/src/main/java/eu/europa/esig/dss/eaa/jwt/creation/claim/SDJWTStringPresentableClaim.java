package eu.europa.esig.dss.eaa.jwt.creation.claim;

/**
 * Represents a String claim
 */
public class SDJWTStringPresentableClaim extends SDJWTPresentableClaim {

    private static final long serialVersionUID = 1L;

    private final String value;

    /**
     * Constructor
     *
     * @param value the claim value
     */
    public SDJWTStringPresentableClaim(final String value) {
        this(null, value, false, null);
    }

    /**
     * Constructor
     *
     * @param name  {@link String} the claim name
     * @param value the claim value
     */
    public SDJWTStringPresentableClaim(final String name, final String value) {
        this(name, value, false, null);
    }

    /**
     * Constructor
     *
     * @param name  {@link String} the claim name
     * @param value the claim value
     * @param selectivelyDisclosable whether the claim is selectively disclosable
     * @param salt {@link String} the salt (mandatory if the claim is selectively disclosable)
     */
    public SDJWTStringPresentableClaim(final String name, final String value, final boolean selectivelyDisclosable, final String salt) {
        super(name, selectivelyDisclosable, salt);
        this.value = value;
    }

    /**
     * Gets the claim value
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

    @Override
    public String getValueAsString() {
        return getValue();
    }
}

