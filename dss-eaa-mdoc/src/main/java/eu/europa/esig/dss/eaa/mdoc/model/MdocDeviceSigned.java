package eu.europa.esig.dss.eaa.mdoc.model;

/**
 * DeviceSigned contains the mdoc authentication structure and the data elements protected by mdoc
 * authentication. nameSpaces contains the returned data elements as part of their corresponding
 * namespaces. nameSpaces is a mandatory element because the element is authenticated using mdoc
 * authentication. The DeviceNameSpaces structure can be an empty structure. The DeviceAuth structure
 * contains either the DeviceSignature or the DeviceMac element, both are defined in 9.1.3.
 *
 */
public class MdocDeviceSigned {

    /** Returned data elements */
    private MdocDeviceNameSpaces deviceNameSpaces;

    /** Contains the device authentication for mdoc authentication  */
    private MdocDeviceAuth deviceAuth;

    /**
     * Default constructor
     */
    public MdocDeviceSigned() {
        // empty
    }

    /**
     * Gets the returned data elements
     *
     * @return {@link MdocDeviceNameSpaces}
     */
    public MdocDeviceNameSpaces getDeviceNameSpaces() {
        return deviceNameSpaces;
    }

    /**
     * Sets the returned data elements
     *
     * @param deviceNameSpaces {@link MdocDeviceNameSpaces}
     */
    public void setDeviceNameSpaces(MdocDeviceNameSpaces deviceNameSpaces) {
        this.deviceNameSpaces = deviceNameSpaces;
    }

    /**
     * Gets the device authentication
     *
     * @return {@link MdocDeviceAuth}
     */
    public MdocDeviceAuth getDeviceAuth() {
        return deviceAuth;
    }

    /**
     * Sets the device authentication
     *
     * @param deviceAuth {@link MdocDeviceAuth}
     */
    public void setDeviceAuth(MdocDeviceAuth deviceAuth) {
        this.deviceAuth = deviceAuth;
    }

}
