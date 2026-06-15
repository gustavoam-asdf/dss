package eu.europa.esig.dss.eaa.mdoc.creation;

import eu.europa.esig.dss.cbades.signature.CBAdESSignatureParameters;
import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.EAAWrapper;
import eu.europa.esig.dss.diagnostic.claim.ClaimWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDigestMatcher;
import eu.europa.esig.dss.eaa.mdoc.MdocConstants;
import eu.europa.esig.dss.eaa.mdoc.validation.MdocDeviceResponseEAAPresentationValidator;
import eu.europa.esig.dss.eaa.mdoc.validation.MdocValidationParameters;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.EllipticCurve;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.validation.SignedDocumentValidator;
import org.junit.jupiter.api.BeforeEach;

import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MdocEAAISONonMdLWithKBTest extends AbstractMdocEAAPresentationTestIssuance {

    private MdocEAAPayloadParameters payloadParameters;
    private CBAdESSignatureParameters signatureParameters;

    private CBAdESSignatureParameters keyBindingSignatureParameters;
    private MdocKeyBindingParameters keyBindingParameters;

    @BeforeEach
    void init() {
        payloadParameters = new MdocEAAPayloadParameters();
        payloadParameters.setDocType(MdocConstants.ISO23220_1_MID_DOC_TYPE);
        payloadParameters.setDeviceKey(getSigningCert());
        payloadParameters.selectivelyDisclosable().setFamilyName("Doe");
        payloadParameters.selectivelyDisclosable().setGivenName("John");
        payloadParameters.selectivelyDisclosable().setBirthdate(DSSUtils.getUtcDate(2001, Calendar.JANUARY, 1));
        payloadParameters.selectivelyDisclosable().setAdministrativeIssuanceDate(DSSUtils.getUtcDate(2026, Calendar.JUNE, 1));
        payloadParameters.selectivelyDisclosable().setAdministrativeExpirationDate(DSSUtils.getUtcDate(2026, Calendar.AUGUST, 31));
        payloadParameters.selectivelyDisclosable().setIssuingCountry("LU");

        payloadParameters.selectivelyDisclosable().setIssuingAuthority("TEST Authority");
        payloadParameters.selectivelyDisclosable().setDocumentNumber("123456789");

        signatureParameters = new CBAdESSignatureParameters();
        signatureParameters.setDigestAlgorithm(DigestAlgorithm.SHA256);
        signatureParameters.setSigningCertificate(getSigningCert());
        signatureParameters.setCertificateChain(getCertificateChain());

        keyBindingSignatureParameters = new CBAdESSignatureParameters();
        keyBindingSignatureParameters.setDigestAlgorithm(DigestAlgorithm.SHA256);
        keyBindingSignatureParameters.setSigningCertificate(getSigningCert());

        keyBindingParameters = new MdocKeyBindingParameters();
        keyBindingParameters.setDocType(MdocConstants.ISO23220_1_MID_DOC_TYPE);
        keyBindingParameters.setSessionTranscript(buildSessionTranscript());
    }

    @Override
    protected MdocEAAPayloadParameters getPayloadParameters() {
        return payloadParameters;
    }

    @Override
    protected CBAdESSignatureParameters getSignatureParameters() {
        return signatureParameters;
    }

    @Override
    protected CBAdESSignatureParameters getKeyBindingSignatureParameters() {
        return keyBindingSignatureParameters;
    }

    @Override
    protected MdocKeyBindingParameters getKeyBindingParameters() {
        return keyBindingParameters;
    }

    @Override
    protected SignedDocumentValidator getValidator(DSSDocument signedDocument) {
        SignedDocumentValidator validator = super.getValidator(signedDocument);
        MdocDeviceResponseEAAPresentationValidator mdocValidator = assertInstanceOf(MdocDeviceResponseEAAPresentationValidator.class, validator);
        MdocValidationParameters mdocValidationParameters = new MdocValidationParameters();
        mdocValidationParameters.setSessionTranscript(buildSessionTranscript());
        mdocValidator.setEAAValidationParameters(mdocValidationParameters);
        return validator;
    }

    private DSSDocument buildSessionTranscript() {
        byte[] select = new byte[]{0x01, 0x02};
        byte[] request = new byte[]{0x03, 0x04};
        SessionTranscriptBuilder builder =
                SessionTranscriptBuilder.nfcHandover(select, request)
                        .security(EllipticCurve.P_256, getSigningCert().getPublicKey())
                        .eReaderKey(getSigningCert().getPublicKey());

        return builder.build();
    }

    @Override
    protected void checkEAADigestMatchers(DiagnosticData diagnosticData) {
        super.checkEAADigestMatchers(diagnosticData);

        EAAWrapper eaa = diagnosticData.getEAAs().get(0);
        List<XmlDigestMatcher> digestMatchers = eaa.getDigestMatchers();
        assertEquals(8, digestMatchers.size());

        boolean familyNameSDFound = false;
        boolean givenNameSDFound = false;
        boolean birthdateSDFound = false;
        boolean issueDateSDFound = false;
        boolean expiryDateSDFound = false;
        boolean issuingCountrySDFound = false;
        boolean issuingAuthoritySDFound = false;
        boolean documentNumberSDFound = false;
        for (XmlDigestMatcher xmlDigestMatcher : digestMatchers) {
            assertNotNull(xmlDigestMatcher.getDisclosableClaim());
            if ("family_name".equals(xmlDigestMatcher.getDisclosableClaim().getName())) {
                assertEquals("org.iso.23220.1", xmlDigestMatcher.getDisclosableClaim().getNamespace());
                assertEquals("Doe", xmlDigestMatcher.getDisclosableClaim().getValue());
                familyNameSDFound = true;
            } else if ("given_name".equals(xmlDigestMatcher.getDisclosableClaim().getName())) {
                assertEquals("org.iso.23220.1", xmlDigestMatcher.getDisclosableClaim().getNamespace());
                assertEquals("John", xmlDigestMatcher.getDisclosableClaim().getValue());
                givenNameSDFound = true;
            } else if ("birth_date".equals(xmlDigestMatcher.getDisclosableClaim().getName())) {
                assertEquals("org.iso.23220.1", xmlDigestMatcher.getDisclosableClaim().getNamespace());
                assertEquals("{\"birth_date\": \"2001-01-01\"}", xmlDigestMatcher.getDisclosableClaim().getValue());
                birthdateSDFound = true;
            } else if ("issue_date".equals(xmlDigestMatcher.getDisclosableClaim().getName())) {
                assertEquals("org.iso.23220.1", xmlDigestMatcher.getDisclosableClaim().getNamespace());
                assertEquals("2026-06-01", xmlDigestMatcher.getDisclosableClaim().getValue());
                issueDateSDFound = true;
            } else if ("expiry_date".equals(xmlDigestMatcher.getDisclosableClaim().getName())) {
                assertEquals("org.iso.23220.1", xmlDigestMatcher.getDisclosableClaim().getNamespace());
                assertEquals("2026-08-31", xmlDigestMatcher.getDisclosableClaim().getValue());
                expiryDateSDFound = true;
            } else if ("issuing_country".equals(xmlDigestMatcher.getDisclosableClaim().getName())) {
                assertEquals("org.iso.23220.1", xmlDigestMatcher.getDisclosableClaim().getNamespace());
                assertEquals("LU", xmlDigestMatcher.getDisclosableClaim().getValue());
                issuingCountrySDFound = true;
            } else if ("issuing_authority".equals(xmlDigestMatcher.getDisclosableClaim().getName())) {
                assertEquals("org.iso.23220.1", xmlDigestMatcher.getDisclosableClaim().getNamespace());
                assertEquals("TEST Authority", xmlDigestMatcher.getDisclosableClaim().getValue());
                issuingAuthoritySDFound = true;
            } else if ("document_number".equals(xmlDigestMatcher.getDisclosableClaim().getName())) {
                assertEquals("org.iso.23220.1", xmlDigestMatcher.getDisclosableClaim().getNamespace());
                assertEquals("123456789", xmlDigestMatcher.getDisclosableClaim().getValue());
                documentNumberSDFound = true;
            }
        }
        assertTrue(familyNameSDFound);
        assertTrue(givenNameSDFound);
        assertTrue(birthdateSDFound);
        assertTrue(issueDateSDFound);
        assertTrue(expiryDateSDFound);
        assertTrue(issuingCountrySDFound);
        assertTrue(issuingAuthoritySDFound);
        assertTrue(documentNumberSDFound);
    }

    @Override
    protected void checkClaims(DiagnosticData diagnosticData) {
        super.checkClaims(diagnosticData);

        EAAWrapper eaa = diagnosticData.getEAAById(diagnosticData.getFirstEAAId());
        assertEquals("1.0", eaa.getEAAVersion());
        assertEquals("org.iso.23220.1.mID", eaa.getEAADocumentType());

        final List<ClaimWrapper> otherKeyBindingPayloadClaims = eaa.getOtherKeyBindingPayloadClaims();
        assertEquals(0, otherKeyBindingPayloadClaims.size());
    }

    @Override
    protected String getSigningAlias() {
        return ECDSA_USER;
    }

}
