package eu.europa.esig.dss.ws.eaa.creation.dto.parameters;

import eu.europa.esig.dss.enumerations.EAAType;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * DTO containing parameters for EAA Presentation issuance
 *
 */
public class RemoteEAAPresentationParameters implements Serializable {

    private static final long serialVersionUID = 9020368962150645764L;

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

    @Override
    public String toString() {
        return "RemoteEAAPresentationParameters [" +
                "eaaType=" + eaaType +
                ", deviceSignedDataElements=" + deviceSignedDataElements +
                ']';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        RemoteEAAPresentationParameters that = (RemoteEAAPresentationParameters) object;
        return eaaType == that.eaaType
                && Objects.equals(deviceSignedDataElements, that.deviceSignedDataElements);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(eaaType);
        result = 31 * result + Objects.hashCode(deviceSignedDataElements);
        return result;
    }

}
