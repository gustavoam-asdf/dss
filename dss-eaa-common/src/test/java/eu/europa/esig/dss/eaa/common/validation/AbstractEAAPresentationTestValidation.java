package eu.europa.esig.dss.eaa.common.validation;

import eu.europa.esig.dss.detailedreport.DetailedReport;
import eu.europa.esig.dss.detailedreport.jaxb.XmlEAAPresentation;
import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.EAAPresentationWrapper;
import eu.europa.esig.dss.diagnostic.claim.ClaimWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDigestMatcher;
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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
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
        List<EAAPresentation> eaaPresentations = eaaValidator.getEAAPresentations();
        assertEquals(1, Utils.collectionSize(eaaPresentations));

        EAAPresentation eaaPresentation = eaaPresentations.get(0);
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
        assertEquals(getEAAPresentationType(), eaaPresentation.getEAAType());
        assertEquals(EAAPresentationType.ISO_IEC_MDOC == eaaPresentation.getEAAType(), eaaPresentation.getEAADocumentType() != null);

        checkEAAPresentationDigestMatchers(diagnosticData);
        checkClaims(diagnosticData);
    }

    protected void checkEAAPresentationDigestMatchers(DiagnosticData diagnosticData) {
        for (EAAPresentationWrapper eaaPresentation : diagnosticData.getEAAPresentations()) {
            boolean namespaceFound = false;
            for (XmlDigestMatcher digestMatcher : eaaPresentation.getDigestMatchers()) {
                if (orphanSelectivelyDisclosableClaimsPresent() && DigestMatcherType.EAA_ORPHAN_SELECTIVELY_DISCLOSABLE_CLAIM == digestMatcher.getType()) {
                    assertFalse(digestMatcher.isDataFound());
                    assertFalse(digestMatcher.isDataIntact());
                } else {
                    assertTrue(DigestMatcherType.EAA_DISCLOSURE == digestMatcher.getType() || DigestMatcherType.EAA_NESTED_DISCLOSURE == digestMatcher.getType());
                    assertTrue(digestMatcher.isDataFound());
                    assertTrue(digestMatcher.isDataIntact());
                    assertNotNull(digestMatcher.getDisclosableClaim());
                    namespaceFound |= digestMatcher.getDisclosableClaim().getNamespace() != null;
                }
            }
            assertEquals(disclosuresPresent() && EAAPresentationType.ISO_IEC_MDOC == eaaPresentation.getEAAType(), namespaceFound);
        }
    }

    protected void checkClaims(DiagnosticData diagnosticData) {
        for (EAAPresentationWrapper eaaPresentation : diagnosticData.getEAAPresentations()) {
            List<ClaimWrapper> eaaPayloadClaims = eaaPresentation.getAllEAAPayloadClaims();
            assertTrue(Utils.isCollectionNotEmpty(eaaPayloadClaims));

            boolean disclosedClaimFound = false;
            boolean namespaceFound = false;
            for (ClaimWrapper claimWrapper : eaaPayloadClaims) {
                assertNotNull(claimWrapper.getName());

                assertTrue(claimWrapper.getText() != null || claimWrapper.getDateTime() != null ||
                        claimWrapper.getNumber() != null || Utils.isCollectionNotEmpty(claimWrapper.getList()) ||
                        claimWrapper.getBoolean() != null || claimWrapper.getBinary() != null || claimWrapper.getMap() != null);
                assertNotEquals(claimWrapper.getText() != null, claimWrapper.getDateTime() != null ||
                        claimWrapper.getNumber() != null || Utils.isCollectionNotEmpty(claimWrapper.getList()) ||
                        claimWrapper.getBoolean() != null || claimWrapper.getBinary() != null || claimWrapper.getMap() != null);
                assertNotEquals(claimWrapper.getDateTime() != null, claimWrapper.getText() != null ||
                        claimWrapper.getNumber() != null || Utils.isCollectionNotEmpty(claimWrapper.getList()) ||
                        claimWrapper.getBoolean() != null || claimWrapper.getBinary() != null || claimWrapper.getMap() != null);
                assertNotEquals(claimWrapper.getNumber() != null, claimWrapper.getText() != null ||
                        claimWrapper.getDateTime() != null || Utils.isCollectionNotEmpty(claimWrapper.getList()) ||
                        claimWrapper.getBoolean() != null || claimWrapper.getBinary() != null || claimWrapper.getMap() != null);
                assertNotEquals(Utils.isCollectionNotEmpty(claimWrapper.getList()), claimWrapper.getText() != null ||
                        claimWrapper.getDateTime() != null || claimWrapper.getNumber() != null ||
                        claimWrapper.getBoolean() != null || claimWrapper.getBinary() != null || claimWrapper.getMap() != null);
                assertNotEquals(claimWrapper.getBoolean() != null, claimWrapper.getText() != null ||
                        claimWrapper.getDateTime() != null || Utils.isCollectionNotEmpty(claimWrapper.getList()) ||
                        claimWrapper.getNumber() != null || claimWrapper.getBinary() != null || claimWrapper.getMap() != null);
                assertNotEquals(claimWrapper.getBinary() != null, claimWrapper.getText() != null ||
                        claimWrapper.getDateTime() != null || Utils.isCollectionNotEmpty(claimWrapper.getList()) ||
                        claimWrapper.getNumber() != null || claimWrapper.getBoolean() != null || claimWrapper.getMap() != null);
                assertNotEquals(claimWrapper.getMap() != null, claimWrapper.getText() != null ||
                        claimWrapper.getDateTime() != null || Utils.isCollectionNotEmpty(claimWrapper.getList()) ||
                        claimWrapper.getNumber() != null || claimWrapper.getBoolean() != null || claimWrapper.getBinary() != null);

                assertTrue(claimWrapper.isText() || claimWrapper.isDateTime() ||
                        claimWrapper.isNumber() || claimWrapper.isList() ||
                        claimWrapper.isBoolean() || claimWrapper.isBinary() || claimWrapper.isMap());
                assertNotEquals(claimWrapper.isText(), claimWrapper.isDateTime() ||
                        claimWrapper.isNumber() || claimWrapper.isList() ||
                        claimWrapper.isBoolean() || claimWrapper.isBinary() || claimWrapper.isMap());
                assertNotEquals(claimWrapper.isDateTime(), claimWrapper.isText() ||
                        claimWrapper.isNumber() || claimWrapper.isList() ||
                        claimWrapper.isBoolean() || claimWrapper.isBinary() || claimWrapper.isMap());
                assertNotEquals(claimWrapper.isNumber(), claimWrapper.isText() ||
                        claimWrapper.isDateTime() || claimWrapper.isList() ||
                        claimWrapper.isBoolean() || claimWrapper.isBinary() || claimWrapper.isMap());
                assertNotEquals(claimWrapper.isList(), claimWrapper.isText() ||
                        claimWrapper.isDateTime() || claimWrapper.isNumber() ||
                        claimWrapper.isBoolean() || claimWrapper.isBinary() || claimWrapper.isMap());
                assertNotEquals(claimWrapper.isBoolean(), claimWrapper.isText() ||
                        claimWrapper.isDateTime() || claimWrapper.isList() ||
                        claimWrapper.isNumber() || claimWrapper.isBinary() || claimWrapper.isMap());
                assertNotEquals(claimWrapper.isBinary(), claimWrapper.isText() ||
                        claimWrapper.isDateTime() || claimWrapper.isList() ||
                        claimWrapper.isNumber() || claimWrapper.isBoolean() || claimWrapper.isMap());
                assertNotEquals(claimWrapper.isMap(), claimWrapper.isText() ||
                        claimWrapper.isDateTime() || claimWrapper.isList() ||
                        claimWrapper.isNumber() || claimWrapper.isBoolean() || claimWrapper.isBinary());
                
                disclosedClaimFound |= Utils.isTrue(claimWrapper.isSelectivelyDisclosable());

                ClaimWrapper claimByHeaderName = eaaPresentation.getClaimByHeaderName(claimWrapper.getName());
                assertNotNull(claimByHeaderName);
                assertEquals(claimByHeaderName.getName(), claimWrapper.getName());
                assertEquals(claimByHeaderName.getNamespace(), claimWrapper.getNamespace());
                assertEquals(claimByHeaderName.getText(), claimWrapper.getText());
                assertEquals(claimByHeaderName.getNumber(), claimWrapper.getNumber());
                assertEquals(claimByHeaderName.getBoolean(), claimWrapper.getBoolean());
                assertArrayEquals(claimByHeaderName.getBinary(), claimWrapper.getBinary());
                if (claimWrapper.getDateTime() != null) {
                    assertNotNull(claimByHeaderName.getDateTime());
                    assertEquals(0, claimWrapper.getDateTime().compareTo(claimByHeaderName.getDateTime()));
                } else {
                    assertNull(claimByHeaderName.getDateTime());
                }
                if (claimWrapper.getList() != null) {
                    assertEquals(claimWrapper.getList().size(), claimByHeaderName.getList().size());
                } else {
                    assertNull(claimByHeaderName.getList());
                }
                if (claimWrapper.getMap() != null) {
                    assertNotNull(claimByHeaderName.getMap());
                    assertEquals(claimWrapper.getMap(), claimByHeaderName.getMap());
                } else {
                    assertNull(claimByHeaderName.getMap());
                }

                assertTrue(Utils.isStringNotEmpty(claimWrapper.getDisplayValue()));

                namespaceFound |= claimWrapper.getNamespace() != null;
            }
            assertEquals(disclosuresPresent(), disclosedClaimFound);
            assertEquals(disclosuresPresent(), Utils.isCollectionNotEmpty(eaaPresentation.getSelectivelyDisclosableClaims()));
            assertEquals(disclosuresPresent() && EAAPresentationType.ISO_IEC_MDOC == eaaPresentation.getEAAType(),
                    namespaceFound);
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
