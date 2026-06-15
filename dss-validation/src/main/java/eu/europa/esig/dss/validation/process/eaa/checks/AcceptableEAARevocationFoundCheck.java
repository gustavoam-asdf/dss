package eu.europa.esig.dss.validation.process.eaa.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlSAV;
import eu.europa.esig.dss.diagnostic.EAARevocationWrapper;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;
import eu.europa.esig.dss.model.policy.MultiValuesRule;
import eu.europa.esig.dss.validation.process.ChainItem;

/**
 * Checks whether an acceptable EAA revocation was found
 *
 */
public class AcceptableEAARevocationFoundCheck extends ChainItem<XmlSAV> {

    /** EAA revocation token to check */
    private final EAARevocationWrapper eaaStatusToken;

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result {@link XmlSAV}
     * @param eaaStatusToken {@link EAARevocationWrapper}
     * @param constraint {@link MultiValuesRule}
     */
    public AcceptableEAARevocationFoundCheck(final I18nProvider i18nProvider, final XmlSAV result,
                                             final EAARevocationWrapper eaaStatusToken, final LevelRule constraint) {
        super(i18nProvider, result, constraint, eaaStatusToken != null ? eaaStatusToken.getId() : null);
        this.eaaStatusToken = eaaStatusToken;
    }

    @Override
    protected boolean process() {
        return eaaStatusToken != null;
    }

    @Override
    protected MessageTag getMessageTag() {
        return MessageTag.EAA_REV_ACC_FND;
    }

    @Override
    protected MessageTag getErrorMessageTag() {
        return MessageTag.EAA_REV_ACC_FND_ANS;
    }

    @Override
    protected String buildAdditionalInfo() {
        if (eaaStatusToken != null) {
            return i18nProvider.getMessage(MessageTag.TOKEN_ID, eaaStatusToken.getId());
        }
        return null;
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
