package eu.europa.esig.dss.ws.eaa.creation.dto;

import eu.europa.esig.dss.ws.eaa.creation.dto.parameters.RemoteEAAPayloadParameters;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO representing an input data for a getDisclosures method for EAA creation.
 * It's only possible to transfer an object by POST and REST.
 * It's impossible to transfer big objects by GET (url size limitation).
 *
 */
public class DisclosuresDTO implements Serializable {

    private static final long serialVersionUID = -2112601914515598469L;

    /** Configuration used for the payload computation */
    private RemoteEAAPayloadParameters payloadParameters;

    /**
     * Default constructor
     */
    public DisclosuresDTO() {
        super();
    }

    /**
     * Constructor with payload parameters
     *
     * @param payloadParameters {@link RemoteEAAPayloadParameters}
     */
    public DisclosuresDTO(RemoteEAAPayloadParameters payloadParameters) {
        this.payloadParameters = payloadParameters;
    }

    /**
     * Gets the EAA payload parameters
     *
     * @return {@link RemoteEAAPayloadParameters}
     */
    public RemoteEAAPayloadParameters getPayloadParameters() {
        return payloadParameters;
    }

    /**
     * Sets the EAA payload parameters
     *
     * @param payloadParameters {@link RemoteEAAPayloadParameters}
     */
    public void setPayloadParameters(RemoteEAAPayloadParameters payloadParameters) {
        this.payloadParameters = payloadParameters;
    }

    @Override
    public String toString() {
        return "DisclosuresDTO [" +
                "payloadParameters=" + payloadParameters +
                ']';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        DisclosuresDTO that = (DisclosuresDTO) object;
        return Objects.equals(payloadParameters, that.payloadParameters);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(payloadParameters);
    }

}
