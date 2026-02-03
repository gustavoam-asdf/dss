package eu.europa.esig.dss.eaa.common.validation.identifier;

import eu.europa.esig.dss.model.identifier.MultipleDigestIdentifier;

/**
 * Builds identifier for EAA Presentation token
 *
 */
public class EAAPresentationIdentifier extends MultipleDigestIdentifier {

    private static final long serialVersionUID = 6359543697190790257L;

    /**
     * Default constructor
     *
     * @param binaries token binaries
     */
    protected EAAPresentationIdentifier(byte[] binaries) {
        super("EAA-", binaries);
    }

}
