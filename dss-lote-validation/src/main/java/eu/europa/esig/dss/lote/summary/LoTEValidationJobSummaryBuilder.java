package eu.europa.esig.dss.lote.summary;

import eu.europa.esig.dss.lote.cache.CacheKey;
import eu.europa.esig.dss.lote.cache.access.ReadOnlyCacheAccess;
import eu.europa.esig.dss.lote.source.ListSource;
import eu.europa.esig.dss.model.lote.ListInfo;
import eu.europa.esig.dss.model.lote.LoTEInfo;
import eu.europa.esig.dss.model.lote.LoTEValidationJobSummary;
import eu.europa.esig.dss.model.lote.OtherListPointer;
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

        final List<ListInfo> listInfos = new ArrayList<>();
        if (Utils.isArrayNotEmpty(listSources)) {
            for (ListSource listSource : listSources) {
                LoTEInfo loTEInfo = buildListInfo(listSource);
                listInfos.add(loTEInfo);
                List<ListInfo> otherListInfos = buildOtherListInfos(loTEInfo);
                if (Utils.isCollectionNotEmpty(otherListInfos)) {
                    listInfos.addAll(otherListInfos);
                }
            }
        }
        return new LoTEValidationJobSummary(listInfos);
    }

    private LoTEInfo buildListInfo(ListSource listSource) {
        CacheKey cacheKey = listSource.getCacheKey();
        return new LoTEInfo(readOnlyCacheAccess.getDownloadCacheDTO(cacheKey), readOnlyCacheAccess.getParsingCacheDTO(cacheKey),
                readOnlyCacheAccess.getValidationCacheDTO(cacheKey), listSource.getUrl());
    }

    private LoTEInfo buildListInfo(ListSource listSource, ListInfo parentInfo, OtherListPointer otherListPointer) {
        CacheKey cacheKey = listSource.getCacheKey();
        return new LoTEInfo(readOnlyCacheAccess.getDownloadCacheDTO(cacheKey), readOnlyCacheAccess.getParsingCacheDTO(cacheKey),
                readOnlyCacheAccess.getValidationCacheDTO(cacheKey), listSource.getUrl(), parentInfo, otherListPointer);
    }

    private List<ListInfo> buildOtherListInfos(LoTEInfo loTEInfo) {
        if (loTEInfo.getParsingCacheInfo() == null || Utils.isCollectionEmpty(loTEInfo.getParsingCacheInfo().getOtherListPointers())) {
            return Collections.emptyList();
        }
        List<ListInfo> children = new ArrayList<>();
        for (OtherListPointer otherListPointer : loTEInfo.getParsingCacheInfo().getOtherListPointers()) {
            ListSource childListSource = new ListSource();
            childListSource.setUrl(otherListPointer.getLocationUrl());
            children.add(buildListInfo(childListSource, loTEInfo, otherListPointer));
        }
        loTEInfo.setOtherLoTEInfos(children);
        return children;
    }

}
