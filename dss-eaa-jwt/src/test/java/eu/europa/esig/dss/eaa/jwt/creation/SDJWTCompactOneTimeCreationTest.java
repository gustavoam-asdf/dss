package eu.europa.esig.dss.eaa.jwt.creation;

import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.EAAWrapper;
import eu.europa.esig.dss.eaa.common.creation.claim.EAAClaimObject;
import eu.europa.esig.dss.eaa.common.creation.claim.AbstractEAAClaim;
import eu.europa.esig.dss.eaa.jwt.validation.AbstractSDJWTEAAPresentationTestValidation;
import eu.europa.esig.dss.enumerations.JWSSerializationType;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.jades.DSSJsonUtils;
import eu.europa.esig.dss.jades.JAdESSignatureParameters;
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
        SDJWTPayloadBuilder payloadBuilder = new SDJWTPayloadBuilder();
        payloadBuilder.setIssuer("https://issuer.example.com");
        payloadBuilder.addClaim("issuing_authority", "Public body");
        payloadBuilder.addClaim("issuing_country", "LU");
        payloadBuilder.addClaim("iss_reg_id", "XX12345");
        payloadBuilder.addClaim("sub", getSigningCert().getSubject().getPrettyPrintRFC2253());
        payloadBuilder.addClaim("given_name", "Alice");
        payloadBuilder.addClaim("family_name", "Doe");

        payloadBuilder.setOneTime(true);

        SDJWTEAAClaimObject cnf = new SDJWTEAAClaimObject("cnf");
        SDJWTEAAClaimObject jwk = new SDJWTEAAClaimObject("jwk");
        jwk.addChild(new SDJWTEAAClaim("kty", "EC"));
        jwk.addChild(new SDJWTEAAClaim("crv", "P-256"));
        jwk.addChild(new SDJWTEAAClaim("x",  toBase64Url(((ECPublicKey) getSigningCert().getPublicKey()).getW().getAffineX(), 32)));
        jwk.addChild(new SDJWTEAAClaim("y", toBase64Url(((ECPublicKey) getSigningCert().getPublicKey()).getW().getAffineY(), 32)));
        cnf.addChild(jwk);

        payloadBuilder.addClaim(cnf);

        signer = GOOD_USER;

        JAdESSignatureParameters signatureParameters = new JAdESSignatureParameters();
        signatureParameters.setSigningCertificate(getSigningCert());
        signatureParameters.setCertificateChain(getCertificateChain());
        signatureParameters.setSignatureLevel(SignatureLevel.JAdES_BASELINE_B);
        signatureParameters.setSignaturePackaging(SignaturePackaging.ENVELOPING);
        signatureParameters.setJwsSerializationType(JWSSerializationType.COMPACT_SERIALIZATION);
        signatureParameters.setIncludeKeyIdentifier(false);
        signatureParameters.setSignatureType("dc+sd-jwt");

        SDJWTEAAService service = new SDJWTEAAService(getOfflineCertificateVerifier());

        DSSDocument payload = payloadBuilder.buildPayload();
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

        EAAWrapper eaa = diagnosticData.getEAAs().get(0);
        assertTrue(eaa.getOneTimeUse());
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
