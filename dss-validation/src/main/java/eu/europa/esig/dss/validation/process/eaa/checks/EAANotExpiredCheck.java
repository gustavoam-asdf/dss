package eu.europa.esig.dss.validation.process.eaa.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationProcessEAA;
import eu.europa.esig.dss.diagnostic.EAAWrapper;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;
import eu.europa.esig.dss.validation.process.ChainItem;
import eu.europa.esig.dss.validation.process.ValidationProcessUtils;

import java.util.Date;

/**
 * Verified whether the validation time is within EAA technical validity period range
 *
 */
public class EAANotExpiredCheck extends ChainItem<XmlValidationProcessEAA> {

    /** EAA to check */
    private final EAAWrapper eaa;

    /** EAA validation time */
    private final Date validationTime;

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlValidationProcessEAA}
     * @param eaa {@link EAAWrapper}
     * @param validationTime {@link Date}
     * @param constraint {@link LevelRule}
     */
    public EAANotExpiredCheck(I18nProvider i18nProvider, XmlValidationProcessEAA result,
                              EAAWrapper eaa, Date validationTime, LevelRule constraint) {
        super(i18nProvider, result, constraint);
        this.eaa = eaa;
        this.validationTime = validationTime;
    }

    @Override
    protected boolean process() {
        return notBefore() && notAtOrAfter();
    }

    private boolean notBefore() {
        /*
         * The "nbf" (not before) claim identifies the time before which the JWT
         * MUST NOT be accepted for processing.  The processing of the "nbf"
         * claim requires that the current date/time MUST be after or equal to
         * the not-before date/time listed in the "nbf" claim.
         */
        return eaa.getEAANotBefore() != null && !validationTime.before(eaa.getEAANotBefore());
    }

    private boolean notAtOrAfter() {
        /*
         * The "exp" (expiration time) claim identifies the expiration time on
         * or after which the JWT MUST NOT be accepted for processing. The
         * processing of the "exp" claim requires that the current date/time
         * MUST be before the expiration date/time listed in the "exp" claim.
         */
        return eaa.getEAAExpiration() != null && validationTime.before(eaa.getEAAExpiration());
    }

    @Override
    protected String buildAdditionalInfo() {
        if (!notBefore() || !notAtOrAfter()) {
            return i18nProvider.getMessage(MessageTag.EAA_VT_ITVR_VALIDITY,
                    ValidationProcessUtils.getFormattedDate(validationTime),
                    ValidationProcessUtils.getFormattedDate(eaa.getEAANotBefore()),
                    ValidationProcessUtils.getFormattedDate(eaa.getEAAExpiration()));
        }
        return null;
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.EAA_VT_ITVR;
    }

    @Override
    protected MessageTag getErrorMessageTag() {
        return MessageTag.EAA_VT_ITVR_ANS;
    }

    @Override
    protected Indication getFailedIndicationForConclusion() {
        return Indication.INDETERMINATE;
    }

    @Override
    protected SubIndication getFailedSubIndicationForConclusion() {
        return SubIndication.OUT_OF_BOUNDS_NO_POE;
    }

}
