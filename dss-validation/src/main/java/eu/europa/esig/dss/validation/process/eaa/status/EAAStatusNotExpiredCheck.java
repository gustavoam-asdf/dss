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
import eu.europa.esig.dss.validation.process.ValidationProcessUtils;

import java.util.Date;

/**
 * Verifies whether the EAA status is not yet expired
 *
 */
public class EAAStatusNotExpiredCheck extends ChainItem<XmlSAV> {

    /** EAA status token to check */
    private final EAAStatusTokenWrapper eaaStatusToken;

    /** Validation time */
    private final Date validationTime;

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlSAV}
     * @param eaaStatusToken {@link EAAStatusWrapper}
     * @param constraint {@link LevelRule}
     */
    public EAAStatusNotExpiredCheck(I18nProvider i18nProvider, XmlSAV result, EAAStatusTokenWrapper eaaStatusToken,
                                    Date validationTime, LevelRule constraint) {
        super(i18nProvider, result, constraint);
        this.eaaStatusToken = eaaStatusToken;
        this.validationTime = validationTime;
    }

    @Override
    protected boolean process() {
        /*
         * The "exp" (expiration time) claim identifies the expiration time on
         * or after which the JWT MUST NOT be accepted for processing.
         */
        return eaaStatusToken.getExpirationTime() != null && validationTime.before(eaaStatusToken.getExpirationTime());
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.EAA_STATUS_NOT_EXP;
    }

    @Override
    protected MessageTag getErrorMessageTag() {
        return MessageTag.EAA_STATUS_NOT_EXP_ANS;
    }

    @Override
    protected String buildAdditionalInfo() {
        return i18nProvider.getMessage(MessageTag.EAA_STATUS_TIME, ValidationProcessUtils.getFormattedDate(validationTime),
                ValidationProcessUtils.getFormattedDate(eaaStatusToken.getIssuedAt()), ValidationProcessUtils.getFormattedDate(eaaStatusToken.getExpirationTime()));
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
