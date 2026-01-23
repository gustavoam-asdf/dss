package eu.europa.esig.dss.lote.xml.parsing.function;

import eu.europa.esig.dss.model.lote.ServiceStatusAndInformationExtensions;
import eu.europa.esig.dss.model.lote.TrustedEntityService;
import eu.europa.esig.dss.model.lote.TrustedEntityServiceStatusAndInformationExtensions;
import eu.europa.esig.dss.model.timedependent.MutableTimeDependentValues;
import eu.europa.esig.dss.model.timedependent.TimeDependentValues;
import eu.europa.esig.dss.model.tsl.ConditionForQualifiers;
import eu.europa.esig.dss.model.x509.CertificateToken;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.lote.jaxb.AttributedNonEmptyURIType;
import eu.europa.esig.lote.jaxb.DigitalIdentityListType;
import eu.europa.esig.lote.jaxb.ExtensionType;
import eu.europa.esig.lote.jaxb.ExtensionsListType;
import eu.europa.esig.lote.jaxb.ServiceHistoryInstanceType;
import eu.europa.esig.lote.jaxb.ServiceSupplyPointsType;
import eu.europa.esig.lote.jaxb.TEServiceInformationType;
import eu.europa.esig.lote.jaxb.TrustedEntityServiceType;
import eu.europa.esig.trustedlist.jaxb.ecc.QualificationElementType;
import eu.europa.esig.trustedlist.jaxb.ecc.QualificationsType;
import eu.europa.esig.trustedlist.jaxb.ecc.QualifierType;
import eu.europa.esig.trustedlist.jaxb.ecc.QualifiersType;
import eu.europa.esig.trustedlist.jaxb.tsl.AdditionalServiceInformationType;
import eu.europa.esig.trustedlist.jaxb.tsl.NonEmptyMultiLangURIType;
import jakarta.xml.bind.JAXBElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

public class TrustedEntityServiceConverter implements Function<TrustedEntityServiceType, TrustedEntityService> {

    private static final Logger LOG = LoggerFactory.getLogger(TrustedEntityServiceConverter.class);

    /**
     * Default constructor
     */
    public TrustedEntityServiceConverter() {
        // empty
    }

    @Override
    public TrustedEntityService apply(TrustedEntityServiceType original) {
        TrustedEntityService.TrustEntityServiceBuilder serviceBuilder = new TrustedEntityService.TrustEntityServiceBuilder();
        if (original.getServiceInformation() != null) {
            serviceBuilder.setCertificates(extractCertificates(original.getServiceInformation()))
                    .setStatusAndInformationExtensions(extractStatusAndHistory(original));
        } else {
            LOG.warn("No mandatory TSPServiceInformation element found within TSPService element!");
        }
        return serviceBuilder.build();
    }

    private List<CertificateToken> extractCertificates(TEServiceInformationType serviceInformation) {
        DigitalIdentityListTypeConverter converter = new DigitalIdentityListTypeConverter();
        DigitalIdentityListType serviceDigitalIdentity = serviceInformation.getServiceDigitalIdentity();
        return Collections.unmodifiableList(converter.apply(serviceDigitalIdentity));
    }

    private TimeDependentValues<ServiceStatusAndInformationExtensions> extractStatusAndHistory(TrustedEntityServiceType original) {
        MutableTimeDependentValues<ServiceStatusAndInformationExtensions> statusHistoryList = new MutableTimeDependentValues<>();

        TEServiceInformationType serviceInfo = original.getServiceInformation();

        InternationalNamesTypeConverter converter = new InternationalNamesTypeConverter();

        TrustedEntityServiceStatusAndInformationExtensions.ServiceStatusAndInformationExtensionsBuilder statusBuilder =
                new TrustedEntityServiceStatusAndInformationExtensions.ServiceStatusAndInformationExtensionsBuilder();
        statusBuilder.setNames(converter.apply(serviceInfo.getServiceName()));
        statusBuilder.setType(serviceInfo.getServiceTypeIdentifier());
        statusBuilder.setStatus(serviceInfo.getServiceStatus());
        statusBuilder.setServiceSupplyPoints(getServiceSupplyPoints(serviceInfo.getServiceSupplyPoints()));

        parseExtensionsList(serviceInfo.getServiceInformationExtensions(), statusBuilder);

        Date nextEndDate = convertToDate(serviceInfo.getStatusStartingTime());
        statusBuilder.setStartDate(nextEndDate);
        statusHistoryList.addOldest(statusBuilder.build());

        if (original.getServiceHistory() != null && Utils.isCollectionNotEmpty(original.getServiceHistory().getServiceHistoryInstance())) {
            for (ServiceHistoryInstanceType serviceHistory : original.getServiceHistory().getServiceHistoryInstance()) {
                if (serviceHistory.getStatusStartingTime() == null) {
                    LOG.warn("No StatusStartingTime is found within a ServiceHistoryInstance element. The entry is skipped.");
                    continue;
                }

                TrustedEntityServiceStatusAndInformationExtensions.ServiceStatusAndInformationExtensionsBuilder statusHistoryBuilder =
                        new TrustedEntityServiceStatusAndInformationExtensions.ServiceStatusAndInformationExtensionsBuilder();
                statusHistoryBuilder.setNames(converter.apply(serviceHistory.getServiceName()));
                statusHistoryBuilder.setType(serviceHistory.getServiceTypeIdentifier());
                statusHistoryBuilder.setStatus(serviceHistory.getServiceStatus());

                parseExtensionsList(serviceHistory.getServiceInformationExtensions(), statusHistoryBuilder);

                statusHistoryBuilder.setEndDate(nextEndDate);
                nextEndDate = convertToDate(serviceHistory.getStatusStartingTime());
                statusHistoryBuilder.setStartDate(nextEndDate);
                statusHistoryList.addOldest(statusHistoryBuilder.build());
            }
        }

        return statusHistoryList;
    }

