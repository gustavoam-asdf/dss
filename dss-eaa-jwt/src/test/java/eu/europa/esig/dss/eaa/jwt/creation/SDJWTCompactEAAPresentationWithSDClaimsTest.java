package eu.europa.esig.dss.eaa.jwt.creation;

import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.EAAWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDigestMatcher;
import eu.europa.esig.dss.eaa.jwt.validation.AbstractSDJWTEAAPresentationTestValidation;
import eu.europa.esig.dss.enumerations.JWSSerializationType;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.jades.JAdESSignatureParameters;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.SignatureValue;
import eu.europa.esig.dss.model.ToBeSigned;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SDJWTCompactEAAPresentationWithSDClaimsTest extends AbstractSDJWTEAAPresentationTestValidation {

    @Override
    protected DSSDocument getSignedDocument() {
        SDJWTEAAPayloadParameters payloadParameters = new SDJWTEAAPayloadParameters();
        payloadParameters.setIssuer("https://issuer.example.com");
        payloadParameters.addClaim("given_name", "John", true);
        payloadParameters.addClaim("family_name", "Doe", true);

        JAdESSignatureParameters signatureParameters = new JAdESSignatureParameters();
        signatureParameters.setSigningCertificate(getSigningCert());
        signatureParameters.setCertificateChain(getCertificateChain());
        signatureParameters.setSignatureLevel(SignatureLevel.JAdES_BASELINE_B);
        signatureParameters.setSignaturePackaging(SignaturePackaging.ENVELOPING);
        signatureParameters.setJwsSerializationType(JWSSerializationType.COMPACT_SERIALIZATION);
        signatureParameters.setX509Url("http://nowina.lu/pki-factory/good-cert");

        SDJWTEAAService service = new SDJWTEAAService(getOfflineCertificateVerifier());

        ToBeSigned dataToSign = service.getDataToBeSigned(payloadParameters, signatureParameters);
        SignatureValue signatureValue = getToken().sign(dataToSign, signatureParameters.getDigestAlgorithm(), getPrivateKeyEntry());
        DSSDocument signedDocument = service.signEAA(payloadParameters, signatureParameters, signatureValue);
        List<SDJWTEAADisclosure> disclosures = service.getDisclosures(payloadParameters);
        return service.issuePresentation(signedDocument, disclosures);
    }

    @Override
    protected void checkEAAPresentationDigestMatchers(DiagnosticData diagnosticData) {
        super.checkEAAPresentationDigestMatchers(diagnosticData);

        EAAWrapper eaa = diagnosticData.getEAAs().get(0);
        List<XmlDigestMatcher> digestMatchers = eaa.getDigestMatchers();
        assertEquals(2, digestMatchers.size());

        boolean givenNameSDFound = false;
        boolean familyNameSDFound = false;
        for (XmlDigestMatcher xmlDigestMatcher : digestMatchers) {
            assertNotNull(xmlDigestMatcher.getDisclosableClaim());
            if ("given_name".equals(xmlDigestMatcher.getDisclosableClaim().getName())) {
                givenNameSDFound = true;
            } else if ("family_name".equals(xmlDigestMatcher.getDisclosableClaim().getName())) {
                familyNameSDFound = true;
            }
        }
        assertTrue(givenNameSDFound);
        assertTrue(familyNameSDFound);
    }

    @Override
    protected void checkClaims(final DiagnosticData diagnosticData) {
        super.checkClaims(diagnosticData);

        EAAWrapper eaa = diagnosticData.getEAAs().get(0);
        assertEquals("https://issuer.example.com", eaa.getEAAIssuer());
        assertEquals("John", eaa.getHolderFirstName());
        assertEquals("Doe", eaa.getHolderLastName());
    }

    @Override
    protected boolean keyBindingPresent() {
        return false;
    }

    @Override
    protected String getSigningAlias() {
        return GOOD_USER;
    }

}
