package eu.europa.esig.dss.validation.job.cache.access;

import eu.europa.esig.dss.model.job.DownloadInfoRecord;
import eu.europa.esig.dss.model.job.ParsingInfoRecord;
import eu.europa.esig.dss.model.job.ValidationInfoRecord;

/**
 * Provides a read only interface for cache by key
 *
 */
public interface ReadOnlyCacheAccessByKey {

    /**
     * Returns the cached read-only download result DTO
     *
     * @return {@link DownloadInfoRecord}
     */
    DownloadInfoRecord getDownloadReadOnlyResult();

    /**
     * Returns the cached read-only parsing result DTO
     *
     * @return {@link ParsingInfoRecord}
     */
    ParsingInfoRecord getParsingReadOnlyResult();

    /**
     * Returns the cached read-only validation result DTO
     *
     * @return {@link ValidationInfoRecord}
     */
    ValidationInfoRecord getValidationReadOnlyResult();

}
