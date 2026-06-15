package eu.europa.esig.dss.validation.process.qualification.eaa.pid.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationPIDQualificationProcess;
import eu.europa.esig.dss.diagnostic.CertificateWrapper;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;
import eu.europa.esig.dss.validation.process.ChainItem;

/**
 * Checks whether a List of Trusted Entities has been reached for the given certificate chain
 *
 */
public class ListOfTrustedEntitiesReachedForCertificateChainCheck extends ChainItem<XmlValidationPIDQualificationProcess> {

    /** End-entity certificate */
    private final CertificateWrapper signingCertificate;

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlValidationPIDQualificationProcess}
     * @param signingCertificate {@link CertificateWrapper}
     * @param constraint {@link LevelRule}
     */
    public ListOfTrustedEntitiesReachedForCertificateChainCheck(I18nProvider i18nProvider, XmlValidationPIDQualificationProcess result,
                                                      CertificateWrapper signingCertificate, LevelRule constraint) {
        super(i18nProvider, result, constraint);
        this.signingCertificate = signingCertificate;
    }

    @Override
    protected boolean process() {
        return signingCertificate != null && signingCertificate.isListOfTrustedEntitiesReached();
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.EAA_CERT_LOTE_REACHED;
    }

    @Override
    protected MessageTag getErrorMessageTag() {
        return MessageTag.EAA_CERT_LOTE_REACHED_ANS;
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
