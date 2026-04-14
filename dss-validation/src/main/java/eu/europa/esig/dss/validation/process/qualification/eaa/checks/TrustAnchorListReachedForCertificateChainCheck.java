package eu.europa.esig.dss.validation.process.qualification.eaa.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationEAAQualification;
import eu.europa.esig.dss.diagnostic.CertificateWrapper;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;
import eu.europa.esig.dss.validation.process.ChainItem;

/**
 * Checks if one of the trust anchor lists has been reached for the certificate chain
 *
 */
public class TrustAnchorListReachedForCertificateChainCheck extends ChainItem<XmlValidationEAAQualification> {

    /** End-entity certificate */
    private final CertificateWrapper signingCertificate;

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlValidationEAAQualification}
     * @param signingCertificate {@link CertificateWrapper}
     * @param constraint {@link LevelRule}
     */
    public TrustAnchorListReachedForCertificateChainCheck(I18nProvider i18nProvider, XmlValidationEAAQualification result,
                                                          CertificateWrapper signingCertificate, LevelRule constraint) {
        super(i18nProvider, result, constraint);
        this.signingCertificate = signingCertificate;
    }

    @Override
    protected boolean process() {
        return signingCertificate != null && (signingCertificate.isTrustedListReached() || signingCertificate.isListOfTrustedEntitiesReached());
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.EAA_CERT_TRUST_ANCHOR_LIST_REACHED;
    }

    @Override
    protected MessageTag getErrorMessageTag() {
        return MessageTag.EAA_CERT_TRUST_ANCHOR_LIST_REACHED_ANS;
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
