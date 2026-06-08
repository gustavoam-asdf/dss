package eu.europa.esig.dss.spi.eaa;

import eu.europa.esig.dss.enumerations.EAAStatusOrigin;
import eu.europa.esig.dss.enumerations.EAAStatus;
import eu.europa.esig.dss.enumerations.SignatureValidity;
import eu.europa.esig.dss.model.identifier.TokenIdentifier;
import eu.europa.esig.dss.model.x509.Token;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;

import javax.security.auth.x500.X500Principal;
import java.io.Serializable;
import java.security.PublicKey;
import java.util.Date;

public class EAAStatusToken extends Token {

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

    /** The time at which the Status List Token was issued. */
    protected Date issuanceTime;

    /** The time at which the Status List Token is considered expired by the Status Issuer. */
    protected Date expirationTime;

    /** The time until when the Status List Token can be cached by a consumer before a fresh copy SHOULD be retrieved. */
    protected Date timeToLive;

    /**
     * Constructor to instantiate the EAA Status List object from a builder
     *
     * @param builder {@link EAAStatusTokenBuilder}
     */
    protected EAAStatusToken(EAAStatusTokenBuilder builder) {
        this.encoded = builder.binary;
        this.signature = builder.signature;
        this.relatedEAA = builder.relatedEAA;
        this.sourceURL = builder.sourceURL;
        this.status = builder.status;
        this.issuanceTime = builder.issuanceTime;
        this.expirationTime = builder.expirationTime;
        this.timeToLive = builder.timeToLive;
    }

    /**
     * Instantiates a new builder to create the EAAStatusToken
     *
     * @return {@link EAAStatusTokenBuilder}
     */
    public static EAAStatusTokenBuilder initBuilder() {
        return new EAAStatusTokenBuilder();
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
    public Date getCreationDate() {
        return issuanceTime;
    }

    @Override
    public String toString(String indentStr) {
        return "";
    }

    @Override
    public byte[] getEncoded() {
        return encoded.getBinaries();
    }

    /**
     * Builder to create the EAA Status Token
     *
     */
    public static class EAAStatusTokenBuilder implements Serializable {

        private static final long serialVersionUID = -5818532413563116918L;

        /** Extracted binaries of the Status Token */
        protected StatusTokenBinary binary;

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

        /** The time at which the Status List Token was issued. */
        protected Date issuanceTime;

        /** The time at which the Status List Token is considered expired by the Status Issuer. */
        protected Date expirationTime;

        /** The time until when the Status List Token can be cached by a consumer before a fresh copy SHOULD be retrieved. */
        protected Date timeToLive;

        /**
         * Default constructor
         */
        protected EAAStatusTokenBuilder() {
            // empty
        }

        public EAAStatusTokenBuilder setBinary(StatusTokenBinary binary) {
            this.binary = binary;
            return this;
        }

        public EAAStatusTokenBuilder setSignature(AdvancedSignature signature) {
            this.signature = signature;
            return this;
        }

        public EAAStatusTokenBuilder setRelatedEAA(EAA relatedEAA) {
            this.relatedEAA = relatedEAA;
            return this;
        }

        public EAAStatusTokenBuilder setSourceURL(String sourceURL) {
            this.sourceURL = sourceURL;
            return this;
        }

        public EAAStatusTokenBuilder setOrigin(EAAStatusOrigin origin) {
            this.origin = origin;
            return this;
        }

        public EAAStatusTokenBuilder setStatus(EAAStatus status) {
            this.status = status;
            return this;
        }

        public EAAStatusTokenBuilder setIssuanceTime(Date issuanceTime) {
            this.issuanceTime = issuanceTime;
            return this;
        }

        public EAAStatusTokenBuilder setExpirationTime(Date expirationTime) {
            this.expirationTime = expirationTime;
            return this;
        }

        public EAAStatusTokenBuilder setTimeToLive(Date timeToLive) {
            this.timeToLive = timeToLive;
            return this;
        }

        /**
         * Builds the {@link EAAStatusToken}
         *
         * @return {@link EAAStatusToken}
         */
        public EAAStatusToken build() {
            return new EAAStatusToken(this);
        }

    }

}
