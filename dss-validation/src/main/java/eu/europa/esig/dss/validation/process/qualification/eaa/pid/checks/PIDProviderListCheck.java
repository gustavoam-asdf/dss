package eu.europa.esig.dss.validation.process.qualification.eaa.pid.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationPIDQualificationProcess;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.ListType;
import eu.europa.esig.dss.enumerations.LoTETypeEnum;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;
import eu.europa.esig.dss.validation.process.ChainItem;

/**
 * Verifies whether the List of Trusted Entities is of PID Providers list type
 * 
 */
public class PIDProviderListCheck extends ChainItem<XmlValidationPIDQualificationProcess> {

    /** List Type URI */
    private final String listTypeUri;

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlValidationPIDQualificationProcess}
     * @param listTypeUri {@link String}
     * @param constraint {@link LevelRule}
     */
    public PIDProviderListCheck(final I18nProvider i18nProvider, final XmlValidationPIDQualificationProcess result,
                                final String listTypeUri, final LevelRule constraint) {
        super(i18nProvider, result, constraint);
        this.listTypeUri = listTypeUri;
    }

    @Override
    public boolean process() {
        ListType listType = ListType.fromUri(listTypeUri);
        return LoTETypeEnum.EUPIDProvidersList == listType;
    }

    @Override
    protected String buildAdditionalInfo() {
        return i18nProvider.getMessage(MessageTag.CERTIFICATE_USAGE_LIST_TYPE, listTypeUri);
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.PID_LOTE_TYPE_PID_PROVIDERS;
    }

    @Override
    protected MessageTag getErrorMessageTag() {
        return MessageTag.PID_LOTE_TYPE_PID_PROVIDERS_ANS;
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