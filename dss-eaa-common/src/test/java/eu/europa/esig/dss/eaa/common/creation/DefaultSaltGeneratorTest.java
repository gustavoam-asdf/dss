package eu.europa.esig.dss.eaa.common.creation;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultSaltGeneratorTest {

    @Test
    void testDefaultSaltGenerator() {
        DefaultEAASaltGenerator defaultSaltGenerator = new DefaultEAASaltGenerator();

        final List<byte[]> salts = new ArrayList<>();
        for (int i = 0; i<1000; i++) {
            salts.add(defaultSaltGenerator.generateSalt());
        }

        // Check that all the generated salts are different
        assertEquals(salts.size(), salts.stream().distinct().count());
    }

    @Test
    void testSaltLength() {
        DefaultEAASaltGenerator defaultSaltGenerator = new DefaultEAASaltGenerator();
        assertEquals(DefaultEAASaltGenerator.DEFAULT_SALT_LENGTH, defaultSaltGenerator.generateSalt().length);

        DefaultEAASaltGenerator saltGenerator = new DefaultEAASaltGenerator(20);
        assertEquals(20, saltGenerator.generateSalt().length);
    }

}
