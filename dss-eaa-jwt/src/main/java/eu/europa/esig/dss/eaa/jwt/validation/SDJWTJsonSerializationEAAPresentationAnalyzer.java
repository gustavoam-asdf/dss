package eu.europa.esig.dss.eaa.jwt.validation;

import eu.europa.esig.dss.eaa.jwt.SDJWTJsonSerializationParser;
import eu.europa.esig.dss.eaa.jwt.SDJWTSerializationObject;
import eu.europa.esig.dss.model.DSSDocument;

/**
 * This class performs analysis and processing of an SD-JWT VC token, created using either
 * a Flattened JSON Serialization or General JSON Serialization
 *
 */
public class SDJWTJsonSerializationEAAPresentationAnalyzer extends AbstractSDJWTEAAPresentationAnalyzer {

    /**
     * Empty constructor
     */
    public SDJWTJsonSerializationEAAPresentationAnalyzer() {
        // empty
    }

    /**
     * Default constructor
     *
     * @param document {@link DSSDocument} to validate
     */
    public SDJWTJsonSerializationEAAPresentationAnalyzer(DSSDocument document) {
        super(document);
    }

    @Override
    public boolean isSupported(DSSDocument document) {
        SDJWTJsonSerializationParser parser = new SDJWTJsonSerializationParser(document);
        return parser.isSupported();
    }

    @Override
    protected SDJWTSerializationObject buildSDJWTSerializationObject() {
        SDJWTJsonSerializationParser parser = new SDJWTJsonSerializationParser(document);
        return parser.parse();
    }
}
