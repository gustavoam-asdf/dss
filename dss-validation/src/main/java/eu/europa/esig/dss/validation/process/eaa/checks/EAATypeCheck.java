package eu.europa.esig.dss.validation.process.eaa.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationProcessEAAPresentation;
import eu.europa.esig.dss.diagnostic.EAAPresentationWrapper;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.MultiValuesRule;
import eu.europa.esig.dss.validation.process.bbb.AbstractMultiValuesCheckItem;

/**
 * This class verifies whether the EAA Presentation contains an acceptable EAA type
 *
 */
public class EAATypeCheck extends AbstractMultiValuesCheckItem<XmlValidationProcessEAAPresentation> {

    /** EAA Presentation to check */
    private final EAAPresentationWrapper eaaPresentation;

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlValidationProcessEAAPresentation}
     * @param eaaPresentation {@link EAAPresentationWrapper}
     * @param constraint {@link MultiValuesRule}
     */
    public EAATypeCheck(I18nProvider i18nProvider, XmlValidationProcessEAAPresentation result,
                        EAAPresentationWrapper eaaPresentation, MultiValuesRule constraint) {
        super(i18nProvider, result, constraint);
        this.eaaPresentation = eaaPresentation;
    }

    @Override
    protected boolean process() {
        switch (eaaPresentation.getEAAType()) {
            case SD_JWT_VC:
                return processValueCheck(eaaPresentation.getEAAMetadataUri());
            case ISO_IEC_MDOC:
                return processValueCheck(eaaPresentation.getEAADocumentType());
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
