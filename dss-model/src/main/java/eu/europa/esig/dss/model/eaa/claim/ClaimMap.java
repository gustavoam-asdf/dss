package eu.europa.esig.dss.model.eaa.claim;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a Map encoded (selectively) disclosable claim
 *
 */
public class ClaimMap extends AbstractClaim {

    private static final long serialVersionUID = 3493467581292504831L;

    /** Map value of the claim */
    protected final Map<?,?> value;

    /**
     * Default constructor
     *
     * @param value value of the claim
     */
    public ClaimMap(final Map<?,?> value) {
        this(null, value);
    }

    /**
     * Constructor with claim name provided
     *
     * @param name {@link String} claim header name
     * @param value value of the claim
     */
    public ClaimMap(final String name, final Map<?,?> value) {
        this(name, value, false);
    }

    /**
     * Constructor with claim name and selectively disclosable status provided
     *
     * @param name {@link String} claim header name
     * @param value value of the claim
     * @param selectivelyDisclosable whether the claim is selectively disclosable
     *                               (can be TRUE only when the value of claim is provided in a form of disclosure)
     */
    public ClaimMap(final String name, final Map<?,?> value, final boolean selectivelyDisclosable) {
        super(name, selectivelyDisclosable);
        this.value = value;
    }

    @Override
    public Map<String, Claim> getMapValue() {
        if (value == null || value.isEmpty()) {
            return Collections.emptyMap();
        }
        final Map<String, Claim> result = new HashMap<>();
        for (Map.Entry<?, ?> entry : value.entrySet()) {
            String headerName = (String) entry.getKey();
            Claim claim = Claim.create(headerName, entry.getValue());
            result.put(headerName, claim);
        }
        return result;
    }

    /**
     * Gets the claim value if a map from the current map using the {@code headerName} as a key
     *
     * @param headerName {@link String}
     * @return {@link ClaimMap}
     */
    public ClaimMap getAsMap(String headerName) {
        Claim claim = getMapValue().get(headerName);
        if (claim != null && claim.isMapValueType()) {
            return (ClaimMap) claim;
        }
        return null;
    }

    /**
     * Gets the claim value if a number from the current map using the {@code headerName} as a key
     *
     * @param headerName {@link String}
     * @return {@link ClaimNumber}
     */
    public ClaimNumber getAsNumber(String headerName) {
        Claim claim = getMapValue().get(headerName);
        if (claim != null && claim.isNumberValueType()) {
            return (ClaimNumber) claim;
        }
        return null;
    }

    /**
     * Gets the claim value if a string from the current map using the {@code headerName} as a key
     *
     * @param headerName {@link String}
     * @return {@link ClaimString}
     */
    public ClaimString getAsString(String headerName) {
        Claim claim = getMapValue().get(headerName);
        if (claim != null && claim.isStringValueType()) {
            return (ClaimString) claim;
        }
        return null;
    }

    @Override
    public boolean isMapValueType() {
        return true;
    }

    @Override
    public boolean isNullOrEmpty() {
        return value == null || value.isEmpty();
    }

    @Override
    public String getValueAsString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        Iterator<Map.Entry<String, Claim>> it = getMapValue().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Claim> entry = it.next();
            sb.append("\"");
            sb.append(entry.getKey());
            sb.append("\": ");
            Claim claimValue = entry.getValue();
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
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        ClaimMap that = (ClaimMap) object;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

}
