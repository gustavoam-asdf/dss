package eu.europa.esig.dss.ws.eaa.creation.dto;


import eu.europa.esig.dss.ws.dto.SignatureValueDTO;
import eu.europa.esig.dss.ws.eaa.creation.dto.parameters.RemoteEAAPayloadParameters;
import eu.europa.esig.dss.ws.signature.dto.AbstractSignDocumentDTO;
import eu.europa.esig.dss.ws.signature.dto.parameters.RemoteSignatureParameters;

import java.util.Objects;

/**
 * DTO representing an input data for a signEAA method for EAA creation.
 * It's only possible to transfer an object by POST and REST.
 * It's impossible to transfer big objects by GET (url size limitation).
 *
 */
public class SignEAADTO extends AbstractSignDocumentDTO {

    private static final long serialVersionUID = 7028088598170070790L;

    /** The EAA payload parameters */
    private RemoteEAAPayloadParameters payloadParameters;

    /**
     * Empty constructor
     */
    public SignEAADTO() {
        super(null, null);
    }

    /**
     * Default constructor
     *
     * @param payloadParameters {@link RemoteEAAPayloadParameters}
     * @param parameters {@link RemoteSignatureParameters}
     * @param signatureValue {@link SignatureValueDTO}
     */
    public SignEAADTO(RemoteEAAPayloadParameters payloadParameters, RemoteSignatureParameters parameters, SignatureValueDTO signatureValue) {
        super(parameters, signatureValue);
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
        return "SignEAADTO [" +
                "payloadParameters=" + payloadParameters +
                "] " + super.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;

        SignEAADTO that = (SignEAADTO) object;
        return Objects.equals(payloadParameters, that.payloadParameters);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Objects.hashCode(payloadParameters);
        return result;
    }

}
