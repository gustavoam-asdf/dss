package eu.europa.esig.dss.eaa.revocation.cwt.validation.identifierlist;

import eu.europa.esig.dss.eaa.revocation.validation.identifierlist.IdentifierListValidator;
import eu.europa.esig.dss.eaa.revocation.validation.identifierlist.IdentifierListValidatorFactory;

/**
 * Loads a corresponding validator for an Identifier List provided in CWT Format,
 * as defined in ISO/IEC 18013-5 "12.3.6.4 Identifier list details".
 *
 */
public class CWTIdentifierListValidatorFactory implements IdentifierListValidatorFactory {

    /**
     * Default constructor
     */
    public CWTIdentifierListValidatorFactory() {
        // empty
    }

    @Override
    public boolean isSupported(byte[] eaaStatusList) {
        return new CWTIdentifierListValidator().isSupported(eaaStatusList);
    }

    @Override
    public IdentifierListValidator create(byte[] eaaStatusList) {
        return new CWTIdentifierListValidator(eaaStatusList);
    }

}
