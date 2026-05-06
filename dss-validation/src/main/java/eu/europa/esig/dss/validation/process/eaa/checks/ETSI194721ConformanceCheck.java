package eu.europa.esig.dss.validation.process.eaa.checks;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationProcessEAAPresentation;
import eu.europa.esig.dss.diagnostic.CertificateWrapper;
import eu.europa.esig.dss.diagnostic.EAAPresentationWrapper;
import eu.europa.esig.dss.diagnostic.RelatedCertificateWrapper;
import eu.europa.esig.dss.diagnostic.SignatureWrapper;
import eu.europa.esig.dss.enumerations.EAAPresentationType;
import eu.europa.esig.dss.enumerations.EAAQualification;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.validation.process.ChainItem;
import eu.europa.esig.dss.validation.process.ValidationProcessUtils;

/**
 * This class verifies whether the issuing authority identifier is valid as per TS 119 472-1
 */
public class ETSI194721ConformanceCheck extends ChainItem<XmlValidationProcessEAAPresentation> {

    /** EAA Presentation to check */
    private final EAAPresentationWrapper eaaPresentation;

    private final Date now = new Date();

    /**
     * Default constructor
     *
     * @param i18nProvider
     *         {@link I18nProvider}
     * @param result
     *         {@link XmlValidationProcessEAAPresentation}
     * @param eaaPresentation
     *         {@link EAAPresentationWrapper}
     * @param constraint
     *         {@link LevelRule}
     */
    public ETSI194721ConformanceCheck(I18nProvider i18nProvider, XmlValidationProcessEAAPresentation result,
                                      EAAPresentationWrapper eaaPresentation, LevelRule constraint) {
        super(i18nProvider, result, constraint);
        this.eaaPresentation = eaaPresentation;
    }

    @Override
    protected boolean process() {
        return checkNowAfterNotBefore()
                && checkNowBeforeExpiration()
                && checkNowAfterAdministrativeDateIssuance()
                && checkNowBeforeAdministrativeDateExpiration()
                && checkMDOCDocumentNumberPresentCheck()
                && checkSDJWTAdministrativeDateConformance()
                && checkMDOCIssuingAuthorityPresentCheck()
                && checkSDJWTIssuingAuthorityPresentCheck()
                && checkNoStatusIfShortLived()
                && checkStatusIsPresentIfMandatory()
                && checkSDJWTStatusConformance();
    }

    private boolean checkSDJWTIssuingAuthorityPresentCheck() {
        if (EAAPresentationType.SD_JWT_VC.equals(eaaPresentation.getEAAType())) {
            SignatureWrapper eaaSignature = eaaPresentation.getEAAPresentationSignatures().get(0);
            CertificateWrapper signingCertificate = eaaSignature.getSigningCertificate();
            List<RelatedCertificateWrapper> relatedCertificates = eaaSignature.foundCertificates().getRelatedCertificates();
            if (signingCertificate != null && signingCertificate.isQcCompliance() && Utils.isCollectionNotEmpty(relatedCertificates)
                    && relatedCertificates.stream().anyMatch(c -> signingCertificate.getId().equals(c.getId()))) {
                return eaaPresentation.getDocumentIssuingAuthority() == null && eaaPresentation.getDocumentIssuingAuthorityCountry() == null;
            } else if (eaaPresentation.getCategoryQualification().equals(EAAQualification.QEAA)
                    || eaaPresentation.getCategoryQualification().equals(EAAQualification.PUBEAA)) {
                return eaaPresentation.getDocumentIssuingAuthority() != null;
            }
        }

        return true;
    }

    private boolean checkMDOCIssuingAuthorityPresentCheck() {
        if (EAAPresentationType.ISO_IEC_MDOC.equals(eaaPresentation.getEAAType())) {
            return eaaPresentation.getDocumentIssuingAuthority() != null;
        }

        return true;
    }

    private boolean checkMDOCDocumentNumberPresentCheck() {
        if (EAAPresentationType.ISO_IEC_MDOC.equals(eaaPresentation.getEAAType())) {
            return eaaPresentation.getDocumentNumber() != null;
        }

        return true;
    }

    private boolean checkSDJWTAdministrativeDateConformance() {
        if (EAAPresentationType.SD_JWT_VC == eaaPresentation.getEAAType()) {
            return (eaaPresentation.getAdministrativeIssuanceDate() == null) == (eaaPresentation.getAdministrativeExpirationDate() == null);
        }

        return true;
    }

    private boolean checkNowAfterAdministrativeDateIssuance() {
        if (eaaPresentation.getAdministrativeIssuanceDate() != null) {
            return now.after(eaaPresentation.getAdministrativeIssuanceDate());
        }

        // Administrative date is optional, return true if not present
        return true;
    }

    private boolean checkNowBeforeAdministrativeDateExpiration() {
        if (eaaPresentation.getAdministrativeExpirationDate() != null) {
            return now.before(eaaPresentation.getAdministrativeExpirationDate());
        }

        // Administrative date is optional, return true if not present
        return true;
    }

