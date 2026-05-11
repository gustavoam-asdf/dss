package eu.europa.esig.dss.cbades.validation;

import eu.europa.esig.dss.cbades.cbor.CBORArray;
import eu.europa.esig.dss.cbades.cbor.CBORByteString;
import eu.europa.esig.dss.cbades.cbor.CBORMap;
import eu.europa.esig.dss.cbades.cbor.CBORUtils;
import eu.europa.esig.dss.model.x509.CertificateToken;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.spi.client.http.MemoryDataLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class COSEX509URLCertificateSourceTest {

    private static final String URL = "https://nowina.lu/pki-factory/good-pki/cert-chain";

    private static CertificateToken token;
    private static CertificateToken token2;

    @BeforeAll
    static void init() {
        token = DSSUtils.loadCertificate(new File("src/test/resources/CZ.cer"));
        token2 = DSSUtils.loadCertificate(new File("src/test/resources/CZ_CA.cer"));
    }

    @Test
    void test() {
        HashMap<String, byte[]> map = new HashMap<>();
        map.put(URL, token.getEncoded());

        MemoryDataLoader dataLoader = new MemoryDataLoader(map);

        COSEX509URLCertificateSource x509URLCertificateSource = new COSEX509URLCertificateSource(dataLoader);
        assertEquals(Collections.singletonList(token), x509URLCertificateSource.getCertificatesByUrl(URL));

        map.put(URL, DSSUtils.convertToPEM(token).getBytes());
        dataLoader = new MemoryDataLoader(map);

        x509URLCertificateSource = new COSEX509URLCertificateSource(dataLoader);
        assertEquals(Collections.singletonList(token), x509URLCertificateSource.getCertificatesByUrl(URL));

        CBORByteString cborByteString = new CBORByteString(token.getEncoded());
        map.put(URL, CBORUtils.serializeCborObject(cborByteString));
        dataLoader = new MemoryDataLoader(map);

        x509URLCertificateSource = new COSEX509URLCertificateSource(dataLoader);
        assertEquals(Collections.singletonList(token), x509URLCertificateSource.getCertificatesByUrl(URL));

        CBORArray cborArray = new CBORArray();
        cborArray.add(new CBORByteString(token.getEncoded()));
        cborArray.add(new CBORByteString(token2.getEncoded()));
        map.put(URL, CBORUtils.serializeCborObject(cborArray));
        dataLoader = new MemoryDataLoader(map);

        x509URLCertificateSource = new COSEX509URLCertificateSource(dataLoader);
        assertEquals(Arrays.asList(token, token2), x509URLCertificateSource.getCertificatesByUrl(URL));

        CBORMap cborMap = new CBORMap();
        cborMap.put(URL, new CBORByteString(token.getEncoded()));
        map.put(URL, CBORUtils.serializeCborObject(cborMap));
        dataLoader = new MemoryDataLoader(map);

        x509URLCertificateSource = new COSEX509URLCertificateSource(dataLoader);
        assertEquals(Collections.emptyList(), x509URLCertificateSource.getCertificatesByUrl(URL));

        map.put(URL, DSSUtils.EMPTY_BYTE_ARRAY);
        dataLoader = new MemoryDataLoader(map);

        x509URLCertificateSource = new COSEX509URLCertificateSource(dataLoader);
        assertEquals(Collections.emptyList(), x509URLCertificateSource.getCertificatesByUrl(URL));
    }

}
