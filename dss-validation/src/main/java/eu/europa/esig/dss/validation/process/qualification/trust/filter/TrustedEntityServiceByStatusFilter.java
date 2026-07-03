package eu.europa.esig.dss.validation.process.qualification.trust.filter;

import eu.europa.esig.dss.diagnostic.TrustedEntityServiceWrapper;

/**
 * Filters trusted entity services by a status URI
 *
 */
public class TrustedEntityServiceByStatusFilter extends AbstractTrustedEntityServiceFilter {

    /** Service Status Uri to filter by */
    private final String statusUri;

    /**
     * Default constructor
     *
     * @param statusUri {@link String} to filter Trusted Services with the given status
     */
    public TrustedEntityServiceByStatusFilter(String statusUri) {
        this.statusUri = statusUri;
    }

    @Override
    protected boolean isAcceptable(TrustedEntityServiceWrapper service) {
        // if Status is NULL, it means no history entries are present, thus all services are valid
        if (statusUri == null) {
            return service.getStatus() == null;
        }
        return statusUri.equals(service.getStatus());
    }

}
