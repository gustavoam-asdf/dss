package eu.europa.esig.dss.eaa.common.validation;

import eu.europa.esig.dss.enumerations.ValidationLevel;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.spi.eaa.EAAPresentation;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import eu.europa.esig.dss.validation.SignedDocumentValidator;
import eu.europa.esig.dss.validation.eaa.EAAPresentationValidator;
import eu.europa.esig.dss.validation.executor.DocumentProcessExecutor;
import eu.europa.esig.dss.validation.executor.eaa.EAAPresentationProcessExecutor;
import eu.europa.esig.dss.validation.policy.ValidationPolicyLoader;
import eu.europa.esig.dss.validation.reports.diagnostic.SignedDocumentDiagnosticDataBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;

/**
 * This class is used to perform a validation of a presentation of Electronic Attestation of Attributes (EAA)
 * <p>
 * In order to perform validation-process, please ensure the `dss-validation` module is loaded
 * within the dependencies list of your project.
 *
 */
public abstract class DefaultEAAPresentationValidator extends SignedDocumentValidator implements EAAPresentationValidator {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultEAAPresentationValidator.class);

    /** The path for default EAA Presentation policy */
    private static final String EAA_PRESENTATION_POLICY_LOCATION = "/policy/eaa-constraint.xml";

    /**
     * Empty constructor
     *
     * @param eaaPresentationAnalyzer {@link DefaultEAAPresentationAnalyzer}
     */
    protected DefaultEAAPresentationValidator(final DefaultEAAPresentationAnalyzer eaaPresentationAnalyzer) {
        super(eaaPresentationAnalyzer);
    }

    @Override
    public DefaultEAAPresentationAnalyzer getDocumentAnalyzer() {
        return (DefaultEAAPresentationAnalyzer) super.getDocumentAnalyzer();
    }

    /**
     * This method guesses the document format and returns an appropriate EAA Presentation validator.
     *
     * @param dssDocument
     *            The instance of {@code DSSDocument} to validate
     * @return returns the specific instance of {@link DefaultEAAPresentationValidator} in terms of the document type
     */
    public static DefaultEAAPresentationValidator fromDocument(final DSSDocument dssDocument) {
        Objects.requireNonNull(dssDocument, "DSSDocument is null");
        ServiceLoader<EAAPresentationValidatorFactory> serviceLoaders = ServiceLoader.load(EAAPresentationValidatorFactory.class);
        for (EAAPresentationValidatorFactory factory : serviceLoaders) {
            if (factory.isSupported(dssDocument)) {
                return factory.create(dssDocument);
            }
        }
        throw new UnsupportedOperationException("Document format not recognized/handled");
    }

    @Override
    public EAAPresentation getEAAPresentation() {
        return getDocumentAnalyzer().getEAAPresentation();
    }

    @Override
    public DocumentProcessExecutor getDefaultProcessExecutor() {
        return new EAAPresentationProcessExecutor();
    }

    @Override
    protected ValidationPolicyLoader fromDefaultValidationPolicyLoader() {
        return ValidationPolicyLoader.fromValidationPolicy(
                DefaultEAAPresentationValidator.class.getResourceAsStream(EAA_PRESENTATION_POLICY_LOCATION));
    }

    @Override
    public SignedDocumentDiagnosticDataBuilder initializeDiagnosticDataBuilder() {
        return new EAAPresentationDiagnosticDataBuilder()
                .foundEAAPresentations(Collections.singletonList(getEAAPresentation()))
                .setSignatureDiagnosticDataBuilder(getSignatureDiagnosticDataBuilder());
    }

    @Override
    public void setValidationLevel(ValidationLevel validationLevel) {
        LOG.info("#setValidationLevel method is not supported within the EAAPresentationValidator class! " +
                "The validation always corresponds to the BASIC_SIGNATURES level.");
    }

    /**
     * This method returns a signature format specific {@code DiagnosticDataBuilder}
     *
     * @return {@link SignedDocumentDiagnosticDataBuilder}
     */
    protected abstract SignedDocumentDiagnosticDataBuilder getSignatureDiagnosticDataBuilder();

    @Override
    public List<DSSDocument> getOriginalDocuments(AdvancedSignature advancedSignature) {
        throw new UnsupportedOperationException("#getOriginalDocuments(AdvancedSignature) is " +
                "not supported for EAAPresentationValidator!");
    }

}
