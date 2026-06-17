package eu.europa.esig.dss.ws.eaa.creation.dto;


import eu.europa.esig.dss.ws.dto.RemoteDocument;
import eu.europa.esig.dss.ws.dto.SignatureValueDTO;
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

    /** The EAA payload */
    private RemoteDocument payloadDocument;

    /**
     * Empty constructor
     */
    public SignEAADTO() {
        super(null, null);
    }

    /**
     * Default constructor
     *
     * @param payloadDocument {@link RemoteDocument} to be signed
     * @param parameters {@link RemoteSignatureParameters}
     * @param signatureValue {@link SignatureValueDTO}
     */
    public SignEAADTO(RemoteDocument payloadDocument, RemoteSignatureParameters parameters, SignatureValueDTO signatureValue) {
        super(parameters, signatureValue);
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
        return "SignEAADTO [" +
                "payloadDocument=" + payloadDocument +
                "] " + super.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;

        SignEAADTO that = (SignEAADTO) object;
        return Objects.equals(payloadDocument, that.payloadDocument);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Objects.hashCode(payloadDocument);
        return result;
    }

}
