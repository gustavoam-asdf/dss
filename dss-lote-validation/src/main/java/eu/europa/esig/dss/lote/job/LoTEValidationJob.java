package eu.europa.esig.dss.lote.job;

import eu.europa.esig.dss.alert.Alert;
import eu.europa.esig.dss.lote.alerts.LoTEValidationJobAlerter;
import eu.europa.esig.dss.lote.analysis.ListAnalysis;
import eu.europa.esig.dss.lote.cache.CacheCleaner;
import eu.europa.esig.dss.lote.cache.CacheKey;
import eu.europa.esig.dss.lote.cache.access.CacheAccessByKey;
import eu.europa.esig.dss.lote.cache.access.CacheAccessFactory;
import eu.europa.esig.dss.lote.source.ListSource;
import eu.europa.esig.dss.lote.summary.LoTEValidationJobSummaryBuilder;
import eu.europa.esig.dss.lote.sync.AcceptAllStrategy;
import eu.europa.esig.dss.lote.sync.LoTECertificateSourceSynchronizer;
import eu.europa.esig.dss.lote.sync.SynchronizationStrategy;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.model.lote.ListInfo;
import eu.europa.esig.dss.model.lote.ListOfListsInfo;
import eu.europa.esig.dss.model.lote.LoTEValidationJobSummary;
import eu.europa.esig.dss.spi.client.http.DSSFileLoader;
import eu.europa.esig.dss.spi.lote.TrustedEntitiesCertificateSource;
import eu.europa.esig.dss.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * The main user-interface for validation of Lists of trusted entities,
 * including business logic for download, parsing, validation tasks
 *
 */
public class LoTEValidationJob {

    private static final Logger LOG = LoggerFactory.getLogger(LoTEValidationJob.class);

    /**
     * Contains all caches for the current validation job
     */
    private final CacheAccessFactory cacheAccessFactory = new CacheAccessFactory();

    /**
     * Provides methods to manage the asynchronous behaviour
     */
    private ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * Array of zero, one or more Lists sources.
     * <p>
     * These trusted lists are not referenced in a List Of Trusted Lists (LOTL)
     */
    private ListSource[] listSources;

    /**
     * The DSSFileLoader used for offline data loading from a local source
     */
    private DSSFileLoader offlineLoader;

    /**
     * The DSSFileLoader used for online data loading from a remote source
     */
    private DSSFileLoader onlineLoader;

    /**
     * Used to clean the cache
     */
    private CacheCleaner cacheCleaner;

    /**
     * The certificate source to be synchronized
     */
    private TrustedEntitiesCertificateSource trustPropertiesCertificateSource;

    /**
     * The strategy to follow to synchronize the certificates.
     * <p>
     * Default : all lists and lists of lists are synchronized
     */
    private SynchronizationStrategy synchronizationStrategy = new AcceptAllStrategy();

    /**
     * This property allows to print the cache content before and after the
     * synchronization (default : false)
     */
    private boolean debug = false;

    /**
     * List of lists info alerts
     */
    private List<Alert<ListOfListsInfo>> listOfListsAlerts;

    /**
     * List of info alerts
     */
    private List<Alert<ListInfo>> listAlerts;

    /**
     * Default constructor instantiating object with null configuration
     */
    public LoTEValidationJob() {
        // empty
    }

    /**
     * Sets the additional List Sources
     *
     * @param listSources {@link ListSource}s
     */
    public void setListSources(ListSource... listSources) {
        this.listSources = listSources;
    }

    /**
     * Sets the execution service to manage the asynchronous behaviour
     *
     * @param executorService {@link ExecutorService}
     */
    public void setExecutorService(ExecutorService executorService) {
        if (this.executorService != null && !this.executorService.isShutdown()) {
            this.executorService.shutdownNow();
        }
        this.executorService = executorService;
    }

    /**
     * Sets the offline DSSFileLoader used for data loading from the local source
     * @param offlineLoader {@link DSSFileLoader}
     */
    public void setOfflineDataLoader(DSSFileLoader offlineLoader) {
        this.offlineLoader = offlineLoader;
    }

    /**
     * Sets the online DSSFileLoader used for data loading from a remote source
     * @param onlineLoader {@link DSSFileLoader}
     */
    public void setOnlineDataLoader(DSSFileLoader onlineLoader) {
        this.onlineLoader = onlineLoader;
    }

    /**
     * Sets the cacheCleaner
     * @param cacheCleaner {@link CacheCleaner}
     */
    public void setCacheCleaner(final CacheCleaner cacheCleaner) {
        this.cacheCleaner = cacheCleaner;
    }

    /**
     * Sets the TrustedListsCertificateSource to be filled with the job
     *
     * @param trustPropertiesCertificateSource
     *                                     the TrustedListsCertificateSource to fill
     *                                     with the job results
     */
    public void setTrustedListCertificateSource(TrustedEntitiesCertificateSource trustPropertiesCertificateSource) {
        this.trustPropertiesCertificateSource = trustPropertiesCertificateSource;
    }

    /**
     * Sets the strategy to follow for the certificate synchronization
     *
     * @param synchronizationStrategy
     *                                the different options for the certificate
     *                                synchronization
     */
    public void setSynchronizationStrategy(SynchronizationStrategy synchronizationStrategy) {
        Objects.requireNonNull(synchronizationStrategy, "The SynchronizationStrategy cannot be null");
        this.synchronizationStrategy = synchronizationStrategy;
    }

