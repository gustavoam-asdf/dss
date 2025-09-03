package eu.europa.esig.dss.model.policy.crypto;

import eu.europa.esig.dss.enumerations.CryptographicSuiteAlgorithmUsage;
import eu.europa.esig.dss.enumerations.CryptographicSuiteRecommendation;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;

class CryptographicSuiteEvaluationTest {

    @Test
    void testNull() {
        assertNull(CryptographicSuiteEvaluation.copy(null));
    }

    @Test
    void testFilled() {
        CryptographicSuiteParameter param = new CryptographicSuiteParameter();
        param.setName("n");
        param.setMin(1);
        param.setMax(2);

        Date start = new Date(1000L);
        Date end = new Date(2000L);

        CryptographicSuiteEvaluation original = new CryptographicSuiteEvaluation();
        original.setParameterList(Collections.singletonList(param));
        original.setValidityStart(start);
        original.setValidityEnd(end);
        original.setAlgorithmUsage(Collections.singletonList(CryptographicSuiteAlgorithmUsage.SIGN_DATA));
        original.setRecommendation(CryptographicSuiteRecommendation.RECOMMENDED);

        CryptographicSuiteEvaluation copy = CryptographicSuiteEvaluation.copy(original);

        assertNotNull(copy);
        assertEquals(start, copy.getValidityStart());
        assertEquals(end, copy.getValidityEnd());
        assertEquals(CryptographicSuiteRecommendation.RECOMMENDED, copy.getRecommendation());

        // parameter deep copy check
        assertNotNull(copy.getParameterList());
        assertEquals(1, copy.getParameterList().size());
        assertNotSame(param, copy.getParameterList().get(0));
        assertEquals("n", copy.getParameterList().get(0).getName());

        // algorithmUsage shallow copy check
        assertNotNull(copy.getAlgorithmUsage());
        assertEquals(1, copy.getAlgorithmUsage().size());
        assertEquals(CryptographicSuiteAlgorithmUsage.SIGN_DATA, copy.getAlgorithmUsage().get(0));
    }

    @Test
    void testCopyEmpty() {
        CryptographicSuiteEvaluation original = new CryptographicSuiteEvaluation();
        CryptographicSuiteEvaluation copy = CryptographicSuiteEvaluation.copy(original);

        assertNull(copy.getParameterList());
        assertNull(copy.getValidityStart());
        assertNull(copy.getValidityEnd());
        assertNull(copy.getAlgorithmUsage());
        assertNull(copy.getRecommendation());
    }

}
