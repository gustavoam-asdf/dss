package eu.europa.esig.dss.validation.job.cache.access;

import eu.europa.esig.dss.model.job.DownloadInfoRecord;
import eu.europa.esig.dss.model.job.ParsingInfoRecord;
import eu.europa.esig.dss.model.job.ValidationInfoRecord;
import eu.europa.esig.dss.validation.job.cache.CacheKey;

import java.util.Set;

/**
 * Access cache in read-only mode
 *
 */
public interface ReadOnlyCacheAccess {

    /**
     * Returns download cache DTO result
     *
     * @param key {@link CacheKey} to extract download result for
     * @return {@link DownloadInfoRecord}
     */
    DownloadInfoRecord getDownloadInfoRecord(final CacheKey key);

    /**
     * Returns download cache DTO result
     *
     * @param key {@link CacheKey} to extract parsing result for
     * @return {@link ParsingInfoRecord}
     */
    ParsingInfoRecord getParsingInfoRecord(final CacheKey key);

    /**
     * Returns download cache DTO result
     *
     * @param key {@link CacheKey} to extract validation result for
     * @return {@link ValidationInfoRecord}
     */
    ValidationInfoRecord getValidationInfoRecord(final CacheKey key);

    /**
     * This method returns all found keys in any cache
     *
     * @return a set of cache keys
     */
    Set<CacheKey> getAllCacheKeys();

}
