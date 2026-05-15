package eu.europa.esig.dss.validation.process.eaa.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlSAV;
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
public class EAATypeCheck extends AbstractMultiValuesCheckItem<XmlSAV> {

    /** EAA to check */
    private final EAAWrapper eaa;

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlSAV}
     * @param eaa {@link EAAWrapper}
     * @param constraint {@link MultiValuesRule}
     */
    public EAATypeCheck(I18nProvider i18nProvider, XmlSAV result,
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
                String docType = eaa.getEAADocumentType();
                if (docType == null) {
                    // Handle IssuerSigned token
                    docType = eaa.getDocumentType();
                }
                return processValueCheck(docType);
            default:
                throw new UnsupportedOperationException(String.format("The EAA Type '%s' is not supported!", eaa.getEAAType()));
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
