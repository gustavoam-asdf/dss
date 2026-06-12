package eu.europa.esig.dss.eaa.revocation.model.identifierlist;

import java.util.Date;
import java.util.List;

/**
 * Represents a payload of an Identifier List
 *
 */
public interface IdentifierListPayload {

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
    List<byte[]> getIdentifierListIdentifiers();

    /**
     * Gets the value of the 'aggregation_uri' (Aggregation URI) of the "status_list" claim that contains
     * a URI to retrieve the Status List Aggregation for this type of Referenced Token or Issuer.
     *
     * @return {@link String}
     */
    String getAggregationUri();

}
