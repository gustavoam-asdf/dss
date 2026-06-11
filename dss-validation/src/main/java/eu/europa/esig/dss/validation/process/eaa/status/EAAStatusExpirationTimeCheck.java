package eu.europa.esig.dss.validation.process.eaa.status;

import eu.europa.esig.dss.detailedreport.jaxb.XmlSAV;
import eu.europa.esig.dss.diagnostic.EAAStatusTokenWrapper;
import eu.europa.esig.dss.diagnostic.EAAStatusWrapper;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;
import eu.europa.esig.dss.validation.process.ChainItem;

/**
 * Verifies whether the EAA status token contains an expiration time
 *
 */
public class EAAStatusExpirationTimeCheck extends ChainItem<XmlSAV> {

    /** EAA status token to check */
    private final EAAStatusTokenWrapper eaaStatusToken;

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlSAV}
     * @param eaaStatusToken {@link EAAStatusWrapper}
     * @param constraint {@link LevelRule}
     */
    public EAAStatusExpirationTimeCheck(I18nProvider i18nProvider, XmlSAV result,
                                        EAAStatusTokenWrapper eaaStatusToken, LevelRule constraint) {
        super(i18nProvider, result, constraint);
        this.eaaStatusToken = eaaStatusToken;
    }

    @Override
    protected boolean process() {
        return eaaStatusToken.getExpirationTime() != null;
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.EAA_STATUS_EXP;
    }

    @Override
    protected MessageTag getErrorMessageTag() {
        return MessageTag.EAA_STATUS_EXP_ANS;
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
