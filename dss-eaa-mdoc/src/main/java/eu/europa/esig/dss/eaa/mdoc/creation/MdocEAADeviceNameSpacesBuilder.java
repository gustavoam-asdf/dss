package eu.europa.esig.dss.eaa.mdoc.creation;

import eu.europa.esig.dss.cbades.cbor.CBORByteString;

/**
 * Builds the DeviceNameSpacesBytes to use for key binding signature
 */
public interface MdocEAADeviceNameSpacesBuilder {

    /**
     * Builds the DeviceNameSpacesBytes based on the provided parameters
     *
     * @param mdocEAADeviceSignedParameters {@link MdocEAADeviceSignedParameters}
     * @return {@link CBORByteString}
     */
    CBORByteString buildDeviceNameSpacesBytes(MdocEAADeviceSignedParameters mdocEAADeviceSignedParameters);
}
