package eu.europa.esig.dss.lote.job;

import eu.europa.esig.dss.lote.cache.CacheKey;
import eu.europa.esig.dss.lote.dto.ParsingCacheDTO;
import eu.europa.esig.dss.lote.source.ListSource;
import eu.europa.esig.dss.model.lote.OtherListPointer;
import eu.europa.esig.dss.model.x509.CertificateToken;
import eu.europa.esig.dss.spi.x509.CertificateSource;
import eu.europa.esig.dss.spi.x509.CommonCertificateSource;
import eu.europa.esig.dss.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class creates a list of {@link eu.europa.esig.dss.lote.source.ListSource}s
 *
 */
public class ListSourceBuilder {

    /** The list of original List sources */
    private final List<ListSource> originalListSources;

    /** The parsing results map */
    private final Map<CacheKey, ParsingCacheDTO> parsingResults;

    /**
     * Default constructor
     *
     * @param originalListSources a list of original {@link ListSource}s, to get other {@link ListSource}s from, if any
     * @param parsingResults a map of parsing results
     */
    public ListSourceBuilder(List<ListSource> originalListSources, Map<CacheKey, ParsingCacheDTO> parsingResults) {
        this.originalListSources = originalListSources;
        this.parsingResults = parsingResults;
    }

    /**
     * Builds a list of {@code ListSource}s
     *
     * @return a list of {@link ListSource}s
     */
    public List<ListSource> build() {
        List<ListSource> result = new ArrayList<>();
        if (originalListSources != null) {
            for (ListSource listSource : originalListSources) {
                ParsingCacheDTO cachedResult = parsingResults.get(listSource.getCacheKey());
                if (cachedResult != null && cachedResult.isResultExist()) {
                    List<OtherListPointer> otherListPointers = cachedResult.getOtherListPointers();
                    if (Utils.isCollectionNotEmpty(otherListPointers)) {
                        for (OtherListPointer otherListPointer : otherListPointers) {
                            result.add(getListSource(otherListPointer, listSource));
                        }
                    }
                }
            }
        }
        return result;
    }

    private ListSource getListSource(OtherListPointer otherListPointer, ListSource parentListSource) {
        ListSource listSource = new ListSource();
        listSource.setUrl(otherListPointer.getLocationUrl());
        listSource.setCertificateSource(getCertificateSource(otherListPointer.getSdiCertificates()));
        listSource.setTrustedEntityPredicate(parentListSource.getTrustedEntityPredicate());
        listSource.setTrustedServicePredicate(parentListSource.getTrustedServicePredicate());
        // TODO : add recursive List handling ?
        // TODO : add supported versions ?
        return listSource;
    }

    private CertificateSource getCertificateSource(List<CertificateToken> certificates) {
        CertificateSource certificateSource = new CommonCertificateSource();
        for (CertificateToken certificate : certificates) {
            certificateSource.addCertificate(certificate);
        }
        return certificateSource;
    }

}
