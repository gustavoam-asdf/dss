package eu.europa.esig.dss.cbades.signature;

import eu.europa.esig.dss.enumerations.COSEStructureType;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.SigDMechanism;
import eu.europa.esig.dss.enumerations.SignatureForm;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.signature.AbstractSignatureParameters;

import java.util.Objects;

/**
 * The parameters to create/extend a CB-AdES signature
 * 
 */
public class CBAdESSignatureParameters extends AbstractSignatureParameters<CBAdESTimestampParameters> {

    /**
     * Defines if certificate chain binaries must be included into the signed header ('x5chain' signed header)
     * <p>
     * DEFAULT: TRUE (the certificate chain header will be included into the signed header)
     */
    private boolean includeCertificateChain = true;

    /**
     * This property defines whether a 'kid' (key identifier) header parameter should be added to a signed header.
     * <p>
     * NOTE: a signing certificate shall be provided to embed the 'kid' header
     * <p>
     * DEFAULT: TRUE ('kid' header parameter is included into the signed header, provided that
     *           the signing-certificate is defined within the signature parameters).
     */
    private boolean includeKeyIdentifier = true;

    /**
     * This property defines a value for the 'x5u' signed header parameter (see RFC 9360).
     * The value shall refer to a URI where the X.509 public key certificate or certificate chain
     * corresponding to the key used to digitally sign the COSE can be retrieved from.
     * <p>
     * NOTE: use methods {@code #setSigningCertificate} and {@code #includeCertificateChain}
     *       to disable encapsulation of the signing certificate and certificate chain binaries
     * <p>
     * DEFAULT: NULL (the 'x5u' header parameter is not included)
     */
    private String x509Url;

    /**
     * The DigestAlgorithm used to create a reference to a signing certificate, namely 'x5t' signed header
     */
    private DigestAlgorithm signingCertificateDigestMethod = DigestAlgorithm.SHA512;

    /**
     * Defines the COSE structure, whether to allow multiple signers (COSE_SIGN) or preserve only one signer (COSE_SIGN1)
     * Default : COSEStructureType.COSE_SIGN (allows multiple signature incorporation)
     */
    private COSEStructureType coseStructureType;

    /**
     * Defines a used 'sigD' mechanism for a detached signature
     */
    private SigDMechanism sigDMechanism;

    /**
     * Default constructor instantiating object with default parameters
     */
    public CBAdESSignatureParameters() {
        // empty
    }

    @Override
    public void setSignatureLevel(SignatureLevel signatureLevel) {
        if (signatureLevel == null || SignatureForm.CBAdES != signatureLevel.getSignatureForm()) {
            throw new IllegalArgumentException("Only CBAdES form is allowed !");
        }
        super.setSignatureLevel(signatureLevel);
    }

    @Override
    public CBAdESTimestampParameters getContentTimestampParameters() {
        if (contentTimestampParameters == null) {
            contentTimestampParameters = new CBAdESTimestampParameters();
        }
        return contentTimestampParameters;
    }

    @Override
    public CBAdESTimestampParameters getSignatureTimestampParameters() {
        if (signatureTimestampParameters == null) {
            signatureTimestampParameters = new CBAdESTimestampParameters();
        }
        return signatureTimestampParameters;
    }

    @Override
    public CBAdESTimestampParameters getArchiveTimestampParameters() {
        if (archiveTimestampParameters == null) {
            archiveTimestampParameters = new CBAdESTimestampParameters();
        }
        return archiveTimestampParameters;
    }

    /**
     * Defines if complete certificate chain binaries must be included into the signed header ('x5chain' signed header)
     *
     * @return TRUE if the certificate chain must be included, FALSE otherwise
     */
    public boolean isIncludeCertificateChain() {
        return includeCertificateChain;
    }

    /**
     * Sets if complete certificate chain binaries must be included into the 'x5chain' signed header
     * Default: TRUE (the complete binaries will be included into the signed header)
     *
     * @param includeCertificateChain if the certificate chain binaries must be included into the signed header
     */
    public void setIncludeCertificateChain(boolean includeCertificateChain) {
        this.includeCertificateChain = includeCertificateChain;
    }

