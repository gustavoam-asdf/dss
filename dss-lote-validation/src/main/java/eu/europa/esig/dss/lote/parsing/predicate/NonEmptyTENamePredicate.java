package eu.europa.esig.dss.lote.parsing.predicate;

import eu.europa.esig.dss.model.lote.TrustedEntity;
import eu.europa.esig.dss.utils.Utils;

import java.util.function.Predicate;

/**
 * This predicate is used to filter out all Trusted Entity entries with null or empty names
 *
 */
public class NonEmptyTENamePredicate implements Predicate<TrustedEntity>  {

    /**
     * Default constructor
     */
    public NonEmptyTENamePredicate() {
        // empty
    }

    @Override
    public boolean test(TrustedEntity trustedEntity) {
        return Utils.isMapNotEmpty(trustedEntity.getNames()) || Utils.isMapNotEmpty(trustedEntity.getTradeNames()) ;
    }

}
