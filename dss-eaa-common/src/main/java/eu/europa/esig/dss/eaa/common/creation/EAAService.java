package eu.europa.esig.dss.eaa.common.creation;

import java.util.List;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.SerializableSignatureParameters;
import eu.europa.esig.dss.model.SignatureValue;
import eu.europa.esig.dss.model.ToBeSigned;

/**
 * This interface {@link EAAService} provides operations for the issuance of Electronic Attestations of Attributes (EAA).
 *
 * @param <SP>
 *         implementation of signature parameters corresponding to the supported signature format
 * @param <P>
 *         implementation of EAA parameters corresponding to the EAA format
 * @param <C>
 *         Type of a claim for this implementation
 */
public interface EAAService<SP extends SerializableSignatureParameters, P extends EAAParameters, C> {

    ToBeSigned getDataToBeSigned(DSSDocument payload, SP signatureParameters);

    ToBeSigned getDataToBeSigned(P eaaParameters, SP signatureParameters);

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
     * @param eaaParameters
     *            the parameters to use to generate the payload
     * @param signatureParameters
     *            set of the driving signing parameters
     * @param signatureValue
     *            the signature value to incorporate
     * @return the signed EAA
     */
    DSSDocument signEAA(P eaaParameters, SP signatureParameters, SignatureValue signatureValue);

    ToBeSigned getDataToSignForKeybindingSignature(DSSDocument eaa, SP signatureParameters);

    ToBeSigned getDataToSignForKeybindingSignature(DSSDocument eaa, List<String> disclosures, SP signatureParameters);

    DSSDocument createKeybindingSignature(DSSDocument eea, SP signatureParameters, SignatureValue signatureValue);

    DSSDocument createKeybindingSignature(DSSDocument eea, List<String> disclosures, SP signatureParameters, SignatureValue signatureValue);

    /**
     * This method allows to create a list of disclosures for the provided claims based on the provided parameters
     *
     * @param claims
     *         the list of claims
     * @param eaaParameters
     *         the parameters
     * @return the list of disclosure as {@link String} Base64URL encoded
     */
    List<String> getDisclosures(List<C> claims, P eaaParameters);

    /**
     * This method allows to create a list of disclosures for the provided claims based on the provided digest algorithm
     *
     * @param claims
     *         the list of claims
     * @param digestAlgorithm
     *         the {@link DigestAlgorithm}
     * @return the list of disclosure as {@link String} Base64URL encoded
     */
    List<String> getDisclosures(List<C> claims, DigestAlgorithm digestAlgorithm);

    DSSDocument issuePresentation(DSSDocument eaa, List<String> disclosures);

    DSSDocument issuePresentation(DSSDocument eaa, DSSDocument keybinding);

    DSSDocument issuePresentation(DSSDocument eaa, List<String> disclosures, DSSDocument keyBinding);
}
