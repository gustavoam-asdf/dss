package eu.europa.esig.dss.eaa.common.validation;

import eu.europa.esig.dss.enumerations.EAAPresentationType;
import eu.europa.esig.dss.spi.eaa.EAAPresentation;
import eu.europa.esig.dss.spi.eaa.EAA;

import java.util.List;

/**
 * Abstract implementation of an EAA Presentation
 */
public abstract class DefaultEAAPresentation implements EAAPresentation {

    /** Type of the EAA Presentation */
    private EAAPresentationType eaaPresentationType;

    /** List of incorporated Electronic Attestations of Attributes */
    private List<EAA> electronicAttestationsOfAttributes;

    /**
     * Default constructor
     */
    protected DefaultEAAPresentation() {
        // empty
    }

    @Override
    public EAAPresentationType getEAAPresentationType() {
        return eaaPresentationType;
    }

    /**
     * Sets the type of the EAA Presentation document
     *
     * @param eaaPresentationType {@link EAAPresentationType}
     */
    public void setEAAPresentationType(EAAPresentationType eaaPresentationType) {
        this.eaaPresentationType = eaaPresentationType;
    }

    @Override
    public List<EAA> getElectronicAttestationsOfAttributes() {
        return electronicAttestationsOfAttributes;
    }

    /**
     * Sets a list of incorporated Electronic Attestations of Attributes
     *
     * @param electronicAttestationsOfAttributes a list of {@link EAA}
     */
    public void setElectronicAttestationsOfAttributes(List<EAA> electronicAttestationsOfAttributes) {
        this.electronicAttestationsOfAttributes = electronicAttestationsOfAttributes;
    }

}
