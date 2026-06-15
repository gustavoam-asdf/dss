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
 * This class verifies whether the EAA issuing country claim contains one of the expected values
 *
 */
public class EAAIssuingCountryCheck extends AbstractMultiValuesCheckItem<XmlSAV> {

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
    public EAAIssuingCountryCheck(I18nProvider i18nProvider, XmlSAV result,
                            EAAWrapper eaa, MultiValuesRule constraint) {
        super(i18nProvider, result, constraint);
        this.eaa = eaa;
    }

    @Override
    protected boolean process() {
        return processValueCheck(eaa.getDocumentIssuingAuthorityCountry());
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.EAA_ISS_COUN;
    }

    @Override
    protected MessageTag getErrorMessageTag() {
        return MessageTag.EAA_ISS_COUN_ANS;
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
