package eu.europa.esig.dss.eaa.jwt.claim;

import eu.europa.esig.dss.eaa.jwt.SDJWTUtils;
import eu.europa.esig.dss.model.eaa.claim.Claim;
import eu.europa.esig.dss.model.eaa.claim.ClaimDate;
import eu.europa.esig.dss.model.eaa.claim.ClaimMap;
import eu.europa.esig.dss.model.eaa.claim.ClaimNumber;
import eu.europa.esig.dss.model.eaa.claim.ClaimString;
import eu.europa.esig.dss.spi.DSSUtils;

import java.util.Date;
import java.util.Map;

/**
 * SD-JWT implementation of a ClaimMap
 *
 */
public class SDJWTClaimMap extends ClaimMap {

    private static final long serialVersionUID = -8277442405573676334L;

    /**
     * Simplified constructor with a map value
     *
     * @param value {@link Map}
     */
    protected SDJWTClaimMap(Map<?, ?> value) {
        super(value);
    }

    /**
     * Default constructor
     *
     * @param name {@link String} claim header name
     * @param value value of the claim
     * @param selectivelyDisclosable whether the claim is selectively disclosable
     *                               (can be TRUE only when the value of claim is provided in a form of disclosure)
     * @param parent {@link Claim} representing the parent claim, when applicable
     */
    public SDJWTClaimMap(String name, Map<?, ?> value, boolean selectivelyDisclosable, Claim parent) {
        super(name, value, selectivelyDisclosable, parent);
    }

    /**
     * Gets the claim value if a Date from the current map using the {@code headerName} as a key
     *
     * @param headerName {@link String}
     * @return {@link ClaimDate}
     */
    public ClaimDate getAsDate(String headerName) {
        ClaimString claimString = getAsString(headerName);
        if (claimString != null) {
            Date date = DSSUtils.parseISO8601Date(claimString.getStringValue());
            if (date != null) {
                return new ClaimDate(headerName, date, claimString.isSelectivelyDisclosable());
            }
        }
        return null;
    }

    /**
     * Gets value of a header with name {@code headerName} as ClaimDate.
     * Returns NULL if no value is provided or the Claim is of a different type.
     *
     * @param headerName {@link String} to get header value from the payload
     * @return {@link ClaimDate}
     */
    public ClaimDate getAsDateTime(String headerName) {
        ClaimString claimString = getAsString(headerName);
        if (claimString != null) {
            Date date = DSSUtils.parseRFCDate(claimString.getStringValue());
            if (date != null) {
                return new ClaimDate(headerName, date, claimString.isSelectivelyDisclosable());
            }
        }
        ClaimNumber claimNumber = getAsNumber(headerName);
        if (claimNumber != null) {
            long timeValueInMilliseconds = DSSUtils.getTimeValueInMilliseconds(claimNumber.getNumberValue().longValue());
            Date date = DSSUtils.getDateFromMilliseconds(timeValueInMilliseconds);
            return new ClaimDate(headerName, date, claimNumber.isSelectivelyDisclosable());
        }
        return null;
    }

    @Override
    protected String getKeyAsString(Object key) {
        return (String) key; // only String keys are supported in JSON
    }

    @Override
    protected Claim createClaim(String name, Object value) {
        return SDJWTUtils.createClaim(name, this, value);
    }

}
