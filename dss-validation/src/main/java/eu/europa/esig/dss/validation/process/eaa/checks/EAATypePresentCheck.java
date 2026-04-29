package eu.europa.esig.dss.validation.process.eaa.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationProcessEAAPresentation;
import eu.europa.esig.dss.diagnostic.EAAPresentationWrapper;
import eu.europa.esig.dss.enumerations.EAAPresentationType;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;
import eu.europa.esig.dss.validation.process.ChainItem;

/**
 * This class verifies whether the EAA Presentation contains a type
 *
 */
public class EAATypePresentCheck extends ChainItem<XmlValidationProcessEAAPresentation> {

    /** EAA Presentation to check */
    private final EAAPresentationWrapper eaaPresentation;

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlValidationProcessEAAPresentation}
     * @param eaaPresentation {@link EAAPresentationWrapper}
     * @param constraint {@link LevelRule}
     */
    public EAATypePresentCheck(I18nProvider i18nProvider, XmlValidationProcessEAAPresentation result,
                               EAAPresentationWrapper eaaPresentation, LevelRule constraint) {
        super(i18nProvider, result, constraint);
        this.eaaPresentation = eaaPresentation;
    }

    @Override
    protected boolean process() {

        switch (eaaPresentation.getEAAType()) {
            case SD_JWT_VC:
                return eaaPresentation.getEAAMetadataUri() != null;
            case ISO_IEC_MDOC:
                return eaaPresentation.getEAADocumentType() != null;
            default:
                return false; // Other EAA types not supported
        }
    }

    @Override
    protected String buildAdditionalInfo() {
        String eaaType;
        if (EAAPresentationType.SD_JWT_VC.equals(eaaPresentation.getEAAType())) {
            eaaType = eaaPresentation.getEAAMetadataUri();
        } else {
            eaaType = eaaPresentation.getEAADocumentType();
        }
        return i18nProvider.getMessage(MessageTag.EEA_TYPE, eaaType);
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.EAA_TYPE_PRESENT;
    }

    @Override
    protected MessageTag getErrorMessageTag() {
        return MessageTag.EAA_TYPE_PRESENT_ANS;
    }

    @Override
    protected Indication getFailedIndicationForConclusion() {
        return Indication.FAILED;
    }

    @Override
    protected SubIndication getFailedSubIndicationForConclusion() {
        return SubIndication.FORMAT_FAILURE;
    }

}
