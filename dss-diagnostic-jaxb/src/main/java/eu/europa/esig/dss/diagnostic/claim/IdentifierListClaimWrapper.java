package eu.europa.esig.dss.diagnostic.claim;

import eu.europa.esig.dss.diagnostic.jaxb.XmlClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlIdentifierListClaim;

import java.util.HashMap;
import java.util.Map;

/**
 * Wraps an {@code eu.europa.esig.dss.diagnostic.jaxb.XmlIdentifierListClaim}
 *
 */
public class IdentifierListClaimWrapper extends ClaimWrapper {

    /**
     * Default constructor
     *
     * @param wrapped {@link XmlIdentifierListClaim}
     */
    public IdentifierListClaimWrapper(final XmlIdentifierListClaim wrapped) {
        super(wrapped);
    }

    /**
     * Constructor with a parent provided
     *
     * @param wrapped {@link XmlIdentifierListClaim}
     * @param parent {@link ClaimWrapper}
     */
    public IdentifierListClaimWrapper(final XmlIdentifierListClaim wrapped, final ClaimWrapper parent) {
        super(wrapped, parent);
    }

    /**
     * Gets the status's unique identifier
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getIdentifier() {
        XmlClaim identifier = getWrapped().getIdentifier();
        if (identifier != null) {
            return new ClaimWrapper(identifier, this);
        }
        return null;
    }

    /**
     * Gets the status's uri
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getUri() {
        XmlClaim uri = getWrapped().getUri();
        if (uri != null) {
            return new ClaimWrapper(uri, this);
        }
        return null;
    }

    /**
     * Gets the certificate containing the public key that signed or sealed the top-level
     * certificate in the x5chain element in the MSO revocation list structure
     *
     * @return {@link ClaimWrapper}
     */
    public ClaimWrapper getCertificate() {
        XmlClaim certificate = getWrapped().getCertificate();
        if (certificate != null) {
            return new ClaimWrapper(certificate, this);
        }
        return null;
    }

    @Override
    public boolean isMap() {
        return true;
    }

    @Override
    public Map<String, ClaimWrapper> getMap() {
        final Map<String, ClaimWrapper> result = new HashMap<>(super.getMap());
        ClaimWrapper identifier = getIdentifier();
        if (identifier != null) {
            result.put(identifier.getName(), identifier);
        }
        ClaimWrapper uri = getUri();
        if (uri != null) {
            result.put(uri.getName(), uri);
        }
        ClaimWrapper certificate = getCertificate();
        if (certificate != null) {
            result.put(certificate.getName(), certificate);
        }
        return result;
    }

    @Override
    public XmlIdentifierListClaim getWrapped() {
        return (XmlIdentifierListClaim) super.getWrapped();
    }

}
