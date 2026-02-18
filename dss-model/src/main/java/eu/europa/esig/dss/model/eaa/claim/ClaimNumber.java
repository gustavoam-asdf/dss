package eu.europa.esig.dss.model.eaa.claim;

import java.util.Objects;

/**
 * Represents a Number encoded (selectively) disclosable claim
 *
 */
public class ClaimNumber extends AbstractClaim {

    private static final long serialVersionUID = 6284783236982686257L;

    /** Number value of the claim */
    private final Number value;

    /**
     * Default constructor
     *
     * @param value {@link String} value of the claim
     */
    public ClaimNumber(final Number value) {
        this(null, value);
    }

    /**
     * Constructor with claim name provided
     *
     * @param name {@link String} claim header name
     * @param value {@link String} value of the claim
     */
    public ClaimNumber(final String name, final Number value) {
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
    public ClaimNumber(final String name, final Number value, final boolean selectivelyDisclosable) {
        super(name, selectivelyDisclosable);
        this.value = value;
    }

    @Override
    public Number getNumberValue() {
        return value;
    }

    @Override
    public boolean isNumberValueType() {
        return true;
    }

    @Override
    public String getValueAsString() {
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    @Override
    public boolean isNullOrEmpty() {
        return value == null;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        ClaimNumber that = (ClaimNumber) object;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

}
