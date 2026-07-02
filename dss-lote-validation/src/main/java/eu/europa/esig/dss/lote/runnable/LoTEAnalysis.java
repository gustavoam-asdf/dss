package eu.europa.esig.dss.lote.runnable;

import eu.europa.esig.dss.lote.source.LoTESource;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.spi.client.http.DSSFileLoader;
import eu.europa.esig.dss.validation.job.cache.access.CacheAccessByKey;

import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.concurrent.CountDownLatch;

/**
 * Performs analysis for the TS 119 602 List Of Trusted Entities
 *
 */
public class LoTEAnalysis extends AbstractRunnableLoTEAnalysis<LoTESource> {

    /**
     * Default constructor
     *
     * @param source {@link LoTESource}
     * @param cacheAccess {@link CacheAccessByKey}
     * @param dssFileLoader {@link DSSFileLoader}
     * @param latch {@link CountDownLatch}
     */
    public LoTEAnalysis(LoTESource source, CacheAccessByKey cacheAccess, DSSFileLoader dssFileLoader,
                        CountDownLatch latch) {
        super(source, cacheAccess, dssFileLoader, latch);
    }

    @Override
    protected LoTEAnalysisExecutor getAnalysisExecutor(DSSDocument document) {
        ServiceLoader<LoTEAnalysisExecutor> serviceLoaders = ServiceLoader.load(LoTEAnalysisExecutor.class);
        for (LoTEAnalysisExecutor executor : serviceLoaders) {
            if (executor.isSupported(document)) {
                return executor;
            }
        }
        Iterator<LoTEAnalysisExecutor> iterator = serviceLoaders.iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }
        throw new UnsupportedOperationException("Please ensure that at least one of dss-lote-validation-xml or " +
                "dss-lote-validation-json modules is added within the dependencies list.");
    }

}
