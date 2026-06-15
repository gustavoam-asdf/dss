package eu.europa.esig.dss.validation.process.bbb.xcv.sub;

import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraint;
import eu.europa.esig.dss.detailedreport.jaxb.XmlStatus;
import eu.europa.esig.dss.detailedreport.jaxb.XmlSubXCV;
import eu.europa.esig.dss.diagnostic.CertificateWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlCertificate;
import eu.europa.esig.dss.diagnostic.jaxb.XmlQcPSB;
import eu.europa.esig.dss.diagnostic.jaxb.XmlQcStatements;
import eu.europa.esig.dss.enumerations.CertificateExtensionEnum;
import eu.europa.esig.dss.enumerations.Level;
import eu.europa.esig.dss.policy.MultiValuesConstraintWrapper;
import eu.europa.esig.dss.policy.jaxb.MultiValuesConstraint;
import eu.europa.esig.dss.validation.process.bbb.AbstractTestCheck;
import eu.europa.esig.dss.validation.process.bbb.xcv.sub.checks.CertificateQcPSBCountryOfLegislationCheck;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CertificateQcPSBCountryOfLegislationCheckTest extends AbstractTestCheck {

    @Test
    void validTest() {
        XmlQcStatements xmlQcStatements = new XmlQcStatements();
        xmlQcStatements.setOID(CertificateExtensionEnum.QC_STATEMENTS.getOid());

        XmlQcPSB xmlQcPSB = new XmlQcPSB();
        xmlQcPSB.setCountryOfLegislation("FR");
        xmlQcStatements.setQcPSB(xmlQcPSB);

        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.setLevel(Level.FAIL);
        constraint.getId().add("FR");

        XmlCertificate xc = new XmlCertificate();
        xc.getCertificateExtensions().add(xmlQcStatements);

        XmlSubXCV result = new XmlSubXCV();
        CertificateQcPSBCountryOfLegislationCheck cqcpsbclc = new CertificateQcPSBCountryOfLegislationCheck(
                i18nProvider, result, new CertificateWrapper(xc), new MultiValuesConstraintWrapper(constraint));
        cqcpsbclc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

    @Test
    void invalidTest() {
        XmlQcStatements xmlQcStatements = new XmlQcStatements();
        xmlQcStatements.setOID(CertificateExtensionEnum.QC_STATEMENTS.getOid());

        XmlQcPSB xmlQcPSB = new XmlQcPSB();
        xmlQcPSB.setCountryOfLegislation("FR");
        xmlQcStatements.setQcPSB(xmlQcPSB);

        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.setLevel(Level.FAIL);
        constraint.getId().add("DE");

        XmlCertificate xc = new XmlCertificate();
        xc.getCertificateExtensions().add(xmlQcStatements);

        XmlSubXCV result = new XmlSubXCV();
        CertificateQcPSBCountryOfLegislationCheck cqcpsbclc = new CertificateQcPSBCountryOfLegislationCheck(
                i18nProvider, result, new CertificateWrapper(xc), new MultiValuesConstraintWrapper(constraint));
        cqcpsbclc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

    @Test
    void multiValuesTest() {
        XmlQcStatements xmlQcStatements = new XmlQcStatements();
        xmlQcStatements.setOID(CertificateExtensionEnum.QC_STATEMENTS.getOid());

        XmlQcPSB xmlQcPSB = new XmlQcPSB();
        xmlQcPSB.setCountryOfLegislation("FR");
        xmlQcStatements.setQcPSB(xmlQcPSB);

        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.setLevel(Level.FAIL);
        constraint.getId().add("FR");
        constraint.getId().add("DE");

        XmlCertificate xc = new XmlCertificate();
        xc.getCertificateExtensions().add(xmlQcStatements);

        XmlSubXCV result = new XmlSubXCV();
        CertificateQcPSBCountryOfLegislationCheck cqcpsbclc = new CertificateQcPSBCountryOfLegislationCheck(
                i18nProvider, result, new CertificateWrapper(xc), new MultiValuesConstraintWrapper(constraint));
        cqcpsbclc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

    @Test
    void notDefinedTest() {
        XmlQcStatements xmlQcStatements = new XmlQcStatements();
        xmlQcStatements.setOID(CertificateExtensionEnum.QC_STATEMENTS.getOid());

        XmlQcPSB xmlQcPSB = new XmlQcPSB();
        xmlQcPSB.setAuthSourceIdentification("FR");
        xmlQcStatements.setQcPSB(xmlQcPSB);

        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.setLevel(Level.FAIL);
        constraint.getId().add("FR");

        XmlCertificate xc = new XmlCertificate();
        xc.getCertificateExtensions().add(xmlQcStatements);

        XmlSubXCV result = new XmlSubXCV();
        CertificateQcPSBCountryOfLegislationCheck cqcpsbclc = new CertificateQcPSBCountryOfLegislationCheck(
                i18nProvider, result, new CertificateWrapper(xc), new MultiValuesConstraintWrapper(constraint));
        cqcpsbclc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

    @Test
    void noQcPSBTest() {
        XmlQcStatements xmlQcStatements = new XmlQcStatements();
        xmlQcStatements.setOID(CertificateExtensionEnum.QC_STATEMENTS.getOid());

        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.setLevel(Level.FAIL);
        constraint.getId().add("FR");

        XmlCertificate xc = new XmlCertificate();
        xc.getCertificateExtensions().add(xmlQcStatements);

        XmlSubXCV result = new XmlSubXCV();
        CertificateQcPSBCountryOfLegislationCheck cqcpsbclc = new CertificateQcPSBCountryOfLegislationCheck(
                i18nProvider, result, new CertificateWrapper(xc), new MultiValuesConstraintWrapper(constraint));
        cqcpsbclc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

    @Test
    void noQcStatementsTest() {
        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.setLevel(Level.FAIL);
        constraint.getId().add("FR");

        XmlCertificate xc = new XmlCertificate();

        XmlSubXCV result = new XmlSubXCV();
        CertificateQcPSBCountryOfLegislationCheck cqcpsbclc = new CertificateQcPSBCountryOfLegislationCheck(
                i18nProvider, result, new CertificateWrapper(xc), new MultiValuesConstraintWrapper(constraint));
        cqcpsbclc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

}
