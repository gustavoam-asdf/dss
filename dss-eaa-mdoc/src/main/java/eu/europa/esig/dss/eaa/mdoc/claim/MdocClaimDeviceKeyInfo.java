package eu.europa.esig.dss.eaa.mdoc.claim;

import eu.europa.esig.dss.eaa.mdoc.COSEKeyParser;
import eu.europa.esig.dss.eaa.mdoc.MdocConstants;
import eu.europa.esig.dss.model.Digest;
import eu.europa.esig.dss.model.eaa.claim.ClaimByteString;
import eu.europa.esig.dss.model.eaa.claim.ClaimDeviceKey;
import eu.europa.esig.dss.model.eaa.claim.ClaimMap;
import eu.europa.esig.dss.model.eaa.claim.ClaimString;
import eu.europa.esig.dss.model.x509.CertificateToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PublicKey;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Mdoc representartion of the wallet holder's key, as defined in "9.1.2.4 Signing method and structure for MSO" of
 * ISO/IEC 18013-5 and further profiled in "9.1.5.2 Cipher suite".
 *
 */
public class MdocClaimDeviceKeyInfo extends MdocClaimMap implements ClaimDeviceKey {

    private static final Logger LOG = LoggerFactory.getLogger(MdocClaimDeviceKeyInfo.class);

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
        MdocClaimDeviceKey deviceKey = getDeviceKey();
        if (deviceKey != null) {
            try {
                return COSEKeyParser.from(deviceKey).parse();
            } catch (Exception e) {
                String errorMessage = "Unable to extract public key : {}";
                if (LOG.isDebugEnabled()) {
                    LOG.warn(errorMessage, e.getMessage(), e);
                } else {
                    LOG.warn(errorMessage, e.getMessage());
                }
            }
        }
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
        MdocClaimDeviceKey deviceKey = getDeviceKey();
        if (deviceKey != null) {
            ClaimByteString kid = deviceKey.getKID();
            if (kid != null) {
                // TODO : process as a string or b64 ?
                return Collections.singletonList(new String(kid.getBinaryValue()));
            }
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> getCertificateUrls() {
        return Collections.emptyList();
    }

    /**
     * Gets the device key claim value containing the representation of the key identifier claim
     *
     * @return {@link ClaimString}
     */
    public MdocClaimDeviceKey getDeviceKey() {
        ClaimMap deviceKey = getAsMap(MdocConstants.DEVICE_KEY);
        if (deviceKey != null) {
            return new MdocClaimDeviceKey(deviceKey);
        }
        return null;
    }

    @Override
    public List<String> getAuthorizedNamespaces() {
        MdocClaimKeyAuthorizations keyAuthorizations = getKeyAuthorizations();
        if (keyAuthorizations != null && keyAuthorizations.getAuthorizedNamespaces() != null) {
            return keyAuthorizations.getAuthorizedNamespaces().getNamespaces();
        }
        return Collections.emptyList();
    }

    @Override
    public Map<String, List<String>> getAuthorizedDataElements() {
        MdocClaimKeyAuthorizations keyAuthorizations = getKeyAuthorizations();
        if (keyAuthorizations != null && keyAuthorizations.getAuthorizedDataElements() != null) {
            return keyAuthorizations.getAuthorizedDataElements().getDataElements();
        }
        return Collections.emptyMap();
    }

    /**
     * Gets namespaces and data elements the key is authorized to sign or MAC
     *
     * @return {@link MdocClaimKeyAuthorizations}
     */
    public MdocClaimKeyAuthorizations getKeyAuthorizations() {
        ClaimMap keyAuthorizations = getAsMap(MdocConstants.KEY_AUTHORIZATIONS);
        if (keyAuthorizations != null) {
            return new MdocClaimKeyAuthorizations(keyAuthorizations);
        }
        return null;
    }

}
