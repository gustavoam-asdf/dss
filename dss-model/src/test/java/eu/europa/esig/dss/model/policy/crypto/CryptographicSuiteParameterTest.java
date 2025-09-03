package eu.europa.esig.dss.model.policy.crypto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;

class CryptographicSuiteParameterTest {

    @Test
    void testCopy_nullInput_returnsNull() {
        assertNull(CryptographicSuiteParameter.copy(null));
    }

    @Test
    void testCopy_allFieldsCopied() {
        CryptographicSuiteParameter original = new CryptographicSuiteParameter();
        original.setName("Param");
        original.setMin(5);
        original.setMax(10);

        CryptographicSuiteParameter copy = CryptographicSuiteParameter.copy(original);

        assertNotNull(copy);
        assertEquals("Param", copy.getName());
        assertEquals(5, copy.getMin());
        assertEquals(10, copy.getMax());

        // ensure deep copy (different object)
        assertNotSame(original, copy);
    }

    @Test
    void testCopy_handlesNullFields() {
        CryptographicSuiteParameter original = new CryptographicSuiteParameter();
        CryptographicSuiteParameter copy = CryptographicSuiteParameter.copy(original);

        assertNull(copy.getName());
        assertNull(copy.getMin());
        assertNull(copy.getMax());
    }

}
