package eu.europa.esig.dss.lote.parsing;

import eu.europa.esig.dss.model.lote.OtherListPointer;

import java.util.List;

/**
 * Represents a parsing result for a List of TS 119 602 Lists of Trusted Entities
 *
 */
public class LoLoTEParsingResult extends AbstractLoTEParsingResult {

    /** List of self pointers to the current list */
    private List<OtherListPointer> currentListPointers;

    /** List of List pointers */
    private List<OtherListPointer> otherListPointers;

    /**
     * Default constructor
     */
    public LoLoTEParsingResult() {
        super();
    }

    /**
     * Gets List of self pointer to the current list (i.e. used within the pivot processing)
     *
     * @return a list of {@link OtherListPointer}s
     */
    public List<OtherListPointer> getCurrentListPointers() {
        return currentListPointers;
    }

    /**
     * Sets List of self pointer to the current list
     *
     * @param currentListPointers a list of {@link OtherListPointer}s
     */
    public void setCurrentListPointers(List<OtherListPointer> currentListPointers) {
        this.currentListPointers = currentListPointers;
    }

    /**
     * Gets List to other TSL pointers
     *
     * @return a list of {@link OtherListPointer}s
     */
    public List<OtherListPointer> getOtherListPointers() {
        return otherListPointers;
    }

    /**
     * Sets List to other pointers
     *
     * @param otherListPointers a list of {@link OtherListPointer}s
     */
    public void setOtherListPointers(List<OtherListPointer> otherListPointers) {
        this.otherListPointers = otherListPointers;
    }

}
