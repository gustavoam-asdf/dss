package eu.europa.esig.dss.eaa.revocation.model.statuslist;

import eu.europa.esig.dss.enumerations.EAAStatus;
import eu.europa.esig.dss.spi.eaa.EAARevocationToken;
import eu.europa.esig.dss.spi.eaa.EAARevocationTokenBinary;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import eu.europa.esig.dss.spi.x509.CertificateSource;

import java.io.Serializable;
import java.util.Date;

/**
 * Represents a validated Token Status List response against a given EAA
 *
 */
public class EAAStatusListToken extends EAARevocationToken {

    private static final long serialVersionUID = -471820238992127908L;

    /** Payload of the Token Status List */
    protected StatusListPayload payload;

    /**
     * Constructor to instantiate the EAA Status List object from a builder
     *
     * @param builder {@link EAAStatusListTokenBuilder}
     */
    protected EAAStatusListToken(EAAStatusListTokenBuilder builder) {
        this.encoded = builder.binary;
        this.signature = builder.signature;
        this.status = builder.status;
        this.payload = builder.payload;
    }

    /**
     * Instantiates a new builder to create the EAAStatusToken
     *
     * @return {@link EAAStatusListTokenBuilder}
     */
    public static EAAStatusListTokenBuilder initBuilder() {
        return new EAAStatusListTokenBuilder();
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
    public static class EAAStatusListTokenBuilder implements Serializable {

        private static final long serialVersionUID = -5818532413563116918L;

        /** Extracted binaries of the Status Token */
        protected EAARevocationTokenBinary binary;

        /** Signature used to sign the EAA revocation data */
        protected AdvancedSignature signature;

        /** Contains the revocation status of the token. */
        protected EAAStatus status;

        /** Payload of the Token Status List */
        protected StatusListPayload payload;

        /** Certificate source built on the extracted information from the EAA revocation */
        protected CertificateSource certificateSource;

        /**
         * Default constructor
         */
        protected EAAStatusListTokenBuilder() {
            // empty
        }

        /**
         * Sets binaries of the revocation token
         *
         * @param binary {@link EAARevocationTokenBinary}
         * @return this {@link EAAStatusListTokenBuilder}
         */
        public EAAStatusListTokenBuilder setBinary(EAARevocationTokenBinary binary) {
            this.binary = binary;
            return this;
        }

        /**
         * Sets signature used to sign this token
         *
         * @param signature {@link AdvancedSignature}
         * @return this {@link EAAStatusListTokenBuilder}
         */
        public EAAStatusListTokenBuilder setSignature(AdvancedSignature signature) {
            this.signature = signature;
            return this;
        }

        /**
         * Sets the status value for the corresponding EAA
         *
         * @param status {@link EAAStatus}
         * @return this {@link EAAStatusListTokenBuilder}
         */
        public EAAStatusListTokenBuilder setStatus(EAAStatus status) {
            this.status = status;
            return this;
        }

        /**
         * Sets the payload of the revocation token
         *
         * @param payload {@link StatusListPayload}
         * @return this {@link EAAStatusListTokenBuilder}
         */
        public EAAStatusListTokenBuilder setPayload(StatusListPayload payload) {
            this.payload = payload;
            return this;
        }

        /**
         * Sets the certificate source
         *
         * @param certificateSource {@link CertificateSource}
         * @return this {@link EAAStatusListTokenBuilder}
         */
        public EAAStatusListTokenBuilder setCertificateSource(CertificateSource certificateSource) {
            this.certificateSource = certificateSource;
            return this;
        }

        /**
         * Builds the {@link EAAStatusListToken}
         *
         * @return {@link EAAStatusListToken}
         */
        public EAAStatusListToken build() {
            return new EAAStatusListToken(this);
        }

    }

}
