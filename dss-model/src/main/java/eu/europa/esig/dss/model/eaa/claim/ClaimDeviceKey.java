package eu.europa.esig.dss.model.eaa.claim;

import eu.europa.esig.dss.model.Digest;
import eu.europa.esig.dss.model.x509.CertificateToken;

import java.security.PublicKey;
import java.util.List;
import java.util.Map;

/**
 * Represents a device key used for creating a key-binding signature.
 *
 */
public interface ClaimDeviceKey extends Claim {

    /**
     * Gets the public key
     *
     * @return {@link PublicKey}
     */
    PublicKey getPublicKey();

    /**
     * Gets a list of provided certificates
     *
     * @return a list of {@link CertificateToken}s
     */
    List<CertificateToken> getCertificates();

    /**
     * Gets a list of certificate digests
     *
     * @return a list of {@link Digest}s
     */
    List<Digest> getCertificateDigests();

    /**
     * Gets a list of certificate key identifiers (KID)
     *
     * @return a list of {@link String}s
     */
    List<String> getCertificateKeyIdentifiers();

    /**
     * Gets a list of certificate access URLs
     *
     * @return a list of {@link String}s
     */
    List<String> getCertificateUrls();

    /**
     * Gets a list of namespaces the key is authorized to sign
     *
     * @return a list of {@link String}s
     */
    List<String> getAuthorizedNamespaces();

    /**
     * Gets a map of namespaces and applicable data element lists the key is authorized to sign
     *
     * @return a map of {@link String} namespaces and lists of {@link String} data elements
     */
    Map<String, List<String>> getAuthorizedDataElements();

}
