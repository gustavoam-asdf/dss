package eu.europa.esig.dss.spi.eaa;

import eu.europa.esig.dss.enumerations.EAAStatus;
import eu.europa.esig.dss.enumerations.EAAStatusOrigin;
import eu.europa.esig.dss.enumerations.SignatureValidity;
import eu.europa.esig.dss.model.identifier.TokenIdentifier;
import eu.europa.esig.dss.model.x509.Token;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import eu.europa.esig.dss.spi.x509.CertificateSource;

import javax.security.auth.x500.X500Principal;
import java.security.PublicKey;

/**
 * Represents an EAA status representation
 *
 */
public abstract class EAAStatusToken extends Token {

    private static final long serialVersionUID = 3803119761156101993L;

    /** Extracted binaries of the Status Token */
    protected StatusTokenBinary encoded;

    /** Signature used to sign the EAA status data */
    protected AdvancedSignature signature;

    /** Related {@link EAA} to this status object */
    protected EAA relatedEAA;

    /** The URL which was used to obtain the status data (online). */
    protected String sourceURL;

    /** The external origin (EXTERNAL or CACHED) */
    protected EAAStatusOrigin origin;

    /** Contains the revocation status of the token. */
    protected EAAStatus status;

    /** Certificate source built on the extracted information from the EAA status */
    protected CertificateSource certificateSource;

    /**
     * Default constructor
     */
    protected EAAStatusToken() {
        // empty
    }

    /**
     * Sets a related EAA
     *
     * @param relatedEAA {@link EAA}
     */
    public void setRelatedEAA(EAA relatedEAA) {
        this.relatedEAA = relatedEAA;
    }

    /**
     * Sets the source URL used to access the status token
     *
     * @param sourceURL {@link String}
     */
    public void setSourceURL(String sourceURL) {
        this.sourceURL = sourceURL;
    }

    /**
     * Sets the origin of the status token (e.g. EXTERNAL or CACHED)
     *
     * @param origin {@link EAAStatusOrigin}
     */
    public void setOrigin(EAAStatusOrigin origin) {
        this.origin = origin;
    }

    /**
     * Gets signature used to sign the EAA status token
     *
     * @return {@link AdvancedSignature}
     */
    public AdvancedSignature getSignature() {
        return signature;
    }

    /**
     * Gets the indication of the status of the related token (e.g. VALID, INVALID, etc.)
     *
     * @return {@link EAAStatus}
     */
    public EAAStatus getStatus() {
        return status;
    }

    /**
     * Gets the certificate source built on the extracted EAA status information
     *
     * @return {@link CertificateSource}
     */
    public CertificateSource getCertificateSource() {
        return certificateSource;
    }

    /**
     * Sets the certificate source built on the extracted EAA status information
     *
     * @param certificateSource {@link CertificateSource}
     */
    public void setCertificateSource(CertificateSource certificateSource) {
        this.certificateSource = certificateSource;
    }

    @Override
    protected TokenIdentifier buildTokenIdentifier() {
        return new EAAStatusTokenIdentifier(this);
    }

    @Override
    protected SignatureValidity checkIsSignedBy(PublicKey publicKey) {
        throw new UnsupportedOperationException(this.getClass().getName());
    }

    @Override
    public X500Principal getIssuerX500Principal() {
        if (signature.getSigningCertificateToken() != null) {
            return signature.getSigningCertificateToken().getSubject().getPrincipal();
        }
        return null;
    }

    @Override
    public String toString(String indentStr) {
        // TODO : to be implemented
        return "";
    }

    @Override
    public byte[] getEncoded() {
        return encoded.getBinaries();
    }

}
