package eu.europa.esig.dss.cbades.extension;

import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.SignatureWrapper;
import eu.europa.esig.dss.diagnostic.TimestampWrapper;
import eu.europa.esig.dss.enumerations.SignatureLevel;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CBAdESExtensionTToTTest extends AbstractCBAdESTestExtension {

    @Override
    protected SignatureLevel getOriginalSignatureLevel() {
        return SignatureLevel.CB_AdES_BASELINE_T;
    }

    @Override
    protected SignatureLevel getFinalSignatureLevel() {
        return SignatureLevel.CB_AdES_BASELINE_T;
    }

    @Override
    protected void checkFinalLevel(DiagnosticData diagnosticData) {
        super.checkFinalLevel(diagnosticData);

        SignatureWrapper signature = diagnosticData.getSignatureById(diagnosticData.getFirstSignatureId());
        List<TimestampWrapper> signatureTimestamps = signature.getSignatureTimestamps();
        assertEquals(2, signatureTimestamps.size());
        for (TimestampWrapper timestampWrapper : signatureTimestamps) {
            assertTrue(timestampWrapper.isMessageImprintDataFound());
            assertTrue(timestampWrapper.isMessageImprintDataIntact());
            assertTrue(timestampWrapper.isSignatureValid());
        }
    }

}
