package eu.europa.esig.dss.cbades.validation;

import eu.europa.esig.dss.cbades.COSESignatureContext;
import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.SignatureWrapper;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.FileDocument;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CBAdESLevelBCoseSignCountersignatureV2Rfc9338Test extends AbstractCBAdESTestValidation {

    @Override
    protected DSSDocument getSignedDocument() {
        return new FileDocument("src/test/resources/validation/cb-ades-cosesign-countersignatureV2-rfc9338.cose");
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
                assertEquals(COSESignatureContext.COSE_SIGN, COSESignatureContext.forLabel(signatureWrapper.getSignatureStructureType()));
                masterSigFound = true;
            } else {
                assertEquals(COSESignatureContext.COSE_COUNTER_SIGNATURE_V2, COSESignatureContext.forLabel(signatureWrapper.getSignatureStructureType()));
                counterSigFound = true;
            }
        }
        assertTrue(masterSigFound);
        assertTrue(counterSigFound);
    }

}
