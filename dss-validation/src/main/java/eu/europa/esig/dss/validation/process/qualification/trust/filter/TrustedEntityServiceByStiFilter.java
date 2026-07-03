package eu.europa.esig.dss.validation.process.qualification.trust.filter;

import eu.europa.esig.dss.diagnostic.TrustedEntityServiceWrapper;

/**
 * Filters trusted entity services by STI URI
 *
 */
public class TrustedEntityServiceByStiFilter extends AbstractTrustedEntityServiceFilter {

    /** Service Type Identifier Uri to filter by */
    private final String stiUri;

    /**
     * Default constructor
     *
     * @param stiUri {@link String} to filter Trusted Services with the given STI
     */
    public TrustedEntityServiceByStiFilter(String stiUri) {
        this.stiUri = stiUri;
    }

    @Override
    protected boolean isAcceptable(TrustedEntityServiceWrapper service) {
        return stiUri != null && stiUri.equals(service.getType());
    }

}
