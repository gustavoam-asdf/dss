package eu.europa.esig.dss.lote.source;

import eu.europa.esig.dss.model.lote.OtherListPointer;

import java.util.function.Predicate;

/**
 * Represents a List of LoTEs definition
 *
 */
public class LoLoTESource extends LoTESource {

    /**
     * Predicate which filters the LoLoTEs
     */
    private Predicate<OtherListPointer> lolotePredicate;

    /**
     * Allows specifying pointers to other lists to be extracted during the parsing process
     */
    private Predicate<OtherListPointer> lotePredicate;

    /**
     * Default constructor instantiating object with null values
     */
    public LoLoTESource() {
        // empty
    }

    /**
     * Gets a LoLoTE predicate to filter lists of lists
     *
     * @return LoLoTE predicate
     */
    public Predicate<OtherListPointer> getLolotePredicate() {
        return lolotePredicate;
    }

    /**
     * Sets a LoLoTE filtering predicate
     *
     * @param lolotePredicate LoLoTE predicate
     */
    public void setLolotePredicate(Predicate<OtherListPointer> lolotePredicate) {
        this.lolotePredicate = lolotePredicate;
    }

    /**
     * Gets a predicate to filter {@code OtherListPointer} in order to extract pointers to other Lists
     *
     * @return other lists pointer predicate
     */
    public Predicate<OtherListPointer> getLotePredicate() {
        return lotePredicate;
    }

    /**
     * Sets a predicate allowing to filter {@code OtherListPointer} in order to extract pointers to other Lists,
     * to be used for further processing (for instance, pointers to other LoTEs from a LoLoTE).
     *
     * @param lotePredicate other lists pointer predicate
     */
    public void setLotePredicate(Predicate<OtherListPointer> lotePredicate) {
        this.lotePredicate = lotePredicate;
    }

}
