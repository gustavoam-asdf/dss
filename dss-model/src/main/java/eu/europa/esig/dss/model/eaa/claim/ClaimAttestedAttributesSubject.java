package eu.europa.esig.dss.model.eaa.claim;

/**
 * Associates a set of attributes to one entity different than the EAA subject.
 *
 */
public interface ClaimAttestedAttributesSubject extends Claim {

    /**
     * Gets the identifier of the attribute subject, which shall associate the attributes to this attribute subject
     *
     * @return {@link Claim}
     */
    Claim getSubjectId();

    /**
     * Gets the pseudonym of an attribute subject which shall associate the attributes to this attribute subject.
     *
     * @return {@link ClaimString}
     */
    ClaimString getSubjectPseudonym();

    /**
     * Gets the attributes associated to the attribute subject whose identifier appears in the sub_id member or
     * whose pseudonym appears in the sub_aka member.
     *
     * @return {@link ClaimArray}
     */
    ClaimArray getAttributes();

}
