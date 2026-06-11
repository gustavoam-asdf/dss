package eu.europa.esig.dss.validation.process.eaa.status.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraint;
import eu.europa.esig.dss.detailedreport.jaxb.XmlSAV;
import eu.europa.esig.dss.detailedreport.jaxb.XmlStatus;
import eu.europa.esig.dss.diagnostic.EAAStatusTokenWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAAStatusToken;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAASubject;
import eu.europa.esig.dss.enumerations.Level;
import eu.europa.esig.dss.policy.LevelConstraintWrapper;
import eu.europa.esig.dss.policy.jaxb.LevelConstraint;
import eu.europa.esig.dss.validation.process.bbb.AbstractTestCheck;
import eu.europa.esig.dss.validation.process.eaa.status.EAAStatusSubjectMatchCheck;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EAAStatusSubjectMatchCheckTest extends AbstractTestCheck {

    @Test
    void validTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAAStatusToken xmlEAAStatusToken = new XmlEAAStatusToken();
        XmlEAASubject xmlEAASubject = new XmlEAASubject();
        xmlEAASubject.setMatch(true);
        xmlEAAStatusToken.setSubject(xmlEAASubject);
        XmlSAV result = new XmlSAV();

        EAAStatusSubjectMatchCheck eaassmc = new EAAStatusSubjectMatchCheck(
                i18nProvider, result, new EAAStatusTokenWrapper(xmlEAAStatusToken), new LevelConstraintWrapper(constraint));
        eaassmc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

    @Test
    void invalidTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAAStatusToken xmlEAAStatusToken = new XmlEAAStatusToken();
        XmlEAASubject xmlEAASubject = new XmlEAASubject();
        xmlEAASubject.setMatch(false);
        xmlEAAStatusToken.setSubject(xmlEAASubject);
        XmlSAV result = new XmlSAV();

        EAAStatusSubjectMatchCheck eaassmc = new EAAStatusSubjectMatchCheck(
                i18nProvider, result, new EAAStatusTokenWrapper(xmlEAAStatusToken), new LevelConstraintWrapper(constraint));
        eaassmc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

    @Test
    void notPresentTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAAStatusToken xmlEAAStatusToken = new XmlEAAStatusToken();
        XmlSAV result = new XmlSAV();

        EAAStatusSubjectMatchCheck eaassmc = new EAAStatusSubjectMatchCheck(
                i18nProvider, result, new EAAStatusTokenWrapper(xmlEAAStatusToken), new LevelConstraintWrapper(constraint));
        eaassmc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

}
