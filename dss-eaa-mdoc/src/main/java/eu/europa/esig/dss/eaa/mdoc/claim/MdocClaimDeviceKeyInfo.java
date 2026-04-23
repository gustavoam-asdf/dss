package eu.europa.esig.dss.eaa.mdoc.claim;

import eu.europa.esig.dss.model.Digest;
import eu.europa.esig.dss.model.eaa.claim.ClaimDeviceKey;
import eu.europa.esig.dss.model.eaa.claim.ClaimMap;
import eu.europa.esig.dss.model.x509.CertificateToken;

import java.security.PublicKey;
import java.util.Collections;
import java.util.List;

/**
 * Mdoc representartion of the wallet holder's key, as defined in "9.1.2.4 Signing method and structure for MSO" of
 * ISO/IEC 18013-5 and further profiled in "9.1.5.2 Cipher suite".
 *
 */
public class MdocClaimDeviceKeyInfo extends MdocClaimMap implements ClaimDeviceKey {

    private static final long serialVersionUID = 4939740857897930307L;

    /**
     * Constructor to initialize MdocClaimDeviceKey from a ClaimMap
     *
     * @param value {@link ClaimMap}
     */
    public MdocClaimDeviceKeyInfo(ClaimMap value) {
        super(value.getName(), value.getNamespace(), value.getMapValue(), value.isSelectivelyDisclosable(), value.getParent());
    }

    @Override
    public PublicKey getPublicKey() {
        // TODO : to be implemented
        return null;
    }

    @Override
    public List<CertificateToken> getCertificates() {
        return Collections.emptyList();
    }

    @Override
    public List<Digest> getCertificateDigests() {
        return Collections.emptyList();
    }

    @Override
    public List<String> getCertificateKeyIdentifiers() {
        return Collections.emptyList();
    }

    @Override
    public List<String> getCertificateUrls() {
        return Collections.emptyList();
    }

}
