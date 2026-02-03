package eu.europa.esig.dss.eaa.common.validation;

import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.spi.eaa.EAAPresentation;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import eu.europa.esig.dss.spi.validation.analyzer.DefaultDocumentAnalyzer;
import eu.europa.esig.dss.spi.validation.analyzer.eaa.EAAPresentationAnalyzer;
import eu.europa.esig.dss.spi.validation.analyzer.eaa.EAAPresentationAnalyzerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Abstract class containing common code for validation of presentations of Electronic Attestation of Attributes.
 * This class can be used as the first point of the EAA presentation validation.
 *
 */
public abstract class DefaultEAAPresentationAnalyzer extends DefaultDocumentAnalyzer implements EAAPresentationAnalyzer {

    /** Cached instance of presentation of Electronic Attestation of Attributes */
    private EAAPresentation eaaPresentation;

    /**
     * Empty constructor
     */
    protected DefaultEAAPresentationAnalyzer() {
        // empty
    }

    /**
     * Instantiates the class with a document to be validated
     *
     * @param document {@link DSSDocument} to be validated
     */
    protected DefaultEAAPresentationAnalyzer(DSSDocument document) {
        Objects.requireNonNull(document, "Document to be validated cannot be null!");
        this.document = document;
    }

    /**
     * This method guesses the document format and returns an appropriate EAA presentation reader.
     *
     * @param dssDocument
     *            The instance of {@code DSSDocument} to validate
     * @return returns the specific instance of {@link EAAPresentationAnalyzer} in terms of the document type
     */
    public static EAAPresentationAnalyzer fromDocument(final DSSDocument dssDocument) {
        return EAAPresentationAnalyzerFactory.fromDocument(dssDocument);
    }

    @Override
    public EAAPresentation getEAAPresentation() {
        if (eaaPresentation == null) {
            eaaPresentation = buildEAAPresentation();
            // TODO : scopes ?
        }
        return eaaPresentation;
    }

    /**
     * Builds a presentation of Electronic Attestation of Attributes object
     *
     * @return {@link EAAPresentation}
     */
    protected abstract EAAPresentation buildEAAPresentation();

    @Override
    protected List<AdvancedSignature> getAllSignatures() {
        EAAPresentation presentation = getEAAPresentation();

        final List<AdvancedSignature> result = new ArrayList<>(presentation.getSignatures());
        if (presentation.getKeyBindingSignature() != null) {
            result.add(presentation.getKeyBindingSignature());
        }
        return result;
    }

    @Override
    public List<DSSDocument> getOriginalDocuments(String signatureId) {
        throw new UnsupportedOperationException("getOriginalDocuments(String signatureId) is " +
                "not supported for DefaultEAAPresentationAnalyzer!");
    }

    @Override
    public List<DSSDocument> getOriginalDocuments(AdvancedSignature advancedSignature) {
        throw new UnsupportedOperationException("getOriginalDocuments(AdvancedSignature advancedSignature) is " +
                "not supported for DefaultEAAPresentationAnalyzer!");
    }

}
