package eu.europa.esig.dss.lote.cache.access;

import eu.europa.esig.dss.lote.dto.LoTEParsingCacheDTO;
import eu.europa.esig.dss.lote.job.LoTEReadOnlyCacheAccess;
import eu.europa.esig.dss.validation.job.cache.CacheKey;
import eu.europa.esig.dss.validation.job.cache.access.AbstractCacheAccessFactory;
import eu.europa.esig.dss.validation.job.dto.DownloadCacheDTO;
import eu.europa.esig.dss.validation.job.dto.ValidationCacheDTO;

/**
 * Accesses the cache for Trusted Lists validation job
 *
 */
public class LoTECacheAccessFactory extends AbstractCacheAccessFactory<DownloadCacheDTO, LoTEParsingCacheDTO, ValidationCacheDTO> {

    /**
     * Default constructor
     */
    public LoTECacheAccessFactory() {
        // empty
    }

    @Override
    public LoTECacheAccessByKey getCacheAccess(CacheKey key) {
        return new LoTECacheAccessByKey(key, downloadCache, parsingCache, validationCache);
    }

    @Override
    public LoTEReadOnlyCacheAccess getReadOnlyCacheAccess() {
        return new LoTEReadOnlyCacheAccess(downloadCache, parsingCache, validationCache);
    }

}
