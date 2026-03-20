package eu.europa.esig.dss.eaa.jwt.claim;

import eu.europa.esig.dss.eaa.jwt.SDJWTUtils;
import eu.europa.esig.dss.model.eaa.claim.Claim;
import eu.europa.esig.dss.model.eaa.claim.ClaimArray;

import java.util.List;

/**
 * SD-JWT implementation of a {@code ClaimArray}
 *
 */
public class SDJWTClaimArray extends ClaimArray {

    private static final long serialVersionUID = 8097598759991618602L;

    /**
     * Constructor with claim name and selectively disclosable status and a parent claim provided
     *
     * @param name {@link String} claim header name
     * @param value a list of {@link Claim}s representing the original array value
     * @param selectivelyDisclosable whether the claim is selectively disclosable
     *                               (can be TRUE only when the value of claim is provided in a form of disclosure)
     * @param parent {@link Claim} representing the parent claim, when applicable
     */
    public SDJWTClaimArray(final String name, final List<?> value, final boolean selectivelyDisclosable, final Claim parent) {
        super(name, value, selectivelyDisclosable, parent);
    }

    @Override
    protected Claim createClaim(Object value) {
        return SDJWTUtils.createClaim(null, this, value);
    }

}
