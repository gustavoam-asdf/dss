package eu.europa.esig.dss.ws.eaa.creation.soap.client;

import eu.europa.esig.dss.ws.dto.RemoteDocument;
import eu.europa.esig.dss.ws.dto.ToBeSignedDTO;
import eu.europa.esig.dss.ws.eaa.creation.dto.CreateKeyBindingSignatureDTO;
import eu.europa.esig.dss.ws.eaa.creation.dto.DataToSignEAADTO;
import eu.europa.esig.dss.ws.eaa.creation.dto.DataToSignForKeyBindingSignatureDTO;
import eu.europa.esig.dss.ws.eaa.creation.dto.DisclosuresDTO;
import eu.europa.esig.dss.ws.eaa.creation.dto.IssuePresentationDTO;
import eu.europa.esig.dss.ws.eaa.creation.dto.SignEAADTO;
import eu.europa.esig.dss.ws.eaa.creation.dto.parameters.DisclosureDTO;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;

import java.io.Serializable;
import java.util.List;

/**
 * This SOAP interface provides operations for the signing of EAA and issuance of EAA presentations.
 *
 */
@WebService(targetNamespace = "http://eaa.creation.dss.esig.europa.eu/")
public interface SoapEAACreationService extends Serializable {

    /**
     * Retrieves the bytes of the data that need to be signed based on the {@code payload} and {@code parameters}.
     *
     * @param dataToSignEAADTO {@link DataToSignEAADTO} a DTO with the needed
     *                         information (payload and signature parameters) to compute the data
     *                         to be signed
     * @return the data to be signed
     */
    @WebResult(name = "response")
    ToBeSignedDTO getDataToSign(@WebParam(name = "dataToSignEAADTO") final DataToSignEAADTO dataToSignEAADTO);

    /**
     * Signs the EAA with the provided signatureValue.
     *
     * @param signEAADTO {@link SignEAADTO} a DTO with the needed
     *                   information (payload and signature parameters, signature value) to
     *                   generate the signed EAA
     * @return the signed document (signature signing the EAA)
     */
    @WebResult(name = "response")
    RemoteDocument signEAA(@WebParam(name = "signEAADTO") final SignEAADTO signEAADTO);

    /**
     * Gets a list of disclosures for all selectively disclosable claims defined within the parameters
     *
     * @param disclosuresDTO {@link DisclosuresDTO} a DTO with the needed
     *                       information (payload parameters) to
     *                       generate the EAA disclosures
     * @return a list of disclosures
     */
    @WebResult(name = "response")
    List<DisclosureDTO> getDisclosures(@WebParam(name = "disclosuresDTO") final DisclosuresDTO disclosuresDTO);

    /**
     * Created a DataToBeSigned (DTBS) for a key-binding signature creation, format specific.
     *
     * @param dataToSignForKeyBindingSignatureDTO {@link DataToSignForKeyBindingSignatureDTO} a DTO with the needed
     *                        information (signed EAA, disclosures, key binding and signature parameter) to compute
     *                        the data to be signed for key binding signature
     * @return the data to be signed
     */
    @WebResult(name = "response")
    ToBeSignedDTO getDataToSignForKeyBindingSignature(@WebParam(name = "dataToSignForKeyBindingSignatureDTO") final DataToSignForKeyBindingSignatureDTO dataToSignForKeyBindingSignatureDTO);

    /**
     * Creates a key-binding signature, format specific.
     *
     * @param createKeyBindingSignatureDTO {@link CreateKeyBindingSignatureDTO} a DTO with the needed information
     *                        (signed EAA, disclosures, key binding and signature parameters and signature value)
     *                        to create the key binding signature
     * @return the key-binding signature document
     */
    @WebResult(name = "response")
    RemoteDocument createKeyBindingSignature(@WebParam(name = "createKeyBindingSignatureDTO") final CreateKeyBindingSignatureDTO createKeyBindingSignatureDTO);

    /**
     * Creates an EAA Presentation, with provided selective disclosures and key binding signature
     *
     * @param issuePresentationDTO {@link IssuePresentationDTO} a DTO with the needed information
     *                        (signed EAA, disclosures, key binding signature and parameters)
     *                        to issue the EAA Presentation
     * @return the EAA Presentation
     */
    @WebResult(name = "response")
    RemoteDocument issuePresentation(@WebParam(name = "issuePresentationDTO") final IssuePresentationDTO issuePresentationDTO);

}
