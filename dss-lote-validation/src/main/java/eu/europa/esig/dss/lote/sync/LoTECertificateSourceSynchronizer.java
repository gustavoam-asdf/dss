package eu.europa.esig.dss.lote.sync;

import eu.europa.esig.dss.lote.cache.CacheKey;
import eu.europa.esig.dss.lote.cache.access.SynchronizerCacheAccess;
import eu.europa.esig.dss.lote.source.ListSource;
import eu.europa.esig.dss.lote.summary.LoTEValidationJobSummaryBuilder;
import eu.europa.esig.dss.model.lote.ListInfo;
import eu.europa.esig.dss.model.lote.LoLoTEInfo;
import eu.europa.esig.dss.model.lote.LoTEInfo;
import eu.europa.esig.dss.model.lote.LoTEValidationJobSummary;
import eu.europa.esig.dss.model.lote.ServiceStatusAndInformationExtensions;
import eu.europa.esig.dss.model.lote.TrustedEntity;
import eu.europa.esig.dss.model.lote.TrustedEntityService;
import eu.europa.esig.dss.model.lote.TrustedProperties;
import eu.europa.esig.dss.model.lote.record.ParsingInfoRecord;
import eu.europa.esig.dss.model.timedependent.TimeDependentValues;
import eu.europa.esig.dss.model.tsl.CertificateTrustTime;
import eu.europa.esig.dss.model.tsl.PivotInfo;
import eu.europa.esig.dss.model.x509.CertificateToken;
import eu.europa.esig.dss.spi.lote.TrustedEntitiesCertificateSource;
import eu.europa.esig.dss.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Predicate;

public class LoTECertificateSourceSynchronizer {

    private static final Logger LOG = LoggerFactory.getLogger(LoTECertificateSourceSynchronizer.class);

    /**
     * List of List Sources to extract summary for
     */
    private final ListSource[] listSources;

    /**
     * The strategy to follow for the certificate synchronization
     */
    private final SynchronizationStrategy synchronizationStrategy;

    /**
     * The certificate source to be synchronized
     */
    private final TrustedEntitiesCertificateSource certificateSource;

    /**
     * The cache access
     */
    private final SynchronizerCacheAccess cacheAccess;

    /**
     * Default constructor
     *
     * @param listSources {@link ListSource}s
     * @param certificateSource {@link TrustedEntitiesCertificateSource}
     * @param synchronizationStrategy {@link SynchronizationStrategy}
     * @param cacheAccess {@link SynchronizerCacheAccess}
     */
    public LoTECertificateSourceSynchronizer(ListSource[] listSources, TrustedEntitiesCertificateSource certificateSource, 
                                                    SynchronizationStrategy synchronizationStrategy, SynchronizerCacheAccess cacheAccess) {
        this.listSources = listSources;
        this.synchronizationStrategy = synchronizationStrategy;
        this.certificateSource = certificateSource;
        this.cacheAccess = cacheAccess;
    }

    /**
     * Synchronizes the trusted certificate source based on the validation job processing result
     */
    public void sync() {
        try {
            LoTEValidationJobSummaryBuilder summaryBuilder = new LoTEValidationJobSummaryBuilder(cacheAccess, listSources);

            LoTEValidationJobSummary summary = summaryBuilder.build();
            if (isCertificateSyncNeeded(summary)) {
                synchronizeCertificates(summary);
            }
            syncCache(summary);

            // re-build summary after synchronization
            summary = summaryBuilder.build();
            certificateSource.setSummary(summary);

        } catch (Exception e) {
            LOG.error("Unable to synchronize the TrustedListsCertificateSource", e);
        }
    }

    private boolean isCertificateSyncNeeded(LoTEValidationJobSummary summary) {
        return isTLParsingDesyncOrError(summary.getOtherListInfos());
    }

    private boolean isTLParsingDesyncOrError(List<LoTEInfo> loteInfos) {
        return loteInfos.stream().anyMatch(this::isTLParsingDesyncOrError);
    }

