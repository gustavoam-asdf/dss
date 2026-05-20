package eu.europa.esig.dss.eaa.jwt.creation;

import eu.europa.esig.dss.eaa.common.creation.claim.AbstractEAAClaim;

/**
 * Implementation of an EAA SD-JWT Claim
 */
public class SDJWTEAAClaim extends AbstractEAAClaim {

    private static final long serialVersionUID = 4900197826207151947L;

    /** Identifies whether the claim is selectively disclosable */
    private final boolean selectivelyDisclosable;

    /** Salt of the selectively disclosable claim, when applicable */
    private final String salt;

    /**
     * Constructor with the claim  value
     *
     * @param value {@link Object} the value of the claim
     */
    public SDJWTEAAClaim(final Object value) {
        this(null, value);
    }

    /**
     * Constructor with the claim name and value
     *
     * @param name {@link String} the claim name
     * @param value {@link Object} the value of the claim
     */
    public SDJWTEAAClaim(final String name, final Object value) {
        this(name, value, false);
    }

    /**
     * Constructor with the claim name, value, selectively disclosable status.
     * When the selectivelyDisclosable status is enabled but no salt is provided,
     * the salt will be generated during the EAA Payload computation.
     *
     * @param name {@link String} the claim name
     * @param value {@link Object} the value of the claim
     * @param selectivelyDisclosable whether the claim is selectively disclosable
     */
    public SDJWTEAAClaim(final String name, final Object value, final boolean selectivelyDisclosable) {
        this(name, value, selectivelyDisclosable, null);
    }

    /**
     * Constructor with the claim name, value, selectively disclosable status and salt provided
     *
     * @param name {@link String} the claim name
     * @param value {@link Object} the value of the claim
     * @param selectivelyDisclosable whether the claim is selectively disclosable
     * @param salt {@link String} the salt (mandatory if the claim is selectively disclosable)
     */
    public SDJWTEAAClaim(final String name, final Object value, final boolean selectivelyDisclosable, final String salt) {
        super(name, value);
        this.selectivelyDisclosable = selectivelyDisclosable;
        this.salt = salt;
    }

    /**
     * Gets whether this claim is selectively disclosable
     *
     * @return whether the claim is disclosable
     */
    public boolean isSelectivelyDisclosable() {
        return selectivelyDisclosable;
    }

    /**
     * Gets the salt
     *
     * @return {@link String}
     */
    public String getSalt() {
        return salt;
    }

    @Override
    public String toString() {
        return "SDJWTEAAClaim [" +
                "name='" + getName() + '\'' +
                ", value=" + getValue() +
                ", selectivelyDisclosable=" + selectivelyDisclosable +
                ", salt='" + salt + '\'' +
                "]";
    }

}
