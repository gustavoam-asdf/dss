package eu.europa.esig.dss.eaa.mdoc.claim;

import eu.europa.esig.dss.eaa.mdoc.ETSI194721Headers;
import eu.europa.esig.dss.model.eaa.claim.ClaimAttestedAttributesSubjectId;
import eu.europa.esig.dss.model.eaa.claim.ClaimMap;
import eu.europa.esig.dss.model.eaa.claim.ClaimString;

/**
 * Univocally identify the attribute subject.
 *
 */
public class MdocClaimAttestedAttributesSubjectId extends MdocClaimMap implements ClaimAttestedAttributesSubjectId {

    private static final long serialVersionUID = 766280420105767688L;

    /**
     * Constructor to initialize MdocClaimAttestedAttributesSubjectId from a ClaimMap
     *
     * @param value {@link ClaimMap}
     */
    public MdocClaimAttestedAttributesSubjectId(ClaimMap value) {
        super(value.getName(), value.getNamespace(), value.getMapValue(), value.isSelectivelyDisclosable(), value.getParent());
    }

    @Override
    public ClaimString getFamilyName() {
        return getAsString(ETSI194721Headers.SUB_ATTRS_ID_FAMILY_NAME);
    }

    @Override
    public ClaimString getGivenName() {
        return getAsString(ETSI194721Headers.SUB_ATTRS_ID_GIVEN_NAME);
    }

    @Override
    public ClaimString getDocumentNumber() {
        return getAsString(ETSI194721Headers.SUB_ATTRS_ID_DOCUMENT_NUMBER);
    }

}
