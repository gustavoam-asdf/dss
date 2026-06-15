package eu.europa.esig.dss.eaa.jwt.validation;

import eu.europa.esig.dss.eaa.common.validation.DefaultEAAPresentationAnalyzer;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.spi.validation.analyzer.eaa.EAAPresentationAnalyzerFactory;

/**
 * This class is used to load a corresponding {@code eu.europa.esig.dss.spi.validation.analyzer.eaa.EAAPresentationAnalyzer}
 * for an SD-JWT VC validation
 *
 */
public class SDJWTEAAPresentationAnalyzerFactory implements EAAPresentationAnalyzerFactory {

    /**
     * Default constructor
     */
    public SDJWTEAAPresentationAnalyzerFactory() {
        // empty
    }

    @Override
    public boolean isSupported(DSSDocument document) {
        SDJWTCompactEAAPresentationAnalyzer compactAnalyzer = new SDJWTCompactEAAPresentationAnalyzer();
        if (compactAnalyzer.isSupported(document)) {
            return true;
        }

        SDJWTJsonSerializationEAAPresentationAnalyzer jsonSerializationAnalyzer = new SDJWTJsonSerializationEAAPresentationAnalyzer();
        if (jsonSerializationAnalyzer.isSupported(document)) {
            return true;
        }

        return false;
    }

    @Override
    public DefaultEAAPresentationAnalyzer create(DSSDocument document) {
        SDJWTCompactEAAPresentationAnalyzer compactAnalyzer = new SDJWTCompactEAAPresentationAnalyzer();
        if (compactAnalyzer.isSupported(document)) {
            return new SDJWTCompactEAAPresentationAnalyzer(document);
        }

        SDJWTJsonSerializationEAAPresentationAnalyzer jsonSerializationAnalyzer = new SDJWTJsonSerializationEAAPresentationAnalyzer();
        if (jsonSerializationAnalyzer.isSupported(document)) {
            return new SDJWTJsonSerializationEAAPresentationAnalyzer(document);
        }

        throw new IllegalArgumentException("Not supported document");
    }

}
