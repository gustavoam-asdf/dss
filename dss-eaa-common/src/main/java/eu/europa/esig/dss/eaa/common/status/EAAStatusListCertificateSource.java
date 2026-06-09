package eu.europa.esig.dss.eaa.common.status;

import eu.europa.esig.dss.enumerations.CertificateOrigin;
import eu.europa.esig.dss.model.eaa.claim.ClaimByteString;
import eu.europa.esig.dss.model.eaa.claim.ClaimStatus;
import eu.europa.esig.dss.model.eaa.claim.ClaimStatusList;
import eu.europa.esig.dss.model.x509.CertificateToken;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.spi.x509.TokenCertificateSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Contains certificates extracted from a Token Status List claim present in the EAA payload
 *
 */
public class EAAStatusListCertificateSource extends TokenCertificateSource {

    private static final long serialVersionUID = 3937099053754104602L;

    private static final Logger LOG = LoggerFactory.getLogger(EAAStatusListCertificateSource.class);

    /** The status list claim */
    private final ClaimStatusList claimStatusList;

    /**
     * Default constructor
     *
     * @param claimStatusList {@link ClaimStatus}
     */
    public EAAStatusListCertificateSource(final ClaimStatusList claimStatusList) {
        Objects.requireNonNull(claimStatusList, "Claim status cannot be null");
        this.claimStatusList = claimStatusList;

        extractCertificates();
    }

    private void extractCertificates() {
        ClaimByteString certificateByteString = claimStatusList.getCertificate();
        if (certificateByteString != null) {
            try {
                CertificateToken certificate = DSSUtils.loadCertificate(certificateByteString.getBinaryValue());
                addCertificate(certificate, CertificateOrigin.STATUS_LIST);
            } catch (Exception e) {
                LOG.warn("Unable to decode a certificate! Reason : {}", e.getMessage(), e);
            }
        }
    }

}
