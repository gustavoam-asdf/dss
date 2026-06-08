package eu.europa.esig.dss.spi.eaa;

import eu.europa.esig.dss.model.identifier.MultipleDigestIdentifier;

/**
 * Contains binaries of the Token Status List (TSL)
 *
 */
public class StatusTokenBinary extends MultipleDigestIdentifier {

    private static final long serialVersionUID = -5325232684459072796L;

    /**
     * Default constructor
     *
     * @param binaries token binaries
     */
    public StatusTokenBinary(byte[] binaries) {
        super("ST", binaries);
    }

}
