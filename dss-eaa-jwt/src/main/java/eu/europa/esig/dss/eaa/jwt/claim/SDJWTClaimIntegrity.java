package eu.europa.esig.dss.eaa.jwt.claim;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.model.eaa.claim.ClaimIntegrity;
import eu.europa.esig.dss.model.eaa.claim.ClaimString;
import eu.europa.esig.dss.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SD-JWT implementation of a claim integrity.
 * The claim integrity is used for those claims, suffixed with "#integrity" string.
 *
 */
public class SDJWTClaimIntegrity extends ClaimString implements ClaimIntegrity {

    private static final long serialVersionUID = -1585182324348523330L;

    private static final Logger LOG = LoggerFactory.getLogger(SDJWTClaimIntegrity.class);

    /**
     * Default constructor
     *
     * @param value {@link ClaimString}
     */
    public SDJWTClaimIntegrity(ClaimString value) {
        super(value.getName(), value.getStringValue(), value.isSelectivelyDisclosable(), value.getParent());
    }

    @Override
    public DigestAlgorithm getDigestAlgorithm() {
        String[] parts = getStringValue().split("-");
        if (parts.length > 1) {
            String srIntegrityId = parts[0];
            try {
                return DigestAlgorithm.forSrIntegrityId(srIntegrityId);
            } catch (IllegalArgumentException e) {
                LOG.warn("Unable to find a corresponding DigestAlgorithm for integrity claim for value '{}'!", srIntegrityId);
            }
        }
        return null;
    }

    @Override
    public byte[] getDigestValue() {
        String[] parts = getStringValue().split("-");
        if (parts.length > 1) {
            /*
             * Digest are expected to be Base64 encoded.
             * See {@code https://www.w3.org/TR/2016/REC-SRI-20160623/#integrity-metadata}
             */
            String digestValueB64 = parts[1];
            if (Utils.isBase64Encoded(digestValueB64)) {
                return Utils.fromBase64(digestValueB64);
            } else {
                LOG.warn("The #integrity bytes are not base64 encoded!");
            }
        }
        return null;
    }

    @Override
    public boolean isSubresourceIntegrityType() {
        return true;
    }

}
