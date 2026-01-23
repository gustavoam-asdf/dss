package eu.europa.esig.dss.model.lote;

import eu.europa.esig.dss.model.timedependent.BaseTimeDependent;
import eu.europa.esig.dss.model.tsl.ConditionForQualifiers;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Contains information about the service status and other information for the given time period
 * 
 */
public class TrustedEntityServiceStatusAndInformationExtensions extends BaseTimeDependent implements ServiceStatusAndInformationExtensions {
    
    private static final long serialVersionUID = -1293425116417892690L;

    /*
     * Key = lang
     *
     * List = values / lang
     */

    /** Map of names */
    private final Map<String, List<String>> names;

    /** The type */
    private final String type;

    /** Status */
    private final String status;

    /** A list of condition for qualifiers */
    private final List<ConditionForQualifiers> conditionsForQualifiers;

    /** Additional service info urls */
    private final List<String> additionalServiceInfoUris;

    /** List of service supply points */
    private final List<String> serviceSupplyPoints;

    /** The expired certs revocation info date */
    private final Date expiredCertsRevocationInfo;

    /**
     * Default constructor
     *
     * @param builder {@link ServiceStatusAndInformationExtensionsBuilder}
     */
    public TrustedEntityServiceStatusAndInformationExtensions(ServiceStatusAndInformationExtensionsBuilder builder) {
        super(builder != null ? builder.startDate : null, builder != null ? builder.endDate : null);
        Objects.requireNonNull(builder, "ServiceStatusAndInformationExtensionsBuilder cannot be null!");

        this.names = builder.names;
        this.type = builder.type;
        this.status = builder.status;
        this.conditionsForQualifiers = builder.conditionsForQualifiers;
        this.additionalServiceInfoUris = builder.additionalServiceInfoUris;
        this.serviceSupplyPoints = builder.serviceSupplyPoints;
        this.expiredCertsRevocationInfo = builder.expiredCertsRevocationInfo;
    }

    /**
     * Gets a map of names
     *
     * @return a map of names
     */
    public Map<String, List<String>> getNames() {
        return names;
    }

    /**
     * Gets type
     *
     * @return {@link String}
     */
    public String getType() {
        return type;
    }

    /**
     * Gets status
     *
     * @return {@link String}
     */
    public String getStatus() {
        return status;
    }

    /**
     * Gets a list of conditions for qualifiers
     *
     * @return a list of {@link ConditionForQualifiers}
     */
    public List<ConditionForQualifiers> getConditionsForQualifiers() {
        return conditionsForQualifiers;
    }

    /**
     * Gets additional service info urls
     *
     * @return a list of {@link String}s
     */
    public List<String> getAdditionalServiceInfoUris() {
        return additionalServiceInfoUris;
    }

    /**
     * Gets service supply points
     *
     * @return a list of {@link String}s
     */
    public List<String> getServiceSupplyPoints() {
        return serviceSupplyPoints;
    }

    /**
     * Gets the expired certs revocation info date
     *
     * @return {@link Date}
     */
    public Date getExpiredCertsRevocationInfo() {
        return expiredCertsRevocationInfo;
    }

    /**
     * Builds {@code ServiceStatusAndInformationExtensions}
     */
    public static class ServiceStatusAndInformationExtensionsBuilder {

        /** Map of names */
        private Map<String, List<String>> names;

        /** The type */
        private String type;

        /** Status */
        private String status;

        /** A list of condition for qualifiers */
        private List<ConditionForQualifiers> conditionsForQualifiers;

        /** Additional service info urls */
        private List<String> additionalServiceInfoUris;

        /** List of service supply points */
        private List<String> serviceSupplyPoints;

        /** The expired certs revocation info date */
        private Date expiredCertsRevocationInfo;

        /** The start of validity date */
        private Date startDate;

        /** The end of validity date */
        private Date endDate;

        /**
         * Default constructor
         */
        public ServiceStatusAndInformationExtensionsBuilder() {
            // empty
        }

