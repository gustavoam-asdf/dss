package eu.europa.esig.dss.eaa.common.validation;

import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.EAAPresentationWrapper;
import eu.europa.esig.dss.enumerations.EAAPresentationType;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.spi.eaa.EAAPresentation;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import eu.europa.esig.dss.test.validation.AbstractDocumentTestValidation;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.validation.SignedDocumentValidator;
import eu.europa.esig.dss.validation.eaa.EAAPresentationValidator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    }

    protected int expectedSignaturesCount() {
        return 1;
    }

    protected boolean disclosuresPresent() {
        return true;
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

}
