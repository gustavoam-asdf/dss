package eu.europa.esig.dss.eaa.revocation.cwt.model.statuslist;

import eu.europa.esig.dss.cbades.cbor.CBORMap;
import eu.europa.esig.dss.cbades.cwt.CWTPayload;
import eu.europa.esig.dss.eaa.revocation.model.statuslist.StatusListPayload;

/**
 * Represents a payload of an CWT-encoded Token Status List
 *
 */
public class CWTStatusListPayload extends CWTPayload implements StatusListPayload {

    /**
     * Default constructor
     *
     * @param payload {@link CBORMap}
     */
    public CWTStatusListPayload(final CBORMap payload) {
        super(payload);
    }

    @Override
    public Number getTimeToLive() {
        return payload.getAsLong(CWTStatusListClaims.TIME_TO_LIVE.cbor());
    }

    /**
     * Gets the value of the 'status_list' (status list) claim that specifies the Status List
     * conforming to the structure defined in Section 4.3.
     *
     * @return {@link CBORMap}
     */
    public CBORMap getStatusList() {
        return payload.getAsMap(CWTStatusListClaims.STATUS_LIST.cbor());
    }

    @Override
    public Number getStatusListBits() {
        CBORMap statusList = getStatusList();
        if (statusList != null) {
            return statusList.getAsLong(CWTStatusListClaims.STATUS_LIST_BITS.cbor());
        }
        return null;
    }

    @Override
    public byte[] getStatusListEncoded() {
        CBORMap statusList = getStatusList();
        if (statusList != null) {
            return statusList.getAsBinaries(CWTStatusListClaims.STATUS_LIST_LST.cbor());
        }
        return null;
    }

    @Override
    public String getAggregationUri() {
        CBORMap statusList = getStatusList();
        if (statusList != null) {
            return statusList.getAsString(CWTStatusListClaims.STATUS_LIST_AGGREGATION_URI.cbor());
        }
        return null;
    }

}
