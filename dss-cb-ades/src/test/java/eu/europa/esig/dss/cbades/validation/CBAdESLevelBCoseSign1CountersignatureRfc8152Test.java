package eu.europa.esig.dss.cbades.validation;

import eu.europa.esig.dss.cbades.COSESignatureContext;
import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.SignatureWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDigestMatcher;
import eu.europa.esig.dss.enumerations.DigestMatcherType;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.FileDocument;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CBAdESLevelBCoseSign1CountersignatureRfc8152Test extends AbstractCBAdESTestValidation {

    @Override
    protected DSSDocument getSignedDocument() {
        return new FileDocument("src/test/resources/validation/cb-ades-cosesign1-countersignature-rfc8152.cose");
    }

    @Override
    protected void checkNumberOfSignatures(DiagnosticData diagnosticData) {
        super.checkNumberOfSignatures(diagnosticData);
        assertEquals(2, diagnosticData.getSignatures().size());
    }

    @Override
    protected void checkBLevelValid(DiagnosticData diagnosticData) {
        List<SignatureWrapper> signatures = diagnosticData.getSignatures();
        assertEquals(2, signatures.size());

        boolean masterSigFound = false;
        boolean counterSigFound = false;
        for (SignatureWrapper signatureWrapper : signatures) {
            if (!signatureWrapper.isCounterSignature()) {
                assertTrue(signatureWrapper.isSignatureIntact());
                assertTrue(signatureWrapper.isSignatureValid());
                assertTrue(diagnosticData.isBLevelTechnicallyValid(signatureWrapper.getId()));
                masterSigFound = true;
            } else {
                assertTrue(signatureWrapper.isSignatureIntact());
                assertFalse(signatureWrapper.isSignatureValid());
                assertFalse(diagnosticData.isBLevelTechnicallyValid(signatureWrapper.getId()));

                List<XmlDigestMatcher> digestMatchers = signatureWrapper.getDigestMatchers();
                assertEquals(2, digestMatchers.size());

                boolean coseInputDMFound = false;
                boolean counterSigDMFound = false;
                for (XmlDigestMatcher xmlDigestMatcher : digestMatchers) {
                    if (DigestMatcherType.COSE_SIG_STRUCTURE == xmlDigestMatcher.getType()) {
                        assertTrue(xmlDigestMatcher.isDataFound());
                        assertTrue(xmlDigestMatcher.isDataIntact());
                        coseInputDMFound = true;
                    } else if (DigestMatcherType.COUNTER_SIGNED_SIGNATURE_VALUE == xmlDigestMatcher.getType()) {
                        // counter-signature is not covered by RFC 8152 counter-signature
                        assertTrue(xmlDigestMatcher.isDataFound());
                        assertFalse(xmlDigestMatcher.isDataIntact());
                        counterSigDMFound = true;
                    }
                }
                assertTrue(coseInputDMFound);
                assertTrue(counterSigDMFound);

                counterSigFound = true;
            }
        }
        assertTrue(masterSigFound);
        assertTrue(counterSigFound);
    }

    @Override
    protected void checkStructureValidation(DiagnosticData diagnosticData) {
        super.checkStructureValidation(diagnosticData);

        List<SignatureWrapper> signatures = diagnosticData.getSignatures();
        assertEquals(2, signatures.size());

        boolean masterSigFound = false;
        boolean counterSigFound = false;
        for (SignatureWrapper signatureWrapper : signatures) {
            if (!signatureWrapper.isCounterSignature()) {
                assertEquals(COSESignatureContext.COSE_SIGN1, COSESignatureContext.forLabel(signatureWrapper.getSignatureStructureType()));
                masterSigFound = true;
            } else {
                assertEquals(COSESignatureContext.COSE_COUNTER_SIGNATURE, COSESignatureContext.forLabel(signatureWrapper.getSignatureStructureType()));
                counterSigFound = true;
            }
        }
        assertTrue(masterSigFound);
        assertTrue(counterSigFound);
    }

    @Override
    protected void checkSignatureLevel(DiagnosticData diagnosticData) {
        super.checkSignatureLevel(diagnosticData);

        List<SignatureWrapper> signatures = diagnosticData.getSignatures();
        assertEquals(2, signatures.size());
        for (SignatureWrapper signatureWrapper : signatures) {
            assertEquals(SignatureLevel.CB_AdES_BASELINE_B, signatureWrapper.getSignatureFormat());
        }
    }

}
