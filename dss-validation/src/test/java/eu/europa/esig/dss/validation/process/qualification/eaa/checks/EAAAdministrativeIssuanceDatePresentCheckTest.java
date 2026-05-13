package eu.europa.esig.dss.validation.process.qualification.eaa.checks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraint;
import eu.europa.esig.dss.detailedreport.jaxb.XmlStatus;
import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationProcessEAA;
import eu.europa.esig.dss.diagnostic.EAAWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAAPayload;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAA;
import eu.europa.esig.dss.enumerations.EAAType;
import eu.europa.esig.dss.enumerations.Level;
import eu.europa.esig.dss.policy.LevelConstraintWrapper;
import eu.europa.esig.dss.policy.jaxb.LevelConstraint;
import eu.europa.esig.dss.validation.process.bbb.AbstractTestCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.EAAAdministrativeIssuanceDatePresentCheck;

class EAAAdministrativeIssuanceDatePresentCheckTest extends AbstractTestCheck {

    @Test
    void validTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAA xmlEAA = new XmlEAA();
        xmlEAA.setEAAType(EAAType.SD_JWT_VC);
        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();

        XmlClaim issuance = new XmlClaim();
        issuance.setDateTime(new Date());
        xmlEAAPayload.setAdministrativeIssuanceDate(issuance);
        xmlEAA.setEAAPayload(xmlEAAPayload);

        XmlValidationProcessEAA result = new XmlValidationProcessEAA();

        EAAAdministrativeIssuanceDatePresentCheck administrativeIssuanceDatePresentCheck = new EAAAdministrativeIssuanceDatePresentCheck(
                i18nProvider, result, new EAAWrapper(xmlEAA), new LevelConstraintWrapper(constraint));
        administrativeIssuanceDatePresentCheck.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

    @Test
    void invalidTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAA xmlEAA = new XmlEAA();
        xmlEAA.setEAAType(EAAType.SD_JWT_VC);
        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();
        xmlEAA.setEAAPayload(xmlEAAPayload);

        XmlValidationProcessEAA result = new XmlValidationProcessEAA();

        EAAAdministrativeIssuanceDatePresentCheck administrativeIssuanceDatePresentCheck = new EAAAdministrativeIssuanceDatePresentCheck(
                i18nProvider, result, new EAAWrapper(xmlEAA), new LevelConstraintWrapper(constraint));
        administrativeIssuanceDatePresentCheck.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }
}
