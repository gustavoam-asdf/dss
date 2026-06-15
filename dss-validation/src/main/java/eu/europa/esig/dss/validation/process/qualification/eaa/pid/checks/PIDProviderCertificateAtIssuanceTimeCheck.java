package eu.europa.esig.dss.validation.process.qualification.eaa.pid.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationPIDQualificationProcess;
import eu.europa.esig.dss.enumerations.CertificateUsage;
import eu.europa.esig.dss.enumerations.CertificateUsageEnum;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;
import eu.europa.esig.dss.validation.process.ChainItem;

/**
 * Verifies whether the certificate's usage corresponds to a certificate for PID issuance at the certificate issuance time
 *
 */
public class PIDProviderCertificateAtIssuanceTimeCheck extends ChainItem<XmlValidationPIDQualificationProcess> {

    /** Certificate qualification at signing time */
    private final CertificateUsage certificateUsageAtIssuanceTime;

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlValidationPIDQualificationProcess}
     * @param certificateUsageAtIssuanceTime {@link CertificateUsage}
     * @param constraint {@link LevelRule}
     */
    public PIDProviderCertificateAtIssuanceTimeCheck(I18nProvider i18nProvider, XmlValidationPIDQualificationProcess result,
                                                     CertificateUsage certificateUsageAtIssuanceTime, LevelRule constraint) {
        super(i18nProvider, result, constraint);
        this.certificateUsageAtIssuanceTime = certificateUsageAtIssuanceTime;
    }

    @Override
    protected boolean process() {
        return CertificateUsageEnum.PID_PROVIDER == certificateUsageAtIssuanceTime;
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.PID_PROVIDER_AT_ISSUANCE_TIME;
    }

    @Override
    protected MessageTag getErrorMessageTag() {
        return MessageTag.PID_PROVIDER_AT_ISSUANCE_TIME_ANS;
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
