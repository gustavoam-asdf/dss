package eu.europa.esig.dss.eaa.jwt.claim;

import eu.europa.esig.dss.model.eaa.claim.ClaimDeviceKey;
import eu.europa.esig.dss.model.eaa.claim.ClaimMap;

import java.security.PublicKey;

/**
 * SD-JWT VC representation of a wallet holder's key as defined in RFC 7517 "JSON Web Key (JWK)".
 * 
 */
public class SDJWTClaimDeviceKey extends SDJWTClaimMap implements ClaimDeviceKey {

    private static final long serialVersionUID = -579979170978240327L;

    /**
     * Constructor to initialize SDJWTClaimKey from a ClaimMap
     *
     * @param value {@link ClaimMap}
     */
    public SDJWTClaimDeviceKey(ClaimMap value) {
        super(value.getName(), value.getMapValue(), value.isSelectivelyDisclosable(), value.getParent());
    }

    @Override
    public PublicKey getPublicKey() {
        // TODO : to be implemented
        return null;
    }

}
