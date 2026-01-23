package eu.europa.esig.dss.lote.analysis;

import eu.europa.esig.dss.lote.cache.access.CacheAccessByKey;
import eu.europa.esig.dss.lote.source.ListSource;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.spi.client.http.DSSFileLoader;

import java.util.ServiceLoader;
import java.util.concurrent.CountDownLatch;

public class ListAnalysis extends AbstractRunnableAnalysis {

    /**
     * Default constructor
     *
     * @param source {@link ListSource}
     * @param cacheAccess {@link CacheAccessByKey}
     * @param dssFileLoader {@link DSSFileLoader}
     * @param latch {@link CountDownLatch}
     */
    public ListAnalysis(ListSource source, CacheAccessByKey cacheAccess, DSSFileLoader dssFileLoader,
                        CountDownLatch latch) {
        super(source, cacheAccess, dssFileLoader, latch);
    }

    @Override
    protected ListAnalysisExecutor getAnalysisExecutor(DSSDocument document) {
        ServiceLoader<ListAnalysisExecutor> serviceLoaders = ServiceLoader.load(ListAnalysisExecutor.class);
        for (ListAnalysisExecutor executor : serviceLoaders) {
            if (executor.isSupported(document)) {
                return executor;
            }
        }
        throw new UnsupportedOperationException("Please ensure that at least one of dss-lote-validation-xml or " +
                "dss-lote-validation-json modules is added within the dependencies list.");
    }

}
