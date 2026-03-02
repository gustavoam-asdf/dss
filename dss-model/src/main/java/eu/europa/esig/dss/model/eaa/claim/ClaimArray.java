package eu.europa.esig.dss.model.eaa.claim;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents an Array encoded (selectively) disclosable claim
 *
 */
public class ClaimArray extends AbstractClaim {

    private static final long serialVersionUID = -7818132616539798304L;

    /** The content of the array */
    protected final List<?> value;

    /**
     * Disclosable claim array
     *
     * @param value a list of {@link Claim}s representing the original array value
     */
    public ClaimArray(final List<?> value) {
        this(null, value);
    }

    /**
     * Constructor with claim name provided
     *
     * @param name {@link String} claim header name
     * @param value a list of {@link Claim}s representing the original array value
     */
    public ClaimArray(final String name, final List<?> value) {
        this(name, value, false);
    }

    /**
     * Constructor with claim name and selectively disclosable status provided
     *
     * @param name {@link String} claim header name
     * @param value a list of {@link Claim}s representing the original array value
     * @param selectivelyDisclosable whether the claim is selectively disclosable
     *                               (can be TRUE only when the value of claim is provided in a form of disclosure)
     */
    public ClaimArray(final String name, final List<?> value, final boolean selectivelyDisclosable) {
        this(name, value, selectivelyDisclosable, null);
    }

    /**
     * Constructor with claim name and selectively disclosable status and a parent claim provided
     *
     * @param name {@link String} claim header name
     * @param value a list of {@link Claim}s representing the original array value
     * @param selectivelyDisclosable whether the claim is selectively disclosable
     *                               (can be TRUE only when the value of claim is provided in a form of disclosure)
     * @param parent {@link Claim} representing the parent claim, when applicable
     */
    public ClaimArray(final String name, final List<?> value, final boolean selectivelyDisclosable, final Claim parent) {
        super(name, selectivelyDisclosable, parent);
        this.value = value;
    }

    @Override
    public List<Claim> getListValue() {
        if (value == null || value.isEmpty()) {
            return Collections.emptyList();
        }
        return value.stream().map(v -> Claim.create(null, this, v)).collect(Collectors.toList());
    }

    @Override
    public boolean isArrayValueType() {
        return true;
    }

    @Override
    public boolean isNullOrEmpty() {
        return value == null || value.isEmpty();
    }

    @Override
    public String getValueAsString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Iterator<Claim> it = getListValue().iterator();
        while (it.hasNext()) {
            Claim claimValue = it.next();
            if (claimValue.isStringValueType()) {
                sb.append("\"");
            }
            sb.append(claimValue.getValueAsString());
            if (claimValue.isStringValueType()) {
                sb.append("\"");
            }
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ClaimArray that = (ClaimArray) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Objects.hashCode(value);
        return result;
    }

}
