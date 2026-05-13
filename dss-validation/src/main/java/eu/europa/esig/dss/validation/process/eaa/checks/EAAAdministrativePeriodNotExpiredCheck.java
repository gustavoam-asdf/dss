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
 * Verified whether the validation time is within EAA administrative validity period range
 *
 */
public class EAAAdministrativePeriodNotExpiredCheck extends ChainItem<XmlValidationProcessEAA> {

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
    public EAAAdministrativePeriodNotExpiredCheck(I18nProvider i18nProvider, XmlValidationProcessEAA result,
                              EAAWrapper eaa, Date validationTime, LevelRule constraint) {
        super(i18nProvider, result, constraint);
        this.eaa = eaa;
        this.validationTime = validationTime;
    }

    @Override
    protected boolean process() {
        return notAdministrativePeriodBefore() && notAdministrativePeriodAtOrAfter();
    }

    private boolean notAdministrativePeriodBefore() {
        /*
         * Same logic is applied as for IETF RFC 7519 "nbf" mutatis mutandis
         */
        return eaa.getAdministrativeIssuanceDate() != null && !validationTime.before(eaa.getAdministrativeIssuanceDate());
    }

    private boolean notAdministrativePeriodAtOrAfter() {
        /*
         * Same logic is applied as for IETF RFC 7519 "exp" mutatis mutandis
         */
        return eaa.getAdministrativeExpirationDate() != null && validationTime.before(eaa.getAdministrativeExpirationDate());
    }

    @Override
    protected String buildAdditionalInfo() {
        if (!notAdministrativePeriodBefore() || !notAdministrativePeriodAtOrAfter()) {
            return i18nProvider.getMessage(MessageTag.EAA_VT_IAVR_VALIDITY,
                    ValidationProcessUtils.getFormattedDate(validationTime),
                    ValidationProcessUtils.getFormattedDate(eaa.getAdministrativeIssuanceDate()),
                    ValidationProcessUtils.getFormattedDate(eaa.getAdministrativeExpirationDate()));
        }
        return null;
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.EAA_VT_IAVR;
    }

    @Override
    protected MessageTag getErrorMessageTag() {
        return MessageTag.EAA_VT_IAVR_ANS;
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
