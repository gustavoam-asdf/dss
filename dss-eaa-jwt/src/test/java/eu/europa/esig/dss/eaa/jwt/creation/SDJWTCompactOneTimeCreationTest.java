package eu.europa.esig.dss.eaa.jwt.creation;

import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.EAAPresentationWrapper;
import eu.europa.esig.dss.eaa.jwt.creation.claim.SDJWTObjectPresentableClaim;
import eu.europa.esig.dss.eaa.jwt.creation.claim.SDJWTStringPresentableClaim;
import eu.europa.esig.dss.eaa.jwt.validation.AbstractSDJWTEAAPresentationTestValidation;
import eu.europa.esig.dss.enumerations.JWSSerializationType;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.jades.DSSJsonUtils;
import eu.europa.esig.dss.jades.JAdESSignatureParameters;
import eu.europa.esig.dss.jades.signature.JAdESService;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.SignatureValue;
import eu.europa.esig.dss.model.ToBeSigned;

import java.math.BigInteger;
import java.security.interfaces.ECPublicKey;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SDJWTCompactOneTimeCreationTest extends AbstractSDJWTEAAPresentationTestValidation {

    private String signer;

    @Override
    protected DSSDocument getSignedDocument() {
        signer = ECDSA_USER;

        // TODO : refactor the claims building
        SDJWTEAAParameters eaaParameters = new SDJWTEAAParameters();
        eaaParameters.setIssuer("https://issuer.example.com");
        eaaParameters.addClaim(new SDJWTStringPresentableClaim("issuing_authority", "Public body"));
        eaaParameters.addClaim(new SDJWTStringPresentableClaim("issuing_country", "LU"));
        eaaParameters.addClaim(new SDJWTStringPresentableClaim("iss_reg_id", "XX12345"));
        eaaParameters.addClaim(new SDJWTStringPresentableClaim("sub", getSigningCert().getSubject().getPrettyPrintRFC2253()));
        eaaParameters.addClaim(new SDJWTStringPresentableClaim("given_name", "Alice"));
        eaaParameters.addClaim(new SDJWTStringPresentableClaim("family_name", "Doe"));
        
        eaaParameters.addClaim(new SDJWTStringPresentableClaim("oneTime", null));

        SDJWTObjectPresentableClaim cnf = new SDJWTObjectPresentableClaim("cnf");
        SDJWTObjectPresentableClaim jwk = new SDJWTObjectPresentableClaim("jwk");
        jwk.addChild(new SDJWTStringPresentableClaim("kty", "EC"));
        jwk.addChild(new SDJWTStringPresentableClaim("crv", "P-256"));
        jwk.addChild(new SDJWTStringPresentableClaim("x",  toBase64Url(((ECPublicKey) getSigningCert().getPublicKey()).getW().getAffineX(), 32)));
        jwk.addChild(new SDJWTStringPresentableClaim("y", toBase64Url(((ECPublicKey) getSigningCert().getPublicKey()).getW().getAffineY(), 32)));
        cnf.addChild(jwk);

        eaaParameters.addClaim(cnf);

        signer = GOOD_USER;

        JAdESSignatureParameters signatureParameters = new JAdESSignatureParameters();
        signatureParameters.setSigningCertificate(getSigningCert());
        signatureParameters.setCertificateChain(getCertificateChain());
        signatureParameters.setSignatureLevel(SignatureLevel.JAdES_BASELINE_B);
        signatureParameters.setSignaturePackaging(SignaturePackaging.ENVELOPING);
        signatureParameters.setJwsSerializationType(JWSSerializationType.COMPACT_SERIALIZATION);
        signatureParameters.setIncludeKeyIdentifier(false);
        signatureParameters.setSignatureType("dc+sd-jwt");

        JAdESService jadesService = new JAdESService(getOfflineCertificateVerifier());
        SDJWTEAAService service = new SDJWTEAAService(jadesService);

        DSSDocument payload = new SDJWTPayloadBuilder().buildPayload(eaaParameters);
        payload.setMimeType(null); // avoid cty

        ToBeSigned dataToSign = service.getDataToBeSigned(payload, signatureParameters);
        SignatureValue signatureValue = getToken().sign(dataToSign, signatureParameters.getDigestAlgorithm(), getPrivateKeyEntry());
        DSSDocument signedDocument = service.signEAA(payload, signatureParameters, signatureValue);
        DSSDocument eaaPresentation = service.issuePresentation(signedDocument, Collections.emptyList());
        return eaaPresentation;
    }

    @Override
    protected void checkClaims(final DiagnosticData diagnosticData) {
        super.checkClaims(diagnosticData);

        EAAPresentationWrapper eaaPresentation = diagnosticData.getEAAPresentations().get(0);
        assertTrue(eaaPresentation.getOneTimeUse());
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
        return signer;
    }

    private String toBase64Url(BigInteger bigInteger, int size) {
        return DSSJsonUtils.toBase64Url(toBytes(bigInteger, size));
    }

    private byte[] toBytes(BigInteger bigInteger, int size) {
        byte[] bytes = bigInteger.toByteArray();

        if (bytes.length == size) return bytes;

        if (bytes.length == size + 1 && bytes[0] == 0) {
            // remove leading zero
            return java.util.Arrays.copyOfRange(bytes, 1, bytes.length);
        }

        byte[] result = new byte[size];
        System.arraycopy(bytes, 0, result, size - bytes.length, bytes.length);
        return result;
    }

}
