package eu.europa.esig.dss.diagnostic.claim;

import eu.europa.esig.dss.diagnostic.jaxb.XmlAttestedAttributesSubjectIdClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlClaim;

import java.util.HashMap;
import java.util.Map;

/**
 * Wraps an {@code eu.europa.esig.dss.diagnostic.jaxb.XmlAttestedAttributesSubjectIdClaim}
 *
 */
public class AttestedAttributesSubjectClaimIdWrapper extends ClaimWrapper {

    /**
     * Default constructor
     *
     * @param wrapped {@link XmlClaim}
     */
    public AttestedAttributesSubjectClaimIdWrapper(final XmlClaim wrapped) {
        super(wrapped);
    }

    /**
     * Constructor with a parent provided
     *
     * @param wrapped {@link XmlClaim}
     * @param parent {@link ClaimWrapper}
     */
    public AttestedAttributesSubjectClaimIdWrapper(final XmlClaim wrapped, final ClaimWrapper parent) {
        super(wrapped, parent);
    }

    /**
     * Gets the family name of the attribute subject
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getFamilyName() {
        XmlClaim wrapped = getWrapped();
        if (wrapped instanceof XmlAttestedAttributesSubjectIdClaim) {
            XmlClaim familyName = ((XmlAttestedAttributesSubjectIdClaim) wrapped).getFamilyName();
            if (familyName != null) {
                return new ClaimWrapper(familyName, this);
            }
        }
        return null;
    }

    /**
     * Gets the given name of the attribute subject
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getGivenName() {
        XmlClaim wrapped = getWrapped();
        if (wrapped instanceof XmlAttestedAttributesSubjectIdClaim) {
            XmlClaim givenName = ((XmlAttestedAttributesSubjectIdClaim) wrapped).getGivenName();
            if (givenName != null) {
                return new ClaimWrapper(givenName, this);
            }
        }
        return null;
    }

    /**
     * Gets the given name of the attribute subject
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getDocumentNumber() {
        XmlClaim wrapped = getWrapped();
        if (wrapped instanceof XmlAttestedAttributesSubjectIdClaim) {
            XmlClaim documentNumber = ((XmlAttestedAttributesSubjectIdClaim) wrapped).getDocumentNumber();
            if (documentNumber != null) {
                return new ClaimWrapper(documentNumber, this);
            }
        }
        return null;
    }

    @Override
    public boolean isMap() {
        XmlClaim wrapped = getWrapped();
        if (wrapped instanceof XmlAttestedAttributesSubjectIdClaim) {
            return true;
        }
        return super.isMap();
    }

    @Override
    public Map<String, ClaimWrapper> getMap() {
        XmlClaim wrapped = getWrapped();
        if (wrapped instanceof XmlAttestedAttributesSubjectIdClaim) {
            final Map<String, ClaimWrapper> result = new HashMap<>(super.getMap());
            ClaimWrapper familyName = getFamilyName();
            if (familyName != null) {
                result.put(familyName.getName(), familyName);
            }
            ClaimWrapper givenName = getGivenName();
            if (givenName != null) {
                result.put(givenName.getName(), givenName);
            }
            ClaimWrapper documentNumber = getDocumentNumber();
            if (documentNumber != null) {
                result.put(documentNumber.getName(), documentNumber);
            }
            return result;
        }
        return super.getMap();
    }

}
