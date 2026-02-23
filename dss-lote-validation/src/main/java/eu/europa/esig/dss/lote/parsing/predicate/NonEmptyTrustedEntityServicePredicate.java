package eu.europa.esig.dss.lote.parsing.predicate;

import eu.europa.esig.dss.model.lote.TrustedEntity;
import eu.europa.esig.dss.utils.Utils;

import java.util.function.Predicate;

/**
 * Verifies whether a Trusted Entity contains a list of corresponding trust entity services
 *
 */
public class NonEmptyTrustedEntityServicePredicate implements Predicate<TrustedEntity> {

    /**
     * Default constructor
     */
    public NonEmptyTrustedEntityServicePredicate() {
        // empty
    }

    @Override
    public boolean test(TrustedEntity t) {
        return Utils.isCollectionNotEmpty(t.getServices());
    }

}
