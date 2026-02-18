package eu.europa.esig.dss.model.eaa.claim;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Defines a claim that may be made selectively disclosable
 *
 */
public interface Claim extends Serializable {

    /**
     * Gets the claim name
     *
     * @return {@link String}
     */
    String getName();

    /**
     * Gets whether the claim was made selectively disclosable and its value has been obtained from a provided disclosure
     *
     * @return whether the claim's value has been obtained from a disclosure
     */
    boolean isSelectivelyDisclosable();

    /**
     * Gets the value as list.
     * If the value is null or not of a list type, returns null
     *
     * @return {@link List}
     */
    List<Claim> getListValue();

    /**
     * Gets the value as binaries.
     * If the value is null or not of binaries type, returns null
     *
     * @return byte array
     */
    byte[] getBinariesValue();

    /**
     * Gets the value as boolean.
     * If the value is null or not of a boolean type, returns null
     *
     * @return {@link Boolean}
     */
    Boolean getBooleanValue();

    /**
     * Gets the value as date.
     * If the value is null or not of a date type, returns null
     *
     * @return {@link Date}
     */
    Date getDateValue();

    /**
     * Gets the value as a map.
     * If the value is null or not of a map type, returns null
     *
     * @return {@link Map}
     */
    Map<String, Claim> getMapValue();

    /**
     * Gets the value as a number.
     * If the value is null or not of a number type, returns null
     *
     * @return {@link Number}
     */
    Number getNumberValue();

    /**
     * Gets the value as a string.
     * If the value is null or not of a string type, returns null
     *
     * @return {@link String}
     */
    String getStringValue();

    /**
     * Gets whether the claim value is of String type
     *
     * @return TRUE if the value is of String type, FALSE otherwise
     */
    boolean isStringValueType();

    /**
     * Gets whether the claim value is of Boolean type
     *
     * @return TRUE if the value is of Binary type, FALSE otherwise
     */
    boolean isBinaryValueType();

    /**
     * Gets whether the claim value is of Boolean type
     *
     * @return TRUE if the value is of Boolean type, FALSE otherwise
     */
    boolean isBooleanValueType();

    /**
     * Gets whether the claim value is of Number type
     *
     * @return TRUE if the value is of Number type, FALSE otherwise
     */
    boolean isNumberValueType();

    /**
     * Gets whether the claim value is of Date type
     *
     * @return TRUE if the value is of Date type, FALSE otherwise
     */
    boolean isDateValueType();

    /**
     * Gets whether the claim value is of Array type
     *
     * @return TRUE if the value is of Array type, FALSE otherwise
     */
    boolean isArrayValueType();

    /**
     * Gets whether the claim value is of Map type
     *
     * @return TRUE if the value is of Map type, FALSE otherwise
     */
    boolean isMapValueType();

    /**
     * Gets whether the claim value is of Null type
     *
     * @return TRUE if the value is of Null type, FALSE otherwise
     */
    boolean isNullValueType();

    /**
     * Gets whether the value of the claim is null or empty
     *
     * @return TRUE whether the value of the claim is null or empty, FALSE otherwise
     */
    boolean isNullOrEmpty();

    /**
     * Converts the claim's value to its corresponding string representation
     *
     * @return {@link String}
     */
    String getValueAsString();

    /**
     * This method parses the {@code value} and wraps it into a {@code ClaimValue} according to its format.
     * This method can be used for non selectively disclosable claims, provided directly within EAA Payload.
     *
     * @param value {@link Object} containing the value of the object
     * @return {@link Claim}
     */
    static Claim create(Object value) {
        return create(value, false);
    }

    /**
     * This method parses the {@code value} and wraps it into a {@code ClaimValue} according to its format.
     * This method can be used for definition of claims used within provided disclosures.
     *
     * @param value {@link Object} containing the value of the object
     * @param selectivelyDisclosable whether the claim is selectively disclosable
     *                               (can be TRUE only when the value of claim is provided in a form of disclosure)
     * @return {@link Claim}
     */
    static Claim create(Object value, boolean selectivelyDisclosable) {
        return create(null, value, selectivelyDisclosable);
    }

    /**
     * This method parses the {@code value} and wraps it into a {@code ClaimValue} according to its format.
     * This method can be used for non selectively disclosable claims, provided directly within EAA Payload.
     *
     * @param claimName {@link String} representing the header name of the claim
     * @param value {@link Object} containing the value of the object
     * @return {@link Claim}
     */
    static Claim create(String claimName, Object value) {
        return create(claimName, value, false);
    }

    /**
     * This method parses the {@code value} and wraps it into a {@code ClaimValue} according to its format.
     * This method can be used for definition of claims used within provided disclosures.
     *
     * @param claimName {@link String} representing the header name of the claim
     * @param value {@link Object} containing the value of the object
     * @param selectivelyDisclosable whether the claim is selectively disclosable
     *                               (can be TRUE only when the value of claim is provided in a form of disclosure)
     * @return {@link Claim}
     */
    static Claim create(String claimName, Object value, boolean selectivelyDisclosable) {
        if (value instanceof Claim) {
            return (Claim) value;
        }else if (value instanceof String) {
            return new ClaimString(claimName, (String) value, selectivelyDisclosable);
        } else if (value instanceof Number) {
            return new ClaimNumber(claimName, (Number) value, selectivelyDisclosable);
        } else if (value instanceof Boolean) {
            return new ClaimBoolean(claimName, (Boolean) value, selectivelyDisclosable);
        } else if (value instanceof byte[]) {
            return new ClaimBinaries(claimName, (byte[]) value, selectivelyDisclosable);
        } else if (value instanceof Date) {
            return new ClaimDate(claimName, (Date) value, selectivelyDisclosable);
        } else if (value instanceof Map) {
            return new ClaimMap(claimName, (Map<?,?>) value, selectivelyDisclosable);
        } else if (value instanceof List) {
            return new ClaimArray(claimName, (List<?>) value, selectivelyDisclosable);
        } else if (value == null) {
            return new ClaimNull(claimName, selectivelyDisclosable);
        } else {
            throw new IllegalArgumentException(String.format("The claim value of type '%s' is not supported!", value.getClass().getSimpleName()));
        }
    }

}
