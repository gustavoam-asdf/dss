package eu.europa.esig.dss.lote.json.download;

import eu.europa.esig.dss.lote.download.DownloadResult;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.model.DigestDocument;
import eu.europa.esig.dss.model.FileDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.spi.DSSUtils;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LoTEJWSCompactDownloadTaskTest {

    @Test
    void testValid() {
        DSSDocument trustedList = new FileDocument("src/test/resources/pid-providers.json");
        LoTEJWSCompactDownloadTask task = new LoTEJWSCompactDownloadTask(trustedList, "https://dss.nowina.lu/pid-providers.json");
        DownloadResult result = task.get();
        assertNotNull(result);
        assertNotNull(result.getDSSDocument());
        assertNotNull(result.getDigest());
        assertFalse(result.getDigest().isEmpty());
    }

    @Test
    void nullResultTest() {
        Exception exception = assertThrows(NullPointerException.class, () -> new LoTEJWSCompactDownloadTask(null, null));
        assertEquals("The url is null", exception.getMessage());

        Map<String, DSSDocument> dataMap = new HashMap<>();
        dataMap.put("null", null);
        dataMap.put("empty-document", new InMemoryDocument());
        dataMap.put("empty-array", new InMemoryDocument(DSSUtils.EMPTY_BYTE_ARRAY));
        dataMap.put("0", new InMemoryDocument(new byte[] { 0 }));
        dataMap.put("digestDoc", new DigestDocument());
        for (Map.Entry<String, DSSDocument> entry : dataMap.entrySet()) {
            LoTEJWSCompactDownloadTask task = new LoTEJWSCompactDownloadTask(entry.getValue(), entry.getKey());
            assertThrows(DSSException.class, task::get);
        }
    }

    @Test
    void notJwsCompactTest() {
        Map<String, DSSDocument> dataMap = new HashMap<>();
        dataMap.put("text", new InMemoryDocument("ey".getBytes()));
        dataMap.put("not-compact", new InMemoryDocument("ey.ey".getBytes()));
        dataMap.put("json", new InMemoryDocument("{ \"hello\" : \"world\" }".getBytes()));
        dataMap.put("sd-jwt", new InMemoryDocument("ey.ey.ey~".getBytes()));
        for (Map.Entry<String, DSSDocument> entry : dataMap.entrySet()) {
            LoTEJWSCompactDownloadTask task = new LoTEJWSCompactDownloadTask(entry.getValue(), entry.getKey());
            assertThrows(DSSException.class, task::get);
        }
    }

}
