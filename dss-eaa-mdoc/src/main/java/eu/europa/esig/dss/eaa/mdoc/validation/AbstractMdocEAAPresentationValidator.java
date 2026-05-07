package eu.europa.esig.dss.eaa.mdoc.validation;

import eu.europa.esig.dss.cbades.validation.CBAdESDiagnosticDataBuilder;
import eu.europa.esig.dss.eaa.common.validation.DefaultEAAPresentationAnalyzer;
import eu.europa.esig.dss.eaa.common.validation.DefaultEAAPresentationValidator;
import eu.europa.esig.dss.validation.reports.diagnostic.SignedDocumentDiagnosticDataBuilder;

/**
 * Abstract implementation of an mdoc document validator
 *
 */
public abstract class AbstractMdocEAAPresentationValidator extends DefaultEAAPresentationValidator {

    /**
     * Empty constructor
     *
     * @param eaaPresentationAnalyzer {@link DefaultEAAPresentationAnalyzer}
     */
    protected AbstractMdocEAAPresentationValidator(final DefaultEAAPresentationAnalyzer eaaPresentationAnalyzer) {
        super(eaaPresentationAnalyzer);
    }

    @Override
    protected SignedDocumentDiagnosticDataBuilder getSignatureDiagnosticDataBuilder() {
        return new CBAdESDiagnosticDataBuilder();
    }

    @Override
    public SignedDocumentDiagnosticDataBuilder initializeDiagnosticDataBuilder() {
        return new MdocPresentationDiagnosticDataBuilder()
                .foundEAAPresentation(getEAAPresentation())
                .setSignatureDiagnosticDataBuilder(getSignatureDiagnosticDataBuilder());
    }

}