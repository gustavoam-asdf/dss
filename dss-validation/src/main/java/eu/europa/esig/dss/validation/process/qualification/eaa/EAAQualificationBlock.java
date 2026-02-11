package eu.europa.esig.dss.validation.process.qualification.eaa;

import eu.europa.esig.dss.detailedreport.jaxb.XmlConclusion;
import eu.europa.esig.dss.detailedreport.jaxb.XmlSignature;
import eu.europa.esig.dss.detailedreport.jaxb.XmlTLAnalysis;
import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationEAAQualification;
import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationSignatureQualification;
import eu.europa.esig.dss.diagnostic.CertificateWrapper;
import eu.europa.esig.dss.diagnostic.EAAPresentationWrapper;
import eu.europa.esig.dss.diagnostic.SignatureWrapper;
import eu.europa.esig.dss.diagnostic.TrustServiceWrapper;
import eu.europa.esig.dss.enumerations.EAACategory;
import eu.europa.esig.dss.enumerations.EAAQualification;
import eu.europa.esig.dss.enumerations.SignatureQualification;
import eu.europa.esig.dss.enumerations.ValidationTime;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.validation.process.Chain;
import eu.europa.esig.dss.validation.process.ChainItem;
import eu.europa.esig.dss.validation.process.qualification.certificate.checks.GrantedStatusCheck;
import eu.europa.esig.dss.validation.process.qualification.certificate.checks.RelatedToMraEnactedTrustServiceCheck;
import eu.europa.esig.dss.validation.process.qualification.eaa.checks.EAACategoryForEAAPresenceCheck;
import eu.europa.esig.dss.validation.process.qualification.eaa.checks.EAACategoryForPubEAACheck;
import eu.europa.esig.dss.validation.process.qualification.eaa.checks.EAACategoryForQEAACheck;
import eu.europa.esig.dss.validation.process.qualification.eaa.checks.EAAIssuerQcPSBPresentCheck;
import eu.europa.esig.dss.validation.process.qualification.eaa.checks.EAAQualifiedSignatureOrSealCheck;
import eu.europa.esig.dss.validation.process.qualification.eaa.checks.QEAACheck;
import eu.europa.esig.dss.validation.process.qualification.signature.checks.AcceptableListOfTrustedListsCheck;
import eu.europa.esig.dss.validation.process.qualification.signature.checks.AcceptableTrustedListCheck;
import eu.europa.esig.dss.validation.process.qualification.signature.checks.AcceptableTrustedListPresenceCheck;
import eu.europa.esig.dss.validation.process.qualification.signature.checks.TrustedListReachedForCertificateChainCheck;
import eu.europa.esig.dss.validation.process.qualification.timestamp.checks.GrantedStatusAtTimeCheck;
import eu.europa.esig.dss.validation.process.qualification.trust.filter.TrustServiceFilter;
import eu.europa.esig.dss.validation.process.qualification.trust.filter.TrustServicesFilterFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class is used to verify qualification status of a signature used to create the EAA
 *
 */
public class EAAQualificationBlock extends Chain<XmlValidationEAAQualification> {

    /** The EAA Presentation to be validated */
    private final EAAPresentationWrapper eaaPresentation;

    /** The conclusion of EAA Presentation validation */
    private final XmlConclusion eaaPresentationConclusion;

    /** Map of signature validation processes */
    private final Map<String, XmlSignature> signatureMap;

    /** The list of all TL analyses */
    private final List<XmlTLAnalysis> tlAnalysis;

    /** Validation time */
    private final Date currentTime;

    /** The list of related LOTL/TL analyses */
    private final List<XmlTLAnalysis> relatedTLAnalyses = new ArrayList<>();

