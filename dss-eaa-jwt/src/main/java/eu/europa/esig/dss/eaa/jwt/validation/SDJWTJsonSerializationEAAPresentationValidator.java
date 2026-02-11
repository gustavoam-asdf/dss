package eu.europa.esig.dss.eaa.jwt.validation;

import eu.europa.esig.dss.model.DSSDocument;

/**
 * Performs validation of an SD-JWT VC token presented in a JSON Serialization form.
 *
 */
public class SDJWTJsonSerializationEAAPresentationValidator extends AbstractSDJWTEAAPresentationValidator {

    /**
     * Empty constructor
     */
    public SDJWTJsonSerializationEAAPresentationValidator() {
        super(new SDJWTJsonSerializationEAAPresentationAnalyzer());
    }

    /**
     * Default constructor
     *
     * @param document {@link DSSDocument} to validate
     */
    public SDJWTJsonSerializationEAAPresentationValidator(DSSDocument document) {
        super(new SDJWTJsonSerializationEAAPresentationAnalyzer(document));
    }

    @Override
    public SDJWTJsonSerializationEAAPresentationAnalyzer getDocumentAnalyzer() {
        return (SDJWTJsonSerializationEAAPresentationAnalyzer) super.getDocumentAnalyzer();
    }

}
