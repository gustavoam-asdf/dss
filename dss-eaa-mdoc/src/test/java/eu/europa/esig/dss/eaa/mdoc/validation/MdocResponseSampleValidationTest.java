package eu.europa.esig.dss.eaa.mdoc.validation;

import eu.europa.esig.dss.diagnostic.CertificateRefWrapper;
import eu.europa.esig.dss.diagnostic.CertificateWrapper;
import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.EAAPresentationWrapper;
import eu.europa.esig.dss.diagnostic.SignatureWrapper;
import eu.europa.esig.dss.diagnostic.claim.ClaimWrapper;
import eu.europa.esig.dss.diagnostic.claim.DeviceKeyClaimWrapper;
import eu.europa.esig.dss.diagnostic.claim.DrivingPrivilegeClaimWrapper;
import eu.europa.esig.dss.diagnostic.claim.DrivingPrivilegesClaimWrapper;
import eu.europa.esig.dss.diagnostic.claim.ValidityInfoClaimWrapper;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.FileDocument;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.utils.Utils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class MdocResponseSampleValidationTest extends AbstractMdocEAAPresentationTestValidation {

    @Override
    protected DSSDocument getSignedDocument() {
        return new FileDocument("src/test/resources/validation/mdocResponseIso180135.mdoc");
    }

    @Override
    protected void checkClaims(DiagnosticData diagnosticData) {
        super.checkClaims(diagnosticData);

        EAAPresentationWrapper eaaPresentation = diagnosticData.getEAAPresentations().get(0);
        assertEquals(DSSUtils.parseRFCDate("2024-10-20T00:00:00Z"), eaaPresentation.getEAAExpirationTime());
        assertEquals(DSSUtils.parseRFCDate("2019-10-20T00:00:00Z"), eaaPresentation.getEAAIssuedAt());
        // assertNotNull(eaaPresentation.getEAADeviceKey()); // TODO: add key binding support

        assertEquals("1.0", eaaPresentation.getEAAVersion());
        assertEquals("org.iso.18013.5.1.mDL", eaaPresentation.getEAADocumentType());
        assertEquals(DSSUtils.parseRFCDate("2020-10-01T13:30:02Z"), eaaPresentation.getSignatureCreatedAt());
        assertEquals(DSSUtils.parseRFCDate("2020-10-01T13:30:02Z"), eaaPresentation.getSignatureNotBefore());
        assertEquals(DSSUtils.parseRFCDate("2021-10-01T13:30:02Z"), eaaPresentation.getSignatureExpirationTime());
        assertEquals("Doe", eaaPresentation.getHolderLastName());
        assertEquals("123456789", eaaPresentation.getDocumentNumber());
        assertTrue(Utils.isArrayNotEmpty(eaaPresentation.getHolderPortrait()));
        assertNotNull(eaaPresentation.getHolderDrivingPrivileges());
        assertEquals(2, eaaPresentation.getHolderDrivingPrivileges().getDrivingPrivileges().size());

        List<ClaimWrapper> selectivelyDisclosableClaims = eaaPresentation.getSelectivelyDisclosableClaims();
        assertEquals(6, selectivelyDisclosableClaims.size());

        List<ClaimWrapper> payloadClaims = eaaPresentation.getAllEAAPayloadClaims();
        assertNotNull(payloadClaims);

        boolean expiryDateClaimFound = false;
        boolean issueDateClaimFound = false;
        boolean deviceKeyInfoClaimFound = false;
        boolean versionClaimFound = false;
        boolean docTypeClaimFound = false;
        boolean validityInfoClaimFound = false;
        boolean familyNameClaimFound = false;
        boolean documentNumberClaimFound = false;
        boolean portraitClaimFound = false;
        boolean drivingPrivilegesClaimFound = false;
        for (ClaimWrapper disclosableClaim : payloadClaims) {
            if ("expiry_date".equals(disclosableClaim.getName())) {
                assertNotNull(disclosableClaim.getDateTime());
                assertEquals("2024-10-20T00:00:00Z", disclosableClaim.getDisplayValue());
                assertTrue(disclosableClaim.isSelectivelyDisclosable());
                assertEquals("org.iso.18013.5.1", disclosableClaim.getNamespace());
                expiryDateClaimFound = true;

            } else if ("issue_date".equals(disclosableClaim.getName())) {
                assertNotNull(disclosableClaim.getDateTime());
                assertEquals("2019-10-20T00:00:00Z", disclosableClaim.getDisplayValue());
                assertTrue(disclosableClaim.isSelectivelyDisclosable());
                assertEquals("org.iso.18013.5.1", disclosableClaim.getNamespace());
                issueDateClaimFound = true;

            } else if ("deviceKeyInfo".equals(disclosableClaim.getName())) {
                assertNotNull(disclosableClaim.getMap());
                assertEquals("{\"deviceKey\": {\"1\": 2, \"-1\": 1, \"-2\": ljE9bGPiTjNydCv9saM7osiX3NaKuMdT5PvUjcprf5o=, " +
                        "\"-3\": H7Mmnt1BiFfeGzmk5KRLkvpITKpyLCKCiPAdDAOiw9Y=}}", disclosableClaim.getDisplayValue());
                assertFalse(disclosableClaim.isSelectivelyDisclosable());
                assertNull(disclosableClaim.getNamespace());
                assertInstanceOf(DeviceKeyClaimWrapper.class, disclosableClaim);
                deviceKeyInfoClaimFound = true;

            } else if ("version".equals(disclosableClaim.getName())) {
                assertNotNull(disclosableClaim.getText());
                assertEquals("1.0", disclosableClaim.getDisplayValue());
                assertFalse(disclosableClaim.isSelectivelyDisclosable());
                assertNull(disclosableClaim.getNamespace());
                versionClaimFound = true;

            } else if ("docType".equals(disclosableClaim.getName())) {
                assertNotNull(disclosableClaim.getText());
                assertEquals("org.iso.18013.5.1.mDL", disclosableClaim.getDisplayValue());
                assertFalse(disclosableClaim.isSelectivelyDisclosable());
                assertNull(disclosableClaim.getNamespace());
                docTypeClaimFound = true;

            } else if ("validityInfo".equals(disclosableClaim.getName())) {
                assertNotNull(disclosableClaim.getMap());
                assertEquals("{\"signed\": \"2020-10-01T13:30:02Z\", \"validUntil\": \"2021-10-01T13:30:02Z\", " +
                        "\"validFrom\": \"2020-10-01T13:30:02Z\"}", disclosableClaim.getDisplayValue());
                assertFalse(disclosableClaim.isSelectivelyDisclosable());
                assertNull(disclosableClaim.getNamespace());
                ValidityInfoClaimWrapper validityInfoClaimWrapper = assertInstanceOf(ValidityInfoClaimWrapper.class, disclosableClaim);
                assertNotNull(validityInfoClaimWrapper.getSigned().getDateTime());
                assertEquals("2020-10-01T13:30:02Z", validityInfoClaimWrapper.getSigned().getDisplayValue());
                assertNotNull(validityInfoClaimWrapper.getValidFrom().getDateTime());
                assertEquals("2020-10-01T13:30:02Z", validityInfoClaimWrapper.getValidFrom().getDisplayValue());
                assertNotNull(validityInfoClaimWrapper.getValidUntil().getDateTime());
                assertEquals("2021-10-01T13:30:02Z", validityInfoClaimWrapper.getValidUntil().getDisplayValue());
                validityInfoClaimFound = true;

            } else if ("family_name".equals(disclosableClaim.getName())) {
                assertNotNull(disclosableClaim.getText());
                assertEquals("Doe", disclosableClaim.getDisplayValue());
                assertTrue(disclosableClaim.isSelectivelyDisclosable());
                assertEquals("org.iso.18013.5.1", disclosableClaim.getNamespace());
                familyNameClaimFound = true;

            } else if ("document_number".equals(disclosableClaim.getName())) {
                assertNotNull(disclosableClaim.getText());
                assertEquals("123456789", disclosableClaim.getDisplayValue());
                assertTrue(disclosableClaim.isSelectivelyDisclosable());
                assertEquals("org.iso.18013.5.1", disclosableClaim.getNamespace());
                documentNumberClaimFound = true;

            } else if ("portrait".equals(disclosableClaim.getName())) {
                assertTrue(Utils.isArrayNotEmpty(disclosableClaim.getBinary()));
                assertTrue(disclosableClaim.isSelectivelyDisclosable());
                assertEquals("org.iso.18013.5.1", disclosableClaim.getNamespace());
                portraitClaimFound = true;

            } else if ("driving_privileges".equals(disclosableClaim.getName())) {
                assertTrue(Utils.isCollectionNotEmpty(disclosableClaim.getList()));
                assertEquals("{\"issue_date\": \"2018-08-09T00:00:00Z\", \"vehicle_category_code\": \"A\", " +
                        "\"expiry_date\": \"2024-10-20T00:00:00Z\"}, {\"issue_date\": \"2017-02-23T00:00:00Z\", " +
                        "\"vehicle_category_code\": \"B\", \"expiry_date\": \"2024-10-20T00:00:00Z\"}", disclosableClaim.getDisplayValue());
                assertTrue(disclosableClaim.isSelectivelyDisclosable());
                assertEquals("org.iso.18013.5.1", disclosableClaim.getNamespace());
                DrivingPrivilegesClaimWrapper drivingPrivilegesClaimWrapper = assertInstanceOf(DrivingPrivilegesClaimWrapper.class, disclosableClaim);
                assertEquals(2, drivingPrivilegesClaimWrapper.getDrivingPrivileges().size());
                DrivingPrivilegeClaimWrapper firstDrivingPrivilege = drivingPrivilegesClaimWrapper.getDrivingPrivileges().get(0);
                assertNotNull(firstDrivingPrivilege.getVehicleCategoryCode());
                assertEquals("A", firstDrivingPrivilege.getVehicleCategoryCode().getDisplayValue());
                assertNotNull(firstDrivingPrivilege.getIssueDate());
                assertEquals("2018-08-09T00:00:00Z", firstDrivingPrivilege.getIssueDate().getDisplayValue());
                assertNotNull(firstDrivingPrivilege.getExpiryDate());
                assertEquals("2024-10-20T00:00:00Z", firstDrivingPrivilege.getExpiryDate().getDisplayValue());
                assertNull(firstDrivingPrivilege.getCodes());
                DrivingPrivilegeClaimWrapper secondDrivingPrivilege = drivingPrivilegesClaimWrapper.getDrivingPrivileges().get(1);
                assertNotNull(secondDrivingPrivilege.getVehicleCategoryCode());
                assertEquals("B", secondDrivingPrivilege.getVehicleCategoryCode().getDisplayValue());
                assertNotNull(secondDrivingPrivilege.getIssueDate());
                assertEquals("2017-02-23T00:00:00Z", secondDrivingPrivilege.getIssueDate().getDisplayValue());
                assertNotNull(secondDrivingPrivilege.getExpiryDate());
                assertEquals("2024-10-20T00:00:00Z", secondDrivingPrivilege.getExpiryDate().getDisplayValue());
                assertNull(secondDrivingPrivilege.getCodes());
                drivingPrivilegesClaimFound = true;

            } else {
                fail(String.format("Not processed claim with name '%s'", disclosableClaim.getName()));
            }
        }
        assertTrue(expiryDateClaimFound);
        assertTrue(issueDateClaimFound);
        assertTrue(deviceKeyInfoClaimFound);
        assertTrue(versionClaimFound);
        assertTrue(docTypeClaimFound);
        assertTrue(validityInfoClaimFound);
        assertTrue(familyNameClaimFound);
        assertTrue(documentNumberClaimFound);
        assertTrue(portraitClaimFound);
        assertTrue(drivingPrivilegesClaimFound);
    }

    @Override
    protected boolean keyBindingPresent() {
        // TODO : add support
        return false;
    }

    @Override
    protected boolean orphanSelectivelyDisclosableClaimsPresent() {
        return true;
    }

    @Override
    protected void checkSigningCertificateValue(DiagnosticData diagnosticData) {
        SignatureWrapper signatureWrapper = diagnosticData.getSignatureById(diagnosticData.getFirstSignatureId());
        assertFalse(signatureWrapper.isSigningCertificateIdentified());
        assertFalse(signatureWrapper.isSigningCertificateReferencePresent());
        assertFalse(signatureWrapper.isSigningCertificateReferenceUnique());

        CertificateRefWrapper signingCertificateReference = signatureWrapper.getSigningCertificateReference();
        assertNull(signingCertificateReference);

        CertificateWrapper signingCertificate = signatureWrapper.getSigningCertificate();
        assertNotNull(signingCertificate);
    }

    @Override
    protected void checkSigningDate(DiagnosticData diagnosticData) {
        SignatureWrapper signatureWrapper = diagnosticData.getSignatureById(diagnosticData.getFirstSignatureId());
        assertNull(signatureWrapper.getClaimedSigningTime());
    }

}
