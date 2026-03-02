package eu.europa.esig.dss.model.eaa.claim;

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

    /**
     * Constructor with claim name and selectively disclosable status and a parent claim provided
     *
     * @param name {@link String}
     * @param selectivelyDisclosable whether the claim is selectively disclosable
     *                               (can be TRUE only when the value of claim is provided in a form of disclosure)
     * @param parent {@link Claim} representing the parent claim, when applicable
     */
    public ClaimNull(final String name, final boolean selectivelyDisclosable, final Claim parent) {
        super(name, selectivelyDisclosable, parent);
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

}
