package eu.europa.esig.dss.cbades.signature;

import eu.europa.esig.dss.enumerations.COSEStructureType;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.EncryptionAlgorithm;
import eu.europa.esig.dss.enumerations.SignatureAlgorithm;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.signature.DocumentSignatureService;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Tag("slow")
class CBAdESLevelBWithECDSATest extends AbstractCBAdESTestSignature {

    private DocumentSignatureService<CBAdESSignatureParameters, CBAdESTimestampParameters> service;
    private CBAdESSignatureParameters signatureParameters;
    private DSSDocument documentToSign;

    private String signingAlias;

    private static Stream<Arguments> data() {
        List<Arguments> args = new ArrayList<>();

        for (COSEStructureType coseStructureType : COSEStructureType.values()) {
            for (DigestAlgorithm digestAlgo : DigestAlgorithm.values()) {
                SignatureAlgorithm sa = SignatureAlgorithm.getAlgorithm(EncryptionAlgorithm.ECDSA, digestAlgo);
                if (sa != null && sa.getCOSEId() != null) {
                    args.add(Arguments.of(coseStructureType, digestAlgo, getSigner(digestAlgo)));
                }
            }
        }

        return args.stream();
    }

    private static String getSigner(DigestAlgorithm digestAlgorithm) {
        switch (digestAlgorithm) {
            case SHA256:
                return ECDSA_USER;
            case SHA384:
                return ECDSA_384_USER;
            case SHA512:
                return ECDSA_521_USER;
            default:
                throw new UnsupportedOperationException(String.format(
                        "DigestAlgorithm '%s' is not supported!", digestAlgorithm));
        }
    }

    @ParameterizedTest(name = "Combination {index} if type {0} and ECDSA with digest algorithm {1} and signer {2}")
    @MethodSource("data")
    void init(COSEStructureType coseStructureType, DigestAlgorithm digestAlgo, String signingAlias) {
        this.signingAlias = signingAlias;

        documentToSign = new InMemoryDocument("Hello World!".getBytes(), "doc.txt");

        signatureParameters = new CBAdESSignatureParameters();
        signatureParameters.setSigningCertificate(getSigningCert());
        signatureParameters.setCertificateChain(getCertificateChain());
        signatureParameters.setSignaturePackaging(SignaturePackaging.ENVELOPING);
        signatureParameters.setSignatureLevel(SignatureLevel.CB_AdES_BASELINE_B);
        signatureParameters.setCoseStructureType(coseStructureType);
        signatureParameters.setDigestAlgorithm(digestAlgo);

        service = new CBAdESService(getOfflineCertificateVerifier());

        super.signAndVerify();
    }

    @Override
    public void signAndVerify() {
    }

    @Override
    protected DocumentSignatureService<CBAdESSignatureParameters, CBAdESTimestampParameters> getService() {
        return service;
    }

    @Override
    protected CBAdESSignatureParameters getSignatureParameters() {
        return signatureParameters;
    }

    @Override
    protected DSSDocument getDocumentToSign() {
        return documentToSign;
    }

    @Override
    protected String getSigningAlias() {
        return signingAlias;
    }

}