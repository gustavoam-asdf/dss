package eu.europa.esig.dss.validation.process.qualification.eaa.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlMessage;
import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationEAAQualificationProcess;
import eu.europa.esig.dss.diagnostic.EAAPresentationWrapper;
import eu.europa.esig.dss.enumerations.EAACategory;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;
import eu.europa.esig.dss.validation.process.ChainItem;

import java.util.Arrays;

/**
 * Verifies whether the EAA payload contains an indication that the attestation has been issued
 * as an EU non-qualified electronic attestation of attributes
 *
 */
public class EAACategoryForEAAPresenceCheck extends ChainItem<XmlValidationEAAQualificationProcess> {

    /** EAA presentation to be checked */
    private final EAAPresentationWrapper eaaPresentation;

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlValidationEAAQualificationProcess}
     * @param eaaPresentation {@link EAAPresentationWrapper}
     * @param constraint {@link LevelRule}
     */
    public EAACategoryForEAAPresenceCheck(I18nProvider i18nProvider, XmlValidationEAAQualificationProcess result,
                                          EAAPresentationWrapper eaaPresentation, LevelRule constraint) {
        super(i18nProvider, result, constraint);

        this.eaaPresentation = eaaPresentation;
    }

    @Override
    protected boolean process() {
        return eaaPresentation.getEAACategory() != null &&
                Arrays.stream(EAACategory.values()).anyMatch(c -> c.getUrn().equals(eaaPresentation.getEAACategory()));
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.EAA_CAT_EAA;
    }

    @Override
    protected XmlMessage buildErrorMessage() {
        if (eaaPresentation.getEAACategory() == null) {
            return buildXmlMessage(MessageTag.EAA_CAT_EAA_ANS_1);
        } else {
            return buildXmlMessage(MessageTag.EAA_CAT_EAA_ANS_2, eaaPresentation.getEAACategory());
        }
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
