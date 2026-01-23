package eu.europa.esig.dss.lote.summary;

import eu.europa.esig.dss.lote.cache.CacheKey;
import eu.europa.esig.dss.lote.cache.access.ReadOnlyCacheAccess;
import eu.europa.esig.dss.lote.source.ListSource;
import eu.europa.esig.dss.model.lote.LoTEInfo;
import eu.europa.esig.dss.model.lote.LoTEValidationJobSummary;
import eu.europa.esig.dss.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Builds a {@code eu.europa.esig.dss.model.lote.LoTEValidationJobSummary}
 */

public class LoTEValidationJobSummaryBuilder {

    /**
     * A read-only access for the cache of the current Validation Job
     */
    private final ReadOnlyCacheAccess readOnlyCacheAccess;

    /**
     * List of sources to extract summary for
     */
    private final ListSource[] listSources;

    /**
     * Default constructor
     *
     * @param readOnlyCacheAccess {@link ReadOnlyCacheAccess}
     * @param listSources {@link ListSource}s
     */
    public LoTEValidationJobSummaryBuilder(final ReadOnlyCacheAccess readOnlyCacheAccess, final ListSource[] listSources) {
        this.readOnlyCacheAccess = readOnlyCacheAccess;
        this.listSources = listSources;
    }

    /**
     * Builds the {@code LoTEValidationJobSummary}
     *
     * @return {@link LoTEValidationJobSummary}
     */
    public LoTEValidationJobSummary build() {

        final List<LoTEInfo> otherListInfos = new ArrayList<>();
        if (Utils.isArrayNotEmpty(listSources)) {
            for (ListSource tlSource : listSources) {
                otherListInfos.add(buildListInfo(tlSource));
            }
        }

        // TODO : add support of LoLoTE
        return new LoTEValidationJobSummary(Collections.emptyList(), otherListInfos);
    }

    private LoTEInfo buildListInfo(ListSource listSource) {
        CacheKey cacheKey = listSource.getCacheKey();
        return new LoTEInfo(readOnlyCacheAccess.getDownloadCacheDTO(cacheKey), readOnlyCacheAccess.getParsingCacheDTO(cacheKey),
                readOnlyCacheAccess.getValidationCacheDTO(cacheKey), listSource.getUrl());
    }

}
