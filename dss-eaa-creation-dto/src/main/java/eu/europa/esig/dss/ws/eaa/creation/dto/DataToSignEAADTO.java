package eu.europa.esig.dss.ws.eaa.creation.dto;

import eu.europa.esig.dss.ws.eaa.creation.dto.parameters.RemoteEAAPayloadParameters;
import eu.europa.esig.dss.ws.signature.dto.AbstractDataToSignDTO;
import eu.europa.esig.dss.ws.signature.dto.parameters.RemoteSignatureParameters;

import java.util.Objects;

/**
 * DTO representing an input data for a getDataToSign method for EAA creation.
 * It's only possible to transfer an object by POST and REST.
 * It's impossible to transfer big objects by GET (url size limitation).
 *
 */
public class DataToSignEAADTO extends AbstractDataToSignDTO {

    private static final long serialVersionUID = 965643473429520606L;

    /** The EAA payload parameters */
    private RemoteEAAPayloadParameters payloadParameters;

    /**
     * Empty constructor
     */
    public DataToSignEAADTO() {
        super(null);
    }

    /**
     * Default constructor
     *
     * @param payloadParameters {@link RemoteEAAPayloadParameters}
     * @param parameters {@link RemoteSignatureParameters}
     */
    public DataToSignEAADTO(RemoteEAAPayloadParameters payloadParameters, RemoteSignatureParameters parameters) {
        super(parameters);
        this.payloadParameters = payloadParameters;
    }

    /**
     * Gets the payload parameters
     *
     * @return {@link RemoteEAAPayloadParameters}
     */
    public RemoteEAAPayloadParameters getPayloadParameters() {
        return payloadParameters;
    }

    /**
     * Sets a payload parameters
     *
     * @param payloadParameters {@link RemoteEAAPayloadParameters}
     */
    public void setPayloadParameters(RemoteEAAPayloadParameters payloadParameters) {
        this.payloadParameters = payloadParameters;
    }

    @Override
    public String toString() {
        return "DataToSignEAADTO [" +
                "payloadParameters=" + payloadParameters +
                "] " + super.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;

        DataToSignEAADTO that = (DataToSignEAADTO) object;
        return Objects.equals(payloadParameters, that.payloadParameters);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Objects.hashCode(payloadParameters);
        return result;
    }

}
