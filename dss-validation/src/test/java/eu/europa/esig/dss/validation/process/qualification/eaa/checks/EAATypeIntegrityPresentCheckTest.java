package eu.europa.esig.dss.validation.process.qualification.eaa.checks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraint;
import eu.europa.esig.dss.detailedreport.jaxb.XmlStatus;
import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationProcessEAA;
import eu.europa.esig.dss.diagnostic.EAAWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAAPayload;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAA;
import eu.europa.esig.dss.diagnostic.jaxb.XmlIntegrityClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlMetadataTypeClaim;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.EAAType;
import eu.europa.esig.dss.enumerations.Level;
import eu.europa.esig.dss.policy.LevelConstraintWrapper;
import eu.europa.esig.dss.policy.jaxb.LevelConstraint;
import eu.europa.esig.dss.validation.process.bbb.AbstractTestCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.EAATypeIntegrityPresentCheck;

class EAATypeIntegrityPresentCheckTest extends AbstractTestCheck {

    @Test
    void validTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAA xmlEAA = new XmlEAA();
        xmlEAA.setEAAType(EAAType.SD_JWT_VC);
        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();
        XmlMetadataTypeClaim xmlMetadataTypeClaim = new XmlMetadataTypeClaim();
        xmlMetadataTypeClaim.setText("urn:eudi:pid:1");
        XmlIntegrityClaim xmlIntegrityClaim = new XmlIntegrityClaim();
        xmlIntegrityClaim.setDigestMethod(DigestAlgorithm.SHA256);
        xmlIntegrityClaim.setDigestValue("test".getBytes());
        xmlMetadataTypeClaim.setIntegrity(xmlIntegrityClaim);
        xmlEAAPayload.setMetadataType(xmlMetadataTypeClaim);
        xmlEAA.setEAAPayload(xmlEAAPayload);

        XmlValidationProcessEAA result = new XmlValidationProcessEAA();

        EAATypeIntegrityPresentCheck integrityPresentCheck = new EAATypeIntegrityPresentCheck(
                i18nProvider, result, new EAAWrapper(xmlEAA), new LevelConstraintWrapper(constraint));
        integrityPresentCheck.execute();

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
        XmlMetadataTypeClaim xmlMetadataTypeClaim = new XmlMetadataTypeClaim();
        xmlEAAPayload.setMetadataType(xmlMetadataTypeClaim);
        xmlEAA.setEAAPayload(xmlEAAPayload);

        XmlValidationProcessEAA result = new XmlValidationProcessEAA();

        EAATypeIntegrityPresentCheck integrityPresentCheck = new EAATypeIntegrityPresentCheck(
                i18nProvider, result, new EAAWrapper(xmlEAA), new LevelConstraintWrapper(constraint));
        integrityPresentCheck.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }
}
