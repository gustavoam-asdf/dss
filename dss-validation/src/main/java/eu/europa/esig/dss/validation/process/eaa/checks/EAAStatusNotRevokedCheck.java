package eu.europa.esig.dss.validation.process.eaa.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlSAV;
import eu.europa.esig.dss.diagnostic.EAAStatusWrapper;
import eu.europa.esig.dss.enumerations.EAAStatus;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;
import eu.europa.esig.dss.model.policy.MultiValuesRule;
import eu.europa.esig.dss.validation.process.ChainItem;

/**
 * Checks whether the corresponding status declares that the EAA is not revoked
 *
 */
public class EAAStatusNotRevokedCheck extends ChainItem<XmlSAV> {

    /** EAA status token to check */
    private final EAAStatusWrapper eaaStatusToken;

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlSAV}
     * @param eaaStatusToken {@link EAAStatusWrapper}
     * @param constraint {@link MultiValuesRule}
     */
    public EAAStatusNotRevokedCheck(final I18nProvider i18nProvider, final XmlSAV result,
                                    final EAAStatusWrapper eaaStatusToken, final LevelRule constraint) {
        super(i18nProvider, result, constraint);
        this.eaaStatusToken = eaaStatusToken;
    }

    @Override
    protected boolean process() {
        return eaaStatusToken == null || EAAStatus.INVALID != eaaStatusToken.getStatus();
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.EAA_STATUS_NOT_REV;
    }

    @Override
    protected MessageTag getErrorMessageTag() {
        return MessageTag.EAA_STATUS_NOT_REV_ANS;
    }

    @Override
    protected Indication getFailedIndicationForConclusion() {
        return Indication.FAILED;
    }

    @Override
    protected SubIndication getFailedSubIndicationForConclusion() {
        return SubIndication.REVOKED;
    }

}
