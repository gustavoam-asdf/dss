package eu.europa.esig.dss.validation.process.eaa.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationProcessEAAPresentation;
import eu.europa.esig.dss.diagnostic.EAAPresentationWrapper;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;
import eu.europa.esig.dss.validation.process.ChainItem;

/**
 * This class verifies whether the EAA Presentation contains an attached key binding signature
 *
 */
public class KeyBindingSignaturePresentCheck extends ChainItem<XmlValidationProcessEAAPresentation> {

    /** EAA Presentation to check */
    private final EAAPresentationWrapper eaaPresentation;

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlValidationProcessEAAPresentation}
     * @param eaaPresentation {@link EAAPresentationWrapper}
     * @param constraint {@link LevelRule}
     */
    public KeyBindingSignaturePresentCheck(I18nProvider i18nProvider, XmlValidationProcessEAAPresentation result,
                                           EAAPresentationWrapper eaaPresentation, LevelRule constraint) {
        super(i18nProvider, result, constraint);
        this.eaaPresentation = eaaPresentation;
    }

    @Override
    protected boolean process() {
        return eaaPresentation.getKeyBindingSignature() != null;
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.EAA_KBSP;
    }

    @Override
    protected MessageTag getErrorMessageTag() {
        return MessageTag.EAA_KBSP_ANS;
    }

    @Override
    protected Indication getFailedIndicationForConclusion() {
        return Indication.FAILED;
    }

    @Override
    protected SubIndication getFailedSubIndicationForConclusion() {
        return SubIndication.FORMAT_FAILURE;
    }

}
