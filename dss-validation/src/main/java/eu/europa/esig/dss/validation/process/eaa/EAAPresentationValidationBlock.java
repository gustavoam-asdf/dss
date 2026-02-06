package eu.europa.esig.dss.validation.process.eaa;

import eu.europa.esig.dss.detailedreport.jaxb.XmlBasicBuildingBlocks;
import eu.europa.esig.dss.detailedreport.jaxb.XmlConclusion;
import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraintsConclusionWithProofOfExistence;
import eu.europa.esig.dss.detailedreport.jaxb.XmlEAAPresentation;
import eu.europa.esig.dss.detailedreport.jaxb.XmlSignature;
import eu.europa.esig.dss.detailedreport.jaxb.XmlTLAnalysis;
import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationProcessBasicSignature;
import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationProcessEAAPresentation;
import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationSignatureQualification;
import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.EAAPresentationWrapper;
import eu.europa.esig.dss.diagnostic.SignatureWrapper;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.model.policy.ValidationPolicy;
import eu.europa.esig.dss.validation.process.qualification.eaa.EAAQualificationBlock;
import eu.europa.esig.dss.validation.process.qualification.signature.SignatureQualificationBlock;
import eu.europa.esig.dss.validation.process.vpfbs.BasicSignatureValidationProcess;
import eu.europa.esig.dss.validation.reports.DSSReportException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class performs validation of the EAA Presentation
 *
 */
public class EAAPresentationValidationBlock {

    /** The i18n provider */
    private final I18nProvider i18nProvider;

    /** Diagnostic data */
    private final DiagnosticData diagnosticData;

    /** The validation policy */
    protected final ValidationPolicy policy;

    /** The validation time */
    protected final Date currentTime;

    /** Map of BasicBuildingBlocks */
    private final Map<String, XmlBasicBuildingBlocks> bbbs;

    /** List of Trusted List validations */
    private final List<XmlTLAnalysis> tlAnalysis;

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param diagnosticData {@link DiagnosticData}
     * @param policy {@link ValidationPolicy}
     * @param currentTime {@link Date} validation time
     * @param bbbs map of {@link XmlBasicBuildingBlocks} to fill the validation result
     * @param tlAnalysis a list of {@link XmlTLAnalysis}
     */
    public EAAPresentationValidationBlock(final I18nProvider i18nProvider, final DiagnosticData diagnosticData,
                                          final ValidationPolicy policy, final Date currentTime, final Map<String, XmlBasicBuildingBlocks> bbbs,
                                          final List<XmlTLAnalysis> tlAnalysis) {
        this.i18nProvider = i18nProvider;
        this.diagnosticData = diagnosticData;
        this.policy = policy;
        this.currentTime = currentTime;
        this.bbbs = bbbs;
        this.tlAnalysis = tlAnalysis;
    }

    /**
     * Performs validation of EAA presentations
     */
    public List<XmlEAAPresentation> execute() {
        final List<XmlEAAPresentation> result = new ArrayList<>();

        for (EAAPresentationWrapper eaaPresentation : diagnosticData.getEAAPresentations()) {
            final XmlEAAPresentation eaaPresentationAnalysis = new XmlEAAPresentation();
            eaaPresentationAnalysis.setId(eaaPresentation.getId());

            final Map<String, XmlSignature> signatureValidationMap = new HashMap<>();

            for (SignatureWrapper signature : eaaPresentation.getEAAPresentationSignatures()) {
                XmlSignature signatureValidation = getEAAPresentationSignatureValidation(signature);
                eaaPresentationAnalysis.getSignature().add(signatureValidation);
                signatureValidationMap.put(signature.getId(), signatureValidation);
            }

            if (eaaPresentation.getKeyBindingSignature() != null) {
                XmlSignature signatureValidation = getEAAPresentationSignatureValidation(eaaPresentation.getKeyBindingSignature());
                eaaPresentationAnalysis.setKeyBindingSignature(signatureValidation);
                signatureValidationMap.put(eaaPresentation.getKeyBindingSignature().getId(), signatureValidation);
            }

            EAAPresentationValidationProcess eaapvp = new EAAPresentationValidationProcess(
                    i18nProvider, diagnosticData, eaaPresentation, signatureValidationMap, policy, currentTime);
            XmlValidationProcessEAAPresentation validationProcessEAAPresentation = eaapvp.execute();
            eaaPresentationAnalysis.setValidationProcessEAAPresentation(validationProcessEAAPresentation);

            XmlConclusion conclusion = validationProcessEAAPresentation.getConclusion();
            eaaPresentationAnalysis.setConclusion(conclusion);

            if (policy.isEIDASConstraintPresent()) {

                for (SignatureWrapper signature : eaaPresentation.getEAAPresentationSignatures()) {

                    XmlSignature xmlSignature = signatureValidationMap.get(signature.getId());
                    if (xmlSignature == null) {
                        throw new IllegalStateException(String.format("Signature validation is not found for Id '%s'", signature.getId()));
                    }

                    SignatureQualificationBlock signatureQualificationBlock = new SignatureQualificationBlock(
                            i18nProvider, xmlSignature.getValidationProcessBasicSignature(), signature.getSigningCertificate(), tlAnalysis);
                    XmlValidationSignatureQualification validationSignatureQualification = signatureQualificationBlock.execute();
                    xmlSignature.setValidationSignatureQualification(validationSignatureQualification);

                }

                EAAQualificationBlock qualificationBlock = new EAAQualificationBlock(
                        i18nProvider, eaaPresentation, conclusion, signatureValidationMap, tlAnalysis, currentTime);
                eaaPresentationAnalysis.setValidationEAAQualification(qualificationBlock.execute());

            }

            result.add(eaaPresentationAnalysis);
        }

        return result;
    }

    protected XmlSignature getEAAPresentationSignatureValidation(SignatureWrapper signatureWrapper) {

        final XmlSignature xmlSignature = new XmlSignature();
        xmlSignature.setId(signatureWrapper.getId());

        XmlConstraintsConclusionWithProofOfExistence validation = executeBasicValidation(xmlSignature, signatureWrapper, bbbs);

        XmlConclusion conclusion = validation.getConclusion();
        conclusion.setIndication(getSignatureFinalIndication(conclusion.getIndication()));
        xmlSignature.setConclusion(conclusion);

        return xmlSignature;
    }

    private XmlValidationProcessBasicSignature executeBasicValidation(XmlSignature signatureAnalysis, SignatureWrapper signature,
                                                                      Map<String, XmlBasicBuildingBlocks> bbbs) {
        BasicSignatureValidationProcess vpfbs = new BasicSignatureValidationProcess(
                i18nProvider, diagnosticData, signature, Collections.emptyList(), bbbs);
        XmlValidationProcessBasicSignature bs = vpfbs.execute();
        signatureAnalysis.setValidationProcessBasicSignature(bs);
        return bs;
    }

    private Indication getSignatureFinalIndication(Indication highestIndication) {
        switch (highestIndication) {
            case PASSED:
                return Indication.TOTAL_PASSED;
            case INDETERMINATE:
                return Indication.INDETERMINATE;
            case FAILED:
                return Indication.TOTAL_FAILED;
            default:
                throw new DSSReportException(String.format("The Indication '%s' is not supported!", highestIndication));
        }
    }

}
