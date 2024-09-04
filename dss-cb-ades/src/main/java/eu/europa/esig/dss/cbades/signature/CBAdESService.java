package eu.europa.esig.dss.cbades.signature;

import eu.europa.esig.dss.enumerations.EncryptionAlgorithm;
import eu.europa.esig.dss.enumerations.MimeTypeEnum;
import eu.europa.esig.dss.enumerations.SigDMechanism;
import eu.europa.esig.dss.enumerations.SignatureAlgorithm;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.SignatureValue;
import eu.europa.esig.dss.model.ToBeSigned;
import eu.europa.esig.dss.model.x509.CertificateToken;
import eu.europa.esig.dss.signature.AbstractSignatureParameters;
import eu.europa.esig.dss.signature.AbstractSignatureService;
import eu.europa.esig.dss.signature.CounterSignatureService;
import eu.europa.esig.dss.signature.MultipleDocumentsSignatureService;
import eu.europa.esig.dss.signature.SigningOperation;
import eu.europa.esig.dss.spi.DSSPKUtils;
import eu.europa.esig.dss.spi.validation.CertificateVerifier;
import eu.europa.esig.dss.spi.x509.tsp.TimestampToken;
import eu.europa.esig.dss.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Contains methods for CB-AdES signature creation/extension
 */
