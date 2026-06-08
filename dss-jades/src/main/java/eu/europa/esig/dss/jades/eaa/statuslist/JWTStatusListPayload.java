package eu.europa.esig.dss.jades.eaa.statuslist;

import eu.europa.esig.dss.jades.DSSJsonUtils;
import eu.europa.esig.dss.jades.eaa.JWTPayload;

import java.util.Map;

/**
 * Represents a payload of a Token Status List (TLS), as defined in
 * {@link <a href="https://www.ietf.org/archive/id/draft-ietf-oauth-status-list-20.html">IETF Token Status List (TSL)</a>}.
 */
public class JWTStatusListPayload extends JWTPayload {

    /**
     * Default constructor
     *
     * @param payload map
     */
    public JWTStatusListPayload(final Map<String, Object> payload) {
        super(payload);
    }

    /**
     * Gets the value of the 'typ' (type) claim that specifies the type of the token.
     *
     * @return {@link String}
     */
    public String getType() {
        return getAsString(JWTStatusListClaimNames.TYP);
    }

    /**
     * Gets the value of the 'ttl' (time to live) claim that specifies the maximum amount of time,
     * in seconds, that the Status List Token can be cached by a consumer before a fresh copy SHOULD be retrieved.
     *
     * @return {@link String}
     */
    public Number getTimeToLive() {
        return getAsNumber(JWTStatusListClaimNames.TTL);
    }

    /**
     * Gets the value of the 'status_list' (status list) claim that specifies the Status List
     * conforming to the structure defined in Section 4.2.
     *
     * @return {@link String}
     */
    public Map<?, ?> getStatusList() {
        return getAsMap(JWTStatusListClaimNames.STATUS_LIST);
    }

    /**
     * Gets the value of the 'bits' (bits) of the "status_list" claim that specifies
     * the number of bits per Referenced Token in the compressed byte array (lst).
     *
     * @return {@link String}
     */
    public Number getStatusListBits() {
        Map<?, ?> statusList = getStatusList();
        if (statusList != null) {
            return DSSJsonUtils.getAsNumber(statusList, JWTStatusListClaimNames.BITS);
        }
        return null;
    }

    /**
     * Gets the value of the 'lst' (list) of the "status_list" claim that contains
     * the status values for all the Referenced Tokens it conveys statuses for.
     * The contained value shall be base64url-encoded.
     * NOTE: this class does not verify validity of the data format.
     *
     * @return {@link String}
     */
    public String getStatusListBase64Url() {
        Map<?, ?> statusList = getStatusList();
        if (statusList != null) {
            return DSSJsonUtils.getAsString(statusList, JWTStatusListClaimNames.LST);
        }
        return null;
    }

    /**
     * Gets the value of the 'aggregation_uri' (Aggregation URI) of the "status_list" claim that contains
     * a URI to retrieve the Status List Aggregation for this type of Referenced Token or Issuer.
     *
     * @return {@link String}
     */
    public String getStatusListAggregationUri() {
        Map<?, ?> statusList = getStatusList();
        if (statusList != null) {
            return DSSJsonUtils.getAsString(statusList, JWTStatusListClaimNames.AGGREGATION_URI);
        }
        return null;
    }

}
