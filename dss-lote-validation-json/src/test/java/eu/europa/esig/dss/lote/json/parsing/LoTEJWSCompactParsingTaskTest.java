package eu.europa.esig.dss.lote.json.parsing;

import eu.europa.esig.dss.lote.parsing.ParsingResult;
import eu.europa.esig.dss.lote.source.ListSource;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.model.FileDocument;
import eu.europa.esig.dss.model.lote.EntityService;
import eu.europa.esig.dss.model.lote.ServiceStatusAndInformationExtensions;
import eu.europa.esig.dss.model.lote.TrustedEntity;
import eu.europa.esig.dss.model.timedependent.TimeDependentValues;
import eu.europa.esig.dss.utils.Utils;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoTEJWSCompactParsingTaskTest {

    @Test
    void testValid() {
        DSSDocument trustedList = new FileDocument("src/test/resources/pid-providers.json");
        LoTEJWSCompactParsingTask task = new LoTEJWSCompactParsingTask(trustedList, new ListSource());
        ParsingResult result = task.get();
        assertNotNull(result);
        assertEquals(1, result.getVersion());
        assertEquals(1, result.getSequenceNumber());
        assertNotNull(result.getIssueDate());
        assertNotNull(result.getNextUpdateDate());
        assertEquals("EU", result.getTerritory());
        assertFalse(Utils.isCollectionNotEmpty(result.getDistributionPoints()));
        assertFalse(Utils.isCollectionNotEmpty(result.getStructureValidationMessages()));

        List<TrustedEntity> trustedEntities = result.getTrustedEntities();
        assertNotNull(trustedEntities);
        assertEquals(15, trustedEntities.size());

        checkTEs(trustedEntities);

        for (int i = 0; i < trustedEntities.size(); i++) {
            TrustedEntity trustedEntity = trustedEntities.get(i);
            if (i == 0) {
                assertEquals(3, trustedEntity.getServices().size());
            } else {
                assertEquals(1, trustedEntity.getServices().size());
            }
        }
    }

    @Test
    void testWrongPayload() {
        DSSDocument trustedList = new FileDocument("src/test/resources/pid-providers-broken-json.json");
        LoTEJWSCompactParsingTask task = new LoTEJWSCompactParsingTask(trustedList, new ListSource());
        Exception exception = assertThrows(DSSException.class, task::get);
        assertTrue(exception.getMessage().contains("Unable to parse binaries."));
    }

    @Test
    void testStructureError() {
        DSSDocument trustedList = new FileDocument("src/test/resources/pid-providers-structure-error.json");
        LoTEJWSCompactParsingTask task = new LoTEJWSCompactParsingTask(trustedList, new ListSource());
        ParsingResult result = task.get();
        assertNotNull(result);
        assertEquals(1, result.getVersion());
        assertEquals(1, result.getSequenceNumber());
        assertNotNull(result.getIssueDate());
        assertNotNull(result.getNextUpdateDate());
        assertEquals("EU", result.getTerritory());
        assertFalse(Utils.isCollectionNotEmpty(result.getDistributionPoints()));
        assertTrue(Utils.isCollectionNotEmpty(result.getStructureValidationMessages()));

        List<TrustedEntity> trustedEntities = result.getTrustedEntities();
        assertNotNull(trustedEntities);
        assertEquals(1, trustedEntities.size());
    }

    private void checkTEs(List<TrustedEntity> trustedEntities) {
        for (TrustedEntity te : trustedEntities) {

            assertNotNull(te.getNames());
            assertFalse(te.getNames().isEmpty());

            assertNotNull(te.getTradeNames());
            assertFalse(te.getTradeNames().isEmpty());

            assertNotNull(te.getElectronicAddresses());
            assertFalse(te.getElectronicAddresses().isEmpty());

            assertNotNull(te.getPostalAddresses());
            assertFalse(te.getPostalAddresses().isEmpty());

            assertNotNull(te.getInformation());
            assertFalse(te.getInformation().isEmpty());

            assertNotNull(te.getServices());
            assertFalse(te.getServices().isEmpty());

            checkServices(te.getServices());
        }
    }

    private void checkServices(List<EntityService> services) {
        for (EntityService entityService : services) {
            assertNotNull(entityService.getCertificates());
            assertFalse(entityService.getCertificates().isEmpty());

            TimeDependentValues<ServiceStatusAndInformationExtensions> statusAndInformationExtensions = entityService.getStatusAndInformationExtensions();
            assertNotNull(statusAndInformationExtensions);

            ServiceStatusAndInformationExtensions latest = statusAndInformationExtensions.getLatest();
            assertNotNull(latest);

            assertNotNull(latest.getNames());
            assertFalse(latest.getNames().isEmpty());

            assertNotNull(latest.getType());
        }
    }

}
