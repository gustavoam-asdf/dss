package eu.europa.esig.dss.jades.eaa.statuslist;

/**
 * Contains a list of claim names specified within the
 * {@link <a href="https://www.ietf.org/archive/id/draft-ietf-oauth-status-list-20.html">IETF Token Status List (TSL)</a>}.
 */
public final class JWTStatusListClaimNames {

    /**
     * Utils class
     */
    private JWTStatusListClaimNames() {
        // empty
    }

    /* 5.1. Status List Token in JWT Format */

    /**
     * Data structure representing the content of a JSON-encoded Status List.
     */
    public static final String STATUS_LIST = "status_list";

    /**
     * The ttl (time to live) claim, if present, MUST specify the maximum amount of time,
     * in seconds, that the Status List Token can be cached by a consumer before a fresh
     * copy SHOULD be retrieved.
     */
    public static final String TTL = "ttl";

    /**
     * Type of the token. The JWT type MUST be statuslist+jwt.
     */
    public static final String TYP = "typ";

    /* 4.2. Status List in JSON Format */

    /**
     * JSON String that contains a URI to retrieve the Status List Aggregation for this type
     * of Referenced Token or Issuer.
     */
    public static final String AGGREGATION_URI = "aggregation_uri";

    /**
     * JSON Integer specifying the number of bits per Referenced Token in the compressed
     * byte array (lst). The allowed values for bits are 1, 2, 4, and 8.
     */
    public static final String BITS = "bits";

    /**
     * JSON String that contains the status values for all the Referenced Tokens it conveys statuses for.
     */
    public static final String LST = "lst";


}
