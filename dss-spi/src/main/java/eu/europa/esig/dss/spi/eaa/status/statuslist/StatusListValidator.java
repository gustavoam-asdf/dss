package eu.europa.esig.dss.spi.eaa.status.statuslist;

import eu.europa.esig.dss.spi.eaa.EAAStatusToken;

/**
 * Accepts a status list document and parses its value as an EAA's Token Status List (TSL)
 *
 */
public interface StatusListValidator {

    /**
     * Checks if the document is supported by the current processor
     *
     * @param statusListDocument to check
     * @return TRUE if the document is supported, FALSE otherwise
     */
    boolean isSupported(byte[] statusListDocument);

    /**
     * Gets the status of the token referenced in the original status list document with the given index
     *
     * @param index of the token to be verified
     * @return {@link EAAStatusToken}
     */
    EAAStatusToken getStatusToken(int index);

}
