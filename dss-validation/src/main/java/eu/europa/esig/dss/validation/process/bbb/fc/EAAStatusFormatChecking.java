package eu.europa.esig.dss.validation.process.bbb.fc;

import eu.europa.esig.dss.detailedreport.jaxb.XmlFC;
import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.EAAStatusTokenWrapper;
import eu.europa.esig.dss.enumerations.Context;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.model.policy.MultiValuesRule;
import eu.europa.esig.dss.model.policy.ValidationPolicy;
import eu.europa.esig.dss.validation.process.ChainItem;
import eu.europa.esig.dss.validation.process.eaa.status.EAAStatusTokenTypeCheck;

/**
 * Verifies format of an EAA status token
 *
 */
public class EAAStatusFormatChecking extends AbstractFormatChecking<EAAStatusTokenWrapper> {

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param diagnosticData {@link DiagnosticData}
     * @param eaaStatusToken {@link EAAStatusTokenWrapper}
     * @param context {@link Context}
     * @param policy {@link ValidationPolicy}
     */
    public EAAStatusFormatChecking(I18nProvider i18nProvider, DiagnosticData diagnosticData,
                                   EAAStatusTokenWrapper eaaStatusToken, Context context, ValidationPolicy policy) {
        super(i18nProvider, diagnosticData, eaaStatusToken, context, policy);
    }

    @Override
    protected void initChain() {

        ChainItem<XmlFC> item = firstItem = type();

        // TODO : JWT/CWT formats checks ?

    }

    private ChainItem<XmlFC> type() {
        MultiValuesRule constraint = policy.getEAAStatusTokenTypeConstraint();
        return new EAAStatusTokenTypeCheck(i18nProvider, result, token, constraint);
    }

}
