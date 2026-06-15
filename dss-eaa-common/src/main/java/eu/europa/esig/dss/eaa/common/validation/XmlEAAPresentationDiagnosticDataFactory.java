package eu.europa.esig.dss.eaa.common.validation;

import eu.europa.esig.dss.validation.reports.diagnostic.DiagnosticDataBuilder;
import eu.europa.esig.dss.validation.reports.diagnostic.XmlDiagnosticDataFactory;

/**
 * Builds a Diagnostic Data for an EAA Presentation validation
 */
public class XmlEAAPresentationDiagnosticDataFactory extends XmlDiagnosticDataFactory {

    /**
     * Default constructor
     *
     * @param diagnosticDataBuilder {@link EAAPresentationDiagnosticDataBuilder}
     */
    public XmlEAAPresentationDiagnosticDataFactory(final EAAPresentationDiagnosticDataBuilder diagnosticDataBuilder) {
        super(diagnosticDataBuilder);
    }

    @Override
    protected DiagnosticDataBuilder initBuilder() {
        EAAPresentationDiagnosticDataBuilder builder = (EAAPresentationDiagnosticDataBuilder) super.initBuilder();
        if (validationContext instanceof EAAValidationContext) {
            EAAValidationContext eaaValidationContext = (EAAValidationContext) validationContext;
            return builder
                    .foundEAAPresentation(eaaValidationContext.getProcessedEAAPresentation())
                    .foundEAAStatusTokens(eaaValidationContext.getProcessedEAAStatusTokens());
        } else {
            throw new IllegalStateException("An instance of EAAValidationContext is expected! Please verify the configuration.");
        }
    }

}
