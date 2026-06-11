package eu.europa.esig.dss.spi.eaa;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.EAAType;
import eu.europa.esig.dss.model.eaa.DisclosureValidation;
import eu.europa.esig.dss.model.identifier.IdentifierBasedObject;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import eu.europa.esig.dss.spi.x509.CertificateSource;

import java.util.List;

/**
 * This class represents a presentation of Electronic Attestation of Attributes
 *
 */
public interface EAA extends IdentifierBasedObject {

    /**
     * Returns a name of the presentation of Electronic Attestation of Attributes document, when present
     *
     * @return {@link String}
     */
    String getFilename();

    /**
     * Gets a list of signatures used to issue the Electronic Attestation of Attributes
     *
     * @return a list of {@link AdvancedSignature}s
     */
    List<AdvancedSignature> getSignatures();

    /**
     * Gets the type of the Electronic Attestation of Attributes
     *
     * @return {@link EAAType}
     */
    EAAType getEAAType();

    /**
     * Gets a list of validation results performed on the selectively disclosable claims
     *
     * @return list of {@link DisclosureValidation}
     */
    List<DisclosureValidation> getDisclosureValidations();

    /**
     * Gets key binding signature, when present
     *
     * @return {@link AdvancedSignature}
     */
    AdvancedSignature getKeyBindingSignature();

    /**
     * Gets key binding payload, when present
     *
     * @return {@link EAAKeyBindingPayload}
     */
    EAAKeyBindingPayload getKeyBindingSignaturePayload();

    /**
     * Gets the certificate source containing a public key or certificate representation of the device holder
     *
     * @return {@link CertificateSource}
     */
    CertificateSource getDeviceKeyCertificateSource();

    /**
     * Gets a clear payload of the Electronic Attestation of Attributes
     *
     * @return {@link EAAPayload}
     */
    EAAPayload getPayload();

    /**
     * Gets DigestAlgorithm used for selective disclosures hashes computation
     *
     * @return {@link DigestAlgorithm}
     */
    DigestAlgorithm getSelectiveDisclosuresDigestAlgorithm();

    /**
     * This method returns the DSS unique id. It allows to unambiguously identify each token.
     *
     * @return {@link String} unique Id
     */
    String getId();

}
