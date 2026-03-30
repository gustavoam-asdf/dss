package eu.europa.esig.dss.diagnostic.claim;

import eu.europa.esig.dss.diagnostic.jaxb.XmlDeviceKeyClaim;

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

    @Override
    public XmlDeviceKeyClaim getWrapped() {
        return (XmlDeviceKeyClaim) super.getWrapped();
    }

}
