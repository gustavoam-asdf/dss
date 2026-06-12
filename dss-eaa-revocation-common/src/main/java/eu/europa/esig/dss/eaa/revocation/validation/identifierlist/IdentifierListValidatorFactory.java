package eu.europa.esig.dss.eaa.revocation.validation.identifierlist;

/**
 * Loads a relevant implementation for an EAA's Identifier List processing
 *
 */
public interface IdentifierListValidatorFactory {

    /**
     * This method tests if the current implementation of {@link IdentifierListValidator}
     * supports the given document
     *
     * @param eaaIdentifierList
     *                 the document to be tested
     * @return true, if the {@link IdentifierListValidator} supports the given document
     */
    boolean isSupported(byte[] eaaIdentifierList);

    /**
     * This method instantiates a {@link IdentifierListValidator} with the given document
     *
     * @param eaaIdentifierList
     *                 the document to be used for the {@link IdentifierListValidator}
     *                 creation
     * @return an instance of {@link IdentifierListValidator} with the document
     */
    IdentifierListValidator create(byte[] eaaIdentifierList);

}
