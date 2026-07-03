package eu.europa.esig.dss.validation.process.qualification.trust.filter;

import eu.europa.esig.dss.diagnostic.TrustedEntityServiceWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract implementation of the {@code eu.europa.esig.dss.validation.process.qualification.trust.filter.TrustedEntityServiceFilter}
 *
 */
public abstract class AbstractTrustedEntityServiceFilter implements TrustedEntityServiceFilter {

    /**
     * Default constructor
     */
    protected AbstractTrustedEntityServiceFilter() {
        // empty
    }

    @Override
    public List<TrustedEntityServiceWrapper> filter(List<TrustedEntityServiceWrapper> trustedServices) {
        List<TrustedEntityServiceWrapper> result = new ArrayList<>();
        for (TrustedEntityServiceWrapper service : trustedServices) {
            if (isAcceptable(service)) {
                result.add(service);
            }
        }
        return result;
    }

    /**
     * Checks whether the {@code service} is acceptable
     *
     * @param service {@link TrustedEntityServiceWrapper} to check
     * @return TRUE if the {@code service} is acceptable, FALSE otherwise
     */
    protected abstract boolean isAcceptable(TrustedEntityServiceWrapper service);

}
