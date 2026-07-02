package eu.europa.esig.dss.lote.runnable;

import eu.europa.esig.dss.lote.source.LoLoTESource;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.spi.client.http.DSSFileLoader;
import eu.europa.esig.dss.validation.job.cache.access.CacheAccessByKey;

import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.concurrent.CountDownLatch;

/**
 * Performs analysis for the List of TS 119 602 Lists Of Trusted Entities
 *
 */
public class LoLoTEAnalysis extends AbstractRunnableLoTEAnalysis<LoLoTESource> {

    /**
     * Default constructor
     *
     * @param source {@link LoLoTESource}
     * @param cacheAccess {@link CacheAccessByKey}
     * @param dssFileLoader {@link DSSFileLoader}
     * @param latch {@link CountDownLatch}
     */
    public LoLoTEAnalysis(LoLoTESource source, CacheAccessByKey cacheAccess, DSSFileLoader dssFileLoader,
                        CountDownLatch latch) {
        super(source, cacheAccess, dssFileLoader, latch);
    }

    @Override
    protected LoLoTEAnalysisExecutor getAnalysisExecutor(DSSDocument document) {
        ServiceLoader<LoLoTEAnalysisExecutor> serviceLoaders = ServiceLoader.load(LoLoTEAnalysisExecutor.class);
        for (LoLoTEAnalysisExecutor executor : serviceLoaders) {
            if (executor.isSupported(document)) {
                return executor;
            }
        }
        Iterator<LoLoTEAnalysisExecutor> iterator = serviceLoaders.iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }
        throw new UnsupportedOperationException("Please ensure that at least one of dss-lote-validation-xml or " +
                "dss-lote-validation-json modules is added within the dependencies list.");
    }

}
