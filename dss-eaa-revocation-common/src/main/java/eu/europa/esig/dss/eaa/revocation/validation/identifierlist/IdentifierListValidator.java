package eu.europa.esig.dss.eaa.revocation.validation.identifierlist;

import eu.europa.esig.dss.spi.eaa.EAARevocationToken;

/**
 * Accepts an identifier list document and parses its value
 *
 */
public interface IdentifierListValidator {

    /**
     * Checks if the document is supported by the current processor
     *
     * @param identifierListDocument to check
     * @return TRUE if the document is supported, FALSE otherwise
     */
    boolean isSupported(byte[] identifierListDocument);

    /**
     * Gets the status of the token referenced in the original identifier list document with the given identifier
     *
     * @param identifier of the token to be verified
     * @return {@link EAARevocationToken}
     */
    EAARevocationToken getRevocationToken(byte[] identifier);

}
