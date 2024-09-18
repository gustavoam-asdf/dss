package eu.europa.esig.dss.cbades.signature;

import eu.europa.esig.dss.cbades.COSEParser;
import eu.europa.esig.dss.cbades.COSESign;
import eu.europa.esig.dss.cbades.COSESign1;
import eu.europa.esig.dss.cbades.COSESignStructure;
import eu.europa.esig.dss.cbades.validation.CBORSignature;
import eu.europa.esig.dss.enumerations.COSEStructureType;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.SigDMechanism;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.model.SignatureValue;
import eu.europa.esig.dss.model.ToBeSigned;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.spi.validation.CertificateVerifier;
import eu.europa.esig.dss.test.PKIFactoryAccess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CBAdESServiceTest extends PKIFactoryAccess {

    private static DSSDocument documentToSign;
    private static CertificateVerifier certificateVerifier;
    private static CBAdESService service;

    @BeforeEach
    void init() {
        documentToSign = new InMemoryDocument("Hello World!".getBytes());
        certificateVerifier = getCompleteCertificateVerifier();
        service = new CBAdESService(certificateVerifier);
        service.setTspSource(getGoodTsa());
    }

    @Test
    void createAndValidateCoseSignTest() {
        CBAdESSignatureParameters signatureParameters = initParameters();
        signatureParameters.setCoseStructureType(COSEStructureType.COSE_SIGN);

        DSSDocument signedDocument = sign(Collections.singletonList(documentToSign), signatureParameters);

        COSESignStructure coseSignStructure = new COSEParser(signedDocument).parse();
        assertInstanceOf(COSESign.class, coseSignStructure);
        List<CBORSignature> cborSignatures = CBORSignature.fromCOSESign((COSESign) coseSignStructure);
        assertEquals(1, cborSignatures.size());
        CBORSignature cborSignature = cborSignatures.get(0);
        cborSignature.setKey(getSigningCert().getPublicKey());
        assertTrue(cborSignature.verifySignature());
    }

    @Test
    void createAndValidateCoseSign1Test() {
        CBAdESSignatureParameters signatureParameters = initParameters();
        signatureParameters.setCoseStructureType(COSEStructureType.COSE_SIGN1);

        DSSDocument signedDocument = sign(Collections.singletonList(documentToSign), signatureParameters);

        COSESignStructure coseSignStructure = new COSEParser(signedDocument).parse();
        assertInstanceOf(COSESign1.class, coseSignStructure);
        COSESign1 coseSign1 = (COSESign1) coseSignStructure;
        assertTrue(coseSign1.isTagged());
        CBORSignature cborSignature = CBORSignature.fromCOSE1Sign(coseSign1);
        cborSignature.setKey(getSigningCert().getPublicKey());
        assertTrue(cborSignature.verifySignature());
    }

    @Test
    void parallelCoseSignTest() {
        CBAdESSignatureParameters signatureParameters = initParameters();
        signatureParameters.setCoseStructureType(COSEStructureType.COSE_SIGN);

        DSSDocument signedDocument = sign(Collections.singletonList(documentToSign), signatureParameters);

        COSESignStructure coseSignStructure = new COSEParser(signedDocument).parse();
        assertInstanceOf(COSESign.class, coseSignStructure);
        COSESign coseSign = (COSESign) coseSignStructure;
        assertTrue(coseSign.isTagged());

        awaitOneSecond();

        signatureParameters = initParameters();
        signatureParameters.setCoseStructureType(COSEStructureType.COSE_SIGN);

        DSSDocument doubleSignedDocument = sign(Collections.singletonList(signedDocument), signatureParameters);

        coseSignStructure = new COSEParser(doubleSignedDocument).parse();
        assertInstanceOf(COSESign.class, coseSignStructure);
        coseSign = (COSESign) coseSignStructure;
        assertTrue(coseSign.isTagged());

        List<CBORSignature> cborSignatures = CBORSignature.fromCOSESign(coseSign);
        assertEquals(2, cborSignatures.size());

        for (CBORSignature cborSignature : cborSignatures) {
            cborSignature.setKey(getSigningCert().getPublicKey());
            assertTrue(cborSignature.verifySignature());
        }
    }

    @Test
    void externallySuppliedDataCoseSign1Test() {
        CBAdESSignatureParameters signatureParameters = initParameters();
        signatureParameters.setCoseStructureType(COSEStructureType.COSE_SIGN1);

        DSSDocument externalDocument = new InMemoryDocument("Bye World!".getBytes());
        signatureParameters.setExternallySuppliedData(externalDocument);

        DSSDocument signedDocument = sign(Collections.singletonList(documentToSign), signatureParameters);

        COSESignStructure coseSignStructure = new COSEParser(signedDocument).parse();
        assertInstanceOf(COSESign1.class, coseSignStructure);

        CBORSignature cborSignature = CBORSignature.fromCOSE1Sign((COSESign1) coseSignStructure);
        cborSignature.setKey(getSigningCert().getPublicKey());
        assertFalse(cborSignature.verifySignature());

        cborSignature.setExternalAttributesBytes(DSSUtils.toByteArray(externalDocument));
        assertTrue(cborSignature.verifySignature());
    }

    @Test
    void detachedCoseSign1Test() {
        CBAdESSignatureParameters signatureParameters = initParameters();
        signatureParameters.setCoseStructureType(COSEStructureType.COSE_SIGN1);
        signatureParameters.setSignaturePackaging(SignaturePackaging.DETACHED);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                sign(Collections.singletonList(documentToSign), signatureParameters));
        assertEquals("The SigDMechanism is not defined for a detached signature! " +
                "Please use CBAdESSignatureParameters.setSigDMechanism(sigDMechanism) method.", exception.getMessage());

        signatureParameters.setSigDMechanism(SigDMechanism.NO_SIG_D);

        DSSDocument signedDocument = sign(Collections.singletonList(documentToSign), signatureParameters);

        COSESignStructure coseSignStructure = new COSEParser(signedDocument).parse();
        assertInstanceOf(COSESign1.class, coseSignStructure);

        CBORSignature cborSignature = CBORSignature.fromCOSE1Sign((COSESign1) coseSignStructure);
        cborSignature.setKey(getSigningCert().getPublicKey());
        assertFalse(cborSignature.verifySignature());

        cborSignature.setPayloadBytes(DSSUtils.toByteArray(documentToSign));
        assertTrue(cborSignature.verifySignature());
    }

    @Test
    void untaggedCoseSign1Test() {
        CBAdESSignatureParameters signatureParameters = initParameters();
        signatureParameters.setCoseStructureType(COSEStructureType.COSE_SIGN1);
        signatureParameters.setTagged(false);

        DSSDocument signedDocument = sign(Collections.singletonList(documentToSign), signatureParameters);

        COSESignStructure coseSignStructure = new COSEParser(signedDocument).parse();
        assertInstanceOf(COSESign1.class, coseSignStructure);
        COSESign1 coseSign1 = (COSESign1) coseSignStructure;
        assertFalse(coseSign1.isTagged());
        CBORSignature cborSignature = CBORSignature.fromCOSE1Sign(coseSign1);
        cborSignature.setKey(getSigningCert().getPublicKey());
        assertTrue(cborSignature.verifySignature());
    }

    @Test
    void untaggedParallelCoseSignTest() {
        CBAdESSignatureParameters signatureParameters = initParameters();
        signatureParameters.setCoseStructureType(COSEStructureType.COSE_SIGN);
        signatureParameters.setTagged(false);

        DSSDocument signedDocument = sign(Collections.singletonList(documentToSign), signatureParameters);

        COSESignStructure coseSignStructure = new COSEParser(signedDocument).parse();
        assertInstanceOf(COSESign.class, coseSignStructure);
        COSESign coseSign = (COSESign) coseSignStructure;
        assertFalse(coseSign.isTagged());

        awaitOneSecond();

        signatureParameters = initParameters();
        signatureParameters.setCoseStructureType(COSEStructureType.COSE_SIGN);
        signatureParameters.setTagged(true); // should be ignored

        DSSDocument doubleSignedDocument = sign(Collections.singletonList(signedDocument), signatureParameters);

        coseSignStructure = new COSEParser(doubleSignedDocument).parse();
        assertInstanceOf(COSESign.class, coseSignStructure);
        coseSign = (COSESign) coseSignStructure;
        assertFalse(coseSign.isTagged());

        List<CBORSignature> cborSignatures = CBORSignature.fromCOSESign(coseSign);
        assertEquals(2, cborSignatures.size());

        for (CBORSignature cborSignature : cborSignatures) {
            cborSignature.setKey(getSigningCert().getPublicKey());
            assertTrue(cborSignature.verifySignature());
        }
    }

    private CBAdESSignatureParameters initParameters() {
        CBAdESSignatureParameters signatureParameters = new CBAdESSignatureParameters();
        signatureParameters.setSigningCertificate(getSigningCert());
        signatureParameters.setCertificateChain(getCertificateChain());
        signatureParameters.setSignatureLevel(SignatureLevel.CB_AdES_BASELINE_B);
        signatureParameters.setSignaturePackaging(SignaturePackaging.ENVELOPING);
        signatureParameters.setDigestAlgorithm(DigestAlgorithm.SHA256);
        return signatureParameters;
    }

    private DSSDocument sign(List<DSSDocument> documentsToSign, CBAdESSignatureParameters signatureParameters) {
        ToBeSigned dataToSign = service.getDataToSign(documentsToSign, signatureParameters);
        SignatureValue signatureValue = getToken().sign(dataToSign, signatureParameters.getDigestAlgorithm(), getPrivateKeyEntry());
        return service.signDocument(documentsToSign, signatureParameters, signatureValue);
    }

    @Override
    protected String getSigningAlias() {
        return ECDSA_USER;
    }

}
