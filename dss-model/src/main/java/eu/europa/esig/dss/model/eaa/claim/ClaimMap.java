package eu.europa.esig.dss.model.eaa.claim;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a Map encoded (selectively) disclosable claim
 *
 */
public abstract class ClaimMap extends AbstractClaim {

    private static final long serialVersionUID = 3493467581292504831L;

    /** Map value of the claim */
    protected final Map<?,?> value;

    /**
     * Default constructor
     *
     * @param value value of the claim
     */
    protected ClaimMap(final Map<?,?> value) {
        this(null, value, false, null);
    }

    /**
     * Constructor with claim name and selectively disclosable status and a parent claim provided
     *
     * @param name {@link String} claim header name
     * @param value value of the claim
     * @param selectivelyDisclosable whether the claim is selectively disclosable
     *                               (can be TRUE only when the value of claim is provided in a form of disclosure)
     * @param parent {@link Claim} representing the parent claim, when applicable
     */
    public ClaimMap(final String name, final Map<?,?> value, final boolean selectivelyDisclosable, final Claim parent) {
        super(name, selectivelyDisclosable, parent);
        this.value = value;
    }

    /**
     * Constructor with claim name, namespace and selectively disclosable status and a parent claim provided
     *
     * @param name {@link String} claim header name
     * @param namespace {@link String} representing the original namespace (NOTE: used in mdoc)
     * @param value value of the claim
     * @param selectivelyDisclosable whether the claim is selectively disclosable
     *                               (can be TRUE only when the value of claim is provided in a form of disclosure)
     * @param parent {@link Claim} representing the parent claim, when applicable
     */
    public ClaimMap(final String name, final String namespace, final Map<?,?> value,
                    final boolean selectivelyDisclosable, final Claim parent) {
        super(name, selectivelyDisclosable, parent);
        this.value = value;
    }

    @Override
    public Map<String, Claim> getMapValue() {
        if (value == null || value.isEmpty()) {
            return Collections.emptyMap();
        }
        final Map<String, Claim> result = new HashMap<>();
        for (Map.Entry<?, ?> entry : value.entrySet()) {
            String headerName = getKeyAsString(entry.getKey());
            Claim claim = createClaim(headerName, value.get(entry.getKey()));
            result.put(headerName, claim);
        }
        return result;
    }

    /**
     * Gets a set of map keys
     *
     * @return a set of {@link String} keys
     */
    public Set<String> getKeys() {
        return value.keySet().stream().map(this::getKeyAsString).collect(Collectors.toSet());
    }

    /**
     * Gets the claims for the corresponding header name key
     *
     * @param headerName {@link String} header name or a map key to get a corresponding value for
     * @return {@link Claim}
     */
    public Claim get(String headerName) {
        return getMapValue().get(headerName);
    }

    /**
     * This method returns a map key as a String
     *
     * @param key object
     * @return {@link String}
     */
    protected abstract String getKeyAsString(Object key);

    /**
     * Creates a claim for the map entry
     *
     * @param name {@link String} header key
     * @param value object
     * @return {@link Claim}
     */
    protected abstract Claim createClaim(String name, Object value);

    /**
     * Gets the claim value if a map from the current map using the {@code headerName} as a key
     *
     * @param headerName {@link String}
     * @return {@link ClaimMap}
     */
    public ClaimMap getAsMap(String headerName) {
        Claim claim = get(headerName);
        if (claim != null && claim.isMapValueType()) {
            return (ClaimMap) claim;
        }
        return null;
    }

    /**
     * Gets the claim value if an array from the current map using the {@code headerName} as a key
     *
     * @param headerName {@link String}
     * @return {@link ClaimArray}
     */
    public ClaimArray getAsArray(String headerName) {
        Claim claim = get(headerName);
        if (claim != null && claim.isArrayValueType()) {
            return (ClaimArray) claim;
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
        Claim claim = get(headerName);
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
        Claim claim = get(headerName);
        if (claim != null && claim.isStringValueType()) {
            return (ClaimString) claim;
        }
        return null;
    }

    /**
     * Gets the claim value if a boolean from the current map using the {@code headerName} as a key
     *
     * @param headerName {@link String}
     * @return {@link ClaimBoolean}
     */
    public ClaimBoolean getAsBoolean(String headerName) {
        Claim claim = get(headerName);
        if (claim != null && claim.isBooleanValueType()) {
            return (ClaimBoolean) claim;
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

    /**
     * Gets size of the map
     *
     * @return number of entries within the map
     */
    public int getSize() {
        if (isNullOrEmpty()) {
            return 0;
        }
        return value.size();
    }

    @Override
    public String getValueAsString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        Iterator<String> it = getKeys().iterator();
        while (it.hasNext()) {
            String key = it.next();
            sb.append("\"");
            sb.append(key);
            sb.append("\": ");
            Claim claimValue = get(key);
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ClaimMap claimMap = (ClaimMap) o;
        return Objects.equals(value, claimMap.value);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Objects.hashCode(value);
        return result;
    }

}
