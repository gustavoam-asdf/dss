package eu.europa.esig.dss.tsl.runnable;

import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.spi.client.http.DSSFileLoader;
import eu.europa.esig.dss.spi.x509.CertificateSource;
import eu.europa.esig.dss.tsl.download.XmlDownloadTask;
import eu.europa.esig.dss.tsl.source.TLSource;
import eu.europa.esig.dss.tsl.validation.TLValidatorTask;
import eu.europa.esig.dss.validation.job.cache.access.CacheAccessByKey;
import eu.europa.esig.dss.validation.job.download.DownloadTask;
import eu.europa.esig.dss.validation.job.runnable.AbstractRunnableAnalysis;
import eu.europa.esig.dss.validation.job.source.DocumentSource;
import eu.europa.esig.dss.validation.job.validation.ValidationTask;

import java.util.concurrent.CountDownLatch;

public abstract class AbstractRunnableTLAnalysis extends AbstractRunnableAnalysis {

    /**
     * Default constructor
     *
     * @param source {@link DocumentSource} representing a TL or LOTL
     * @param cacheAccess {@link CacheAccessByKey}
     * @param dssFileLoader {@link DSSFileLoader}
     * @param latch {@link CountDownLatch}
     */
    protected AbstractRunnableTLAnalysis(final TLSource source, final CacheAccessByKey cacheAccess,
                                         final DSSFileLoader dssFileLoader, CountDownLatch latch) {
        super(source, cacheAccess, dssFileLoader, latch);
    }

    @Override
    protected DownloadTask getDownloadTask(DSSFileLoader dssFileLoader, String url) {
        return new XmlDownloadTask(dssFileLoader, url);
    }

    @Override
    protected ValidationTask getValidationTask(DSSDocument document, CertificateSource certificateSource) {
        return new TLValidatorTask(document, certificateSource);
    }

}
