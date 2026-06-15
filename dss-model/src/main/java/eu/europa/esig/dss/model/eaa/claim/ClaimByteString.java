package eu.europa.esig.dss.model.eaa.claim;

import java.util.Arrays;
import java.util.Base64;

/**
 * Represents a byte array (selectively) disclosable claim
 *
 */
public class ClaimByteString extends AbstractClaim {
    
    private static final long serialVersionUID = -8229099082350076412L;

    /** byte[] value of the claim */
    private final byte[] value;

    /**
     * Default constructor
     *
     * @param value {@link byte[]} value of the claim
     */
    public ClaimByteString(final byte[] value) {
        this(null, value);
    }

    /**
     * Constructor with claim name provided
     *
     * @param name {@link String} claim header name
     * @param value {@link byte[]} value of the claim
     */
    public ClaimByteString(final String name, final byte[] value) {
        this(name, value, false);
    }

    /**
     * Constructor with claim name and selectively disclosable status provided
     *
     * @param name {@link String} claim header name
     * @param value {@link byte[]} value of the claim
     * @param selectivelyDisclosable whether the claim is selectively disclosable
     *                               (can be TRUE only when the value of claim is provided in a form of disclosure)
     */
    public ClaimByteString(final String name, final byte[] value, final boolean selectivelyDisclosable) {
        this(name, value, selectivelyDisclosable, null);
    }

    /**
     * Constructor with claim name and selectively disclosable status and a parent claim provided
     *
     * @param name {@link String} claim header name
     * @param value {@link byte[]} value of the claim
     * @param selectivelyDisclosable whether the claim is selectively disclosable
     *                               (can be TRUE only when the value of claim is provided in a form of disclosure)
     * @param parent {@link Claim} representing the parent claim, when applicable
     */
    public ClaimByteString(final String name, final byte[] value, final boolean selectivelyDisclosable, final Claim parent) {
        this(name, null, value, selectivelyDisclosable, parent);
    }

    /**
     * Constructor with claim name, namespace and selectively disclosable status and a parent claim provided
     *
     * @param name {@link String} claim header name
     * @param namespace {@link String} representing the original namespace (NOTE: used in mdoc)
     * @param value {@link byte[]} value of the claim
     * @param selectivelyDisclosable whether the claim is selectively disclosable
     *                               (can be TRUE only when the value of claim is provided in a form of disclosure)
     * @param parent {@link Claim} representing the parent claim, when applicable
     */
    public ClaimByteString(final String name, final String namespace, final byte[] value,
                           final boolean selectivelyDisclosable, final Claim parent) {
        super(name, namespace, selectivelyDisclosable, parent);
        this.value = value;
    }

    @Override
    public byte[] getBinaryValue() {
        return value;
    }

    @Override
    public boolean isBinaryValueType() {
        return true;
    }

    @Override
    public boolean isNullOrEmpty() {
        return value != null;
    }

    @Override
    public String getValueAsString() {
        return new String(Base64.getEncoder().encode(value));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ClaimByteString that = (ClaimByteString) o;
        return Arrays.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(value);
        return result;
    }

}
