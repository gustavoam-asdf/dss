package eu.europa.esig.dss.eaa.mdoc.validation;

import eu.europa.esig.dss.model.DSSDocument;

/**
 * Processes and validated Electronic Attestation of Attributes (EAA) embedded within an mdoc IssuerSigned structure
 *
 */
public class MdocIssuerSignedEAAPresentationValidator extends AbstractMdocEAAPresentationValidator {

    /**
     * Empty constructor
     */
    public MdocIssuerSignedEAAPresentationValidator() {
        super(new MdocIssuerSignedEAAPresentationAnalyzer());
    }

    /**
     * Default constructor
     *
     * @param document {@link DSSDocument} to validate
     */
    public MdocIssuerSignedEAAPresentationValidator(DSSDocument document) {
        super(new MdocIssuerSignedEAAPresentationAnalyzer(document));
    }

    @Override
    public MdocIssuerSignedEAAPresentationAnalyzer getDocumentAnalyzer() {
        return (MdocIssuerSignedEAAPresentationAnalyzer) super.getDocumentAnalyzer();
    }

}