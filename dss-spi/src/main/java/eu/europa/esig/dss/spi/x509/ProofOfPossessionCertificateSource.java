package eu.europa.esig.dss.spi.x509;

import java.util.Set;

/**
 * Contains a list of keys, to help the recipient cryptographically confirm proof of possession of
 * the key by the token's presenter. Proof of possession of a key is also sometimes described as the presenter
 * being a holder-of-key.
 *
 */
public interface ProofOfPossessionCertificateSource extends CertificateSource {

    /**
     * Returns a Set of all certificate references representing the holder's public key certificate
     *
     * @return a Set of {@link CertificateRef}s
     */
    Set<CertificateRef> getAllCertificateRefs();

}
