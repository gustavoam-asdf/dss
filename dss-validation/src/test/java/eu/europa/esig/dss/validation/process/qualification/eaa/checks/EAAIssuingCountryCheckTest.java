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
import eu.europa.esig.dss.validation.process.eaa.checks.EAAIssuingCountryCheck;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EAAIssuingCountryCheckTest extends AbstractTestCheck {

    @Test
    void validTest() {
        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.getId().add("LU");
        constraint.setLevel(Level.FAIL);

        XmlEAA xmlEAA = new XmlEAA();
        xmlEAA.setEAAType(EAAType.SD_JWT_VC);
        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();
        XmlClaim xmlClaim = new XmlClaim();
        xmlClaim.setText("LU");
        xmlEAAPayload.setIssuingCountry(xmlClaim);
        xmlEAA.setEAAPayload(xmlEAAPayload);

        XmlValidationProcessEAA result = new XmlValidationProcessEAA();

        EAAIssuingCountryCheck eaaicc = new EAAIssuingCountryCheck(
                i18nProvider, result, new EAAWrapper(xmlEAA), new MultiValuesConstraintWrapper(constraint));
        eaaicc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

    @Test
    void invalidTest() {
        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.getId().add("LU");
        constraint.setLevel(Level.FAIL);

        XmlEAA xmlEAA = new XmlEAA();
        xmlEAA.setEAAType(EAAType.SD_JWT_VC);
        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();
        XmlClaim xmlClaim = new XmlClaim();
        xmlClaim.setText("DE");
        xmlEAAPayload.setIssuingCountry(xmlClaim);
        xmlEAA.setEAAPayload(xmlEAAPayload);

        XmlValidationProcessEAA result = new XmlValidationProcessEAA();

        EAAIssuingCountryCheck eaaicc = new EAAIssuingCountryCheck(
                i18nProvider, result, new EAAWrapper(xmlEAA), new MultiValuesConstraintWrapper(constraint));
        eaaicc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

}