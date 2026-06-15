package eu.europa.esig.dss.cbades.validation;

import eu.europa.esig.dss.enumerations.COSESignatureType;
import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.SignatureWrapper;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.FileDocument;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CBAdESLevelBCoseSign1CountersignatureV2InArrayRfc9338UnsignedPropsTest extends AbstractCBAdESTestValidation {

    @Override
    protected DSSDocument getSignedDocument() {
        return new FileDocument("src/test/resources/validation/cb-ades-cosesign1-countersignatureV2-rfc9338-unsigned-props-array.cose");
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
                assertEquals(COSESignatureType.COSE_SIGN1, signatureWrapper.getCOSESignatureType());
                ++masterSigCounter;
            } else {
                assertEquals(COSESignatureType.COSE_COUNTER_SIGNATURE_V2, signatureWrapper.getCOSESignatureType());
                ++counterSigCounter;
            }
        }
        assertEquals(1, masterSigCounter);
        assertEquals(2, counterSigCounter);
    }

}
