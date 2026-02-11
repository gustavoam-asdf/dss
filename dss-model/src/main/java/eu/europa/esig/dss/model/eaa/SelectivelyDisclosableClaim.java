package eu.europa.esig.dss.model.eaa;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * Generic implementation of a selectively diclosable claim
 *
 */
public class SelectivelyDisclosableClaim implements Serializable {

    private static final long serialVersionUID = -2082442478741482452L;

    /** SD claim digest value */
    private byte[] digestValue;

    /** Claim name when provided */
    private String claimName;

    /**
     * Default constructor
     */
    public SelectivelyDisclosableClaim() {
        // empty
    }

    /**
     * Gets digest value of the selectifely disclosable claim
     *
     * @return byte array representing a digest value of the incorporated selectively disclosable claim
     */
    public byte[] getDigestValue() {
        return digestValue;
    }

    /**
     * Sets digest value of the selectively disclosable claim
     *
     * @param digestValue byte array
     */
    public void setDigestValue(byte[] digestValue) {
        this.digestValue = digestValue;
    }

    /**
     * Gets the claim name, when defined (e.g. for selectively disclosable array elements)
     *
     * @return {@link String} representing the claim name
     */
    public String getClaimName() {
        return claimName;
    }

    /**
     * Sets the claim name
     *
     * @param claimName {@link String}
     */
    public void setClaimName(String claimName) {
        this.claimName = claimName;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        SelectivelyDisclosableClaim that = (SelectivelyDisclosableClaim) object;
        return Arrays.equals(digestValue, that.digestValue)
                && Objects.equals(claimName, that.claimName);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(digestValue);
        result = 31 * result + Objects.hashCode(claimName);
        return result;
    }


}
