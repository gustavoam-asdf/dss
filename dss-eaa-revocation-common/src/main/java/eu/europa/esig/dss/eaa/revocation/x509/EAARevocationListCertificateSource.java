package eu.europa.esig.dss.eaa.revocation.x509;

import eu.europa.esig.dss.enumerations.CertificateOrigin;
import eu.europa.esig.dss.enumerations.CertificateSourceType;
import eu.europa.esig.dss.model.eaa.claim.ClaimByteString;
import eu.europa.esig.dss.model.eaa.claim.ClaimRevocationList;
import eu.europa.esig.dss.model.eaa.claim.ClaimStatus;
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
public class EAARevocationListCertificateSource extends TokenCertificateSource {

    private static final long serialVersionUID = 3937099053754104602L;

    private static final Logger LOG = LoggerFactory.getLogger(EAARevocationListCertificateSource.class);

    /** The status list claim */
    private final ClaimRevocationList claimRevocationList;

    /**
     * Default constructor
     *
     * @param claimRevocationList {@link ClaimStatus}
     */
    public EAARevocationListCertificateSource(final ClaimRevocationList claimRevocationList) {
        Objects.requireNonNull(claimRevocationList, "Claim status cannot be null");
        this.claimRevocationList = claimRevocationList;

        extractCertificates();
    }

    private void extractCertificates() {
        ClaimByteString certificateByteString = claimRevocationList.getCertificate();
        if (certificateByteString != null) {
            try {
                CertificateToken certificate = DSSUtils.loadCertificate(certificateByteString.getBinaryValue());
                addCertificate(certificate, CertificateOrigin.EAA);
            } catch (Exception e) {
                LOG.warn("Unable to decode a certificate! Reason : {}", e.getMessage(), e);
            }
        }
    }

    @Override
    public CertificateSourceType getCertificateSourceType() {
        return CertificateSourceType.EAA;
    }

}
