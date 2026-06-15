package eu.europa.esig.dss.eaa.mdoc.validation;

import eu.europa.esig.dss.eaa.common.validation.DefaultEAAPresentationValidator;
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
        MdocDeviceResponseEAAPresentationValidator mdocDeviceResponseValidator = new MdocDeviceResponseEAAPresentationValidator();
        if (mdocDeviceResponseValidator.isSupported(document)) {
            return true;
        }

        MdocIssuerSignedEAAPresentationValidator mdocIssuerSignedValidator = new MdocIssuerSignedEAAPresentationValidator();
        if (mdocIssuerSignedValidator.isSupported(document)) {
            return true;
        }

        return false;
    }

    @Override
    public DefaultEAAPresentationValidator create(DSSDocument document) {
        MdocDeviceResponseEAAPresentationValidator mdocDeviceResponseValidator = new MdocDeviceResponseEAAPresentationValidator();
        if (mdocDeviceResponseValidator.isSupported(document)) {
            return new MdocDeviceResponseEAAPresentationValidator(document);
        }

        MdocIssuerSignedEAAPresentationValidator mdocIssuerSignedValidator = new MdocIssuerSignedEAAPresentationValidator();
        if (mdocIssuerSignedValidator.isSupported(document)) {
            return new MdocIssuerSignedEAAPresentationValidator(document);
        }

        throw new IllegalArgumentException("Not supported document");
    }

}
