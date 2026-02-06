package eu.europa.esig.dss.validation.executor.eaa;

import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.validation.executor.signature.DefaultSignatureProcessExecutor;
import eu.europa.esig.dss.validation.executor.signature.DetailedReportBuilder;

/**
 * Performs validation and reports building for EAA Presentation
 *
 */
public class EAAPresentationProcessExecutor extends DefaultSignatureProcessExecutor {

    /**
     * Default constructor
     */
    public EAAPresentationProcessExecutor() {
        // empty
    }

    @Override
    protected DetailedReportBuilder getDetailedReportBuilder(DiagnosticData diagnosticData) {
        return new DetailedReportForEAAPresentationBuilder(getI18nProvider(), currentTime, policy, diagnosticData, includeSemantics);
    }
}
