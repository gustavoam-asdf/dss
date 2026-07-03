package eu.europa.esig.dss.validation.process.qualification.certificate.usage.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationCertificateApprovalStatus;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.LoTEServiceTypeIdentifier;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;
import eu.europa.esig.dss.validation.process.ChainItem;

/**
 * Verifies whether the type identifier of the trusted entity service is known
 *
 */
public class TrustedEntityServiceTypeIdentifierKnownCheck extends ChainItem<XmlValidationCertificateApprovalStatus> {

    /** Service Type Identifier URI */
    private final String stiUri;

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlValidationCertificateApprovalStatus}
     * @param stiUri {@link String}
     * @param constraint {@link LevelRule}
     */
    public TrustedEntityServiceTypeIdentifierKnownCheck(I18nProvider i18nProvider, XmlValidationCertificateApprovalStatus result,
                                                        String stiUri, LevelRule constraint) {
        super(i18nProvider, result, constraint);
        this.stiUri = stiUri;
    }

    @Override
    protected boolean process() {
        LoTEServiceTypeIdentifier status = LoTEServiceTypeIdentifier.fromUri(stiUri);
        return status != null && status.getLabel() != null; // Label is present -> defined
    }

    @Override
    protected String buildAdditionalInfo() {
        return i18nProvider.getMessage(MessageTag.CERTIFICATE_USAGE_STI, stiUri);
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.CERT_USAGE_STI_KNOWN;
    }

    @Override
    protected MessageTag getErrorMessageTag() {
        return MessageTag.CERT_USAGE_STI_ANS;
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