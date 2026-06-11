package eu.europa.esig.dss.validation.process.eaa.status.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraint;
import eu.europa.esig.dss.detailedreport.jaxb.XmlSAV;
import eu.europa.esig.dss.detailedreport.jaxb.XmlStatus;
import eu.europa.esig.dss.diagnostic.EAAStatusTokenWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAAStatusToken;
import eu.europa.esig.dss.enumerations.Level;
import eu.europa.esig.dss.policy.LevelConstraintWrapper;
import eu.europa.esig.dss.policy.jaxb.LevelConstraint;
import eu.europa.esig.dss.validation.process.bbb.AbstractTestCheck;
import eu.europa.esig.dss.validation.process.eaa.status.EAAStatusNotExpiredCheck;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EAAStatusNotExpiredCheckTest extends AbstractTestCheck {

    @Test
    void validTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAAStatusToken xmlEAAStatusToken = new XmlEAAStatusToken();

        Calendar calendar = Calendar.getInstance();
        Date currentTime = calendar.getTime();
        calendar.add(Calendar.MONTH, -1);
        xmlEAAStatusToken.setIssuedAt(calendar.getTime());
        calendar.add(Calendar.MONTH, 3);
        xmlEAAStatusToken.setExpirationTime(calendar.getTime());

        XmlSAV result = new XmlSAV();

        EAAStatusNotExpiredCheck eaasnec = new EAAStatusNotExpiredCheck(
                i18nProvider, result, new EAAStatusTokenWrapper(xmlEAAStatusToken), currentTime, new LevelConstraintWrapper(constraint));
        eaasnec.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

    @Test
    void invalidTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAAStatusToken xmlEAAStatusToken = new XmlEAAStatusToken();

        Calendar calendar = Calendar.getInstance();
        Date currentTime = calendar.getTime();
        calendar.add(Calendar.MONTH, -1);
        xmlEAAStatusToken.setIssuedAt(calendar.getTime());
        calendar.add(Calendar.DATE, 1);
        xmlEAAStatusToken.setExpirationTime(calendar.getTime());

        XmlSAV result = new XmlSAV();

        EAAStatusNotExpiredCheck eaasnec = new EAAStatusNotExpiredCheck(
                i18nProvider, result, new EAAStatusTokenWrapper(xmlEAAStatusToken), currentTime, new LevelConstraintWrapper(constraint));
        eaasnec.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

}
