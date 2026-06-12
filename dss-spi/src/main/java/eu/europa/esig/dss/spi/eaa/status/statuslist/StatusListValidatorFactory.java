package eu.europa.esig.dss.spi.eaa.status.statuslist;

/**
 * Loads a relevant implementation for an EAA's Token Status List (TSL) processing
 *
 */
public interface StatusListValidatorFactory {

    /**
     * This method tests if the current implementation of {@link StatusListValidator}
     * supports the given document
     *
     * @param eaaStatusList
     *                 the document to be tested
     * @return true, if the {@link StatusListValidator} supports the given document
     */
    boolean isSupported(byte[] eaaStatusList);

    /**
     * This method instantiates a {@link StatusListValidator} with the given document
     *
     * @param eaaStatusList
     *                 the document to be used for the {@link StatusListValidator}
     *                 creation
     * @return an instance of {@link StatusListValidator} with the document
     */
    StatusListValidator create(byte[] eaaStatusList);

}
