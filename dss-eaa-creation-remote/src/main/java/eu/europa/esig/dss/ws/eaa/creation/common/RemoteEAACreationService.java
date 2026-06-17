package eu.europa.esig.dss.ws.eaa.creation.common;

import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.ws.dto.RemoteDocument;
import eu.europa.esig.dss.ws.dto.SignatureValueDTO;
import eu.europa.esig.dss.ws.dto.ToBeSignedDTO;
import eu.europa.esig.dss.ws.eaa.creation.dto.DisclosureDTO;
import eu.europa.esig.dss.ws.eaa.creation.dto.parameters.RemoteEAAPayloadParameters;
import eu.europa.esig.dss.ws.eaa.creation.dto.parameters.RemoteEAAPresentationParameters;
import eu.europa.esig.dss.ws.eaa.creation.dto.parameters.RemoteKeyBindingParameters;
import eu.europa.esig.dss.ws.signature.dto.parameters.RemoteSignatureParameters;

import java.io.Serializable;
import java.util.List;

/**
 * Remote service to perform creation of an Electronic Attestation of Attributes and/or issuance of an EAA Presentation
 *
 */
public interface RemoteEAACreationService extends Serializable {

    /**
     * Retrieves the bytes of the data that need to be signed based on the {@code payload} and {@code parameters}.
     *
     * @param payloadParameters
     *            parameters containing configuration for the payload generation
     * @param signatureParameters
     *            set of the driving signing parameters
     * @return the data to be signed
     * @throws DSSException
     *             if an error occurred
     */
    ToBeSignedDTO getDataToSign(final RemoteEAAPayloadParameters payloadParameters,
                                final RemoteSignatureParameters signatureParameters) throws DSSException;

    /**
     * Signs the payload with the provided signatureValue.
     *
     * @param payloadParameters
     *            parameters containing configuration for the payload generation
     * @param signatureParameters
     *            set of the driving signing parameters
     * @param signatureValue
     *            the signature value to incorporate
     * @return the signed document (signature signing the {@code payload})
     * @throws DSSException
     *             if an error occurred
     */
    RemoteDocument signEAA(final RemoteEAAPayloadParameters payloadParameters, final RemoteSignatureParameters signatureParameters,
                           final SignatureValueDTO signatureValue) throws DSSException;

    /**
     * Gets a list of disclosures for all selectively disclosable claims defined within the parameters
     *
     * @param payloadParameters
     *            the payload parameters
     * @return a list of disclosures
     * @throws DSSException
     *             if an error occurred
     */
    List<DisclosureDTO> getDisclosures(final RemoteEAAPayloadParameters payloadParameters) throws DSSException;

    /**
     * Created a DataToBeSigned (DTBS) for a key-binding signature creation, format specific.
     *
     * @param eaa
     *            document representing a signed EAA
     * @param disclosures
     *            (optional) a list of disclosures to be provided with the EAA presentation
     * @param keyBindingParameters
     *            key binding signature configuration
     * @param signatureParameters
     *            set of the driving signing parameters
     * @return the data to be signed
     * @throws DSSException
     *             if an error occurred
     */
    ToBeSignedDTO getDataToSignForKeyBindingSignature(final RemoteDocument eaa, final List<DisclosureDTO> disclosures,
                                                      final RemoteKeyBindingParameters keyBindingParameters, final RemoteSignatureParameters signatureParameters) throws DSSException;

    /**
     * Creates a key-binding signature, format specific.
     *
     * @param eaa
     *            document representing a signed EAA
     * @param disclosures
     *            (optional) a list of disclosures to be provided with the EAA presentation
     * @param keyBindingParameters
     *            key binding signature configuration
     * @param signatureParameters
     *            set of the driving signing parameters
     * @param signatureValue
     *            the signature value to incorporate
     * @return the key-binding signature document
     * @throws DSSException
     *             if an error occurred
     */
    RemoteDocument createKeyBindingSignature(final RemoteDocument eaa, final List<DisclosureDTO> disclosures,
                                             final RemoteKeyBindingParameters keyBindingParameters, final RemoteSignatureParameters signatureParameters,
                                             final SignatureValueDTO signatureValue) throws DSSException;

    /**
     * Creates an EAA Presentation, with provided selective disclosures and key binding signature
     *
     * @param eaa
     *            document representing a signed EAA
     * @param disclosures
     *            (optional) a list of disclosures to be provided with the EAA presentation
     * @param keyBinding
     *            (optional) document representing a key binding signature
     * @param presentationParameters
     *            configuration of the EAA Presentation
     * @return {@link DSSDocument} EAA Presentation
     */
    RemoteDocument issuePresentation(final RemoteDocument eaa, final List<DisclosureDTO> disclosures,
                                     final RemoteDocument keyBinding, final RemoteEAAPresentationParameters presentationParameters) throws DSSException;

}
