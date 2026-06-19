package eu.europa.esig.dss.ws.eaa.creation.dto;

import eu.europa.esig.dss.ws.dto.RemoteDocument;
import eu.europa.esig.dss.ws.eaa.creation.dto.parameters.DisclosureDTO;
import eu.europa.esig.dss.ws.eaa.creation.dto.parameters.RemoteKeyBindingParameters;
import eu.europa.esig.dss.ws.signature.dto.AbstractDataToSignDTO;
import eu.europa.esig.dss.ws.signature.dto.parameters.RemoteSignatureParameters;

import java.util.List;
import java.util.Objects;

/**
 * This class is a DTO to transfer required objects to execute getDataToSignForKeyBindingSignature method
 * It's only possible to transfer an object by POST and REST.
 * It's impossible to transfer big objects by GET (url size limitation)
 */
public class DataToSignForKeyBindingSignatureDTO extends AbstractDataToSignDTO {

    private static final long serialVersionUID = -9080107638635078347L;

    /** Signed EAA document */
    private RemoteDocument eaa;

    /** (Optional) List of disclosures */
    private List<DisclosureDTO> disclosures;

    /** Parameters for key binding signature creation */
    private RemoteKeyBindingParameters keyBindingParameters;

    /**
     * Empty constructor
     */
    public DataToSignForKeyBindingSignatureDTO() {
        super();
    }

    /**
     * Default constructor
     *
     * @param eaa {@link RemoteDocument} EAA document
     * @param disclosures a list of {@link DisclosureDTO}s
     * @param keyBindingParameters {@link RemoteKeyBindingParameters}
     * @param signatureParameters {@link RemoteSignatureParameters}
     */
    public DataToSignForKeyBindingSignatureDTO(RemoteDocument eaa, List<DisclosureDTO> disclosures,
                                               RemoteKeyBindingParameters keyBindingParameters, RemoteSignatureParameters signatureParameters) {
        super(signatureParameters);
        this.eaa = eaa;
        this.disclosures = disclosures;
        this.keyBindingParameters = keyBindingParameters;
    }

    /**
     * Gets the signed EAA document
     *
     * @return {@link RemoteDocument}
     */
    public RemoteDocument getEaa() {
        return eaa;
    }

    /**
     * Sets a signed EAA document
     *
     * @param eaa {@link RemoteDocument}
     */
    public void setEaa(RemoteDocument eaa) {
        this.eaa = eaa;
    }

    /**
     * Gets a list of disclosures
     *
     * @return a list of {@link DisclosureDTO}s
     */
    public List<DisclosureDTO> getDisclosures() {
        return disclosures;
    }

    /**
     * (Optional) Sets a list of disclosures
     *
     * @param disclosures a list of {@link DisclosureDTO}s
     */
    public void setDisclosures(List<DisclosureDTO> disclosures) {
        this.disclosures = disclosures;
    }

    /**
     * Gets key binding signature parameters
     *
     * @return {@link RemoteKeyBindingParameters}
     */
    public RemoteKeyBindingParameters getKeyBindingParameters() {
        return keyBindingParameters;
    }

    /**
     * Sets key binding signature parameters
     *
     * @param keyBindingParameters {@link RemoteKeyBindingParameters}
     */
    public void setKeyBindingParameters(RemoteKeyBindingParameters keyBindingParameters) {
        this.keyBindingParameters = keyBindingParameters;
    }

    @Override
    public String toString() {
        return "DataToSignForKeyBindingSignature [" +
                "eaa=" + eaa +
                ", disclosures=" + disclosures +
                ", keyBindingParameters=" + keyBindingParameters +
                "] " + super.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;

        DataToSignForKeyBindingSignatureDTO that = (DataToSignForKeyBindingSignatureDTO) object;
        return Objects.equals(eaa, that.eaa)
                && Objects.equals(disclosures, that.disclosures)
                && Objects.equals(keyBindingParameters, that.keyBindingParameters);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Objects.hashCode(eaa);
        result = 31 * result + Objects.hashCode(disclosures);
        result = 31 * result + Objects.hashCode(keyBindingParameters);
        return result;
    }

}
