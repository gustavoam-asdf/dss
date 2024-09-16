package eu.europa.esig.dss.cbades.validation;

import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.spi.validation.analyzer.DocumentAnalyzerFactory;

/**
 * Loads the relevant Analyzer to process a given COSE signature
 *
 */
public class COSEDocumentAnalyzerFactory implements DocumentAnalyzerFactory {

    /**
     * Default constructor
     */
    public COSEDocumentAnalyzerFactory() {
        // empty
    }

    @Override
    public boolean isSupported(DSSDocument document) {
        COSEDocumentAnalyzer validator = new COSEDocumentAnalyzer();
        return validator.isSupported(document);
    }

    @Override
    public COSEDocumentAnalyzer create(DSSDocument document) {
        COSEDocumentAnalyzer validator = new COSEDocumentAnalyzer();
        if (validator.isSupported(document)) {
            return new COSEDocumentAnalyzer(document);
        }
        throw new IllegalArgumentException("Not supported document");
    }

}
