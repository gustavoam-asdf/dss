package eu.europa.esig.dss.validation.process.eaa.status.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraint;
import eu.europa.esig.dss.detailedreport.jaxb.XmlSAV;
import eu.europa.esig.dss.detailedreport.jaxb.XmlStatus;
import eu.europa.esig.dss.diagnostic.EAAStatusTokenWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAAStatusToken;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAASubject;
import eu.europa.esig.dss.enumerations.Level;
import eu.europa.esig.dss.policy.MultiValuesConstraintWrapper;
import eu.europa.esig.dss.policy.jaxb.MultiValuesConstraint;
import eu.europa.esig.dss.validation.process.bbb.AbstractTestCheck;
import eu.europa.esig.dss.validation.process.eaa.status.EAAStatusSubjectCheck;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EAAStatusSubjectCheckTest extends AbstractTestCheck {

    @Test
    void validTest() {
        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.getId().add("https://dss.nowina.lu/pki-factory/eaa/status");
        constraint.setLevel(Level.FAIL);

        XmlEAAStatusToken xmlEAAStatusToken = new XmlEAAStatusToken();
        XmlEAASubject xmlEAASubject = new XmlEAASubject();
        xmlEAASubject.setValue("https://dss.nowina.lu/pki-factory/eaa/status");
        xmlEAAStatusToken.setSubject(xmlEAASubject);
        XmlSAV result = new XmlSAV();

        EAAStatusSubjectCheck eaassc = new EAAStatusSubjectCheck(
                i18nProvider, result, new EAAStatusTokenWrapper(xmlEAAStatusToken), new MultiValuesConstraintWrapper(constraint));
        eaassc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

    @Test
    void invalidTest() {
        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.getId().add("https://dss.nowina.lu/pki-factory/eaa/status");
        constraint.setLevel(Level.FAIL);

        XmlEAAStatusToken xmlEAAStatusToken = new XmlEAAStatusToken();
        XmlEAASubject xmlEAASubject = new XmlEAASubject();
        xmlEAASubject.setValue("https://dss.nowina.lu/pki-factory/neaa/status");
        xmlEAAStatusToken.setSubject(xmlEAASubject);
        XmlSAV result = new XmlSAV();

        EAAStatusSubjectCheck eaassc = new EAAStatusSubjectCheck(
                i18nProvider, result, new EAAStatusTokenWrapper(xmlEAAStatusToken), new MultiValuesConstraintWrapper(constraint));
        eaassc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

    @Test
    void allTest() {
        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.getId().add("*");
        constraint.setLevel(Level.FAIL);

        XmlEAAStatusToken xmlEAAStatusToken = new XmlEAAStatusToken();
        XmlEAASubject xmlEAASubject = new XmlEAASubject();
        xmlEAASubject.setValue("https://dss.nowina.lu/pki-factory/eaa/status");
        xmlEAAStatusToken.setSubject(xmlEAASubject);
        XmlSAV result = new XmlSAV();

        EAAStatusSubjectCheck eaassc = new EAAStatusSubjectCheck(
                i18nProvider, result, new EAAStatusTokenWrapper(xmlEAAStatusToken), new MultiValuesConstraintWrapper(constraint));
        eaassc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

    @Test
    void notPresentTest() {
        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.getId().add("*");
        constraint.setLevel(Level.FAIL);

        XmlEAAStatusToken xmlEAAStatusToken = new XmlEAAStatusToken();
        XmlSAV result = new XmlSAV();

        EAAStatusSubjectCheck eaassc = new EAAStatusSubjectCheck(
                i18nProvider, result, new EAAStatusTokenWrapper(xmlEAAStatusToken), new MultiValuesConstraintWrapper(constraint));
        eaassc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

}
