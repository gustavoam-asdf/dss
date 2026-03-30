package eu.europa.esig.dss.eaa.mdoc.claim;

import eu.europa.esig.dss.eaa.mdoc.MdocUtils;
import eu.europa.esig.dss.model.eaa.claim.Claim;
import eu.europa.esig.dss.model.eaa.claim.ClaimDate;
import eu.europa.esig.dss.model.eaa.claim.ClaimMap;
import eu.europa.esig.dss.spi.DSSUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;

/**
 * Mdoc implementation of a ClaimMap
 *
 */
public class MdocClaimMap extends ClaimMap {

    private static final Logger LOG = LoggerFactory.getLogger(MdocClaimMap.class);

    private static final long serialVersionUID = 5139850883142004890L;

    /**
     * Simplified constructor with a map value
     *
     * @param value {@link Map}
     */
    protected MdocClaimMap(Map<?, ?> value) {
        super(value);
    }

    /**
     * Default constructor
     *
     * @param name {@link String} claim header name
     * @param namespace {@link String} representing the original namespace (NOTE: used in mdoc)
     * @param value value of the claim
     * @param selectivelyDisclosable whether the claim is selectively disclosable
     *                               (can be TRUE only when the value of claim is provided in a form of disclosure)
     * @param parent {@link Claim} representing the parent claim, when applicable
     */
    public MdocClaimMap(final String name, final String namespace, final Map<?, ?> value,
                        final boolean selectivelyDisclosable, final Claim parent) {
        super(name, namespace, value, selectivelyDisclosable, parent);
    }

    /**
     * Checks if the {@code claim} is of date type and returns its value as {@code ClaimDate}
     *
     * @param headerName {@link String} to get header value from the payload
     * @return {@link ClaimDate}
     */
    protected ClaimDate getAsDate(String headerName) {
        Claim claim = get(headerName);
        return getAsDate(claim);
    }

    /**
     * Checks if the {@code claim} is of date type and returns its value as {@code ClaimDate}
     *
     * @param claim {@link Claim}
     * @return {@link ClaimDate}
     */
    protected ClaimDate getAsDate(Claim claim) {
        if (claim != null && claim.isStringValueType()) {
            Date date = DSSUtils.parseISO8601Date(claim.getStringValue());
            if (date != null) {
                return new ClaimDate(claim.getName(), claim.getNamespace(), date, claim.isSelectivelyDisclosable(), claim.getParent());
            }
        }
        return null;
    }

    /**
     * Checks if the {@code claim} is of date-time type and returns its value as {@code ClaimDate}
     *
     * @param headerName {@link String} to get header value from the payload
     * @return {@link ClaimDate}
     */
    protected ClaimDate getAsDateTime(String headerName) {
        Claim claim = get(headerName);
        return getAsDateTime(claim);
    }

    /**
     * Checks if the {@code claim} is of date type and returns its value as {@code ClaimDate}
     *
     * @param claim {@link Claim}
     * @return {@link ClaimDate}
     */
    protected ClaimDate getAsDateTime(Claim claim) {
        if (claim == null) {
            return null;
        }
        if (claim.isStringValueType()) {
            Date date = DSSUtils.parseRFCDate(claim.getStringValue());
            if (date != null) {
                return new ClaimDate(claim.getName(), date, claim.isSelectivelyDisclosable());
            }
        } else if (claim.isNumberValueType()) {
            long timeValueInMilliseconds = DSSUtils.getTimeValueInMilliseconds(claim.getNumberValue().longValue());
            Date date = DSSUtils.getDateFromMilliseconds(timeValueInMilliseconds);
            return new ClaimDate(claim.getName(), claim.getNamespace(), date, claim.isSelectivelyDisclosable(), claim.getParent());
        }
        return null;
    }
    /**
     * Checks if the {@code claim} is of date or date-time type and returns its value as {@code ClaimDate}
     *
     * @param headerName {@link String} to get header value from the payload
     * @return {@link ClaimDate}
     */
    protected ClaimDate getAsDateOrDateTime(String headerName) {
        Claim claim = get(headerName);
        return getAsDateOrDateTime(claim);
    }

    /**
     * Checks if the {@code claim} is of date or date-time type and returns its value as {@code ClaimDate}
     *
     * @param claim {@link Claim}
     * @return {@link ClaimDate}
     */
    protected ClaimDate getAsDateOrDateTime(Claim claim) {
        if (claim == null) {
            return null;
        }
        if (claim.isStringValueType()) {
            String dateTimeString = claim.getStringValue();
            Date date;
            if (DSSUtils.isRFCDate(dateTimeString)) {
                date = DSSUtils.parseRFCDate(dateTimeString);
            } else if (DSSUtils.isISO8601Date(dateTimeString)) {
                date = DSSUtils.parseISO8601Date(dateTimeString);
            } else {
                LOG.warn("Date or full datetime is expected for the claim with name '{}'!", claim.getName());
                return null;
            }
            if (date != null) {
                return new ClaimDate(claim.getName(), claim.getNamespace(), date, claim.isSelectivelyDisclosable(), claim.getParent());
            }

        } else if (claim.isNumberValueType()) {
            long timeValueInMilliseconds = DSSUtils.getTimeValueInMilliseconds(claim.getNumberValue().longValue());
            Date date = DSSUtils.getDateFromMilliseconds(timeValueInMilliseconds);
            return new ClaimDate(claim.getName(), claim.getNamespace(), date, claim.isSelectivelyDisclosable(), claim.getParent());
        }
        return null;
    }

    @Override
    protected String getKeyAsString(Object key) {
        if (key instanceof String) {
            return (String) key;
        }
        // CBOR allows any type of map keys
        return MdocUtils.createClaim(key).getValueAsString();
    }

    @Override
    protected Claim createClaim(String name, Object value) {
        return MdocUtils.createClaim(name, this, value);
    }

}
