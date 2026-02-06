package eu.europa.esig.dss.validation.executor.eaa;

import eu.europa.esig.dss.detailedreport.jaxb.XmlBasicBuildingBlocks;
import eu.europa.esig.dss.detailedreport.jaxb.XmlDetailedReport;
import eu.europa.esig.dss.detailedreport.jaxb.XmlEAAPresentation;
import eu.europa.esig.dss.detailedreport.jaxb.XmlTLAnalysis;
import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.enumerations.ValidationLevel;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.model.policy.ValidationPolicy;
import eu.europa.esig.dss.validation.executor.signature.DetailedReportBuilder;
import eu.europa.esig.dss.validation.process.eaa.EAAPresentationValidationBlock;
import eu.europa.esig.dss.validation.process.vpfswatsp.POEExtraction;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * This class is used to perform validation of an EAA presentation validation
 *
 */
public class DetailedReportForEAAPresentationBuilder extends DetailedReportBuilder {

    /**
     * Default constructor
     *
     * @param i18nProvider     {@link I18nProvider}
     * @param currentTime      {@link Date} validation time
     * @param policy           {@link ValidationPolicy}
     * @param diagnosticData   {@link DiagnosticData}
     * @param includeSemantics defines if the semantics shall be included
     */
    public DetailedReportForEAAPresentationBuilder(I18nProvider i18nProvider, Date currentTime, ValidationPolicy policy,
                                                   DiagnosticData diagnosticData, boolean includeSemantics) {
        super(i18nProvider, currentTime, policy, ValidationLevel.BASIC_SIGNATURES, diagnosticData, includeSemantics);
    }

    @Override
    protected void executeValidation(XmlDetailedReport detailedReport, Map<String, XmlBasicBuildingBlocks> bbbs, POEExtraction poe) {
        List<XmlEAAPresentation> eaaPresentations = executeEAAPresentationValidations(bbbs, detailedReport.getTLAnalysis());
        detailedReport.getSignatureOrTimestampOrEvidenceRecord().addAll(eaaPresentations);
    }

    private List<XmlEAAPresentation> executeEAAPresentationValidations(
            Map<String, XmlBasicBuildingBlocks> bbbs, List<XmlTLAnalysis> tlAnalysis) {
        EAAPresentationValidationBlock eaaPresentationValidationBlock = new EAAPresentationValidationBlock(
                i18nProvider, diagnosticData, policy, currentTime, bbbs, tlAnalysis);
        return eaaPresentationValidationBlock.execute();
    }

}
