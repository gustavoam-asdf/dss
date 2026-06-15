package eu.europa.esig.dss.validation.process.eaa.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlSAV;
import eu.europa.esig.dss.diagnostic.EAAWrapper;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;
import eu.europa.esig.dss.validation.process.ChainItem;

/**
 * This class verifies whether the SD-JWT EAA contains the claim "vct#integrity"
 */
public class EAATypeIntegrityPresentCheck extends ChainItem<XmlSAV> {

    /** EAA to check */
    private final EAAWrapper eaa;

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlSAV}
     * @param eaa {@link EAAWrapper}
     * @param constraint {@link LevelRule}
     */
    public EAATypeIntegrityPresentCheck(I18nProvider i18nProvider, XmlSAV result,
                                        EAAWrapper eaa, LevelRule constraint) {
        super(i18nProvider, result, constraint);
        this.eaa = eaa;
    }

    @Override
    protected boolean process() {
        return eaa.getEAAVerifiableCredentialsTypeIntegrityDigestAlgorithm() != null && eaa.getEAAVerifiableCredentialsTypeIntegrityBytes() != null;
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.SDJWT_EAA_VCT_INT_PRESENT;
    }

    @Override
    protected MessageTag getErrorMessageTag() {
        return MessageTag.SDJWT_EAA_VCT_INT_PRESENT_ANS;
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
