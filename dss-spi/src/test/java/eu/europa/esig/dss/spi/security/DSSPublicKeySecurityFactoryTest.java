package eu.europa.esig.dss.spi.security;

import eu.europa.esig.dss.model.DSSException;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DSSPublicKeySecurityFactoryTest {

    @Test
    void loadRsaPublicKeyFromBinary() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);

        KeyPair keyPair = kpg.generateKeyPair();

        byte[] encoded = keyPair.getPublic().getEncoded();

        PublicKey loadedKey =
                DSSPublicKeySecurityFactory.BINARY_INSTANCE.build(encoded);

        assertNotNull(loadedKey);
        assertEquals(keyPair.getPublic(), loadedKey);
        assertArrayEquals(encoded, loadedKey.getEncoded());
    }

    @Test
    void loadRsaPublicKeyFromInputStream() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);

        KeyPair keyPair = kpg.generateKeyPair();

        byte[] encoded = keyPair.getPublic().getEncoded();

        PublicKey loadedKey =
                DSSPublicKeySecurityFactory.INPUT_STREAM_INSTANCE.build(
                        new ByteArrayInputStream(encoded));

        assertNotNull(loadedKey);
        assertEquals(keyPair.getPublic(), loadedKey);
    }

    @Test
    void loadEcPublicKeyFromBinary() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
        kpg.initialize(256);

        KeyPair keyPair = kpg.generateKeyPair();

        PublicKey loadedKey =
                DSSPublicKeySecurityFactory.BINARY_INSTANCE.build(
                        keyPair.getPublic().getEncoded());

        assertNotNull(loadedKey);
        assertEquals(keyPair.getPublic(), loadedKey);
    }

    @Test
    void loadEcdsaPublicKeyFromBinary() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
        kpg.initialize(256); // secp256r1

        KeyPair keyPair = kpg.generateKeyPair();

        byte[] encoded = keyPair.getPublic().getEncoded();

        PublicKey loadedKey =
                DSSPublicKeySecurityFactory.BINARY_INSTANCE.build(encoded);

        assertNotNull(loadedKey);
        assertEquals("ECDSA", loadedKey.getAlgorithm());
        assertEquals(keyPair.getPublic(), loadedKey);
        assertArrayEquals(encoded, loadedKey.getEncoded());
    }

    @Test
    void loadEcdsaPublicKeyFromInputStream() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
        kpg.initialize(256); // secp256r1

        KeyPair keyPair = kpg.generateKeyPair();

        byte[] encoded = keyPair.getPublic().getEncoded();

        PublicKey loadedKey =
                DSSPublicKeySecurityFactory.INPUT_STREAM_INSTANCE.build(
                        new ByteArrayInputStream(encoded));

        assertNotNull(loadedKey);
        assertEquals("ECDSA", loadedKey.getAlgorithm());
        assertEquals(keyPair.getPublic(), loadedKey);
        assertArrayEquals(encoded, loadedKey.getEncoded());
    }

    @Test
    void binaryAndInputStreamReturnEquivalentKeys() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);

        PublicKey original = kpg.generateKeyPair().getPublic();

        byte[] encoded = original.getEncoded();

        PublicKey binaryLoaded =
                DSSPublicKeySecurityFactory.BINARY_INSTANCE.build(encoded);

        PublicKey streamLoaded =
                DSSPublicKeySecurityFactory.INPUT_STREAM_INSTANCE.build(
                        new ByteArrayInputStream(encoded));

        assertEquals(binaryLoaded, streamLoaded);
        assertArrayEquals(binaryLoaded.getEncoded(), streamLoaded.getEncoded());
    }

    @Test
    void nullBinaryInputThrowsException() {
        Exception exception = assertThrows(NullPointerException.class,
                () -> DSSPublicKeySecurityFactory.BINARY_INSTANCE.build(null));
        assertEquals("Input cannot be null", exception.getMessage());
    }

    @Test
    void nullInputStreamThrowsException() {
        Exception exception = assertThrows(NullPointerException.class,
                () -> DSSPublicKeySecurityFactory.INPUT_STREAM_INSTANCE.build(null));
        assertEquals("Input cannot be null", exception.getMessage());
    }

    @Test
    void invalidBinaryInputThrowsException() {
        byte[] invalid = new byte[] {1, 2, 3, 4, 5};

        Exception exception = assertThrows(DSSException.class,
                () -> DSSPublicKeySecurityFactory.BINARY_INSTANCE.build(invalid));
        assertEquals("Unable to load KeyFactory for the given byte[]. All security providers have failed. " +
                "More detail in debug mode.", exception.getMessage());
    }

    @Test
    void invalidInputStreamThrowsException() {
        byte[] invalid = new byte[] {1, 2, 3, 4, 5};

        Exception exception = assertThrows(DSSException.class,
                () -> DSSPublicKeySecurityFactory.INPUT_STREAM_INSTANCE.build(
                        new ByteArrayInputStream(invalid)));
        assertEquals("Unable to load KeyFactory for the given ByteArrayInputStream. All security providers have failed. " +
                "More detail in debug mode.", exception.getMessage());
    }

}
