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
     * @param eaaRevocationToken {@link EAARevocationToken}
     */
    public EAAStatusTokenIdentifier(EAARevocationToken eaaRevocationToken) {
        this("ST-", eaaRevocationToken);
    }

    /**
     * Internal constructor with a custom prefix
     *
     * @param prefix {@link String}
     * @param eaaRevocationToken {@link EAARevocationToken}
     */
    EAAStatusTokenIdentifier(String prefix, EAARevocationToken eaaRevocationToken) {
        super(prefix, eaaRevocationToken);
    }

}
