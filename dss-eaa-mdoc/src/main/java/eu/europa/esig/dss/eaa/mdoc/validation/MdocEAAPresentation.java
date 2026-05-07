package eu.europa.esig.dss.eaa.mdoc.validation;

import eu.europa.esig.dss.eaa.common.validation.DefaultEAAPresentation;
import eu.europa.esig.dss.eaa.mdoc.model.MdocDeviceResponse;

/**
 * Mdoc ISO/IEC 18013-5 EAA presentation
 *
 */
public class MdocEAAPresentation extends DefaultEAAPresentation {

    /** Represents a parsed mdoc DeviceResponse object */
    private MdocDeviceResponse mdocDeviceResponse;

    /**
     * Default constructor
     */
    public MdocEAAPresentation() {
        // empty
    }

    /**
     * Gets the mdoc DeviceResponse
     *
     * @return {@link MdocDeviceResponse}
     */
    public MdocDeviceResponse getMdocDeviceResponse() {
        return mdocDeviceResponse;
    }

    /**
     * Sets the mdoc DeviceResponse
     *
     * @param mdocDeviceResponse {@link MdocDeviceResponse}
     */
    public void setMdocDeviceResponse(MdocDeviceResponse mdocDeviceResponse) {
        this.mdocDeviceResponse = mdocDeviceResponse;
    }

}
