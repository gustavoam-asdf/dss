package eu.europa.esig.dss.eaa.mdoc.claim;

import eu.europa.esig.dss.eaa.mdoc.ETSI194721Headers;
import eu.europa.esig.dss.model.eaa.claim.Claim;
import eu.europa.esig.dss.model.eaa.claim.ClaimArray;
import eu.europa.esig.dss.model.eaa.claim.ClaimAttestedAttributesSubject;
import eu.europa.esig.dss.model.eaa.claim.ClaimMap;
import eu.europa.esig.dss.model.eaa.claim.ClaimString;

/**
 * Associates one attribute to one entity different than the EAA subject.
 *
 */
public class MdocClaimAttestedAttributesSubject extends MdocClaimMap implements ClaimAttestedAttributesSubject {

    private static final long serialVersionUID = 6496844266166338418L;

    /**
     * Constructor to initialize MdocClaimAttestedAttributesSubject from a ClaimMap
     *
     * @param value {@link ClaimMap}
     */
    public MdocClaimAttestedAttributesSubject(ClaimMap value) {
        super(value.getName(), value.getNamespace(), value.getMapValue(), value.isSelectivelyDisclosable(), value.getParent());
    }

    @Override
    public Claim getSubjectId() {
        ClaimMap subId = getAsMap(ETSI194721Headers.SUB_ATTRS_ID);
        if (subId != null) {
            return new MdocClaimAttestedAttributesSubjectId(subId);
        }
        return null;
    }

    @Override
    public ClaimString getSubjectPseudonym() {
        return getAsString(ETSI194721Headers.SUB_ATTRS_AKA);
    }

    @Override
    public ClaimArray getAttributes() {
        // not supported in mdoc
        return null;
    }

}
