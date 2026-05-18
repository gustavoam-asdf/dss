package eu.europa.esig.dss.eaa.common.creation;

import eu.europa.esig.dss.eaa.common.creation.claim.EAAClaim;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.SerializableSignatureParameters;
import eu.europa.esig.dss.model.SignatureValue;
import eu.europa.esig.dss.model.ToBeSigned;

import java.io.Serializable;
import java.util.List;

/**
 * This interface {@link EAAService} provides operations for the issuance of Electronic Attestations of Attributes (EAA).
 * This service provides the following functionalities:
 * - Signing and issuance of Electronic Attestation of Attributes (EAA);
 * - Generation and extraction of selectively disclosable claims;
 * - Generation of key binding (device binding) signature;
 * - Issuance of Electronic Attestation of Attributes EAA Presentation.
 *
 * @param <SP>
 *         implementation of signature parameters corresponding to the supported signature format
 * @param <B>
 *         implementation of EAA payload builder to the EAA format
 */
public interface EAAService<SP extends SerializableSignatureParameters, B extends EAAPayloadBuilder> extends Serializable {

    ToBeSigned getDataToBeSigned(DSSDocument payload, SP signatureParameters);

    ToBeSigned getDataToBeSigned(B payloadBuilder, SP signatureParameters);

    /**
     * Signs an EAA with the provided signatureValue.
     *
     * @param payload
     *            the EAA to sign
     * @param signatureParameters
     *            set of the driving signing parameters
     * @param signatureValue
     *            the signature value to incorporate
     * @return the signed EAA
     */
    DSSDocument signEAA(DSSDocument payload, SP signatureParameters, SignatureValue signatureValue);

    /**
     * Signs the payload with the provided signatureValue.
     *
     * @param payloadBuilder
     *            the payload builder
     * @param signatureParameters
     *            set of the driving signing parameters
     * @param signatureValue
     *            the signature value to incorporate
     * @return the signed EAA
     */
    DSSDocument signEAA(B payloadBuilder, SP signatureParameters, SignatureValue signatureValue);

    ToBeSigned getDataToSignForKeybindingSignature(DSSDocument eaa, SP signatureParameters);

    ToBeSigned getDataToSignForKeybindingSignature(DSSDocument eaa, List<String> disclosures, SP signatureParameters);

    DSSDocument createKeybindingSignature(DSSDocument eea, SP signatureParameters, SignatureValue signatureValue);

    DSSDocument createKeybindingSignature(DSSDocument eea, List<String> disclosures, SP signatureParameters, SignatureValue signatureValue);

    /**
     * This method allows to create a list of disclosures for the provided claims based on the provided parameters
     *
     * @param claims
     *         the list of {@link EAAClaim}s
     * @param payloadBuilder
     *         the payload builder
     * @return the list of disclosure as {@link String} Base64URL encoded
     */
    List<String> getDisclosures(List<EAAClaim> claims, B payloadBuilder);

    DSSDocument issuePresentation(DSSDocument eaa, List<String> disclosures);

    DSSDocument issuePresentation(DSSDocument eaa, DSSDocument keybinding);

    DSSDocument issuePresentation(DSSDocument eaa, List<String> disclosures, DSSDocument keyBinding);
}
