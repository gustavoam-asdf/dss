package eu.europa.esig.dss.cbades.signature;

import eu.europa.esig.dss.cbades.COSEConstants;
import eu.europa.esig.dss.cbades.COSECounterSignStructure;
import eu.europa.esig.dss.cbades.COSECounterSignature;
import eu.europa.esig.dss.cbades.COSECounterSignatureParser;
import eu.europa.esig.dss.cbades.COSEParser;
import eu.europa.esig.dss.cbades.COSEProtectedHeader;
import eu.europa.esig.dss.cbades.COSESign1;
import eu.europa.esig.dss.cbades.COSESignStructure;
import eu.europa.esig.dss.cbades.COSESignatureContext;
import eu.europa.esig.dss.cbades.COSEStructure;
import eu.europa.esig.dss.cbades.COSEUnprotectedHeader;
import eu.europa.esig.dss.cbades.cbor.CBORArray;
import eu.europa.esig.dss.cbades.cbor.CBORByteString;
import eu.europa.esig.dss.cbades.cbor.CBORMap;
import eu.europa.esig.dss.cbades.cbor.CBORObject;
import eu.europa.esig.dss.cbades.cbor.CBORUtils;
import eu.europa.esig.dss.cbades.validation.CBAdESSignature;
import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.SignatureWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlCommitmentTypeIndication;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDigestAlgoAndValue;
import eu.europa.esig.dss.diagnostic.jaxb.XmlSignatureScope;
import eu.europa.esig.dss.enumerations.COSEStructureType;
import eu.europa.esig.dss.enumerations.CommitmentTypeEnum;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.enumerations.SignatureScopeType;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.model.SignerLocation;
import eu.europa.esig.dss.model.ToBeSigned;
import eu.europa.esig.dss.signature.CounterSignatureService;
import eu.europa.esig.dss.signature.DocumentSignatureService;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import org.junit.jupiter.api.BeforeEach;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CBAdESLevelBCounterSignatureTest extends AbstractCBAdESCounterSignatureTest {

    private CBAdESService service;
    private DSSDocument documentToSign;

    private Date signingDate;

    @BeforeEach
    void init() throws Exception {
        service = new CBAdESService(getCompleteCertificateVerifier());
        service.setTspSource(getGoodTsa());
        documentToSign = new InMemoryDocument("Hello World!".getBytes(), "doc.txt");
        signingDate = new Date();
    }

    @Override
    protected CBAdESSignatureParameters getSignatureParameters() {
        CBAdESSignatureParameters signatureParameters = new CBAdESSignatureParameters();
        signatureParameters.bLevel().setSigningDate(signingDate);
        signatureParameters.setSigningCertificate(getSigningCert());
        signatureParameters.setCertificateChain(getCertificateChain());
        signatureParameters.setSignaturePackaging(SignaturePackaging.ENVELOPING);
        signatureParameters.setSignatureLevel(SignatureLevel.CB_AdES_BASELINE_B);
        signatureParameters.setCoseStructureType(COSEStructureType.COSE_SIGN1);
        return signatureParameters;
    }

    @Override
    protected CBAdESCounterSignatureParameters getCounterSignatureParameters() {
        CBAdESCounterSignatureParameters signatureParameters = new CBAdESCounterSignatureParameters();
        signatureParameters.bLevel().setSigningDate(signingDate);
        signatureParameters.setSigningCertificate(getSigningCert());
        signatureParameters.setCertificateChain(getCertificateChain());
        signatureParameters.setSignatureLevel(SignatureLevel.CB_AdES_BASELINE_B);
        SignerLocation signerLocation = new SignerLocation();
        signerLocation.setLocality("Kehlen");
        signatureParameters.bLevel().setSignerLocation(signerLocation);
        signatureParameters.bLevel().setCommitmentTypeIndications(Collections.singletonList(CommitmentTypeEnum.ProofOfCreation));
        return signatureParameters;
    }

    @Override
    protected void onDocumentSigned(byte[] byteArray) {
        super.onDocumentSigned(byteArray);

        InMemoryDocument signedDocument = new InMemoryDocument(byteArray);
        assertTrue(COSEParser.isSupported(signedDocument));

        COSEParser coseParser = COSEParser.fromDocument(signedDocument);
        COSESignStructure coseSignStructure = coseParser.parse();
        assertNotNull(coseSignStructure);

        assertEquals(COSESignatureContext.COSE_SIGN1, coseSignStructure.getContext());
        COSESign1 coseSign1 = assertInstanceOf(COSESign1.class, coseSignStructure);

        COSEUnprotectedHeader unprotectedHeader = coseSign1.getUnprotectedHeader();
        assertNotNull(unprotectedHeader);
        assertFalse(unprotectedHeader.isEmpty());
        assertEquals(1, unprotectedHeader.getSize());

        CBORArray uHeaders = unprotectedHeader.getAsArray(COSEConstants.U_HEADERS);
        assertNotNull(uHeaders);
        assertEquals(1, uHeaders.getSize());

        List<CBORObject> items = uHeaders.getItems();
        assertEquals(1, items.size());

        CBORObject countersignatureObject = items.get(0);
        assertTrue(countersignatureObject.isByteString());
        CBORByteString countersignatureBtsr = assertInstanceOf(CBORByteString.class, countersignatureObject);

        CBORObject countersignatureComponent = CBORUtils.parseCbor(countersignatureBtsr.getBytes());
        assertTrue(countersignatureComponent.isMap());
        CBORMap cborMap = assertInstanceOf(CBORMap.class, countersignatureComponent);
        assertEquals(1, cborMap.getSize());

        CBORObject counterSignatureV2Object = cborMap.getHeader(COSEConstants.COUNTER_SIGNATURE_V2);
        assertNotNull(counterSignatureV2Object);
        assertTrue(counterSignatureV2Object.isArray());

        COSECounterSignStructure coseCounterSignStructure = COSECounterSignatureParser.fromCBORObject(counterSignatureV2Object)
                .setContext(COSESignatureContext.COSE_COUNTER_SIGNATURE_V2)
                .setMasterSignature(coseSign1)
                .parse();
        assertNotNull(coseCounterSignStructure);
        assertEquals(COSESignatureContext.COSE_COUNTER_SIGNATURE_V2, coseCounterSignStructure.getContext());

        COSECounterSignature coseCounterSignature = assertInstanceOf(COSECounterSignature.class, coseCounterSignStructure);
        assertNotNull(coseCounterSignature.getProtectedHeader());
        assertNotNull(coseCounterSignature.getUnprotectedHeader());
        assertNotNull(coseCounterSignature.getSignature());
        assertNotNull(coseCounterSignature.getMasterSignature());

        COSEProtectedHeader protectedHeader = coseCounterSignature.getProtectedHeader();

        CBORObject cty = protectedHeader.getHeader(COSEConstants.CONTENT_TYPE);
        assertNull(cty);
    }

    @Override
    protected void checkSignatureScopes(DiagnosticData diagnosticData) {
        super.checkSignatureScopes(diagnosticData);

        boolean counterSignatureFound = false;
        for (SignatureWrapper signatureWrapper : diagnosticData.getSignatures()) {
            if (signatureWrapper.isCounterSignature()) {
                List<XmlSignatureScope> signatureScopes = signatureWrapper.getSignatureScopes();
                assertEquals(1, signatureScopes.size());

                XmlSignatureScope xmlSignatureScope = signatureScopes.get(0);
                assertEquals(SignatureScopeType.COUNTER_SIGNATURE, xmlSignatureScope.getScope());
                assertEquals(signatureWrapper.getParent().getId(), xmlSignatureScope.getName());

                counterSignatureFound = true;
            }
        }
        assertTrue(counterSignatureFound);
    }

    @Override
    protected void checkCommitmentTypeIndications(DiagnosticData diagnosticData) {
        super.checkCommitmentTypeIndications(diagnosticData);

        for (SignatureWrapper signature : diagnosticData.getSignatures()) {
            List<XmlCommitmentTypeIndication> commitmentTypeIndications = signature.getCommitmentTypeIndications();
            if (signature.isCounterSignature()) {
                assertEquals(1, commitmentTypeIndications.size());
                XmlCommitmentTypeIndication commitmentTypeIndication = commitmentTypeIndications.get(0);
                assertEquals(CommitmentTypeEnum.ProofOfCreation.getUri(), commitmentTypeIndication.getIdentifier());
                assertEquals(CommitmentTypeEnum.ProofOfCreation.getDescription(), commitmentTypeIndication.getDescription());
                assertEquals(CommitmentTypeEnum.ProofOfCreation.getDocumentationReferences().length,
                        commitmentTypeIndication.getDocumentationReferences().size());
            } else {
                assertEquals(0, commitmentTypeIndications.size());
            }
        }
    }

    @Override
    protected void verifySourcesAndDiagnosticData(List<AdvancedSignature> advancedSignatures,
                                                  DiagnosticData diagnosticData) {
        super.verifySourcesAndDiagnosticData(advancedSignatures, diagnosticData);

        CBAdESSignature jadesSignature = (CBAdESSignature) advancedSignatures.get(0);

        COSEStructure coseSignStructure = jadesSignature.getCoseSignature().getCoseSignStructure();
        DSSDocument signatureDocument = new InMemoryDocument(coseSignStructure.serialize());

        CBAdESCounterSignatureParameters counterSignatureParameters = getCounterSignatureParameters();
        counterSignatureParameters.setSignatureIdToCounterSign(getSignatureIdToCounterSign());

        for (SignatureWrapper signature : diagnosticData.getSignatures()) {
            XmlDigestAlgoAndValue dtbsr = signature.getDataToBeSignedRepresentation();

            ToBeSigned dataToSign;
            if (signature.isCounterSignature()) {
                dataToSign = service.getDataToBeCounterSigned(signatureDocument, counterSignatureParameters);
            } else {
                dataToSign = service.getDataToSign(getDocumentToSign(), getSignatureParameters());
            }
            assertArrayEquals(DSSUtils.digest(dtbsr.getDigestMethod(), dataToSign.getBytes()), dtbsr.getDigestValue());
        }
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
    protected CounterSignatureService<CBAdESCounterSignatureParameters> getCounterSignatureService() {
        return service;
    }

    @Override
    protected String getSigningAlias() {
        return GOOD_USER;
    }

}
