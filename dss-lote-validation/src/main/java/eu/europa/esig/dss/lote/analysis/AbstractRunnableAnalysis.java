package eu.europa.esig.dss.lote.analysis;

import eu.europa.esig.dss.lote.cache.access.CacheAccessByKey;
import eu.europa.esig.dss.lote.download.DownloadResult;
import eu.europa.esig.dss.lote.parsing.ParsingResult;
import eu.europa.esig.dss.lote.source.ListSource;
import eu.europa.esig.dss.lote.validation.ValidationResult;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.spi.client.http.DSSFileLoader;
import eu.europa.esig.dss.spi.x509.CertificateSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.function.Supplier;

public abstract class AbstractRunnableAnalysis implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractRunnableAnalysis.class);

    private static final String LOG_ERROR_PERFORM_ANALYSIS = "Error performing analysis.";

    /** The List source */
    private final ListSource source;

    /** The cache access of the record */
    private final CacheAccessByKey cacheAccess;

    /** The file loader */
    private final DSSFileLoader dssFileLoader;

    /** The tasks counter */
    private final CountDownLatch latch;

    /**
     * Default constructor
     *
     * @param source {@link ListSource} representing a List to be processed
     * @param cacheAccess {@link CacheAccessByKey}
     * @param dssFileLoader {@link DSSFileLoader}
     */
    protected AbstractRunnableAnalysis(final ListSource source, final CacheAccessByKey cacheAccess,
                                       final DSSFileLoader dssFileLoader, final CountDownLatch latch) {
        Objects.requireNonNull(source, "Source cannot be null");
        Objects.requireNonNull(cacheAccess, "CacheAccessByKey cannot be null");
        Objects.requireNonNull(dssFileLoader, "DSSFileLoader cannot be null");
        Objects.requireNonNull(latch, "CountDownLatch cannot be null");

        this.source = source;
        this.cacheAccess = cacheAccess;
        this.dssFileLoader = dssFileLoader;
        this.latch = latch;
    }

    /**
     * Returns the current {@code ListSource}
     *
     * @return {@link ListSource}
     */
    protected final ListSource getSource() {
        return source;
    }

    /**
     * Gets the {@code CacheAccessByKey}
     *
     * @return {@link CacheAccessByKey}
     */
    protected final CacheAccessByKey getCacheAccessByKey() {
        return cacheAccess;
    }

    /**
     * Performs analysis
     */
    protected void doAnalyze() {
        String url = getSource().getUrl();
        LOG.debug("Downloading url '{}'...", url);
        DSSDocument document = dssFileLoader.getDocument(url);
        ListAnalysisExecutor analysisExecutor = getAnalysisExecutor(document);
        document = download(analysisExecutor, document, url);
        if (document != null) {
            parsing(analysisExecutor, document);
            validation(analysisExecutor, document);
        }
    }

    protected abstract ListAnalysisExecutor getAnalysisExecutor(DSSDocument document);

    /**
     * Downloads the document by url
     *
     * @param analysisExecutor {@link ListAnalysisExecutor}
     * @param url {@link String}
     * @return {@link DSSDocument}
     */
    protected DSSDocument download(final ListAnalysisExecutor analysisExecutor, final DSSDocument document, final String url) {
        try {
            Supplier<DownloadResult> downloadTask = analysisExecutor.getDownloadTask(document, url);
            DownloadResult downloadResult = downloadTask.get();
            if (!cacheAccess.isUpToDate(downloadResult)) {
                cacheAccess.update(downloadResult);
                expireCache();
            }
            return downloadResult.getDSSDocument();

        } catch (Exception e) {
            // wrapped exception
            LOG.warn(e.getMessage());
            cacheAccess.downloadError(e);
        }
        return document;
    }

    /**
     * This method expires the cache in order to trigger the corresponding tasks on refresh
     */
    protected void expireCache() {
        cacheAccess.expireParsing();
        cacheAccess.expireValidation();
    }

    /**
     * Parses the document
     *
     * @param analysisExecutor {@link ListAnalysisExecutor}
     * @param document {@link DSSDocument} to parse
     */
    protected void parsing(final ListAnalysisExecutor analysisExecutor, DSSDocument document) {
        // True if EMPTY / REFRESH_REQUIRED
        if (cacheAccess.isParsingRefreshNeeded()) {
            try {
                LOG.debug("Parsing the TL/LOTL with cache key '{}'...", cacheAccess.getCacheKey().getKey());
                Supplier<ParsingResult> parsingTask = analysisExecutor.getParsingTask(document, getSource());
                cacheAccess.update(parsingTask.get());
            } catch (Exception e) {
                LOG.warn("Cannot parse the TL/LOTL with the cache key '{}' : {}", cacheAccess.getCacheKey().getKey(), e.getMessage(), e);
                cacheAccess.parsingError(e);
            }
        }
    }

    /**
     * Validates the document
     *
     * @param analysisExecutor {@link ListAnalysisExecutor}
     * @param document {@link DSSDocument} to validate
     */
    protected void validation(final ListAnalysisExecutor analysisExecutor, DSSDocument document) {
        // True if EMPTY / REFRESH_REQUIRED
        if (cacheAccess.isValidationRefreshNeeded()) {
            try {
                LOG.debug("Validating the TL/LOTL with cache key '{}'...", cacheAccess.getCacheKey().getKey());
                Supplier<ValidationResult> validationTask = analysisExecutor.getValidationTask(document, getCurrentCertificateSource());
                cacheAccess.update(validationTask.get());
            } catch (Exception e) {
                LOG.warn("Cannot validate the TL/LOTL with the cache key '{}' : {}", cacheAccess.getCacheKey().getKey(), e.getMessage());
                cacheAccess.validationError(e);
            }
        }
    }

    /**
     * Returns the certificate source to be used to validate TL/LOTL
     *
     * @return {@link CertificateSource}
     */
    protected CertificateSource getCurrentCertificateSource() {
        return getSource().getCertificateSource();
    }

    @Override
    public void run() {
        try {
            this.doAnalyze();
        } catch (final Throwable exception) {
            // NOTE: Throwable shall be caught
            LOG.warn(LOG_ERROR_PERFORM_ANALYSIS, exception);
        } finally {
            latch.countDown();
        }
    }

}
