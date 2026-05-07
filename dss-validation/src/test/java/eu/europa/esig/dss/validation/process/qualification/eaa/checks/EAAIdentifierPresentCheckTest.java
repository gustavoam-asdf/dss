package eu.europa.esig.dss.validation.process.qualification.eaa.checks;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
import eu.europa.esig.dss.validation.process.eaa.checks.EAAIdentifierPresentCheck;

class EAAIdentifierPresentCheckTest extends AbstractTestCheck {

    @Test
    void sdjwtValidTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAA xmlEAAPresentation = new XmlEAA();
        xmlEAAPresentation.setEAAType(EAAType.SD_JWT_VC);
        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();

        XmlClaim identifier = new XmlClaim();
        identifier.setText("id");
        xmlEAAPayload.setIdentifier(identifier);
        xmlEAAPresentation.setEAAPayload(xmlEAAPayload);

        XmlValidationProcessEAA result = new XmlValidationProcessEAA();

        EAAIdentifierPresentCheck identifierPresentCheck = new EAAIdentifierPresentCheck(
                i18nProvider, result, new EAAWrapper(xmlEAAPresentation), new LevelConstraintWrapper(constraint));
        identifierPresentCheck.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

    @Test
    void mdocValidTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAA xmlEAAPresentation = new XmlEAA();
        xmlEAAPresentation.setEAAType(EAAType.ISO_IEC_MDOC);
        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();

        XmlClaim identifier = new XmlClaim();
        identifier.setText("id");
        xmlEAAPayload.setDocumentNumber(identifier);
        xmlEAAPresentation.setEAAPayload(xmlEAAPayload);

        XmlValidationProcessEAA result = new XmlValidationProcessEAA();

        EAAIdentifierPresentCheck identifierPresentCheck = new EAAIdentifierPresentCheck(
                i18nProvider, result, new EAAWrapper(xmlEAAPresentation), new LevelConstraintWrapper(constraint));
        identifierPresentCheck.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

    @Test
    void sdjwtInvalidTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAA xmlEAAPresentation = new XmlEAA();
        xmlEAAPresentation.setEAAType(EAAType.SD_JWT_VC);
        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();
        xmlEAAPresentation.setEAAPayload(xmlEAAPayload);

        XmlValidationProcessEAA result = new XmlValidationProcessEAA();

        EAAIdentifierPresentCheck identifierPresentCheck = new EAAIdentifierPresentCheck(
                i18nProvider, result, new EAAWrapper(xmlEAAPresentation), new LevelConstraintWrapper(constraint));
        identifierPresentCheck.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

    @Test
    void mdocInvalidTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAA xmlEAAPresentation = new XmlEAA();
        xmlEAAPresentation.setEAAType(EAAType.ISO_IEC_MDOC);
        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();
        xmlEAAPresentation.setEAAPayload(xmlEAAPayload);

        XmlValidationProcessEAA result = new XmlValidationProcessEAA();

        EAAIdentifierPresentCheck identifierPresentCheck = new EAAIdentifierPresentCheck(
                i18nProvider, result, new EAAWrapper(xmlEAAPresentation), new LevelConstraintWrapper(constraint));
        identifierPresentCheck.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }
}