    private boolean isTLParsingDesyncOrError(LoTEInfo loteInfo) {
        ParsingInfoRecord parsingCacheInfo = loteInfo.getParsingCacheInfo();
        return parsingCacheInfo == null || parsingCacheInfo.isDesynchronized() || parsingCacheInfo.isError();
    }

    private void synchronizeCertificates(LoTEValidationJobSummary summary) {
        final Map<CertificateToken, List<TrustedProperties>> trustPropertiesByCerts = new WeakHashMap<>();
        final Map<CertificateToken, List<CertificateTrustTime>> trustTimeByCerts = new WeakHashMap<>();
        for (LoLoTEInfo loloteInfo : summary.getListOfListsInfos()) {
            if (synchronizationStrategy.canBeSynchronized(loloteInfo)) {
                addCertificatesFromTLs(trustPropertiesByCerts, trustTimeByCerts, loloteInfo.getListsInfos(), loloteInfo);
            } else {
                LOG.warn("Certificate synchronization is skipped for LOTL '{}' and its TLs", loloteInfo.getUrl());
            }
        }
        addCertificatesFromTLs(trustPropertiesByCerts, trustTimeByCerts, summary.getOtherListInfos(), null);
        certificateSource.setTrustedPropertiesByCertificates(trustPropertiesByCerts);
        certificateSource.setTrustedTimeByCertificates(trustTimeByCerts);
    }

    private void addCertificatesFromTLs(final Map<CertificateToken, List<TrustedProperties>> trustPropertiesByCerts,
                                        final Map<CertificateToken, List<CertificateTrustTime>> trustTimeByCerts,
                                        final List<LoTEInfo> loteInfos, final LoLoTEInfo relatedLoLoTE) {

        for (final LoTEInfo loteInfo : loteInfos) {
            if (synchronizationStrategy.canBeSynchronized(loteInfo)) {
                ParsingInfoRecord parsingCacheInfo = loteInfo.getParsingCacheInfo();
                if (parsingCacheInfo == null || !parsingCacheInfo.isResultExist()) {
                    LOG.warn("No Parsing result for TLInfo with url [{}]", loteInfo.getUrl());
                } else {
                    final List<TrustedEntity> trustedEntities = parsingCacheInfo.getTrustedEntities();
                    if (Utils.isCollectionNotEmpty(trustedEntities)) {
                        final Predicate<ServiceStatusAndInformationExtensions> trustAnchorValidityPredicate =
                                getTrustAnchorValidityPredicate(loteInfo, relatedLoLoTE);
                        for (TrustedEntity original : trustedEntities) {
                            TrustedEntity detached = getDetached(original);
                            for (Object service : original.getServices()) {
                                TrustedEntityService trustedEntityService = (TrustedEntityService) service;
                                TimeDependentValues<ServiceStatusAndInformationExtensions> statusAndInformationExtensions =
                                        trustedEntityService.getStatusAndInformationExtensions();
                                TrustedProperties trustProperties = getTrustedProperties(
                                        relatedLoLoTE, loteInfo, detached, statusAndInformationExtensions);
                                List<CertificateTrustTime> certificateTrustTimes = getCertificateTrustTimes(statusAndInformationExtensions, trustAnchorValidityPredicate);
                                for (CertificateToken certificate : trustedEntityService.getCertificates()) {
                                    addCertificate(trustPropertiesByCerts, trustTimeByCerts, certificate, trustProperties, certificateTrustTimes);
                                }
                            }
                        }
                    }
                }
            } else {
                LOG.warn("Certificate synchronization is skipped for TL '{}'", loteInfo.getUrl());
            }
        }
    }

