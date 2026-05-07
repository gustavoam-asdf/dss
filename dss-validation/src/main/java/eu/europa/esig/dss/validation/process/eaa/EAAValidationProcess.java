package eu.europa.esig.dss.validation.process.eaa;

import eu.europa.esig.dss.detailedreport.jaxb.XmlAOV;
import eu.europa.esig.dss.detailedreport.jaxb.XmlConclusion;
import eu.europa.esig.dss.detailedreport.jaxb.XmlSignature;
import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationProcessEAA;
import eu.europa.esig.dss.diagnostic.EAAWrapper;
import eu.europa.esig.dss.diagnostic.SignatureWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDigestMatcher;
import eu.europa.esig.dss.enumerations.Context;
import eu.europa.esig.dss.enumerations.DigestMatcherType;
import eu.europa.esig.dss.enumerations.EAAType;
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
import eu.europa.esig.dss.validation.process.bbb.aov.EAAAlgorithmObsolescenceValidation;
import eu.europa.esig.dss.validation.process.bbb.aov.checks.AlgorithmObsolescenceValidationCheck;
import eu.europa.esig.dss.validation.process.bbb.cv.checks.ReferenceDataExistenceCheck;
import eu.europa.esig.dss.validation.process.bbb.cv.checks.ReferenceDataIntactCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.DisclosureListExhaustiveCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.DisclosurePresentCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.EAAAdministrativeExpirationDatePresentCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.EAAAdministrativeIssuanceDatePresentCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.EAAExpirationPresentCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.EAAIdentifierPresentCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.EAANotBeforePresentCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.EAASignatureUnicityCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.EAATypeCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.EAATypeIntegrityPresentCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.ETSI194721ConformanceCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.KeyBindingSignaturePresentCheck;
import eu.europa.esig.dss.validation.process.eaa.checks.KeyBindingSignatureValidationResultCheck;
import eu.europa.esig.dss.validation.process.qualification.signature.checks.SignatureValidationResultCheck;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Performs validation of presentation of Electronic Attestation of Attributes
 *
 */
public class EAAValidationProcess extends Chain<XmlValidationProcessEAA> {

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
     * Validation time
     */
    private final Date currentTime;

    /**
     * Common constructor
     *
     * @param i18nProvider the access to translations
     * @param eaa {@link EAAWrapper} to be validated
     * @param xmlSignatures a map of {@link XmlSignature} validations
     * @param validationPolicy {@link ValidationPolicy} to be used
     * @param currentTime {@link Date} validation time
     */
    public EAAValidationProcess(I18nProvider i18nProvider, EAAWrapper eaa,
                                Map<String, XmlSignature> xmlSignatures, ValidationPolicy validationPolicy, Date currentTime) {
        super(i18nProvider, new XmlValidationProcessEAA());

        this.eaa = eaa;
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

        ChainItem<XmlValidationProcessEAA> item = firstItem = signatureUnicity();

        for (SignatureWrapper signatureWrapper : eaa.getEAASignatures()) {
            item = item.setNextItem(signatureValidationConclusive(signatureWrapper));
        }

        if (item == null) {
            throw new IllegalStateException("EAA shall contain at least one signature!");
        }

        // 2a. Verify disclosures

        item = item.setNextItem(disclosurePresent());

        List<XmlDigestMatcher> digestMatchers = eaa.getDigestMatchers();

        if (Utils.isCollectionNotEmpty(digestMatchers)) {

            for (XmlDigestMatcher digestMatcher : digestMatchers) {

                if (DigestMatcherType.EAA_ORPHAN_SELECTIVELY_DISCLOSABLE_CLAIM != digestMatcher.getType()) {

                    ChainItem<XmlValidationProcessEAA> referenceDataFound = referenceDataFound(digestMatcher);

                    item = item.setNextItem(referenceDataFound);

                    if (digestMatcher.isDataFound()) {
                        item = item.setNextItem(referenceDataIntact(digestMatcher));
                    }

                }

            }

            item = item.setNextItem(disclosureListExhaustive());

            // 2b. Validate cryptographic constraints of DigestMatchers
            AlgorithmObsolescenceValidation<?> algorithmObsolescenceValidation =
                    new EAAAlgorithmObsolescenceValidation(i18nProvider, eaa, currentTime, policy);
            XmlAOV erAOV = algorithmObsolescenceValidation.execute();

            item = item.setNextItem(algorithmsObsolescenceValidation(erAOV, currentTime));

            xmlAOV = erAOV;

        }

        // 3. Verify Key Binding signature

        item = item.setNextItem(keyBindingSignaturePresent());

        if (eaa.getKeyBindingSignature() != null) {
            item = item.setNextItem(keyBindingSignatureValidationConclusive(eaa.getKeyBindingSignature()));
        }

        // 4. Other checks (including TS 119 472-1)

        item = item.setNextItem(eaaType());
        if (EAAType.SD_JWT_VC == eaa.getEAAType()) {
            item = item.setNextItem(typeIntegrityPresent());
        }

        item = item.setNextItem(eaaIdentifierPresent());
        item = item.setNextItem(notBeforePresent());
        item = item.setNextItem(expirationPresent());
        item = item.setNextItem(administrativeIssuanceDatePresent());
        item = item.setNextItem(administrativeExpirationDatePresent());
        item = item.setNextItem(etsi194721Conformance());

        result.setAOV(xmlAOV);

    }

