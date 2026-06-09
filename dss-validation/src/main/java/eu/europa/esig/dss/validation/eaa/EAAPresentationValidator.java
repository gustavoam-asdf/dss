package eu.europa.esig.dss.validation.eaa;

import eu.europa.esig.dss.spi.eaa.EAAPresentation;
import eu.europa.esig.dss.spi.eaa.statuslist.EAAStatusSource;
import eu.europa.esig.dss.validation.DocumentValidator;

/**
 * This class is used to validate an Electronic Attestation of Attributes presentation
 *
 */
public interface EAAPresentationValidator extends DocumentValidator {

    /**
     * Gets EAAPresentation created from the provided document on validation
     *
     * @return {@link EAAPresentation}
     */
    EAAPresentation getEAAPresentation();

    /**
     * Sets the EAA status source providing access to the information about the EAA validity status
     *
     * @param eaaStatusSource {@link EAAStatusSource}
     */
    void setEAAStatusSource(EAAStatusSource eaaStatusSource);

}