    private void parseExtensionsList(ExtensionsListType serviceInformationExtensions, TrustedEntityServiceStatusAndInformationExtensions.ServiceStatusAndInformationExtensionsBuilder statusBuilder) {
        if (serviceInformationExtensions != null) {
            statusBuilder.setConditionsForQualifiers(extractConditionsForQualifiers(serviceInformationExtensions.getExtension()));
            statusBuilder.setAdditionalServiceInfoUris(extractAdditionalServiceInfoUris(serviceInformationExtensions.getExtension()));
            statusBuilder.setExpiredCertsRevocationInfo(extractExpiredCertsRevocationInfo(serviceInformationExtensions.getExtension()));
        }
    }

    @SuppressWarnings("rawtypes")
    private List<ConditionForQualifiers> extractConditionsForQualifiers(List<ExtensionType> extensions) {
        List<ConditionForQualifiers> conditionsForQualifiersList = new ArrayList<>();
        for (ExtensionType extensionType : extensions) {
            List<Object> content = extensionType.getContent();
            if (Utils.isCollectionNotEmpty(content)) {
                for (Object object : content) {
                    if (object instanceof JAXBElement) {
                        JAXBElement jaxbElement = (JAXBElement) object;
                        Object objectValue = jaxbElement.getValue();
                        if (objectValue instanceof QualificationsType) {
                            QualificationsType qualifications = (QualificationsType) jaxbElement.getValue();
                            List<ConditionForQualifiers> conditionForQualifiers =
                                    toConditionForQualificationsType(qualifications, extensionType.isCritical());
                            if (Utils.isCollectionNotEmpty(conditionForQualifiers)) {
                                conditionsForQualifiersList.addAll(conditionForQualifiers);
                            }
                        }
                    }
                }
            }
        }
        return conditionsForQualifiersList;
    }

    private List<ConditionForQualifiers> toConditionForQualificationsType(QualificationsType qt, boolean critical) {
        List<ConditionForQualifiers> conditionForQualifiers = new ArrayList<>();
        if ((qt != null) && Utils.isCollectionNotEmpty(qt.getQualificationElement())) {
            for (QualificationElementType qualificationElement : qt.getQualificationElement()) {
                ConditionForQualifiers condition = toConditionForQualifiers(qualificationElement, critical);
                if (condition != null) {
                    conditionForQualifiers.add(condition);
                }
            }
        }
        return conditionForQualifiers;
    }

    private ConditionForQualifiers toConditionForQualifiers(QualificationElementType qualificationElement, boolean critical) {
        // TODO : not supported yet
        return null;
    }

    @SuppressWarnings("rawtypes")
    private List<String> extractAdditionalServiceInfoUris(List<ExtensionType> extensions) {
        List<String> additionalServiceInfos = new ArrayList<>();
        for (ExtensionType extensionType : extensions) {
            List<Object> content = extensionType.getContent();
            if (Utils.isCollectionNotEmpty(content)) {
                for (Object object : content) {
                    if (object instanceof JAXBElement) {
                        JAXBElement jaxbElement = (JAXBElement) object;
                        Object objectValue = jaxbElement.getValue();
                        if (objectValue instanceof AdditionalServiceInformationType) {
                            AdditionalServiceInformationType additionalServiceInfo = (AdditionalServiceInformationType) objectValue;
                            NonEmptyMultiLangURIType uri = additionalServiceInfo.getURI();
                            if (uri != null && Utils.isStringNotBlank(uri.getValue())) {
                                additionalServiceInfos.add(uri.getValue());
                            }
                        }
                    }
                }
            }
        }
        return additionalServiceInfos;
    }

    @SuppressWarnings("rawtypes")
    private Date extractExpiredCertsRevocationInfo(List<ExtensionType> extensions) {
        for (ExtensionType extensionType : extensions) {
            List<Object> content = extensionType.getContent();
            if (Utils.isCollectionNotEmpty(content)) {
                for (Object object : content) {
                    if (object instanceof JAXBElement) {
                        JAXBElement jaxbElement = (JAXBElement) object;
                        // TODO check tag name
                        Object objectValue = jaxbElement.getValue();
                        if (objectValue instanceof XMLGregorianCalendar) {
                            return convertToDate((XMLGregorianCalendar) objectValue);
                        }
                    }
                }
            }
        }
        return null;
    }

    private Date convertToDate(XMLGregorianCalendar gregorianCalendar) {
        if (gregorianCalendar != null) {
            return gregorianCalendar.toGregorianCalendar().getTime();
        }
        return null;
    }

    private List<String> extractQualifiers(QualificationElementType qualificationElement) {
        List<String> qualifiers = new ArrayList<>();
        QualifiersType qualifiersType = qualificationElement.getQualifiers();
        if ((qualifiersType != null) && Utils.isCollectionNotEmpty(qualifiersType.getQualifier())) {
            for (QualifierType qualitierType : qualifiersType.getQualifier()) {
                qualifiers.add(qualitierType.getUri());
            }
        }
        return qualifiers;
    }

    private List<String> getServiceSupplyPoints(ServiceSupplyPointsType serviceSupplyPoints) {
        List<String> result = new ArrayList<>();
        if (serviceSupplyPoints != null && Utils.isCollectionNotEmpty(serviceSupplyPoints.getServiceSupplyPoint())) {
            for (AttributedNonEmptyURIType nonEmptyURI : serviceSupplyPoints.getServiceSupplyPoint()) {
                result.add(nonEmptyURI.getValue());
            }
        }
        return result;
    }

}
