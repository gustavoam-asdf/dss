package eu.europa.esig.dss.eaa.mdoc.claim;

import eu.europa.esig.dss.eaa.mdoc.MdocUtils;
import eu.europa.esig.dss.model.eaa.claim.Claim;
import eu.europa.esig.dss.model.eaa.claim.ClaimArray;

import java.util.List;

/**
 * Mdoc implementation of a ClaimArray
 *
 */
public class MdocClaimArray extends ClaimArray {

    private static final long serialVersionUID = -2421839275001261767L;

    /**
     * Constructor with claim name and selectively disclosable status and a parent claim provided
     *
     * @param name {@link String} claim header name
     * @param namespace {@link String} representing the original namespace (NOTE: used in mdoc)
     * @param value a list of {@link Claim}s representing the original array value
     * @param selectivelyDisclosable whether the claim is selectively disclosable
     *                               (can be TRUE only when the value of claim is provided in a form of disclosure)
     * @param parent {@link Claim} representing the parent claim, when applicable
     */
    public MdocClaimArray(final String name, final String namespace, final List<?> value,
                          final boolean selectivelyDisclosable, final Claim parent) {
        super(name, namespace, value, selectivelyDisclosable, parent);
    }

    @Override
    protected Claim createClaim(Object value) {
        return MdocUtils.createClaim(null, this, value);
    }

}
