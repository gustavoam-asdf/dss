package eu.europa.esig.dss.eaa.common.validation;

import eu.europa.esig.dss.detailedreport.DetailedReport;
import eu.europa.esig.dss.detailedreport.jaxb.XmlEAAPresentation;
import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.EAAPresentationWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDigestMatcher;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDisclosableClaim;
import eu.europa.esig.dss.enumerations.DigestMatcherType;
import eu.europa.esig.dss.enumerations.EAAPresentationType;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.simplereport.SimpleReport;
import eu.europa.esig.dss.simplereport.jaxb.XmlSignature;
import eu.europa.esig.dss.spi.eaa.EAAPresentation;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import eu.europa.esig.dss.test.validation.AbstractDocumentTestValidation;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.validation.SignedDocumentValidator;
import eu.europa.esig.dss.validation.eaa.EAAPresentationValidator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class AbstractEAAPresentationTestValidation extends AbstractDocumentTestValidation {

    @Override
    protected SignedDocumentValidator getValidator(DSSDocument signedDocument) {
        SignedDocumentValidator validator = DefaultEAAPresentationValidator.fromDocument(signedDocument);
        validator.setCertificateVerifier(getOfflineCertificateVerifier());
        validator.setTokenExtractionStrategy(getTokenExtractionStrategy());
        validator.setSignaturePolicyProvider(getSignaturePolicyProvider());
        validator.setTokenIdentifierProvider(getTokenIdentifierProvider());
        validator.setSigningCertificateSource(getSigningCertificateSource());
        return validator;
    }

    @Override
    protected void checkValidationContext(SignedDocumentValidator validator) {
        super.checkValidationContext(validator);

        EAAPresentationValidator eaaValidator = assertInstanceOf(EAAPresentationValidator.class, validator);
        EAAPresentation eaaPresentation = eaaValidator.getEAAPresentation();
        assertNotNull(eaaPresentation);

        assertNotNull(eaaPresentation.getId());
        assertNotNull(eaaPresentation.getDSSId());

        assertEquals(expectedSignaturesCount(), eaaPresentation.getSignatures().size());
        assertEquals(disclosuresPresent(), Utils.isCollectionNotEmpty(eaaPresentation.getDisclosureValidations()));
        assertEquals(keyBindingPresent(), eaaPresentation.getKeyBindingSignature() != null);
        assertEquals(getEAAPresentationType(), eaaPresentation.getEAAPresentationType());
    }

    @Override
    protected void checkAdvancedSignatures(List<AdvancedSignature> signatures) {
        // skip
    }

    @Override
    protected void verifyDiagnosticData(DiagnosticData diagnosticData) {
        super.verifyDiagnosticData(diagnosticData);

        List<EAAPresentationWrapper> eaaPresentations = diagnosticData.getEAAPresentations();
        assertEquals(1, eaaPresentations.size());

        EAAPresentationWrapper eaaPresentation = eaaPresentations.get(0);
        assertNotNull(eaaPresentation.getId());
        assertEquals(expectedSignaturesCount(), eaaPresentation.getEAAPresentationSignatures().size());
        assertEquals(disclosuresPresent(), Utils.isCollectionNotEmpty(eaaPresentation.getDigestMatchers()));
        assertEquals(keyBindingPresent(), eaaPresentation.getKeyBindingSignature() != null);
        assertEquals(getEAAPresentationType(), eaaPresentation.getType());

        checkEAAPresentationDigestMatchers(diagnosticData);
        checkClaims(diagnosticData);
    }

    protected void checkEAAPresentationDigestMatchers(DiagnosticData diagnosticData) {
        for (EAAPresentationWrapper eaaPresentation : diagnosticData.getEAAPresentations()) {
            for (XmlDigestMatcher digestMatcher : eaaPresentation.getDigestMatchers()) {
                if (orphanSelectivelyDisclosableClaimsPresent() && DigestMatcherType.EAA_ORPHAN_SELECTIVELY_DISCLOSABLE_CLAIM == digestMatcher.getType()) {
                    assertFalse(digestMatcher.isDataFound());
                    assertFalse(digestMatcher.isDataIntact());
                } else {
                    assertTrue(DigestMatcherType.EAA_DISCLOSURE == digestMatcher.getType() || DigestMatcherType.EAA_NESTED_DISCLOSURE == digestMatcher.getType());
                    assertTrue(digestMatcher.isDataFound());
                    assertTrue(digestMatcher.isDataIntact());
                    assertNotNull(digestMatcher.getClaim());
                    assertNotNull(digestMatcher.getClaim().getName());
                }
            }
        }
    }

    protected void checkClaims(DiagnosticData diagnosticData) {
        for (EAAPresentationWrapper eaaPresentation : diagnosticData.getEAAPresentations()) {
            List<XmlDisclosableClaim> eaaPayloadClaims = eaaPresentation.getAllEAAPayloadClaims();
            assertTrue(Utils.isCollectionNotEmpty(eaaPayloadClaims));

            boolean disclosedClaimFound = false;
            for (XmlDisclosableClaim xmlDisclosableClaim : eaaPayloadClaims) {
                assertNotNull(xmlDisclosableClaim.getName());
                assertTrue(xmlDisclosableClaim.getValue() != null || xmlDisclosableClaim.getDateTime() != null ||
                        xmlDisclosableClaim.getNumber() != null || Utils.isCollectionNotEmpty(xmlDisclosableClaim.getItem()) ||
                        xmlDisclosableClaim.isBoolean() != null || xmlDisclosableClaim.getEncoded() != null);
                assertNotEquals(xmlDisclosableClaim.getValue() != null, xmlDisclosableClaim.getDateTime() != null ||
                        xmlDisclosableClaim.getNumber() != null || Utils.isCollectionNotEmpty(xmlDisclosableClaim.getItem()) ||
                        xmlDisclosableClaim.isBoolean() != null || xmlDisclosableClaim.getEncoded() != null);
                assertNotEquals(xmlDisclosableClaim.getDateTime() != null, xmlDisclosableClaim.getValue() != null ||
                        xmlDisclosableClaim.getNumber() != null || Utils.isCollectionNotEmpty(xmlDisclosableClaim.getItem()) ||
                        xmlDisclosableClaim.isBoolean() != null || xmlDisclosableClaim.getEncoded() != null);
                assertNotEquals(xmlDisclosableClaim.getNumber() != null, xmlDisclosableClaim.getValue() != null ||
                        xmlDisclosableClaim.getDateTime() != null || Utils.isCollectionNotEmpty(xmlDisclosableClaim.getItem()) ||
                        xmlDisclosableClaim.isBoolean() != null || xmlDisclosableClaim.getEncoded() != null);
                assertNotEquals(Utils.isCollectionNotEmpty(xmlDisclosableClaim.getItem()), xmlDisclosableClaim.getValue() != null ||
                        xmlDisclosableClaim.getDateTime() != null || xmlDisclosableClaim.getNumber() != null ||
                        xmlDisclosableClaim.isBoolean() != null || xmlDisclosableClaim.getEncoded() != null);
                assertNotEquals(xmlDisclosableClaim.isBoolean() != null, xmlDisclosableClaim.getValue() != null ||
                        xmlDisclosableClaim.getDateTime() != null || Utils.isCollectionNotEmpty(xmlDisclosableClaim.getItem()) ||
                        xmlDisclosableClaim.getNumber() != null || xmlDisclosableClaim.getEncoded() != null);
                assertNotEquals(xmlDisclosableClaim.getEncoded() != null, xmlDisclosableClaim.getValue() != null ||
                        xmlDisclosableClaim.getDateTime() != null || Utils.isCollectionNotEmpty(xmlDisclosableClaim.getItem()) ||
                        xmlDisclosableClaim.getNumber() != null || xmlDisclosableClaim.isBoolean() != null);
                disclosedClaimFound |= Utils.isTrue(xmlDisclosableClaim.isDisclosure());
            }
            assertEquals(disclosuresPresent(), disclosedClaimFound);
            assertEquals(disclosuresPresent(), Utils.isCollectionNotEmpty(eaaPresentation.getSelectivelyDisclosableClaims()));
        }
    }

    protected int expectedSignaturesCount() {
        return 1;
    }

    protected boolean disclosuresPresent() {
        return true;
    }

    protected boolean orphanSelectivelyDisclosableClaimsPresent() {
        return false;
    }

    protected boolean keyBindingPresent() {
        return true;
    }

    protected abstract EAAPresentationType getEAAPresentationType();

    @Override
    protected void checkOrphanTokens(DiagnosticData diagnosticData) {
        assertTrue(Utils.isCollectionEmpty(diagnosticData.getAllOrphanCertificateObjects()));
        // may include orphan certificate references (e.g. x5u)
        assertTrue(Utils.isCollectionEmpty(diagnosticData.getAllOrphanRevocationObjects()));
        assertTrue(Utils.isCollectionEmpty(diagnosticData.getAllOrphanRevocationReferences()));
    }

    @Override
    protected void verifyOriginalDocuments(SignedDocumentValidator validator, DiagnosticData diagnosticData) {
        // skip
    }

    @Override
    protected void verifyDetailedReport(DetailedReport detailedReport) {
        assertNotNull(detailedReport);

        int nbBBBs = detailedReport.getBasicBuildingBlocksNumber();
        for (int i = 0; i < nbBBBs; i++) {
            String id = detailedReport.getBasicBuildingBlocksSignatureId(i);
            assertNotNull(id);

            Indication indication = detailedReport.getBasicBuildingBlocksIndication(id);
            assertNotNull(indication);
            if (!Indication.PASSED.equals(indication)) {
                SubIndication subIndication = detailedReport.getBasicBuildingBlocksSubIndication(id);
                assertNotNull(subIndication);
            }
        }

        List<String> eaaPresentationIds = detailedReport.getEAAPresentationIds();
        for (String eaaId : eaaPresentationIds) {
            XmlEAAPresentation xmlEAAPresentation = detailedReport.getXmlEAAPresentationById(eaaId);
            assertNotNull(xmlEAAPresentation);

            Indication indication = detailedReport.getEAAPresentationValidationIndication(eaaId);
            assertNotNull(indication);
            if (!Indication.PASSED.equals(indication)) {
                SubIndication subIndication = detailedReport.getEAAPresentationValidationSubIndication(eaaId);
                assertNotNull(subIndication);
            }
        }
    }

    @Override
    protected void verifySimpleReport(SimpleReport simpleReport) {
        assertNotNull(simpleReport);

        List<String> eaaPresentationIdList = simpleReport.getEAAPresentationIdList();
        assertEquals(1, eaaPresentationIdList.size());

        assertEquals(eaaPresentationIdList.get(0), simpleReport.getFirstEAAPresentationId());

        String eaaPresentationId = simpleReport.getFirstEAAPresentationId();

        Indication indication = simpleReport.getIndication(eaaPresentationId);
        assertNotNull(indication);
        assertTrue(Indication.PASSED.equals(indication) || Indication.INDETERMINATE.equals(indication)
                || Indication.FAILED.equals(indication));
        if (Indication.PASSED.equals(indication)) {

            assertNull(simpleReport.getSubIndication(eaaPresentationId));
            assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationErrors(eaaPresentationId)));

        } else {
            SubIndication subIndication = simpleReport.getSubIndication(eaaPresentationId);
            assertNotNull(subIndication);
            assertFalse(Utils.isCollectionEmpty(simpleReport.getAdESValidationErrors(eaaPresentationId)));
        }
        assertNotNull(simpleReport.getEAAQualification(eaaPresentationId));

        List<XmlSignature> eaaPresentationSignatures = simpleReport.getEAAPresentationSignatures(eaaPresentationId);
        assertEquals(expectedSignaturesCount(), eaaPresentationSignatures.size());
        for (XmlSignature xmlSignature : eaaPresentationSignatures) {
            verifySimpleReportSignature(simpleReport, xmlSignature);
        }

        XmlSignature keyBindingSignature = simpleReport.getEAAPresentationKeyBindingSignature(eaaPresentationId);
        assertEquals(keyBindingPresent(), keyBindingSignature != null);
        if (keyBindingSignature != null) {
            verifySimpleReportSignature(simpleReport, keyBindingSignature);
        }
    }

    private void verifySimpleReportSignature(SimpleReport simpleReport, XmlSignature xmlSignature) {
        String sigId = xmlSignature.getId();

        Indication indication = simpleReport.getIndication(sigId);
        assertNotNull(indication);
        assertTrue(Indication.TOTAL_PASSED.equals(indication) || Indication.INDETERMINATE.equals(indication)
                || Indication.TOTAL_FAILED.equals(indication));
        if (Indication.TOTAL_PASSED.equals(indication)) {
            assertTrue(Utils.isCollectionNotEmpty(simpleReport.getSignatureScopes(sigId)));

            assertNull(simpleReport.getSubIndication(sigId));
            assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationErrors(sigId)));

            if (!createdWithTrustAnchor(simpleReport.getCertificateChain(sigId))
                    && !timestampedWithTrustAnchor(simpleReport.getSignatureTimestamps(sigId))
                    && !preservedByERWithTrustAnchor(simpleReport.getSignatureEvidenceRecords(sigId))) {
                assertNotNull(simpleReport.getExtensionPeriodMax(sigId));
            }

        } else {
            SubIndication subIndication = simpleReport.getSubIndication(sigId);
            assertNotNull(subIndication);
            assertFalse(Utils.isCollectionEmpty(simpleReport.getAdESValidationErrors(sigId)));
        }
        assertNotNull(simpleReport.getSignatureQualification(sigId));

        List<eu.europa.esig.dss.simplereport.jaxb.XmlTimestamp> signatureTimestamps = simpleReport.getSignatureTimestamps(sigId);
        for (eu.europa.esig.dss.simplereport.jaxb.XmlTimestamp xmlTimestamp : signatureTimestamps) {
            String tstId = xmlTimestamp.getId();
            assertNotNull(tstId);

            Indication timestampIndication = simpleReport.getIndication(tstId);
            assertNotNull(timestampIndication);
            assertTrue(Indication.PASSED.equals(timestampIndication) || Indication.INDETERMINATE.equals(timestampIndication)
                    || Indication.FAILED.equals(timestampIndication));
            if (timestampIndication != Indication.PASSED) {
                assertNotNull(simpleReport.getSubIndication(tstId));
                assertTrue(Utils.isCollectionNotEmpty(simpleReport.getAdESValidationErrors(tstId)));
            }
            assertNotNull(simpleReport.getTimestampQualification(tstId));
        }

    }

}
