package eu.europa.esig.dss.eaa.revocation.jwt.validation.statuslist;

import eu.europa.esig.dss.eaa.revocation.validation.statuslist.StatusListValidator;
import eu.europa.esig.dss.eaa.revocation.validation.statuslist.StatusListValidatorFactory;

/**
 * Loads a corresponding validator for a Token Status List (TSL) provided in JWT Format,
 * as defined in "5.1. Status List Token in JWT Format" of
 * {@link <a href="https://www.ietf.org/archive/id/draft-ietf-oauth-status-list-20.html">IETF Token Status List (TSL)</a>}.
 */
public class JWTStatusListValidatorFactory implements StatusListValidatorFactory {

    /**
     * Default constructor
     */
    public JWTStatusListValidatorFactory() {
        // empty
    }

    @Override
    public boolean isSupported(byte[] eaaStatusList) {
        return new JWTStatusListValidator().isSupported(eaaStatusList);
    }

    @Override
    public StatusListValidator create(byte[] eaaStatusList) {
        return new JWTStatusListValidator(eaaStatusList);
    }

}
