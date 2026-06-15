package eu.europa.esig.dss.validation.process.bbb.sav;

import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraint;
import eu.europa.esig.dss.detailedreport.jaxb.XmlSAV;
import eu.europa.esig.dss.detailedreport.jaxb.XmlStatus;
import eu.europa.esig.dss.diagnostic.SignatureWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlSignature;
import eu.europa.esig.dss.enumerations.Level;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.policy.MultiValuesConstraintWrapper;
import eu.europa.esig.dss.policy.jaxb.MultiValuesConstraint;
import eu.europa.esig.dss.validation.process.bbb.AbstractTestCheck;
import eu.europa.esig.dss.validation.process.bbb.sav.checks.SignatureTypeCheck;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SignatureTypeCheckTest extends AbstractTestCheck {

    @Test
    void valid() {
        XmlSignature sig = new XmlSignature();
        sig.setSignatureType("dc+sd-jwt");
        sig.setSignatureFormat(SignatureLevel.JAdES_BASELINE_B);

        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.setLevel(Level.FAIL);
        constraint.getId().add("dc+sd-jwt");

        XmlSAV result = new XmlSAV();
        SignatureTypeCheck stc = new SignatureTypeCheck(i18nProvider, result, new SignatureWrapper(sig), new MultiValuesConstraintWrapper(constraint));
        stc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

    @Test
    void validApplicationType() {
        XmlSignature sig = new XmlSignature();
        sig.setSignatureType("application/dc+sd-jwt");
        sig.setSignatureFormat(SignatureLevel.JAdES_BASELINE_B);

        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.setLevel(Level.FAIL);
        constraint.getId().add("dc+sd-jwt");

        XmlSAV result = new XmlSAV();
        SignatureTypeCheck stc = new SignatureTypeCheck(i18nProvider, result, new SignatureWrapper(sig), new MultiValuesConstraintWrapper(constraint));
        stc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

    @Test
    void validApplicationConstraintType() {
        XmlSignature sig = new XmlSignature();
        sig.setSignatureType("application/dc+sd-jwt");
        sig.setSignatureFormat(SignatureLevel.JAdES_BASELINE_B);

        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.setLevel(Level.FAIL);
        constraint.getId().add("application/dc+sd-jwt");

        XmlSAV result = new XmlSAV();
        SignatureTypeCheck stc = new SignatureTypeCheck(i18nProvider, result, new SignatureWrapper(sig), new MultiValuesConstraintWrapper(constraint));
        stc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

    @Test
    void invalid() {
        XmlSignature sig = new XmlSignature();
        sig.setSignatureType("dd+sd-jwt");
        sig.setSignatureFormat(SignatureLevel.JAdES_BASELINE_B);

        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.setLevel(Level.FAIL);
        constraint.getId().add("dc+sd-jwt");

        XmlSAV result = new XmlSAV();
        SignatureTypeCheck stc = new SignatureTypeCheck(i18nProvider, result, new SignatureWrapper(sig), new MultiValuesConstraintWrapper(constraint));
        stc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

    @Test
    void all() {
        XmlSignature sig = new XmlSignature();
        sig.setSignatureType("dc+sd-jwt");
        sig.setSignatureFormat(SignatureLevel.JAdES_BASELINE_B);

        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.setLevel(Level.FAIL);
        constraint.getId().add("*");

        XmlSAV result = new XmlSAV();
        SignatureTypeCheck stc = new SignatureTypeCheck(i18nProvider, result, new SignatureWrapper(sig), new MultiValuesConstraintWrapper(constraint));
        stc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

    @Test
    void notPresent() {
        XmlSignature sig = new XmlSignature();
        sig.setSignatureFormat(SignatureLevel.JAdES_BASELINE_B);

        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.setLevel(Level.FAIL);
        constraint.getId().add("*");

        XmlSAV result = new XmlSAV();
        SignatureTypeCheck stc = new SignatureTypeCheck(i18nProvider, result, new SignatureWrapper(sig), new MultiValuesConstraintWrapper(constraint));
        stc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

}
