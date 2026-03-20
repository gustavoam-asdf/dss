package eu.europa.esig.dss.validation.eaa;

import eu.europa.esig.dss.spi.eaa.EAAPresentation;
import eu.europa.esig.dss.validation.DocumentValidator;

import java.util.List;

/**
 * This class is used to validate an Electronic Attestation of Attributes presentation
 *
 */
public interface EAAPresentationValidator extends DocumentValidator {

    /**
     * Gets EAAPresentations created from the provided document on validation
     *
     * @return a list of {@link EAAPresentation}s
     */
    List<EAAPresentation> getEAAPresentations();

}
