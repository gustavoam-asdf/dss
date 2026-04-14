package eu.europa.esig.dss.validation.process.qualification.eaa.pid.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlMessage;
import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationPIDQualificationProcess;
import eu.europa.esig.dss.diagnostic.EAAPresentationWrapper;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;
import eu.europa.esig.dss.validation.process.ChainItem;

/**
 * Verifies whether the PID contains person identification data defined within an eIDAS allowed namespace
 *
 */
public class PIDDocumentTypeAcceptableCheck extends ChainItem<XmlValidationPIDQualificationProcess> {

    /** EAA presentation to be checked */
    private final EAAPresentationWrapper eaaPresentation;

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlValidationPIDQualificationProcess}
     * @param eaaPresentation {@link EAAPresentationWrapper}
     * @param constraint {@link LevelRule}
     */
    public PIDDocumentTypeAcceptableCheck(I18nProvider i18nProvider, XmlValidationPIDQualificationProcess result,
                                          EAAPresentationWrapper eaaPresentation, LevelRule constraint) {
        super(i18nProvider, result, constraint);

        this.eaaPresentation = eaaPresentation;
    }

    @Override
    public boolean process() {
        String documentType = getClaimedDocumentType();
        if (documentType == null) {
            return false;
        }
        switch (eaaPresentation.getEAAType()) {
            case SD_JWT_VC:
                return documentType.startsWith("urn:eudi:pid:");
            case ISO_IEC_MDOC:
                // TODO : not clear what element is to be checked
                /*
                 * The attestation type for person identification data in ISO/IEC mdoc format
                 * shall be "eu.europa.ec.eudi.pid.1".
                 */
                return documentType.equals("eu.europa.ec.eudi.pid.1");
            default:
                throw new UnsupportedOperationException(String.format("Not supported EAA Type : '%s'", eaaPresentation.getEAAType()));
        }
    }

    private String getClaimedDocumentType() {
        switch (eaaPresentation.getEAAType()) {
            case SD_JWT_VC:
                return eaaPresentation.getEAAMetadataUri();
            case ISO_IEC_MDOC:
                // TODO : not clear what element is to be checked
                /*
                 * The attestation type for person identification data in ISO/IEC mdoc format
                 * shall be "eu.europa.ec.eudi.pid.1".
                 */
                return eaaPresentation.getEAADocumentType();
            default:
                throw new UnsupportedOperationException(String.format("Not supported EAA Type : '%s'", eaaPresentation.getEAAType()));
        }
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.PID_DOCUMENT_TYPE;
    }

    @Override
    protected XmlMessage buildErrorMessage() {
        return buildXmlMessage(MessageTag.PID_DOCUMENT_TYPE_ANS, getClaimedDocumentType());
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