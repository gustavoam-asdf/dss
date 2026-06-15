package eu.europa.esig.dss.validation.process.eaa.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraint;
import eu.europa.esig.dss.detailedreport.jaxb.XmlStatus;
import eu.europa.esig.dss.detailedreport.jaxb.XmlSAV;
import eu.europa.esig.dss.diagnostic.EAAWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAA;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAAPayload;
import eu.europa.esig.dss.diagnostic.jaxb.XmlVerifiableCredentialsTypeClaim;
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

        XmlEAA xmlEAA = new XmlEAA();
        xmlEAA.setEAAType(EAAType.SD_JWT_VC);
        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();
        XmlVerifiableCredentialsTypeClaim xmlVerifiableCredentialsTypeClaim = new XmlVerifiableCredentialsTypeClaim();
        xmlVerifiableCredentialsTypeClaim.setText("urn:eudi:pid:1");
        xmlVerifiableCredentialsTypeClaim.setName("metadata");
        xmlEAAPayload.setVerifiableCredentialsType(xmlVerifiableCredentialsTypeClaim);
        xmlEAA.setEAAPayload(xmlEAAPayload);

        XmlSAV result = new XmlSAV();

        EAAClaimsCheck eaacc = new EAAClaimsCheck(
                i18nProvider, result, new EAAWrapper(xmlEAA), new MultiValuesConstraintWrapper(constraint));
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

        XmlEAA xmlEAA = new XmlEAA();
        xmlEAA.setEAAType(EAAType.SD_JWT_VC);
        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();
        XmlVerifiableCredentialsTypeClaim xmlVerifiableCredentialsTypeClaim = new XmlVerifiableCredentialsTypeClaim();
        xmlVerifiableCredentialsTypeClaim.setText("urn:eudi:pid:1");
        xmlVerifiableCredentialsTypeClaim.setName("metadata-wrong");
        xmlEAAPayload.setVerifiableCredentialsType(xmlVerifiableCredentialsTypeClaim);
        xmlEAA.setEAAPayload(xmlEAAPayload);

        XmlSAV result = new XmlSAV();

        EAAClaimsCheck eaacc = new EAAClaimsCheck(
                i18nProvider, result, new EAAWrapper(xmlEAA), new MultiValuesConstraintWrapper(constraint));
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

        XmlEAA xmlEAA = new XmlEAA();
        xmlEAA.setEAAType(EAAType.SD_JWT_VC);
        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();
        XmlVerifiableCredentialsTypeClaim xmlVerifiableCredentialsTypeClaim = new XmlVerifiableCredentialsTypeClaim();
        xmlVerifiableCredentialsTypeClaim.setText("urn:eudi:pid:1");
        xmlVerifiableCredentialsTypeClaim.setName("metadata");
        xmlEAAPayload.setVerifiableCredentialsType(xmlVerifiableCredentialsTypeClaim);

        XmlClaim xmlClaim = new XmlClaim();
        xmlClaim.setName("additional");
        xmlEAAPayload.getOtherClaim().add(xmlClaim);

        xmlEAA.setEAAPayload(xmlEAAPayload);

        XmlSAV result = new XmlSAV();

        EAAClaimsCheck eaacc = new EAAClaimsCheck(
                i18nProvider, result, new EAAWrapper(xmlEAA), new MultiValuesConstraintWrapper(constraint));
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

        XmlEAA xmlEAA = new XmlEAA();
        xmlEAA.setEAAType(EAAType.SD_JWT_VC);
        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();
        XmlVerifiableCredentialsTypeClaim xmlVerifiableCredentialsTypeClaim = new XmlVerifiableCredentialsTypeClaim();
        xmlVerifiableCredentialsTypeClaim.setText("urn:eudi:pid:1");
        xmlVerifiableCredentialsTypeClaim.setName("metadata");
        xmlEAAPayload.setVerifiableCredentialsType(xmlVerifiableCredentialsTypeClaim);

        xmlEAA.setEAAPayload(xmlEAAPayload);

        XmlSAV result = new XmlSAV();

        EAAClaimsCheck eaacc = new EAAClaimsCheck(
                i18nProvider, result, new EAAWrapper(xmlEAA), new MultiValuesConstraintWrapper(constraint));
        eaacc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }
    
}
