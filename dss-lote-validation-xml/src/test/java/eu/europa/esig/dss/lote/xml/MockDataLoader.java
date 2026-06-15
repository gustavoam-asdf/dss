package eu.europa.esig.dss.lote.xml;

import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.spi.client.http.DataLoader;

import java.util.List;
import java.util.Map;

public class MockDataLoader implements DataLoader {

    private static final long serialVersionUID = 4369830794589740505L;

    private Map<String, DSSDocument> dataMap;

    public MockDataLoader(Map<String, DSSDocument> dataMap) {
        this.dataMap = dataMap;
    }

    @Override
    public byte[] get(String url) {
        DSSDocument dssDocument = dataMap.get(url);
        if (dssDocument != null) {
            return DSSUtils.toByteArray(dssDocument);
        }
        return null;
    }

    @Override
    public DataAndUrl get(List<String> urlStrings) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public byte[] post(String url, byte[] content) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setContentType(String contentType) {
        // TODO Auto-generated method stub

    }

}