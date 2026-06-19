package eu.europa.esig.dss.ws.eaa.creation.rest.client;

import eu.europa.esig.dss.ws.dto.RemoteDocument;
import eu.europa.esig.dss.ws.dto.ToBeSignedDTO;
import eu.europa.esig.dss.ws.eaa.creation.dto.CreateKeyBindingSignatureDTO;
import eu.europa.esig.dss.ws.eaa.creation.dto.DataToSignEAADTO;
import eu.europa.esig.dss.ws.eaa.creation.dto.DataToSignForKeyBindingSignatureDTO;
import eu.europa.esig.dss.ws.eaa.creation.dto.DisclosuresDTO;
import eu.europa.esig.dss.ws.eaa.creation.dto.IssuePresentationDTO;
import eu.europa.esig.dss.ws.eaa.creation.dto.SignEAADTO;
import eu.europa.esig.dss.ws.eaa.creation.dto.parameters.DisclosureDTO;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.io.Serializable;
import java.util.List;

/**
 * This REST interface provides operations for the signing of EAA and issuance of EAA presentations.
 *
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface RestEAACreationService extends Serializable {

    /**
     * Retrieves the bytes of the data that need to be signed based on the {@code payload} and {@code parameters}.
     *
     * @param dataToSignEAADTO {@link DataToSignEAADTO} a DTO with the needed
     *                         information (payload and signature parameters) to compute the data
     *                         to be signed
     * @return the data to be signed
     */
    @POST
    @Path("getDataToSign")
    ToBeSignedDTO getDataToSign(final DataToSignEAADTO dataToSignEAADTO);

    /**
     * Signs the EAA with the provided signatureValue.
     *
     * @param signEAADTO {@link SignEAADTO} a DTO with the needed
     *                   information (payload and signature parameters, signature value) to
     *                   generate the signed EAA
     * @return the signed document (signature signing the EAA)
     */
    @POST
    @Path("signEAA")
    RemoteDocument signEAA(final SignEAADTO signEAADTO);

    /**
     * Gets a list of disclosures for all selectively disclosable claims defined within the parameters
     *
     * @param disclosuresDTO {@link DisclosuresDTO} a DTO with the needed
     *                       information (payload parameters) to
     *                       generate the EAA disclosures
     * @return a list of disclosures
     */
    @POST
    @Path("getDisclosures")
    List<DisclosureDTO> getDisclosures(final DisclosuresDTO disclosuresDTO);

    /**
     * Created a DataToBeSigned (DTBS) for a key-binding signature creation, format specific.
     *
     * @param dataToSignForKeyBindingSignatureDTO {@link DataToSignForKeyBindingSignatureDTO} a DTO with the needed
     *                        information (signed EAA, disclosures, key binding and signature parameter) to compute
     *                        the data to be signed for key binding signature
     * @return the data to be signed
     */
    @POST
    @Path("getDataToSignForKeyBindingSignature")
    ToBeSignedDTO getDataToSignForKeyBindingSignature(final DataToSignForKeyBindingSignatureDTO dataToSignForKeyBindingSignatureDTO);

    /**
     * Creates a key-binding signature, format specific.
     *
     * @param createKeyBindingSignatureDTO {@link CreateKeyBindingSignatureDTO} a DTO with the needed information
     *                        (signed EAA, disclosures, key binding and signature parameters and signature value)
     *                        to create the key binding signature
     * @return the key-binding signature document
     */
    @POST
    @Path("createKeyBindingSignature")
    RemoteDocument createKeyBindingSignature(final CreateKeyBindingSignatureDTO createKeyBindingSignatureDTO);

    /**
     * Creates an EAA Presentation, with provided selective disclosures and key binding signature
     *
     * @param issuePresentationDTO {@link IssuePresentationDTO} a DTO with the needed information
     *                        (signed EAA, disclosures, key binding signature and parameters)
     *                        to issue the EAA Presentation
     * @return the EAA Presentation
     */
    @POST
    @Path("issuePresentation")
    RemoteDocument issuePresentation(final IssuePresentationDTO issuePresentationDTO);

}
