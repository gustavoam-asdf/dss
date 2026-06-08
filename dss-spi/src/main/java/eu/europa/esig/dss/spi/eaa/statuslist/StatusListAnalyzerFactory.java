package eu.europa.esig.dss.spi.eaa.statuslist;

import eu.europa.esig.dss.model.DSSDocument;

/**
 * Loads a relevant implementation for an EAA's Token Status List (TSL) processing
 *
 */
public interface StatusListAnalyzerFactory {

    /**
     * This method tests if the current implementation of {@link StatusListAnalyzer}
     * supports the given document
     *
     * @param eaaStatusList
     *                 the document to be tested
     * @return true, if the {@link StatusListAnalyzer} supports the given document
     */
    boolean isSupported(DSSDocument eaaStatusList);

    /**
     * This method instantiates a {@link StatusListAnalyzer} with the given document
     *
     * @param eaaStatusList
     *                 the document to be used for the {@link StatusListAnalyzer}
     *                 creation
     * @return an instance of {@link StatusListAnalyzer} with the document
     */
    StatusListAnalyzer create(DSSDocument eaaStatusList);

}