public class CBAdESService extends AbstractSignatureService<CBAdESSignatureParameters, CBAdESTimestampParameters> implements
        MultipleDocumentsSignatureService<CBAdESSignatureParameters, CBAdESTimestampParameters>,
        CounterSignatureService<CBAdESCounterSignatureParameters> {

    private static final Logger LOG = LoggerFactory.getLogger(CBAdESService.class);

    /**
     * This is the constructor to create an instance of the {@code CBAdESService}. A
     * certificate verifier must be provided.
     *
     * @param certificateVerifier {@code CertificateVerifier} provides information
     *                            on the sources to be used in the validation
     *                            process in the context of a signature.
     */
    public CBAdESService(final CertificateVerifier certificateVerifier) {
        super(certificateVerifier);
        LOG.debug("+ CBAdESService created");
    }

    @Override
    public ToBeSigned getDataToSign(DSSDocument toSignDocument, CBAdESSignatureParameters parameters) {
        Objects.requireNonNull(toSignDocument, "toSignDocument cannot be null!");
        Objects.requireNonNull(parameters, "SignatureParameters cannot be null!");

        assertSigningCertificateValid(parameters);

        CBAdESBuilder cbadesBuilder = getCBAdESBuilder(parameters, Collections.singletonList(toSignDocument));
        return cbadesBuilder.buildDataToBeSigned();
    }

    @Override
    public ToBeSigned getDataToSign(List<DSSDocument> toSignDocuments, CBAdESSignatureParameters parameters) {
        Objects.requireNonNull(toSignDocuments, "toSignDocuments cannot be null!");
        Objects.requireNonNull(parameters, "SignatureParameters cannot be null!");

        assertMultiDocumentsAllowed(toSignDocuments, parameters);

        CBAdESBuilder cbadesBuilder = getCBAdESBuilder(parameters, toSignDocuments);
        return cbadesBuilder.buildDataToBeSigned();
    }

    @Override
    public DSSDocument signDocument(DSSDocument toSignDocument, CBAdESSignatureParameters parameters, SignatureValue signatureValue) {
        Objects.requireNonNull(toSignDocument, "toSignDocument cannot be null!");
        return signDocument(Collections.singletonList(toSignDocument), parameters, signatureValue);
    }

    @Override
    public DSSDocument signDocument(List<DSSDocument> toSignDocuments, CBAdESSignatureParameters parameters, SignatureValue signatureValue) {
        Objects.requireNonNull(toSignDocuments, "toSignDocuments cannot be null!");
        Objects.requireNonNull(parameters, "SignatureParameters cannot be null!");
        Objects.requireNonNull(signatureValue, "SignatureValue cannot be null!");
        assertMultiDocumentsAllowed(toSignDocuments, parameters);
        assertSigningCertificateValid(parameters);

        CBAdESBuilder cbadesBuilder = getCBAdESBuilder(parameters, toSignDocuments);
        DSSDocument signedDocument = cbadesBuilder.build(signatureValue);

        // TODO : add extension support

        parameters.reinit();
        signedDocument.setName(getFinalFileName(toSignDocuments.iterator().next(), SigningOperation.SIGN, parameters.getSignatureLevel()));
        signedDocument.setMimeType(MimeTypeEnum.COSE);
        return signedDocument;
    }

    @Override
    public DSSDocument extendDocument(DSSDocument toExtendDocument, CBAdESSignatureParameters parameters) {
        return null;
    }

    @Override
    public ToBeSigned getDataToBeCounterSigned(DSSDocument signatureDocument, CBAdESCounterSignatureParameters parameters) {
        return null;
    }

    @Override
    public DSSDocument counterSignSignature(DSSDocument signatureDocument, CBAdESCounterSignatureParameters parameters, SignatureValue signatureValue) {
        return null;
    }

    @Override
    public TimestampToken getContentTimestamp(List<DSSDocument> toSignDocuments, CBAdESSignatureParameters parameters) {
        return null;
    }

    @Override
    public DSSDocument timestamp(List<DSSDocument> toTimestampDocuments, CBAdESTimestampParameters parameters) {
        return null;
    }

    @Override
    public TimestampToken getContentTimestamp(DSSDocument toSignDocument, CBAdESSignatureParameters parameters) {
        return null;
    }

    /**
     * Returns the CBAdESBuilder to be used
     *
     * @param parameters {@link CBAdESSignatureParameters}
     * @param documentsToSign a list of {@link DSSDocument}s
     * @return {@link CBAdESBuilder}
     */
    protected CBAdESBuilder getCBAdESBuilder(CBAdESSignatureParameters parameters, List<DSSDocument> documentsToSign) {
        // TODO : add support of parallel signatures
        return new CBAdESBuilder(certificateVerifier, parameters, documentsToSign);
    }

    @Override
    protected void assertSigningCertificateValid(AbstractSignatureParameters<?> parameters) {
        super.assertSigningCertificateValid(parameters);
        assertSigningCertificateValid(parameters.getSignatureAlgorithm(), parameters.getSigningCertificate());
    }

    private void assertSigningCertificateValid(SignatureAlgorithm signatureAlgorithm, CertificateToken signingCertificate) {
        if (signatureAlgorithm.getEncryptionAlgorithm() != null && signatureAlgorithm.getDigestAlgorithm() != null &&
                signatureAlgorithm.getEncryptionAlgorithm().isEquivalent(EncryptionAlgorithm.ECDSA) &&
                signingCertificate != null) {
            String errorMessage = "For ECDSA with %s a key with P-%s curve shall be used for a COSE! See RFC 9053.";
            int keySize = DSSPKUtils.getPublicKeySize(signingCertificate.getPublicKey());
            switch (signatureAlgorithm.getDigestAlgorithm()) {
                case SHA256:
                    if (256 != keySize) {
                        throw new IllegalArgumentException(String.format(errorMessage, signatureAlgorithm.getDigestAlgorithm(), 256));
                    }
                    break;
                case SHA384:
                    if (384 != keySize) {
                        throw new IllegalArgumentException(String.format(errorMessage, signatureAlgorithm.getDigestAlgorithm(), 384));
                    }
                    break;
                case SHA512:
                    if (521 != keySize) {
                        throw new IllegalArgumentException(String.format(errorMessage, signatureAlgorithm.getDigestAlgorithm(), 521));
                    }
                    break;
                default:
                    throw new UnsupportedOperationException(String.format(
                            "ECDSA with %s is not supported for COSE!", signatureAlgorithm.getDigestAlgorithm()));
            }
        }
    }

    /**
     * Only DETACHED signatures are allowed
     *
     * @param toSignDocuments list of {@link DSSDocument}s
     * @param parameters {@link CBAdESSignatureParameters}
     */
    private void assertMultiDocumentsAllowed(List<DSSDocument> toSignDocuments, CBAdESSignatureParameters parameters) {
        Objects.requireNonNull(parameters.getSignaturePackaging(), "SignaturePackaging shall be defined!");

        if (Utils.isCollectionEmpty(toSignDocuments)) {
            throw new IllegalArgumentException("The documents to sign must be provided!");
        }
        SignaturePackaging signaturePackaging = parameters.getSignaturePackaging();
        if (!SignaturePackaging.DETACHED.equals(signaturePackaging) && toSignDocuments.size() > 1) {
            throw new IllegalArgumentException("Not supported operation (only DETACHED are allowed for multiple document signing)!");
        }
        if (SignaturePackaging.DETACHED.equals(signaturePackaging) && SigDMechanism.NO_SIG_D.equals(parameters.getSigDMechanism())
                && toSignDocuments.size() > 1) {
            throw new IllegalArgumentException("NO_SIG_D mechanism is not allowed for multiple documents!");
        }
    }

}
