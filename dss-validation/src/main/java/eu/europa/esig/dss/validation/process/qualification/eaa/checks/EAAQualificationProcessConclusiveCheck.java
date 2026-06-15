package eu.europa.esig.dss.validation.process.qualification.eaa.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraintsConclusion;
import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationEAAQualification;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.validation.process.ChainItem;

import java.util.Collection;

/**
 * Verifies whether at least one of the EAA qualification processes concluded with a positive status
 *
 */
public class EAAQualificationProcessConclusiveCheck extends ChainItem<XmlValidationEAAQualification> {

    /** Collection of EAA qualification processes */
    private final Collection<? extends XmlConstraintsConclusion> qualificationProcesses;

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlValidationEAAQualification}
     * @param qualificationProcesses a collection of {@link XmlConstraintsConclusion}
     * @param constraint {@link LevelRule}
     */
    public EAAQualificationProcessConclusiveCheck(final I18nProvider i18nProvider, final XmlValidationEAAQualification result,
                                                  final Collection<? extends XmlConstraintsConclusion> qualificationProcesses,
                                                  final LevelRule constraint) {
        super(i18nProvider, result, constraint);
        this.qualificationProcesses = qualificationProcesses;
    }

    @Override
    protected boolean process() {
        return Utils.isCollectionNotEmpty(qualificationProcesses) && qualificationProcesses.stream().anyMatch(this::isValid);
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.EAA_QUAL_CONCLUSIVE;
    }

    @Override
    protected MessageTag getErrorMessageTag() {
        return MessageTag.EAA_QUAL_CONCLUSIVE_ANS;
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
