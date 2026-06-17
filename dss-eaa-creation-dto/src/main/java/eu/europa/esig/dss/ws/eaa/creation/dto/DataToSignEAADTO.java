package eu.europa.esig.dss.ws.eaa.creation.dto;

import eu.europa.esig.dss.ws.dto.RemoteDocument;
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

    /** The EAA payload */
    private RemoteDocument payloadDocument;

    /**
     * Empty constructor
     */
    public DataToSignEAADTO() {
        super(null);
    }

    /**
     * Default constructor
     *
     * @param payloadDocument {@link RemoteDocument} payload to be signed
     * @param parameters {@link RemoteSignatureParameters}
     */
    public DataToSignEAADTO(RemoteDocument payloadDocument, RemoteSignatureParameters parameters) {
        super(parameters);
        this.payloadDocument = payloadDocument;
    }

    /**
     * Gets a pre-built EAA payload document
     *
     * @return {@link RemoteDocument}
     */
    public RemoteDocument getPayloadDocument() {
        return payloadDocument;
    }

    /**
     * Sets a pre-built EAA payload document
     *
     * @param payloadDocument {@link RemoteDocument}
     */
    public void setPayloadDocument(RemoteDocument payloadDocument) {
        this.payloadDocument = payloadDocument;
    }

    @Override
    public String toString() {
        return "DataToSignEAADTO [" +
                "payloadDocument=" + payloadDocument +
                "] " + super.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;

        DataToSignEAADTO that = (DataToSignEAADTO) object;
        return Objects.equals(payloadDocument, that.payloadDocument);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Objects.hashCode(payloadDocument);
        return result;
    }

}
