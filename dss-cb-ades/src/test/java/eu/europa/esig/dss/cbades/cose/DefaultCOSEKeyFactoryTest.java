package eu.europa.esig.dss.cbades.cose;

import eu.europa.esig.dss.cbades.COSEConstants;
import eu.europa.esig.dss.cbades.cbor.CBORMap;
import eu.europa.esig.dss.enumerations.EllipticCurve;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.spec.ECGenParameterSpec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultCOSEKeyFactoryTest {

    private static DefaultCOSEKeyFactory factory;

    @BeforeAll
    static void setup() {
        factory = new DefaultCOSEKeyFactory();
    }

    @Test
    void testECDSA_P256() throws Exception {
        KeyPair kp = generateEC("secp256r1");
        CBORMap cose = factory.create(kp.getPublic());

        assertEquals(4, cose.getSize());
        assertEquals(COSEConstants.COSE_KEY_TYPE_EC2_VALUE, cose.getAsLong(COSEConstants.COSE_KEY_KTY));
        assertEquals(EllipticCurve.P_256.getCOSEValue(), cose.getAsLong(COSEConstants.COSE_KEY_TYPE_EC2_CRV));
        assertEquals(EllipticCurve.P_256.getSize(), (cose.getAsBinaries(COSEConstants.COSE_KEY_TYPE_EC2_X)).length);
        assertEquals(EllipticCurve.P_256.getSize(), (cose.getAsBinaries(COSEConstants.COSE_KEY_TYPE_EC2_Y)).length);
    }

    @Test
    void testECDSA_P384() throws Exception {
        KeyPair kp = generateEC("secp384r1");
        CBORMap cose = factory.create(kp.getPublic());

        assertEquals(4, cose.getSize());
        assertEquals(COSEConstants.COSE_KEY_TYPE_EC2_VALUE, cose.getAsLong(COSEConstants.COSE_KEY_KTY));
        assertEquals(EllipticCurve.P_384.getCOSEValue(), cose.getAsLong(COSEConstants.COSE_KEY_TYPE_EC2_CRV));
        assertEquals(EllipticCurve.P_384.getSize(), (cose.getAsBinaries(COSEConstants.COSE_KEY_TYPE_EC2_X)).length);
        assertEquals(EllipticCurve.P_384.getSize(), (cose.getAsBinaries(COSEConstants.COSE_KEY_TYPE_EC2_Y)).length);
    }

    @Test
    void testECDSA_P512() throws Exception {
        KeyPair kp = generateEC("secp521r1");
        CBORMap cose = factory.create(kp.getPublic());

        assertEquals(4, cose.getSize());
        assertEquals(COSEConstants.COSE_KEY_TYPE_EC2_VALUE, cose.getAsLong(COSEConstants.COSE_KEY_KTY));
        assertEquals(EllipticCurve.P_521.getCOSEValue(), cose.getAsLong(COSEConstants.COSE_KEY_TYPE_EC2_CRV));
        assertEquals(EllipticCurve.P_521.getSize(), (cose.getAsBinaries(COSEConstants.COSE_KEY_TYPE_EC2_X)).length);
        assertEquals(EllipticCurve.P_521.getSize(), (cose.getAsBinaries(COSEConstants.COSE_KEY_TYPE_EC2_Y)).length);
    }

    @Test
    void testECDSA_brainpoolP256() throws Exception {
        KeyPair kp = generateEC("brainpoolP256r1");
        CBORMap cose = factory.create(kp.getPublic());

        assertEquals(4, cose.getSize());
        assertEquals(COSEConstants.COSE_KEY_TYPE_EC2_VALUE, cose.getAsLong(COSEConstants.COSE_KEY_KTY));
        assertEquals(EllipticCurve.BRAINPOOL_P256_R1.getCOSEValue(), cose.getAsLong(COSEConstants.COSE_KEY_TYPE_EC2_CRV));
        assertEquals(EllipticCurve.BRAINPOOL_P256_R1.getSize(), (cose.getAsBinaries(COSEConstants.COSE_KEY_TYPE_EC2_X)).length);
        assertEquals(EllipticCurve.BRAINPOOL_P256_R1.getSize(), (cose.getAsBinaries(COSEConstants.COSE_KEY_TYPE_EC2_Y)).length);
    }

    @Test
    void testECDSA_brainpoolP320() throws Exception {
        KeyPair kp = generateEC("brainpoolP320r1");
        CBORMap cose = factory.create(kp.getPublic());

        assertEquals(4, cose.getSize());
        assertEquals(COSEConstants.COSE_KEY_TYPE_EC2_VALUE, cose.getAsLong(COSEConstants.COSE_KEY_KTY));
        assertEquals(EllipticCurve.BRAINPOOL_P320_R1.getCOSEValue(), cose.getAsLong(COSEConstants.COSE_KEY_TYPE_EC2_CRV));
        assertEquals(EllipticCurve.BRAINPOOL_P320_R1.getSize(), (cose.getAsBinaries(COSEConstants.COSE_KEY_TYPE_EC2_X)).length);
        assertEquals(EllipticCurve.BRAINPOOL_P320_R1.getSize(), (cose.getAsBinaries(COSEConstants.COSE_KEY_TYPE_EC2_Y)).length);
    }

    @Test
    void testECDSA_brainpoolP384() throws Exception {
        KeyPair kp = generateEC("brainpoolP384r1");
        CBORMap cose = factory.create(kp.getPublic());

        assertEquals(4, cose.getSize());
        assertEquals(COSEConstants.COSE_KEY_TYPE_EC2_VALUE, cose.getAsLong(COSEConstants.COSE_KEY_KTY));
        assertEquals(EllipticCurve.BRAINPOOL_P384_R1.getCOSEValue(), cose.getAsLong(COSEConstants.COSE_KEY_TYPE_EC2_CRV));
        assertEquals(EllipticCurve.BRAINPOOL_P384_R1.getSize(), (cose.getAsBinaries(COSEConstants.COSE_KEY_TYPE_EC2_X)).length);
        assertEquals(EllipticCurve.BRAINPOOL_P384_R1.getSize(), (cose.getAsBinaries(COSEConstants.COSE_KEY_TYPE_EC2_Y)).length);
    }

    @Test
    void testECDSA_brainpoolP512() throws Exception {
        KeyPair kp = generateEC("brainpoolP512r1");
        CBORMap cose = factory.create(kp.getPublic());

        assertEquals(4, cose.getSize());
        assertEquals(COSEConstants.COSE_KEY_TYPE_EC2_VALUE, cose.getAsLong(COSEConstants.COSE_KEY_KTY));
        assertEquals(EllipticCurve.BRAINPOOL_P512_R1.getCOSEValue(), cose.getAsLong(COSEConstants.COSE_KEY_TYPE_EC2_CRV));
        assertEquals(EllipticCurve.BRAINPOOL_P512_R1.getSize(), (cose.getAsBinaries(COSEConstants.COSE_KEY_TYPE_EC2_X)).length);
        assertEquals(EllipticCurve.BRAINPOOL_P512_R1.getSize(), (cose.getAsBinaries(COSEConstants.COSE_KEY_TYPE_EC2_Y)).length);
    }

    @Test
    void testEd25519() throws Exception {
        KeyPair kp = generateEd("Ed25519");
        CBORMap cose = factory.create(kp.getPublic());

        assertEquals(3, cose.getSize());
        assertEquals(COSEConstants.COSE_KEY_TYPE_OKP_VALUE, cose.getAsLong(COSEConstants.COSE_KEY_KTY));
        assertEquals(EllipticCurve.ED25519.getCOSEValue(), cose.getAsLong(COSEConstants.COSE_KEY_TYPE_OKP_CRV));
        assertEquals(EllipticCurve.ED25519.getSize(), cose.getAsBinaries(COSEConstants.COSE_KEY_TYPE_OKP_X).length);
    }

    @Test
    void testEd448() throws Exception {
        KeyPair kp = generateEd("Ed448");
        CBORMap cose = factory.create(kp.getPublic());

        assertEquals(3, cose.getSize());
        assertEquals(COSEConstants.COSE_KEY_TYPE_OKP_VALUE, cose.getAsLong(COSEConstants.COSE_KEY_KTY));
        assertEquals(EllipticCurve.ED448.getCOSEValue(), cose.getAsLong(COSEConstants.COSE_KEY_TYPE_OKP_CRV));
        assertEquals(EllipticCurve.ED448.getSize(), cose.getAsBinaries(COSEConstants.COSE_KEY_TYPE_OKP_X).length);
    }

    @Test
    void testX25519() throws Exception {
        KeyPair kp = generateEd("X25519");
        CBORMap cose = factory.create(kp.getPublic());

        assertEquals(3, cose.getSize());
        assertEquals(COSEConstants.COSE_KEY_TYPE_OKP_VALUE, cose.getAsLong(COSEConstants.COSE_KEY_KTY));
        assertEquals(EllipticCurve.X25519.getCOSEValue(), cose.getAsLong(COSEConstants.COSE_KEY_TYPE_OKP_CRV));
        assertEquals(EllipticCurve.X25519.getSize(), cose.getAsBinaries(COSEConstants.COSE_KEY_TYPE_OKP_X).length);
    }

    @Test
    void testX448() throws Exception {
        KeyPair kp = generateEd("X448");
        CBORMap cose = factory.create(kp.getPublic());

        assertEquals(3, cose.getSize());
        assertEquals(COSEConstants.COSE_KEY_TYPE_OKP_VALUE, cose.getAsLong(COSEConstants.COSE_KEY_KTY));
        assertEquals(EllipticCurve.X448.getCOSEValue(), cose.getAsLong(COSEConstants.COSE_KEY_TYPE_OKP_CRV));
        assertEquals(EllipticCurve.X448.getSize(), cose.getAsBinaries(COSEConstants.COSE_KEY_TYPE_OKP_X).length);
    }

    @Test
    void testRSA2048() throws Exception {
        KeyPair kp = generateRSA(2048);
        CBORMap cose = factory.create(kp.getPublic());

        assertEquals(3, cose.getSize());
        assertEquals(COSEConstants.COSE_KEY_TYPE_RSA_VALUE, cose.getAsLong(COSEConstants.COSE_KEY_KTY));
        assertEquals(256, cose.getAsBinaries(COSEConstants.COSE_KEY_TYPE_RSA_N).length); // 2048 bits
        assertTrue(cose.getAsBinaries(COSEConstants.COSE_KEY_TYPE_RSA_E).length <= 4);   // usually 3 bytes (65537)
    }

    @Test
    void testRSA4096() throws Exception {
        KeyPair kp = generateRSA(4096);
        CBORMap cose = factory.create(kp.getPublic());

        assertEquals(3, cose.getSize());
        assertEquals(COSEConstants.COSE_KEY_TYPE_RSA_VALUE, cose.getAsLong(COSEConstants.COSE_KEY_KTY));
        assertEquals(512, cose.getAsBinaries(COSEConstants.COSE_KEY_TYPE_RSA_N).length);
        assertTrue(cose.getAsBinaries(COSEConstants.COSE_KEY_TYPE_RSA_E).length <= 4);   // usually 3 bytes (65537)
    }

    @Test
    void testNull() {
        Exception exception = assertThrows(NullPointerException.class, () -> factory.create(null));
        assertEquals("Public key cannot be null!", exception.getMessage());
    }

    @Test
    void testUnsupportedKey() {
        PublicKey fakeKey = new PublicKey() {

            private static final long serialVersionUID = 2417707349262582123L;

            public String getAlgorithm() { return "Fake"; }
            public String getFormat() { return "X.509"; }
            public byte[] getEncoded() { return new byte[0]; }
        };

        Exception exception = assertThrows(UnsupportedOperationException.class, () -> factory.create(fakeKey));
        assertEquals("The key of type '' is not supported! Provide a custom COSEKeyFactory should you need to support the key.", exception.getMessage());
    }

    private KeyPair generateEC(String curve) throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC", "BC");
        kpg.initialize(new ECGenParameterSpec(curve));
        return kpg.generateKeyPair();
    }

    private KeyPair generateEd(String alg) throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(alg, "BC");
        return kpg.generateKeyPair();
    }

    private KeyPair generateRSA(int size) throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(size);
        return kpg.generateKeyPair();
    }
    
}
