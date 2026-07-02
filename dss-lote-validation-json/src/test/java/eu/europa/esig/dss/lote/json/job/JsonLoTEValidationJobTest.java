package eu.europa.esig.dss.lote.json.job;

import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.lote.job.LoTEValidationJob;
import eu.europa.esig.dss.lote.json.MockDataLoader;
import eu.europa.esig.dss.lote.source.LoTESource;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.FileDocument;
import eu.europa.esig.dss.model.lote.LoTEInfo;
import eu.europa.esig.dss.model.lote.LoTEValidationJobSummary;
import eu.europa.esig.dss.service.http.commons.FileCacheDataLoader;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.spi.x509.CertificateSource;
import eu.europa.esig.dss.spi.x509.CommonTrustedCertificateSource;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.validation.job.cache.CacheCleaner;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonLoTEValidationJobTest {

    private static LoTEValidationJob loteValidationJob;
    private static CacheCleaner cacheCleaner;
    private static FileCacheDataLoader offlineFileLoader;
    private static FileCacheDataLoader onlineFileLoader;

    private static Map<String, DSSDocument> urlMap;

    private static File cacheDirectory;

    @BeforeAll
    static void initBeforeAll() {
        urlMap = new HashMap<>();

        cacheDirectory = new File("target/cache");

        offlineFileLoader = new FileCacheDataLoader();
        offlineFileLoader.setCacheExpirationTime(Long.MAX_VALUE);
        offlineFileLoader.setDataLoader(new MockDataLoader(urlMap));
        offlineFileLoader.setFileCacheDirectory(cacheDirectory);

        Map<String, DSSDocument> onlineMap = new HashMap<>(urlMap);

        onlineFileLoader = new FileCacheDataLoader();
        onlineFileLoader.setCacheExpirationTime(0);
        onlineFileLoader.setDataLoader(new MockDataLoader(onlineMap));
        onlineFileLoader.setFileCacheDirectory(cacheDirectory);

        cacheCleaner = new CacheCleaner();
        cacheCleaner.setDSSFileLoader(offlineFileLoader);
        cacheCleaner.setCleanFileSystem(true);
    }

    @BeforeEach
    void init() {
        populateMap();
    }

    private void populateMap() {
        urlMap.put("http://dss.nowina.lu/pid-providers.json", new FileDocument("src/test/resources/pid-providers.json"));
    }

    @Test
    void pidProviderListTest() {
        LoTESource pidProvidersLoTESource = getPIDProviderListSource();

        loteValidationJob = new LoTEValidationJob();
        loteValidationJob.setOfflineDataLoader(offlineFileLoader);
        loteValidationJob.setLoTESources(pidProvidersLoTESource);
        loteValidationJob.offlineRefresh();

        LoTEValidationJobSummary summary = loteValidationJob.getSummary();
        assertEquals(1, summary.getOtherLoTEInfos().size());

        LoTEInfo loteInfo = summary.getOtherLoTEInfos().get(0);
        assertTrue(loteInfo.getDownloadCacheInfo().isResultExist());
        assertFalse(loteInfo.getDownloadCacheInfo().isError());
        assertTrue(loteInfo.getParsingCacheInfo().isResultExist());
        assertFalse(loteInfo.getParsingCacheInfo().isError());
        assertFalse(Utils.isCollectionNotEmpty(loteInfo.getParsingCacheInfo().getStructureValidationMessages()));
        assertEquals(15, loteInfo.getParsingCacheInfo().getTrustedEntitiesNumber());
        assertEquals(17, loteInfo.getParsingCacheInfo().getTrustedServicesNumber());
        assertEquals(17, loteInfo.getParsingCacheInfo().getCertNumber());
        assertTrue(loteInfo.getValidationCacheInfo().isResultExist());
        assertFalse(loteInfo.getValidationCacheInfo().isError());
        assertEquals(Indication.TOTAL_PASSED, loteInfo.getValidationCacheInfo().getIndication());
    }

    private LoTESource getPIDProviderListSource() {
        LoTESource pidProviderList = new LoTESource();
        pidProviderList.setUrl("http://dss.nowina.lu/pid-providers.json");
        CertificateSource trustedCertificateSource = new CommonTrustedCertificateSource();
        trustedCertificateSource.addCertificate(DSSUtils.loadCertificate(new File("src/test/resources/pid-providers-cert.cer")));
        pidProviderList.setCertificateSource(trustedCertificateSource);
        return pidProviderList;
    }

}
