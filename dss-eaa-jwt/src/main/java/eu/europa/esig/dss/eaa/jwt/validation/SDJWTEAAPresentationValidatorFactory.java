package eu.europa.esig.dss.eaa.jwt.validation;

import eu.europa.esig.dss.eaa.common.validation.EAAPresentationValidatorFactory;
import eu.europa.esig.dss.model.DSSDocument;

/**
 * This class is used to load a relevant validator for a presentation of Electronic Attestation of Attributes validation
 *
 */
public class SDJWTEAAPresentationValidatorFactory implements EAAPresentationValidatorFactory {

    /**
     * Default constructor
     */
    public SDJWTEAAPresentationValidatorFactory() {
        // empty
    }

    @Override
    public boolean isSupported(DSSDocument document) {
        SDJWTCompactEAAPresentationValidator compactValidator = new SDJWTCompactEAAPresentationValidator();
        if (compactValidator.isSupported(document)) {
            return true;
        }

        // TODO : add JSON serialization validator

        return false;
    }

    @Override
    public AbstractSDJWTEAAPresentationValidator create(DSSDocument document) {
        SDJWTCompactEAAPresentationValidator compactValidator = new SDJWTCompactEAAPresentationValidator();
        if (compactValidator.isSupported(document)) {
            return new SDJWTCompactEAAPresentationValidator(document);
        }

        // TODO : add JSON serialization validator

        throw new IllegalArgumentException("Not supported document");
    }

}