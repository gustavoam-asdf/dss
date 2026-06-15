package eu.europa.esig.dss.diagnostic.claim;

import eu.europa.esig.dss.diagnostic.jaxb.XmlAttestedAttributesSubjectClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlClaim;

import java.util.HashMap;
import java.util.Map;

/**
 * Wraps an {@code eu.europa.esig.dss.diagnostic.jaxb.XmlAttestedAttributesSubjectClaim}
 *
 */
public class AttestedAttributesSubjectClaimWrapper extends ClaimWrapper {

    /**
     * Default constructor
     *
     * @param wrapped {@link XmlAttestedAttributesSubjectClaim}
     */
    public AttestedAttributesSubjectClaimWrapper(final XmlAttestedAttributesSubjectClaim wrapped) {
        super(wrapped);
    }

    /**
     * Gets the status's unique index identifier
     *
     * @return {@link ClaimWrapper}
     */
    public AttestedAttributesSubjectClaimIdWrapper getSubjectId() {
        XmlClaim subjectId = getWrapped().getSubjectId();
        if (subjectId != null) {
            return new AttestedAttributesSubjectClaimIdWrapper(subjectId, this);
        }
        return null;
    }

    /**
     * Gets the status's unique index identifier
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getSubjectPseudonym() {
        XmlClaim pseudonym = getWrapped().getSubjectPseudonym();
        if (pseudonym != null) {
            return new ClaimWrapper(pseudonym, this);
        }
        return null;
    }

    /**
     * Gets the status's unique index identifier
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getAttributes() {
        XmlClaim attributes = getWrapped().getAttributes();
        if (attributes != null) {
            return new ClaimWrapper(attributes, this);
        }
        return null;
    }

    @Override
    public Map<String, ClaimWrapper> getMap() {
        final Map<String, ClaimWrapper> result = new HashMap<>(super.getMap());
        ClaimWrapper subjectId = getSubjectId();
        if (subjectId != null) {
            result.put(subjectId.getName(), subjectId);
        }
        ClaimWrapper subjectPseudonym = getSubjectPseudonym();
        if (subjectPseudonym != null) {
            result.put(subjectPseudonym.getName(), subjectPseudonym);
        }
        ClaimWrapper attributes = getAttributes();
        if (attributes != null) {
            result.put(attributes.getName(), attributes);
        }
        return result;
    }

    @Override
    public XmlAttestedAttributesSubjectClaim getWrapped() {
        return (XmlAttestedAttributesSubjectClaim) super.getWrapped();
    }

}
