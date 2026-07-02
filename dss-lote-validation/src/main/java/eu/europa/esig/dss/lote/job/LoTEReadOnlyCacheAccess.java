package eu.europa.esig.dss.lote.job;

import eu.europa.esig.dss.lote.dto.LoTEParsingCacheDTO;
import eu.europa.esig.dss.lote.dto.builder.LoTEParsingCacheDTOBuilder;
import eu.europa.esig.dss.validation.job.cache.CacheKey;
import eu.europa.esig.dss.validation.job.cache.DownloadCache;
import eu.europa.esig.dss.validation.job.cache.ParsingCache;
import eu.europa.esig.dss.validation.job.cache.ValidationCache;
import eu.europa.esig.dss.validation.job.cache.access.AbstractCacheAccess;
import eu.europa.esig.dss.validation.job.cache.access.ParametrizedReadOnlyCacheAccess;
import eu.europa.esig.dss.validation.job.cache.state.CachedEntry;
import eu.europa.esig.dss.validation.job.download.DownloadResult;
import eu.europa.esig.dss.validation.job.dto.DownloadCacheDTO;
import eu.europa.esig.dss.validation.job.dto.ValidationCacheDTO;
import eu.europa.esig.dss.validation.job.dto.builder.DownloadCacheDTOBuilder;
import eu.europa.esig.dss.validation.job.dto.builder.ValidationCacheDTOBuilder;
import eu.europa.esig.dss.validation.job.parsing.ParsingResult;
import eu.europa.esig.dss.validation.job.validation.ValidationResult;

/**
 * Access the Trusted List cache in a read-only mode
 *
 */
public class LoTEReadOnlyCacheAccess extends AbstractCacheAccess implements ParametrizedReadOnlyCacheAccess<DownloadCacheDTO, LoTEParsingCacheDTO, ValidationCacheDTO> {

    /**
     * Default constructor
     *
     * @param fileCache       {@link DownloadCache}
     * @param parsingCache    {@link ParsingCache}
     * @param validationCache {@link ValidationCache}
     */
    public LoTEReadOnlyCacheAccess(DownloadCache fileCache, ParsingCache parsingCache, ValidationCache validationCache) {
        super(fileCache, parsingCache, validationCache);
    }

    @Override
    public DownloadCacheDTO getDownloadInfoRecord(CacheKey key) {
        CachedEntry<DownloadResult> downloadCacheEntry = getDownloadCacheEntry(key);
        return new DownloadCacheDTOBuilder(downloadCacheEntry).build();
    }

    @Override
    public LoTEParsingCacheDTO getParsingInfoRecord(CacheKey key) {
        CachedEntry<ParsingResult> parsingCacheEntry = getParsingCacheEntry(key);
        return new LoTEParsingCacheDTOBuilder(parsingCacheEntry).build();
    }

    @Override
    public ValidationCacheDTO getValidationInfoRecord(CacheKey key) {
        CachedEntry<ValidationResult> validationCacheEntry = getValidationCacheEntry(key);
        return new ValidationCacheDTOBuilder(validationCacheEntry).build();
    }

}
