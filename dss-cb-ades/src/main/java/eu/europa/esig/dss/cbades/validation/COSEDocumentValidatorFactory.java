package eu.europa.esig.dss.cbades.validation;

import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.validation.DocumentValidator;
import eu.europa.esig.dss.validation.DocumentValidatorFactory;

/**
 * This interface loads a factory to create a {@link DocumentValidator} for
 * a given COSE {@link DSSDocument}
 *
 */
public class COSEDocumentValidatorFactory implements DocumentValidatorFactory {

    /**
     * Default constructor
     */
    public COSEDocumentValidatorFactory() {
        // empty
    }

    @Override
    public boolean isSupported(DSSDocument document) {
        COSEDocumentValidator validator = new COSEDocumentValidator();
        return validator.isSupported(document);
    }

    @Override
    public COSEDocumentValidator create(DSSDocument document) {
        COSEDocumentValidator validator = new COSEDocumentValidator();
        if (validator.isSupported(document)) {
            return new COSEDocumentValidator(document);
        }
        throw new IllegalArgumentException("Not supported document");
    }

}