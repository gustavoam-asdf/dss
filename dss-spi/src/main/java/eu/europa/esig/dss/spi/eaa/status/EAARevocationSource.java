package eu.europa.esig.dss.spi.eaa.status;

import eu.europa.esig.dss.spi.eaa.EAA;
import eu.europa.esig.dss.spi.eaa.EAARevocationToken;

/**
 * Executes an EAA Status request for the given EAA token using the Status List Token mechanism, as defined in
 * <a href="https://www.ietf.org/archive/id/draft-ietf-oauth-status-list-20.html">IETF Token Status List (TSL)</a>.
 *
 */
public interface EAARevocationSource {

    /**
     * Gets the resulting revocation token for the given {@code EAA}
     *
     * @param eaa {@link EAA} to get revocation value for
     * @return {@link EAARevocationToken}
     */
    EAARevocationToken getEAARevocation(EAA eaa);

}
