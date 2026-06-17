package eu.europa.esig.dss.ws.eaa.creation.dto.parameters;

import eu.europa.esig.dss.enumerations.EAAType;

import java.util.List;

/**
 * DTO containing parameters for EAA Presentation issuance
 *
 */
public class RemoteEAAPresentationParameters {

    /** (Required) Type of the EAA to be created */
    private EAAType eaaType;

    /* Mdoc parameters */

    /** The list of device signed data elements */
    private List<ClaimDTO> deviceSignedDataElements;

    /**
     * Default constructor
     */
    public RemoteEAAPresentationParameters() {
        super();
    }

    /**
     * Constructor with EAA type provided
     */
    public RemoteEAAPresentationParameters(EAAType eaaType) {
        this.eaaType = eaaType;
    }

    /**
     * Gets the EAA Type
     *
     * @return {@link EAAType}
     */
    public EAAType getEaaType() {
        return eaaType;
    }

    /**
     * Sets the target EAA type
     *
     * @param eaaType {@link EAAType}
     */
    public void setEaaType(EAAType eaaType) {
        this.eaaType = eaaType;
    }

    /**
     * Gets the list of device signed data elements
     *
     * @return {@link List<ClaimDTO>}
     */
    public List<ClaimDTO> getDeviceSignedDataElements() {
        return deviceSignedDataElements;
    }

    /**
     * (Mdoc) Sets the list of device signed data elements
     *
     * @param deviceSignedDataElements {@link List<ClaimDTO>}
     */
    public void setDeviceSignedDataElements(final List<ClaimDTO> deviceSignedDataElements) {
        this.deviceSignedDataElements = deviceSignedDataElements;
    }

}
