package eu.europa.esig.dss.validation.process.qualification.trust.filter;

import eu.europa.esig.dss.diagnostic.TrustServiceWrapper;
import eu.europa.esig.dss.validation.process.qualification.trust.ServiceTypeIdentifier;

/**
 * This class filters trust services with the 'EAA/Q' service identifier type
 *
 */
public class QEAAServiceFilter extends AbstractTrustServiceFilter {

    /**
     * Default constructor
     */
    public QEAAServiceFilter() {
        // empty
    }

    @Override
    protected boolean isAcceptable(TrustServiceWrapper service) {
        return ServiceTypeIdentifier.isQEAA(service.getType());
    }

}