    private ChainItem<XmlValidationProcessEAA> signatureUnicity() {
        LevelRule constraint = policy.getEAASignatureUnicityConstraint();
        return new EAASignatureUnicityCheck(i18nProvider, result, eaa, constraint);
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

    private ChainItem<XmlValidationProcessEAA> disclosurePresent() {
        LevelRule constraint = policy.getEAADisclosurePresentConstraint();
        return new DisclosurePresentCheck(i18nProvider, result, eaa, constraint);
    }

    private ChainItem<XmlValidationProcessEAA> referenceDataFound(XmlDigestMatcher digestMatcher) {
        LevelRule constraint = policy.getEAADisclosureFoundConstraint();
        return new ReferenceDataExistenceCheck<>(i18nProvider, result, digestMatcher, constraint);
    }

    private ChainItem<XmlValidationProcessEAA> referenceDataIntact(XmlDigestMatcher digestMatcher) {
        LevelRule constraint = policy.getEAADisclosureIntactConstraint();
        return new ReferenceDataIntactCheck<>(i18nProvider, result, digestMatcher, constraint);
    }

    private ChainItem<XmlValidationProcessEAA> disclosureListExhaustive() {
        LevelRule constraint = policy.getEAADisclosureListExhaustiveConstraint();
        return new DisclosureListExhaustiveCheck(i18nProvider, result, eaa, constraint);
    }

    private ChainItem<XmlValidationProcessEAA> keyBindingSignaturePresent() {
        LevelRule constraint = policy.getEAAKeyBindingSignaturePresentConstraint();
        return new KeyBindingSignaturePresentCheck(i18nProvider, result, eaa, constraint);
    }

    private ChainItem<XmlValidationProcessEAA> keyBindingSignatureValidationConclusive(SignatureWrapper signatureWrapper) {
        LevelRule constraint = policy.getEAAKeyBindingSignatureValidConstraint();
        XmlSignature xmlSignature = xmlSignatures.get(signatureWrapper.getId());
        return new KeyBindingSignatureValidationResultCheck(i18nProvider, result,
                xmlSignature.getValidationProcessBasicSignature().getConclusion(), constraint);
    }

    private ChainItem<XmlValidationProcessEAA> eaaType() {
        MultiValuesRule constraint = policy.getEAATypeConstraint();
        return new EAATypeCheck(i18nProvider, result, eaa, constraint);
    }

    private ChainItem<XmlValidationProcessEAA> typeIntegrityPresent() {
        LevelRule constraint = policy.getEAATypeIntegrityPresentConstraint();
        return new EAATypeIntegrityPresentCheck(i18nProvider, result, eaa, constraint);
    }

    private ChainItem<XmlValidationProcessEAA> notBeforePresent() {
        LevelRule constraint = policy.getEAANotBeforePresentConstraint();
        return new EAANotBeforePresentCheck(i18nProvider, result, eaa, constraint);
    }

    private ChainItem<XmlValidationProcessEAA> expirationPresent() {
        LevelRule constraint = policy.getEAAExpirationPresentConstraint();
        return new EAAExpirationPresentCheck(i18nProvider, result, eaa, constraint);
    }

    private ChainItem<XmlValidationProcessEAA> administrativeIssuanceDatePresent() {
        LevelRule constraint = policy.getEAAAdministrativeIssuanceDatePresentConstraint();
        return new EAAAdministrativeIssuanceDatePresentCheck(i18nProvider, result, eaa, constraint);
    }

    private ChainItem<XmlValidationProcessEAA> administrativeExpirationDatePresent() {
        LevelRule constraint = policy.getEAAAdministrativeExpirationDatePresentConstraint();
        return new EAAAdministrativeExpirationDatePresentCheck(i18nProvider, result, eaa, constraint);
    }

    private ChainItem<XmlValidationProcessEAA> eaaIdentifierPresent() {
        LevelRule constraint = policy.getEAAIdentifierPresentConstraint();
        return new EAAIdentifierPresentCheck(i18nProvider, result, eaa, constraint);
    }

    private ChainItem<XmlValidationProcessEAA> etsi194721Conformance() {
        LevelRule constraint = policy.getEAAETSI194721ConformanceConstraint();
        return new ETSI194721ConformanceCheck(i18nProvider, result, eaa, constraint);
    }

    private ChainItem<XmlValidationProcessEAA> algorithmsObsolescenceValidation(XmlAOV aovResult, Date lowestPOETime) {
        MessageTag position = ValidationProcessUtils.getCryptoPosition(Context.EAA_PRESENTATION);
        return new AlgorithmObsolescenceValidationCheck<>(i18nProvider, result, aovResult, lowestPOETime, position, eaa.getId());
    }

}
