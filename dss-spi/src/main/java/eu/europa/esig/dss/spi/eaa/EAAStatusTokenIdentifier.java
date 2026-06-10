package eu.europa.esig.dss.spi.eaa;

import eu.europa.esig.dss.model.identifier.TokenIdentifier;

/**
 * An identifier for an EAA Status Token
 *
 */
public class EAAStatusTokenIdentifier extends TokenIdentifier {

    private static final long serialVersionUID = -4702754467043154156L;

    /**
     * Default constructor
     *
     * @param eaaStatusToken {@link EAAStatusToken}
     */
    public EAAStatusTokenIdentifier(EAAStatusToken eaaStatusToken) {
        this("ST-", eaaStatusToken);
    }

    /**
     * Internal constructor with a custom prefix
     *
     * @param prefix {@link String}
     * @param eaaStatusToken {@link EAAStatusToken}
     */
    EAAStatusTokenIdentifier(String prefix, EAAStatusToken eaaStatusToken) {
        super(prefix, eaaStatusToken);
    }

}
