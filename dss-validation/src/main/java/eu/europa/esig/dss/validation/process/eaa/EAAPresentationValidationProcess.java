package eu.europa.esig.dss.validation.process.eaa;

import eu.europa.esig.dss.detailedreport.jaxb.XmlAOV;
import eu.europa.esig.dss.detailedreport.jaxb.XmlSignature;
import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationProcessEAAPresentation;
import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.EAAPresentationWrapper;
import eu.europa.esig.dss.diagnostic.SignatureWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDigestMatcher;
import eu.europa.esig.dss.enumerations.Context;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.ValidationPolicy;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.validation.process.Chain;
import eu.europa.esig.dss.validation.process.ChainItem;
import eu.europa.esig.dss.validation.process.ValidationProcessUtils;
import eu.europa.esig.dss.validation.process.bbb.aov.AlgorithmObsolescenceValidation;
import eu.europa.esig.dss.validation.process.bbb.aov.EAAPresentationAlgorithmObsolescenceValidation;
import eu.europa.esig.dss.validation.process.bbb.aov.checks.AlgorithmObsolescenceValidationCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.KeyBindingSignatureValidationResultCheck;
import eu.europa.esig.dss.validation.process.qualification.signature.checks.SignatureValidationResultCheck;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Performs validation of presentation of Electronic Attestation of Attributes
 *
 */
public class EAAPresentationValidationProcess extends Chain<XmlValidationProcessEAAPresentation> {

    /**
     * Diagnostic data
     */
    private final DiagnosticData diagnosticData;

    /**
     * EAA Presentation being validated
     */
    private final EAAPresentationWrapper eaaPresentation;

    /**
     * Map of validated signatures
     */
    private final Map<String, XmlSignature> xmlSignatures;

    /**
     * Validation policy used to validate evidence records
     */
    private final ValidationPolicy policy;

    /**
     * Validation time
     */
    private final Date currentTime;

    /**
     * Common constructor
     *
     * @param i18nProvider the access to translations
     * @param diagnosticData {@link DiagnosticData}
     * @param eaaPresentation {@link EAAPresentationWrapper} to be validated
     * @param xmlSignatures a map of {@link XmlSignature} validations
     * @param validationPolicy {@link ValidationPolicy} to be used
     * @param currentTime {@link Date} validation time
     */
    public EAAPresentationValidationProcess(I18nProvider i18nProvider, DiagnosticData diagnosticData,
                                            EAAPresentationWrapper eaaPresentation, Map<String, XmlSignature> xmlSignatures,
                                            ValidationPolicy validationPolicy, Date currentTime) {
        super(i18nProvider, new XmlValidationProcessEAAPresentation());

        this.diagnosticData = diagnosticData;
        this.eaaPresentation = eaaPresentation;
        this.xmlSignatures = xmlSignatures;
        this.policy = validationPolicy;
        this.currentTime = currentTime;
    }

    @Override
    protected MessageTag getTitle() {
        return MessageTag.VPEAA;
    }

    @Override
    protected void initChain() {

        ChainItem<XmlValidationProcessEAAPresentation> item = null;

        // Algorithm Obsolescence Validation to be included
        XmlAOV xmlAOV = null; // TODO : include signatures validation ?

        // 1. Verify electronic signatures

        for (SignatureWrapper signatureWrapper : eaaPresentation.getEAAPresentationSignatures()) {

            ChainItem<XmlValidationProcessEAAPresentation> signatureValidationConclusive = signatureValidationConclusive(signatureWrapper);
            if (item == null) {
                firstItem = item = signatureValidationConclusive;
            } else {
                item = item.setNextItem(signatureValidationConclusive);
            }

        }

        if (item == null) {
            throw new IllegalStateException("EAA Presentation shall contain at least one signature!");
        }

        // 2a. Verify disclosures

        List<XmlDigestMatcher> digestMatchers = eaaPresentation.getDigestMatchers();

        if (Utils.isCollectionNotEmpty(digestMatchers)) {

            for (XmlDigestMatcher digestMatcher : digestMatchers) {
                // TODO : add digest matchers checks
            }

        }

        // 2b. Validate cryptographic constraints of DigestMatchers
        if (Utils.isCollectionNotEmpty(digestMatchers)) {
            AlgorithmObsolescenceValidation<?> algorithmObsolescenceValidation =
                    new EAAPresentationAlgorithmObsolescenceValidation(i18nProvider, eaaPresentation, currentTime, policy);
            XmlAOV erAOV = algorithmObsolescenceValidation.execute();

            item = item.setNextItem(algorithmsObsolescenceValidation(erAOV, currentTime));

            xmlAOV = erAOV;
        }

        // 3. Verify Key Binding signature
        if (eaaPresentation.getKeyBindingSignature() != null) {
            item = item.setNextItem(keyBindingSignatureValidationConclusive(eaaPresentation.getKeyBindingSignature()));
        }

        // 4. Other checks (including TS 119 472-1)

        // TODO : implement

        result.setAOV(xmlAOV);

    }

    private ChainItem<XmlValidationProcessEAAPresentation> signatureValidationConclusive(SignatureWrapper signatureWrapper) {
        XmlSignature xmlSignature = xmlSignatures.get(signatureWrapper.getId());
        return new SignatureValidationResultCheck<>(i18nProvider, result,
                xmlSignature.getValidationProcessBasicSignature().getConclusion(), getFailLevelRule());
    }

    private ChainItem<XmlValidationProcessEAAPresentation> keyBindingSignatureValidationConclusive(SignatureWrapper signatureWrapper) {
        XmlSignature xmlSignature = xmlSignatures.get(signatureWrapper.getId());
        return new KeyBindingSignatureValidationResultCheck(i18nProvider, result,
                xmlSignature.getValidationProcessBasicSignature().getConclusion(), getFailLevelRule());
    }

    private ChainItem<XmlValidationProcessEAAPresentation> algorithmsObsolescenceValidation(XmlAOV aovResult, Date lowestPOETime) {
        MessageTag position = ValidationProcessUtils.getCryptoPosition(Context.ELECTRONIC_ATTESTATION_OF_ATTRIBUTES);
        return new AlgorithmObsolescenceValidationCheck<>(i18nProvider, result, aovResult, lowestPOETime, position, eaaPresentation.getId());
    }

}