    /**
     * Returns whether a 'kid' (key identifier) header parameter should be created
     *
     * @return TRUE if the 'kid' should be created, FALSE otherwise
     */
    public boolean isIncludeKeyIdentifier() {
        return includeKeyIdentifier;
    }

    /**
     * Sets whether a 'kid' (key identifier) header parameter should be created within a signed header,
     * provided that a signing-certificate is defined within the signature parameters.
     * <p>
     * DEFAULT : TRUE (the 'kid' header parameter is created)
     *
     * @param includeKeyIdentifier identifies whether 'kid' should be created (when a signing-certificate is provided)
     */
    public void setIncludeKeyIdentifier(boolean includeKeyIdentifier) {
        this.includeKeyIdentifier = includeKeyIdentifier;
    }

    /**
     * Returns the value of the 'x5u' (X.509 URL) header parameter if present
     *
     * @return {@link String}
     */
    public String getX509Url() {
        return x509Url;
    }

    /**
     * Sets the value for the 'x5u' (X.509 URL) signed header parameter (see RFC 9360).
     * The value shall refer to a URI where the X.509 public key certificate or certificate chain
     * corresponding to the key used to digitally sign the COSE can be retrieved from.
     * <p>
     * NOTE: use methods {@code #setSigningCertificate} and {@code #includeCertificateChain}
     *       to disable encapsulation of the signing certificate and certificate chain binaries (included by default).
     * <p>
     * DEFAULT: NULL (the 'x5u' (X.509 URL) header parameter is not included)
     *
     * @param x509Url {@link String} value of 'x5u' header parameter
     */
    public void setX509Url(String x509Url) {
        this.x509Url = x509Url;
    }

    /**
     * See {@link #setSigningCertificateDigestMethod(DigestAlgorithm)}.
     *
     * @return {@link DigestAlgorithm} to be used for signing certificate digest representation
     */
    public DigestAlgorithm getSigningCertificateDigestMethod() {
        return signingCertificateDigestMethod;
    }

    /**
     * The digest method indicates the digest algorithm to be used to calculate the certificate digest
     * to define a signing certificate (RFC 9360 'x5t' signed header)
     * Default: DigestAlgorithm.SHA512
     *
     * @param signingCertificateDigestMethod {@link DigestAlgorithm} to be used
     */
    public void setSigningCertificateDigestMethod(final DigestAlgorithm signingCertificateDigestMethod) {
        Objects.requireNonNull(signingCertificateDigestMethod, "SigningCertificateDigestMethod cannot be null!");
        this.signingCertificateDigestMethod = signingCertificateDigestMethod;
    }

    /**
     * Gets the COSE structure type
     *
     * @return {@link COSEStructureType}
     */
    public COSEStructureType getCoseStructureType() {
        return coseStructureType;
    }

    /**
     * Sets the COSE structure type as per RFC 9052 "4. Signing Objects".
     * - COSE_SIGN is used to create a signature format with multiple signers;
     * - COSE_SIGN1 is used to create a signature format with one and only one signer.
     * NOTE: unlike JWS serialization types, no type conversion is allowed for CBOR signatures.
     * Default : COSEStructureType.COSE_SIGN (multiple signers are allowed)
     *
     * @param coseStructureType {@link COSEStructureType}
     */
    public void setCoseStructureType(COSEStructureType coseStructureType) {
        this.coseStructureType = coseStructureType;
    }

    /**
     * Returns a sigD mechanism to use
     *
     * @return {@link SigDMechanism}
     */
    public SigDMechanism getSigDMechanism() {
        return sigDMechanism;
    }

    /**
     * Sets sigD mechanism to use for a Detached signature
     *
     * @param sigDMechanism {@link SigDMechanism}
     */
    public void setSigDMechanism(SigDMechanism sigDMechanism) {
        this.sigDMechanism = sigDMechanism;
    }
    
}