        /**
         * Constructor with {@code ServiceStatusAndInformationExtensions}
         *
         * @param status {@link TrustedEntityServiceStatusAndInformationExtensions}
         */
        public ServiceStatusAndInformationExtensionsBuilder(TrustedEntityServiceStatusAndInformationExtensions status) {
            this.names = status.getNames();
            this.type = status.getType();
            this.status = status.getStatus();
            this.conditionsForQualifiers = status.getConditionsForQualifiers();
            this.additionalServiceInfoUris = status.getAdditionalServiceInfoUris();
            this.serviceSupplyPoints = status.getServiceSupplyPoints();
            this.expiredCertsRevocationInfo = status.getExpiredCertsRevocationInfo();
            this.startDate = status.getStartDate();
            this.endDate = status.getEndDate();
        }

        /**
         * Builds {@code ServiceStatusAndInformationExtensions}
         *
         * @return {@link TrustedEntityServiceStatusAndInformationExtensions}
         */
        public TrustedEntityServiceStatusAndInformationExtensions build() {
            return new TrustedEntityServiceStatusAndInformationExtensions(this);
        }

        /**
         * Sets a map of names
         *
         * @param names a map of names
         * @return this {@link ServiceStatusAndInformationExtensionsBuilder}
         */
        public ServiceStatusAndInformationExtensionsBuilder setNames(Map<String, List<String>> names) {
            this.names = names;
            return this;
        }

        /**
         * Sets a type
         *
         * @param type {@link String}
         * @return this {@link ServiceStatusAndInformationExtensionsBuilder}
         */
        public ServiceStatusAndInformationExtensionsBuilder setType(String type) {
            this.type = type;
            return this;
        }

        /**
         * Sets a status
         *
         * @param status {@link String}
         * @return this {@link ServiceStatusAndInformationExtensionsBuilder}
         */
        public ServiceStatusAndInformationExtensionsBuilder setStatus(String status) {
            this.status = status;
            return this;
        }

        /**
         * Sets conditions for qualifiers
         *
         * @param conditionsForQualifiers a list of {@link ConditionForQualifiers}
         * @return this {@link ServiceStatusAndInformationExtensionsBuilder}
         */
        public ServiceStatusAndInformationExtensionsBuilder setConditionsForQualifiers(List<ConditionForQualifiers> conditionsForQualifiers) {
            this.conditionsForQualifiers = conditionsForQualifiers;
            return this;
        }

        /**
         * Sets additional service info urls
         *
         * @param additionalServiceInfoUris a list of {@link String}
         * @return this {@link ServiceStatusAndInformationExtensionsBuilder}
         */
        public ServiceStatusAndInformationExtensionsBuilder setAdditionalServiceInfoUris(List<String> additionalServiceInfoUris) {
            this.additionalServiceInfoUris = additionalServiceInfoUris;
            return this;
        }

        /**
         * Sets the service supply points
         *
         * @param serviceSupplyPoints a list of {@link String}
         * @return this {@link ServiceStatusAndInformationExtensionsBuilder}
         */
        public ServiceStatusAndInformationExtensionsBuilder setServiceSupplyPoints(List<String> serviceSupplyPoints) {
            this.serviceSupplyPoints = serviceSupplyPoints;
            return this;
        }

        /**
         * Sets the expired certs revocation info date
         *
         * @param expiredCertsRevocationInfo {@link Date}
         * @return this {@link ServiceStatusAndInformationExtensionsBuilder}
         */
        public ServiceStatusAndInformationExtensionsBuilder setExpiredCertsRevocationInfo(Date expiredCertsRevocationInfo) {
            this.expiredCertsRevocationInfo = expiredCertsRevocationInfo;
            return this;
        }

        /**
         * Sets the start of validity date
         *
         * @param date {@link Date}
         * @return this {@link ServiceStatusAndInformationExtensionsBuilder}
         */
        public ServiceStatusAndInformationExtensionsBuilder setStartDate(Date date) {
            this.startDate = date;
            return this;
        }

        /**
         * Sets the end of validity date
         *
         * @param date {@link Date}
         * @return this {@link ServiceStatusAndInformationExtensionsBuilder}
         */
        public ServiceStatusAndInformationExtensionsBuilder setEndDate(Date date) {
            this.endDate = date;
            return this;
        }

    }
    
}
