package eu.europa.esig.dss.eaa.jwt.creation;

import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.EAAWrapper;
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

        SDJWTEAAPayloadParameters parameters = new SDJWTEAAPayloadParameters();
        parameters.setIssuer("https://issuer.example.com");
        parameters.setSubject(getSigningCert().getSubject().getPrettyPrintRFC2253());
        parameters.nonSelectivelyDisclosable().setIssuingAuthority("Public body");
        parameters.nonSelectivelyDisclosable().setIssuingCountry("LU");
        parameters.nonSelectivelyDisclosable().setIssuingAuthorityRegistrationIdentifier("XX12345");
        parameters.nonSelectivelyDisclosable().setGivenName("Alice");
        parameters.nonSelectivelyDisclosable().setFamilyName("Doe");

        parameters.setOneTime(true);

        SDJWTEAAClaimObject cnf = SDJWTEAAClaim.createObject("cnf");
        SDJWTEAAClaimObject jwk = SDJWTEAAClaim.createObject("jwk");
        jwk.addChild(SDJWTEAAClaim.create("kty", "EC"));
        jwk.addChild(SDJWTEAAClaim.create("crv", "P-256"));
        jwk.addChild(SDJWTEAAClaim.create("x",  toBase64Url(((ECPublicKey) getSigningCert().getPublicKey()).getW().getAffineX(), 32)));
        jwk.addChild(SDJWTEAAClaim.create("y", toBase64Url(((ECPublicKey) getSigningCert().getPublicKey()).getW().getAffineY(), 32)));
        cnf.addChild(jwk);

        parameters.nonSelectivelyDisclosable().addClaim(cnf); // TODO : replace the method with setDeviceKey

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

        SDJWTPayloadBuilder payloadBuilder = new SDJWTPayloadBuilder();
        DSSDocument payload = payloadBuilder.buildPayload(parameters);
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
