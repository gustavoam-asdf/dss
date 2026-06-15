package eu.europa.esig.dss.spi.eaa;

import eu.europa.esig.dss.enumerations.EAAPresentationType;
import eu.europa.esig.dss.enumerations.EAAType;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;

import java.util.List;

/**
 * Represents an EAA Presentation document.
 *
 */
public interface EAAPresentation {

    /**
     * Gets the type of the Electronic Attestation of Attributes presentation
     *
     * @return {@link EAAType}
     */
    EAAPresentationType getEAAPresentationType();

    /**
     * Gets a list of signatures used to issue the Electronic Attestation of Attributes
     *
     * @return a list of {@link AdvancedSignature}s
     */
    List<EAA> getElectronicAttestationsOfAttributes();

}
