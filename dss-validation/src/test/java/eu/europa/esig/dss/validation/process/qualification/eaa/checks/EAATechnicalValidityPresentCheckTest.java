package eu.europa.esig.dss.validation.process.qualification.eaa.checks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraint;
import eu.europa.esig.dss.detailedreport.jaxb.XmlStatus;
import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationProcessEAAPresentation;
import eu.europa.esig.dss.diagnostic.EAAPresentationWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAAPayload;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAAPresentation;
import eu.europa.esig.dss.diagnostic.jaxb.XmlMetadataTypeClaim;
import eu.europa.esig.dss.enumerations.EAAPresentationType;
import eu.europa.esig.dss.enumerations.Level;
import eu.europa.esig.dss.policy.LevelConstraintWrapper;
import eu.europa.esig.dss.policy.jaxb.LevelConstraint;
import eu.europa.esig.dss.validation.process.bbb.AbstractTestCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.EAATechnicalValidityPresentCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.SDJWTEAAVctIntegrityPresentCheck;

class EAATechnicalValidityPresentCheckTest extends AbstractTestCheck {

    @Test
    void validTest(){
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAAPresentation xmlEAAPresentation = new XmlEAAPresentation();
        xmlEAAPresentation.setEAAType(EAAPresentationType.SD_JWT_VC);
        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();

        XmlClaim notBefore = new XmlClaim();
        notBefore.setDateTime(new Date());
        XmlClaim expiration = new XmlClaim();
        expiration.setDateTime(new Date(notBefore.getDateTime().getTime()  + 3600 * 1000));

        xmlEAAPayload.setNotBefore(notBefore);
        xmlEAAPayload.setExpirationTime(expiration);
        xmlEAAPresentation.setEAAPayload(xmlEAAPayload);

        XmlValidationProcessEAAPresentation result = new XmlValidationProcessEAAPresentation();

        EAATechnicalValidityPresentCheck typePresentCheck = new EAATechnicalValidityPresentCheck(
                i18nProvider, result, new EAAPresentationWrapper(xmlEAAPresentation), new LevelConstraintWrapper(constraint));
        typePresentCheck.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

    @Test
    void invalidTest(){
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAAPresentation xmlEAAPresentation = new XmlEAAPresentation();
        xmlEAAPresentation.setEAAType(EAAPresentationType.SD_JWT_VC);
        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();
        xmlEAAPresentation.setEAAPayload(xmlEAAPayload);

        XmlValidationProcessEAAPresentation result = new XmlValidationProcessEAAPresentation();

        EAATechnicalValidityPresentCheck typePresentCheck = new EAATechnicalValidityPresentCheck(
                i18nProvider, result, new EAAPresentationWrapper(xmlEAAPresentation), new LevelConstraintWrapper(constraint));
        typePresentCheck.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

    @Test
    void invalidOnlyNotBeforeTest(){
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAAPresentation xmlEAAPresentation = new XmlEAAPresentation();
        xmlEAAPresentation.setEAAType(EAAPresentationType.SD_JWT_VC);
        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();
        XmlClaim notBefore = new XmlClaim();
        notBefore.setDateTime(new Date());
        xmlEAAPayload.setNotBefore(notBefore);
        xmlEAAPresentation.setEAAPayload(xmlEAAPayload);

        XmlValidationProcessEAAPresentation result = new XmlValidationProcessEAAPresentation();

        EAATechnicalValidityPresentCheck typePresentCheck = new EAATechnicalValidityPresentCheck(
                i18nProvider, result, new EAAPresentationWrapper(xmlEAAPresentation), new LevelConstraintWrapper(constraint));
        typePresentCheck.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }
}
