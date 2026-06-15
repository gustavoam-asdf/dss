package eu.europa.esig.dss.eaa.mdoc.model;

import eu.europa.esig.dss.cbades.COSESignStructure;
import eu.europa.esig.dss.cbades.cbor.CBORArray;

/**
 * The DeviceAuth structure contains either the DeviceSignature or the DeviceMac element, both are defined in 9.1.3.
 *
 */
public class MdocDeviceAuth {

    /** Contains a key binding signature */
    private COSESignStructure deviceSignature;

    /** Contains a MAC authentication signature */
    private CBORArray deviceMac;

    /**
     * Default constructor
     */
    public MdocDeviceAuth() {
        // empty
    }

    /**
     * Gets a device signature
     *
     * @return {@link COSESignStructure}
     */
    public COSESignStructure getDeviceSignature() {
        return deviceSignature;
    }

    /**
     * Sets a device signature
     *
     * @param deviceSignature {@link COSESignStructure}
     */
    public void setDeviceSignature(COSESignStructure deviceSignature) {
        this.deviceSignature = deviceSignature;
    }

    /**
     * Gets a device MAC structure
     *
     * @return {@link CBORArray}
     */
    public CBORArray getDeviceMac() {
        return deviceMac;
    }

    /**
     * Sets a device MAC structure
     *
     * @param deviceMac {@link CBORArray}
     */
    public void setDeviceMac(CBORArray deviceMac) {
        this.deviceMac = deviceMac;
    }

}
