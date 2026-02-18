package eu.europa.esig.dss.model.eaa.claim;

import java.util.Objects;

/**
 * Represents a Null encoded (selectively) disclosable claim
 *
 */
public class ClaimNull extends AbstractClaim {

    private static final long serialVersionUID = 6071033418783328062L;

    /**
     * Default constructor
     */
    public ClaimNull() {
        // empty
    }

    /**
     * Constructor with claim header name provided
     *
     * @param name {@link String}
     */
    public ClaimNull(final String name) {
        super(name);
    }

    /**
     * Constructor with claim name and selectively disclosable status provided
     *
     * @param name {@link String}
     * @param selectivelyDisclosable whether the claim is selectively disclosable
     *                               (can be TRUE only when the value of claim is provided in a form of disclosure)
     */
    public ClaimNull(final String name, final boolean selectivelyDisclosable) {
        super(name, selectivelyDisclosable);
    }

    @Override
    public String getValueAsString() {
        return "null";
    }

    @Override
    public boolean isNullValueType() {
        return true;
    }

    @Override
    public boolean isNullOrEmpty() {
        return true;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        return object != null && getClass() == object.getClass();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getClass());
    }

}
