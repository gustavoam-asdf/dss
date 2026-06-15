package eu.europa.esig.dss.validation.process.qualification.eaa.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationEAAQualificationProcess;
import eu.europa.esig.dss.diagnostic.EAAWrapper;
import eu.europa.esig.dss.enumerations.EAACategory;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;
import eu.europa.esig.dss.validation.process.ChainItem;

/**
 * Verifies whether the EAA payload contains an indication that the attestation has been issued
 * as an EU qualified electronic attestation of attributes
 *
 */
public class EAACategoryForQEAACheck extends ChainItem<XmlValidationEAAQualificationProcess> {

    /** EAA presentation to be checked */
    private final EAAWrapper eaa;

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlValidationEAAQualificationProcess}
     * @param eaa {@link EAAWrapper}
     * @param constraint {@link LevelRule}
     */
    public EAACategoryForQEAACheck(I18nProvider i18nProvider, XmlValidationEAAQualificationProcess result,
                                   EAAWrapper eaa, LevelRule constraint) {
        super(i18nProvider, result, constraint);

        this.eaa = eaa;
    }

    @Override
    protected boolean process() {
        return EAACategory.EU_QEAA.getUrn().equals(eaa.getEAACategory());
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.EAA_CAT_QEAA;
    }

    @Override
    protected MessageTag getErrorMessageTag() {
        return MessageTag.EAA_CAT_QEAA_ANS;
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
