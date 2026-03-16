package eu.europa.esig.dss.cbades.extension;

import eu.europa.esig.dss.alert.exception.AlertException;
import eu.europa.esig.dss.cbades.signature.CBAdESService;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.spi.validation.CertificateVerifier;
import eu.europa.esig.dss.spi.x509.CommonTrustedCertificateSource;
import eu.europa.esig.dss.spi.x509.tsp.TSPSource;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CBAdESExtensionTToLTANotTrustedTSPTest extends AbstractCBAdESTestExtension {

    @Override
    protected SignatureLevel getOriginalSignatureLevel() {
        return SignatureLevel.CB_AdES_BASELINE_T;
    }

    @Override
    protected SignatureLevel getFinalSignatureLevel() {
        return SignatureLevel.CB_AdES_BASELINE_LTA;
    }

    @Override
    protected TSPSource getUsedTSPSourceAtSignatureTime() {
        return getSHA3GoodTsa();
    }

    @Override
    protected CBAdESService getSignatureServiceToExtend() {
        CertificateVerifier certificateVerifier = getCompleteCertificateVerifier();

        CommonTrustedCertificateSource trustedCertificateSource = new CommonTrustedCertificateSource();
        trustedCertificateSource.importAsTrusted(getGoodPKITrustAnchors());
        certificateVerifier.setTrustedCertSources(trustedCertificateSource);

        CBAdESService service = new CBAdESService(certificateVerifier);
        service.setTspSource(getUsedTSPSourceAtExtensionTime());

        return service;
    }

    @Test
    @Override
    public void extendAndVerify() throws Exception {
        Exception exception = assertThrows(AlertException.class, super::extendAndVerify);
        assertTrue(exception.getMessage().contains("Revocation data is missing for one or more certificate(s)."));
        assertTrue(exception.getMessage().contains("Revocation data is skipped for untrusted certificate chain!"));
    }

}
