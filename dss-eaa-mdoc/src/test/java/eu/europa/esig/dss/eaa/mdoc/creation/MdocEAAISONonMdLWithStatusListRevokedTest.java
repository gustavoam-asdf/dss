package eu.europa.esig.dss.eaa.mdoc.creation;

import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.EAARevocationWrapper;
import eu.europa.esig.dss.diagnostic.EAAWrapper;
import eu.europa.esig.dss.eaa.mdoc.pki.PKICWTStatusListSource;
import eu.europa.esig.dss.enumerations.EAAStatus;
import eu.europa.esig.dss.spi.eaa.status.EAARevocationSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MdocEAAISONonMdLWithStatusListRevokedTest extends MdocEAAISONonMdLWithStatusListTest {

    @Override
    protected EAARevocationSource getEAAStatusSource() {
        PKICWTStatusListSource statusListSource = new PKICWTStatusListSource(getCertEntityRepository(), getCertEntity(GOOD_CA));
        byte[] bytes = new byte[8];
        Arrays.fill(bytes, (byte) 1);
        statusListSource.setStatusList(bytes);
        return statusListSource;
    }

    @Override
    protected void checkEAARevocations(DiagnosticData diagnosticData) {
        EAAWrapper eaa = diagnosticData.getEAAById(diagnosticData.getFirstEAAId());
        List<EAARevocationWrapper> eaaStatuses = eaa.getEAARevocations();
        assertEquals(1, eaaStatuses.size());
        assertEquals(EAAStatus.INVALID, eaaStatuses.get(0).getStatus());
        assertEquals("application/statuslist+cwt", eaaStatuses.get(0).getType());
    }

}
