package eu.europa.esig.dss.spi.eaa.statuslist;

import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.spi.eaa.EAAStatusToken;

/**
 * Validates EAA's Token Status List (TSL)
 *
 */
public interface StatusListAnalyzer {

    /**
     * Checks if the document is supported by the current processor
     *
     * @param eaaDocument {@link DSSDocument} to check
     * @return TRUE if the document is supported, FALSE otherwise
     */
    boolean isSupported(DSSDocument eaaDocument);

    /**
     * Gets the resulted Token Status List (TSL) representation constructed from the processed sata
     *
     * @return {@link EAAStatusToken}
     */
    EAAStatusToken getStatusToken();

}
