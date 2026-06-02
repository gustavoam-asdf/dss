package eu.europa.esig.dss.validation.process.eaa;

import eu.europa.esig.dss.detailedreport.jaxb.XmlBasicBuildingBlocks;
import eu.europa.esig.dss.detailedreport.jaxb.XmlCV;
import eu.europa.esig.dss.detailedreport.jaxb.XmlConclusion;
import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraint;
import eu.europa.esig.dss.detailedreport.jaxb.XmlFC;
import eu.europa.esig.dss.detailedreport.jaxb.XmlMessage;
import eu.europa.esig.dss.detailedreport.jaxb.XmlSAV;
import eu.europa.esig.dss.detailedreport.jaxb.XmlSignature;
import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationProcessEAA;
import eu.europa.esig.dss.diagnostic.EAAWrapper;
import eu.europa.esig.dss.diagnostic.SignatureWrapper;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;
import eu.europa.esig.dss.model.policy.ValidationPolicy;
import eu.europa.esig.dss.validation.process.ChainItem;
import eu.europa.esig.dss.validation.process.eaa.checks.KeyBindingSignatureValidationResultCheck;
import eu.europa.esig.dss.validation.process.qualification.signature.checks.SignatureValidationResultCheck;
import eu.europa.esig.dss.validation.process.vpfbs.AbstractBasicValidationProcess;
import eu.europa.esig.dss.validation.process.vpfbs.checks.SignatureAcceptanceValidationResultCheck;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Performs validation of presentation of Electronic Attestation of Attributes
 *
 */
public class EAAValidationProcess extends AbstractBasicValidationProcess<XmlValidationProcessEAA> {

    /**
     * EAA being validated
     */
    private final EAAWrapper eaa;

    /**
     * Map of validated signatures
     */
    private final Map<String, XmlSignature> xmlSignatures;

    /**
     * Validation policy used to validate evidence records
     */
    private final ValidationPolicy policy;

    /**
     * Common constructor
     *
     * @param i18nProvider the access to translations
     * @param eaa {@link EAAWrapper} to be validated
     * @param xmlSignatures a map of {@link XmlSignature} validations
     * @param validationPolicy {@link ValidationPolicy} to be used
     */
    public EAAValidationProcess(final I18nProvider i18nProvider, final EAAWrapper eaa,
                                final Map<String, XmlSignature> xmlSignatures, final Map<String, XmlBasicBuildingBlocks> bbbs,
                                final ValidationPolicy validationPolicy) {
        super(i18nProvider, new XmlValidationProcessEAA(), null, eaa, bbbs);
        this.eaa = eaa;
        this.xmlSignatures = xmlSignatures;
        this.policy = validationPolicy;
    }

    @Override
    protected MessageTag getTitle() {
        return MessageTag.VPEAA;
    }

    @Override
    protected void initChain() {

        final XmlBasicBuildingBlocks eaaBBBs = bbbs.get(eaa.getId());
        if (eaaBBBs == null) {
            throw new IllegalStateException(
                    String.format("Missing Basic Building Blocks result for token with Id '%s'", eaa.getId()));
        }

        ChainItem<XmlValidationProcessEAA> item = firstItem;

        // 1. Format checking
        XmlFC xmlFC = eaaBBBs.getFC();
        if (xmlFC != null) {
            item = firstItem = formatChecking(xmlFC);
        }

        // 2. Verify electronic signatures
        for (SignatureWrapper signatureWrapper : eaa.getEAASignatures()) {
            item = item.setNextItem(signatureValidationConclusive(signatureWrapper));
        }

        // 3. Verify Key Binding signature
        if (eaa.getKeyBindingSignature() != null) {
            item = item.setNextItem(keyBindingSignatureValidationConclusive(eaa.getKeyBindingSignature()));
        }

        // 4. Digest (selective disclosures) validation
        XmlCV xmlCV = eaaBBBs.getCV();
        if (xmlCV != null) {
            item = item.setNextItem(cryptographicVerification(xmlCV));
        }

        // 5. EAA Acceptance Validation
        XmlSAV xmlSAV = eaaBBBs.getSAV();
        if (xmlSAV != null) {
            item = item.setNextItem(signatureAcceptanceValidation(xmlSAV));
        }

    }

    private ChainItem<XmlValidationProcessEAA> signatureValidationConclusive(SignatureWrapper signatureWrapper) {
        LevelRule constraint = policy.getEAASignatureValidConstraint();
        return new SignatureValidationResultCheck<>(i18nProvider, result, getSignatureBasicProcessConclusion(signatureWrapper), constraint);
    }

    private XmlConclusion getSignatureBasicProcessConclusion(SignatureWrapper signatureWrapper) {
        XmlSignature xmlSignature = xmlSignatures.get(signatureWrapper.getId());
        if (xmlSignature == null) {
            throw new IllegalStateException(String.format("Invalid state! No basic signature validation process " +
                    "found for the signature with Id '%s'!", signatureWrapper.getId()));
        }
        return xmlSignature.getValidationProcessBasicSignature().getConclusion();
    }

    private ChainItem<XmlValidationProcessEAA> keyBindingSignatureValidationConclusive(SignatureWrapper signatureWrapper) {
        LevelRule constraint = policy.getEAAKeyBindingSignatureValidConstraint();
        XmlSignature xmlSignature = xmlSignatures.get(signatureWrapper.getId());
        return new KeyBindingSignatureValidationResultCheck(i18nProvider, result,
                xmlSignature.getValidationProcessBasicSignature().getConclusion(), constraint);
    }

    @Override
    protected ChainItem<XmlValidationProcessEAA> signatureAcceptanceValidation(final XmlSAV xmlSAV) {
        return new SignatureAcceptanceValidationResultCheck<XmlValidationProcessEAA>(i18nProvider, result, xmlSAV, token, getFailLevelRule()) {

            @Override
            protected MessageTag getMessageTag() {
                return MessageTag.BSV_IEAAAVRC;
            }

            @Override
            protected MessageTag getErrorMessageTag() {
                return MessageTag.BSV_IEAAAVRC_ANS;
            }

        };
    }

    @Override
    protected void collectAdditionalMessages(XmlConclusion conclusion) {
        final XmlBasicBuildingBlocks tokenBBBs = bbbs.get(token.getId());
        if (tokenBBBs != null) {
            clear(conclusion.getErrors());
            conclusion.getErrors().addAll(tokenBBBs.getConclusion().getErrors());
            clear(conclusion.getWarnings());
            conclusion.getWarnings().addAll(tokenBBBs.getConclusion().getWarnings());
            clear(conclusion.getInfos());
            conclusion.getInfos().addAll(tokenBBBs.getConclusion().getInfos());

            for (XmlConstraint constraint : result.getConstraint()) {
                collectMessages(conclusion, constraint);
            }
        }
    }

    private void clear(List<XmlMessage> messageList) {
        // clears if not signature validation
        List<XmlMessage> result = messageList.stream().filter(m ->
                MessageTag.ADEST_IBSVPSC_ANS.getId().equals(m.getKey()) ||
                        MessageTag.EAA_KBRC_ANS.getId().equals(m.getKey())
        ).collect(Collectors.toList());
        messageList.clear();
        messageList.addAll(result);
    }

}
