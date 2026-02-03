package eu.europa.esig.dss.eaa.jwt.validation;

import eu.europa.esig.dss.model.DSSDocument;

/**
 * This class is used to perform validation of SD-JWT VC provided in a compact serialization form
 *
 */
public class SDJWTCompactEAAPresentationValidator extends AbstractSDJWTEAAPresentationValidator {

    /**
     * Empty constructor
     */
    public SDJWTCompactEAAPresentationValidator() {
        super(new SDJWTCompactEAAPresentationAnalyzer());
    }

    /**
     * Default constructor
     *
     * @param document {@link DSSDocument} to validate
     */
    public SDJWTCompactEAAPresentationValidator(DSSDocument document) {
        super(new SDJWTCompactEAAPresentationAnalyzer(document));
    }

    @Override
    public SDJWTCompactEAAPresentationAnalyzer getDocumentAnalyzer() {
        return (SDJWTCompactEAAPresentationAnalyzer) super.getDocumentAnalyzer();
    }

}
