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
 * This class verifies whether the EAA issuing authority claim contains one of the expected values
 *
 */
public class EAAIssuingAuthorityCheck extends AbstractMultiValuesCheckItem<XmlValidationProcessEAA> {

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
    public EAAIssuingAuthorityCheck(final I18nProvider i18nProvider, final XmlValidationProcessEAA result,
                                    final EAAWrapper eaa, final MultiValuesRule constraint) {
        super(i18nProvider, result, constraint);
        this.eaa = eaa;
    }

    @Override
    protected boolean process() {
        return processValueCheck(eaa.getDocumentIssuingAuthority());
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.EAA_ISS_AUTH;
    }

    @Override
    protected MessageTag getErrorMessageTag() {
        return MessageTag.EAA_ISS_AUTH_ANS;
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
