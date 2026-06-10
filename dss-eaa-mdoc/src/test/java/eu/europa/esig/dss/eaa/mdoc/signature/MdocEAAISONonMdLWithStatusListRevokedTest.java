package eu.europa.esig.dss.eaa.mdoc.signature;

import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.EAAStatusWrapper;
import eu.europa.esig.dss.diagnostic.EAAWrapper;
import eu.europa.esig.dss.eaa.mdoc.pki.PKICWTStatusListSource;
import eu.europa.esig.dss.enumerations.EAAStatus;
import eu.europa.esig.dss.spi.eaa.statuslist.EAAStatusSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MdocEAAISONonMdLWithStatusListRevokedTest extends MdocEAAISONonMdLWithStatusListTest {

    @Override
    protected EAAStatusSource getEAAStatusSource() {
        PKICWTStatusListSource statusListSource = new PKICWTStatusListSource(getCertEntityRepository(), getCertEntity(GOOD_CA));
        byte[] bytes = new byte[8];
        Arrays.fill(bytes, (byte) 1);
        statusListSource.setStatusList(bytes);
        return statusListSource;
    }

    @Override
    protected void checkEAAStatuses(DiagnosticData diagnosticData) {
        EAAWrapper eaa = diagnosticData.getEAAById(diagnosticData.getFirstEAAId());
        List<EAAStatusWrapper> eaaStatuses = eaa.getEAAStatuses();
        assertEquals(1, eaaStatuses.size());
        assertEquals(EAAStatus.INVALID, eaaStatuses.get(0).getStatus());
        assertEquals("statuslist+cwt", eaaStatuses.get(0).getType());
    }

}
