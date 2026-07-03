package eu.europa.esig.dss.validation.process.qualification.eaa.pid.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationPIDQualificationProcess;
import eu.europa.esig.dss.enumerations.CertificateApprovalStatus;
import eu.europa.esig.dss.enumerations.CertificateApprovalStatusEnum;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;
import eu.europa.esig.dss.validation.process.ChainItem;

/**
 * Verifies whether the certificate's usage corresponds to a certificate for PID issuance at the validation time
 *
 */
public class PIDProviderCertificateAtValidationTimeCheck extends ChainItem<XmlValidationPIDQualificationProcess> {

    /** Certificate qualification at signing time */
    private final CertificateApprovalStatus certificateApprovalStatusAtValidationTime;

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlValidationPIDQualificationProcess}
     * @param certificateApprovalStatusAtValidationTime {@link CertificateApprovalStatus}
     * @param constraint {@link LevelRule}
     */
    public PIDProviderCertificateAtValidationTimeCheck(I18nProvider i18nProvider, XmlValidationPIDQualificationProcess result,
                                                       CertificateApprovalStatus certificateApprovalStatusAtValidationTime, LevelRule constraint) {
        super(i18nProvider, result, constraint);
        this.certificateApprovalStatusAtValidationTime = certificateApprovalStatusAtValidationTime;
    }

    @Override
    protected boolean process() {
        return CertificateApprovalStatusEnum.PID_PROVIDER == certificateApprovalStatusAtValidationTime;
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.PID_PROVIDER_AT_VALIDATION_TIME;
    }

    @Override
    protected MessageTag getErrorMessageTag() {
        return MessageTag.PID_PROVIDER_AT_VALIDATION_TIME_ANS;
    }

    @Override
    protected Indication getFailedIndicationForConclusion() {
        return Indication.FAILED;
    }

    @Override
    protected SubIndication getFailedSubIndicationForConclusion() {
        return null;
    }

}
