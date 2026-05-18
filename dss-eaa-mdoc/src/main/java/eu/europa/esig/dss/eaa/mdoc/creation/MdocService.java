package eu.europa.esig.dss.eaa.mdoc.creation;

import eu.europa.esig.dss.cbades.signature.CBAdESSignatureParameters;
import eu.europa.esig.dss.eaa.common.creation.AbstractEAAService;
import eu.europa.esig.dss.eaa.common.creation.claim.EAAClaim;
import eu.europa.esig.dss.spi.validation.CertificateVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This service is used to handle creation and issuance workflow for ISO/IEC 18013-5 mdoc EAAs and presentations
 *
 */
public class MdocService extends AbstractEAAService<CBAdESSignatureParameters, MdocPayloadBuilder, EAAClaim> {

    private static final long serialVersionUID = 6514504397480840459L;

    private static final Logger LOG = LoggerFactory.getLogger(MdocService.class);

    /**
     * Default constructor to instantiate an {@code SDJWTEAAService}
     *
     * @param certificateVerifier {@link CertificateVerifier}
     */
    public MdocService(final CertificateVerifier certificateVerifier) {
        super(certificateVerifier);
        LOG.debug("+ MdocService created");
    }

}
