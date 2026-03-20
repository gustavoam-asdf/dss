package eu.europa.esig.dss.eaa.jwt.claim;

import eu.europa.esig.dss.eaa.jwt.SDJWTUtils;
import eu.europa.esig.dss.model.eaa.claim.Claim;
import eu.europa.esig.dss.model.eaa.claim.ClaimMap;

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

    @Override
    protected String getKeyAsString(Object key) {
        return (String) key; // only String keys are supported in JSON
    }

    @Override
    protected Claim createClaim(String name, Object value) {
        return SDJWTUtils.createClaim(name, this, value);
    }

}
