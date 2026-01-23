package eu.europa.esig.dss.lote.xml.parsing.predicate;

import eu.europa.esig.dss.model.lote.TrustedEntity;
import eu.europa.esig.dss.utils.Utils;

import java.util.function.Predicate;

public class NonEmptyTrustedEntityService implements Predicate<TrustedEntity> {

    /**
     * Default constructor
     */
    public NonEmptyTrustedEntityService() {
        // empty
    }

    @Override
    public boolean test(TrustedEntity t) {
        return Utils.isCollectionNotEmpty(t.getServices());
    }

}
