package eu.europa.esig.dss.eaa.jwt.validation;

import eu.europa.esig.dss.enumerations.CertificateRefOrigin;
import eu.europa.esig.dss.model.Digest;
import eu.europa.esig.dss.model.eaa.claim.ClaimDeviceKey;
import eu.europa.esig.dss.model.x509.CertificateToken;
import eu.europa.esig.dss.spi.x509.CertificateRef;
import eu.europa.esig.dss.spi.x509.ProofOfPossessionCertificateSource;
import eu.europa.esig.dss.spi.x509.TokenCertificateSource;
import eu.europa.esig.dss.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PublicKey;
import java.util.List;
import java.util.Objects;

/**
 * Certificate source containing certificates extracted from a "cnf" header (RFC 7800 "3.1. Confirmation Claim")
 *
 */
public class DeviceKeyClaimCertificateSource extends TokenCertificateSource implements ProofOfPossessionCertificateSource {

    private static final long serialVersionUID = 9142049850205709206L;

    private static final Logger LOG = LoggerFactory.getLogger(DeviceKeyClaimCertificateSource.class);

    /** The "cnf" claim value incorporated in SD-JWT */
    private final ClaimDeviceKey claimDeviceKey;

    /**
     * Default constructor
     *
     * @param claimDeviceKey {@link ClaimDeviceKey}
     */
    public DeviceKeyClaimCertificateSource(final ClaimDeviceKey claimDeviceKey) {
        Objects.requireNonNull(claimDeviceKey, "Device key claim cannot be null");
        this.claimDeviceKey = claimDeviceKey;

        extractPublicKey();
        extractCertificates();
        extractDigestReferences();
        extractKidReferences();
        extractUrlReferences();
    }

    private void extractPublicKey() {
        PublicKey publicKey = claimDeviceKey.getPublicKey();
        if (publicKey != null) {
            CertificateRef certificateRef = new CertificateRef();
            certificateRef.setPublicKey(publicKey);
            addCertificateRef(certificateRef, CertificateRefOrigin.PUBLIC_KEY);
        }
    }

    private void extractCertificates() {
        List<CertificateToken> certificates = claimDeviceKey.getCertificates();
        if (Utils.isCollectionNotEmpty(certificates)) {
            if (Utils.collectionSize(certificates) != 1) {
                LOG.warn("More than one certificate found in a 'x5c' certificate chain within a JWK confirmation claim!" );
            }
            for (CertificateToken certificateToken : certificates) {
                addCertificate(certificateToken); // TODO : add origin ?
            }
        }
    }

    private void extractDigestReferences() {
        List<Digest> certificateDigests = claimDeviceKey.getCertificateDigests();
        if (Utils.isCollectionNotEmpty(certificateDigests)) {
            for (Digest digest : certificateDigests) {
                CertificateRef certRef = new CertificateRef();
                certRef.setCertDigest(digest);
                addCertificateRef(certRef, CertificateRefOrigin.SIGNING_CERTIFICATE);
            }
        }
    }

    private void extractKidReferences() {
        List<String> certificateKeyIdentifiers = claimDeviceKey.getCertificateKeyIdentifiers();
        if (Utils.isCollectionNotEmpty(certificateKeyIdentifiers)) {
            for (String kid : certificateKeyIdentifiers) {
                CertificateRef certRef = new CertificateRef();
                certRef.setKid(kid);
                addCertificateRef(certRef, CertificateRefOrigin.KEY_IDENTIFIER);
            }
        }
    }

    private void extractUrlReferences() {
        List<String> certificateUrls = claimDeviceKey.getCertificateUrls();
        if (Utils.isCollectionNotEmpty(certificateUrls)) {
            for (String url : certificateUrls) {
                CertificateRef certRef = new CertificateRef();
                certRef.setX509Url(url);
                addCertificateRef(certRef, CertificateRefOrigin.X509_URL);
            }
        }
    }

}
