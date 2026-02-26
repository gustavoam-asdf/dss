package eu.europa.esig.dss.diagnostic.claim;

import eu.europa.esig.dss.diagnostic.jaxb.XmlDisclosableClaim;
import eu.europa.esig.dss.jaxb.parsers.DateParser;

import java.math.BigInteger;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This class represents a user-friendly wrapper of a generic {@code XmlDisclosableClaim} object,
 * containing an information about a single claim extracted from an EAA Payload.
 * <p>
 * The wrapper may return only one of the following values:
 * - Text; or
 * - Number; or
 * - Boolean; or
 * - DateTime; or
 * - Serialized bytes.
 * <p>
 * Should you need to retrieve any value, you may use the method {@code #getDisplayValue} in order to obtain
 * a String derived from the original value, irrespective of the original claim data type.
 *
 */
public class ClaimWrapper {

    /** Wrapped disclosable claim */
    private final XmlDisclosableClaim wrapped;

    /**
     * Default constructor
     *
     * @param wrapped {@link XmlDisclosableClaim}
     */
    public ClaimWrapper(final XmlDisclosableClaim wrapped) {
        Objects.requireNonNull(wrapped, "XmlDisclosableClaim cannot be null!");
        this.wrapped = wrapped;
    }

    /**
     * Gets the claim name
     *
     * @return {@link String}
     */
    public String getName() {
        return wrapped.getName();
    }

    /**
     * Gets whether the claim was made selectively disclosable and its value has been obtained from a provided disclosure
     *
     * @return whether the claim's value has been obtained from a disclosure
     */
    public boolean isSelectivelyDisclosable() {
        return wrapped.isDisclosure() != null && wrapped.isDisclosure();
    }

    /**
     * Gets the value as a string.
     * If the value is null or not of a string type, returns null
     *
     * @return {@link String}
     */
    public String getText() {
        return wrapped.getText();
    }

    /**
     * Gets whether the claim value is of String type
     *
     * @return TRUE if the value is of String type, FALSE otherwise
     */
    public boolean isText() {
        return wrapped.getText() != null;
    }

    /**
     * Gets the value as a number.
     * If the value is null or not of a number type, returns null
     *
     * @return {@link BigInteger}
     */
    public BigInteger getNumber() {
        return wrapped.getNumber();
    }

    /**
     * Gets whether the claim value is of Number type
     *
     * @return TRUE if the value is of Number type, FALSE otherwise
     */
    public boolean isNumber() {
        return wrapped.getNumber() != null;
    }

    /**
     * Gets the value as boolean.
     * If the value is null or not of a boolean type, returns null
     *
     * @return {@link Boolean}
     */
    public Boolean getBoolean() {
        return wrapped.isBoolean();
    }

    /**
     * Gets whether the claim value is of Boolean type
     *
     * @return TRUE if the value is of Boolean type, FALSE otherwise
     */
    public boolean isBoolean() {
        return wrapped.isBoolean() != null;
    }

    /**
     * Gets the value as date.
     * If the value is null or not of a date type, returns null
     *
     * @return {@link Date}
     */
    public Date getDateTime() {
        return wrapped.getDateTime();
    }

    /**
     * Gets whether the claim value is of Date type
     *
     * @return TRUE if the value is of Date type, FALSE otherwise
     */
    public boolean isDateTime() {
        return wrapped.getDateTime() != null;
    }

    /**
     * Gets the value as list.
     * If the value is null or not of a list type, returns null
     *
     * @return {@link List}
     */
    public List<ClaimWrapper> getItemList() {
        if (!isItemList()) {
            return null;
        }
        return wrapped.getItem().stream().map(ClaimWrapper::new).collect(Collectors.toList());
    }

    /**
     * Gets whether the claim value is of a list type.
     *
     * @return TRUE if the value is of list type, FALSE otherwise
     */
    public boolean isItemList() {
        return wrapped.getItem() != null && !wrapped.getItem().isEmpty();
    }

    /**
     * Gets the value as serialized binaries.
     * If the value is null or not of binaries type, returns null.
     * NOTE: This applies for objects encoded as a map or all other objects types
     * which are not supported directly by the implementation.
     *
     * @return byte array
     */
    public byte[] getSerialized() {
        return wrapped.getSerialized();
    }

    /**
     * Gets whether the claim value is provided as serialized bytes.
     * This applies when the claim is of a map type or other, not directly supported type.
     *
     * @return TRUE if the value is serialized bytes, FALSE otherwise
     */
    public boolean isSerialized() {
        return wrapped.getSerialized() != null;
    }

    /**
     * Gets the wrapped JAXB disclosable claim object
     *
     * @return {@link XmlDisclosableClaim}
     */
    protected XmlDisclosableClaim getWrapped() {
        return wrapped;
    }

    /**
     * Converts the claim's value to its corresponding string representation
     *
     * @return {@link String}
     */
    public String getDisplayValue() {
        if (isText()) {
            return getText();
        } else if (isNumber()) {
            return getNumber().toString();
        } else if (isBoolean()) {
            return getBoolean().toString();
        } else if (isDateTime()) {
            return new DateParser().toString(getDateTime());
        } else if (isItemList()) {
            return toDisplayValue(getItemList());
        } else if (isSerialized()) {
            return new String(getSerialized());
        }
        throw new UnsupportedOperationException("Claim of unsupported type!");
    }

    private String toDisplayValue(List<ClaimWrapper> items) {
        StringBuilder sb = new StringBuilder();
        Iterator<ClaimWrapper> it = items.iterator();
        while (it.hasNext()) {
            ClaimWrapper claimValue = it.next();
            sb.append(claimValue.getDisplayValue());
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

}
