package eu.europa.esig.dss.validation.process.eaa.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlSAV;
import eu.europa.esig.dss.diagnostic.CertificateWrapper;
import eu.europa.esig.dss.diagnostic.EAAWrapper;
import eu.europa.esig.dss.diagnostic.RelatedCertificateWrapper;
import eu.europa.esig.dss.diagnostic.SignatureWrapper;
import eu.europa.esig.dss.enumerations.EAAQualification;
import eu.europa.esig.dss.enumerations.EAAType;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.validation.process.ChainItem;
import eu.europa.esig.dss.validation.process.ValidationProcessUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class verifies whether the issuing authority identifier is valid as per TS 119 472-1
 */
public class ETSI194721ConformanceCheck extends ChainItem<XmlSAV> {

    /** EAA to check */
    private final EAAWrapper eaa;

    /** Validation time */
    private final Date validationTime;

    /**
     * Default constructor
     *
     * @param i18nProvider
     *         {@link I18nProvider}
     * @param result
     *         {@link XmlSAV}
     * @param eaa
     *         {@link EAAWrapper}
     * @param validationTime
     *         {@link Date}
     * @param constraint
     *         {@link LevelRule}
     */
    public ETSI194721ConformanceCheck(I18nProvider i18nProvider, XmlSAV result,
                                      EAAWrapper eaa, Date validationTime, LevelRule constraint) {
        super(i18nProvider, result, constraint);
        this.eaa = eaa;
        this.validationTime = validationTime;
    }

    @Override
    protected boolean process() {
        return checkNowAfterNotBefore()
                && checkNowBeforeExpiration()
                && checkNowAfterAdministrativeDateIssuance()
                && checkNowBeforeAdministrativeDateExpiration()
                && checkSDJWTAdministrativeDateConformance()
                && checkMDOCDocumentNumberPresent()
                && checkMDOCIssuingAuthorityPresent()
                && checkSDJWTIssuingAuthorityAndCountryPresent()
                && checkNoStatusIfShortLived()
                && checkStatusIsPresentIfMandatory()
                && checkSDJWTStatusConformance();
    }

    private boolean checkSDJWTIssuingAuthorityAndCountryPresent() {
        if (EAAType.SD_JWT_VC.equals(eaa.getEAAType())) {
            SignatureWrapper eaaSignature = eaa.getEAASignatures().get(0);
            CertificateWrapper signingCertificate = eaaSignature.getSigningCertificate();
            List<RelatedCertificateWrapper> relatedCertificates = eaaSignature.foundCertificates().getRelatedCertificates();

            boolean signCertPresent = signingCertificate != null && Utils.isCollectionNotEmpty(relatedCertificates)
                    && relatedCertificates.stream().anyMatch(c -> signingCertificate.getId().equals(c.getId()));
            if (signCertPresent) {
                if (signingCertificate.isQcCompliance()) {
                    return eaa.getDocumentIssuingAuthority() == null && eaa.getDocumentIssuingAuthorityCountry() == null;
                }
            } else if (eaa.getCategoryQualification().equals(EAAQualification.QEAA)
                    || eaa.getCategoryQualification().equals(EAAQualification.PUBEAA)) {
                // NOTE: TS 119 472-1 v1.2.1 expects a QC for a QEAA/PubEAA, but does not define how to proceed for a not QC
                // Therefore we accept any certificate in such a case
                return eaa.getDocumentIssuingAuthority() != null && eaa.getDocumentIssuingAuthorityCountry() != null;
            }
        }

        return true;
    }

    private boolean checkMDOCIssuingAuthorityPresent() {
        if (EAAType.ISO_IEC_MDOC.equals(eaa.getEAAType())) {
            return eaa.getDocumentIssuingAuthority() != null;
        }

        return true;
    }

    private boolean checkMDOCDocumentNumberPresent() {
        if (EAAType.ISO_IEC_MDOC.equals(eaa.getEAAType())) {
            return eaa.getDocumentNumber() != null;
        }

        return true;
    }

    private boolean checkSDJWTAdministrativeDateConformance() {
        if (EAAType.SD_JWT_VC == eaa.getEAAType()) {
            return (eaa.getAdministrativeIssuanceDate() == null) == (eaa.getAdministrativeExpirationDate() == null);
        }

        return true;
    }

    private boolean checkNowAfterAdministrativeDateIssuance() {
        if (eaa.getAdministrativeIssuanceDate() != null) {
            return !validationTime.before(eaa.getAdministrativeIssuanceDate());
        }

        // Administrative date is optional, return true if not present
        return true;
    }

