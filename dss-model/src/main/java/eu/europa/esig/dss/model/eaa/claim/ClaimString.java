package eu.europa.esig.dss.model.eaa.claim;

import java.util.Objects;

/**
 * Represents a String encoded (selectively) disclosable claim
 *
 */
public class ClaimString extends AbstractClaim {

    private static final long serialVersionUID = -4405160230836421331L;

    /** String value of the claim */
    private final String value;

    /**
     * Default constructor
     *
     * @param value {@link String} value of the claim
     */
    public ClaimString(final String value) {
        this(null, value);
    }

    /**
     * Constructor with claim header name provided
     *
     * @param name {@link String} claim header name
     * @param value {@link String} value of the claim
     */
    public ClaimString(final String name, final String value) {
        this(name, value, false);
    }

    /**
     * Constructor with claim name and selectively disclosable status provided
     *
     * @param name {@link String} claim header name
     * @param value {@link String} value of the claim
     * @param selectivelyDisclosable whether the claim is selectively disclosable
     *                               (can be TRUE only when the value of claim is provided in a form of disclosure)
     */
    public ClaimString(final String name, final String value, final boolean selectivelyDisclosable) {
        super(name, selectivelyDisclosable);
        this.value = value;
    }

    @Override
    public String getStringValue() {
        return value;
    }

    @Override
    public boolean isStringValueType() {
        return true;
    }

    @Override
    public String getValueAsString() {
        return value;
    }

    @Override
    public boolean isNullOrEmpty() {
        return value == null;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        ClaimString that = (ClaimString) object;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " {" +
                "'" + getName() + "'" + (isSelectivelyDisclosable() ? " (disclosed)" : "") + ": '" + value +
                "'}";
    }

}
