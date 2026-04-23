package eu.europa.esig.dss.diagnostic.claim;

import eu.europa.esig.dss.diagnostic.CertificateWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDeviceKeyClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDigestAlgoAndValue;
import eu.europa.esig.dss.diagnostic.jaxb.XmlX509Certificate;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides user-friendly access to the information present within a claim representing a wallet holder's key
 *
 */
public class DeviceKeyClaimWrapper extends ClaimWrapper {

    /**
     * Default constructor
     *
     * @param wrapped {@link XmlDeviceKeyClaim}
     */
    public DeviceKeyClaimWrapper(final XmlDeviceKeyClaim wrapped) {
        super(wrapped);
    }

    /**
     * Constructor with a parent provided
     *
     * @param wrapped {@link XmlDeviceKeyClaim}
     * @param parent {@link ClaimWrapper}
     */
    public DeviceKeyClaimWrapper(final XmlDeviceKeyClaim wrapped, final ClaimWrapper parent) {
        super(wrapped, parent);
    }

    /**
     * Gets the public key provided within the claim
     *
     * @return byte array representing the public key
     */
    public byte[] getPublicKey() {
        return getWrapped().getPublicKey();
    }

    /**
     * Gets a list of certificate tokens
     *
     * @return a list of {@link CertificateWrapper}s
     */
    public List<CertificateWrapper> getCertificates() {
        List<XmlX509Certificate> x509Certificates = getWrapped().getX509Certificate();
        if (x509Certificates != null && !x509Certificates.isEmpty()) {
            return x509Certificates.stream().map(x -> new CertificateWrapper(x.getCertificate())).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * Gets a list of certificate digests
     *
     * @return a list of {@link XmlDigestAlgoAndValue}s
     */
    public List<XmlDigestAlgoAndValue> getCertificateDigests() {
        return getWrapped().getDigestAlgoAndValue();
    }

    /**
     * Gets a list of certificate key identifiers
     *
     * @return a list of {@link String}s
     */
    public List<String> getKIDs() {
        return getWrapped().getKID();
    }

    /**
     * Gets a list of certificate access URLs
     *
     * @return a list of {@link String}s
     */
    public List<String> getX509URLs() {
        return getWrapped().getX509Url();
    }

    @Override
    public XmlDeviceKeyClaim getWrapped() {
        return (XmlDeviceKeyClaim) super.getWrapped();
    }

}