    private void addCertificate(final Map<CertificateToken, List<TrustedProperties>> trustedPropertiesByCerts,
                                final Map<CertificateToken, List<CertificateTrustTime>> trustTimeByCerts,
                                CertificateToken certificate, TrustedProperties trustProperties, List<CertificateTrustTime> certificateTrustTimes) {
        List<TrustedProperties> trustPropertiesList = trustedPropertiesByCerts.computeIfAbsent(certificate, k -> new ArrayList<>());
        if (!trustPropertiesList.contains(trustProperties)) {
            trustPropertiesList.add(trustProperties);
        }
        List<CertificateTrustTime> certificateTrustTimeList = trustTimeByCerts.computeIfAbsent(certificate, k -> new ArrayList<>());
        for (CertificateTrustTime certificateTrustTime : certificateTrustTimes) {
            if (!certificateTrustTimeList.contains(certificateTrustTime)) {
                certificateTrustTimeList.add(certificateTrustTime);
            }
        }
    }

    private TrustedEntity getDetached(TrustedEntity original) {
        TrustedEntityBuilder builder = new TrustedEntityBuilder(original);
        builder.setServices(Collections.emptyList());
        return builder.build();
    }

    private void syncCache(LoTEValidationJobSummary summary) {
        for (LoLoTEInfo loloteInfo : summary.getListOfListsInfos()) {
            syncListInfosCache(loloteInfo.getListsInfos());
            //syncPivotsCache(loloteInfo.getPivotInfos());
            cacheAccess.sync(new CacheKey(loloteInfo.getUrl()));
        }
        syncListInfosCache(summary.getOtherListInfos());
    }

    private void syncPivotsCache(List<PivotInfo> pivotInfos) {
        for (PivotInfo pivotInfo : pivotInfos) {
            cacheAccess.sync(new CacheKey(pivotInfo.getUrl()));
        }
    }

    private void syncListInfosCache(List<? extends ListInfo> tlInfos) {
        for (ListInfo tlInfo : tlInfos) {
            cacheAccess.sync(new CacheKey(tlInfo.getUrl()));
        }
    }

    private TrustedProperties getTrustedProperties(LoLoTEInfo relatedLoLoTE, LoTEInfo loteInfo, TrustedEntity detached,
                                                 TimeDependentValues<ServiceStatusAndInformationExtensions> statusAndInformationExtensions) {
        if (relatedLoLoTE == null) {
            return new TrustedProperties(loteInfo, detached, statusAndInformationExtensions);
        }
        return new TrustedProperties(relatedLoLoTE, loteInfo, detached, statusAndInformationExtensions);
    }

    private List<CertificateTrustTime> getCertificateTrustTimes(
            TimeDependentValues<ServiceStatusAndInformationExtensions> statusAndInformationExtensions,
            Predicate<ServiceStatusAndInformationExtensions> trustAnchorValidityPredicate) {
        if (trustAnchorValidityPredicate == null) {
            // return empty instance (always valid), when no predicate is defined
            return Collections.singletonList(new CertificateTrustTime(true));
        }

        final List<CertificateTrustTime> result = new ArrayList<>();
        for (ServiceStatusAndInformationExtensions trustServiceStatusAndInformation : statusAndInformationExtensions) {
            // TODO : add handling of MRA ?
            if (trustAnchorValidityPredicate.test(trustServiceStatusAndInformation)) {
                result.add(new CertificateTrustTime(trustServiceStatusAndInformation.getStartDate(), trustServiceStatusAndInformation.getEndDate()));
            } else {
                result.add(new CertificateTrustTime(false)); // not trusted
            }
        }
        return result;
    }

    private Predicate<ServiceStatusAndInformationExtensions> getTrustAnchorValidityPredicate(LoTEInfo loteInfo, LoLoTEInfo loloteInfo) {
        ListSource listSource = getRelatedListSource(loteInfo, loloteInfo);
        if (listSource != null) {
            return listSource.getTrustAnchorValidityPredicate();
        }
        return null;
    }

    private ListSource getRelatedListSource(LoTEInfo tlInfo, LoLoTEInfo relatedLOTLInfo) {
        for (ListSource listSource : listSources) {
            if (listSource.getUrl().equals(tlInfo.getUrl())) {
                return listSource;
            }
        }
        return null;
    }

}