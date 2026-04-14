package eu.europa.esig.dss.validation.process.qualification.eaa.pid.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationPIDQualificationProcess;
import eu.europa.esig.dss.diagnostic.TrustedEntityServiceWrapper;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.validation.process.ChainItem;

import java.util.List;

/**
 * Checks if the list of extracted trusted entity services for PID issuance is not empty
 *
 */
public class PIDIssuanceTrustedEntityServicesCheck extends ChainItem<XmlValidationPIDQualificationProcess> {

    /** Pre-filtered list of trusted entity services for PID issuance */
    private final List<TrustedEntityServiceWrapper> pidIssuanceTrustedEntityServices;

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlValidationPIDQualificationProcess}
     * @param pidIssuanceTrustedEntityServices a list of {@link TrustedEntityServiceWrapper}s
     * @param constraint {@link LevelRule}
     */
    public PIDIssuanceTrustedEntityServicesCheck(final I18nProvider i18nProvider, final XmlValidationPIDQualificationProcess result,
            final List<TrustedEntityServiceWrapper> pidIssuanceTrustedEntityServices, final LevelRule constraint) {
        super(i18nProvider, result, constraint);
        this.pidIssuanceTrustedEntityServices = pidIssuanceTrustedEntityServices;
    }

    @Override
    public boolean process() {
        return Utils.isCollectionNotEmpty(pidIssuanceTrustedEntityServices);
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.PID_STI_PID_ISSUANCE;
    }

    @Override
    protected MessageTag getErrorMessageTag() {
        return MessageTag.PID_STI_PID_ISSUANCE_ANS;
    }

    @Override
    protected Indication getFailedIndicationForConclusion() {
        return Indication.FAILED;
    }

    @Override
    protected SubIndication getFailedSubIndicationForConclusion() {
        return null;
    }

}