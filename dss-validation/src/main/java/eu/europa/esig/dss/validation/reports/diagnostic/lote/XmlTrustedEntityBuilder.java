package eu.europa.esig.dss.validation.reports.diagnostic.lote;

import eu.europa.esig.dss.diagnostic.CertificateWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlCertificate;
import eu.europa.esig.dss.diagnostic.jaxb.XmlLangAndValue;
import eu.europa.esig.dss.diagnostic.jaxb.XmlMRATrustServiceMapping;
import eu.europa.esig.dss.diagnostic.jaxb.XmlOriginalThirdCountryTrustServiceMapping;
import eu.europa.esig.dss.diagnostic.jaxb.XmlQualifier;
import eu.europa.esig.dss.diagnostic.jaxb.XmlTrustSourceList;
import eu.europa.esig.dss.diagnostic.jaxb.XmlTrustedEntity;
import eu.europa.esig.dss.diagnostic.jaxb.XmlTrustedEntityService;
import eu.europa.esig.dss.enumerations.AdditionalServiceInformation;
import eu.europa.esig.dss.enumerations.QCType;
import eu.europa.esig.dss.enumerations.QCTypeEnum;
import eu.europa.esig.dss.model.lote.ListInfo;
import eu.europa.esig.dss.model.lote.ListOfListsInfo;
import eu.europa.esig.dss.model.lote.ServiceStatusAndInformationExtensions;
import eu.europa.esig.dss.model.lote.TrustedEntity;
import eu.europa.esig.dss.model.lote.TrustedProperties;
import eu.europa.esig.dss.model.timedependent.TimeDependentValues;
import eu.europa.esig.dss.model.tsl.Condition;
import eu.europa.esig.dss.model.tsl.ConditionForQualifiers;
import eu.europa.esig.dss.model.tsl.ServiceEquivalence;
import eu.europa.esig.dss.model.tsl.ServiceTypeASi;
import eu.europa.esig.dss.model.x509.CertificateToken;
import eu.europa.esig.dss.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlTrustedEntityBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(XmlTrustedEntityBuilder.class);

    /**
     * The map of certificates identifiers and their corresponding XML representations
     */
    private final Map<String, XmlCertificate> xmlCertsMap;

    /**
     * The map of Trust Sources
     */
    private final Map<String, XmlTrustSourceList> xmlTrustSourceListsMap;

    /**
     * Default constructor
     *
     * @param xmlCertsMap a map of certificate identifiers and corresponding XML representations
     * @param xmlTrustSourceListsMap a map of trust source list identifiers and corresponding XML representations
     */
    public XmlTrustedEntityBuilder(final Map<String, XmlCertificate> xmlCertsMap,
                                  final Map<String, XmlTrustSourceList> xmlTrustSourceListsMap) {
        this.xmlCertsMap = xmlCertsMap;
        this.xmlTrustSourceListsMap = xmlTrustSourceListsMap;
    }

    /**
     * This method builds a list of {@link XmlTrustedEntity}s corresponding to the given {@code CertificateToken}
     *
     * @param certificateToken {@link CertificateToken} to get a list of {@link XmlTrustedEntity}s
     * @param relatedTrustedProperties a map of trust anchor {@link CertificateToken}s and their corresponding trusted services
     * @return a list of {@link XmlTrustedEntity}s
     */
    public List<XmlTrustedEntity> build(CertificateToken certificateToken,
                                        Map<CertificateToken, List<TrustedProperties>> relatedTrustedProperties) {
        List<XmlTrustedEntity> result = new ArrayList<>();
        for (Map.Entry<CertificateToken, List<TrustedProperties>> entry : relatedTrustedProperties.entrySet()) {
            CertificateToken trustedCert = entry.getKey();
            List<TrustedProperties> services = entry.getValue();

            Map<TrustedEntity, List<TrustedProperties>> servicesByProviders = classifyByServiceProvider(services);

            for (Map.Entry<TrustedEntity, List<TrustedProperties>> servicesByProvider : servicesByProviders
                    .entrySet()) {

                List<TrustedProperties> trustServices = servicesByProvider.getValue();
                if (Utils.isCollectionNotEmpty(trustServices)) {
                    result.add(getXmlTrustedEntity(certificateToken, trustServices, trustedCert));
                }
            }

        }
        return Collections.unmodifiableList(result);
    }

    private Map<TrustedEntity, List<TrustedProperties>> classifyByServiceProvider(
            List<TrustedProperties> trustPropertiesList) {
        Map<TrustedEntity, List<TrustedProperties>> servicesByProviders = new HashMap<>();
        if (Utils.isCollectionNotEmpty(trustPropertiesList)) {
            for (TrustedProperties trustProperties : trustPropertiesList) {
                TrustedEntity currentTrustedEntity = trustProperties.getTrustedEntity();
                List<TrustedProperties> list = servicesByProviders.computeIfAbsent(currentTrustedEntity, k -> new ArrayList<>());
                list.add(trustProperties);
            }
        }
        return servicesByProviders;
    }

    private XmlTrustedEntity getXmlTrustedEntity(CertificateToken certificateToken, List<TrustedProperties> trustServices,
                                                               CertificateToken trustAnchor) {
        TrustedProperties trustProperties = trustServices.iterator().next();

        final XmlTrustedEntity result = new XmlTrustedEntity();

        ListOfListsInfo listOfListsInfo = trustProperties.getListOfListsInfo();
        if (listOfListsInfo != null) {
            XmlTrustSourceList xmlList = xmlTrustSourceListsMap.get(listOfListsInfo.getDSSIdAsString());
            if (xmlList == null) {
                throw new IllegalStateException(String.format("LoLoTE with Id '%s' has not been found! " +
                        "Please verify TrustedListsCertificateSource contains TLValidationSummary.", listOfListsInfo.getDSSIdAsString()));
            }
            result.setLoLoTE(xmlList);
        }
        ListInfo listInfo = trustProperties.getListInfo();
        if (listInfo != null) {
            XmlTrustSourceList xmlList = xmlTrustSourceListsMap.get(listInfo.getDSSIdAsString());
            if (xmlList == null) {
                throw new IllegalStateException(String.format("LoTE with Id '%s' has not been found! " +
                        "Please verify TrustedListsCertificateSource contains TLValidationSummary.", listInfo.getDSSIdAsString()));
            }
            result.setLoTE(xmlList);
        }

        TrustedEntity tsp = trustProperties.getTrustedEntity();
        result.setNames(getLangAndValues(tsp.getNames()));
        result.setTradeNames(getLangAndValues(tsp.getTradeNames()));
        result.setRegistrationIdentifiers(tsp.getRegistrationIdentifiers());

        result.setTrustedEntityServices(buildXmlTrustedEntityServicesList(certificateToken, trustServices, trustAnchor));

        return result;
    }

    private List<XmlLangAndValue> getLangAndValues(Map<String, List<String>> map) {
        if (Utils.isMapNotEmpty(map)) {
            List<XmlLangAndValue> result = new ArrayList<>();
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                String lang = entry.getKey();
                for (String value : entry.getValue()) {
                    XmlLangAndValue langAndValue = new XmlLangAndValue();
                    langAndValue.setLang(lang);
                    langAndValue.setValue(value);
                    result.add(langAndValue);
                }
            }
            return result;
        }
        return null;
    }

    private List<XmlTrustedEntityService> buildXmlTrustedEntityServicesList(CertificateToken certToken, List<TrustedProperties> trustServices,
                                                                            CertificateToken trustAnchor) {
        List<XmlTrustedEntityService> result = new ArrayList<>();

        for (TrustedProperties trustProperties : trustServices) {
            TimeDependentValues<ServiceStatusAndInformationExtensions> trustService =
                    trustProperties.getTrustedServices();
            List<ServiceStatusAndInformationExtensions> serviceStatusAfterOfEqualsCertIssuance =
                    trustService.getAfter(certToken.getNotBefore());
            if (Utils.isCollectionNotEmpty(serviceStatusAfterOfEqualsCertIssuance)) {
                for (ServiceStatusAndInformationExtensions serviceInfoStatus : serviceStatusAfterOfEqualsCertIssuance) {
                    result.add(getXmlTrustedEntityService(serviceInfoStatus, certToken, trustAnchor));
                }
            }
        }
        return Collections.unmodifiableList(result);
    }

    private XmlTrustedEntityService getXmlTrustedEntityService(ServiceStatusAndInformationExtensions serviceInfoStatus,
                                               CertificateToken certToken, CertificateToken trustAnchor) {
        XmlTrustedEntityService trustService = new XmlTrustedEntityService();

        trustService.setServiceDigitalIdentifier(xmlCertsMap.get(trustAnchor.getDSSIdAsString()));
        trustService.setServiceNames(getLangAndValues(serviceInfoStatus.getNames()));
        trustService.setServiceType(serviceInfoStatus.getType());
        trustService.setStatus(serviceInfoStatus.getStatus());
        trustService.setStartDate(serviceInfoStatus.getStartDate());
        trustService.setEndDate(serviceInfoStatus.getEndDate());

        List<XmlQualifier> qualifiers = getQualifiers(serviceInfoStatus, certToken);
        if (Utils.isCollectionNotEmpty(qualifiers)) {
            trustService.setCapturedQualifiers(qualifiers);
        }

        List<String> additionalServiceInfoUris = serviceInfoStatus.getAdditionalServiceInfoUris();
        if (Utils.isCollectionNotEmpty(additionalServiceInfoUris)) {
            trustService.setAdditionalServiceInfoUris(additionalServiceInfoUris);
        }

        List<String> serviceSupplyPoints = serviceInfoStatus.getServiceSupplyPoints();
        if (Utils.isCollectionNotEmpty(serviceSupplyPoints)) {
            trustService.setServiceSupplyPoints(serviceSupplyPoints);
        }

        return trustService;
    }

    /**
     * Retrieves all the qualifiers for which the corresponding conditionEntry is true.
     *
     * @param serviceInfoStatus {@link ServiceStatusAndInformationExtensions}
     * @param certificateToken {@link CertificateToken}
     * @return a list of {@link XmlQualifier}
     */
    private List<XmlQualifier> getQualifiers(ServiceStatusAndInformationExtensions serviceInfoStatus,
                                             CertificateToken certificateToken) {
        LOG.trace("--> GET_QUALIFIERS()");
        final List<XmlQualifier> list = new ArrayList<>();
        final List<ConditionForQualifiers> conditionsForQualifiers = serviceInfoStatus.getConditionsForQualifiers();
        if (Utils.isCollectionNotEmpty(conditionsForQualifiers)) {
            for (ConditionForQualifiers conditionForQualifiers : conditionsForQualifiers) {
                Condition condition = conditionForQualifiers.getCondition();
                if (condition.check(certificateToken)) {
                    for (String qualifier : conditionForQualifiers.getQualifiers()) {
                        list.add(getXmlQualifier(qualifier, conditionForQualifiers.isCritical()));
                    }
                }
            }
        }
        return list;
    }

    private XmlQualifier getXmlQualifier(String value, boolean critical) {
        final XmlQualifier xmlQualifier = new XmlQualifier();
        xmlQualifier.setValue(value);
        xmlQualifier.setCritical(critical);
        return xmlQualifier;
    }

    private boolean checkServiceTypeASi(ServiceStatusAndInformationExtensions serviceInfoStatus, ServiceTypeASi serviceTypeASi) {
        return serviceInfoStatus.getType() != null && serviceInfoStatus.getType().equals(serviceTypeASi.getType()) &&
                (serviceTypeASi.getAsi() == null || serviceInfoStatus.getAdditionalServiceInfoUris().contains(serviceTypeASi.getAsi()));
    }

    private boolean checkCertTypeAsiEquivalence(CertificateToken certToken,
                                                Map<ServiceTypeASi, ServiceTypeASi> typeAsiEquivalenceMap) {
        XmlCertificate xmlCertificate = xmlCertsMap.get(certToken.getDSSIdAsString());
        if (xmlCertificate == null) {
            throw new IllegalStateException(String.format(
                    "XML certificate with Id '%s' is not yet created!", certToken.getDSSIdAsString()));
        }
        if (Utils.isMapEmpty(typeAsiEquivalenceMap)) {
            LOG.debug("No MRA equivalence is defined for Trust Service ASI.");
            return false;
        }

        CertificateWrapper certificateWrapper = new CertificateWrapper(xmlCertificate);
        boolean qcCompliance = certificateWrapper.isQcCompliance();
        List<QCType> qcTypes = certificateWrapper.getQcTypes();
        for (ServiceTypeASi serviceTypeASi : typeAsiEquivalenceMap.values()) {
            if (serviceTypeASi.getAsi() == null) {
                // no aSI -> accept all
                return true;
            }

            if (Utils.isCollectionNotEmpty(qcTypes)) {
                for (QCType qcType : qcTypes) {
                    if (isQcTypeMatch(qcType, serviceTypeASi)) {
                        return true;
                    }
                }

            } else if (qcCompliance) {
                // qcCompliance + no type -> foreSign
                if (isQcTypeMatch(QCTypeEnum.QCT_ESIGN, serviceTypeASi)) {
                    return true;
                }

            } else {
                // no qcType -> accept all
                return true;
            }
        }
        return false;
    }

    private boolean isQcTypeMatch(QCType qcType, ServiceTypeASi serviceTypeASi) {
        String asi = serviceTypeASi.getAsi();
        if (QCTypeEnum.QCT_ESIGN.equals(qcType)) {
            return AdditionalServiceInformation.isForeSignatures(asi);
        } else if (QCTypeEnum.QCT_ESEAL.equals(qcType)) {
            return AdditionalServiceInformation.isForeSeals(asi);
        } else if (QCTypeEnum.QCT_WEB.equals(qcType)) {
            return AdditionalServiceInformation.isForWebAuth(asi);
        }
        return false;
    }

    private boolean checkStatusEquivalence(ServiceStatusAndInformationExtensions serviceInfoStatus,
                                           Map<List<String>, List<String>> statusEquivalenceMap) {
        if (Utils.isMapEmpty(statusEquivalenceMap)) {
            LOG.debug("No MRA equivalence is defined for Trust Service status.");
            return false;
        }
        for (Map.Entry<List<String>, List<String>> statusEquivalence : statusEquivalenceMap.entrySet()) {
            if (statusEquivalence.getKey().contains(serviceInfoStatus.getStatus())) {
                return true;
            }
        }
        return false;
    }

    private XmlMRATrustServiceMapping getXmlMRATrustServiceMapping(ServiceStatusAndInformationExtensions serviceInfoStatus,
                                                                   CertificateToken certToken, ServiceEquivalence serviceEquivalence) {
        XmlMRATrustServiceMapping mraTrustServiceMapping = new XmlMRATrustServiceMapping();
        mraTrustServiceMapping.setTrustServiceLegalIdentifier(serviceEquivalence.getLegalInfoIdentifier());
        mraTrustServiceMapping.setEquivalenceStatusStartingTime(serviceEquivalence.getStartDate());
        mraTrustServiceMapping.setEquivalenceStatusEndingTime(serviceEquivalence.getEndDate());
        mraTrustServiceMapping.setOriginalThirdCountryMapping(getXmlOriginalThirdCountryTrustServiceMapping(serviceInfoStatus, certToken));
        return mraTrustServiceMapping;
    }

    private XmlOriginalThirdCountryTrustServiceMapping getXmlOriginalThirdCountryTrustServiceMapping(
            ServiceStatusAndInformationExtensions serviceInfoStatus, CertificateToken certToken) {
        XmlOriginalThirdCountryTrustServiceMapping originalThirdCountryMapping = new XmlOriginalThirdCountryTrustServiceMapping();
        originalThirdCountryMapping.setServiceType(serviceInfoStatus.getType());
        originalThirdCountryMapping.setStatus(serviceInfoStatus.getStatus());

        List<XmlQualifier> qualifiers = getQualifiers(serviceInfoStatus, certToken);
        if (Utils.isCollectionNotEmpty(qualifiers)) {
            originalThirdCountryMapping.setCapturedQualifiers(qualifiers);
        }

        List<String> additionalServiceInfoUris = serviceInfoStatus.getAdditionalServiceInfoUris();
        if (Utils.isCollectionNotEmpty(additionalServiceInfoUris)) {
            originalThirdCountryMapping.setAdditionalServiceInfoUris(additionalServiceInfoUris);
        }

        return originalThirdCountryMapping;
    }
    
}
