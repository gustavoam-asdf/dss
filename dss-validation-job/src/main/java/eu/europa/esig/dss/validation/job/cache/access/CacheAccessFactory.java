package eu.europa.esig.dss.validation.job.cache.access;

import eu.europa.esig.dss.validation.job.cache.CacheKey;

/**
 * Factory used to create objects to interact with the cache
 *
 */
public interface CacheAccessFactory {

    /**
     * Loads a class to deal with a cache by the {@code key} records
     *
     * @param key {@link CacheKey} to use
     * @return {@link CacheAccessByKey}
     */
    CacheAccessByKey getCacheAccess(CacheKey key);

    /**
     * Loads a class for document updates
     *
     * @return {@link ChangesCacheAccess}
     */
    ChangesCacheAccess getDocumentChangesCacheAccess();

    /**
     * Loads a read-only cache access
     *
     * @return {@link ReadOnlyCacheAccess}
     */
    ReadOnlyCacheAccess getReadOnlyCacheAccess();

    /**
     * Loads a cache access to synchronize records
     *
     * @return {@link SynchronizerCacheAccess}
     */
    SynchronizerCacheAccess getSynchronizerCacheAccess();

    /**
     * Loads a cache access to load the information about the current cache state
     *
     * @return {@link DebugCacheAccess}
     */
    DebugCacheAccess getDebugCacheAccess();

}
