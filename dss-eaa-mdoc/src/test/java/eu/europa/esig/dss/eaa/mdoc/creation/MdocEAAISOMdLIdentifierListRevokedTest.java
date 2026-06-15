package eu.europa.esig.dss.eaa.mdoc.creation;

import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.EAARevocationWrapper;
import eu.europa.esig.dss.diagnostic.EAAWrapper;
import eu.europa.esig.dss.eaa.mdoc.pki.PKICWTIdentifierListSource;
import eu.europa.esig.dss.enumerations.EAAStatus;
import eu.europa.esig.dss.spi.eaa.status.EAARevocationSource;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MdocEAAISOMdLIdentifierListRevokedTest extends MdocEAAISOMdLIdentifierListTest {

    @Override
    protected EAARevocationSource getEAAStatusSource() {
        PKICWTIdentifierListSource identifierListSource = new PKICWTIdentifierListSource(getCertEntityRepository(), getCertEntity(GOOD_CA));
        identifierListSource.setIdentifiers(Collections.singletonList(new byte[] { 1 }));
        return identifierListSource;
    }

    @Override
    protected void checkEAARevocations(DiagnosticData diagnosticData) {
        EAAWrapper eaa = diagnosticData.getEAAById(diagnosticData.getFirstEAAId());
        List<EAARevocationWrapper> eaaStatuses = eaa.getEAARevocations();
        assertEquals(1, eaaStatuses.size());
        assertEquals(EAAStatus.INVALID, eaaStatuses.get(0).getStatus());
        assertEquals("application/identifierlist+cwt", eaaStatuses.get(0).getType());
    }

}
