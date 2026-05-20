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
     * Create a {@link SDJWTEAAClaim} with the provided value. The name of the claim will be null.
     *
     * @param value {@link Object} the value of the claim
     * @return the created {@link SDJWTEAAClaim}
     */
    public static SDJWTEAAClaim create(final Object value) {
        return new SDJWTEAAClaim(null, value, false);
    }

    /**
     * Create a {@link SDJWTEAAClaim} with the provided name and value.
     *
     * @param name {@link String} the claim name
     * @param value {@link Object} the claim value
     * @return the created {@link SDJWTEAAClaim}
     */
    public static SDJWTEAAClaim create(final String name, final Object value) {
        return new SDJWTEAAClaim(name, value, false);
    }

    /**
     * Create a selectively disclosable {@link SDJWTEAAClaim} with the provided value. The name of the claim will be null.
     * The salt will be generated during the EAA Payload computation.
     *
     * @param value {@link Object} the value of the claim
     * @return the created {@link SDJWTEAAClaim}
     */
    public static SDJWTEAAClaim createSelectivelyDisclosable(final Object value) {
        return new SDJWTEAAClaim(null, value, true);
    }

    /**
     * Create a selectively disclosable {@link SDJWTEAAClaim} with the provided value and salt
     *
     * @param value {@link Object} the value of the claim
     * @param salt {@link String} the salt
     * @return the created {@link SDJWTEAAClaim}
     */
    public static SDJWTEAAClaim createSelectivelyDisclosableWithSalt(final Object value, final String salt) {
        return new SDJWTEAAClaim(null, value, true, salt);
    }

    /**
     * Create a selectively disclosable {@link SDJWTEAAClaim} with the provided name and value.
     * The salt will be generated during the EAA Payload computation.
     *
     * @param name {@link String} the claim name
     * @param value {@link Object} the value of the claim
     * @return the created {@link SDJWTEAAClaim}
     */
    public static SDJWTEAAClaim createSelectivelyDisclosable(final String name, final Object value) {
        return new SDJWTEAAClaim(name, value, true);
    }

    /**
     * Create a selectively disclosable {@link SDJWTEAAClaim} with the provided name, value and salt
     *
     * @param name {@link String} the claim name
     * @param value {@link Object} the value of the claim
     * @param salt {@link String} the salt
     * @return the created {@link SDJWTEAAClaim}
     */
    public static SDJWTEAAClaim createSelectivelyDisclosableWithSalt(final String name, final Object value, final String salt) {
        return new SDJWTEAAClaim(name, value, true, salt);
    }

    /**
     * Create a {@link SDJWTEAAClaimObject}. The name of the claim will be null.
     *
     * @return the created {@link SDJWTEAAClaimObject}
     */
    public static SDJWTEAAClaimObject createObject() {
        return SDJWTEAAClaimObject.create();
    }

    /**
     * Create a {@link SDJWTEAAClaimObject} with the provided name.
     *
     * @param name {@link String} the name of the claim
     * @return the created {@link SDJWTEAAClaimObject}
     */
    public static SDJWTEAAClaimObject createObject(final String name) {
        return SDJWTEAAClaimObject.create(name);
    }

    /**
     * Create a selectively disclosable {@link SDJWTEAAClaimObject}. The name of the claim will be null.
     *
     * @return the created {@link SDJWTEAAClaimObject}
     */
    public static SDJWTEAAClaimObject createObjectSelectivelyDisclosable() {
        return SDJWTEAAClaimObject.createSelectivelyDisclosable();
    }

    /**
     * Create a selectively disclosable {@link SDJWTEAAClaimObject} with the provided salt. The name of the claim will be null.
     *
     * @param salt {@link String} the salt value
     * @return the created {@link SDJWTEAAClaimObject}
     */
    public static SDJWTEAAClaimObject createObjectSelectivelyDisclosableWithSalt(final String salt) {
        return SDJWTEAAClaimObject.createSelectivelyDisclosableWithSalt(salt);
    }

    /**
     * Create a selectively disclosable {@link SDJWTEAAClaimObject} with the provided name.
     *
     * @param name {@link String} the name of the claim
     * @return the created {@link SDJWTEAAClaimObject}
     */
    public static SDJWTEAAClaimObject createObjectSelectivelyDisclosable(final String name) {
        return SDJWTEAAClaimObject.createSelectivelyDisclosable(name);
    }

    /**
     * Create a selectively disclosable {@link SDJWTEAAClaimObject} with the provided name and salt.
     *
     * @param name {@link String} the name of the claim
     * @param salt {@link String} the salt value
     * @return the created {@link SDJWTEAAClaimObject}
     */
    public static SDJWTEAAClaimObject createObjectSelectivelyDisclosableWithSalt(final String name, final String salt) {
        return SDJWTEAAClaimObject.createSelectivelyDisclosableWithSalt(name, salt);
    }

    /**
     * Create a {@link SDJWTEAAClaimArray}. The name of the claim will be null.
     *
     * @return the created {@link SDJWTEAAClaimArray}
     */
    public static SDJWTEAAClaimArray createArray() {
        return SDJWTEAAClaimArray.create();
    }

    /**
     * Create a {@link SDJWTEAAClaimArray} with the provided name.
     *
     * @param name {@link String} the name of the claim
     * @return the created {@link SDJWTEAAClaimArray}
     */
    public static SDJWTEAAClaimArray createArray(final String name) {
        return SDJWTEAAClaimArray.create(name);
    }

    /**
     * Create a selectively disclosable {@link SDJWTEAAClaimArray}. The name of the claim will be null.
     *
     * @return the created {@link SDJWTEAAClaimArray}
     */
    public static SDJWTEAAClaimArray createArraySelectivelyDisclosable() {
        return SDJWTEAAClaimArray.createSelectivelyDisclosable();
    }

    /**
     * Create a selectively disclosable {@link SDJWTEAAClaimArray} with the provided salt. The name of the claim will be null.
     *
     * @param salt {@link String} the salt value
     * @return the created {@link SDJWTEAAClaimArray}
     */
    public static SDJWTEAAClaimArray createArraySelectivelyDisclosableWithSalt(final String salt) {
        return SDJWTEAAClaimArray.createSelectivelyDisclosableWithSalt(salt);
    }

    /**
     * Create a selectively disclosable {@link SDJWTEAAClaimArray} with the provided name.
     *
     * @param name {@link String} the name of the claim
     * @return the created {@link SDJWTEAAClaimArray}
     */
    public static SDJWTEAAClaimArray createArraySelectivelyDisclosable(final String name) {
        return SDJWTEAAClaimArray.createSelectivelyDisclosable(name);
    }

    /**
     * Create a selectively disclosable {@link SDJWTEAAClaimArray} with the provided name and salt.
     *
     * @param name {@link String} the name of the claim
     * @param salt {@link String} the salt value
     * @return the created {@link SDJWTEAAClaimArray}
     */
    public static SDJWTEAAClaimArray createArraySelectivelyDisclosableWithSalt(final String name, final String salt) {
        return SDJWTEAAClaimArray.createSelectivelyDisclosableWithSalt(name, salt);
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
    protected SDJWTEAAClaim(final String name, final Object value, final boolean selectivelyDisclosable) {
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
    protected SDJWTEAAClaim(final String name, final Object value, final boolean selectivelyDisclosable, final String salt) {
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
