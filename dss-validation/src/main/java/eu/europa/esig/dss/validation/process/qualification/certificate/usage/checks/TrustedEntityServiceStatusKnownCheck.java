package eu.europa.esig.dss.validation.process.qualification.certificate.usage.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationCertificateApprovalStatus;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.LoTEServiceStatus;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;
import eu.europa.esig.dss.validation.process.ChainItem;

public class TrustedEntityServiceStatusKnownCheck extends ChainItem<XmlValidationCertificateApprovalStatus> {

    /** Service Status URI */
    private final String serviceStatusUri;

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlValidationCertificateApprovalStatus}
     * @param serviceStatusUri {@link String}
     * @param constraint {@link LevelRule}
     */
    public TrustedEntityServiceStatusKnownCheck(I18nProvider i18nProvider, XmlValidationCertificateApprovalStatus result,
                                                String serviceStatusUri, LevelRule constraint) {
        super(i18nProvider, result, constraint);
        this.serviceStatusUri = serviceStatusUri;
    }

    @Override
    protected boolean process() {
        LoTEServiceStatus status = LoTEServiceStatus.fromUri(serviceStatusUri);
        return status != null && status.getLabel() != null; // Label is present -> defined
    }

    @Override
    protected String buildAdditionalInfo() {
        return i18nProvider.getMessage(MessageTag.CERTIFICATE_USAGE_STATUS, serviceStatusUri);
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.CERT_USAGE_STATUS_KNOWN;
    }

    @Override
    protected MessageTag getErrorMessageTag() {
        return MessageTag.CERT_USAGE_STATUS_KNOWN_ANS;
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