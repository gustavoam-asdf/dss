package eu.europa.esig.dss.validation.process.eaa.status.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraint;
import eu.europa.esig.dss.detailedreport.jaxb.XmlFC;
import eu.europa.esig.dss.detailedreport.jaxb.XmlStatus;
import eu.europa.esig.dss.diagnostic.EAARevocationTokenWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAARevocationToken;
import eu.europa.esig.dss.enumerations.Level;
import eu.europa.esig.dss.policy.MultiValuesConstraintWrapper;
import eu.europa.esig.dss.policy.jaxb.MultiValuesConstraint;
import eu.europa.esig.dss.validation.process.bbb.AbstractTestCheck;
import eu.europa.esig.dss.validation.process.eaa.status.EAARevocationTokenTypeCheck;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EAARevocationTokenTypeCheckTest extends AbstractTestCheck {

    @Test
    void validTest() {
        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.getId().add("statuslist+jwt");
        constraint.setLevel(Level.FAIL);

        XmlEAARevocationToken xmlEAARevocationToken = new XmlEAARevocationToken();
        xmlEAARevocationToken.setType("statuslist+jwt");
        XmlFC result = new XmlFC();

        EAARevocationTokenTypeCheck eaattc = new EAARevocationTokenTypeCheck(
                i18nProvider, result, new EAARevocationTokenWrapper(xmlEAARevocationToken), new MultiValuesConstraintWrapper(constraint));
        eaattc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

    @Test
    void invalidTest() {
        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.getId().add("statuslist+jwt");
        constraint.setLevel(Level.FAIL);

        XmlEAARevocationToken xmlEAARevocationToken = new XmlEAARevocationToken();
        xmlEAARevocationToken.setType("statuslist+cwt");
        XmlFC result = new XmlFC();

        EAARevocationTokenTypeCheck eaattc = new EAARevocationTokenTypeCheck(
                i18nProvider, result, new EAARevocationTokenWrapper(xmlEAARevocationToken), new MultiValuesConstraintWrapper(constraint));
        eaattc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

    @Test
    void allTest() {
        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.getId().add("*");
        constraint.setLevel(Level.FAIL);

        XmlEAARevocationToken xmlEAARevocationToken = new XmlEAARevocationToken();
        xmlEAARevocationToken.setType("statuslist+jwt");
        XmlFC result = new XmlFC();

        EAARevocationTokenTypeCheck eaattc = new EAARevocationTokenTypeCheck(
                i18nProvider, result, new EAARevocationTokenWrapper(xmlEAARevocationToken), new MultiValuesConstraintWrapper(constraint));
        eaattc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

    @Test
    void notPresentTest() {
        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.getId().add("*");
        constraint.setLevel(Level.FAIL);

        XmlEAARevocationToken xmlEAARevocationToken = new XmlEAARevocationToken();
        XmlFC result = new XmlFC();

        EAARevocationTokenTypeCheck eaattc = new EAARevocationTokenTypeCheck(
                i18nProvider, result, new EAARevocationTokenWrapper(xmlEAARevocationToken), new MultiValuesConstraintWrapper(constraint));
        eaattc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

}
