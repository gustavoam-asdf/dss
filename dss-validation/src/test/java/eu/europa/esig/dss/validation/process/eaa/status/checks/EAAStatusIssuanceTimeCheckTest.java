package eu.europa.esig.dss.validation.process.eaa.status.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraint;
import eu.europa.esig.dss.detailedreport.jaxb.XmlSAV;
import eu.europa.esig.dss.detailedreport.jaxb.XmlStatus;
import eu.europa.esig.dss.diagnostic.EAAStatusTokenWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAAStatusToken;
import eu.europa.esig.dss.enumerations.Level;
import eu.europa.esig.dss.policy.LevelConstraintWrapper;
import eu.europa.esig.dss.policy.jaxb.LevelConstraint;
import eu.europa.esig.dss.validation.process.bbb.AbstractTestCheck;
import eu.europa.esig.dss.validation.process.eaa.status.EAAStatusIssuanceTimeCheck;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EAAStatusIssuanceTimeCheckTest extends AbstractTestCheck {

    @Test
    void validTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAAStatusToken xmlEAAStatusToken = new XmlEAAStatusToken();
        xmlEAAStatusToken.setIssuedAt(new Date());
        XmlSAV result = new XmlSAV();

        EAAStatusIssuanceTimeCheck eaasitc = new EAAStatusIssuanceTimeCheck(
                i18nProvider, result, new EAAStatusTokenWrapper(xmlEAAStatusToken), new LevelConstraintWrapper(constraint));
        eaasitc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

    @Test
    void invalidTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAAStatusToken xmlEAAStatusToken = new XmlEAAStatusToken();
        XmlSAV result = new XmlSAV();

        EAAStatusIssuanceTimeCheck eaasitc = new EAAStatusIssuanceTimeCheck(
                i18nProvider, result, new EAAStatusTokenWrapper(xmlEAAStatusToken), new LevelConstraintWrapper(constraint));
        eaasitc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

}
