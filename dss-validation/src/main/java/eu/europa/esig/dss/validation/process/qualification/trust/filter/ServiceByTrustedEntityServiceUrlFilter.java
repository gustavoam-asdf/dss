package eu.europa.esig.dss.validation.process.qualification.trust.filter;

import eu.europa.esig.dss.diagnostic.TrustedEntityServiceWrapper;
import eu.europa.esig.dss.utils.Utils;

import java.util.Collection;
import java.util.Collections;

/**
 * Filters trusted entity services by the given URLs
 *
 */
public class ServiceByTrustedEntityServiceUrlFilter extends AbstractTrustedEntityServiceFilter {

    /** Collection of Trusted Source URLs to filter by */
    private final Collection<String> tlUrls;

    /**
     * Constructor to instantiate the filter with a single Trusted Source URL
     *
     * @param tlUrl {@link String}
     */
    public ServiceByTrustedEntityServiceUrlFilter(String tlUrl) {
        this(Collections.singleton(tlUrl));
    }

    /**
     * Constructor to instantiate the filter with a set of Trusted Source URLs
     *
     * @param tlUrls a collection of {@link String}s
     */
    public ServiceByTrustedEntityServiceUrlFilter(Collection<String> tlUrls) {
        this.tlUrls = tlUrls;
    }

    @Override
    protected boolean isAcceptable(TrustedEntityServiceWrapper service) {
        for (String url : tlUrls) {
            if (Utils.areStringsEqualIgnoreCase(url, service.getTrustedSourceList().getUrl())) {
                return true;
            }
        }
        return false;
    }

}
