package eu.europa.esig.dss.tsl.cache.access;

import eu.europa.esig.dss.tsl.dto.TLParsingCacheDTO;
import eu.europa.esig.dss.tsl.job.TLReadOnlyCacheAccess;
import eu.europa.esig.dss.validation.job.cache.CacheKey;
import eu.europa.esig.dss.validation.job.cache.access.AbstractCacheAccessFactory;
import eu.europa.esig.dss.validation.job.dto.DownloadCacheDTO;
import eu.europa.esig.dss.validation.job.dto.ValidationCacheDTO;

/**
 * Accesses the cache for Trusted Lists validation job
 *
 */
public class TLCacheAccessFactory extends AbstractCacheAccessFactory<DownloadCacheDTO, TLParsingCacheDTO, ValidationCacheDTO> {

    /**
     * Default constructor
     */
    public TLCacheAccessFactory() {
        // empty
    }

    @Override
    public TLCacheAccessByKey getCacheAccess(CacheKey key) {
        return new TLCacheAccessByKey(key, downloadCache, parsingCache, validationCache);
    }

    @Override
    public TLReadOnlyCacheAccess getReadOnlyCacheAccess() {
        return new TLReadOnlyCacheAccess(downloadCache, parsingCache, validationCache);
    }

}
