package eu.europa.esig.dss.model.eaa.claim;

import java.util.Arrays;
import java.util.Base64;

/**
 * Represents a byte array encoded (selectively) disclosable claim
 *
 */
public class ClaimBinaries extends AbstractClaim {

    private static final long serialVersionUID = 2456980111724075951L;

    /** String value of the claim */
    private final byte[] value;

    /**
     * Default constructor
     *
     * @param value byte array value of the claim
     */
    public ClaimBinaries(final byte[] value) {
        this(null, value);
    }

    /**
     * Constructor with claim name provided
     *
     * @param name {@link String} claim header name
     * @param value byte array value of the claim
     */
    public ClaimBinaries(final String name, final byte[] value) {
        this(name, value, false);
    }

    /**
     * Constructor with claim name and selectively disclosable status provided
     *
     * @param name {@link String} claim header name
     * @param value byte array value of the claim
     * @param selectivelyDisclosable whether the claim is selectively disclosable
     *                               (can be TRUE only when the value of claim is provided in a form of disclosure)
     */
    public ClaimBinaries(final String name, final byte[] value, final boolean selectivelyDisclosable) {
        super(name, selectivelyDisclosable);
        this.value = value;
    }

    @Override
    public byte[] getBinariesValue() {
        return value;
    }

    @Override
    public boolean isBinaryValueType() {
        return true;
    }

    @Override
    public String getValueAsString() {
        return value != null ? Base64.getEncoder().encodeToString(value) : null;
    }

    @Override
    public boolean isNullOrEmpty() {
        return value != null;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        ClaimBinaries that = (ClaimBinaries) object;
        return Arrays.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(value);
    }

}
