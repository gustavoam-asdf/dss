package eu.europa.esig.dss.validation.process.eaa.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationProcessEAA;
import eu.europa.esig.dss.diagnostic.EAAWrapper;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;
import eu.europa.esig.dss.validation.process.ChainItem;

/**
 * This class verifies whether the EAA contains the expiration
 *
 */
public class EAAExpirationPresentCheck extends ChainItem<XmlValidationProcessEAA> {

    /** EAA to check */
    private final EAAWrapper eaa;

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlValidationProcessEAA}
     * @param eaa {@link EAAWrapper}
     * @param constraint {@link LevelRule}
     */
    public EAAExpirationPresentCheck(I18nProvider i18nProvider, XmlValidationProcessEAA result,
                                     EAAWrapper eaa, LevelRule constraint) {
        super(i18nProvider, result, constraint);
        this.eaa = eaa;
    }

    @Override
    protected boolean process() {
        return eaa.getEAANotAfter() != null;
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.EAA_EXP_PRESENT;
    }

    @Override
    protected MessageTag getErrorMessageTag() {
        return MessageTag.EAA_EXP_PRESENT_ANS;
    }

    @Override
    protected Indication getFailedIndicationForConclusion() {
        return Indication.INDETERMINATE;
    }

    @Override
    protected SubIndication getFailedSubIndicationForConclusion() {
        return SubIndication.EAA_CONSTRAINTS_FAILURE;
    }

}
