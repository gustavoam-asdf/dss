package eu.europa.esig.dss.eaa.mdoc.claim;

import eu.europa.esig.dss.cbades.COSEConstants;
import eu.europa.esig.dss.model.eaa.claim.Claim;
import eu.europa.esig.dss.model.eaa.claim.ClaimArray;
import eu.europa.esig.dss.model.eaa.claim.ClaimByteString;
import eu.europa.esig.dss.model.eaa.claim.ClaimMap;

/**
 * deviceKey contains the public part of the key pair used for mdoc authentication (see 9.1.3.4). The
 * deviceKey element is encoded as an untagged COSE_Key element as specified in RFC 8152; further
 * requirements are defined in 9.1.5.2.
 *
 */
public class MdocClaimDeviceKey extends MdocClaimMap {

    private static final long serialVersionUID = 4939740857897930307L;

    /**
     * Constructor to initialize MdocClaimDevice from a ClaimMap
     *
     * @param value {@link ClaimMap}
     */
    public MdocClaimDeviceKey(ClaimMap value) {
        super(value.getName(), value.getNamespace(), value.getMapValue(), value.isSelectivelyDisclosable(), value.getParent());
    }

    /**
     * Gets the identification of the key type claim
     *
     * @return {@link Claim}
     */
    public Claim getKTY() {
        return get(COSEConstants.COSE_KEY_KTY);
    }

    /**
     * Gets the key identification value -- match to kid in message claim
     *
     * @return {@link ClaimByteString}
     */
    public ClaimByteString getKID() {
        return getAsByteString(COSEConstants.COSE_KEY_KID);
    }

    /**
     * Gets the key usage restriction to this algorithm claim
     *
     * @return {@link Claim}
     */
    public Claim getALG() {
        return get(COSEConstants.COSE_KEY_ALG);
    }

    /**
     * Gets the restrict set of permissible operations claim
     *
     * @return {@link ClaimArray}
     */
    public ClaimArray getKeyOps() {
        return getAsArray(COSEConstants.COSE_KEY_KEY_OPS);
    }

    /**
     * Gets the Base IV to be xor-ed with Partial IVs
     *
     * @return {@link ClaimByteString}
     */
    public ClaimByteString getBaseIV() {
        return getAsByteString(COSEConstants.COSE_KEY_BASE_IV);
    }

}
