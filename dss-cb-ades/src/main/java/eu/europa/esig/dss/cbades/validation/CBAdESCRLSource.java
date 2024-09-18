package eu.europa.esig.dss.cbades.validation;

import eu.europa.esig.dss.spi.x509.revocation.crl.OfflineCRLSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extracts and stores CRLs from a CB-AdES signature
 *
 */
public class CBAdESCRLSource extends OfflineCRLSource {

    private static final Logger LOG = LoggerFactory.getLogger(CBAdESCRLSource.class);

    /**
     * Default constructor
     */
    public CBAdESCRLSource() {
        // TODO : implement uHeaders support
    }

}
