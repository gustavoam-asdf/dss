/**
 * DSS - Digital Signature Services
 * Copyright (C) 2015 European Commission, provided under the CEF programme
 * <p>
 * This file is part of the "DSS - Digital Signature Services" project.
 * <p>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package eu.europa.esig.dss.validation.job;

import eu.europa.esig.dss.alert.Alert;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.model.job.DocumentInfo;
import eu.europa.esig.dss.model.job.DocumentListInfo;
import eu.europa.esig.dss.model.job.ValidationJobSummary;
import eu.europa.esig.dss.spi.client.http.DSSFileLoader;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.validation.job.alerts.ValidationJobAlerter;
import eu.europa.esig.dss.validation.job.cache.CacheCleaner;
import eu.europa.esig.dss.validation.job.cache.CacheKey;
import eu.europa.esig.dss.validation.job.cache.access.CacheAccessByKey;
import eu.europa.esig.dss.validation.job.cache.access.CacheAccessFactory;
import eu.europa.esig.dss.validation.job.cache.access.ReadOnlyCacheAccess;
import eu.europa.esig.dss.validation.job.cache.state.CachedEntry;
import eu.europa.esig.dss.validation.job.parsing.ParsingResult;
import eu.europa.esig.dss.validation.job.source.DocumentSource;
import eu.europa.esig.dss.validation.job.summary.ValidationJobSummaryBuilder;
import eu.europa.esig.dss.validation.job.sync.AcceptAllStrategy;
import eu.europa.esig.dss.validation.job.sync.SynchronizationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * The main class performing the document download / parsing / validation tasks
 *
 */
public abstract class ValidationJob<D extends DocumentInfo<L>, L extends DocumentListInfo<L, D>> {

	private static final Logger LOG = LoggerFactory.getLogger(ValidationJob.class);

	/**
	 * Contains all caches for the current validation job
	 */
	private final CacheAccessFactory cacheAccessFactory = new CacheAccessFactory();

	/**
	 * Provides methods to manage the asynchronous behaviour
	 */
	private ExecutorService executorService = Executors.newCachedThreadPool();

	/**
	 * Array of zero, one or more Trusted List (TL) sources.
	 * <p>
	 * These trusted lists are not referenced in a List Of Trusted Lists (LOTL)
	 */
	private DocumentSource[] documentSources;

	/**
	 * Array of zero, one or more List Of Trusted List (LOTL) sources.
	 */
	private DocumentSource[] documentListSources;
	
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
	 * The strategy to follow to synchronize the certificates.
	 * <p>
	 * Default : all trusted lists and LOTLs are synchronized
	 */
	private SynchronizationStrategy synchronizationStrategy = new AcceptAllStrategy();

	/**
	 * This property allows printing the cache content before and after the
	 * synchronization (default : false)
	 */
	private boolean debug = false;
	
	/**
     * List of LOTL info alerts
     */
    private List<Alert<L>> documentListAlerts;
	
	/**
     * List of TL info alerts
     */
    private List<Alert<D>> documentAlerts;

	/**
	 * Default constructor instantiating object with null configuration
	 */
	protected ValidationJob() {
		// empty
	}

	/**
	 * Gets the cache access factory
	 *
	 * @return {@link CacheAccessFactory}
	 */
	protected CacheAccessFactory getCacheAccessFactory() {
		return cacheAccessFactory;
	}

	/**
	 * Gets independent document sources
	 *
	 * @return {@link  DocumentSource}s
	 */
	protected DocumentSource[] getDocumentSources() {
		return documentSources;
	}

	/**
	 * Sets the additional TL Sources
	 *
	 * @param documentSources {@link DocumentSource}s
	 */
	public void setDocumentSources(DocumentSource... documentSources) {
		this.documentSources = documentSources;
	}

	/**
	 * Gets document list sources
	 *
	 * @return {@link  DocumentSource}s
	 */
	protected DocumentSource[] getDocumentListSources() {
		return documentListSources;
	}

