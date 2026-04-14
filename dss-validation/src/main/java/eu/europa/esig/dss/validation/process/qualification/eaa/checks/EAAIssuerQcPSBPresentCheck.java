package eu.europa.esig.dss.validation.process.qualification.eaa.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationEAAQualificationProcess;
import eu.europa.esig.dss.diagnostic.CertificateWrapper;
import eu.europa.esig.dss.diagnostic.EAAPresentationWrapper;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;
import eu.europa.esig.dss.validation.process.ChainItem;

/**
 * Verifies presence of a QcPSB QcStatement
 *
 */
public class EAAIssuerQcPSBPresentCheck extends ChainItem<XmlValidationEAAQualificationProcess> {

    /** Signing-certificate of the EAA signature */
    private final CertificateWrapper signingCertificate;

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlValidationEAAQualificationProcess}
     * @param signingCertificate {@link EAAPresentationWrapper}
     * @param constraint {@link LevelRule}
     */
    public EAAIssuerQcPSBPresentCheck(I18nProvider i18nProvider, XmlValidationEAAQualificationProcess result,
                                      CertificateWrapper signingCertificate, LevelRule constraint) {
        super(i18nProvider, result, constraint);

        this.signingCertificate = signingCertificate;
    }

    @Override
    public boolean process() {
        return signingCertificate.getQcPSB() != null; // TODO : check country ?
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.EAA_QC_PSB;
    }

    @Override
    protected MessageTag getErrorMessageTag() {
        return MessageTag.EAA_QC_PSB_ANS;
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
