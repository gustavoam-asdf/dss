package eu.europa.esig.dss.model.eaa.claim;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

/**
 * Represents a Date encoded (selectively) disclosable claim
 *
 */
public class ClaimDate extends AbstractClaim {

    private static final long serialVersionUID = 6114436137496767072L;

    /** RFC 3339 DateTime format used by default */
    public static final String RFC3339_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    /** The UTC timezone (GMT+0), used by default */
    public static final TimeZone UTC_TIMEZONE = TimeZone.getTimeZone("UTC");

    /** Date value of the claim */
    private final Date value;

    /**
     * Default constructor
     *
     * @param value {@link Date} value of the claim
     */
    public ClaimDate(final Date value) {
        this(null, value);
    }

    /**
     * Constructor with claim name provided
     *
     * @param name {@link String} claim header name
     * @param value {@link Date} value of the claim
     */
    public ClaimDate(final String name, final Date value) {
        this(name, value, false);
    }

    /**
     * Constructor with claim name and selectively disclosable status provided
     *
     * @param name {@link String} claim header name
     * @param value {@link Date} value of the claim
     * @param selectivelyDisclosable whether the claim is selectively disclosable
     *                               (can be TRUE only when the value of claim is provided in a form of disclosure)
     */
    public ClaimDate(final String name, final Date value, final boolean selectivelyDisclosable) {
        super(name, selectivelyDisclosable);
        this.value = value;
    }

    @Override
    public Date getDateValue() {
        return value;
    }

    @Override
    public boolean isDateValueType() {
        return true;
    }

    @Override
    public String getValueAsString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(RFC3339_TIME_FORMAT);
        simpleDateFormat.setTimeZone(UTC_TIMEZONE);
        return (value == null) ? "N/A" : simpleDateFormat.format(value);
    }

    @Override
    public boolean isNullOrEmpty() {
        return value == null;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        ClaimDate that = (ClaimDate) object;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

}
