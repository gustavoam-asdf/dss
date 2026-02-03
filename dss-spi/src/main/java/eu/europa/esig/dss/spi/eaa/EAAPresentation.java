package eu.europa.esig.dss.spi.eaa;

import eu.europa.esig.dss.enumerations.EAAPresentationType;
import eu.europa.esig.dss.model.eaa.DisclosureValidation;
import eu.europa.esig.dss.model.identifier.IdentifierBasedObject;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;

import java.util.List;

/**
 * This class represents a presentation of Electronic Attestation of Attributes
 *
 */
public interface EAAPresentation extends IdentifierBasedObject {

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
     * Gets the type of the Electronic Attestation of Attributes presentation it is built on
     *
     * @return {@link EAAPresentationType}
     */
    EAAPresentationType getEAAPresentationType();

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
     * This method returns the DSS unique id. It allows to unambiguously identify each token.
     *
     * @return {@link String} unique Id
     */
    String getId();

}
