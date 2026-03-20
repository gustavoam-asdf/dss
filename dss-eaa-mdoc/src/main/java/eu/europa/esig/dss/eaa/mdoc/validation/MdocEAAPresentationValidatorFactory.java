package eu.europa.esig.dss.eaa.mdoc.validation;

import eu.europa.esig.dss.eaa.common.validation.EAAPresentationValidatorFactory;
import eu.europa.esig.dss.model.DSSDocument;

/**
 * Processes and validated Electronic Attestation of Attributes (EAAs) provided within an mdoc document structure
 * as per ISO 18013-5.
 *
 */
public class MdocEAAPresentationValidatorFactory implements EAAPresentationValidatorFactory {

    /**
     * Default constructor
     */
    public MdocEAAPresentationValidatorFactory() {
        // empty
    }

    @Override
    public boolean isSupported(DSSDocument document) {
        MdocEAAPresentationValidator validator = new MdocEAAPresentationValidator();
        return validator.isSupported(document);
    }

    @Override
    public MdocEAAPresentationValidator create(DSSDocument document) {
        return new MdocEAAPresentationValidator(document);
    }

}
