package eu.europa.esig.dss.eaa.mdoc.creation;

import eu.europa.esig.dss.model.DSSDocument;

/**
 * Buils a DeviceAuthentication structure to use for key binding signature
 */
public interface MdocEAADeviceAuthenticationBuilder {

    /**
     * Buils the DeviceAuthentication structure based on the provided parameters
     *
     * @param keyBindingParameters {@link MdocKeyBindingParameters}
     * @return {@link DSSDocument} the built DeviceAuthentication
     */
    DSSDocument build(MdocKeyBindingParameters keyBindingParameters);
}
