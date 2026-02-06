package eu.europa.esig.dss.validation.process.qualification.signature.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlConclusion;
import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraintsConclusion;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;
import eu.europa.esig.dss.validation.process.ChainItem;

/**
 * Performs verification of the Basic Signature validation process result
 *
 * @param <T> {@link XmlConstraintsConclusion}
 */
public class SignatureValidationResultCheck<T extends XmlConstraintsConclusion> extends ChainItem<T> {

    /** Basic Validation conclusion of the signature */
    private final XmlConclusion signatureBasicValidationConclusion;

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlConstraintsConclusion}
     * @param basicValidationConclusion {@link XmlConclusion}
     * @param constraint {@link LevelRule}
     */
    public SignatureValidationResultCheck(final I18nProvider i18nProvider, final T result,
            final XmlConclusion basicValidationConclusion, final LevelRule constraint) {
        super(i18nProvider, result, constraint);
        this.signatureBasicValidationConclusion = basicValidationConclusion;
    }

    @Override
    protected boolean process() {
        return isValidConclusion(signatureBasicValidationConclusion);
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.ADEST_IBSVPSC;
    }

    @Override
    protected MessageTag getErrorMessageTag() {
        return MessageTag.ADEST_IBSVPSC_ANS;
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
