package eu.europa.esig.dss.lote.cache.access;

import eu.europa.esig.dss.lote.dto.LoTEParsingCacheDTO;
import eu.europa.esig.dss.lote.job.LoTEReadOnlyCacheAccess;
import eu.europa.esig.dss.validation.job.cache.CacheKey;
import eu.europa.esig.dss.validation.job.cache.DownloadCache;
import eu.europa.esig.dss.validation.job.cache.ParsingCache;
import eu.europa.esig.dss.validation.job.cache.ValidationCache;
import eu.europa.esig.dss.validation.job.cache.access.AbstractCacheAccessByKey;
import eu.europa.esig.dss.validation.job.dto.DownloadCacheDTO;
import eu.europa.esig.dss.validation.job.dto.ValidationCacheDTO;

/**
 * Accesses cache information for a Trusted List by key
 *
 */
public class LoTECacheAccessByKey extends AbstractCacheAccessByKey<DownloadCacheDTO, LoTEParsingCacheDTO, ValidationCacheDTO> {

    /**
     * Default constructor
     *
     * @param key                 {@link CacheKey} to use
     * @param downloadCache       {@link DownloadCache}
     * @param parsingCache        {@link ParsingCache}
     * @param validationCache     {@link ValidationCache}
     */
    public LoTECacheAccessByKey(CacheKey key, DownloadCache downloadCache, ParsingCache parsingCache, ValidationCache validationCache) {
        super(key, downloadCache, parsingCache, validationCache, new LoTEReadOnlyCacheAccess(downloadCache, parsingCache, validationCache));
    }

}
