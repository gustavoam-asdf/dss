package eu.europa.esig.dss.validation.process.qualification.eaa.checks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraint;
import eu.europa.esig.dss.detailedreport.jaxb.XmlStatus;
import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationProcessEAAPresentation;
import eu.europa.esig.dss.diagnostic.EAAPresentationWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAAPayload;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAAPresentation;
import eu.europa.esig.dss.diagnostic.jaxb.XmlMetadataTypeClaim;
import eu.europa.esig.dss.enumerations.EAAPresentationType;
import eu.europa.esig.dss.enumerations.Level;
import eu.europa.esig.dss.policy.MultiValuesConstraintWrapper;
import eu.europa.esig.dss.policy.jaxb.MultiValuesConstraint;
import eu.europa.esig.dss.validation.process.bbb.AbstractTestCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.AcceptableEAATypeCheck;

class AcceptableEAATypeCheckTest extends AbstractTestCheck {

    @Test
    void sdjwtValidTest(){
        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.getId().add("urn:eudi:pid:1");
        constraint.setLevel(Level.FAIL);

        XmlEAAPresentation xmlEAAPresentation = new XmlEAAPresentation();
        xmlEAAPresentation.setEAAType(EAAPresentationType.SD_JWT_VC);
        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();
        XmlMetadataTypeClaim xmlMetadataTypeClaim = new XmlMetadataTypeClaim();
        xmlMetadataTypeClaim.setText("urn:eudi:pid:1");
        xmlEAAPayload.setMetadataType(xmlMetadataTypeClaim);
        xmlEAAPresentation.setEAAPayload(xmlEAAPayload);

        XmlValidationProcessEAAPresentation result = new XmlValidationProcessEAAPresentation();

        AcceptableEAATypeCheck typePresentCheck = new AcceptableEAATypeCheck(
                i18nProvider, result, new EAAPresentationWrapper(xmlEAAPresentation), new MultiValuesConstraintWrapper(constraint));
        typePresentCheck.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

    @Test
    void sdjwtValidAllValuesTest(){
        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.getId().add("*");
        constraint.setLevel(Level.FAIL);

        XmlEAAPresentation xmlEAAPresentation = new XmlEAAPresentation();
        xmlEAAPresentation.setEAAType(EAAPresentationType.SD_JWT_VC);
        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();
        XmlMetadataTypeClaim xmlMetadataTypeClaim = new XmlMetadataTypeClaim();
        xmlMetadataTypeClaim.setText("urn:eudi:pid:1");
        xmlEAAPayload.setMetadataType(xmlMetadataTypeClaim);
        xmlEAAPresentation.setEAAPayload(xmlEAAPayload);

        XmlValidationProcessEAAPresentation result = new XmlValidationProcessEAAPresentation();

        AcceptableEAATypeCheck typePresentCheck = new AcceptableEAATypeCheck(
                i18nProvider, result, new EAAPresentationWrapper(xmlEAAPresentation), new MultiValuesConstraintWrapper(constraint));
        typePresentCheck.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

    @Test
    void mdocValidTest(){
        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.getId().add("urn:eudi:pid:1");
        constraint.setLevel(Level.FAIL);

        XmlEAAPresentation xmlEAAPresentation = new XmlEAAPresentation();
        xmlEAAPresentation.setEAAType(EAAPresentationType.ISO_IEC_MDOC);
        xmlEAAPresentation.setDocumentType("urn:eudi:pid:1");

        XmlValidationProcessEAAPresentation result = new XmlValidationProcessEAAPresentation();

        AcceptableEAATypeCheck typePresentCheck = new AcceptableEAATypeCheck(
                i18nProvider, result, new EAAPresentationWrapper(xmlEAAPresentation), new MultiValuesConstraintWrapper(constraint));
        typePresentCheck.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

    @Test
    void mdocValidAllValuesTest(){
        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.getId().add("*");
        constraint.setLevel(Level.FAIL);

        XmlEAAPresentation xmlEAAPresentation = new XmlEAAPresentation();
        xmlEAAPresentation.setEAAType(EAAPresentationType.ISO_IEC_MDOC);
        xmlEAAPresentation.setDocumentType("urn:eudi:pid:1");

        XmlValidationProcessEAAPresentation result = new XmlValidationProcessEAAPresentation();

        AcceptableEAATypeCheck typePresentCheck = new AcceptableEAATypeCheck(
                i18nProvider, result, new EAAPresentationWrapper(xmlEAAPresentation), new MultiValuesConstraintWrapper(constraint));
        typePresentCheck.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

    @Test
    void sdjwtInvalidTest(){
        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.getId().add("urn:eudi:pid:2");
        constraint.setLevel(Level.FAIL);

        XmlEAAPresentation xmlEAAPresentation = new XmlEAAPresentation();
        xmlEAAPresentation.setEAAType(EAAPresentationType.SD_JWT_VC);
        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();
        XmlMetadataTypeClaim xmlMetadataTypeClaim = new XmlMetadataTypeClaim();
        xmlMetadataTypeClaim.setText("urn:eudi:pid:1");
        xmlEAAPayload.setMetadataType(xmlMetadataTypeClaim);
        xmlEAAPresentation.setEAAPayload(xmlEAAPayload);

        XmlValidationProcessEAAPresentation result = new XmlValidationProcessEAAPresentation();

        AcceptableEAATypeCheck typePresentCheck = new AcceptableEAATypeCheck(
                i18nProvider, result, new EAAPresentationWrapper(xmlEAAPresentation), new MultiValuesConstraintWrapper(constraint));
        typePresentCheck.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

    @Test
    void mdocInvalidTest(){
        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.getId().add("urn:eudi:pid:2");
        constraint.setLevel(Level.FAIL);

        XmlEAAPresentation xmlEAAPresentation = new XmlEAAPresentation();
        xmlEAAPresentation.setEAAType(EAAPresentationType.ISO_IEC_MDOC);
        xmlEAAPresentation.setDocumentType("urn:eudi:pid:1");

        XmlValidationProcessEAAPresentation result = new XmlValidationProcessEAAPresentation();

        AcceptableEAATypeCheck typePresentCheck = new AcceptableEAATypeCheck(
                i18nProvider, result, new EAAPresentationWrapper(xmlEAAPresentation), new MultiValuesConstraintWrapper(constraint));
        typePresentCheck.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

    @Test
    void sdjwtNotPresentTest(){
        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.getId().add("*");
        constraint.setLevel(Level.FAIL);

        XmlEAAPresentation xmlEAAPresentation = new XmlEAAPresentation();
        xmlEAAPresentation.setEAAType(EAAPresentationType.SD_JWT_VC);

        XmlValidationProcessEAAPresentation result = new XmlValidationProcessEAAPresentation();

        AcceptableEAATypeCheck typePresentCheck = new AcceptableEAATypeCheck(
                i18nProvider, result, new EAAPresentationWrapper(xmlEAAPresentation), new MultiValuesConstraintWrapper(constraint));
        typePresentCheck.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

    @Test
    void mdocNotPresentTest(){
        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.getId().add("*");
        constraint.setLevel(Level.FAIL);

        XmlEAAPresentation xmlEAAPresentation = new XmlEAAPresentation();
        xmlEAAPresentation.setEAAType(EAAPresentationType.ISO_IEC_MDOC);

        XmlValidationProcessEAAPresentation result = new XmlValidationProcessEAAPresentation();

        AcceptableEAATypeCheck typePresentCheck = new AcceptableEAATypeCheck(
                i18nProvider, result, new EAAPresentationWrapper(xmlEAAPresentation), new MultiValuesConstraintWrapper(constraint));
        typePresentCheck.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }
}