    /**
     * Default constructor
     *
     * @param i18nProvider         {@link I18nProvider}
     * @param eaaPresentation      {@link EAAPresentationWrapper} for which qualification is to be determined
     * @param eaaPresentationConclusion {@link XmlConclusion}
     * @param signatureMap         a map of signature validations
     * @param tlAnalysis           a list of performed {@link XmlTLAnalysis}
     * @param currentTime          {@link Date}
     */
    public EAAQualificationBlock(final I18nProvider i18nProvider, final EAAPresentationWrapper eaaPresentation,
                                 final XmlConclusion eaaPresentationConclusion, final Map<String, XmlSignature> signatureMap,
                                 final List<XmlTLAnalysis> tlAnalysis, final Date currentTime) {
        super(i18nProvider, new XmlValidationEAAQualification());
        this.eaaPresentation = eaaPresentation;
        this.eaaPresentationConclusion = eaaPresentationConclusion;
        this.signatureMap = signatureMap;
        this.tlAnalysis = tlAnalysis;
        this.currentTime = currentTime;
    }

    @Override
    protected MessageTag getTitle() {
        return MessageTag.EAA_QUALIFICATION;
    }

    @Override
    protected void initChain() {

        ChainItem<XmlValidationEAAQualification> item = firstItem = categoryPresent();

        EAAQualification claimedQualification = getClaimedQualification();

        if (EAAQualification.QEAA == claimedQualification) {

            item = item.setNextItem(categoryForQEAA());

        } else if (EAAQualification.PUBEAA == claimedQualification) {

            item = item.setNextItem(categoryForPubEAA());

        }

        SignatureQualification signatureQualification = SignatureQualification.NA;

        if (Utils.collectionSize(eaaPresentation.getEAAPresentationSignatures()) != 1) {

            // TODO : diff between N/A and NOT_EAA ?
            claimedQualification = EAAQualification.NOT_EAA;

        } else {

            SignatureWrapper signature = eaaPresentation.getEAAPresentationSignatures().get(0);
            CertificateWrapper signingCertificate = signature.getSigningCertificate();

            item = item.setNextItem(isTrustedListReachedForCertificateChain(signingCertificate));

            if (signingCertificate != null) {

                Set<String> acceptableTLUrls = new HashSet<>();

                if (signingCertificate.isTrustedListReached()) {

                    List<TrustServiceWrapper> originalTSPs = signingCertificate.getTrustServices();

                    Set<String> listOfTrustedListUrls = originalTSPs.stream().filter(t -> t.getListOfTrustedLists() != null)
                            .map(t -> t.getListOfTrustedLists().getUrl()).collect(Collectors.toSet());

                    Set<String> acceptableLOTLUrls = new HashSet<>();
                    for (String lotlURL : listOfTrustedListUrls) {
                        XmlTLAnalysis lotlAnalysis = getTlAnalysis(lotlURL);
                        if (lotlAnalysis != null) {
                            relatedTLAnalyses.add(lotlAnalysis);

                            AcceptableListOfTrustedListsCheck<XmlValidationEAAQualification> acceptableLOTL = isAcceptableLOTL(lotlAnalysis);
                            item = item.setNextItem(acceptableLOTL);
                            if (acceptableLOTL.process()) {
                                acceptableLOTLUrls.add(lotlURL);
                            }
                        }
                    }

                    // filter TLs with a found valid set of LOTLs (if assigned)
                    Set<String> trustedListUrls = originalTSPs.stream().filter(t -> t.getTrustedList() != null &&
                                    (t.getListOfTrustedLists() == null || acceptableLOTLUrls.contains(t.getListOfTrustedLists().getUrl())))
                            .map(t -> t.getTrustedList().getUrl()).collect(Collectors.toSet());

                    if (Utils.isCollectionNotEmpty(trustedListUrls)) {
                        for (String tlURL : trustedListUrls) {
                            XmlTLAnalysis currentTL = getTlAnalysis(tlURL);
                            if (currentTL != null) {
                                relatedTLAnalyses.add(currentTL);

                                AcceptableTrustedListCheck<XmlValidationEAAQualification> acceptableTL = isAcceptableTL(currentTL);
                                item = item.setNextItem(acceptableTL);
                                if (acceptableTL.process()) {
                                    acceptableTLUrls.add(tlURL);
                                }
                            }
                        }
                    }

                    item = item.setNextItem(isAcceptableTLPresent(acceptableTLUrls));

                    if (Utils.isCollectionNotEmpty(acceptableTLUrls)) {

                        TrustServiceFilter filter = TrustServicesFilterFactory.createFilterByUrls(acceptableTLUrls);
                        List<TrustServiceWrapper> filteredServices = filter.filter(originalTSPs);

                        // Execute only for Trusted Lists with defined MRA
                        if (isMRAEnactedForTrustedList(filteredServices)) {
                            filter = TrustServicesFilterFactory.createMRAEnactedFilter();
                            filteredServices = filter.filter(filteredServices);

                            filter = TrustServicesFilterFactory.createFilterByMRAEquivalenceStartingDate(currentTime);
                            filteredServices = filter.filter(filteredServices);

                            item = firstItem = hasMraEnactedTrustService(filteredServices);
                        }

                        // TODO : add filter for EAA and Pub-EAA ?

                        if (EAAQualification.QEAA == claimedQualification) {

                            // 1. filter by service for EAA/Q
                            filter = TrustServicesFilterFactory.createFilterByQEAA();
                            filteredServices = filter.filter(filteredServices);

                            item = item.setNextItem(hasQEAA(filteredServices));

                        }

                        // 2. filter by granted
                        filter = TrustServicesFilterFactory.createFilterByGranted();
                        filteredServices = filter.filter(filteredServices);

                        item = item.setNextItem(hasGrantedStatus(filteredServices));

                        // 3. filter by date (validation time)
                        filter = TrustServicesFilterFactory.createFilterByDate(currentTime);
                        filteredServices = filter.filter(filteredServices);

                        item = item.setNextItem(hasGrantedStatusAtValidationTime(filteredServices));

                        if (Utils.isCollectionEmpty(filteredServices)) {
                            claimedQualification = toNotQualifiedEAA(claimedQualification);
                        }

                    }

                }

                XmlSignature xmlSignature = signatureMap.get(signature.getId());
                if (xmlSignature == null) {
                    throw new IllegalStateException(String.format("Signature validation is not found for Id '%s'", signature.getId()));
                }
                XmlValidationSignatureQualification validationSignatureQualification = xmlSignature.getValidationSignatureQualification();
                if (validationSignatureQualification == null) {
                    throw new IllegalStateException(String.format("Signature qualification validation is not found for Id '%s'", signature.getId()));
                }

                signatureQualification = validationSignatureQualification.getSignatureQualification();

                if (EAAQualification.QEAA == claimedQualification || EAAQualification.PUBEAA == claimedQualification) {

                    item = item.setNextItem(isSignatureQualificationStatusAcceptable(signature, signatureQualification));

                }

                if (EAAQualification.PUBEAA == claimedQualification) {

                    EAAIssuerQcPSBPresentCheck psbEaa = psbEaa(signingCertificate);
                    item = item.setNextItem(psbEaa);

                    if (!psbEaa.process()) {
                        claimedQualification = toNotQualifiedEAA(claimedQualification);
                    }

                }

            }

        }

        determineFinalQualification(claimedQualification, signatureQualification);

    }

