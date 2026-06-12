package eu.europa.esig.dss.eaa.revocation.cwt.model.statuslist;

import eu.europa.esig.dss.cbades.cbor.CBORObject;
import eu.europa.esig.dss.cbades.cbor.CBORObjectFactory;

/**
 * Contains claims for processing the CWT-encoded Token Status List.
 * Please see "5.2. Status List Token in CWT Format" of draft-ietf-oauth-status-list-20.html.
 *
 */
public enum CWTStatusListClaims {

    /**
     * The status list map.
     */
    STATUS_LIST(65533L),

    /**
     * The time to live claim, if present, MUST specify the maximum amount of time,
     * in seconds, that the Status List Token can be cached by a consumer before a
     * fresh copy SHOULD be retrieved.
     */
    TIME_TO_LIVE(65534L),

    /* Status list entries */

    /**
     * URI to retrieve the Status List Aggregation for this type of Referenced Token
     */
    STATUS_LIST_AGGREGATION_URI("aggregation_uri"),

    /**
     * The number of bits per Referenced Token in the compressed byte array (lst)
     */
    STATUS_LIST_BITS("bits"),

    /**
     * The status values for all the Referenced Tokens it conveys statuses for
     */
    STATUS_LIST_LST("lst");

    /** Long encoded key */
    private final CBORObject cborKey;

    /**
     * Constructor with a Long key
     *
     * @param longKey long key value
     */
    CWTStatusListClaims(final Long longKey) {
        this.cborKey = CBORObjectFactory.toCBORObject(longKey);
    }

    /**
     * Constructor with a String key
     *
     * @param stringKey {@link String} key value
     */
    CWTStatusListClaims(final String stringKey) {
        this.cborKey = CBORObjectFactory.toCBORObject(stringKey);
    }

    /**
     * Gets a representation of the key in a form of a CBOR object
     *
     * @return {@link CBORObject}
     */
    public CBORObject cbor() {
        return cborKey;
    }

}
