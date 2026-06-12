package eu.europa.esig.dss.cbades.eaa.status.statuslist;

import eu.europa.esig.dss.spi.eaa.status.statuslist.StatusListValidator;
import eu.europa.esig.dss.spi.eaa.status.statuslist.StatusListValidatorFactory;

/**
 * Loads a corresponding validator for a Token Status List (TSL) provided in CWT Format, 
 * as defined in "5.2. Status List Token in CWT Format" of
 * {@link <a href="https://www.ietf.org/archive/id/draft-ietf-oauth-status-list-20.html">IETF Token Status List (TSL)</a>}.
 */
public class CWTStatusListValidatorFactory implements StatusListValidatorFactory {

    /**
     * Default constructor
     */
    public CWTStatusListValidatorFactory() {
        // empty
    }

    @Override
    public boolean isSupported(byte[] eaaStatusList) {
        return new CWTStatusListValidator().isSupported(eaaStatusList);
    }

    @Override
    public StatusListValidator create(byte[] eaaStatusList) {
        return new CWTStatusListValidator(eaaStatusList);
    }

}
