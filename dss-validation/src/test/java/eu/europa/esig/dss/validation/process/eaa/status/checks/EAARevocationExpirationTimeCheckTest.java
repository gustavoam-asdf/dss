package eu.europa.esig.dss.validation.process.eaa.status.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraint;
import eu.europa.esig.dss.detailedreport.jaxb.XmlSAV;
import eu.europa.esig.dss.detailedreport.jaxb.XmlStatus;
import eu.europa.esig.dss.diagnostic.EAARevocationTokenWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAARevocationToken;
import eu.europa.esig.dss.enumerations.Level;
import eu.europa.esig.dss.policy.LevelConstraintWrapper;
import eu.europa.esig.dss.policy.jaxb.LevelConstraint;
import eu.europa.esig.dss.validation.process.bbb.AbstractTestCheck;
import eu.europa.esig.dss.validation.process.eaa.status.EAARevocationExpirationTimeCheck;
import eu.europa.esig.dss.validation.process.eaa.status.EAARevocationIssuanceTimeCheck;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EAARevocationExpirationTimeCheckTest extends AbstractTestCheck {

    @Test
    void validTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAARevocationToken xmlEAARevocationToken = new XmlEAARevocationToken();
        xmlEAARevocationToken.setExpirationTime(new Date());
        XmlSAV result = new XmlSAV();

        EAARevocationExpirationTimeCheck eaasetc = new EAARevocationExpirationTimeCheck(
                i18nProvider, result, new EAARevocationTokenWrapper(xmlEAARevocationToken), new LevelConstraintWrapper(constraint));
        eaasetc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

    @Test
    void invalidTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAARevocationToken xmlEAARevocationToken = new XmlEAARevocationToken();
        XmlSAV result = new XmlSAV();

        EAARevocationIssuanceTimeCheck eaasitc = new EAARevocationIssuanceTimeCheck(
                i18nProvider, result, new EAARevocationTokenWrapper(xmlEAARevocationToken), new LevelConstraintWrapper(constraint));
        eaasitc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

}
