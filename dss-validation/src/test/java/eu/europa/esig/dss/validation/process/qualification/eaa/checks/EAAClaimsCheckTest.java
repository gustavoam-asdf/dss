package eu.europa.esig.dss.validation.process.qualification.eaa.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraint;
import eu.europa.esig.dss.detailedreport.jaxb.XmlStatus;
import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationProcessEAA;
import eu.europa.esig.dss.diagnostic.EAAWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAA;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAAPayload;
import eu.europa.esig.dss.diagnostic.jaxb.XmlMetadataTypeClaim;
import eu.europa.esig.dss.enumerations.EAAType;
import eu.europa.esig.dss.enumerations.Level;
import eu.europa.esig.dss.policy.MultiValuesConstraintWrapper;
import eu.europa.esig.dss.policy.jaxb.MultiValuesConstraint;
import eu.europa.esig.dss.validation.process.bbb.AbstractTestCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.EAAClaimsCheck;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EAAClaimsCheckTest extends AbstractTestCheck {

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

        EAAClaimsCheck eaacc = new EAAClaimsCheck(
                i18nProvider, result, new EAAWrapper(xmlEAAPresentation), new MultiValuesConstraintWrapper(constraint));
        eaacc.execute();

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

        EAAClaimsCheck eaacc = new EAAClaimsCheck(
                i18nProvider, result, new EAAWrapper(xmlEAAPresentation), new MultiValuesConstraintWrapper(constraint));
        eaacc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

    @Test
    void additionalClaimTest() {
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

        XmlClaim xmlClaim = new XmlClaim();
        xmlClaim.setName("additional");
        xmlEAAPayload.getOtherClaim().add(xmlClaim);

        xmlEAAPresentation.setEAAPayload(xmlEAAPayload);

        XmlValidationProcessEAA result = new XmlValidationProcessEAA();

        EAAClaimsCheck eaacc = new EAAClaimsCheck(
                i18nProvider, result, new EAAWrapper(xmlEAAPresentation), new MultiValuesConstraintWrapper(constraint));
        eaacc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

    @Test
    void notPresentClaimTest() {
        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.getId().add("metadata");
        constraint.getId().add("additional");
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

        EAAClaimsCheck eaacc = new EAAClaimsCheck(
                i18nProvider, result, new EAAWrapper(xmlEAAPresentation), new MultiValuesConstraintWrapper(constraint));
        eaacc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }
    
}
