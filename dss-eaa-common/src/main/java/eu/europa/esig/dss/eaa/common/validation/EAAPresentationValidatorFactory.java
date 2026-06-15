package eu.europa.esig.dss.eaa.common.validation;

import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.validation.DocumentValidatorFactory;
import eu.europa.esig.dss.validation.eaa.EAAPresentationValidator;

/**
 * This interface defines the factory to create a {@link eu.europa.esig.dss.validation.eaa.EAAPresentationValidator} for
 * a given {@link DSSDocument}
 */
public interface EAAPresentationValidatorFactory extends DocumentValidatorFactory {

    /**
     * This method tests if the current implementation of {@link EAAPresentationValidator}
     * supports the given document
     *
     * @param document
     *                 the document to be tested
     * @return true, if the {@link EAAPresentationValidator} supports the given document
     */
    boolean isSupported(DSSDocument document);

    /**
     * This method instantiates a {@link EAAPresentationValidator} with the given document
     *
     * @param document
     *                 the document to be used for the {@link EAAPresentationValidator}
     *                 creation
     * @return an instance of {@link EAAPresentationValidator} with the document
     */
    DefaultEAAPresentationValidator create(DSSDocument document);

}