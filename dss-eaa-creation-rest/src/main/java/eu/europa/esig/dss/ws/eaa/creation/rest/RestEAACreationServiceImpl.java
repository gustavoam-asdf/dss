package eu.europa.esig.dss.ws.eaa.creation.rest;

import eu.europa.esig.dss.ws.dto.RemoteDocument;
import eu.europa.esig.dss.ws.dto.ToBeSignedDTO;
import eu.europa.esig.dss.ws.eaa.creation.common.RemoteEAACreationService;
import eu.europa.esig.dss.ws.eaa.creation.dto.CreateKeyBindingSignatureDTO;
import eu.europa.esig.dss.ws.eaa.creation.dto.DataToSignEAADTO;
import eu.europa.esig.dss.ws.eaa.creation.dto.DataToSignForKeyBindingSignatureDTO;
import eu.europa.esig.dss.ws.eaa.creation.dto.DisclosuresDTO;
import eu.europa.esig.dss.ws.eaa.creation.dto.IssuePresentationDTO;
import eu.europa.esig.dss.ws.eaa.creation.dto.SignEAADTO;
import eu.europa.esig.dss.ws.eaa.creation.dto.parameters.DisclosureDTO;
import eu.europa.esig.dss.ws.eaa.creation.rest.client.RestEAACreationService;

import java.util.List;

/**
 * REST implementation of the remote EAA creation service
 *
 */
public class RestEAACreationServiceImpl implements RestEAACreationService {

    private static final long serialVersionUID = 3799568238390145342L;

    /** The service to use */
    private RemoteEAACreationService service;

    /**
     * Default construction instantiating object with null RemoteEAACreationService
     */
    public RestEAACreationServiceImpl() {
        // empty
    }

    /**
     * Sets the remote EAA creation service
     *
     * @param service {@link RemoteEAACreationService}
     */
    public void setService(RemoteEAACreationService service) {
        this.service = service;
    }

    @Override
    public ToBeSignedDTO getDataToSign(DataToSignEAADTO dataToSignEAADTO) {
        return service.getDataToSign(dataToSignEAADTO.getPayloadParameters(), dataToSignEAADTO.getParameters());
    }

    @Override
    public RemoteDocument signEAA(SignEAADTO signEAADTO) {
        return service.signEAA(signEAADTO.getPayloadParameters(), signEAADTO.getParameters(), signEAADTO.getSignatureValue());
    }

    @Override
    public List<DisclosureDTO> getDisclosures(DisclosuresDTO disclosuresDTO) {
        return service.getDisclosures(disclosuresDTO.getPayloadParameters());
    }

    @Override
    public ToBeSignedDTO getDataToSignForKeyBindingSignature(DataToSignForKeyBindingSignatureDTO dataToSignForKeyBindingSignatureDTO) {
        return service.getDataToSignForKeyBindingSignature(dataToSignForKeyBindingSignatureDTO.getEaa(),
                dataToSignForKeyBindingSignatureDTO.getDisclosures(), dataToSignForKeyBindingSignatureDTO.getKeyBindingParameters(),
                dataToSignForKeyBindingSignatureDTO.getParameters());
    }

    @Override
    public RemoteDocument createKeyBindingSignature(CreateKeyBindingSignatureDTO createKeyBindingSignatureDTO) {
        return service.createKeyBindingSignature(createKeyBindingSignatureDTO.getEaa(),
                createKeyBindingSignatureDTO.getDisclosures(), createKeyBindingSignatureDTO.getKeyBindingParameters(),
                createKeyBindingSignatureDTO.getParameters(), createKeyBindingSignatureDTO.getSignatureValue());
    }

    @Override
    public RemoteDocument issuePresentation(IssuePresentationDTO issuePresentationDTO) {
        return service.issuePresentation(issuePresentationDTO.getEaa(), issuePresentationDTO.getDisclosures(),
                issuePresentationDTO.getKeyBindingSignature(), issuePresentationDTO.getPresentationParameters());
    }

}
