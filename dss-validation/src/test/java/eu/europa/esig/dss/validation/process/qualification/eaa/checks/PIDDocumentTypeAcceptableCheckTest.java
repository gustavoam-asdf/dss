package eu.europa.esig.dss.validation.process.qualification.eaa.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraint;
import eu.europa.esig.dss.detailedreport.jaxb.XmlStatus;
import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationPIDQualificationProcess;
import eu.europa.esig.dss.diagnostic.EAAPresentationWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAAPayload;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAAPresentation;
import eu.europa.esig.dss.diagnostic.jaxb.XmlMetadataTypeClaim;
import eu.europa.esig.dss.enumerations.EAAPresentationType;
import eu.europa.esig.dss.enumerations.Level;
import eu.europa.esig.dss.policy.LevelConstraintWrapper;
import eu.europa.esig.dss.policy.jaxb.LevelConstraint;
import eu.europa.esig.dss.validation.process.bbb.AbstractTestCheck;
import eu.europa.esig.dss.validation.process.qualification.eaa.pid.checks.PIDDocumentTypeAcceptableCheck;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PIDDocumentTypeAcceptableCheckTest extends AbstractTestCheck {

    @Test
    void sdJwtPidTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAAPresentation xmlEAAPresentation = new XmlEAAPresentation();
        xmlEAAPresentation.setEAAType(EAAPresentationType.SD_JWT_VC);
        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();
        XmlMetadataTypeClaim xmlMetadataTypeClaim = new XmlMetadataTypeClaim();
        xmlMetadataTypeClaim.setText("urn:eudi:pid:1");
        xmlEAAPayload.setMetadataType(xmlMetadataTypeClaim);
        xmlEAAPresentation.setEAAPayload(xmlEAAPayload);

        XmlValidationPIDQualificationProcess result = new XmlValidationPIDQualificationProcess();

        PIDDocumentTypeAcceptableCheck tlscbpsc = new PIDDocumentTypeAcceptableCheck(
                i18nProvider, result, new EAAPresentationWrapper(xmlEAAPresentation), new LevelConstraintWrapper(constraint));
        tlscbpsc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

    @Test
    void sdJwtPidDeTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAAPresentation xmlEAAPresentation = new XmlEAAPresentation();
        xmlEAAPresentation.setEAAType(EAAPresentationType.SD_JWT_VC);
        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();
        XmlMetadataTypeClaim xmlMetadataTypeClaim = new XmlMetadataTypeClaim();
        xmlMetadataTypeClaim.setText("urn:eudi:pid:1:de");
        xmlEAAPayload.setMetadataType(xmlMetadataTypeClaim);
        xmlEAAPresentation.setEAAPayload(xmlEAAPayload);

        XmlValidationPIDQualificationProcess result = new XmlValidationPIDQualificationProcess();

        PIDDocumentTypeAcceptableCheck tlscbpsc = new PIDDocumentTypeAcceptableCheck(
                i18nProvider, result, new EAAPresentationWrapper(xmlEAAPresentation), new LevelConstraintWrapper(constraint));
        tlscbpsc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

    @Test
    void sdJwtPidNotEuTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAAPresentation xmlEAAPresentation = new XmlEAAPresentation();
        xmlEAAPresentation.setEAAType(EAAPresentationType.SD_JWT_VC);
        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();
        XmlMetadataTypeClaim xmlMetadataTypeClaim = new XmlMetadataTypeClaim();
        xmlMetadataTypeClaim.setText("urn:zzdi:pid:1");
        xmlEAAPayload.setMetadataType(xmlMetadataTypeClaim);
        xmlEAAPresentation.setEAAPayload(xmlEAAPayload);

        XmlValidationPIDQualificationProcess result = new XmlValidationPIDQualificationProcess();

        PIDDocumentTypeAcceptableCheck tlscbpsc = new PIDDocumentTypeAcceptableCheck(
                i18nProvider, result, new EAAPresentationWrapper(xmlEAAPresentation), new LevelConstraintWrapper(constraint));
        tlscbpsc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

    @Test
    void sdJwtPidDocTypeTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAAPresentation xmlEAAPresentation = new XmlEAAPresentation();
        xmlEAAPresentation.setEAAType(EAAPresentationType.SD_JWT_VC);
        xmlEAAPresentation.setDocumentType("urn:eudi:pid:1");

        XmlValidationPIDQualificationProcess result = new XmlValidationPIDQualificationProcess();

        PIDDocumentTypeAcceptableCheck tlscbpsc = new PIDDocumentTypeAcceptableCheck(
                i18nProvider, result, new EAAPresentationWrapper(xmlEAAPresentation), new LevelConstraintWrapper(constraint));
        tlscbpsc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

    @Test
    void mdocPidTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAAPresentation xmlEAAPresentation = new XmlEAAPresentation();
        xmlEAAPresentation.setEAAType(EAAPresentationType.ISO_IEC_MDOC);
        xmlEAAPresentation.setDocumentType("eu.europa.ec.eudi.pid.1");

        XmlValidationPIDQualificationProcess result = new XmlValidationPIDQualificationProcess();

        PIDDocumentTypeAcceptableCheck tlscbpsc = new PIDDocumentTypeAcceptableCheck(
                i18nProvider, result, new EAAPresentationWrapper(xmlEAAPresentation), new LevelConstraintWrapper(constraint));
        tlscbpsc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

    @Test
    void mdocPidWrongTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAAPresentation xmlEAAPresentation = new XmlEAAPresentation();
        xmlEAAPresentation.setEAAType(EAAPresentationType.ISO_IEC_MDOC);
        xmlEAAPresentation.setDocumentType("eu.europa.ec.eudi.pid.1.de");

        XmlValidationPIDQualificationProcess result = new XmlValidationPIDQualificationProcess();

        PIDDocumentTypeAcceptableCheck tlscbpsc = new PIDDocumentTypeAcceptableCheck(
                i18nProvider, result, new EAAPresentationWrapper(xmlEAAPresentation), new LevelConstraintWrapper(constraint));
        tlscbpsc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

    @Test
    void mdocPidMetadataClaimTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAAPresentation xmlEAAPresentation = new XmlEAAPresentation();
        xmlEAAPresentation.setEAAType(EAAPresentationType.ISO_IEC_MDOC);
        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();
        XmlMetadataTypeClaim xmlMetadataTypeClaim = new XmlMetadataTypeClaim();
        xmlMetadataTypeClaim.setText("eu.europa.ec.eudi.pid.1");
        xmlEAAPayload.setMetadataType(xmlMetadataTypeClaim);
        xmlEAAPresentation.setEAAPayload(xmlEAAPayload);

        XmlValidationPIDQualificationProcess result = new XmlValidationPIDQualificationProcess();

        PIDDocumentTypeAcceptableCheck tlscbpsc = new PIDDocumentTypeAcceptableCheck(
                i18nProvider, result, new EAAPresentationWrapper(xmlEAAPresentation), new LevelConstraintWrapper(constraint));
        tlscbpsc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

}
