package eu.europa.esig.dss.validation.process.qualification.eaa.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraint;
import eu.europa.esig.dss.detailedreport.jaxb.XmlStatus;
import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationProcessEAA;
import eu.europa.esig.dss.diagnostic.EAAWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAA;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAAPayload;
import eu.europa.esig.dss.enumerations.EAAType;
import eu.europa.esig.dss.enumerations.Level;
import eu.europa.esig.dss.policy.MultiValuesConstraintWrapper;
import eu.europa.esig.dss.policy.jaxb.MultiValuesConstraint;
import eu.europa.esig.dss.validation.process.bbb.AbstractTestCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.EAAIssuingAuthorityCheck;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EAAIssuingAuthorityCheckTest extends AbstractTestCheck {

    @Test
    void validTest() {
        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.getId().add("Example Authority");
        constraint.setLevel(Level.FAIL);

        XmlEAA xmlEAA = new XmlEAA();
        xmlEAA.setEAAType(EAAType.SD_JWT_VC);
        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();
        XmlClaim xmlClaim = new XmlClaim();
        xmlClaim.setText("Example Authority");
        xmlEAAPayload.setIssuingAuthority(xmlClaim);
        xmlEAA.setEAAPayload(xmlEAAPayload);

        XmlValidationProcessEAA result = new XmlValidationProcessEAA();

        EAAIssuingAuthorityCheck eaaiac = new EAAIssuingAuthorityCheck(
                i18nProvider, result, new EAAWrapper(xmlEAA), new MultiValuesConstraintWrapper(constraint));
        eaaiac.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

    @Test
    void invalidTest() {
        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.getId().add("Example Authority");
        constraint.setLevel(Level.FAIL);

        XmlEAA xmlEAA = new XmlEAA();
        xmlEAA.setEAAType(EAAType.SD_JWT_VC);
        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();
        XmlClaim xmlClaim = new XmlClaim();
        xmlClaim.setText("Alternative Authority");
        xmlEAAPayload.setIssuingAuthority(xmlClaim);
        xmlEAA.setEAAPayload(xmlEAAPayload);

        XmlValidationProcessEAA result = new XmlValidationProcessEAA();

        EAAIssuingAuthorityCheck eaaiac = new EAAIssuingAuthorityCheck(
                i18nProvider, result, new EAAWrapper(xmlEAA), new MultiValuesConstraintWrapper(constraint));
        eaaiac.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

}