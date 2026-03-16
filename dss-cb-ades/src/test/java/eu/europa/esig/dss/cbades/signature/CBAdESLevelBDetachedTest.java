package eu.europa.esig.dss.cbades.signature;

import eu.europa.esig.dss.cbades.COSEConstants;
import eu.europa.esig.dss.cbades.COSEProtectedHeader;
import eu.europa.esig.dss.cbades.COSESign;
import eu.europa.esig.dss.cbades.COSESignStructure;
import eu.europa.esig.dss.cbades.cbor.CBORArray;
import eu.europa.esig.dss.cbades.cbor.CBORMap;
import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.SignerDataWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDigestAlgoAndValue;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.SigDMechanism;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.signature.DocumentSignatureService;
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

class CBAdESLevelBDetachedTest extends AbstractCBAdESTestSignature {

    private DocumentSignatureService<CBAdESSignatureParameters, CBAdESTimestampParameters> service;
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
        signatureParameters.setSignaturePackaging(SignaturePackaging.DETACHED);
        signatureParameters.setSigDMechanism(SigDMechanism.OBJECT_ID_BY_URI_HASH);
        signatureParameters.setSignatureLevel(SignatureLevel.CB_AdES_BASELINE_B);
        return signatureParameters;
    }

    @Override
    protected List<DSSDocument> getDetachedContents() {
        return Collections.singletonList(documentToSign);
    }

    @Override
    protected void checkCOSESignStructure(COSESignStructure coseSignStructure) {
        super.checkCOSESignStructure(coseSignStructure);

        assertInstanceOf(COSESign.class, coseSignStructure);
        COSESign coseSign = (COSESign) coseSignStructure;
        assertEquals(1, coseSign.getSignatures().size());

        assertRequirementsValid(coseSign.getSignatures().get(0).getProtectedHeader());
    }

    private void assertRequirementsValid(COSEProtectedHeader protectedHeader) {
        String cty = protectedHeader.getAsString(COSEConstants.CONTENT_TYPE);
        assertNull(cty);

        CBORMap sigD = protectedHeader.getAsMap(COSEConstants.SIG_D);
        assertNotNull(sigD);

        String mId = sigD.getAsString(COSEConstants.SIG_D_MID);
        assertNotNull(mId);
        assertEquals(SigDMechanism.OBJECT_ID_BY_URI_HASH.getCBAdESUri(), mId);

        Long hashM = sigD.getAsLong(COSEConstants.SIG_D_HASH_M);
        assertNotNull(hashM);
        DigestAlgorithm digestAlgorithm = DigestAlgorithm.forCOSE(hashM);
        assertNotNull(digestAlgorithm);

        CBORArray pars = sigD.getAsArray(COSEConstants.SIG_D_PARS);
        assertNotNull(pars);
        assertFalse(pars.isEmpty());
        assertEquals(1, pars.getSize());
        assertEquals(documentToSign.getName(), pars.getAsString(0));

        CBORArray hashV = sigD.getAsArray(COSEConstants.SIG_D_HASH_V);
        assertNotNull(hashV);
        assertFalse(hashV.isEmpty());
        assertEquals(1, hashV.getSize());
        assertArrayEquals(documentToSign.getDigestValue(digestAlgorithm), hashV.getAsBinaries(0));

        CBORArray ctys = sigD.getAsArray(COSEConstants.SIG_D_CTYS);
        assertNotNull(ctys);
        assertFalse(ctys.isEmpty());
        assertEquals(1, ctys.getSize());
        assertEquals(documentToSign.getMimeType().getMimeTypeString(), ctys.getAsString(0));
    }

    @Override
    protected void checkSignatureScopes(DiagnosticData diagnosticData) {
        super.checkSignatureScopes(diagnosticData);

        assertEquals(1, diagnosticData.getOriginalSignerDocuments().size());

        SignerDataWrapper signerData = diagnosticData.getOriginalSignerDocuments().get(0);
        XmlDigestAlgoAndValue digestAlgoAndValue = signerData.getDigestAlgoAndValue();
        assertNotNull(digestAlgoAndValue);

        assertArrayEquals(documentToSign.getDigestValue(digestAlgoAndValue.getDigestMethod()), digestAlgoAndValue.getDigestValue());
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
    protected String getSigningAlias() {
        return GOOD_USER;
    }

}
