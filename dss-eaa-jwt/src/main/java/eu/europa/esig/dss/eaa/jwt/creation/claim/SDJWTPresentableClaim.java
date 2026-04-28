package eu.europa.esig.dss.eaa.jwt.creation.claim;

import java.io.Serializable;
import java.util.Objects;

/**
 * Base class for defining an SD-JWT claim
 */
public abstract class SDJWTPresentableClaim implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;

    private final boolean selectivelyDisclosable;

    private final String salt;

    /**
     * Constructor with the claim name, selectively disclosable status and salt provided
     *
     * @param name {@link String} the claim name
     * @param selectivelyDisclosable whether the claim is selectively disclosable
     * @param salt {@link String} the salt (mandatory if the claim is selectively disclosable)
     */
    protected SDJWTPresentableClaim(final String name, final boolean selectivelyDisclosable, final String salt) {
        if (selectivelyDisclosable) {
            Objects.requireNonNull(salt, "The salt cannot be null if selectivelyDisclosable is true");
        }

        this.name = name;
        this.selectivelyDisclosable = selectivelyDisclosable;
        this.salt = salt;
    }

    /**
     * Gets the claim name
     *
     * @return {@link String}
     */
    public String getName() {
        return name;
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

    /**
     * Converts the claim's value to its corresponding string representation
     *
     * @return {@link String}
     */
    public abstract String getValueAsString();
}

