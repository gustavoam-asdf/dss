package eu.europa.esig.dss.lote.xml.parsing.predicate;

import eu.europa.esig.dss.model.lote.TrustedEntityService;

import java.util.function.Predicate;

public class NonEmptyTESInformation implements Predicate<TrustedEntityService>  {

    @Override
    public boolean test(TrustedEntityService trustedEntityService) {
        return trustedEntityService.getStatusAndInformationExtensions() != null;
    }

}
