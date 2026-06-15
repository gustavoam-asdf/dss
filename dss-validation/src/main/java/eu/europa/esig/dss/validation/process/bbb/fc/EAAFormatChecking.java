package eu.europa.esig.dss.validation.process.bbb.fc;

import eu.europa.esig.dss.detailedreport.jaxb.XmlFC;
import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.EAAWrapper;
import eu.europa.esig.dss.enumerations.Context;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.model.policy.LevelRule;
import eu.europa.esig.dss.model.policy.ValidationPolicy;
import eu.europa.esig.dss.validation.process.ChainItem;
import eu.europa.esig.dss.validation.process.eaa.checks.DisclosureListExhaustiveCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.DisclosurePresentCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.EAASignatureUnicityCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.KeyBindingSignaturePresentCheck;

/**
 * Verifies format of an Electronic Attestation of Attributes (EAA)
 *
 */
public class EAAFormatChecking extends AbstractFormatChecking<EAAWrapper> {

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param diagnosticData {@link DiagnosticData}
     * @param eaa {@link EAAWrapper}
     * @param context {@link Context}
     * @param policy {@link ValidationPolicy}
     */
    public EAAFormatChecking(I18nProvider i18nProvider, DiagnosticData diagnosticData,
                             EAAWrapper eaa, Context context, ValidationPolicy policy) {
        super(i18nProvider, diagnosticData, eaa, context, policy);
    }

    @Override
    protected void initChain() {

        ChainItem<XmlFC> item = firstItem = signatureUnicity();

        item = item.setNextItem(disclosurePresent());

        item = item.setNextItem(disclosureListExhaustive());

        item = item.setNextItem(keyBindingSignaturePresent());

    }

    private ChainItem<XmlFC> signatureUnicity() {
        LevelRule constraint = policy.getEAASignatureUnicityConstraint();
        return new EAASignatureUnicityCheck(i18nProvider, result, token, constraint);
    }

    private ChainItem<XmlFC> disclosurePresent() {
        LevelRule constraint = policy.getEAADisclosurePresentConstraint();
        return new DisclosurePresentCheck(i18nProvider, result, token, constraint);
    }

    private ChainItem<XmlFC> disclosureListExhaustive() {
        LevelRule constraint = policy.getEAADisclosureListExhaustiveConstraint();
        return new DisclosureListExhaustiveCheck(i18nProvider, result, token, constraint);
    }

    private ChainItem<XmlFC> keyBindingSignaturePresent() {
        LevelRule constraint = policy.getEAAKeyBindingSignaturePresentConstraint();
        return new KeyBindingSignaturePresentCheck(i18nProvider, result, token, constraint);
    }

}
