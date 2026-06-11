package eu.europa.esig.dss.validation.process.eaa.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlConclusion;
import eu.europa.esig.dss.detailedreport.jaxb.XmlSAV;
import eu.europa.esig.dss.diagnostic.EAAStatusWrapper;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;
import eu.europa.esig.dss.model.policy.MultiValuesRule;
import eu.europa.esig.dss.validation.process.ChainItem;

/**
 * Checks whether the EAA status token is acceptable
 *
 */
public class EAAStatusAcceptableCheck extends ChainItem<XmlSAV> {

    /** EAA status token to check */
    private final EAAStatusWrapper eaaStatusToken;

    /** BBB validation result of the token */
    private final XmlConclusion conclusion;

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlSAV}
     * @param eaaStatusToken {@link EAAStatusWrapper}
     * @param conclusion {@link XmlConclusion}
     * @param constraint {@link MultiValuesRule}
     */
    public EAAStatusAcceptableCheck(final I18nProvider i18nProvider, final XmlSAV result,
                                   final EAAStatusWrapper eaaStatusToken, final XmlConclusion conclusion, final LevelRule constraint) {
        super(i18nProvider, result, constraint, eaaStatusToken.getId());
        this.eaaStatusToken = eaaStatusToken;
        this.conclusion = conclusion;
    }

    @Override
    protected boolean process() {
        return isValidConclusion(conclusion);
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.EAA_STATUS_ACC;
    }

    @Override
    protected MessageTag getErrorMessageTag() {
        return MessageTag.EAA_STATUS_ACC_ANS;
    }

    @Override
    protected String buildAdditionalInfo() {
        return i18nProvider.getMessage(MessageTag.TOKEN_ID, eaaStatusToken.getId());
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