    private ChainItem<XmlValidationEAAQualification> categoryPresent() {
        return new EAACategoryForEAAPresenceCheck(i18nProvider, result, eaaPresentation, getInfoLevelRule());
    }

    private ChainItem<XmlValidationEAAQualification> categoryForQEAA() {
        return new EAACategoryForQEAACheck(i18nProvider, result, eaaPresentation, getFailLevelRule());
    }

    private ChainItem<XmlValidationEAAQualification> categoryForPubEAA() {
        return new EAACategoryForPubEAACheck(i18nProvider, result, eaaPresentation, getFailLevelRule());
    }

    private ChainItem<XmlValidationEAAQualification> isSignatureQualificationStatusAcceptable(
            SignatureWrapper signature, SignatureQualification signatureQualification) {
        return new EAAQualifiedSignatureOrSealCheck(i18nProvider, result, signature, signatureQualification, getFailLevelRule());
    }

    private ChainItem<XmlValidationEAAQualification> isTrustedListReachedForCertificateChain(CertificateWrapper signingCertificate) {
        return new TrustedListReachedForCertificateChainCheck<>(i18nProvider, result, signingCertificate, getFailLevelRule());
    }

    private AcceptableListOfTrustedListsCheck<XmlValidationEAAQualification> isAcceptableLOTL(XmlTLAnalysis xmlLOTLAnalysis) {
        return new AcceptableListOfTrustedListsCheck<>(i18nProvider, result, xmlLOTLAnalysis, getWarnLevelRule());
    }