	/**
	 * Sets the LOTL Sources
	 *
	 * @param documentListSources {@link DocumentSource}s
	 */
	public void setDocumentListSources(DocumentSource... documentListSources) {
		this.documentListSources = documentListSources;
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
	 * Gets the synchronization strategy
	 *
	 * @return {@link SynchronizationStrategy}
	 */
	protected SynchronizationStrategy getSynchronizationStrategy() {
		return synchronizationStrategy;
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
	 * Sets the LOTL alerts to be processed
	 * 
	 * @param documentListAlerts a list of {@link Alert}s
	 */
	public void setDocumentListAlerts(List<Alert<L>> documentListAlerts) {
	    this.documentListAlerts = documentListAlerts;
	}
	
	/**
	 * Sets the TL alerts to be processed
	 * 
	 * @param documentAlerts a list of {@link Alert}s
	 */
	public void setDocumentAlerts(List<Alert<D>> documentAlerts) {
	    this.documentAlerts = documentAlerts;
	}

	/**
	 * Returns validation job summary for all processed LOTL / TLs
	 * @return {@link ValidationJobSummary}
	 */
	public synchronized ValidationJobSummary<D, L> getSummary() {
		return getValidationJobSummaryBuilder().build();
	}

	protected abstract ValidationJobSummaryBuilder<D, L> getValidationJobSummaryBuilder();

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

		List<DocumentSource> currentTLSources = new ArrayList<>();
		if (documentSources != null) {
			currentTLSources.addAll(Arrays.asList(documentSources));
		}

		// Execute all LOTLs
		if (Utils.isArrayNotEmpty(documentListSources)) {
			final List<DocumentSource> lotlList = Arrays.asList(documentListSources);

			executeLOTLSourcesAnalysis(lotlList, dssFileLoader);

			// Check LOTLs consistency

			// extract TLSources from cached LOTLs
			currentTLSources.addAll(extractOtherDocumentSources());
		}

		// And then, execute all TLs (manual configs + TLs from LOTLs)
		executeTLSourcesAnalysis(currentTLSources, dssFileLoader);

		// alerts()
		if (Utils.isCollectionNotEmpty(documentListAlerts) || Utils.isCollectionNotEmpty(documentAlerts)) {
			ValidationJobSummary<D, L> jobSummary = getSummary();
			ValidationJobAlerter<D, L> alerter = new ValidationJobAlerter<>(documentListAlerts, documentAlerts);
			alerter.detectChanges(jobSummary);
		}

		if (debug) {
			LOG.info("Dump before synchronization");
			cacheAccessFactory.getDebugCacheAccess().dump();
		}

		// TLCerSource sync + cache sync if needed
		synchronizeCertificateSources();

		executeCacheCleaner();

		if (debug) {
			LOG.info("Dump after synchronization");
			cacheAccessFactory.getDebugCacheAccess().dump();
		}
	}

	private void executeLOTLSourcesAnalysis(List<DocumentSource> lotlSources, DSSFileLoader dssFileLoader) {
		checkNoDuplicateUrls(lotlSources);

		int nbLOTLSources = lotlSources.size();

		LOG.info("Running analysis for {} LOTLSource(s)", nbLOTLSources);

		Map<CacheKey, CachedEntry<ParsingResult>> oldParsingValues = extractParsingCache(lotlSources);

		CountDownLatch latch = new CountDownLatch(nbLOTLSources);
		for (DocumentSource lotlSource : lotlSources) {
			executorService.submit(getDocumentListAnalysis(lotlSource, dssFileLoader, latch));
		}

		try {
			latch.await();
			LOG.info("Analysis is DONE for {} LOTLSource(s)", nbLOTLSources);
		} catch (InterruptedException e) {
			LOG.error("Interruption in the LOTLSource process", e);
			Thread.currentThread().interrupt();
		}

		Map<CacheKey, CachedEntry<ParsingResult>> newParsingValues = extractParsingCache(lotlSources);

		// Analyze introduced changes for TLs + adapt cache for TLs (EXPIRED)
		final DocumentChangeApplier lotlChangeApplier = new DocumentChangeApplier(cacheAccessFactory.getTLChangesCacheAccess(), oldParsingValues, newParsingValues);
		lotlChangeApplier.analyzeAndApply();
	}

	protected abstract Runnable getDocumentAnalysis(DocumentSource documentSource, DSSFileLoader dssFileLoader, CountDownLatch latch);

	protected abstract Runnable getDocumentListAnalysis(DocumentSource documentSource, DSSFileLoader dssFileLoader, CountDownLatch latch);
	
	protected abstract List<? extends DocumentSource> extractOtherDocumentSources();
	
    private Map<CacheKey, CachedEntry<ParsingResult>> extractParsingCache(List<DocumentSource> lotlSources) {
        final ReadOnlyCacheAccess readOnlyCacheAccess = cacheAccessFactory.getReadOnlyCacheAccess();
        return lotlSources.stream().collect(Collectors.toMap(DocumentSource::getCacheKey,
				s -> readOnlyCacheAccess.getParsingCacheEntry(s.getCacheKey())));
    }

	private void executeTLSourcesAnalysis(List<DocumentSource> tlSources, DSSFileLoader dssFileLoader) {
		int nbTLSources = tlSources.size();
		if (nbTLSources == 0) {
			LOG.info("No TL to be analyzed");
			return;
		}

		checkNoDuplicateUrls(tlSources);

		LOG.info("Running analysis for {} TLSource(s)", nbTLSources);

		CountDownLatch latch = new CountDownLatch(nbTLSources);
		for (DocumentSource tlSource : tlSources) {
			executorService.submit(getDocumentAnalysis(tlSource, dssFileLoader, latch));
		}

		try {
			latch.await();
			LOG.info("Analysis is DONE for {} TLSource(s)", nbTLSources);
		} catch (InterruptedException e) {
			LOG.error("Interruption in the TLAnalysis process", e);
			Thread.currentThread().interrupt();
		}
	}

	protected abstract void synchronizeCertificateSources();

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
	 *                a list of TLSource
	 */
	private void checkNoDuplicateUrls(List<DocumentSource> sources) {
		List<String> allUrls = sources.stream().map(DocumentSource::getUrl).collect(Collectors.toList());
		Set<String> uniqueUrls = new HashSet<>(allUrls);
		if (allUrls.size() > uniqueUrls.size()) {
			throw new DSSException(String.format("Duplicate urls found : %s", allUrls));
		}
	}

}
