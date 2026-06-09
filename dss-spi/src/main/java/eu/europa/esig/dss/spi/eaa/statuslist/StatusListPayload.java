package eu.europa.esig.dss.spi.eaa.statuslist;

import java.util.Date;

/**
 * Represents a payload of a Token Status List object
 *
 */
public interface StatusListPayload {

    /**
     * Gets the value of the (Type) claim that specifies the type of the token.
     *
     * @return {@link String}
     */
    String getType();

    /**
     * Gets the value of the Subject claim identifying the principal that is the subject of the token.
     *
     * @return {@link String}
     */
    String getSubject();

    /**
     * Gets the value of the Expiration Time claim identifying the expiration time on
     * or after which the token MUST NOT be accepted for processing.
     *
     * @return {@link String}
     */
    Date getExpirationTime();

    /**
     * Gets the value of the Issued At claim identifying the time before which the token
     * MUST NOT be accepted for processing.
     *
     * @return {@link String}
     */
    Date getIssuedAt();

    /**
     * Gets the value of the Time To Live claim that specifies the maximum amount of time,
     * in seconds, that the Status List Token can be cached by a consumer before a fresh copy SHOULD be retrieved.
     *
     * @return {@link String}
     */
    Number getTimeToLive();

    /**
     * Gets the value of the 'bits' (bits) of the "status_list" claim that specifies
     * the number of bits per Referenced Token in the compressed byte array (lst).
     *
     * @return {@link String}
     */
    Number getStatusListBits();

    /**
     * Gets the value of the 'lst' (list) of the "status_list" claim that contains
     * the status values for all the Referenced Tokens it conveys statuses for.
     *
     * @return {@link String}
     */
    byte[] getStatusListEncoded();

    /**
     * Gets the value of the 'aggregation_uri' (Aggregation URI) of the "status_list" claim that contains
     * a URI to retrieve the Status List Aggregation for this type of Referenced Token or Issuer.
     *
     * @return {@link String}
     */
    String getStatusListAggregationUri();

}
