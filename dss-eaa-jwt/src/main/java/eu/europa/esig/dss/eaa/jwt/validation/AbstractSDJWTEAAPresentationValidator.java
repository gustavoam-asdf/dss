package eu.europa.esig.dss.eaa.jwt.validation;

import eu.europa.esig.dss.eaa.common.validation.DefaultEAAPresentationValidator;
import eu.europa.esig.dss.jades.validation.JAdESDiagnosticDataBuilder;
import eu.europa.esig.dss.validation.reports.diagnostic.SignedDocumentDiagnosticDataBuilder;

/**
 * The abstract class for an SD-JWT VC validation.
 * <p>
 * In order to perform validation-process, please ensure the `dss-validation` module is loaded
 * within the dependencies list of your project.
 *
 */
public abstract class AbstractSDJWTEAAPresentationValidator extends DefaultEAAPresentationValidator {

    /**
     * Empty constructor
     *
     * @param analyzer {@link AbstractSDJWTEAAPresentationAnalyzer}
     */
    protected AbstractSDJWTEAAPresentationValidator(final AbstractSDJWTEAAPresentationAnalyzer analyzer) {
        super(analyzer);
    }

    @Override
    public AbstractSDJWTEAAPresentationAnalyzer getDocumentAnalyzer() {
        return (AbstractSDJWTEAAPresentationAnalyzer) super.getDocumentAnalyzer();
    }

    @Override
    protected SignedDocumentDiagnosticDataBuilder getSignatureDiagnosticDataBuilder() {
        return new JAdESDiagnosticDataBuilder();
    }

}