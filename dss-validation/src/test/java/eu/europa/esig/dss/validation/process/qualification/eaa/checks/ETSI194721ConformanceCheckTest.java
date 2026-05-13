package eu.europa.esig.dss.validation.process.qualification.eaa.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraint;
import eu.europa.esig.dss.detailedreport.jaxb.XmlStatus;
import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationProcessEAA;
import eu.europa.esig.dss.diagnostic.EAAWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAAPayload;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAASignature;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAA;
import eu.europa.esig.dss.diagnostic.jaxb.XmlMetadataTypeClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlSignature;
import eu.europa.esig.dss.diagnostic.jaxb.XmlStatusClaim;
import eu.europa.esig.dss.enumerations.EAACategory;
import eu.europa.esig.dss.enumerations.EAAType;
import eu.europa.esig.dss.enumerations.Level;
import eu.europa.esig.dss.policy.LevelConstraintWrapper;
import eu.europa.esig.dss.policy.jaxb.LevelConstraint;
import eu.europa.esig.dss.validation.process.bbb.AbstractTestCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.ETSI194721ConformanceCheck;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ETSI194721ConformanceCheckTest extends AbstractTestCheck {

    @Test
    void sdjwtBasicValidTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAA xmlEAA = new XmlEAA();
        xmlEAA.setEAAType(EAAType.SD_JWT_VC);

        XmlEAASignature presentationSignature = new XmlEAASignature();
        XmlSignature signature = new XmlSignature();
        presentationSignature.setSignature(signature);
        xmlEAA.getEAASignature().add(presentationSignature);

        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();
        XmlMetadataTypeClaim xmlMetadataTypeClaim = new XmlMetadataTypeClaim();
        xmlMetadataTypeClaim.setText("urn:eudi:pid:1");
        xmlEAAPayload.setMetadataType(xmlMetadataTypeClaim);

        XmlClaim notBefore = new XmlClaim();
        notBefore.setDateTime(new Date(System.currentTimeMillis() - 60000));
        xmlEAAPayload.setNotBefore(notBefore);

        XmlClaim notAfter = new XmlClaim();
        notAfter.setDateTime(new Date(notBefore.getDateTime().getTime() + 3600 * 1000));
        xmlEAAPayload.setExpiration(notAfter);

        xmlEAA.setEAAPayload(xmlEAAPayload);

        XmlValidationProcessEAA result = new XmlValidationProcessEAA();

        ETSI194721ConformanceCheck etsi194721ConformanceCheck = new ETSI194721ConformanceCheck(
                i18nProvider, result, new EAAWrapper(xmlEAA), new Date(), new LevelConstraintWrapper(constraint));
        etsi194721ConformanceCheck.execute();

        List<XmlConstraint> constraints = result.getConstraint();

        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

    @Test
    void mdocBasicValidTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAA xmlEAA = new XmlEAA();
        xmlEAA.setEAAType(EAAType.ISO_IEC_MDOC);

        XmlEAASignature presentationSignature = new XmlEAASignature();
        XmlSignature signature = new XmlSignature();
        presentationSignature.setSignature(signature);
        xmlEAA.getEAASignature().add(presentationSignature);

        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();
        XmlMetadataTypeClaim xmlMetadataTypeClaim = new XmlMetadataTypeClaim();
        xmlMetadataTypeClaim.setText("urn:eudi:pid:1");
        xmlEAAPayload.setMetadataType(xmlMetadataTypeClaim);

        XmlClaim notBefore = new XmlClaim();
        notBefore.setDateTime(new Date(System.currentTimeMillis() - 60000));
        xmlEAAPayload.setNotBefore(notBefore);

        XmlClaim notAfter = new XmlClaim();
        notAfter.setDateTime(new Date(notBefore.getDateTime().getTime() + 3600 * 1000));
        xmlEAAPayload.setExpiration(notAfter);

        XmlClaim documentNumber = new XmlClaim();
        documentNumber.setText("test-value");
        xmlEAAPayload.setDocumentNumber(documentNumber);

        XmlClaim issuingAuthority = new XmlClaim();
        issuingAuthority.setText("issuingAuthority");
        xmlEAAPayload.setIssuingAuthority(issuingAuthority);

        xmlEAA.setEAAPayload(xmlEAAPayload);

        XmlValidationProcessEAA result = new XmlValidationProcessEAA();

        ETSI194721ConformanceCheck etsi194721ConformanceCheck = new ETSI194721ConformanceCheck(
                i18nProvider, result, new EAAWrapper(xmlEAA), new Date(), new LevelConstraintWrapper(constraint));
        etsi194721ConformanceCheck.execute();

        List<XmlConstraint> constraints = result.getConstraint();

        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

    @Test
    void technicalValidityNotYetValidTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAA xmlEAA = new XmlEAA();
        xmlEAA.setEAAType(EAAType.SD_JWT_VC);

        XmlEAASignature presentationSignature = new XmlEAASignature();
        XmlSignature signature = new XmlSignature();
        presentationSignature.setSignature(signature);
        xmlEAA.getEAASignature().add(presentationSignature);

        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();
        XmlMetadataTypeClaim xmlMetadataTypeClaim = new XmlMetadataTypeClaim();
        xmlMetadataTypeClaim.setText("urn:eudi:pid:1");
        xmlEAAPayload.setMetadataType(xmlMetadataTypeClaim);

        XmlClaim notBefore = new XmlClaim();
        notBefore.setDateTime(new Date(System.currentTimeMillis() + 60000));
        xmlEAAPayload.setNotBefore(notBefore);

        XmlClaim notAfter = new XmlClaim();
        notAfter.setDateTime(new Date(notBefore.getDateTime().getTime() + 3600 * 1000));
        xmlEAAPayload.setExpiration(notAfter);

        xmlEAA.setEAAPayload(xmlEAAPayload);

        XmlValidationProcessEAA result = new XmlValidationProcessEAA();

        ETSI194721ConformanceCheck etsi194721ConformanceCheck = new ETSI194721ConformanceCheck(
                i18nProvider, result, new EAAWrapper(xmlEAA), new Date(), new LevelConstraintWrapper(constraint));
        etsi194721ConformanceCheck.execute();

        List<XmlConstraint> constraints = result.getConstraint();

        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

    @Test
    void technicalValidityNoLongerValidTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAA xmlEAA = new XmlEAA();
        xmlEAA.setEAAType(EAAType.SD_JWT_VC);

        XmlEAASignature presentationSignature = new XmlEAASignature();
        XmlSignature signature = new XmlSignature();
        presentationSignature.setSignature(signature);
        xmlEAA.getEAASignature().add(presentationSignature);

        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();
        XmlMetadataTypeClaim xmlMetadataTypeClaim = new XmlMetadataTypeClaim();
        xmlMetadataTypeClaim.setText("urn:eudi:pid:1");
        xmlEAAPayload.setMetadataType(xmlMetadataTypeClaim);

        XmlClaim notBefore = new XmlClaim();
        notBefore.setDateTime(new Date(System.currentTimeMillis() - 600000));
        xmlEAAPayload.setNotBefore(notBefore);

        XmlClaim notAfter = new XmlClaim();
        notAfter.setDateTime(new Date(System.currentTimeMillis() - 60000));
        xmlEAAPayload.setExpiration(notAfter);

        xmlEAA.setEAAPayload(xmlEAAPayload);

        XmlValidationProcessEAA result = new XmlValidationProcessEAA();

        ETSI194721ConformanceCheck etsi194721ConformanceCheck = new ETSI194721ConformanceCheck(
                i18nProvider, result, new EAAWrapper(xmlEAA), new Date(), new LevelConstraintWrapper(constraint));
        etsi194721ConformanceCheck.execute();

        List<XmlConstraint> constraints = result.getConstraint();

        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

    @Test
    void administrativeValidityNoLongerValidTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAA xmlEAA = new XmlEAA();
        xmlEAA.setEAAType(EAAType.SD_JWT_VC);

        XmlEAASignature presentationSignature = new XmlEAASignature();
        XmlSignature signature = new XmlSignature();
        presentationSignature.setSignature(signature);
        xmlEAA.getEAASignature().add(presentationSignature);

        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();
        XmlMetadataTypeClaim xmlMetadataTypeClaim = new XmlMetadataTypeClaim();
        xmlMetadataTypeClaim.setText("urn:eudi:pid:1");
        xmlEAAPayload.setMetadataType(xmlMetadataTypeClaim);

        XmlClaim notBefore = new XmlClaim();
        notBefore.setDateTime(new Date(System.currentTimeMillis() - 60000));
        xmlEAAPayload.setNotBefore(notBefore);

        XmlClaim notAfter = new XmlClaim();
        notAfter.setDateTime(new Date(notBefore.getDateTime().getTime() + 3600 * 1000));
        xmlEAAPayload.setExpiration(notAfter);

        XmlClaim administrativeIssuance = new XmlClaim();
        administrativeIssuance.setDateTime(new Date(System.currentTimeMillis() - 600000));
        xmlEAAPayload.setAdministrativeIssuanceDate(administrativeIssuance);

        XmlClaim administrativeExpiration = new XmlClaim();
        administrativeExpiration.setDateTime(new Date(System.currentTimeMillis() - 60000));
        xmlEAAPayload.setAdministrativeExpirationDate(administrativeExpiration);

        xmlEAA.setEAAPayload(xmlEAAPayload);

        XmlValidationProcessEAA result = new XmlValidationProcessEAA();

        ETSI194721ConformanceCheck etsi194721ConformanceCheck = new ETSI194721ConformanceCheck(
                i18nProvider, result, new EAAWrapper(xmlEAA), new Date(), new LevelConstraintWrapper(constraint));
        etsi194721ConformanceCheck.execute();

        List<XmlConstraint> constraints = result.getConstraint();

        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

    @Test
    void administrativeValidityNotYetValidTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAA xmlEAA = new XmlEAA();
        xmlEAA.setEAAType(EAAType.SD_JWT_VC);

        XmlEAASignature presentationSignature = new XmlEAASignature();
        XmlSignature signature = new XmlSignature();
        presentationSignature.setSignature(signature);
        xmlEAA.getEAASignature().add(presentationSignature);

        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();
        XmlMetadataTypeClaim xmlMetadataTypeClaim = new XmlMetadataTypeClaim();
        xmlMetadataTypeClaim.setText("urn:eudi:pid:1");
        xmlEAAPayload.setMetadataType(xmlMetadataTypeClaim);

        XmlClaim notBefore = new XmlClaim();
        notBefore.setDateTime(new Date(System.currentTimeMillis() - 60000));
        xmlEAAPayload.setNotBefore(notBefore);

        XmlClaim notAfter = new XmlClaim();
        notAfter.setDateTime(new Date(notBefore.getDateTime().getTime() + 3600 * 1000));
        xmlEAAPayload.setExpiration(notAfter);

        XmlClaim administrativeIssuance = new XmlClaim();
        administrativeIssuance.setDateTime(new Date(System.currentTimeMillis() + 60000));
        xmlEAAPayload.setAdministrativeIssuanceDate(administrativeIssuance);

        XmlClaim administrativeExpiration = new XmlClaim();
        administrativeExpiration.setDateTime(new Date(administrativeIssuance.getDateTime().getTime() + 3600 * 1000));
        xmlEAAPayload.setAdministrativeExpirationDate(administrativeExpiration);

        xmlEAA.setEAAPayload(xmlEAAPayload);

        XmlValidationProcessEAA result = new XmlValidationProcessEAA();

        ETSI194721ConformanceCheck etsi194721ConformanceCheck = new ETSI194721ConformanceCheck(
                i18nProvider, result, new EAAWrapper(xmlEAA), new Date(), new LevelConstraintWrapper(constraint));
        etsi194721ConformanceCheck.execute();

        List<XmlConstraint> constraints = result.getConstraint();

        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

    @Test
    void sdjwtAdministrativeValidityNotCompleteTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAA xmlEAA = new XmlEAA();
        xmlEAA.setEAAType(EAAType.SD_JWT_VC);

        XmlEAASignature presentationSignature = new XmlEAASignature();
        XmlSignature signature = new XmlSignature();
        presentationSignature.setSignature(signature);
        xmlEAA.getEAASignature().add(presentationSignature);

        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();
        XmlMetadataTypeClaim xmlMetadataTypeClaim = new XmlMetadataTypeClaim();
        xmlMetadataTypeClaim.setText("urn:eudi:pid:1");
        xmlEAAPayload.setMetadataType(xmlMetadataTypeClaim);

        XmlClaim notBefore = new XmlClaim();
        notBefore.setDateTime(new Date(System.currentTimeMillis() - 60000));
        xmlEAAPayload.setNotBefore(notBefore);

        XmlClaim notAfter = new XmlClaim();
        notAfter.setDateTime(new Date(notBefore.getDateTime().getTime() + 3600 * 1000));
        xmlEAAPayload.setExpiration(notAfter);

        XmlClaim administrativeIssuance = new XmlClaim();
        administrativeIssuance.setDateTime(new Date(System.currentTimeMillis() - 60000));
        xmlEAAPayload.setAdministrativeIssuanceDate(administrativeIssuance);

        xmlEAA.setEAAPayload(xmlEAAPayload);

        XmlValidationProcessEAA result = new XmlValidationProcessEAA();

        ETSI194721ConformanceCheck etsi194721ConformanceCheck = new ETSI194721ConformanceCheck(
                i18nProvider, result, new EAAWrapper(xmlEAA), new Date(), new LevelConstraintWrapper(constraint));
        etsi194721ConformanceCheck.execute();

        List<XmlConstraint> constraints = result.getConstraint();

        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

    @Test
    void mdocAdministrativeValidityNotCompleteTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAA xmlEAA = new XmlEAA();
        xmlEAA.setEAAType(EAAType.ISO_IEC_MDOC);

        XmlEAASignature presentationSignature = new XmlEAASignature();
        XmlSignature signature = new XmlSignature();
        presentationSignature.setSignature(signature);
        xmlEAA.getEAASignature().add(presentationSignature);

        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();
        XmlMetadataTypeClaim xmlMetadataTypeClaim = new XmlMetadataTypeClaim();
        xmlMetadataTypeClaim.setText("urn:eudi:pid:1");
        xmlEAAPayload.setMetadataType(xmlMetadataTypeClaim);

        XmlClaim notBefore = new XmlClaim();
        notBefore.setDateTime(new Date(System.currentTimeMillis() - 60000));
        xmlEAAPayload.setNotBefore(notBefore);

        XmlClaim notAfter = new XmlClaim();
        notAfter.setDateTime(new Date(notBefore.getDateTime().getTime() + 3600 * 1000));
        xmlEAAPayload.setExpiration(notAfter);

        XmlClaim administrativeIssuance = new XmlClaim();
        administrativeIssuance.setDateTime(new Date(System.currentTimeMillis() - 60000));
        xmlEAAPayload.setAdministrativeIssuanceDate(administrativeIssuance);

        XmlClaim documentNumber = new XmlClaim();
        documentNumber.setText("test-value");
        xmlEAAPayload.setDocumentNumber(documentNumber);

        XmlClaim issuingAuthority = new XmlClaim();
        issuingAuthority.setText("issuingAuthority");
        xmlEAAPayload.setIssuingAuthority(issuingAuthority);

        xmlEAA.setEAAPayload(xmlEAAPayload);

        XmlValidationProcessEAA result = new XmlValidationProcessEAA();

        ETSI194721ConformanceCheck etsi194721ConformanceCheck = new ETSI194721ConformanceCheck(
                i18nProvider, result, new EAAWrapper(xmlEAA), new Date(), new LevelConstraintWrapper(constraint));
        etsi194721ConformanceCheck.execute();

        List<XmlConstraint> constraints = result.getConstraint();

        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

    @Test
    void mdocDocumentNumberAbsentTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAA xmlEAA = new XmlEAA();
        xmlEAA.setEAAType(EAAType.ISO_IEC_MDOC);

        XmlEAASignature presentationSignature = new XmlEAASignature();
        XmlSignature signature = new XmlSignature();
        presentationSignature.setSignature(signature);
        xmlEAA.getEAASignature().add(presentationSignature);

        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();
        XmlMetadataTypeClaim xmlMetadataTypeClaim = new XmlMetadataTypeClaim();
        xmlMetadataTypeClaim.setText("urn:eudi:pid:1");
        xmlEAAPayload.setMetadataType(xmlMetadataTypeClaim);

        XmlClaim notBefore = new XmlClaim();
        notBefore.setDateTime(new Date(System.currentTimeMillis() - 60000));
        xmlEAAPayload.setNotBefore(notBefore);

        XmlClaim notAfter = new XmlClaim();
        notAfter.setDateTime(new Date(notBefore.getDateTime().getTime() + 3600 * 1000));
        xmlEAAPayload.setExpiration(notAfter);

        XmlClaim issuingAuthority = new XmlClaim();
        issuingAuthority.setText("issuingAuthority");
        xmlEAAPayload.setIssuingAuthority(issuingAuthority);

        xmlEAA.setEAAPayload(xmlEAAPayload);

        XmlValidationProcessEAA result = new XmlValidationProcessEAA();

        ETSI194721ConformanceCheck etsi194721ConformanceCheck = new ETSI194721ConformanceCheck(
                i18nProvider, result, new EAAWrapper(xmlEAA), new Date(), new LevelConstraintWrapper(constraint));
        etsi194721ConformanceCheck.execute();

        List<XmlConstraint> constraints = result.getConstraint();

        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

    @Test
    void mdocIssuingAuthorityAbsentTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAA xmlEAA = new XmlEAA();
        xmlEAA.setEAAType(EAAType.ISO_IEC_MDOC);

        XmlEAASignature presentationSignature = new XmlEAASignature();
        XmlSignature signature = new XmlSignature();
        presentationSignature.setSignature(signature);
        xmlEAA.getEAASignature().add(presentationSignature);

        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();
        XmlMetadataTypeClaim xmlMetadataTypeClaim = new XmlMetadataTypeClaim();
        xmlMetadataTypeClaim.setText("urn:eudi:pid:1");
        xmlEAAPayload.setMetadataType(xmlMetadataTypeClaim);

        XmlClaim notBefore = new XmlClaim();
        notBefore.setDateTime(new Date(System.currentTimeMillis() - 60000));
        xmlEAAPayload.setNotBefore(notBefore);

        XmlClaim notAfter = new XmlClaim();
        notAfter.setDateTime(new Date(notBefore.getDateTime().getTime() + 3600 * 1000));
        xmlEAAPayload.setExpiration(notAfter);

        XmlClaim documentNumber = new XmlClaim();
        documentNumber.setText("test-value");
        xmlEAAPayload.setDocumentNumber(documentNumber);

        xmlEAA.setEAAPayload(xmlEAAPayload);

        XmlValidationProcessEAA result = new XmlValidationProcessEAA();

        ETSI194721ConformanceCheck etsi194721ConformanceCheck = new ETSI194721ConformanceCheck(
                i18nProvider, result, new EAAWrapper(xmlEAA), new Date(), new LevelConstraintWrapper(constraint));
        etsi194721ConformanceCheck.execute();

        List<XmlConstraint> constraints = result.getConstraint();

        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

    @Test
    void qeaaIssuingAuthorityAbsentTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAA xmlEAA = new XmlEAA();
        xmlEAA.setEAAType(EAAType.SD_JWT_VC);

        XmlEAASignature presentationSignature = new XmlEAASignature();
        XmlSignature signature = new XmlSignature();
        presentationSignature.setSignature(signature);
        xmlEAA.getEAASignature().add(presentationSignature);

        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();
        XmlMetadataTypeClaim xmlMetadataTypeClaim = new XmlMetadataTypeClaim();
        xmlMetadataTypeClaim.setText("urn:eudi:pid:1");
        xmlEAAPayload.setMetadataType(xmlMetadataTypeClaim);

        XmlClaim shortLived = new XmlClaim();
        xmlEAAPayload.setShortLived(shortLived);

        XmlClaim notBefore = new XmlClaim();
        notBefore.setDateTime(new Date(System.currentTimeMillis() - 60000));
        xmlEAAPayload.setNotBefore(notBefore);

        XmlClaim notAfter = new XmlClaim();
        notAfter.setDateTime(new Date(notBefore.getDateTime().getTime() + 3600 * 1000));
        xmlEAAPayload.setExpiration(notAfter);

        XmlClaim category = new XmlClaim();
        category.setText(EAACategory.EU_QEAA.getUrn());
        xmlEAAPayload.setCategory(category);

        xmlEAA.setEAAPayload(xmlEAAPayload);

        XmlValidationProcessEAA result = new XmlValidationProcessEAA();

        ETSI194721ConformanceCheck etsi194721ConformanceCheck = new ETSI194721ConformanceCheck(
                i18nProvider, result, new EAAWrapper(xmlEAA), new Date(), new LevelConstraintWrapper(constraint));
        etsi194721ConformanceCheck.execute();

        List<XmlConstraint> constraints = result.getConstraint();

        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

    @Test
    void pubeaaIssuingAuthorityAbsentTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAA xmlEAA = new XmlEAA();
        xmlEAA.setEAAType(EAAType.SD_JWT_VC);

        XmlEAASignature presentationSignature = new XmlEAASignature();
        XmlSignature signature = new XmlSignature();
        presentationSignature.setSignature(signature);
        xmlEAA.getEAASignature().add(presentationSignature);

        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();
        XmlMetadataTypeClaim xmlMetadataTypeClaim = new XmlMetadataTypeClaim();
        xmlMetadataTypeClaim.setText("urn:eudi:pid:1");
        xmlEAAPayload.setMetadataType(xmlMetadataTypeClaim);

        XmlClaim shortLived = new XmlClaim();
        xmlEAAPayload.setShortLived(shortLived);

        XmlClaim notBefore = new XmlClaim();
        notBefore.setDateTime(new Date(System.currentTimeMillis() - 60000));
        xmlEAAPayload.setNotBefore(notBefore);

        XmlClaim notAfter = new XmlClaim();
        notAfter.setDateTime(new Date(notBefore.getDateTime().getTime() + 3600 * 1000));
        xmlEAAPayload.setExpiration(notAfter);

        XmlClaim category = new XmlClaim();
        category.setText(EAACategory.EU_PUBEAA.getUrn());
        xmlEAAPayload.setCategory(category);

        xmlEAA.setEAAPayload(xmlEAAPayload);

        XmlValidationProcessEAA result = new XmlValidationProcessEAA();

        ETSI194721ConformanceCheck etsi194721ConformanceCheck = new ETSI194721ConformanceCheck(
                i18nProvider, result, new EAAWrapper(xmlEAA), new Date(), new LevelConstraintWrapper(constraint));
        etsi194721ConformanceCheck.execute();

        List<XmlConstraint> constraints = result.getConstraint();

        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

    @Test
    void qeaaIssuingCountryAbsentTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAA xmlEAA = new XmlEAA();
        xmlEAA.setEAAType(EAAType.SD_JWT_VC);

        XmlEAASignature presentationSignature = new XmlEAASignature();
        XmlSignature signature = new XmlSignature();
        presentationSignature.setSignature(signature);
        xmlEAA.getEAASignature().add(presentationSignature);

        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();
        XmlMetadataTypeClaim xmlMetadataTypeClaim = new XmlMetadataTypeClaim();
        xmlMetadataTypeClaim.setText("urn:eudi:pid:1");
        xmlEAAPayload.setMetadataType(xmlMetadataTypeClaim);

        XmlClaim issuingAuthority = new XmlClaim();
        issuingAuthority.setText("VAT-12345");
        xmlEAAPayload.setIssuingAuthority(issuingAuthority);

        XmlClaim shortLived = new XmlClaim();
        xmlEAAPayload.setShortLived(shortLived);

        XmlClaim notBefore = new XmlClaim();
        notBefore.setDateTime(new Date(System.currentTimeMillis() - 60000));
        xmlEAAPayload.setNotBefore(notBefore);

        XmlClaim notAfter = new XmlClaim();
        notAfter.setDateTime(new Date(notBefore.getDateTime().getTime() + 3600 * 1000));
        xmlEAAPayload.setExpiration(notAfter);

        XmlClaim category = new XmlClaim();
        category.setText(EAACategory.EU_QEAA.getUrn());
        xmlEAAPayload.setCategory(category);

        xmlEAA.setEAAPayload(xmlEAAPayload);

        XmlValidationProcessEAA result = new XmlValidationProcessEAA();

        ETSI194721ConformanceCheck etsi194721ConformanceCheck = new ETSI194721ConformanceCheck(
                i18nProvider, result, new EAAWrapper(xmlEAA), new Date(), new LevelConstraintWrapper(constraint));
        etsi194721ConformanceCheck.execute();

        List<XmlConstraint> constraints = result.getConstraint();

        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

    @Test
    void pubeaaIssuingCountryAbsentTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAA xmlEAA = new XmlEAA();
        xmlEAA.setEAAType(EAAType.SD_JWT_VC);

        XmlEAASignature presentationSignature = new XmlEAASignature();
        XmlSignature signature = new XmlSignature();
        presentationSignature.setSignature(signature);
        xmlEAA.getEAASignature().add(presentationSignature);

        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();
        XmlMetadataTypeClaim xmlMetadataTypeClaim = new XmlMetadataTypeClaim();
        xmlMetadataTypeClaim.setText("urn:eudi:pid:1");
        xmlEAAPayload.setMetadataType(xmlMetadataTypeClaim);

        XmlClaim issuingAuthority = new XmlClaim();
        issuingAuthority.setText("VAT-12345");
        xmlEAAPayload.setIssuingAuthority(issuingAuthority);

        XmlClaim shortLived = new XmlClaim();
        xmlEAAPayload.setShortLived(shortLived);

        XmlClaim notBefore = new XmlClaim();
        notBefore.setDateTime(new Date(System.currentTimeMillis() - 60000));
        xmlEAAPayload.setNotBefore(notBefore);

        XmlClaim notAfter = new XmlClaim();
        notAfter.setDateTime(new Date(notBefore.getDateTime().getTime() + 3600 * 1000));
        xmlEAAPayload.setExpiration(notAfter);

        XmlClaim category = new XmlClaim();
        category.setText(EAACategory.EU_PUBEAA.getUrn());
        xmlEAAPayload.setCategory(category);

        xmlEAA.setEAAPayload(xmlEAAPayload);

        XmlValidationProcessEAA result = new XmlValidationProcessEAA();

        ETSI194721ConformanceCheck etsi194721ConformanceCheck = new ETSI194721ConformanceCheck(
                i18nProvider, result, new EAAWrapper(xmlEAA), new Date(), new LevelConstraintWrapper(constraint));
        etsi194721ConformanceCheck.execute();

        List<XmlConstraint> constraints = result.getConstraint();

        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

    @Test
    void qeaaIssuingCountryPresentTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAA xmlEAA = new XmlEAA();
        xmlEAA.setEAAType(EAAType.SD_JWT_VC);

        XmlEAASignature presentationSignature = new XmlEAASignature();
        XmlSignature signature = new XmlSignature();
        presentationSignature.setSignature(signature);
        xmlEAA.getEAASignature().add(presentationSignature);

        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();
        XmlMetadataTypeClaim xmlMetadataTypeClaim = new XmlMetadataTypeClaim();
        xmlMetadataTypeClaim.setText("urn:eudi:pid:1");
        xmlEAAPayload.setMetadataType(xmlMetadataTypeClaim);

        XmlClaim issuingAuthority = new XmlClaim();
        issuingAuthority.setText("VAT-12345");
        xmlEAAPayload.setIssuingAuthority(issuingAuthority);

        XmlClaim issuingCountry = new XmlClaim();
        issuingCountry.setText("LU");
        xmlEAAPayload.setIssuingCountry(issuingCountry);

        XmlClaim shortLived = new XmlClaim();
        xmlEAAPayload.setShortLived(shortLived);

        XmlClaim notBefore = new XmlClaim();
        notBefore.setDateTime(new Date(System.currentTimeMillis() - 60000));
        xmlEAAPayload.setNotBefore(notBefore);

        XmlClaim notAfter = new XmlClaim();
        notAfter.setDateTime(new Date(notBefore.getDateTime().getTime() + 3600 * 1000));
        xmlEAAPayload.setExpiration(notAfter);

        XmlClaim category = new XmlClaim();
        category.setText(EAACategory.EU_QEAA.getUrn());
        xmlEAAPayload.setCategory(category);

        xmlEAA.setEAAPayload(xmlEAAPayload);

        XmlValidationProcessEAA result = new XmlValidationProcessEAA();

        ETSI194721ConformanceCheck etsi194721ConformanceCheck = new ETSI194721ConformanceCheck(
                i18nProvider, result, new EAAWrapper(xmlEAA), new Date(), new LevelConstraintWrapper(constraint));
        etsi194721ConformanceCheck.execute();

        List<XmlConstraint> constraints = result.getConstraint();

        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

    @Test
    void pubeaaIssuingCountryPresentTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAA xmlEAA = new XmlEAA();
        xmlEAA.setEAAType(EAAType.SD_JWT_VC);

        XmlEAASignature presentationSignature = new XmlEAASignature();
        XmlSignature signature = new XmlSignature();
        presentationSignature.setSignature(signature);
        xmlEAA.getEAASignature().add(presentationSignature);

        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();
        XmlMetadataTypeClaim xmlMetadataTypeClaim = new XmlMetadataTypeClaim();
        xmlMetadataTypeClaim.setText("urn:eudi:pid:1");
        xmlEAAPayload.setMetadataType(xmlMetadataTypeClaim);

        XmlClaim issuingAuthority = new XmlClaim();
        issuingAuthority.setText("VAT-12345");
        xmlEAAPayload.setIssuingAuthority(issuingAuthority);

        XmlClaim issuingCountry = new XmlClaim();
        issuingCountry.setText("LU");
        xmlEAAPayload.setIssuingCountry(issuingCountry);

        XmlClaim shortLived = new XmlClaim();
        xmlEAAPayload.setShortLived(shortLived);

        XmlClaim notBefore = new XmlClaim();
        notBefore.setDateTime(new Date(System.currentTimeMillis() - 60000));
        xmlEAAPayload.setNotBefore(notBefore);

        XmlClaim notAfter = new XmlClaim();
        notAfter.setDateTime(new Date(notBefore.getDateTime().getTime() + 3600 * 1000));
        xmlEAAPayload.setExpiration(notAfter);

        XmlClaim category = new XmlClaim();
        category.setText(EAACategory.EU_PUBEAA.getUrn());
        xmlEAAPayload.setCategory(category);

        xmlEAA.setEAAPayload(xmlEAAPayload);

        XmlValidationProcessEAA result = new XmlValidationProcessEAA();

        ETSI194721ConformanceCheck etsi194721ConformanceCheck = new ETSI194721ConformanceCheck(
                i18nProvider, result, new EAAWrapper(xmlEAA), new Date(), new LevelConstraintWrapper(constraint));
        etsi194721ConformanceCheck.execute();

        List<XmlConstraint> constraints = result.getConstraint();

        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

    @Test
    void shortLivedWithStatusTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAA xmlEAA = new XmlEAA();
        xmlEAA.setEAAType(EAAType.ISO_IEC_MDOC);

        XmlEAASignature presentationSignature = new XmlEAASignature();
        XmlSignature signature = new XmlSignature();
        presentationSignature.setSignature(signature);
        xmlEAA.getEAASignature().add(presentationSignature);

        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();
        XmlMetadataTypeClaim xmlMetadataTypeClaim = new XmlMetadataTypeClaim();
        xmlMetadataTypeClaim.setText("urn:eudi:pid:1");
        xmlEAAPayload.setMetadataType(xmlMetadataTypeClaim);

        XmlClaim notBefore = new XmlClaim();
        notBefore.setDateTime(new Date(System.currentTimeMillis() - 60000));
        xmlEAAPayload.setNotBefore(notBefore);

        XmlClaim notAfter = new XmlClaim();
        notAfter.setDateTime(new Date(notBefore.getDateTime().getTime() + 3600 * 1000));
        xmlEAAPayload.setExpiration(notAfter);

        xmlEAAPayload.setShortLived(new XmlClaim());
        xmlEAAPayload.setStatus(new XmlStatusClaim());

        XmlClaim documentNumber = new XmlClaim();
        documentNumber.setText("test-value");
        xmlEAAPayload.setDocumentNumber(documentNumber);

        XmlClaim issuingAuthority = new XmlClaim();
        issuingAuthority.setText("issuingAuthority");
        xmlEAAPayload.setIssuingAuthority(issuingAuthority);

        xmlEAA.setEAAPayload(xmlEAAPayload);

        XmlValidationProcessEAA result = new XmlValidationProcessEAA();

        ETSI194721ConformanceCheck etsi194721ConformanceCheck = new ETSI194721ConformanceCheck(
                i18nProvider, result, new EAAWrapper(xmlEAA), new Date(), new LevelConstraintWrapper(constraint));
        etsi194721ConformanceCheck.execute();

        List<XmlConstraint> constraints = result.getConstraint();

        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

    // TODO : disabled until review in ETSI TS 119 472-1
    @Disabled
    @Test
    void sdjwtStatusConformanceInvalidTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAA xmlEAA = new XmlEAA();
        xmlEAA.setEAAType(EAAType.SD_JWT_VC);

        XmlEAASignature presentationSignature = new XmlEAASignature();
        XmlSignature signature = new XmlSignature();
        presentationSignature.setSignature(signature);
        xmlEAA.getEAASignature().add(presentationSignature);

        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();
        XmlMetadataTypeClaim xmlMetadataTypeClaim = new XmlMetadataTypeClaim();
        xmlMetadataTypeClaim.setText("urn:eudi:pid:1");
        xmlEAAPayload.setMetadataType(xmlMetadataTypeClaim);

        XmlClaim notBefore = new XmlClaim();
        notBefore.setDateTime(new Date(System.currentTimeMillis() - 60000));
        xmlEAAPayload.setNotBefore(notBefore);

        XmlClaim notAfter = new XmlClaim();
        notAfter.setDateTime(new Date(notBefore.getDateTime().getTime() + 3600 * 1000));
        xmlEAAPayload.setExpiration(notAfter);

        xmlEAAPayload.setStatus(new XmlStatusClaim());

        xmlEAA.setEAAPayload(xmlEAAPayload);

        XmlValidationProcessEAA result = new XmlValidationProcessEAA();

        ETSI194721ConformanceCheck etsi194721ConformanceCheck = new ETSI194721ConformanceCheck(
                i18nProvider, result, new EAAWrapper(xmlEAA), new Date(), new LevelConstraintWrapper(constraint));
        etsi194721ConformanceCheck.execute();

        List<XmlConstraint> constraints = result.getConstraint();

        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

    @Test
    void sdjwtStatusConformanceValidTest() {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlEAA xmlEAA = new XmlEAA();
        xmlEAA.setEAAType(EAAType.SD_JWT_VC);

        XmlEAASignature presentationSignature = new XmlEAASignature();
        XmlSignature signature = new XmlSignature();
        presentationSignature.setSignature(signature);
        xmlEAA.getEAASignature().add(presentationSignature);

        XmlEAAPayload xmlEAAPayload = new XmlEAAPayload();
        XmlMetadataTypeClaim xmlMetadataTypeClaim = new XmlMetadataTypeClaim();
        xmlMetadataTypeClaim.setText("urn:eudi:pid:1");
        xmlEAAPayload.setMetadataType(xmlMetadataTypeClaim);

        XmlClaim notBefore = new XmlClaim();
        notBefore.setDateTime(new Date(System.currentTimeMillis() - 60000));
        xmlEAAPayload.setNotBefore(notBefore);

        XmlClaim notAfter = new XmlClaim();
        notAfter.setDateTime(new Date(notBefore.getDateTime().getTime() + 3600 * 1000));
        xmlEAAPayload.setExpiration(notAfter);

        XmlStatusClaim statusClaim = new XmlStatusClaim();
        XmlClaim type = new XmlClaim();
        type.setText("status type");
        XmlClaim purpose = new XmlClaim();
        purpose.setText("status purpose");
        XmlClaim index = new XmlClaim();
        index.setNumber(BigInteger.ONE);
        XmlClaim uri = new XmlClaim();
        uri.setText("status uri");
        statusClaim.setType(type);
        statusClaim.setPurpose(purpose);
        statusClaim.setIndex(index);
        statusClaim.setUri(uri);

        xmlEAAPayload.setStatus(statusClaim);

        xmlEAA.setEAAPayload(xmlEAAPayload);

        XmlValidationProcessEAA result = new XmlValidationProcessEAA();

        ETSI194721ConformanceCheck etsi194721ConformanceCheck = new ETSI194721ConformanceCheck(
                i18nProvider, result, new EAAWrapper(xmlEAA), new Date(), new LevelConstraintWrapper(constraint));
        etsi194721ConformanceCheck.execute();

        List<XmlConstraint> constraints = result.getConstraint();

        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

}
