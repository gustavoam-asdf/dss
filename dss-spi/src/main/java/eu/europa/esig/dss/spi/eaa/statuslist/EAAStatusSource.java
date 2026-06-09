package eu.europa.esig.dss.spi.eaa.statuslist;

import eu.europa.esig.dss.spi.eaa.EAA;
import eu.europa.esig.dss.spi.eaa.EAAStatusToken;

/**
 * Executes an EAA Status request for the given EAA token using the Status List Token mechanism, as defined in
 * {@link <a href="https://www.ietf.org/archive/id/draft-ietf-oauth-status-list-20.html">IETF Token Status List (TSL)</a>}.
 *
 */
public interface EAAStatusSource {

    /**
     * Gets the resulting status_list token for the given {@code EAA}
     *
     * @param eaa {@link EAA} to get status value for
     * @return {@link EAAStatusToken}
     */
    EAAStatusToken getEAAStatus(EAA eaa);

}