    private boolean checkNowAfterNotBefore() {
        return eaaPresentation.getEAANotBefore() != null && now.after(eaaPresentation.getEAANotBefore());
    }

    private boolean checkNowBeforeExpiration() {
        return eaaPresentation.getEAANotAfter() != null && now.before(eaaPresentation.getEAANotAfter());
    }

    private boolean checkNoStatusIfShortLived() {
        if (Boolean.TRUE.equals(eaaPresentation.getShortLived())) {
            return eaaPresentation.getEAAPayload().getEAAStatus() == null;
        }
        return true;
    }

    private boolean checkStatusIsPresentIfMandatory() {
        if ((eaaPresentation.getCategoryQualification().equals(EAAQualification.QEAA) || eaaPresentation.getCategoryQualification().equals(EAAQualification.PUBEAA))
                && !Boolean.TRUE.equals(eaaPresentation.getShortLived())) {
            return eaaPresentation.getEAAPayload().getEAAStatus() != null;
        }

        return true;
    }

    private boolean checkSDJWTStatusConformance() {
        if (EAAPresentationType.SD_JWT_VC == eaaPresentation.getEAAType()
                && eaaPresentation.getEAAPayload().getEAAStatus() != null) {
            return eaaPresentation.getEAAStatusUri() != null
                    && eaaPresentation.getEAAStatusIndex() != null
                    && eaaPresentation.getEAAStatusType() != null
                    && eaaPresentation.getEAAStatusPurpose() != null;
        }

        return true;
    }

    @Override
    protected String buildAdditionalInfo() {
        List<String> errors = new ArrayList<>();
        if (checkNowAfterNotBefore()) {
            errors.add(i18nProvider.getMessage(MessageTag.EAA_NOW_BEFORE_NBF,
                    ValidationProcessUtils.getFormattedDate(now),
                    ValidationProcessUtils.getFormattedDate(eaaPresentation.getEAANotBefore())));
        }
        if (checkNowBeforeExpiration()) {
            errors.add(i18nProvider.getMessage(MessageTag.EAA_NOW_AFTER_EXP,
                    ValidationProcessUtils.getFormattedDate(now),
                    ValidationProcessUtils.getFormattedDate(eaaPresentation.getEAANotAfter())));
        }
        if (checkNowAfterAdministrativeDateIssuance()) {
            errors.add(i18nProvider.getMessage(MessageTag.EAA_NOW_BEFORE_ADI,
                    ValidationProcessUtils.getFormattedDate(now),
                    ValidationProcessUtils.getFormattedDate(eaaPresentation.getAdministrativeIssuanceDate())));
        }
        if (checkNowBeforeAdministrativeDateExpiration()) {
            errors.add(i18nProvider.getMessage(MessageTag.EAA_NOW_AFTER_ADE,
                    ValidationProcessUtils.getFormattedDate(now),
                    ValidationProcessUtils.getFormattedDate(eaaPresentation.getAdministrativeExpirationDate())));
        }
        if (checkMDOCDocumentNumberPresentCheck()) {
            errors.add(i18nProvider.getMessage(MessageTag.EAA_MDOC_DOCUMENT_NUMBER_ABSENT));
        }
        if (checkMDOCIssuingAuthorityPresentCheck()) {
            errors.add(i18nProvider.getMessage(MessageTag.EAA_MDOC_ISSUING_AUTHORITY));
        }
        if (checkSDJWTIssuingAuthorityPresentCheck()) {
            errors.add(i18nProvider.getMessage(MessageTag.EAA_SDJWT_ISSUING_AUTHORITY));
        }
        if (checkSDJWTAdministrativeDateConformance()) {
            errors.add(i18nProvider.getMessage(MessageTag.EAA_AD_SDJWT_CONFORMANCE));
        }
        if (checkNoStatusIfShortLived()) {
            errors.add(i18nProvider.getMessage(MessageTag.EAA_SHORT_LIVED_STATUS_PRESENT));
        }
        if (checkStatusIsPresentIfMandatory()) {
            errors.add(i18nProvider.getMessage(MessageTag.EAA_MANDATORY_STATUS_ABSENT));
        }
        if (checkSDJWTStatusConformance()) {
            errors.add(i18nProvider.getMessage(MessageTag.EAA_STATUS_SDJWT_CONFORMANCE));
        }

        return String.join(" - ", errors);
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.EAA_ETSI194721;
    }

    @Override
    protected MessageTag getErrorMessageTag() {
        return MessageTag.EAA_ETSI194721_ANS;
    }

    @Override
    protected Indication getFailedIndicationForConclusion() {
        return Indication.INDETERMINATE;
    }

    @Override
    protected SubIndication getFailedSubIndicationForConclusion() {
        return SubIndication.EAA_CONSTRAINTS_FAILURE;
    }

}
