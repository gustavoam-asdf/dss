package eu.europa.esig.dss.validation.job.cache.access;

import eu.europa.esig.dss.model.job.DownloadInfoRecord;
import eu.europa.esig.dss.model.job.ParsingInfoRecord;
import eu.europa.esig.dss.model.job.ValidationInfoRecord;
import eu.europa.esig.dss.validation.job.cache.CacheKey;

/**
 * Parametrized interface to access cache in read-only mode
 *
 * @param <D> {@link DownloadInfoRecord}
 * @param <P> {@link ParsingInfoRecord}
 * @param <V> {@link ValidationInfoRecord}
 */
public interface ParametrizedReadOnlyCacheAccess<D extends DownloadInfoRecord, P extends ParsingInfoRecord, V extends ValidationInfoRecord> extends ReadOnlyCacheAccess {

    /**
     * Returns download cache DTO result
     *
     * @param key {@link CacheKey} to extract download result for
     * @return {@link DownloadInfoRecord}
     */
    D getDownloadInfoRecord(final CacheKey key);

    /**
     * Returns download cache DTO result
     *
     * @param key {@link CacheKey} to extract parsing result for
     * @return {@link ParsingInfoRecord}
     */
    P getParsingInfoRecord(final CacheKey key);

    /**
     * Returns download cache DTO result
     *
     * @param key {@link CacheKey} to extract validation result for
     * @return {@link ValidationInfoRecord}
     */
    V getValidationInfoRecord(final CacheKey key);

}
