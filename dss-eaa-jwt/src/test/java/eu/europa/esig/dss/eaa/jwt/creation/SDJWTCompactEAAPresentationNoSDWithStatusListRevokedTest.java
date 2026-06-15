package eu.europa.esig.dss.eaa.jwt.creation;

import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.EAARevocationWrapper;
import eu.europa.esig.dss.diagnostic.EAAWrapper;
import eu.europa.esig.dss.eaa.jwt.pki.PKIJWTStatusListSource;
import eu.europa.esig.dss.enumerations.EAAStatus;
import eu.europa.esig.dss.spi.eaa.status.EAARevocationSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SDJWTCompactEAAPresentationNoSDWithStatusListRevokedTest extends SDJWTCompactEAAPresentationNoSDWithStatusListTest {

    @Override
    protected EAARevocationSource getEAAStatusSource() {
        PKIJWTStatusListSource statusListSource = new PKIJWTStatusListSource(getCertEntityRepository(), getCertEntity(GOOD_CA));
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
        assertEquals("application/statuslist+jwt", eaaStatuses.get(0).getType());
    }

}
