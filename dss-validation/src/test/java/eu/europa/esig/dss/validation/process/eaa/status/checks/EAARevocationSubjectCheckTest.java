package eu.europa.esig.dss.validation.process.eaa.status.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraint;
import eu.europa.esig.dss.detailedreport.jaxb.XmlSAV;
import eu.europa.esig.dss.detailedreport.jaxb.XmlStatus;
import eu.europa.esig.dss.diagnostic.EAARevocationTokenWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAARevocationToken;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAASubject;
import eu.europa.esig.dss.enumerations.Level;
import eu.europa.esig.dss.policy.MultiValuesConstraintWrapper;
import eu.europa.esig.dss.policy.jaxb.MultiValuesConstraint;
import eu.europa.esig.dss.validation.process.bbb.AbstractTestCheck;
import eu.europa.esig.dss.validation.process.eaa.status.EAARevocationSubjectCheck;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EAARevocationSubjectCheckTest extends AbstractTestCheck {

    @Test
    void validTest() {
        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.getId().add("https://dss.nowina.lu/pki-factory/eaa/status");
        constraint.setLevel(Level.FAIL);

        XmlEAARevocationToken xmlEAARevocationToken = new XmlEAARevocationToken();
        XmlEAASubject xmlEAASubject = new XmlEAASubject();
        xmlEAASubject.setValue("https://dss.nowina.lu/pki-factory/eaa/status");
        xmlEAARevocationToken.setSubject(xmlEAASubject);
        XmlSAV result = new XmlSAV();

        EAARevocationSubjectCheck eaassc = new EAARevocationSubjectCheck(
                i18nProvider, result, new EAARevocationTokenWrapper(xmlEAARevocationToken), new MultiValuesConstraintWrapper(constraint));
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

        XmlEAARevocationToken xmlEAARevocationToken = new XmlEAARevocationToken();
        XmlEAASubject xmlEAASubject = new XmlEAASubject();
        xmlEAASubject.setValue("https://dss.nowina.lu/pki-factory/neaa/status");
        xmlEAARevocationToken.setSubject(xmlEAASubject);
        XmlSAV result = new XmlSAV();

        EAARevocationSubjectCheck eaassc = new EAARevocationSubjectCheck(
                i18nProvider, result, new EAARevocationTokenWrapper(xmlEAARevocationToken), new MultiValuesConstraintWrapper(constraint));
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

        XmlEAARevocationToken xmlEAARevocationToken = new XmlEAARevocationToken();
        XmlEAASubject xmlEAASubject = new XmlEAASubject();
        xmlEAASubject.setValue("https://dss.nowina.lu/pki-factory/eaa/status");
        xmlEAARevocationToken.setSubject(xmlEAASubject);
        XmlSAV result = new XmlSAV();

        EAARevocationSubjectCheck eaassc = new EAARevocationSubjectCheck(
                i18nProvider, result, new EAARevocationTokenWrapper(xmlEAARevocationToken), new MultiValuesConstraintWrapper(constraint));
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

        XmlEAARevocationToken xmlEAARevocationToken = new XmlEAARevocationToken();
        XmlSAV result = new XmlSAV();

        EAARevocationSubjectCheck eaassc = new EAARevocationSubjectCheck(
                i18nProvider, result, new EAARevocationTokenWrapper(xmlEAARevocationToken), new MultiValuesConstraintWrapper(constraint));
        eaassc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

}
