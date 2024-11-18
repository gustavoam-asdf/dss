package eu.europa.esig.dss.cbades.extension;

import eu.europa.esig.dss.cbades.signature.AbstractCBAdESTestSignature;
import eu.europa.esig.dss.cbades.signature.CBAdESConverter;
import eu.europa.esig.dss.cbades.signature.CBAdESService;
import eu.europa.esig.dss.cbades.signature.CBAdESSignatureParameters;
import eu.europa.esig.dss.cbades.signature.CBAdESTimestampParameters;
import eu.europa.esig.dss.enumerations.COSEStructureType;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.signature.DocumentSignatureService;
import eu.europa.esig.dss.spi.exception.IllegalInputException;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CBAdESExtensionTClearToLTBtsrTest extends AbstractCBAdESTestSignature {

    private DocumentSignatureService<CBAdESSignatureParameters, CBAdESTimestampParameters> service;
    private DSSDocument documentToSign;
    private CBAdESSignatureParameters signatureParameters;

    @BeforeEach
    void init() {
        service = new CBAdESService(getCompleteCertificateVerifier());
        service.setTspSource(getGoodTsa());

        documentToSign = new InMemoryDocument("Hello world!".getBytes());
        signatureParameters = new CBAdESSignatureParameters();
        signatureParameters.setSigningCertificate(getSigningCert());
        signatureParameters.setCertificateChain(getCertificateChain());
        signatureParameters.setSignaturePackaging(SignaturePackaging.ENVELOPING);
        signatureParameters.setCoseStructureType(COSEStructureType.COSE_SIGN);
        signatureParameters.setSignatureLevel(SignatureLevel.CB_AdES_BASELINE_T);
        signatureParameters.setCborBtsrWrappedComponents(false);
    }

    @Override
    protected DSSDocument sign() {
        DSSDocument signedDocument = super.sign();

        signatureParameters.setCborBtsrWrappedComponents(true);
        signatureParameters.setSignatureLevel(SignatureLevel.CB_AdES_BASELINE_LT);

        Exception exception = assertThrows(IllegalInputException.class,
                () -> service.extendDocument(signedDocument, signatureParameters));
        assertEquals("Extension is not possible! The encoding of 'uHeaders' components shall match! " +
                        "Use cbadesSignatureParameters.setCborByteStringWrapperComponents(false)",
                exception.getMessage());

        DSSDocument convertedDocument = CBAdESConverter.fromUHeadersWithClearToBtsrIncorporation(signedDocument);

        DSSDocument extendedDocument = service.extendDocument(convertedDocument, signatureParameters);
        assertNotNull(extendedDocument);
        return extendedDocument;
    }

    @Override
    protected DSSDocument getDocumentToSign() {
        return documentToSign;
    }

    @Override
    protected DocumentSignatureService<CBAdESSignatureParameters, CBAdESTimestampParameters> getService() {
        return service;
    }

    @Override
    protected CBAdESSignatureParameters getSignatureParameters() {
        return signatureParameters;
    }

    @Override
    protected String getSigningAlias() {
        return GOOD_USER;
    }

}
