package eu.europa.esig.dss.eaa.mdoc.validation;

import eu.europa.esig.dss.model.DSSDocument;

/**
 * Processes and validated Electronic Attestation of Attributes (EAAs) embedded within an mdoc DeviceResponse structure
 *
 */
public class MdocDeviceResponseEAAPresentationValidator extends AbstractMdocEAAPresentationValidator {

    /**
     * Empty constructor
     */
    public MdocDeviceResponseEAAPresentationValidator() {
        super(new MdocDeviceResponseEAAPresentationAnalyzer());
    }

    /**
     * Default constructor
     *
     * @param document {@link DSSDocument} to validate
     */
    public MdocDeviceResponseEAAPresentationValidator(DSSDocument document) {
        super(new MdocDeviceResponseEAAPresentationAnalyzer(document));
    }

    @Override
    public MdocDeviceResponseEAAPresentationAnalyzer getDocumentAnalyzer() {
        return (MdocDeviceResponseEAAPresentationAnalyzer) super.getDocumentAnalyzer();
    }

}