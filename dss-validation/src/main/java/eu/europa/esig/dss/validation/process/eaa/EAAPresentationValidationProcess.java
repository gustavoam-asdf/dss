package eu.europa.esig.dss.validation.process.eaa;

import eu.europa.esig.dss.detailedreport.jaxb.XmlAOV;
import eu.europa.esig.dss.detailedreport.jaxb.XmlConclusion;
import eu.europa.esig.dss.detailedreport.jaxb.XmlSignature;
import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationProcessEAAPresentation;
import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.EAAPresentationWrapper;
import eu.europa.esig.dss.diagnostic.SignatureWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDigestMatcher;
import eu.europa.esig.dss.enumerations.Context;
import eu.europa.esig.dss.enumerations.DigestMatcherType;
import eu.europa.esig.dss.enumerations.EAAPresentationType;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.model.policy.LevelRule;
import eu.europa.esig.dss.model.policy.MultiValuesRule;
import eu.europa.esig.dss.model.policy.ValidationPolicy;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.validation.process.Chain;
import eu.europa.esig.dss.validation.process.ChainItem;
import eu.europa.esig.dss.validation.process.ValidationProcessUtils;
import eu.europa.esig.dss.validation.process.bbb.aov.AlgorithmObsolescenceValidation;
import eu.europa.esig.dss.validation.process.bbb.aov.EAAPresentationAlgorithmObsolescenceValidation;
import eu.europa.esig.dss.validation.process.bbb.aov.checks.AlgorithmObsolescenceValidationCheck;
import eu.europa.esig.dss.validation.process.bbb.cv.checks.ReferenceDataExistenceCheck;
import eu.europa.esig.dss.validation.process.bbb.cv.checks.ReferenceDataIntactCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.AcceptableEAATypeCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.DisclosureListExhaustiveCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.DisclosurePresentCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.EAASignatureUnicityCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.EAATypePresentCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.KeyBindingSignaturePresentCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.KeyBindingSignatureValidationResultCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.SDJWTEAAVctIntegrityPresentCheck;
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

        // Algorithm Obsolescence Validation to be included
        XmlAOV xmlAOV = null;

        // 1. Verify electronic signatures

        ChainItem<XmlValidationProcessEAAPresentation> item = firstItem = signatureUnicity();

        for (SignatureWrapper signatureWrapper : eaaPresentation.getEAAPresentationSignatures()) {
            item = item.setNextItem(signatureValidationConclusive(signatureWrapper));
        }

        if (item == null) {
            throw new IllegalStateException("EAA Presentation shall contain at least one signature!");
        }

        // 2a. Verify disclosures

        List<XmlDigestMatcher> digestMatchers = eaaPresentation.getDigestMatchers();

        if (Utils.isCollectionNotEmpty(digestMatchers)) {

            item = item.setNextItem(disclosurePresent());

            for (XmlDigestMatcher digestMatcher : digestMatchers) {

                if (DigestMatcherType.EAA_ORPHAN_SELECTIVELY_DISCLOSABLE_CLAIM != digestMatcher.getType()) {

                    ChainItem<XmlValidationProcessEAAPresentation> referenceDataFound = referenceDataFound(digestMatcher);

                    item = item.setNextItem(referenceDataFound);

                    if (digestMatcher.isDataFound()) {
                        item = item.setNextItem(referenceDataIntact(digestMatcher));
                    }

                }

            }

            item = item.setNextItem(disclosureListExhaustive());

            // 2b. Validate cryptographic constraints of DigestMatchers
            AlgorithmObsolescenceValidation<?> algorithmObsolescenceValidation =
                    new EAAPresentationAlgorithmObsolescenceValidation(i18nProvider, eaaPresentation, currentTime, policy);
            XmlAOV erAOV = algorithmObsolescenceValidation.execute();

            item = item.setNextItem(algorithmsObsolescenceValidation(erAOV, currentTime));

            xmlAOV = erAOV;

        }

        // 3. Verify Key Binding signature

        item = item.setNextItem(keyBindingSignaturePresent());

        if (eaaPresentation.getKeyBindingSignature() != null) {
            item = item.setNextItem(keyBindingSignatureValidationConclusive(eaaPresentation.getKeyBindingSignature()));
        }

        // 4. Other checks (including TS 119 472-1)

        item = item.setNextItem(eaaTypePresent());
        item = item.setNextItem(acceptableEaaType());

        if (EAAPresentationType.SD_JWT_VC == eaaPresentation.getEAAType()) {
            item = item.setNextItem(sdjwtEaaVctIntegrityPresent());
        }

        // TODO : implement

        result.setAOV(xmlAOV);

    }

    private ChainItem<XmlValidationProcessEAAPresentation> signatureUnicity() {
        LevelRule constraint = policy.getEAAPresentationEAASignatureUnicityConstraint();
        return new EAASignatureUnicityCheck(i18nProvider, result, eaaPresentation, constraint);
    }

    private ChainItem<XmlValidationProcessEAAPresentation> signatureValidationConclusive(SignatureWrapper signatureWrapper) {
        LevelRule constraint = policy.getEAAPresentationEAASignatureValidConstraint();
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

    private ChainItem<XmlValidationProcessEAAPresentation> disclosurePresent() {
        LevelRule constraint = policy.getEAAPresentationDisclosurePresentConstraint();
        return new DisclosurePresentCheck(i18nProvider, result, eaaPresentation, constraint);
    }

    private ChainItem<XmlValidationProcessEAAPresentation> referenceDataFound(XmlDigestMatcher digestMatcher) {
        LevelRule constraint = policy.getEAAPresentationDisclosureFoundConstraint();
        return new ReferenceDataExistenceCheck<>(i18nProvider, result, digestMatcher, constraint);
    }

    private ChainItem<XmlValidationProcessEAAPresentation> referenceDataIntact(XmlDigestMatcher digestMatcher) {
        LevelRule constraint = policy.getEAAPresentationDisclosureIntactConstraint();
        return new ReferenceDataIntactCheck<>(i18nProvider, result, digestMatcher, constraint);
    }

    private ChainItem<XmlValidationProcessEAAPresentation> disclosureListExhaustive() {
        LevelRule constraint = policy.getEAAPresentationDisclosureListExhaustiveConstraint();
        return new DisclosureListExhaustiveCheck(i18nProvider, result, eaaPresentation, constraint);
    }

    private ChainItem<XmlValidationProcessEAAPresentation> keyBindingSignaturePresent() {
        LevelRule constraint = policy.getEAAPresentationKeyBindingSignaturePresentConstraint();
        return new KeyBindingSignaturePresentCheck(i18nProvider, result, eaaPresentation, constraint);
    }

    private ChainItem<XmlValidationProcessEAAPresentation> keyBindingSignatureValidationConclusive(SignatureWrapper signatureWrapper) {
        LevelRule constraint = policy.getEAAPresentationKeyBindingSignatureValidConstraint();
        XmlSignature xmlSignature = xmlSignatures.get(signatureWrapper.getId());
        return new KeyBindingSignatureValidationResultCheck(i18nProvider, result,
                xmlSignature.getValidationProcessBasicSignature().getConclusion(), constraint);
    }

    private ChainItem<XmlValidationProcessEAAPresentation> eaaTypePresent() {
        LevelRule constraint = policy.getEAAPresentationEAATypePresentConstraint();
        return new EAATypePresentCheck(i18nProvider, result, eaaPresentation, constraint);
    }

    private ChainItem<XmlValidationProcessEAAPresentation> acceptableEaaType() {
        MultiValuesRule constraint = policy.getEAAPresentationEAATypeAcceptableConstraint();
        return new AcceptableEAATypeCheck(i18nProvider, result, eaaPresentation, constraint);
    }

    private ChainItem<XmlValidationProcessEAAPresentation> sdjwtEaaVctIntegrityPresent() {
        LevelRule constraint = policy.getEAAPresentationEAATypePresentConstraint();
        return new SDJWTEAAVctIntegrityPresentCheck(i18nProvider, result, eaaPresentation, constraint);
    }

    private ChainItem<XmlValidationProcessEAAPresentation> algorithmsObsolescenceValidation(XmlAOV aovResult, Date lowestPOETime) {
        MessageTag position = ValidationProcessUtils.getCryptoPosition(Context.EAA_PRESENTATION);
        return new AlgorithmObsolescenceValidationCheck<>(i18nProvider, result, aovResult, lowestPOETime, position, eaaPresentation.getId());
    }

}
