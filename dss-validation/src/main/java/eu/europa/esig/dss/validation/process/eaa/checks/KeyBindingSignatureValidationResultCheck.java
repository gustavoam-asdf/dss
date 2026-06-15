package eu.europa.esig.dss.validation.process.eaa.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlConclusion;
import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraintsConclusion;
import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationProcessEAA;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;
import eu.europa.esig.dss.validation.process.qualification.signature.checks.SignatureValidationResultCheck;

/**
 * Performs verification of the validation result of the key binding signature
 *
 */
public class KeyBindingSignatureValidationResultCheck extends SignatureValidationResultCheck<XmlValidationProcessEAA> {

    /**
     * Default constructor
     *
     * @param i18nProvider              {@link I18nProvider}
     * @param result                    {@link XmlConstraintsConclusion}
     * @param basicValidationConclusion {@link XmlConclusion}
     * @param constraint                {@link LevelRule}
     */
    public KeyBindingSignatureValidationResultCheck(I18nProvider i18nProvider, XmlValidationProcessEAA result,
                                                    XmlConclusion basicValidationConclusion, LevelRule constraint) {
        super(i18nProvider, result, basicValidationConclusion, constraint);
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.EAA_KBRC;
    }

    @Override
    protected MessageTag getErrorMessageTag() {
        return MessageTag.EAA_KBRC_ANS;
    }

}
