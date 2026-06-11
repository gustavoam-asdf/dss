package eu.europa.esig.dss.validation.process.eaa.status;

import eu.europa.esig.dss.detailedreport.jaxb.XmlSAV;
import eu.europa.esig.dss.diagnostic.EAAStatusWrapper;
import eu.europa.esig.dss.enumerations.EAAStatus;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;
import eu.europa.esig.dss.validation.process.ChainItem;

/**
 * Verifies whether the obtained EAA status is known
 *
 */
public class EAAStatusKnownCheck extends ChainItem<XmlSAV> {

    /** EAA status token to check */
    private final EAAStatusWrapper eaaStatus;

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlSAV}
     * @param eaaStatus {@link EAAStatusWrapper}
     * @param constraint {@link LevelRule}
     */
    public EAAStatusKnownCheck(I18nProvider i18nProvider, XmlSAV result,
                               EAAStatusWrapper eaaStatus, LevelRule constraint) {
        super(i18nProvider, result, constraint, eaaStatus.getId());
        this.eaaStatus = eaaStatus;
    }

    @Override
    protected boolean process() {
        return EAAStatus.VALID == eaaStatus.getStatus() ||
                EAAStatus.INVALID == eaaStatus.getStatus() ||
                EAAStatus.SUSPENDED == eaaStatus.getStatus();
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.EAA_STATUS_KNOWN;
    }

    @Override
    protected MessageTag getErrorMessageTag() {
        return MessageTag.EAA_STATUS_KNOWN_ANS;
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
