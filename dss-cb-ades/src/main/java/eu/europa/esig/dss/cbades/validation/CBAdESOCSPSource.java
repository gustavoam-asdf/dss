package eu.europa.esig.dss.cbades.validation;

import eu.europa.esig.dss.spi.x509.revocation.ocsp.OfflineOCSPSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extracts and stores OCSPs from a CB-AdES signature
 *
 */
public class CBAdESOCSPSource extends OfflineOCSPSource {

    private static final Logger LOG = LoggerFactory.getLogger(CBAdESOCSPSource.class);

    /**
     * Default constructor
     */
    public CBAdESOCSPSource() {
        // TODO : implement uHeaders support
    }

}
