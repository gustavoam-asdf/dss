package eu.europa.esig.dss.validation.process.eaa.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationProcessEAA;
import eu.europa.esig.dss.diagnostic.EAAWrapper;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;
import eu.europa.esig.dss.model.policy.MultiValuesRule;
import eu.europa.esig.dss.validation.process.ChainItem;

/**
 * This class verifies whether the EAA uses a pseudonym
 *
 */
public class EAAPseudonymUsageCheck extends ChainItem<XmlValidationProcessEAA> {

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
    public EAAPseudonymUsageCheck(final I18nProvider i18nProvider, final XmlValidationProcessEAA result,
                                  final EAAWrapper eaa, final LevelRule constraint) {
        super(i18nProvider, result, constraint);
        this.eaa = eaa;
    }

    @Override
    protected boolean process() {
        return eaa.getHolderPseudonym() == null;
    }

    @Override
    protected String buildAdditionalInfo() {
        if (eaa.getHolderPseudonym() != null) {
            return i18nProvider.getMessage(MessageTag.PSEUDO, eaa.getHolderPseudonym());
        }
        return null;
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.EAA_PSEUDO_USED;
    }

    @Override
    protected MessageTag getErrorMessageTag() {
        return MessageTag.EAA_PSEUDO_USED_ANS;
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
