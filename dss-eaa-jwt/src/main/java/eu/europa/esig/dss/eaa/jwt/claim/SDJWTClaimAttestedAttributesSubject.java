package eu.europa.esig.dss.eaa.jwt.claim;

import eu.europa.esig.dss.eaa.jwt.SDJWTConstants;
import eu.europa.esig.dss.model.eaa.claim.ClaimArray;
import eu.europa.esig.dss.model.eaa.claim.ClaimAttestedAttributesSubject;
import eu.europa.esig.dss.model.eaa.claim.ClaimMap;
import eu.europa.esig.dss.model.eaa.claim.ClaimString;

/**
 * SD-JWT implementation of the attested attributes claims
 *
 */
public class SDJWTClaimAttestedAttributesSubject extends SDJWTClaimMap implements ClaimAttestedAttributesSubject {

    private static final long serialVersionUID = 2378393232187408462L;

    /**
     * Default constructor
     *
     * @param value {@link ClaimMap}
     */
    public SDJWTClaimAttestedAttributesSubject(ClaimMap value) {
        super(value.getName(), value.getMapValue(), value.isSelectivelyDisclosable(), value.getParent());
    }

    @Override
    public ClaimString getSubjectId() {
        /*
        * EAA-5.3-04: The sub_id member shall be a JSON String whose value shall be the identifier of the attribute
        * subject, which shall associate the attributes to this attribute subject.
        */
        return getAsString(SDJWTConstants.ATTESTED_ATTRIBUTES_SUBJECT_ID);
    }

    @Override
    public ClaimString getSubjectPseudonym() {
        /*
         * EAA-5.3-05: The sub_aka member shall be a JSON String whose value shall be the pseudonym of an attribute
         * subject which shall associate the attributes to this attribute subject.
         */
        return getAsString(SDJWTConstants.ATTESTED_ATTRIBUTES_SUBJECT_AKA);
    }

    @Override
    public ClaimArray getAttributes() {
        /*
         * EAA-5.3-07: The attrs member shall be a JSON Array whose elements shall be the attributes associated to the
         * attribute subject whose identifier appears in the sub_id member or whose pseudonym appears in the sub_aka
         * member.
         */
        return getAsArray(SDJWTConstants.ATTESTED_ATTRIBUTES_SUBJECT_ATTRIBUTES);
    }

}
