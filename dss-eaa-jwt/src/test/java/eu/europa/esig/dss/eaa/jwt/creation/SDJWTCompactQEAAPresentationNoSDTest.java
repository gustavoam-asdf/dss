package eu.europa.esig.dss.eaa.jwt.creation;

import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.EAAWrapper;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.jades.JAdESSignatureParameters;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.Digest;
import eu.europa.esig.dss.spi.DSSASN1Utils;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.spi.x509.CommonX509URLCertificateSource;
import eu.europa.esig.dss.validation.SignedDocumentValidator;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SDJWTCompactQEAAPresentationNoSDTest extends AbstractSDJWTEAAPresentationTestIssuance {

    private SDJWTEAAPayloadParameters payloadParameters;
    private JAdESSignatureParameters signatureParameters;

    @BeforeEach
    void init() {
        payloadParameters = new SDJWTEAAPayloadParameters();
        payloadParameters.setIssuer("EAA provider");
        payloadParameters.setSubject(DSSASN1Utils.getSubjectCommonName(getSigningCert()));
        payloadParameters.setDeviceKey(getSigningCert().getPublicKey());

        payloadParameters.setVerifiableCredentialsType("urn:eudi:eaa:1");
        Digest digest = new Digest(DigestAlgorithm.SHA256, DSSUtils.digest(DigestAlgorithm.SHA256, "vct".getBytes()));
        payloadParameters.setVerifiableCredentialsTypeIntegrity(digest);

        payloadParameters.setStatusList(1, "https://pki.nowina.lu/eaa/status_list");
        payloadParameters.setCategory("urn:etsi:esi:eaa:eu:qualified");

        payloadParameters.nonSelectivelyDisclosable().setGivenName("John");
        payloadParameters.nonSelectivelyDisclosable().setFamilyName("Doe");
        payloadParameters.nonSelectivelyDisclosable().setIssuingAuthority("TEST Authority");
        payloadParameters.nonSelectivelyDisclosable().setIssuingCountry("LU");
        payloadParameters.nonSelectivelyDisclosable().setIssuingAuthorityRegistrationIdentifier("VATLU-123456");

        signatureParameters = new JAdESSignatureParameters();
        signatureParameters.setDigestAlgorithm(DigestAlgorithm.SHA256);
        signatureParameters.setSigningCertificate(getSigningCert());
        signatureParameters.setCertificateChain(getCertificateChain());

        signatureParameters.setIncludeKeyIdentifier(false);
        signatureParameters.setX509Url("https://pki.nowina.lu/eaa/qeaa.crt");
    }

    @Override
    protected SignedDocumentValidator getValidator(DSSDocument signedDocument) {
        SignedDocumentValidator documentValidator = super.getValidator(signedDocument);
        CommonX509URLCertificateSource x509URLCertificateSource = new CommonX509URLCertificateSource();
        x509URLCertificateSource.addCertificate("https://pki.nowina.lu/eaa/qeaa.crt", getSigningCert());
        documentValidator.setSigningCertificateSource(x509URLCertificateSource);
        return documentValidator;
    }

    @Override
    protected SDJWTEAAPayloadParameters getPayloadParameters() {
        return payloadParameters;
    }

    @Override
    protected JAdESSignatureParameters getSignatureParameters() {
        return signatureParameters;
    }

    @Override
    protected JAdESSignatureParameters getKeyBindingSignatureParameters() {
        return null;
    }

    @Override
    protected SDJWTKeyBindingParameters getKeyBindingParameters() {
        return null;
    }

    @Override
    protected void checkClaims(DiagnosticData diagnosticData) {
        super.checkClaims(diagnosticData);

        EAAWrapper eaa = diagnosticData.getEAAById(diagnosticData.getFirstEAAId());
        assertEquals("urn:eudi:eaa:1", eaa.getEAAMetadataUri());
        assertEquals(DigestAlgorithm.SHA256, eaa.getEAAMetadataIntegrityDigestAlgorithm());
        assertArrayEquals(DSSUtils.digest(DigestAlgorithm.SHA256, "vct".getBytes()), eaa.getEAAMetadataIntegrityBytes());
        assertEquals(DSSUtils.formatDateToRFC(getSignatureParameters().bLevel().getSigningDate()), DSSUtils.formatDateToRFC(eaa.getEAANotBefore()));
        assertEquals(DSSUtils.formatDateToRFC(getSigningCert().getNotAfter()), DSSUtils.formatDateToRFC(eaa.getEAAExpiration()));
        assertEquals("EAA provider", eaa.getEAAIssuer());
        assertEquals(DSSASN1Utils.getSubjectCommonName(getSigningCert()), eaa.getEAASubject());
        assertEquals("TEST Authority", eaa.getDocumentIssuingAuthority());
        assertEquals("LU", eaa.getDocumentIssuingAuthorityCountry());
        assertEquals("VATLU-123456", eaa.getIssuingRegistrationIdentifier());
        assertEquals("John", eaa.getHolderGivenName());
        assertEquals("Doe", eaa.getHolderFamilyName());

        assertEquals("urn:etsi:esi:eaa:eu:qualified", eaa.getEAACategory());

        assertEquals(1, eaa.getEAAStatusIndex());
        assertEquals("https://pki.nowina.lu/eaa/status_list", eaa.getEAAStatusUri());

        assertArrayEquals(getSigningCert().getPublicKey().getEncoded(), eaa.getEAADevicePublicKey());
    }

    @Override
    protected boolean keyBindingPresent() {
        return false;
    }

    @Override
    protected boolean disclosuresPresent() {
        return false;
    }

    @Override
    protected String getSigningAlias() {
        return GOOD_USER;
    }

}