package eu.europa.esig.dss.validation.process.qualification.trust.filter;

import eu.europa.esig.dss.diagnostic.TrustedEntityServiceWrapper;

import java.util.List;

/**
 * Filters {@code TrustedEntityServiceWrapper}s by the given conditions
 *
 */
public interface TrustedEntityServiceFilter {

    /**
     * Filters a list of {@code TrustedEntityServiceWrapper}s
     *
     * @param trustedServices a list of {@link TrustedEntityServiceWrapper}s to filter
     * @return filtered list of {@link TrustedEntityServiceWrapper}s
     */
    List<TrustedEntityServiceWrapper> filter(List<TrustedEntityServiceWrapper> trustedServices);

}
