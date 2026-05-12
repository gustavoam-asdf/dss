package eu.europa.esig.dss.spi.x509;

import eu.europa.esig.dss.model.x509.CertificateToken;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.utils.Utils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class KidCertificateSourceTest {

    private static CertificateToken token;
    private static CertificateToken token2;

    private static byte[] issuerSerialToken;
    private static byte[] issuerSerialToken2;

    @BeforeAll
    static void init() {
        token = DSSUtils.loadCertificate(new File("src/test/resources/citizen_ca.cer"));
        token2 = DSSUtils.loadCertificate(new File("src/test/resources/ecdsa.cer"));

        issuerSerialToken = DSSUtils.generateKid(token);
        issuerSerialToken2 = DSSUtils.generateKid(token2);
    }

    @Test
    void addCertificatesTest() {
        KidCertificateSource kidCertificateSource = new KidCertificateSource();
        assertEquals(0, kidCertificateSource.getNumberOfCertificates());

        kidCertificateSource.addCertificate(token);
        assertEquals(1, kidCertificateSource.getNumberOfCertificates());
        assertEquals(token, kidCertificateSource.getCertificateByKid(Utils.toBase64(issuerSerialToken)));

        kidCertificateSource.addCertificate(token2);
        assertEquals(2, kidCertificateSource.getNumberOfCertificates());
        assertEquals(token, kidCertificateSource.getCertificateByKid(Utils.toBase64(issuerSerialToken)));
        assertEquals(token2, kidCertificateSource.getCertificateByKid(Utils.toBase64(issuerSerialToken2)));

        kidCertificateSource = new KidCertificateSource();
        kidCertificateSource.addCertificate("KID-ID", token);

        assertEquals(1, kidCertificateSource.getNumberOfCertificates());
        assertNull(kidCertificateSource.getCertificateByKid(Utils.toBase64(issuerSerialToken)));
        assertEquals(token, kidCertificateSource.getCertificateByKid("KID-ID"));

        kidCertificateSource = new KidCertificateSource();
        kidCertificateSource.addCertificate("KID-ID".getBytes(), token);

        assertEquals(1, kidCertificateSource.getNumberOfCertificates());
        assertNull(kidCertificateSource.getCertificateByKid(Utils.toBase64(issuerSerialToken)));
        assertEquals(token, kidCertificateSource.getCertificateByKid("KID-ID".getBytes()));
        assertEquals(token, kidCertificateSource.getCertificateByKid(Utils.toBase64("KID-ID".getBytes())));
    }

    @Test
    void nullTest() {
        KidCertificateSource kidCertificateSource = new KidCertificateSource();

        Exception exception = assertThrows(NullPointerException.class, () -> kidCertificateSource.addCertificate(null));
        assertEquals("The certificate must be filled", exception.getMessage());

        exception = assertThrows(NullPointerException.class, () -> kidCertificateSource.addCertificate((String) null, token));
        assertEquals("kid cannot be null!", exception.getMessage());

        exception = assertThrows(NullPointerException.class, () -> kidCertificateSource.addCertificate((byte[]) null, token));
        assertEquals("kid cannot be null!", exception.getMessage());

        exception = assertThrows(NullPointerException.class, () -> kidCertificateSource.addCertificate("KID-ID", null));
        assertEquals("The certificate must be filled", exception.getMessage());

        exception = assertThrows(NullPointerException.class, () -> kidCertificateSource.addCertificate("KID-ID".getBytes(), null));
        assertEquals("The certificate must be filled", exception.getMessage());
    }

}
