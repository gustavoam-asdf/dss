package eu.europa.esig.dss.validation.process.eaa.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraint;
import eu.europa.esig.dss.detailedreport.jaxb.XmlStatus;
import eu.europa.esig.dss.detailedreport.jaxb.XmlSAV;
import eu.europa.esig.dss.diagnostic.EAAWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAA;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAAPayload;
import eu.europa.esig.dss.enumerations.EAAType;
import eu.europa.esig.dss.enumerations.Level;
import eu.europa.esig.dss.policy.LevelConstraintWrapper;
import eu.europa.esig.dss.policy.jaxb.LevelConstraint;
import eu.europa.esig.dss.validation.process.bbb.AbstractTestCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.EAAAdministrativePeriodNotExpiredCheck;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EAAAdministrativePeriodNotExpiredCheckTest extends AbstractTestCheck {

    @Test
    void validTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAA xmlEAA = new XmlEAA();
        xmlEAA.setEAAType(EAAType.SD_JWT_VC);
        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();

        Date now = new Date();
        long nowMil = now.getTime();

        XmlClaim notBefore = new XmlClaim();
        notBefore.setDateTime(new Date(nowMil - 86400000)); // 24 hours ago
        xmlEAAPayload.setAdministrativeIssuanceDate(notBefore);

        XmlClaim expiration = new XmlClaim();
        expiration.setDateTime(new Date(nowMil + 86400000)); // in 24 hours
        xmlEAAPayload.setAdministrativeExpirationDate(expiration);

        xmlEAA.setEAAPayload(xmlEAAPayload);

        XmlSAV result = new XmlSAV();

        EAAAdministrativePeriodNotExpiredCheck eaaapnec = new EAAAdministrativePeriodNotExpiredCheck(
                i18nProvider, result, new EAAWrapper(xmlEAA), now, new LevelConstraintWrapper(constraint));
        eaaapnec.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

    @Test
    void expiredTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAA xmlEAA = new XmlEAA();
        xmlEAA.setEAAType(EAAType.SD_JWT_VC);
        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();

        Date now = new Date();
        long nowMil = now.getTime();

        XmlClaim notBefore = new XmlClaim();
        notBefore.setDateTime(new Date(nowMil - 172800000)); // 48 hours ago
        xmlEAAPayload.setAdministrativeIssuanceDate(notBefore);

        XmlClaim expiration = new XmlClaim();
        expiration.setDateTime(new Date(nowMil - 86400000)); // 24 hours ago
        xmlEAAPayload.setAdministrativeExpirationDate(expiration);

        xmlEAA.setEAAPayload(xmlEAAPayload);

        XmlSAV result = new XmlSAV();

        EAAAdministrativePeriodNotExpiredCheck eaaapnec = new EAAAdministrativePeriodNotExpiredCheck(
                i18nProvider, result, new EAAWrapper(xmlEAA), now, new LevelConstraintWrapper(constraint));
        eaaapnec.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

    @Test
    void notYetValidTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAA xmlEAA = new XmlEAA();
        xmlEAA.setEAAType(EAAType.SD_JWT_VC);
        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();

        Date now = new Date();
        long nowMil = now.getTime();

        XmlClaim notBefore = new XmlClaim();
        notBefore.setDateTime(new Date(nowMil + 86400000)); // 24 hours ago
        xmlEAAPayload.setAdministrativeIssuanceDate(notBefore);

        XmlClaim expiration = new XmlClaim();
        expiration.setDateTime(new Date(nowMil + 172800000)); // 48 hours after
        xmlEAAPayload.setAdministrativeExpirationDate(expiration);

        xmlEAA.setEAAPayload(xmlEAAPayload);

        XmlSAV result = new XmlSAV();

        EAAAdministrativePeriodNotExpiredCheck eaaapnec = new EAAAdministrativePeriodNotExpiredCheck(
                i18nProvider, result, new EAAWrapper(xmlEAA), now, new LevelConstraintWrapper(constraint));
        eaaapnec.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

}