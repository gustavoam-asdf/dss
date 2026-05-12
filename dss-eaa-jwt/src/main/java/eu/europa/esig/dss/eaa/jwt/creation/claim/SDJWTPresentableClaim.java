package eu.europa.esig.dss.eaa.jwt.creation.claim;

import java.io.Serializable;
import java.util.Objects;

/**
 * Base class for defining an SD-JWT claim
 */
public class SDJWTPresentableClaim implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Object value;

    private final String name;

    private final boolean selectivelyDisclosable;

    private final String salt;

    /**
     * Constructor with the value
     *
     * @param value {@link Object} the value of the claim
     */
    public SDJWTPresentableClaim(final Object value) {
        this(null, value, false, null);
    }

    /**
     * Constructor with the value and claim name
     *
     * @param name {@link String} the claim name
     * @param value {@link Object} the value of the claim
     */
    public SDJWTPresentableClaim(final String name, final Object value) {
        this(name, value, false, null);
    }

    /**
     * Constructor with the value, claim name, selectively disclosable status and salt provided
     *
     * @param name {@link String} the claim name
     * @param value {@link Object} the value of the claim
     * @param selectivelyDisclosable whether the claim is selectively disclosable
     * @param salt {@link String} the salt (mandatory if the claim is selectively disclosable)
     */
    public SDJWTPresentableClaim(final String name, final Object value, final boolean selectivelyDisclosable, final String salt) {
        if (selectivelyDisclosable) {
            Objects.requireNonNull(salt, "The salt cannot be null if selectivelyDisclosable is true");
        }

        this.value = value;
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
     * Gets the value
     *
     * @return {@link Object} the value
     */
    public Object getValue() {
        return value;
    }
}

