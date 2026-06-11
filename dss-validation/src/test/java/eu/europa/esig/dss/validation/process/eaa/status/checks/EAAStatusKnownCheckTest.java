package eu.europa.esig.dss.validation.process.eaa.status.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraint;
import eu.europa.esig.dss.detailedreport.jaxb.XmlSAV;
import eu.europa.esig.dss.detailedreport.jaxb.XmlStatus;
import eu.europa.esig.dss.diagnostic.EAAStatusWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAAStatus;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAAStatusToken;
import eu.europa.esig.dss.enumerations.EAAStatus;
import eu.europa.esig.dss.enumerations.Level;
import eu.europa.esig.dss.policy.LevelConstraintWrapper;
import eu.europa.esig.dss.policy.jaxb.LevelConstraint;
import eu.europa.esig.dss.validation.process.bbb.AbstractTestCheck;
import eu.europa.esig.dss.validation.process.eaa.status.EAAStatusKnownCheck;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EAAStatusKnownCheckTest extends AbstractTestCheck {

    @Test
    void validTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAAStatus xmlEAAStatus = new XmlEAAStatus();
        xmlEAAStatus.setEAAStatusToken(new XmlEAAStatusToken());
        xmlEAAStatus.setStatus(EAAStatus.VALID);
        XmlSAV result = new XmlSAV();

        EAAStatusKnownCheck eaaskc = new EAAStatusKnownCheck(
                i18nProvider, result, new EAAStatusWrapper(xmlEAAStatus), new LevelConstraintWrapper(constraint));
        eaaskc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

    @Test
    void invalidTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAAStatus xmlEAAStatus = new XmlEAAStatus();
        xmlEAAStatus.setEAAStatusToken(new XmlEAAStatusToken());
        xmlEAAStatus.setStatus(EAAStatus.INVALID);
        XmlSAV result = new XmlSAV();

        EAAStatusKnownCheck eaaskc = new EAAStatusKnownCheck(
                i18nProvider, result, new EAAStatusWrapper(xmlEAAStatus), new LevelConstraintWrapper(constraint));
        eaaskc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

    @Test
    void suspendedTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAAStatus xmlEAAStatus = new XmlEAAStatus();
        xmlEAAStatus.setEAAStatusToken(new XmlEAAStatusToken());
        xmlEAAStatus.setStatus(EAAStatus.SUSPENDED);
        XmlSAV result = new XmlSAV();

        EAAStatusKnownCheck eaaskc = new EAAStatusKnownCheck(
                i18nProvider, result, new EAAStatusWrapper(xmlEAAStatus), new LevelConstraintWrapper(constraint));
        eaaskc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

    @Test
    void unknownTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAAStatus xmlEAAStatus = new XmlEAAStatus();
        xmlEAAStatus.setEAAStatusToken(new XmlEAAStatusToken());
        xmlEAAStatus.setStatus(EAAStatus.UNKNOWN);
        XmlSAV result = new XmlSAV();

        EAAStatusKnownCheck eaaskc = new EAAStatusKnownCheck(
                i18nProvider, result, new EAAStatusWrapper(xmlEAAStatus), new LevelConstraintWrapper(constraint));
        eaaskc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

}
