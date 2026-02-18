package eu.europa.esig.dss.model.eaa.claim;

import java.util.Objects;

/**
 * Represents a Boolean encoded (selectively) disclosable claim
 *
 */
public class ClaimBoolean extends AbstractClaim {

    private static final long serialVersionUID = 6071033418783328062L;

    /** Boolean value of the claim */
    private final Boolean value;

    /**
     * Default constructor
     *
     * @param value {@link Boolean} value of the claim
     */
    public ClaimBoolean(final Boolean value) {
        this(null, value);
    }

    /**
     * Constructor with claim name provided
     *
     * @param name {@link String} claim header name
     * @param value {@link Boolean} value of the claim
     */
    public ClaimBoolean(final String name, final Boolean value) {
        this(name, value, false);
    }

    /**
     * Constructor with claim name and selectively disclosable status provided
     *
     * @param name {@link String} claim header name
     * @param value {@link Boolean} value of the claim
     * @param selectivelyDisclosable whether the claim is selectively disclosable
     *                               (can be TRUE only when the value of claim is provided in a form of disclosure)
     */
    public ClaimBoolean(final String name, final Boolean value, final boolean selectivelyDisclosable) {
        super(name, selectivelyDisclosable);
        this.value = value;
    }

    @Override
    public Boolean getBooleanValue() {
        return value;
    }

    @Override
    public boolean isBooleanValueType() {
        return true;
    }

    @Override
    public String getValueAsString() {
        return value != null ? value.toString() : null;
    }

    @Override
    public boolean isNullOrEmpty() {
        return value == null;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        ClaimBoolean that = (ClaimBoolean) object;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

}
