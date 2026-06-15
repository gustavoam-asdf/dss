package eu.europa.esig.dss.cbades.extension;

import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.FileDocument;
import eu.europa.esig.dss.spi.exception.IllegalInputException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CBAdESExtensionTClearUHeadersToTTest extends AbstractCBAdESTestExtension {

    @Override
    protected DSSDocument getSignedDocument(DSSDocument doc) {
        return new FileDocument("src/test/resources/validation/cb-ades-cosesign1-level-t-clear-uheaders.cose");
    }

    @Override
    protected SignatureLevel getOriginalSignatureLevel() {
        return SignatureLevel.CB_AdES_BASELINE_B;
    }

    @Override
    protected SignatureLevel getFinalSignatureLevel() {
        return SignatureLevel.CB_AdES_BASELINE_T;
    }

    @Test
    @Override
    public void extendAndVerify() {
        Exception exception = assertThrows(IllegalInputException.class, super::extendAndVerify);
        assertEquals("Extension is not possible! The members of 'uHeaders' component shall be represented by CBOR byte strings.", exception.getMessage());
    }

}
