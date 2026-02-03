package eu.europa.esig.dss.spi.validation.analyzer.eaa;

import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.spi.validation.analyzer.DocumentAnalyzerFactory;

import java.util.Objects;
import java.util.ServiceLoader;

/**
 * This class is used to load a specific {@code eu.europa.esig.dss.spi.validation.analyzer.eaa.EAAPresentationAnalyzer}
 * based on the format of the provided presentation of Electronic Attestation of Attributes
 *
 */
public interface EAAPresentationAnalyzerFactory extends DocumentAnalyzerFactory {

    /**
     * This method tests if the current implementation of {@link EAAPresentationAnalyzer}
     * supports the given document
     *
     * @param document
     *                 the document to be tested
     * @return true, if the {@link EAAPresentationAnalyzer} supports the given document
     */
    boolean isSupported(DSSDocument document);

    /**
     * This method instantiates a {@link EAAPresentationAnalyzer} with the given document
     *
     * @param document
     *                 the document to be used for the {@link EAAPresentationAnalyzer}
     *                 creation
     * @return an instance of {@link EAAPresentationAnalyzer} with the document
     */
    EAAPresentationAnalyzer create(DSSDocument document);

    /**
     * Verifies if the {@code document} is supported by one of the implementations,
     * across {@code EAAPresentationValidatorFactory} instances found by ServiceLoader.
     *
     * @param document {@link DSSDocument} to verify
     * @return TRUE if the evidence record is supported by one of the found implementations, FALSE otherwise
     */
    static boolean isSupportedDocument(DSSDocument document) {
        Objects.requireNonNull(document, "DSSDocument is null");
        ServiceLoader<EAAPresentationAnalyzerFactory> serviceLoaders = ServiceLoader.load(EAAPresentationAnalyzerFactory.class);
        for (EAAPresentationAnalyzerFactory factory : serviceLoaders) {
            if (factory.isSupported(document)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates an {@code EAAPresentationValidator} by loading a corresponding implementation,
     * across {@code EAAPresentationValidatorFactory} instances found by ServiceLoader.
     *
     * @param document {@link DSSDocument} to load validator for
     * @return {@link EAAPresentationAnalyzer} if corresponding implementation found
     * @throws UnsupportedOperationException is the document format is not supported or implementation is not found
     */
    static EAAPresentationAnalyzer fromDocument(DSSDocument document) throws UnsupportedOperationException {
        Objects.requireNonNull(document, "DSSDocument is null");
        ServiceLoader<EAAPresentationAnalyzerFactory> serviceLoaders = ServiceLoader.load(EAAPresentationAnalyzerFactory.class);
        for (EAAPresentationAnalyzerFactory factory : serviceLoaders) {
            if (factory.isSupported(document)) {
                return factory.create(document);
            }
        }
        throw new UnsupportedOperationException("Document format not recognized/handled");
    }

}
