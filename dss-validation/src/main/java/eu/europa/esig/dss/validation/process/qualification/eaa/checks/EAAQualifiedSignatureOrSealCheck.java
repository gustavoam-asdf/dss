package eu.europa.esig.dss.validation.process.qualification.eaa.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlMessage;
import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationEAAQualificationProcess;
import eu.europa.esig.dss.diagnostic.SignatureWrapper;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.SignatureQualification;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;
import eu.europa.esig.dss.validation.process.ChainItem;

/**
 * This class is used to verify whether the EAA Presentation has been created with
 * a qualified electronic signature or seal
 *
 */
public class EAAQualifiedSignatureOrSealCheck extends ChainItem<XmlValidationEAAQualificationProcess> {

    /** Signature to be checked */
    private final SignatureWrapper signature;

    /** The Signature Qualification to be checked */
    private final SignatureQualification signatureQualification;

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlValidationEAAQualificationProcess}
     * @param signature {@link SignatureWrapper}
     * @param signatureQualification {@link SignatureQualification}
     * @param constraint {@link LevelRule}
     */
    public EAAQualifiedSignatureOrSealCheck(I18nProvider i18nProvider, XmlValidationEAAQualificationProcess result,
                                            SignatureWrapper signature, SignatureQualification signatureQualification,
                                            LevelRule constraint) {
        super(i18nProvider, result, constraint);

        this.signature = signature;
        this.signatureQualification = signatureQualification;
    }

    @Override
    protected boolean process() {
        // Indeterminate statuses are handled separately
        return SignatureQualification.QESIG == signatureQualification || SignatureQualification.QESEAL == signatureQualification ||
                SignatureQualification.INDETERMINATE_QESIG == signatureQualification || SignatureQualification.INDETERMINATE_QESEAL == signatureQualification;
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.EAA_SIG_QUAL;
    }

    @Override
    protected MessageTag getErrorMessageTag() {
        return MessageTag.EAA_SIG_QUAL_ANS;
    }

    @Override
    protected XmlMessage buildErrorMessage() {
        return buildXmlMessage(getErrorMessageTag(), signatureQualification.getReadable());
    }

    @Override
    protected String buildAdditionalInfo() {
        return i18nProvider.getMessage(MessageTag.SIGNATURE_ID, signature.getId());
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
