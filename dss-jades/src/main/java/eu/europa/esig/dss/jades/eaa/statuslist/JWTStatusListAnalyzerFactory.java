package eu.europa.esig.dss.jades.eaa.statuslist;

import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.spi.eaa.statuslist.StatusListAnalyzer;
import eu.europa.esig.dss.spi.eaa.statuslist.StatusListAnalyzerFactory;

/**
 * Validates a Token Status List (TSL) provided in JWT Format, as defined in "5.1. Status List Token in JWT Format" of
 * {@link <a href="https://www.ietf.org/archive/id/draft-ietf-oauth-status-list-20.html">IETF Token Status List (TSL)</a>}.
 */
public class JWTStatusListAnalyzerFactory implements StatusListAnalyzerFactory {

    /**
     * Default constructor
     */
    public JWTStatusListAnalyzerFactory() {
        // empty
    }

    @Override
    public boolean isSupported(DSSDocument eaaStatusList) {
        return false;
    }

    @Override
    public StatusListAnalyzer create(DSSDocument eaaStatusList) {
        return null;
    }

}
