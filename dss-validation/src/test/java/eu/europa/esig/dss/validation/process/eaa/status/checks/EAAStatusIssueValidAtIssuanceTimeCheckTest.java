package eu.europa.esig.dss.validation.process.eaa.status.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraint;
import eu.europa.esig.dss.detailedreport.jaxb.XmlSAV;
import eu.europa.esig.dss.detailedreport.jaxb.XmlStatus;
import eu.europa.esig.dss.diagnostic.EAAStatusTokenWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlCertificate;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAAStatusToken;
import eu.europa.esig.dss.diagnostic.jaxb.XmlSigningCertificate;
import eu.europa.esig.dss.enumerations.Level;
import eu.europa.esig.dss.policy.LevelConstraintWrapper;
import eu.europa.esig.dss.policy.jaxb.LevelConstraint;
import eu.europa.esig.dss.validation.process.bbb.AbstractTestCheck;
import eu.europa.esig.dss.validation.process.eaa.status.EAAStatusIssueValidAtIssuanceTimeCheck;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EAAStatusIssueValidAtIssuanceTimeCheckTest extends AbstractTestCheck {

    @Test
    void validTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAAStatusToken xmlEAAStatusToken = new XmlEAAStatusToken();

        Calendar calendar = Calendar.getInstance();
        Date currentTime = calendar.getTime();
        xmlEAAStatusToken.setIssuedAt(currentTime);

        XmlSigningCertificate xmlSigningCertificate = new XmlSigningCertificate();
        XmlCertificate xmlCertificate = new XmlCertificate();
        calendar.add(Calendar.MONTH, -1);
        xmlCertificate.setNotBefore(calendar.getTime());
        calendar.add(Calendar.MONTH, 2);
        xmlCertificate.setNotAfter(calendar.getTime());
        xmlSigningCertificate.setCertificate(xmlCertificate);

        xmlEAAStatusToken.setSigningCertificate(xmlSigningCertificate);
        XmlSAV result = new XmlSAV();

        EAAStatusIssueValidAtIssuanceTimeCheck eaasivaitc = new EAAStatusIssueValidAtIssuanceTimeCheck(
                i18nProvider, result, new EAAStatusTokenWrapper(xmlEAAStatusToken), new LevelConstraintWrapper(constraint));
        eaasivaitc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

    @Test
    void expiredTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAAStatusToken xmlEAAStatusToken = new XmlEAAStatusToken();

        Calendar calendar = Calendar.getInstance();
        Date currentTime = calendar.getTime();
        xmlEAAStatusToken.setIssuedAt(currentTime);

        XmlSigningCertificate xmlSigningCertificate = new XmlSigningCertificate();
        XmlCertificate xmlCertificate = new XmlCertificate();
        calendar.add(Calendar.MONTH, -6);
        xmlCertificate.setNotBefore(calendar.getTime());
        calendar.add(Calendar.MONTH, 2);
        xmlCertificate.setNotAfter(calendar.getTime());
        xmlSigningCertificate.setCertificate(xmlCertificate);

        xmlEAAStatusToken.setSigningCertificate(xmlSigningCertificate);
        XmlSAV result = new XmlSAV();

        EAAStatusIssueValidAtIssuanceTimeCheck eaasivaitc = new EAAStatusIssueValidAtIssuanceTimeCheck(
                i18nProvider, result, new EAAStatusTokenWrapper(xmlEAAStatusToken), new LevelConstraintWrapper(constraint));
        eaasivaitc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

    @Test
    void notYetValidTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAAStatusToken xmlEAAStatusToken = new XmlEAAStatusToken();

        Calendar calendar = Calendar.getInstance();
        Date currentTime = calendar.getTime();
        xmlEAAStatusToken.setIssuedAt(currentTime);

        XmlSigningCertificate xmlSigningCertificate = new XmlSigningCertificate();
        XmlCertificate xmlCertificate = new XmlCertificate();
        calendar.add(Calendar.MONTH, 6);
        xmlCertificate.setNotBefore(calendar.getTime());
        calendar.add(Calendar.MONTH, 2);
        xmlCertificate.setNotAfter(calendar.getTime());
        xmlSigningCertificate.setCertificate(xmlCertificate);

        xmlEAAStatusToken.setSigningCertificate(xmlSigningCertificate);
        XmlSAV result = new XmlSAV();

        EAAStatusIssueValidAtIssuanceTimeCheck eaasivaitc = new EAAStatusIssueValidAtIssuanceTimeCheck(
                i18nProvider, result, new EAAStatusTokenWrapper(xmlEAAStatusToken), new LevelConstraintWrapper(constraint));
        eaasivaitc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

    @Test
    void notSignCertTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAAStatusToken xmlEAAStatusToken = new XmlEAAStatusToken();

        Calendar calendar = Calendar.getInstance();
        Date currentTime = calendar.getTime();
        xmlEAAStatusToken.setIssuedAt(currentTime);

        XmlSAV result = new XmlSAV();

        EAAStatusIssueValidAtIssuanceTimeCheck eaasivaitc = new EAAStatusIssueValidAtIssuanceTimeCheck(
                i18nProvider, result, new EAAStatusTokenWrapper(xmlEAAStatusToken), new LevelConstraintWrapper(constraint));
        eaasivaitc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

}