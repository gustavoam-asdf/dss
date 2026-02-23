package eu.europa.esig.dss.lote.parsing.predicate;

import eu.europa.esig.dss.model.lote.TrustedEntityService;

import java.util.function.Predicate;

/**
 * Verifies whether the Trusted Entity Service contains status and information extensions
 *
 */
public class NonEmptyTESInformationPredicate implements Predicate<TrustedEntityService>  {

    /**
     * Default constructor
     */
    public NonEmptyTESInformationPredicate() {
        // empty
    }

    @Override
    public boolean test(TrustedEntityService trustedEntityService) {
        return trustedEntityService.getStatusAndInformationExtensions() != null;
    }

}
