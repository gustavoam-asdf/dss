package eu.europa.esig.dss.validation.process.qualification.eaa.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationEAAQualification;
import eu.europa.esig.dss.diagnostic.EAAPresentationWrapper;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.validation.process.ChainItem;

/**
 * Verifies whether the EAA has been created with a single signature
 *
 */
public class EAASignaturePresentCheck extends ChainItem<XmlValidationEAAQualification> {

    /** EAA Presentation to check */
    private final EAAPresentationWrapper eaaPresentation;

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlValidationEAAQualification}
     * @param eaaPresentation {@link EAAPresentationWrapper}
     * @param constraint {@link LevelRule}
     */
    public EAASignaturePresentCheck(I18nProvider i18nProvider, XmlValidationEAAQualification result,
                                    EAAPresentationWrapper eaaPresentation, LevelRule constraint) {
        super(i18nProvider, result, constraint);
        this.eaaPresentation = eaaPresentation;
    }

    @Override
    protected boolean process() {
        // TODO : to be implemented in TS 119 472-1. Verify the check later
        return Utils.collectionSize(eaaPresentation.getEAAPresentationSignatures()) == 1;
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.EAA_SIG_PRESENT;
    }

    @Override
    protected MessageTag getErrorMessageTag() {
        return MessageTag.EAA_SIG_PRESENT_ANS;
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
