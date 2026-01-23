package eu.europa.esig.dss.lote.xml.parsing.predicate;

import eu.europa.esig.dss.model.lote.TrustedEntity;
import eu.europa.esig.dss.utils.Utils;

import java.util.function.Predicate;

public class NonEmptyTEName implements Predicate<TrustedEntity>  {

    /**
     * Default constructor
     */
    public NonEmptyTEName() {
        // empty
    }

    @Override
    public boolean test(TrustedEntity trustedEntity) {
        return Utils.isMapNotEmpty(trustedEntity.getNames()) || Utils.isMapNotEmpty(trustedEntity.getTradeNames()) ;
    }

}
