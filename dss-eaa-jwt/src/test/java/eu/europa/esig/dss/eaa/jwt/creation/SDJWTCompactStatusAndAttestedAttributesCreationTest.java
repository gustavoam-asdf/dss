package eu.europa.esig.dss.eaa.jwt.creation;

import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.EAAWrapper;
import eu.europa.esig.dss.eaa.jwt.validation.AbstractSDJWTEAAPresentationTestValidation;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.JWSSerializationType;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.jades.JAdESSignatureParameters;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.SignatureValue;
import eu.europa.esig.dss.model.ToBeSigned;
import eu.europa.esig.dss.spi.DSSASN1Utils;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.utils.Utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SDJWTCompactStatusAndAttestedAttributesCreationTest extends AbstractSDJWTEAAPresentationTestValidation {

    @Override
    protected DSSDocument getSignedDocument() {
        Date issuanceDate = new Date();

        // TODO : refactor the claims building
        SDJWTEAAPayloadParameters payloadParameters = new SDJWTEAAPayloadParameters();
        payloadParameters.setIssuer("https://issuer.example.com");
        payloadParameters.setIssuanceDate(issuanceDate);
        payloadParameters.setExpirationDate(new Date(issuanceDate.getTime() + 3600 * 1000));
        payloadParameters.addClaim(SDJWTEAAClaim.create("issuing_authority", "Public body"));
        payloadParameters.addClaim(SDJWTEAAClaim.create("given_name", "Alice"));
        payloadParameters.addClaim(SDJWTEAAClaim.create("family_name", "Doe"));
        payloadParameters.addClaim(SDJWTEAAClaim.create("vct", "https://nowina.lu/eaa/metadata"));
        String digest = Utils.toBase64(DSSUtils.digest(DigestAlgorithm.SHA256, "Hello World".getBytes()));
        payloadParameters.addClaim(SDJWTEAAClaim.create("vct#integrity", DigestAlgorithm.SHA256.getSubresourceIntegrityId() + "-" + digest));

        SDJWTEAAClaimObject status = SDJWTEAAClaim.createObject("status");
        status.addChild(SDJWTEAAClaim.create("type", "TokenStatusList"));
        status.addChild(SDJWTEAAClaim.create("purpose", "revocation"));
        status.addChild(SDJWTEAAClaim.create("index", 0));
        status.addChild(SDJWTEAAClaim.create("uri", "https://nowina.lu/pki-factory/status"));
        payloadParameters.addClaim(status);

        SDJWTEAAClaimObject subAttrs = SDJWTEAAClaim.createObject("subAttrs");
        subAttrs.addChild(SDJWTEAAClaim.create("sub_id", DSSASN1Utils.getSubjectCommonName(getSigningCert())));
        SDJWTEAAClaimArray attrs = SDJWTEAAClaimArray.create("attrs");
        attrs.addElement(SDJWTEAAClaim.create("given_name"));
        attrs.addElement(SDJWTEAAClaim.create("family_name"));
        subAttrs.addChild(attrs);
        payloadParameters.addClaim(subAttrs);

        SDJWTEAAClaimObject placeOfBirth = SDJWTEAAClaim.createObject("place_of_birth");
        placeOfBirth.addChild(SDJWTEAAClaim.create("country", "LU"));
        payloadParameters.addClaim(placeOfBirth);

        JAdESSignatureParameters signatureParameters = new JAdESSignatureParameters();
        signatureParameters.bLevel().setSigningDate(issuanceDate);
        signatureParameters.setSigningCertificate(getSigningCert());
        signatureParameters.setCertificateChain(getCertificateChain());
        signatureParameters.setSignatureLevel(SignatureLevel.JAdES_BASELINE_B);
        signatureParameters.setSignaturePackaging(SignaturePackaging.ENVELOPING);
        signatureParameters.setJwsSerializationType(JWSSerializationType.COMPACT_SERIALIZATION);
        signatureParameters.setIncludeKeyIdentifier(false);
        signatureParameters.setSignatureType("dc+sd-jwt");

        SDJWTEAAService service = new SDJWTEAAService(getOfflineCertificateVerifier());

        ToBeSigned dataToSign = service.getDataToBeSigned(payloadParameters, signatureParameters);
        SignatureValue signatureValue = getToken().sign(dataToSign, signatureParameters.getDigestAlgorithm(), getPrivateKeyEntry());
        DSSDocument signedDocument = service.signEAA(payloadParameters, signatureParameters, signatureValue);
        DSSDocument eaaPresentation = service.issuePresentation(signedDocument, Collections.emptyList());
        return eaaPresentation;
    }

    @Override
    protected void checkClaims(final DiagnosticData diagnosticData) {
        super.checkClaims(diagnosticData);

        EAAWrapper eaa = diagnosticData.getEAAs().get(0);

        assertEquals("TokenStatusList", eaa.getEAAStatusType());
        assertEquals("revocation", eaa.getEAAStatusPurpose());
        assertEquals(0, eaa.getEAAStatusIndex().intValue());
        assertEquals("https://nowina.lu/pki-factory/status", eaa.getEAAStatusUri());

        assertEquals("good-user", eaa.getAttestedAttributesSubjectId());
        assertNull(eaa.getAttestedAttributesSubjectFamilyName());
        assertNull(eaa.getAttestedAttributesSubjectGivenName());
        assertNull(eaa.getAttestedAttributesSubjectDocumentNumber());
        assertNull(eaa.getAttestedAttributesSubjectPseudonym());
        assertEquals(Arrays.asList("given_name", "family_name"), eaa.getAttestedAttributes());

        assertEquals("LU", eaa.getHolderPlaceOfBirthCountry());
    }

    @Override
    protected boolean disclosuresPresent() {
        return false;
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
