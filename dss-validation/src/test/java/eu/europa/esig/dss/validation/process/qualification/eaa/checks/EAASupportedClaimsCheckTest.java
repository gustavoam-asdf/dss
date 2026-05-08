package eu.europa.esig.dss.validation.process.qualification.eaa.checks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraint;
import eu.europa.esig.dss.detailedreport.jaxb.XmlStatus;
import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationProcessEAA;
import eu.europa.esig.dss.diagnostic.EAAWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAA;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAAPayload;
import eu.europa.esig.dss.diagnostic.jaxb.XmlMetadataTypeClaim;
import eu.europa.esig.dss.enumerations.EAAType;
import eu.europa.esig.dss.enumerations.Level;
import eu.europa.esig.dss.policy.MultiValuesConstraintWrapper;
import eu.europa.esig.dss.policy.jaxb.MultiValuesConstraint;
import eu.europa.esig.dss.validation.process.bbb.AbstractTestCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.EAASupportedClaimsCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.EAATypeCheck;

class EAASupportedClaimsCheckTest extends AbstractTestCheck {

    @Test
    void validTest() {
        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.getId().add("metadata");
        constraint.setLevel(Level.FAIL);

        XmlEAA xmlEAAPresentation = new XmlEAA();
        xmlEAAPresentation.setEAAType(EAAType.SD_JWT_VC);
        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();
        XmlMetadataTypeClaim xmlMetadataTypeClaim = new XmlMetadataTypeClaim();
        xmlMetadataTypeClaim.setText("urn:eudi:pid:1");
        xmlMetadataTypeClaim.setName("metadata");
        xmlEAAPayload.setMetadataType(xmlMetadataTypeClaim);
        xmlEAAPresentation.setEAAPayload(xmlEAAPayload);

        XmlValidationProcessEAA result = new XmlValidationProcessEAA();

        EAASupportedClaimsCheck supportedClaimsCheck = new EAASupportedClaimsCheck(
                i18nProvider, result, new EAAWrapper(xmlEAAPresentation), new MultiValuesConstraintWrapper(constraint));
        supportedClaimsCheck.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

    @Test
    void invalidTest() {
        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.getId().add("metadata");
        constraint.setLevel(Level.FAIL);

        XmlEAA xmlEAAPresentation = new XmlEAA();
        xmlEAAPresentation.setEAAType(EAAType.SD_JWT_VC);
        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();
        XmlMetadataTypeClaim xmlMetadataTypeClaim = new XmlMetadataTypeClaim();
        xmlMetadataTypeClaim.setText("urn:eudi:pid:1");
        xmlMetadataTypeClaim.setName("metadata-wrong");
        xmlEAAPayload.setMetadataType(xmlMetadataTypeClaim);
        xmlEAAPresentation.setEAAPayload(xmlEAAPayload);

        XmlValidationProcessEAA result = new XmlValidationProcessEAA();

        EAATypeCheck typePresentCheck = new EAATypeCheck(
                i18nProvider, result, new EAAWrapper(xmlEAAPresentation), new MultiValuesConstraintWrapper(constraint));
        typePresentCheck.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }
}
