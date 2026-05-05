package eu.europa.esig.dss.validation.process.qualification.eaa.checks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraint;
import eu.europa.esig.dss.detailedreport.jaxb.XmlStatus;
import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationProcessEAAPresentation;
import eu.europa.esig.dss.diagnostic.EAAPresentationWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAAPayload;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAAPresentation;
import eu.europa.esig.dss.enumerations.EAAPresentationType;
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

        XmlEAAPresentation xmlEAAPresentation = new XmlEAAPresentation();
        xmlEAAPresentation.setEAAType(EAAPresentationType.SD_JWT_VC);
        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();

        XmlClaim issuance = new XmlClaim();
        issuance.setDateTime(new Date());
        xmlEAAPayload.setAdministrativeIssuanceDate(issuance);
        xmlEAAPresentation.setEAAPayload(xmlEAAPayload);

        XmlValidationProcessEAAPresentation result = new XmlValidationProcessEAAPresentation();

        EAAAdministrativeIssuanceDatePresentCheck administrativeIssuanceDatePresentCheck = new EAAAdministrativeIssuanceDatePresentCheck(
                i18nProvider, result, new EAAPresentationWrapper(xmlEAAPresentation), new LevelConstraintWrapper(constraint));
        administrativeIssuanceDatePresentCheck.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

    @Test
    void invalidTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAAPresentation xmlEAAPresentation = new XmlEAAPresentation();
        xmlEAAPresentation.setEAAType(EAAPresentationType.SD_JWT_VC);
        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();
        xmlEAAPresentation.setEAAPayload(xmlEAAPayload);

        XmlValidationProcessEAAPresentation result = new XmlValidationProcessEAAPresentation();

        EAAAdministrativeIssuanceDatePresentCheck administrativeIssuanceDatePresentCheck = new EAAAdministrativeIssuanceDatePresentCheck(
                i18nProvider, result, new EAAPresentationWrapper(xmlEAAPresentation), new LevelConstraintWrapper(constraint));
        administrativeIssuanceDatePresentCheck.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }
}