    private AcceptableTrustedListCheck<XmlValidationEAAQualification> isAcceptableTL(XmlTLAnalysis xmlTLAnalysis) {
        return new AcceptableTrustedListCheck<>(i18nProvider, result, xmlTLAnalysis, getWarnLevelRule());
    }

    private ChainItem<XmlValidationEAAQualification> isAcceptableTLPresent(Set<String> acceptableUrls) {
        return new AcceptableTrustedListPresenceCheck<>(i18nProvider, result, acceptableUrls, getFailLevelRule());
    }

    private ChainItem<XmlValidationEAAQualification> hasMraEnactedTrustService(List<TrustServiceWrapper> services) {
        return new RelatedToMraEnactedTrustServiceCheck<>(i18nProvider, result, services, getFailLevelRule());
    }

    private ChainItem<XmlValidationEAAQualification> hasQEAA(List<TrustServiceWrapper> services) {
        return new QEAACheck(i18nProvider, result, services, getFailLevelRule());
    }

    private ChainItem<XmlValidationEAAQualification> hasGrantedStatus(List<TrustServiceWrapper> services) {
        return new GrantedStatusCheck<>(i18nProvider, result, services, getFailLevelRule());
    }

    private ChainItem<XmlValidationEAAQualification> hasGrantedStatusAtValidationTime(List<TrustServiceWrapper> services) {
        return new GrantedStatusAtTimeCheck<>(i18nProvider, result, services, ValidationTime.VALIDATION_TIME, getFailLevelRule());
    }

    private EAAIssuerQcPSBPresentCheck psbEaa(CertificateWrapper certificateWrapper) {
        return new EAAIssuerQcPSBPresentCheck(i18nProvider, result, certificateWrapper, getFailLevelRule());
    }

    private boolean isMRAEnactedForTrustedList(List<TrustServiceWrapper> trustServices) {
        for (TrustServiceWrapper trustService : trustServices) {
            if (Utils.isTrue(trustService.getTrustedList().isMra())) {
                return true;
            }
        }
        return false;
    }

    private XmlTLAnalysis getTlAnalysis(String url) {
        for (XmlTLAnalysis xmlTLAnalysis : tlAnalysis) {
            if (Utils.areStringsEqual(url, xmlTLAnalysis.getURL())) {
                return xmlTLAnalysis;
            }
        }
        return null;
    }

    private EAAQualification getClaimedQualification() {
        String eaaCategory = eaaPresentation.getEAACategory();
        if (EAACategory.EU_QEAA.getUrn().equals(eaaCategory)) {
            return EAAQualification.QEAA;
        } else if (EAACategory.EU_PUBEAA.getUrn().equals(eaaCategory)) {
            return EAAQualification.PUBEAA;
        } else if (eaaCategory == null) {
            /*
             * EAA-5.2.2.1-01: SD-JWT VC EAAs issued by EAAs issuers registered in the European Union,
             * which are neither SD-JWT VC QEAAs nor SD-JWT VC PuB-EAAs, shall not include the category claim.
             */
            return EAAQualification.EAA;
        } else {
            return EAAQualification.UNKNOWN;
        }
    }

    private EAAQualification toNotQualifiedEAA(EAAQualification qualification) {
        if (EAAQualification.QEAA == qualification || EAAQualification.PUBEAA == qualification) {
            return EAAQualification.EAA;
        }
        return qualification;
    }

    private void determineFinalQualification(EAAQualification claimedQualification, SignatureQualification signatureQualification) {
        EAAQualification finalQualification = EAAQualificationMatrix.getEAAQualification(
                eaaPresentationConclusion.getIndication(), claimedQualification, signatureQualification);
        result.setEAAQualification(finalQualification);
    }

}