    private boolean checkNowBeforeAdministrativeDateExpiration() {
        if (eaa.getAdministrativeExpirationDate() != null) {
            return validationTime.before(eaa.getAdministrativeExpirationDate());
        }

        // Administrative date is optional, return true if not present
        return true;
    }

    private boolean checkNowAfterNotBefore() {
        return eaa.getEAANotBefore() != null && !validationTime.before(eaa.getEAANotBefore());
    }

    private boolean checkNowBeforeExpiration() {
        return eaa.getEAAExpiration() != null && validationTime.before(eaa.getEAAExpiration());
    }

    private boolean checkNoStatusIfShortLived() {
        if (Utils.isTrue(eaa.getShortLived())) {
            return eaa.getEAAPayload().getEAAStatus() == null;
        }
        return true;
    }

    private boolean checkStatusIsPresentIfMandatory() {
        if ((eaa.getCategoryQualification().equals(EAAQualification.QEAA) || eaa.getCategoryQualification().equals(EAAQualification.PUBEAA))
                && !Utils.isTrue(eaa.getShortLived())) {
            return eaa.getEAAPayload().getEAAStatus() != null;
        }

        return true;
    }

    private boolean checkSDJWTStatusConformance() {
        // TODO: lax processing until TS 119 472-1 review
//        if (EAAType.SD_JWT_VC == eaa.getEAAType()
//                && eaa.getEAAPayload().getEAAStatus() != null) {
//            return eaa.getEAAStatusUri() != null
//                    && eaa.getEAAStatusIndex() != null
//                    && eaa.getEAAStatusType() != null
//                    && eaa.getEAAStatusPurpose() != null;
//        }
        return true;
    }

    @Override
    protected String buildAdditionalInfo() {
        List<String> errors = new ArrayList<>();
        if (!checkNowAfterNotBefore()) {
            errors.add(i18nProvider.getMessage(MessageTag.EAA_NOW_BEFORE_NBF,
                    ValidationProcessUtils.getFormattedDate(validationTime),
                    ValidationProcessUtils.getFormattedDate(eaa.getEAANotBefore())));
        }
        if (!checkNowBeforeExpiration()) {
            errors.add(i18nProvider.getMessage(MessageTag.EAA_NOW_AFTER_EXP,
                    ValidationProcessUtils.getFormattedDate(validationTime),
                    ValidationProcessUtils.getFormattedDate(eaa.getEAAExpiration())));
        }
        if (!checkNowAfterAdministrativeDateIssuance()) {
            errors.add(i18nProvider.getMessage(MessageTag.EAA_NOW_BEFORE_ADI,
                    ValidationProcessUtils.getFormattedDate(validationTime),
                    ValidationProcessUtils.getFormattedDate(eaa.getAdministrativeIssuanceDate())));
        }
        if (!checkNowBeforeAdministrativeDateExpiration()) {
            errors.add(i18nProvider.getMessage(MessageTag.EAA_NOW_AFTER_ADE,
                    ValidationProcessUtils.getFormattedDate(validationTime),
                    ValidationProcessUtils.getFormattedDate(eaa.getAdministrativeExpirationDate())));
        }
        if (!checkMDOCDocumentNumberPresent()) {
            errors.add(i18nProvider.getMessage(MessageTag.EAA_MDOC_DOCUMENT_NUMBER_ABSENT));
        }
        if (!checkMDOCIssuingAuthorityPresent()) {
            errors.add(i18nProvider.getMessage(MessageTag.EAA_MDOC_ISSUING_AUTHORITY));
        }
        if (!checkSDJWTIssuingAuthorityAndCountryPresent()) {
            errors.add(i18nProvider.getMessage(MessageTag.EAA_SDJWT_ISSUING_AUTHORITY));
        }
        if (!checkSDJWTAdministrativeDateConformance()) {
            errors.add(i18nProvider.getMessage(MessageTag.EAA_AD_SDJWT_CONFORMANCE));
        }
        if (!checkNoStatusIfShortLived()) {
            errors.add(i18nProvider.getMessage(MessageTag.EAA_SHORT_LIVED_STATUS_PRESENT));
        }
        if (!checkStatusIsPresentIfMandatory()) {
            errors.add(i18nProvider.getMessage(MessageTag.EAA_MANDATORY_STATUS_ABSENT));
        }
        if (!checkSDJWTStatusConformance()) {
            errors.add(i18nProvider.getMessage(MessageTag.EAA_STATUS_SDJWT_CONFORMANCE));
        }

        return Utils.joinStrings(errors, " - ");
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
