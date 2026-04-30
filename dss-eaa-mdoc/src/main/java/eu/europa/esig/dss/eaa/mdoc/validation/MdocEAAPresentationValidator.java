package eu.europa.esig.dss.eaa.mdoc.validation;

import eu.europa.esig.dss.cbades.validation.CBAdESDiagnosticDataBuilder;
import eu.europa.esig.dss.eaa.common.validation.DefaultEAAPresentationValidator;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.validation.reports.diagnostic.SignedDocumentDiagnosticDataBuilder;

/**
 * Processes and validated Electronic Attestation of Attributes (EAAs) embedded within an mdoc document structure
 *
 */
public class MdocEAAPresentationValidator extends DefaultEAAPresentationValidator {

    /**
     * Empty constructor
     */
    public MdocEAAPresentationValidator() {
        super(new MdocEAAPresentationAnalyzer());
    }

    /**
     * Default constructor
     *
     * @param document {@link DSSDocument} to validate
     */
    public MdocEAAPresentationValidator(DSSDocument document) {
        super(new MdocEAAPresentationAnalyzer(document));
    }

    /**
     * Sets the session transcript of communication used for the device retrieval (mdoc key binding signature)
     *
     * @param sessionTranscript {@link DSSDocument}
     */
    public void setSessionTranscript(DSSDocument sessionTranscript) {
        getDocumentAnalyzer().setSessionTranscript(sessionTranscript);
    }

    @Override
    public MdocEAAPresentationAnalyzer getDocumentAnalyzer() {
        return (MdocEAAPresentationAnalyzer) super.getDocumentAnalyzer();
    }

    @Override
    protected SignedDocumentDiagnosticDataBuilder getSignatureDiagnosticDataBuilder() {
        return new CBAdESDiagnosticDataBuilder();
    }

    @Override
    public SignedDocumentDiagnosticDataBuilder initializeDiagnosticDataBuilder() {
        return new MdocPresentationDiagnosticDataBuilder()
                .foundEAAPresentations(getEAAPresentations())
                .setSignatureDiagnosticDataBuilder(getSignatureDiagnosticDataBuilder());
    }

}