package eu.europa.esig.dss.validation.process.qualification.certificate.usage;

import eu.europa.esig.dss.detailedreport.jaxb.XmlCertificateUsage;
import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationCertificateUsage;
import eu.europa.esig.dss.diagnostic.CertificateWrapper;
import eu.europa.esig.dss.diagnostic.TrustedEntityServiceWrapper;
import eu.europa.esig.dss.diagnostic.TrustedSourceServiceWrapper;
import eu.europa.esig.dss.enumerations.CertificateUsage;
import eu.europa.esig.dss.enumerations.CertificateUsageEnum;
import eu.europa.esig.dss.enumerations.ListType;
import eu.europa.esig.dss.enumerations.LoTEServiceStatus;
import eu.europa.esig.dss.enumerations.LoTEServiceTypeIdentifier;
import eu.europa.esig.dss.enumerations.ValidationTime;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.validation.process.Chain;
import eu.europa.esig.dss.validation.process.ChainItem;
import eu.europa.esig.dss.validation.process.ValidationProcessUtils;
import eu.europa.esig.dss.validation.process.qualification.certificate.usage.checks.TrustedEntityServiceAtTimeCheck;
import eu.europa.esig.dss.validation.process.qualification.certificate.usage.checks.TrustedEntityServiceStatusConsistencyCheck;
import eu.europa.esig.dss.validation.process.qualification.certificate.usage.checks.TrustedEntityServiceStatusKnownCheck;
import eu.europa.esig.dss.validation.process.qualification.certificate.usage.checks.TrustedEntityServiceTypeIdentifierKnownCheck;
import eu.europa.esig.dss.validation.process.qualification.trust.filter.TrustedEntitiesFilterFactory;
import eu.europa.esig.dss.validation.process.qualification.trust.filter.TrustedEntityServiceFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CertificateUsageAtTimeBlock extends Chain<XmlValidationCertificateUsage> {

    private static final Logger LOG = LoggerFactory.getLogger(CertificateUsageAtTimeBlock.class);

    /** The time type to get the qualification at */
    private final ValidationTime validationTime;

    /** The time to check against */
    private final Date date;

    /** Corresponding LoTE Type URI */
    private final String listTypeUri;

    /** Service Type Identifier URI */
    private final String stiUri;

    /** List of matching TrustServices */
    private final List<TrustedEntityServiceWrapper> acceptableServices;

    /** Cached list of filtered services */
    private List<TrustedEntityServiceWrapper> filteredServices;

    /**
     * Constructor to instantiate the validation at the certificate's issuance time
     *
     * @param i18nProvider {@link I18nProvider}
     * @param validationTime {@link ValidationTime}
     * @param signingCertificate {@link CertificateWrapper} to get qualification for
     * @param acceptableServices list of {@link TrustedEntityServiceWrapper}s
     */
    public CertificateUsageAtTimeBlock(I18nProvider i18nProvider, ValidationTime validationTime,
                                       CertificateWrapper signingCertificate, String listTypeUri, String stiUri, List<TrustedEntityServiceWrapper> acceptableServices) {
        this(i18nProvider, validationTime, null, signingCertificate, listTypeUri, stiUri, acceptableServices);
    }

    /**
     * Constructor to instantiate the validation at the validation time
     *
     * @param i18nProvider {@link I18nProvider}
     * @param validationTime {@link ValidationTime}
     * @param date {@link Date}
     * @param signingCertificate {@link CertificateWrapper} to get qualification for
     * @param acceptableServices list of {@link TrustedEntityServiceWrapper}s
     */
    public CertificateUsageAtTimeBlock(I18nProvider i18nProvider, ValidationTime validationTime, Date date,
                                       CertificateWrapper signingCertificate, String listTypeUri, String stiUri, List<TrustedEntityServiceWrapper> acceptableServices) {
        super(i18nProvider, new XmlValidationCertificateUsage());
        result.setId(signingCertificate.getId());

        this.validationTime = validationTime;
        this.listTypeUri = listTypeUri;
        this.stiUri = stiUri;
        this.acceptableServices = new ArrayList<>(acceptableServices);

        switch (validationTime) {
            case CERTIFICATE_ISSUANCE_TIME:
                this.date = signingCertificate.getNotBefore();
                break;
            case BEST_SIGNATURE_TIME:
            case VALIDATION_TIME:
                this.date = date;
                break;
            default:
                throw new IllegalArgumentException("Unknown qualification time : " + validationTime);
        }
    }

    @Override
    protected String buildChainTitle() {
        MessageTag message = MessageTag.CERT_USAGE_AT_TIME;
        MessageTag param = ValidationProcessUtils.getValidationTimeMessageTag(validationTime);
        return i18nProvider.getMessage(message, getStiUserFriendlyLabel(), param);
    }

    private String getStiUserFriendlyLabel() {
        LoTEServiceTypeIdentifier sti = LoTEServiceTypeIdentifier.fromUri(stiUri);
        if (sti != null && sti.getLabel() != null) {
            return sti.getLabel();
        }
        return stiUri;
    }

    @Override
    protected void initChain() {

        // Init internal variable to the provided list of extracted Trust Services
        filteredServices = new ArrayList<>(acceptableServices);

        // 1b. Filter by date
        TrustedEntityServiceFilter filterByDate = TrustedEntitiesFilterFactory.createFilterByDate(date);
        filteredServices = filterByDate.filter(filteredServices);

        ChainItem<XmlValidationCertificateUsage> item = firstItem = hasTrustedServiceAtTime(filteredServices);

        item = item.setNextItem(trustedServiceTypeIdentifierKnown(stiUri));

        // 2a. Check status consistency
        item = item.setNextItem(hasTrustedServicesConsistent(filteredServices));

        String serviceStatusUri = getServiceStatusUri(filteredServices);
        if (serviceStatusUri != null) {
            item = item.setNextItem(trustedServiceStatusKnown(serviceStatusUri));
        }
        // NOTE: status can be null, validate successfully in this case

    }

    @Override
    protected void addAdditionalInfo() {
        XmlCertificateUsage certificateUsage = new XmlCertificateUsage();
        ListType listType = ListType.fromUri(listTypeUri);
        certificateUsage.setListType(listType);
        LoTEServiceTypeIdentifier sti = LoTEServiceTypeIdentifier.fromUri(stiUri);
        certificateUsage.setServiceTypeIdentifier(sti);
        String serviceStatusUri = getServiceStatusUri(filteredServices);
        LoTEServiceStatus status = LoTEServiceStatus.fromUri(serviceStatusUri);
        certificateUsage.setServiceStatus(status);

        CertificateUsage certUsage = CertificateUsage.fromDefinition(listType, sti, status);
        if (certUsage == null) {
            certUsage = CertificateUsageEnum.CERT_FOR_UNKNOWN;
        }
        certificateUsage.setLabel(certUsage.getLabel());
        result.setCertificateUsage(certificateUsage);

        result.setValidationTime(validationTime);
        result.setDateTime(date);
    }

    private ChainItem<XmlValidationCertificateUsage> hasTrustedServiceAtTime(List<TrustedEntityServiceWrapper> trustedServices) {
        return new TrustedEntityServiceAtTimeCheck(i18nProvider, result, trustedServices, validationTime, getFailLevelRule());
    }

    private ChainItem<XmlValidationCertificateUsage> trustedServiceTypeIdentifierKnown(String serviceStatusUri) {
        return new TrustedEntityServiceTypeIdentifierKnownCheck(i18nProvider, result, serviceStatusUri, getWarnLevelRule());
    }

    private ChainItem<XmlValidationCertificateUsage> hasTrustedServicesConsistent(List<TrustedEntityServiceWrapper> trustedServices) {
        return new TrustedEntityServiceStatusConsistencyCheck(i18nProvider, result, trustedServices, getFailLevelRule());
    }

    private ChainItem<XmlValidationCertificateUsage> trustedServiceStatusKnown(String serviceStatusUri) {
        return new TrustedEntityServiceStatusKnownCheck(i18nProvider, result, serviceStatusUri, getWarnLevelRule());
    }

    private String getServiceStatusUri(List<TrustedEntityServiceWrapper> filteredServices) {
        Set<String> statusSet = filteredServices.stream().map(TrustedSourceServiceWrapper::getStatus).collect(Collectors.toSet());
        if (Utils.collectionSize(statusSet) == 0) {
            return null;
        } else if (Utils.collectionSize(statusSet) == 1) {
            return statusSet.iterator().next();
        }
        LOG.warn("Conflict in service statuses detected!");
        return "?";
    }
    
}
