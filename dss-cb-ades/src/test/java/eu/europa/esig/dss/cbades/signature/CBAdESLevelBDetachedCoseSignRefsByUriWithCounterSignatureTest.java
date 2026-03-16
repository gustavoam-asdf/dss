package eu.europa.esig.dss.cbades.signature;

import eu.europa.esig.dss.cbades.COSEConstants;
import eu.europa.esig.dss.cbades.COSECounterSignStructure;
import eu.europa.esig.dss.cbades.COSECounterSignature;
import eu.europa.esig.dss.cbades.COSECounterSignatureParser;
import eu.europa.esig.dss.cbades.COSEParser;
import eu.europa.esig.dss.cbades.COSEProtectedHeader;
import eu.europa.esig.dss.cbades.COSESign;
import eu.europa.esig.dss.cbades.COSESignStructure;
import eu.europa.esig.dss.cbades.COSESignature;
import eu.europa.esig.dss.cbades.COSESignatureContext;
import eu.europa.esig.dss.cbades.COSEUnprotectedHeader;
import eu.europa.esig.dss.cbades.cbor.CBORArray;
import eu.europa.esig.dss.cbades.cbor.CBORByteString;
import eu.europa.esig.dss.cbades.cbor.CBORMap;
import eu.europa.esig.dss.cbades.cbor.CBORNull;
import eu.europa.esig.dss.cbades.cbor.CBORObject;
import eu.europa.esig.dss.cbades.cbor.CBORUtils;
import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.SignatureWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlSignatureScope;
import eu.europa.esig.dss.enumerations.COSEStructureType;
import eu.europa.esig.dss.enumerations.SigDMechanism;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.enumerations.SignatureScopeType;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.signature.CounterSignatureService;
import eu.europa.esig.dss.signature.DocumentSignatureService;
import org.junit.jupiter.api.BeforeEach;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CBAdESLevelBDetachedCoseSignRefsByUriWithCounterSignatureTest extends AbstractCBAdESCounterSignatureTest {

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
        signatureParameters.setSignatureLevel(SignatureLevel.CB_AdES_BASELINE_B);
        signatureParameters.setCoseStructureType(COSEStructureType.COSE_SIGN);
        signatureParameters.setSignaturePackaging(SignaturePackaging.DETACHED);
        signatureParameters.setSigDMechanism(SigDMechanism.OBJECT_ID_BY_URI);
        return signatureParameters;
    }

    @Override
    protected CBAdESCounterSignatureParameters getCounterSignatureParameters() {
        CBAdESCounterSignatureParameters signatureParameters = new CBAdESCounterSignatureParameters();
        signatureParameters.bLevel().setSigningDate(signingDate);
        signatureParameters.setSigningCertificate(getSigningCert());
        signatureParameters.setCertificateChain(getCertificateChain());
        signatureParameters.setSignatureLevel(SignatureLevel.CB_AdES_BASELINE_B);
        return signatureParameters;
    }

    @Override
    protected List<DSSDocument> getDetachedContents() {
        return Collections.singletonList(documentToSign);
    }

    @Override
    protected void onDocumentSigned(byte[] byteArray) {
        super.onDocumentSigned(byteArray);

        InMemoryDocument signedDocument = new InMemoryDocument(byteArray);
        assertTrue(COSEParser.isSupported(signedDocument));

        COSEParser coseParser = COSEParser.fromDocument(signedDocument);
        COSESignStructure coseSignStructure = coseParser.parse();
        assertNotNull(coseSignStructure);

        assertEquals(COSESignatureContext.COSE_SIGN, coseSignStructure.getContext());
        COSESign coseSign = assertInstanceOf(COSESign.class, coseSignStructure);

        assertNotNull(coseSign.getPayload());
        assertInstanceOf(CBORNull.class, coseSign.getPayload());

        COSEUnprotectedHeader unprotectedHeader = coseSign.getUnprotectedHeader();
        assertNotNull(unprotectedHeader);
        assertTrue(unprotectedHeader.isEmpty());

        List<COSESignature> signatures = coseSign.getSignatures();
        assertEquals(1, signatures.size());

        COSESignature coseSignature = signatures.get(0);
        unprotectedHeader = coseSignature.getUnprotectedHeader();
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
                .setMasterSignature(coseSign)
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
