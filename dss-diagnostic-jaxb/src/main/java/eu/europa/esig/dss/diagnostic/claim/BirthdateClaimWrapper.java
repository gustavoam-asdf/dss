package eu.europa.esig.dss.diagnostic.claim;

import eu.europa.esig.dss.diagnostic.jaxb.XmlBirthdateClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlClaim;

import java.util.HashMap;
import java.util.Map;

/**
 * Wraps an {@code eu.europa.esig.dss.diagnostic.jaxb.XmlBirthdateClaim}
 *
 */
public class BirthdateClaimWrapper extends ClaimWrapper {

    /**
     * Default constructor
     *
     * @param wrapped {@link XmlClaim}
     */
    public BirthdateClaimWrapper(final XmlClaim wrapped) {
        super(wrapped);
    }

    /**
     * Constructor with a parent claim provided
     *
     * @param wrapped {@link XmlClaim}
     * @param parent {@link ClaimWrapper}
     */
    public BirthdateClaimWrapper(final XmlClaim wrapped, final ClaimWrapper parent) {
        super(wrapped, parent);
    }

    /**
     * Gets the user's birthdate
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getBirthdate() {
        XmlClaim wrapped = getWrapped();
        if (wrapped instanceof XmlBirthdateClaim) {
            XmlClaim birthdate = ((XmlBirthdateClaim) wrapped).getBirthdate();
            if (birthdate != null) {
                return new ClaimWrapper(birthdate, this);
            }
        }
        if (isDateTime()) {
            return this;
        }
        return null;
    }

    /**
     * Gets an 8 digit flag to denote the location of the mask in YYYYMMDD format.1 denotes mask.
     * Issuing authority should pick one exact date to be used for full-date value.
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getApproximateMask() {
        XmlClaim wrapped = getWrapped();
        if (wrapped instanceof XmlBirthdateClaim) {
            XmlClaim approximateMask = ((XmlBirthdateClaim) wrapped).getApproximateMask();
            if (approximateMask != null) {
                return new ClaimWrapper(approximateMask, this);
            }
        }
        return null;
    }

    @Override
    public boolean isMap() {
        XmlClaim wrapped = getWrapped();
        if (wrapped instanceof XmlBirthdateClaim && wrapped.getDateTime() == null) {
            return true;
        }
        return super.isMap();
    }

    @Override
    public Map<String, ClaimWrapper> getMap() {
        if (isMap()) {
            final Map<String, ClaimWrapper> result = new HashMap<>(super.getMap());
            ClaimWrapper birthdate = getBirthdate();
            if (birthdate != null) {
                result.put(birthdate.getName(), birthdate);
            }
            ClaimWrapper approximateMask = getApproximateMask();
            if (approximateMask != null) {
                result.put(approximateMask.getName(), approximateMask);
            }
            return result;
        }
        return super.getMap();
    }

}
