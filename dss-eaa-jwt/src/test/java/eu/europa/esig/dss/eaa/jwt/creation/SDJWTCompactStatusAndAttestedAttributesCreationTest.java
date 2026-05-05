package eu.europa.esig.dss.eaa.jwt.creation;

import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.EAAPresentationWrapper;
import eu.europa.esig.dss.eaa.jwt.creation.claim.SDJWTArrayPresentableClaim;
import eu.europa.esig.dss.eaa.jwt.creation.claim.SDJWTObjectPresentableClaim;
import eu.europa.esig.dss.eaa.jwt.creation.claim.SDJWTStringPresentableClaim;
import eu.europa.esig.dss.eaa.jwt.validation.AbstractSDJWTEAAPresentationTestValidation;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.JWSSerializationType;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.jades.JAdESSignatureParameters;
import eu.europa.esig.dss.jades.signature.JAdESService;
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
        SDJWTEAAParameters eaaParameters = new SDJWTEAAParameters();
        eaaParameters.setIssuer("https://issuer.example.com");
        eaaParameters.setIssuanceDate(issuanceDate);
        eaaParameters.setExpirationDate(new Date(issuanceDate.getTime() + 3600 * 1000));
        eaaParameters.addClaim(new SDJWTStringPresentableClaim("issuing_authority", "Public body"));
        eaaParameters.addClaim(new SDJWTStringPresentableClaim("given_name", "Alice"));
        eaaParameters.addClaim(new SDJWTStringPresentableClaim("family_name", "Doe"));
        eaaParameters.addClaim(new SDJWTStringPresentableClaim("vct", "https://nowina.lu/eaa/metadata"));
        String digest = Utils.toBase64(DSSUtils.digest(DigestAlgorithm.SHA256, "Hello World".getBytes()));
        eaaParameters.addClaim(new SDJWTStringPresentableClaim("vct#integrity", DigestAlgorithm.SHA256.getSubresourceIntegrityId() + "-" + digest));

        SDJWTObjectPresentableClaim status = new SDJWTObjectPresentableClaim("status");
        status.addChild(new SDJWTStringPresentableClaim("type", "TokenStatusList"));
        status.addChild(new SDJWTStringPresentableClaim("purpose", "revocation"));
        status.addChild(new SDJWTStringPresentableClaim("index", "0")); // TODO : add support of numeric values
        status.addChild(new SDJWTStringPresentableClaim("uri", "https://nowina.lu/pki-factory/status"));
        eaaParameters.addClaim(status);

        SDJWTObjectPresentableClaim subAttrs = new SDJWTObjectPresentableClaim("subAttrs");
        subAttrs.addChild(new SDJWTStringPresentableClaim("sub_id", DSSASN1Utils.getSubjectCommonName(getSigningCert())));
        SDJWTArrayPresentableClaim attrs = new SDJWTArrayPresentableClaim("attrs");
        attrs.addElement(new SDJWTStringPresentableClaim("given_name"));
        attrs.addElement(new SDJWTStringPresentableClaim("family_name"));
        subAttrs.addChild(attrs);
        eaaParameters.addClaim(subAttrs);

        SDJWTObjectPresentableClaim placeOfBirth = new SDJWTObjectPresentableClaim("place_of_birth");
        placeOfBirth.addChild(new SDJWTStringPresentableClaim("country", "LU"));
        eaaParameters.addClaim(placeOfBirth);

        JAdESSignatureParameters signatureParameters = new JAdESSignatureParameters();
        signatureParameters.bLevel().setSigningDate(issuanceDate);
        signatureParameters.setSigningCertificate(getSigningCert());
        signatureParameters.setCertificateChain(getCertificateChain());
        signatureParameters.setSignatureLevel(SignatureLevel.JAdES_BASELINE_B);
        signatureParameters.setSignaturePackaging(SignaturePackaging.ENVELOPING);
        signatureParameters.setJwsSerializationType(JWSSerializationType.COMPACT_SERIALIZATION);
        signatureParameters.setIncludeKeyIdentifier(false);
        signatureParameters.setSignatureType("dc+sd-jwt");

        JAdESService jadesService = new JAdESService(getOfflineCertificateVerifier());
        SDJWTEAAService service = new SDJWTEAAService(jadesService);

        ToBeSigned dataToSign = service.getDataToBeSigned(eaaParameters, signatureParameters);
        SignatureValue signatureValue = getToken().sign(dataToSign, signatureParameters.getDigestAlgorithm(), getPrivateKeyEntry());
        DSSDocument signedDocument = service.signEAA(eaaParameters, signatureParameters, signatureValue);
        DSSDocument eaaPresentation = service.issuePresentation(signedDocument, Collections.emptyList());
        return eaaPresentation;
    }

    @Override
    protected void checkClaims(final DiagnosticData diagnosticData) {
        super.checkClaims(diagnosticData);

        EAAPresentationWrapper eaaPresentation = diagnosticData.getEAAPresentations().get(0);

        assertEquals("TokenStatusList", eaaPresentation.getEAAStatusType());
        assertEquals("revocation", eaaPresentation.getEAAStatusPurpose());
        // assertEquals(0, eaaPresentation.getEAAStatusIndex().intValue()); // TODO : add support of numeric claims
        assertEquals("https://nowina.lu/pki-factory/status", eaaPresentation.getEAAStatusUri());

        assertEquals("good-user", eaaPresentation.getAttestedAttributesSubjectId());
        assertNull(eaaPresentation.getAttestedAttributesSubjectFamilyName());
        assertNull(eaaPresentation.getAttestedAttributesSubjectGivenName());
        assertNull(eaaPresentation.getAttestedAttributesSubjectDocumentNumber());
        assertNull(eaaPresentation.getAttestedAttributesSubjectPseudonym());
        assertEquals(Arrays.asList("given_name", "family_name"), eaaPresentation.getAttestedAttributes());

        assertEquals("LU", eaaPresentation.getHolderPlaceOfBirthCountry());
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