    /**
     * Sets the debug mode (print the cache contents before and after the
     * synchronization)
     *
     * @param debug
     *              TRUE to enable the debug mode (default = false)
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    /**
     * Sets the List of Lists alerts to be processed
     *
     * @param listOfListsAlerts a list of {@link Alert}s
     */
    public void setListOfListsAlerts(List<Alert<ListOfListsInfo>> listOfListsAlerts) {
        this.listOfListsAlerts = listOfListsAlerts;
    }

    /**
     * Sets the List alerts to be processed
     *
     * @param tlAlerts a list of {@link Alert}s
     */
    public void setListAlerts(List<Alert<ListInfo>> tlAlerts) {
        this.listAlerts = tlAlerts;
    }

    /**
     * Returns validation job summary for all processed List of Lists or Lists
     *
     * @return {@link LoTEValidationJobSummary}
     */
    public synchronized LoTEValidationJobSummary getSummary() {
        return new LoTEValidationJobSummaryBuilder(cacheAccessFactory.getReadOnlyCacheAccess(), listSources).build();
    }

    /**
     * Used to execute the refresh in offline mode (no date from remote sources will be downloaded)
     * By default used on initialization
     */
    public synchronized void offlineRefresh() {
        Objects.requireNonNull(offlineLoader, "The offlineLoader must be defined!");
        LOG.info("Offline refresh is running...");
        refresh(offlineLoader);
        LOG.info("Offline refresh is DONE.");
    }

    /**
     * Used to execute the refresh in online mode (all data will be updated from remote sources)
     * Used as default database update.
     */
    public synchronized void onlineRefresh() {
        Objects.requireNonNull(onlineLoader, "The onlineLoader must be defined!");
        LOG.info("Online refresh is running...");
        refresh(onlineLoader);
        LOG.info("Online refresh is DONE.");
    }

    private void refresh(DSSFileLoader dssFileLoader) {

        List<ListSource> currentListSources = new ArrayList<>();
        if (listSources != null) {
            currentListSources.addAll(Arrays.asList(listSources));
        }

        // TODO : execute List of Lists

        // And then, execute all TLs (manual configs + TLs from LOTLs)
        executeListSourcesAnalysis(currentListSources, dssFileLoader);

        // alerts()
        if (Utils.isCollectionNotEmpty(listOfListsAlerts) || Utils.isCollectionNotEmpty(listAlerts)) {
            LoTEValidationJobSummary jobSummary = getSummary();
            LoTEValidationJobAlerter alerter = new LoTEValidationJobAlerter(listAlerts);
            alerter.detectChanges(jobSummary);
        }

        if (debug) {
            LOG.info("Dump before synchronization");
            cacheAccessFactory.getDebugCacheAccess().dump();
        }

        // TLCerSource sync + cache sync if needed
        synchronizeTLCertificateSource();

        executeCacheCleaner();

        if (debug) {
            LOG.info("Dump after synchronization");
            cacheAccessFactory.getDebugCacheAccess().dump();
        }
    }

    private void executeListSourcesAnalysis(List<ListSource> listSources, DSSFileLoader dssFileLoader) {
        int nbTLSources = listSources.size();
        if (nbTLSources == 0) {
            LOG.info("No TL to be analyzed");
            return;
        }

        checkNoDuplicateUrls(listSources);

        LOG.info("Running analysis for {} TLSource(s)", nbTLSources);

        CountDownLatch latch = new CountDownLatch(nbTLSources);
        for (ListSource listSource : listSources) {
            final CacheAccessByKey cacheAccess = cacheAccessFactory.getCacheAccess(listSource.getCacheKey());
            executorService.submit(new ListAnalysis(listSource, cacheAccess, dssFileLoader, latch));
        }

        try {
            latch.await();
            LOG.info("Analysis is DONE for {} TLSource(s)", nbTLSources);
        } catch (InterruptedException e) {
            LOG.error("Interruption in the TLAnalysis process", e);
            Thread.currentThread().interrupt();
        }
    }

    private void synchronizeTLCertificateSource() {
        if (trustPropertiesCertificateSource == null) {
            LOG.warn("No TrustedListCertificateSource to be synchronized");
            return;
        }

        LoTECertificateSourceSynchronizer synchronizer = new LoTECertificateSourceSynchronizer(
                listSources, trustPropertiesCertificateSource, synchronizationStrategy,
                cacheAccessFactory.getSynchronizerCacheAccess());
        synchronizer.sync();
    }

    private void executeCacheCleaner() {
        if (cacheCleaner == null) {
            LOG.debug("Cache cleaner is not defined");
            return;
        }

        LOG.info("Running CacheCleaner");
        Set<CacheKey> cacheKeys = cacheAccessFactory.getReadOnlyCacheAccess().getAllCacheKeys();
        for (CacheKey cacheKey : cacheKeys) {
            final CacheAccessByKey cacheAccess = cacheAccessFactory.getCacheAccess(cacheKey);
            cacheCleaner.clean(cacheAccess);
        }
        LOG.info("CacheCleaner process is DONE");
    }

    /**
     * Duplicate urls mean cache conflict.
     *
     * @param sources
     *                a list of ListSource
     */
    private void checkNoDuplicateUrls(List<? extends ListSource> sources) {
        List<String> allUrls = sources.stream().map(ListSource::getUrl).collect(Collectors.toList());
        Set<String> uniqueUrls = new HashSet<>(allUrls);
        if (allUrls.size() > uniqueUrls.size()) {
            throw new DSSException(String.format("Duplicate urls found : %s", allUrls));
        }
    }

}
