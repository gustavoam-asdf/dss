package eu.europa.esig.dss.spi.eaa.status.identifierlist;

import eu.europa.esig.dss.enumerations.EAAStatus;
import eu.europa.esig.dss.spi.eaa.EAAStatusToken;
import eu.europa.esig.dss.spi.eaa.StatusTokenBinary;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import eu.europa.esig.dss.spi.x509.CertificateSource;

import java.io.Serializable;
import java.util.Date;

/**
 * Represents a validated token containing revocation information for an EAA provided as an Identifier List
 *
 */
public class EAAIdentifierListToken extends EAAStatusToken {

    private static final long serialVersionUID = -471820238992127908L;

    /** Payload of the Token Status List */
    protected IdentifierListPayload payload;

    /**
     * Constructor to instantiate the EAA Status List object from a builder
     *
     * @param builder {@link EAAIdentifierListToken.EAAIdentifierListTokenBuilder}
     */
    protected EAAIdentifierListToken(EAAIdentifierListToken.EAAIdentifierListTokenBuilder builder) {
        this.encoded = builder.binary;
        this.signature = builder.signature;
        this.status = builder.status;
        this.payload = builder.payload;
    }

    /**
     * Instantiates a new builder to create the EAAStatusToken
     *
     * @return {@link EAAIdentifierListToken.EAAIdentifierListTokenBuilder}
     */
    public static EAAIdentifierListToken.EAAIdentifierListTokenBuilder initBuilder() {
        return new EAAIdentifierListToken.EAAIdentifierListTokenBuilder();
    }

    @Override
    public String getSubject() {
        return payload != null ? payload.getSubject() : null;
    }

    @Override
    public Boolean getSubjectMatch() {
        return sourceURL != null && sourceURL.equals(getSubject());
    }

    @Override
    public Date getCreationDate() {
        return payload != null ? payload.getIssuedAt() : null;
    }

    @Override
    public Date getExpirationDate() {
        return payload != null ? payload.getExpirationTime() : null;
    }

    @Override
    public Number getTimeToLive() {
        return payload != null ? payload.getTimeToLive() : null;
    }

    /**
     * Builder to create the EAA Status Token
     *
     */
    public static class EAAIdentifierListTokenBuilder implements Serializable {

        private static final long serialVersionUID = -5818532413563116918L;

        /** Extracted binaries of the Status Token */
        protected StatusTokenBinary binary;

        /** Signature used to sign the EAA status data */
        protected AdvancedSignature signature;

        /** Contains the revocation status of the token. */
        protected EAAStatus status;

        /** Payload of the Token Status List */
        protected IdentifierListPayload payload;

        /** Certificate source built on the extracted information from the EAA status */
        protected CertificateSource certificateSource;

        /**
         * Default constructor
         */
        protected EAAIdentifierListTokenBuilder() {
            // empty
        }

        /**
         * Sets binaries of the revocation token
         *
         * @param binary {@link StatusTokenBinary}
         * @return this {@link EAAIdentifierListTokenBuilder}
         */
        public EAAIdentifierListTokenBuilder setBinary(StatusTokenBinary binary) {
            this.binary = binary;
            return this;
        }

        /**
         * Sets signature used to sign this token
         *
         * @param signature {@link AdvancedSignature}
         * @return this {@link EAAIdentifierListTokenBuilder}
         */
        public EAAIdentifierListTokenBuilder setSignature(AdvancedSignature signature) {
            this.signature = signature;
            return this;
        }

        /**
         * Sets the status value for the corresponding EAA
         *
         * @param status {@link EAAStatus}
         * @return this {@link EAAIdentifierListTokenBuilder}
         */
        public EAAIdentifierListTokenBuilder setStatus(EAAStatus status) {
            this.status = status;
            return this;
        }

        /**
         * Sets the payload of the revocation token
         *
         * @param payload {@link IdentifierListPayload}
         * @return this {@link EAAIdentifierListTokenBuilder}
         */
        public EAAIdentifierListTokenBuilder setPayload(IdentifierListPayload payload) {
            this.payload = payload;
            return this;
        }

        /**
         * Sets the certificate source
         *
         * @param certificateSource {@link CertificateSource}
         * @return this {@link EAAIdentifierListTokenBuilder}
         */
        public EAAIdentifierListTokenBuilder setCertificateSource(CertificateSource certificateSource) {
            this.certificateSource = certificateSource;
            return this;
        }

        /**
         * Builds the {@link EAAIdentifierListToken}
         *
         * @return {@link EAAIdentifierListToken}
         */
        public EAAIdentifierListToken build() {
            return new EAAIdentifierListToken(this);
        }

    }

}
