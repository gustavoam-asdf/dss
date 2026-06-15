package eu.europa.esig.dss.spi.eaa;

import eu.europa.esig.dss.model.identifier.MultipleDigestIdentifier;

/**
 * Contains binaries of the EAA revocation token
 *
 */
public class EAARevocationTokenBinary extends MultipleDigestIdentifier {

    private static final long serialVersionUID = -5325232684459072796L;

    /**
     * Default constructor
     *
     * @param binaries token binaries
     */
    public EAARevocationTokenBinary(byte[] binaries) {
        super("EAAR", binaries);
    }

}
