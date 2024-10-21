package eu.europa.esig.dss.cbades.validation;

import eu.europa.esig.dss.cbades.COSESignatureContext;
import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.SignatureWrapper;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.FileDocument;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CBAdESLevelBCoseSignCountersignatureInArrayRfc8152Test extends AbstractCBAdESTestValidation {

    @Override
    protected DSSDocument getSignedDocument() {
        return new FileDocument("src/test/resources/validation/cb-ades-cosesign-countersignature-rfc8152-array.cose");
    }

    @Override
    protected void checkStructureValidation(DiagnosticData diagnosticData) {
        super.checkStructureValidation(diagnosticData);

        List<SignatureWrapper> signatures = diagnosticData.getSignatures();
        assertEquals(3, signatures.size());

        int masterSigCounter = 0;
        int counterSigCounter = 0;
        for (SignatureWrapper signatureWrapper : signatures) {
            if (!signatureWrapper.isCounterSignature()) {
                assertEquals(COSESignatureContext.COSE_SIGN, COSESignatureContext.forLabel(signatureWrapper.getSignatureStructureType()));
                ++masterSigCounter;
            } else {
                assertEquals(COSESignatureContext.COSE_COUNTER_SIGNATURE, COSESignatureContext.forLabel(signatureWrapper.getSignatureStructureType()));
                ++counterSigCounter;
            }
        }
        assertEquals(1, masterSigCounter);
        assertEquals(2, counterSigCounter);
    }

}
