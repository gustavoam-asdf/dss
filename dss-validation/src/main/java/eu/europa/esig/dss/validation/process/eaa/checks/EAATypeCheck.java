package eu.europa.esig.dss.validation.process.eaa.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationProcessEAA;
import eu.europa.esig.dss.diagnostic.EAAWrapper;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.MultiValuesRule;
import eu.europa.esig.dss.validation.process.bbb.AbstractMultiValuesCheckItem;

/**
 * This class verifies whether the EAA contains an acceptable EAA type
 *
 */
public class EAATypeCheck extends AbstractMultiValuesCheckItem<XmlValidationProcessEAA> {

    /** EAA to check */
    private final EAAWrapper eaa;

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlValidationProcessEAA}
     * @param eaa {@link EAAWrapper}
     * @param constraint {@link MultiValuesRule}
     */
    public EAATypeCheck(I18nProvider i18nProvider, XmlValidationProcessEAA result,
                        EAAWrapper eaa, MultiValuesRule constraint) {
        super(i18nProvider, result, constraint);
        this.eaa = eaa;
    }

    @Override
    protected boolean process() {
        switch (eaa.getEAAType()) {
            case SD_JWT_VC:
                return processValueCheck(eaa.getEAAMetadataUri());
            case ISO_IEC_MDOC:
                return processValueCheck(eaa.getEAADocumentType());
            default:
                return false; // Other EAA types not supported
        }
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.EAA_ACCEPTABLE_TYPE;
    }

    @Override
    protected MessageTag getErrorMessageTag() {
        return MessageTag.EAA_ACCEPTABLE_TYPE_ANS;
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
