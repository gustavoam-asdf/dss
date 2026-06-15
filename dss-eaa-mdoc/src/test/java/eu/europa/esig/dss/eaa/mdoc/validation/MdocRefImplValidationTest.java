package eu.europa.esig.dss.eaa.mdoc.validation;

import eu.europa.esig.dss.diagnostic.CertificateRefWrapper;
import eu.europa.esig.dss.diagnostic.CertificateWrapper;
import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.SignatureWrapper;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.FileDocument;
import eu.europa.esig.validationreport.jaxb.SignatureIdentifierType;
import eu.europa.esig.validationreport.jaxb.SignerInformationType;
import eu.europa.esig.validationreport.jaxb.SignersDocumentType;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MdocRefImplValidationTest extends AbstractMdocEAAPresentationTestValidation {

    @Override
    protected DSSDocument getSignedDocument() {
        return new FileDocument("src/test/resources/validation/mdocRefImpl.mdoc");
    }

    @Override
    protected boolean orphanSelectivelyDisclosableClaimsPresent() {
        return true;
    }

    @Override
    protected void checkBLevelValid(DiagnosticData diagnosticData) {
        boolean eaaSignatureFound = false;
        boolean kbSignatureFound = false;
        for (SignatureWrapper signatureWrapper : diagnosticData.getSignatures()) {
            if (signatureWrapper.isKeyBindingSignature()) {
                assertFalse(signatureWrapper.isBLevelTechnicallyValid());
                assertFalse(signatureWrapper.isSignatureIntact());
                assertFalse(signatureWrapper.isSignatureValid());
                kbSignatureFound = true;
            } else {
                assertTrue(signatureWrapper.isBLevelTechnicallyValid());
                assertTrue(signatureWrapper.isSignatureIntact());
                assertTrue(signatureWrapper.isSignatureValid());
                eaaSignatureFound = true;
            }
        }
        assertTrue(eaaSignatureFound);
        assertTrue(kbSignatureFound);
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

    @Override
    protected void checkDTBSR(DiagnosticData diagnosticData) {
        boolean eaaSignatureFound = false;
        boolean kbSignatureFound = false;
        for (SignatureWrapper signatureWrapper : diagnosticData.getSignatures()) {
            if (signatureWrapper.isKeyBindingSignature()) {
                assertNull(signatureWrapper.getDataToBeSignedRepresentation());
                kbSignatureFound = true;
            } else {
                assertNotNull(signatureWrapper.getDataToBeSignedRepresentation());
                eaaSignatureFound = true;
            }
        }
        assertTrue(eaaSignatureFound);
        assertTrue(kbSignatureFound);
    }

    @Override
    protected void validateETSISignatureIdentifier(SignatureIdentifierType signatureIdentifier) {
        // skip
    }

    @Override
    protected void validateSignerInformation(SignerInformationType signerInformation) {
        // skip
    }

    @Override
    protected void validateETSISignersDocument(SignersDocumentType signersDocument) {
        // skip
    }

}
