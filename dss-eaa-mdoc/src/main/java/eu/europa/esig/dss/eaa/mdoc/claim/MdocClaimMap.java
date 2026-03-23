package eu.europa.esig.dss.eaa.mdoc.claim;

import eu.europa.esig.dss.eaa.mdoc.MdocUtils;
import eu.europa.esig.dss.model.eaa.claim.Claim;
import eu.europa.esig.dss.model.eaa.claim.ClaimMap;

import java.util.Map;

/**
 * Mdoc implementation of a ClaimMap
 *
 */
public class MdocClaimMap extends ClaimMap {

    private static final long serialVersionUID = 5139850883142004890L;

    /**
     * Simplified constructor with a map value
     *
     * @param value {@link Map}
     */
    protected MdocClaimMap(Map<?, ?> value) {
        super(value);
    }

    /**
     * Default constructor
     *
     * @param name {@link String} claim header name
     * @param namespace {@link String} representing the original namespace (NOTE: used in mdoc)
     * @param value value of the claim
     * @param selectivelyDisclosable whether the claim is selectively disclosable
     *                               (can be TRUE only when the value of claim is provided in a form of disclosure)
     * @param parent {@link Claim} representing the parent claim, when applicable
     */
    public MdocClaimMap(final String name, final String namespace, final Map<?, ?> value,
                        final boolean selectivelyDisclosable, final Claim parent) {
        super(name, namespace, value, selectivelyDisclosable, parent);
    }

    @Override
    protected String getKeyAsString(Object key) {
        if (key instanceof String) {
            return (String) key;
        }
        // CBOR allows any type of map keys
        return MdocUtils.createClaim(key).getValueAsString();
    }

    @Override
    protected Claim createClaim(String name, Object value) {
        return MdocUtils.createClaim(name, this, value);
    }

}
