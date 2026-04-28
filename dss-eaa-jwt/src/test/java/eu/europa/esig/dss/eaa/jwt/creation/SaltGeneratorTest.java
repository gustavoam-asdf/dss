package eu.europa.esig.dss.eaa.jwt.creation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.jose4j.base64url.Base64Url;
import org.junit.jupiter.api.Test;

class SaltGeneratorTest {
    @Test
    void testDefaultSaltGenerator() {
        SDJWTDefaultSaltGenerator defaultSaltGenerator = new SDJWTDefaultSaltGenerator();

        final List<String> salts = new ArrayList<>();
        for (int i = 0; i<1000; i++) {
            salts.add(defaultSaltGenerator.generateSalt());
        }

        // Check that all the generated salts are different
        assertEquals(salts.size(), salts.stream().distinct().count());
    }

    @Test
    void testSaltLength() {
        SDJWTDefaultSaltGenerator defaultSaltGenerator = new SDJWTDefaultSaltGenerator();
        assertEquals(SDJWTDefaultSaltGenerator.DEFAULT_SALT_LENGTH, Base64Url.decode(defaultSaltGenerator.generateSalt()).length);

        SDJWTDefaultSaltGenerator saltGenerator = new SDJWTDefaultSaltGenerator(20);
        assertEquals(20, Base64Url.decode(saltGenerator.generateSalt()).length);
    }
}
