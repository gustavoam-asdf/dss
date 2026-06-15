package eu.europa.esig.dss.model.lote;

import eu.europa.esig.dss.model.timedependent.TimeDependentValues;
import eu.europa.esig.dss.model.x509.CertificateToken;

import java.util.List;

/**
 * This class contains information about a single trusted entity's service
 *
 */
public class TrustedEntityService implements EntityService<ServiceStatusAndInformationExtensions> {

    private static final long serialVersionUID = -2657583530747203938L;

    /** List of certificates */
    private final List<CertificateToken> certificates;

    /** Statuses based on time */
    private final TimeDependentValues<ServiceStatusAndInformationExtensions> status;

    /**
     * Default constructor
     *
     * @param certificates a list of {@link CertificateToken}s
     * @param status {@link TimeDependentValues}
     */
    public TrustedEntityService(final List<CertificateToken> certificates,
                                final TimeDependentValues<ServiceStatusAndInformationExtensions> status) {
        this.certificates = certificates;
        this.status = status;
    }

    /**
     * Gets a list of certificates
     *
     * @return a list of {@link CertificateToken}s
     */
    public List<CertificateToken> getCertificates() {
        return certificates;
    }

    /**
     * Gets status based on time
     *
     * @return {@link TimeDependentValues}
     */
    public TimeDependentValues<ServiceStatusAndInformationExtensions> getStatusAndInformationExtensions() {
        return status;
    }

    /**
     * Builds {@code TrustedEntityService}
     */
    public static final class TrustEntityServiceBuilder {

        /** List of certificates */
        private List<CertificateToken> certificates;

        /** Statuses based on time */
        private TimeDependentValues<ServiceStatusAndInformationExtensions> status;

        /**
         * Default constructor
         */
        public TrustEntityServiceBuilder() {
            // empty
        }

        /**
         * Sets a list of certificates
         *
         * @param certificates a list of {@link CertificateToken}s
         * @return this {@link TrustEntityServiceBuilder}
         */
        public TrustEntityServiceBuilder setCertificates(List<CertificateToken> certificates) {
            this.certificates = certificates;
            return this;
        }

        /**
         * Sets a status
         *
         * @param status {@link TimeDependentValues}
         * @return this {@link TrustEntityServiceBuilder}
         */
        public TrustEntityServiceBuilder setStatusAndInformationExtensions(
                TimeDependentValues<ServiceStatusAndInformationExtensions> status) {
            this.status = status;
            return this;
        }

        /**
         * Builds {@code TrustedEntityService}
         *
         * @return {@link TrustedEntityService}
         */
        public TrustedEntityService build() {
            return new TrustedEntityService(certificates, status);
        }

    }

}
