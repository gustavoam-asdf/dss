package eu.europa.esig.dss.eaa.jwt.creation;

import java.util.Objects;

import eu.europa.esig.dss.eaa.common.creation.claim.AbstractEAAClaim;

/**
 * Implementation of an EAA SD-JWT Claim
 */
public class SDJWTEAAClaim extends AbstractEAAClaim {

    private final boolean selectivelyDisclosable;

    private final String salt;

    /**
     * Constructor with the claim  value
     *
     * @param value {@link Object} the value of the claim
     */
    public SDJWTEAAClaim(final Object value) {
        this(null, value, false, null);
    }

    /**
     * Constructor with the claim name and value
     *
     * @param name {@link String} the claim name
     * @param value {@link Object} the value of the claim
     */
    public SDJWTEAAClaim(final String name, final Object value) {
        this(name, value, false, null);
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

        if (selectivelyDisclosable) {
            Objects.requireNonNull(salt, "The salt cannot be null if selectivelyDisclosable is true");
        }
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
}
