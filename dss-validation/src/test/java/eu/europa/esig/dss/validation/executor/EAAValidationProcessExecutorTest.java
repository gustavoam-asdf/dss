package eu.europa.esig.dss.validation.executor;

import eu.europa.esig.dss.detailedreport.DetailedReport;
import eu.europa.esig.dss.detailedreport.jaxb.XmlAOV;
import eu.europa.esig.dss.detailedreport.jaxb.XmlBasicBuildingBlocks;
import eu.europa.esig.dss.detailedreport.jaxb.XmlCV;
import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraint;
import eu.europa.esig.dss.detailedreport.jaxb.XmlEAA;
import eu.europa.esig.dss.detailedreport.jaxb.XmlFC;
import eu.europa.esig.dss.detailedreport.jaxb.XmlSAV;
import eu.europa.esig.dss.detailedreport.jaxb.XmlStatus;
import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationProcessEAA;
import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.DiagnosticDataFacade;
import eu.europa.esig.dss.diagnostic.EAAWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlClaim;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDiagnosticData;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDigestMatcher;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAAPayload;
import eu.europa.esig.dss.enumerations.DigestMatcherType;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.Level;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.policy.EtsiValidationPolicy;
import eu.europa.esig.dss.policy.jaxb.LevelConstraint;
import eu.europa.esig.dss.policy.jaxb.MultiValuesConstraint;
import eu.europa.esig.dss.simplereport.SimpleReport;
import eu.europa.esig.dss.simplereport.jaxb.XmlSignature;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.validation.executor.eaa.EAAPresentationProcessExecutor;
import eu.europa.esig.dss.validation.policy.ValidationPolicyLoader;
import eu.europa.esig.dss.validation.process.ValidationProcessUtils;
import eu.europa.esig.dss.validation.reports.Reports;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EAAValidationProcessExecutorTest extends AbstractTestValidationExecutor {

    private static final String EAA_POLICY_LOCATION = "/policy/eaa-constraint.xml";

    private static I18nProvider i18nProvider;

    @BeforeAll
    static void init() {
        i18nProvider = new I18nProvider(Locale.getDefault());
    }

    @Test
    void validTest() throws Exception {
        XmlDiagnosticData diagnosticData = DiagnosticDataFacade.newFacade().unmarshall(
                new File("src/test/resources/diag-data/eaa-validation/diag_data_eaa.xml"));
        assertNotNull(diagnosticData);

        EAAPresentationProcessExecutor executor = new EAAPresentationProcessExecutor();
        executor.setDiagnosticData(diagnosticData);
        executor.setCurrentTime(diagnosticData.getValidationDate());
        executor.setValidationPolicy(loadDefaultPolicy());

        Reports reports = executor.execute();

        SimpleReport simpleReport = reports.getSimpleReport();
        assertNotNull(simpleReport);

        assertEquals(Indication.PASSED, simpleReport.getIndication(simpleReport.getFirstEAAId()));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationErrors(simpleReport.getFirstEAAId())));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationWarnings(simpleReport.getFirstEAAId())));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationInfo(simpleReport.getFirstEAAId())));

        DetailedReport detailedReport = reports.getDetailedReport();
        assertEquals(Indication.PASSED, detailedReport.getFinalIndication(simpleReport.getFirstEAAId()));

        XmlEAA xmlEAA = detailedReport.getXmlEAAById(detailedReport.getFirstEAAId());
        assertNotNull(xmlEAA);

        XmlValidationProcessEAA validationProcessEAA = xmlEAA.getValidationProcessEAA();
        assertNotNull(validationProcessEAA);
        assertEquals(Indication.PASSED, validationProcessEAA.getConclusion().getIndication());

        boolean fcCheckFound = false;
        boolean sigValidationConclusiveCheckFound = false;
        boolean kbSigValidationConclusiveCheckFound = false;
        boolean cvCheckFound = false;
        boolean savCheckFound = false;
        for (XmlConstraint xmlConstraint : validationProcessEAA.getConstraint()) {
            assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
            if (MessageTag.BSV_IFCRC.getId().equals(xmlConstraint.getName().getKey())) {
                fcCheckFound = true;
            } else if (MessageTag.ADEST_IBSVPSC.getId().equals(xmlConstraint.getName().getKey())) {
                sigValidationConclusiveCheckFound = true;
            } else if (MessageTag.EAA_KBRC.getId().equals(xmlConstraint.getName().getKey())) {
                kbSigValidationConclusiveCheckFound = true;
            } else if (MessageTag.BSV_ICVRC.getId().equals(xmlConstraint.getName().getKey())) {
                cvCheckFound = true;
            } else if (MessageTag.BSV_IEAAAVRC.getId().equals(xmlConstraint.getName().getKey())) {
                savCheckFound = true;
            }
        }
        assertTrue(fcCheckFound);
        assertTrue(sigValidationConclusiveCheckFound);
        assertTrue(kbSigValidationConclusiveCheckFound);
        assertTrue(cvCheckFound);
        assertTrue(savCheckFound);

        XmlBasicBuildingBlocks eaaBBB = detailedReport.getBasicBuildingBlockById(xmlEAA.getId());
        assertNotNull(eaaBBB);

        XmlFC xmlFC = eaaBBB.getFC();
        assertNotNull(xmlFC);

        boolean sigPresentCheckFound = false;
        boolean disclosuresPresentCheckFound = false;
        boolean kbSigPresentCheckFound = false;
        for (XmlConstraint xmlConstraint : xmlFC.getConstraint()) {
            assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
            if (MessageTag.EAA_SIG_PRESENT.getId().equals(xmlConstraint.getName().getKey())) {
                sigPresentCheckFound = true;
            } else if (MessageTag.EAA_DPEAAP.getId().equals(xmlConstraint.getName().getKey())) {
                disclosuresPresentCheckFound = true;
            } else if (MessageTag.EAA_KBSP.getId().equals(xmlConstraint.getName().getKey())) {
                kbSigPresentCheckFound = true;
            }
        }
        assertTrue(sigPresentCheckFound);
        assertTrue(disclosuresPresentCheckFound);
        assertTrue(kbSigPresentCheckFound);

        XmlCV xmlCV = eaaBBB.getCV();
        assertNotNull(xmlCV);

        int disclosureFoundCounter = 0;
        int disclosureIntactCounter = 0;
        for (XmlConstraint xmlConstraint : xmlCV.getConstraint()) {
            assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
            if (MessageTag.BBB_CV_EAA_SDCBF.getId().equals(xmlConstraint.getName().getKey())) {
                ++disclosureFoundCounter;
            } else if (MessageTag.BBB_CV_EAA_SDCBI.getId().equals(xmlConstraint.getName().getKey())) {
                ++disclosureIntactCounter;
            }
        }
        assertEquals(10, disclosureFoundCounter);
        assertEquals(10, disclosureIntactCounter);

        XmlAOV xmlAOV = eaaBBB.getAOV();
        assertNotNull(xmlAOV);

        XmlSAV xmlSAV = eaaBBB.getSAV();
        assertNotNull(xmlSAV);

        assertNull(eaaBBB.getISC());
        assertNull(eaaBBB.getVCI());
        assertNull(eaaBBB.getXCV());

        checkReports(reports);
    }

    @Test
    void validWithOrphanDisclosuresTest() throws Exception {
        XmlDiagnosticData diagnosticData = DiagnosticDataFacade.newFacade().unmarshall(
                new File("src/test/resources/diag-data/eaa-validation/diag_data_eaa.xml"));
        assertNotNull(diagnosticData);

        List<XmlDigestMatcher> digestMatchers = diagnosticData.getEAAs().get(0).getDigestMatchers();

        XmlDigestMatcher xmlDigestMatcher = new XmlDigestMatcher();
        xmlDigestMatcher.setType(DigestMatcherType.EAA_ORPHAN_SELECTIVELY_DISCLOSABLE_CLAIM);
        xmlDigestMatcher.setDataFound(false);
        xmlDigestMatcher.setDataIntact(false);
        digestMatchers.add(xmlDigestMatcher);

        EAAPresentationProcessExecutor executor = new EAAPresentationProcessExecutor();
        executor.setDiagnosticData(diagnosticData);
        executor.setCurrentTime(diagnosticData.getValidationDate());
        executor.setValidationPolicy(loadDefaultPolicy());

        Reports reports = executor.execute();

        SimpleReport simpleReport = reports.getSimpleReport();
        assertNotNull(simpleReport);

        assertEquals(Indication.PASSED, simpleReport.getIndication(simpleReport.getFirstEAAId()));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationErrors(simpleReport.getFirstEAAId())));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationWarnings(simpleReport.getFirstEAAId())));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationInfo(simpleReport.getFirstEAAId())));

        DetailedReport detailedReport = reports.getDetailedReport();
        assertEquals(Indication.PASSED, detailedReport.getFinalIndication(simpleReport.getFirstEAAId()));

        XmlEAA xmlEAA = detailedReport.getXmlEAAById(detailedReport.getFirstEAAId());
        assertNotNull(xmlEAA);

        XmlValidationProcessEAA validationProcessEAA = xmlEAA.getValidationProcessEAA();
        assertNotNull(validationProcessEAA);
        assertEquals(Indication.PASSED, validationProcessEAA.getConclusion().getIndication());

        boolean fcCheckFound = false;
        boolean sigValidationConclusiveCheckFound = false;
        boolean kbSigValidationConclusiveCheckFound = false;
        boolean cvCheckFound = false;
        boolean savCheckFound = false;
        for (XmlConstraint xmlConstraint : validationProcessEAA.getConstraint()) {
            assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
            if (MessageTag.BSV_IFCRC.getId().equals(xmlConstraint.getName().getKey())) {
                fcCheckFound = true;
            } else if (MessageTag.ADEST_IBSVPSC.getId().equals(xmlConstraint.getName().getKey())) {
                sigValidationConclusiveCheckFound = true;
            } else if (MessageTag.EAA_KBRC.getId().equals(xmlConstraint.getName().getKey())) {
                kbSigValidationConclusiveCheckFound = true;
            } else if (MessageTag.BSV_ICVRC.getId().equals(xmlConstraint.getName().getKey())) {
                cvCheckFound = true;
            } else if (MessageTag.BSV_IEAAAVRC.getId().equals(xmlConstraint.getName().getKey())) {
                savCheckFound = true;
            }
        }
        assertTrue(fcCheckFound);
        assertTrue(sigValidationConclusiveCheckFound);
        assertTrue(kbSigValidationConclusiveCheckFound);
        assertTrue(cvCheckFound);
        assertTrue(savCheckFound);

        XmlBasicBuildingBlocks eaaBBB = detailedReport.getBasicBuildingBlockById(xmlEAA.getId());
        assertNotNull(eaaBBB);

        XmlFC xmlFC = eaaBBB.getFC();
        assertNotNull(xmlFC);

        boolean sigPresentCheckFound = false;
        boolean disclosuresPresentCheckFound = false;
        boolean kbSigPresentCheckFound = false;
        for (XmlConstraint xmlConstraint : xmlFC.getConstraint()) {
            assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
            if (MessageTag.EAA_SIG_PRESENT.getId().equals(xmlConstraint.getName().getKey())) {
                sigPresentCheckFound = true;
            } else if (MessageTag.EAA_DPEAAP.getId().equals(xmlConstraint.getName().getKey())) {
                disclosuresPresentCheckFound = true;
            } else if (MessageTag.EAA_KBSP.getId().equals(xmlConstraint.getName().getKey())) {
                kbSigPresentCheckFound = true;
            }
        }
        assertTrue(sigPresentCheckFound);
        assertTrue(disclosuresPresentCheckFound);
        assertTrue(kbSigPresentCheckFound);

        XmlCV xmlCV = eaaBBB.getCV();
        assertNotNull(xmlCV);

        int disclosureFoundCounter = 0;
        int disclosureIntactCounter = 0;
        for (XmlConstraint xmlConstraint : xmlCV.getConstraint()) {
            assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
            if (MessageTag.BBB_CV_EAA_SDCBF.getId().equals(xmlConstraint.getName().getKey())) {
                ++disclosureFoundCounter;
            } else if (MessageTag.BBB_CV_EAA_SDCBI.getId().equals(xmlConstraint.getName().getKey())) {
                ++disclosureIntactCounter;
            }
        }
        assertEquals(10, disclosureFoundCounter);
        assertEquals(10, disclosureIntactCounter);

        XmlAOV xmlAOV = eaaBBB.getAOV();
        assertNotNull(xmlAOV);

        XmlSAV xmlSAV = eaaBBB.getSAV();
        assertNotNull(xmlSAV);

        checkReports(reports);
    }

    @Test
    void disclosureNotIntactTest() throws Exception {
        XmlDiagnosticData diagnosticData = DiagnosticDataFacade.newFacade().unmarshall(
                new File("src/test/resources/diag-data/eaa-validation/diag_data_eaa.xml"));
        assertNotNull(diagnosticData);

        List<XmlDigestMatcher> digestMatchers = diagnosticData.getEAAs().get(0).getDigestMatchers();
        digestMatchers.get(0).setDataIntact(false);

        EAAPresentationProcessExecutor executor = new EAAPresentationProcessExecutor();
        executor.setDiagnosticData(diagnosticData);
        executor.setCurrentTime(diagnosticData.getValidationDate());
        executor.setValidationPolicy(loadDefaultPolicy());

        Reports reports = executor.execute();
        reports.print();

        SimpleReport simpleReport = reports.getSimpleReport();
        assertNotNull(simpleReport);

        assertEquals(Indication.FAILED, simpleReport.getIndication(simpleReport.getFirstEAAId()));
        assertEquals(SubIndication.HASH_FAILURE, simpleReport.getSubIndication(simpleReport.getFirstEAAId()));
        assertFalse(Utils.isCollectionEmpty(simpleReport.getAdESValidationErrors(simpleReport.getFirstEAAId())));
        assertTrue(checkMessageValuePresence(simpleReport.getAdESValidationErrors(simpleReport.getFirstEAAId()), i18nProvider.getMessage(MessageTag.BBB_CV_EAA_SDCBI_ANS)));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationWarnings(simpleReport.getFirstEAAId())));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationInfo(simpleReport.getFirstEAAId())));

        DetailedReport detailedReport = reports.getDetailedReport();
        assertEquals(Indication.FAILED, detailedReport.getFinalIndication(simpleReport.getFirstEAAId()));
        assertEquals(SubIndication.HASH_FAILURE, detailedReport.getFinalSubIndication(simpleReport.getFirstEAAId()));

        XmlEAA xmlEAA = detailedReport.getXmlEAAById(detailedReport.getFirstEAAId());
        assertNotNull(xmlEAA);

        XmlValidationProcessEAA validationProcessEAA = xmlEAA.getValidationProcessEAA();
        assertNotNull(validationProcessEAA);
        assertEquals(Indication.FAILED, validationProcessEAA.getConclusion().getIndication());
        assertEquals(SubIndication.HASH_FAILURE, validationProcessEAA.getConclusion().getSubIndication());

        boolean fcCheckFound = false;
        boolean sigValidationConclusiveCheckFound = false;
        boolean kbSigValidationConclusiveCheckFound = false;
        boolean cvCheckFound = false;
        boolean savCheckFound = false;
        for (XmlConstraint xmlConstraint : validationProcessEAA.getConstraint()) {
            if (MessageTag.BSV_IFCRC.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                fcCheckFound = true;
            } else if (MessageTag.ADEST_IBSVPSC.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                sigValidationConclusiveCheckFound = true;
            } else if (MessageTag.EAA_KBRC.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                kbSigValidationConclusiveCheckFound = true;
            } else if (MessageTag.BSV_ICVRC.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.NOT_OK, xmlConstraint.getStatus());
                assertEquals(MessageTag.BSV_ICVRC_ANS.getId(), xmlConstraint.getError().getKey());
                cvCheckFound = true;
            } else if (MessageTag.BSV_IEAAAVRC.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                savCheckFound = true;
            }
        }
        assertTrue(fcCheckFound);
        assertTrue(sigValidationConclusiveCheckFound);
        assertTrue(kbSigValidationConclusiveCheckFound);
        assertTrue(cvCheckFound);
        assertFalse(savCheckFound);

        XmlBasicBuildingBlocks eaaBBB = detailedReport.getBasicBuildingBlockById(xmlEAA.getId());
        assertNotNull(eaaBBB);

        XmlFC xmlFC = eaaBBB.getFC();
        assertNotNull(xmlFC);
        assertEquals(Indication.PASSED, xmlFC.getConclusion().getIndication());

        boolean sigPresentCheckFound = false;
        boolean disclosuresPresentCheckFound = false;
        boolean kbSigPresentCheckFound = false;
        for (XmlConstraint xmlConstraint : xmlFC.getConstraint()) {
            assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
            if (MessageTag.EAA_SIG_PRESENT.getId().equals(xmlConstraint.getName().getKey())) {
                sigPresentCheckFound = true;
            } else if (MessageTag.EAA_DPEAAP.getId().equals(xmlConstraint.getName().getKey())) {
                disclosuresPresentCheckFound = true;
            } else if (MessageTag.EAA_KBSP.getId().equals(xmlConstraint.getName().getKey())) {
                kbSigPresentCheckFound = true;
            }
        }
        assertTrue(sigPresentCheckFound);
        assertTrue(disclosuresPresentCheckFound);
        assertTrue(kbSigPresentCheckFound);

        XmlCV xmlCV = eaaBBB.getCV();
        assertNotNull(xmlCV);
        assertEquals(Indication.FAILED, xmlCV.getConclusion().getIndication());
        assertEquals(SubIndication.HASH_FAILURE, xmlCV.getConclusion().getSubIndication());

        int disclosureFoundCounter = 0;
        int disclosureIntactCounter = 0;
        for (XmlConstraint xmlConstraint : xmlCV.getConstraint()) {
            if (MessageTag.BBB_CV_EAA_SDCBF.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                ++disclosureFoundCounter;
            } else if (MessageTag.BBB_CV_EAA_SDCBI.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.NOT_OK, xmlConstraint.getStatus());
                assertEquals(MessageTag.BBB_CV_EAA_SDCBI_ANS.getId(), xmlConstraint.getError().getKey());
                assertEquals(i18nProvider.getMessage(MessageTag.REFERENCE, digestMatchers.get(0).getDisclosableClaim().getName()), xmlConstraint.getAdditionalInfo());
                ++disclosureIntactCounter;
            }
        }
        assertEquals(1, disclosureFoundCounter);
        assertEquals(1, disclosureIntactCounter);

        XmlAOV xmlAOV = eaaBBB.getAOV();
        assertNotNull(xmlAOV);
        assertEquals(Indication.PASSED, xmlAOV.getConclusion().getIndication());

        XmlSAV xmlSAV = eaaBBB.getSAV();
        assertNotNull(xmlSAV);
        assertEquals(Indication.PASSED, xmlSAV.getConclusion().getIndication());

        checkReports(reports);
    }

    @Test
    void noDisclosuresWarnTest() throws Exception {
        XmlDiagnosticData diagnosticData = DiagnosticDataFacade.newFacade().unmarshall(
                new File("src/test/resources/diag-data/eaa-validation/diag_data_eaa_no_disclosures.xml"));
        assertNotNull(diagnosticData);

        EtsiValidationPolicy validationPolicy = loadDefaultPolicy();
        LevelConstraint levelConstraint = new LevelConstraint();
        levelConstraint.setLevel(Level.WARN);
        validationPolicy.getEAAConstraints().setDisclosurePresent(levelConstraint);

        EAAPresentationProcessExecutor executor = new EAAPresentationProcessExecutor();
        executor.setDiagnosticData(diagnosticData);
        executor.setCurrentTime(diagnosticData.getValidationDate());
        executor.setValidationPolicy(validationPolicy);

        Reports reports = executor.execute();

        SimpleReport simpleReport = reports.getSimpleReport();
        assertNotNull(simpleReport);

        assertEquals(Indication.PASSED, simpleReport.getIndication(simpleReport.getFirstEAAId()));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationErrors(simpleReport.getFirstEAAId())));
        assertFalse(Utils.isCollectionEmpty(simpleReport.getAdESValidationWarnings(simpleReport.getFirstEAAId())));
        assertTrue(checkMessageValuePresence(simpleReport.getAdESValidationWarnings(simpleReport.getFirstEAAId()), i18nProvider.getMessage(MessageTag.EAA_DPEAAP_ANS)));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationInfo(simpleReport.getFirstEAAId())));

        DetailedReport detailedReport = reports.getDetailedReport();
        assertEquals(Indication.PASSED, detailedReport.getFinalIndication(simpleReport.getFirstEAAId()));

        XmlEAA xmlEAA = detailedReport.getXmlEAAById(detailedReport.getFirstEAAId());
        assertNotNull(xmlEAA);

        XmlValidationProcessEAA validationProcessEAA = xmlEAA.getValidationProcessEAA();
        assertNotNull(validationProcessEAA);
        assertEquals(Indication.PASSED, validationProcessEAA.getConclusion().getIndication());

        boolean fcCheckFound = false;
        boolean sigValidationConclusiveCheckFound = false;
        boolean kbSigValidationConclusiveCheckFound = false;
        boolean cvCheckFound = false;
        boolean savCheckFound = false;
        for (XmlConstraint xmlConstraint : validationProcessEAA.getConstraint()) {
            if (MessageTag.BSV_IFCRC.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                fcCheckFound = true;
            } else if (MessageTag.ADEST_IBSVPSC.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                sigValidationConclusiveCheckFound = true;
            } else if (MessageTag.EAA_KBRC.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                kbSigValidationConclusiveCheckFound = true;
            } else if (MessageTag.BSV_ICVRC.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                cvCheckFound = true;
            } else if (MessageTag.BSV_IEAAAVRC.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                savCheckFound = true;
            }
        }
        assertTrue(fcCheckFound);
        assertTrue(sigValidationConclusiveCheckFound);
        assertTrue(kbSigValidationConclusiveCheckFound);
        assertTrue(cvCheckFound);
        assertTrue(savCheckFound);

        XmlBasicBuildingBlocks eaaBBB = detailedReport.getBasicBuildingBlockById(xmlEAA.getId());
        assertNotNull(eaaBBB);

        XmlFC xmlFC = eaaBBB.getFC();
        assertNotNull(xmlFC);
        assertEquals(Indication.PASSED, xmlFC.getConclusion().getIndication());

        boolean sigPresentCheckFound = false;
        boolean disclosuresPresentCheckFound = false;
        boolean kbSigPresentCheckFound = false;
        for (XmlConstraint xmlConstraint : xmlFC.getConstraint()) {
            if (MessageTag.EAA_SIG_PRESENT.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                sigPresentCheckFound = true;
            } else if (MessageTag.EAA_DPEAAP.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.WARNING, xmlConstraint.getStatus());
                assertEquals(MessageTag.EAA_DPEAAP_ANS.getId(), xmlConstraint.getWarning().getKey());
                disclosuresPresentCheckFound = true;
            } else if (MessageTag.EAA_KBSP.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                kbSigPresentCheckFound = true;
            }
        }
        assertTrue(sigPresentCheckFound);
        assertTrue(disclosuresPresentCheckFound);
        assertTrue(kbSigPresentCheckFound);

        XmlCV xmlCV = eaaBBB.getCV();
        assertNotNull(xmlCV);
        assertEquals(Indication.PASSED, xmlCV.getConclusion().getIndication());

        int disclosureFoundCounter = 0;
        int disclosureIntactCounter = 0;
        for (XmlConstraint xmlConstraint : xmlCV.getConstraint()) {
            assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
            if (MessageTag.BBB_CV_EAA_SDCBF.getId().equals(xmlConstraint.getName().getKey())) {
                ++disclosureFoundCounter;
            } else if (MessageTag.BBB_CV_EAA_SDCBI.getId().equals(xmlConstraint.getName().getKey())) {
                ++disclosureIntactCounter;
            }
        }
        assertEquals(0, disclosureFoundCounter);
        assertEquals(0, disclosureIntactCounter);

        XmlAOV xmlAOV = eaaBBB.getAOV();
        assertNotNull(xmlAOV);
        assertEquals(Indication.PASSED, xmlAOV.getConclusion().getIndication());

        XmlSAV xmlSAV = eaaBBB.getSAV();
        assertNotNull(xmlSAV);
        assertEquals(Indication.PASSED, xmlSAV.getConclusion().getIndication());

        checkReports(reports);
    }

    @Test
    void noDisclosuresFailTest() throws Exception {
        XmlDiagnosticData diagnosticData = DiagnosticDataFacade.newFacade().unmarshall(
                new File("src/test/resources/diag-data/eaa-validation/diag_data_eaa_no_disclosures.xml"));
        assertNotNull(diagnosticData);

        EtsiValidationPolicy validationPolicy = loadDefaultPolicy();
        LevelConstraint levelConstraint = new LevelConstraint();
        levelConstraint.setLevel(Level.FAIL);
        validationPolicy.getEAAConstraints().setDisclosurePresent(levelConstraint);

        EAAPresentationProcessExecutor executor = new EAAPresentationProcessExecutor();
        executor.setDiagnosticData(diagnosticData);
        executor.setCurrentTime(diagnosticData.getValidationDate());
        executor.setValidationPolicy(validationPolicy);

        Reports reports = executor.execute();

        SimpleReport simpleReport = reports.getSimpleReport();
        assertNotNull(simpleReport);

        assertEquals(Indication.FAILED, simpleReport.getIndication(simpleReport.getFirstEAAId()));
        assertEquals(SubIndication.FORMAT_FAILURE, simpleReport.getSubIndication(simpleReport.getFirstEAAId()));

        assertFalse(Utils.isCollectionEmpty(simpleReport.getAdESValidationErrors(simpleReport.getFirstEAAId())));
        assertTrue(checkMessageValuePresence(simpleReport.getAdESValidationErrors(simpleReport.getFirstEAAId()), i18nProvider.getMessage(MessageTag.EAA_DPEAAP_ANS)));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationWarnings(simpleReport.getFirstEAAId())));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationInfo(simpleReport.getFirstEAAId())));

        DetailedReport detailedReport = reports.getDetailedReport();
        assertEquals(Indication.FAILED, detailedReport.getFinalIndication(simpleReport.getFirstEAAId()));
        assertEquals(SubIndication.FORMAT_FAILURE, detailedReport.getFinalSubIndication(simpleReport.getFirstEAAId()));

        XmlEAA xmlEAA = detailedReport.getXmlEAAById(detailedReport.getFirstEAAId());
        assertNotNull(xmlEAA);

        XmlValidationProcessEAA validationProcessEAA = xmlEAA.getValidationProcessEAA();
        assertNotNull(validationProcessEAA);
        assertEquals(Indication.FAILED, validationProcessEAA.getConclusion().getIndication());
        assertEquals(SubIndication.FORMAT_FAILURE, validationProcessEAA.getConclusion().getSubIndication());

        boolean fcCheckFound = false;
        boolean sigValidationConclusiveCheckFound = false;
        boolean kbSigValidationConclusiveCheckFound = false;
        boolean cvCheckFound = false;
        boolean savCheckFound = false;
        for (XmlConstraint xmlConstraint : validationProcessEAA.getConstraint()) {
            if (MessageTag.BSV_IFCRC.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.NOT_OK, xmlConstraint.getStatus());
                assertEquals(MessageTag.BSV_IFCRC_ANS.getId(), xmlConstraint.getError().getKey());
                fcCheckFound = true;
            } else if (MessageTag.ADEST_IBSVPSC.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                sigValidationConclusiveCheckFound = true;
            } else if (MessageTag.EAA_KBRC.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                kbSigValidationConclusiveCheckFound = true;
            } else if (MessageTag.BSV_ICVRC.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                cvCheckFound = true;
            } else if (MessageTag.BSV_IEAAAVRC.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                savCheckFound = true;
            }
        }
        assertTrue(fcCheckFound);
        assertFalse(sigValidationConclusiveCheckFound);
        assertFalse(kbSigValidationConclusiveCheckFound);
        assertFalse(cvCheckFound);
        assertFalse(savCheckFound);

        XmlBasicBuildingBlocks eaaBBB = detailedReport.getBasicBuildingBlockById(xmlEAA.getId());
        assertNotNull(eaaBBB);

        XmlFC xmlFC = eaaBBB.getFC();
        assertNotNull(xmlFC);
        assertEquals(Indication.FAILED, xmlFC.getConclusion().getIndication());
        assertEquals(SubIndication.FORMAT_FAILURE, xmlFC.getConclusion().getSubIndication());

        boolean sigPresentCheckFound = false;
        boolean disclosuresPresentCheckFound = false;
        boolean kbSigPresentCheckFound = false;
        for (XmlConstraint xmlConstraint : xmlFC.getConstraint()) {
            if (MessageTag.EAA_SIG_PRESENT.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                sigPresentCheckFound = true;
            } else if (MessageTag.EAA_DPEAAP.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.NOT_OK, xmlConstraint.getStatus());
                assertEquals(MessageTag.EAA_DPEAAP_ANS.getId(), xmlConstraint.getError().getKey());
                disclosuresPresentCheckFound = true;
            } else if (MessageTag.EAA_KBSP.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                kbSigPresentCheckFound = true;
            }
        }
        assertTrue(sigPresentCheckFound);
        assertTrue(disclosuresPresentCheckFound);
        assertFalse(kbSigPresentCheckFound);

        XmlCV xmlCV = eaaBBB.getCV();
        assertNotNull(xmlCV);
        assertEquals(Indication.PASSED, xmlCV.getConclusion().getIndication());

        int disclosureFoundCounter = 0;
        int disclosureIntactCounter = 0;
        for (XmlConstraint xmlConstraint : xmlCV.getConstraint()) {
            assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
            if (MessageTag.BBB_CV_EAA_SDCBF.getId().equals(xmlConstraint.getName().getKey())) {
                ++disclosureFoundCounter;
            } else if (MessageTag.BBB_CV_EAA_SDCBI.getId().equals(xmlConstraint.getName().getKey())) {
                ++disclosureIntactCounter;
            }
        }
        assertEquals(0, disclosureFoundCounter);
        assertEquals(0, disclosureIntactCounter);

        XmlAOV xmlAOV = eaaBBB.getAOV();
        assertNotNull(xmlAOV);
        assertEquals(Indication.PASSED, xmlAOV.getConclusion().getIndication());

        XmlSAV xmlSAV = eaaBBB.getSAV();
        assertNotNull(xmlSAV);
        assertEquals(Indication.PASSED, xmlSAV.getConclusion().getIndication());

        checkReports(reports);
    }

    @Test
    void sigInvalidTest() throws Exception {
        XmlDiagnosticData diagnosticData = DiagnosticDataFacade.newFacade().unmarshall(
                new File("src/test/resources/diag-data/eaa-validation/diag_data_eaa.xml"));
        assertNotNull(diagnosticData);

        EtsiValidationPolicy validationPolicy = loadDefaultPolicy();
        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.getId().add(SignatureLevel.CB_AdES_BASELINE_B.toString());
        constraint.setLevel(Level.FAIL);
        validationPolicy.getSignatureConstraints().setAcceptableFormats(constraint);

        EAAPresentationProcessExecutor executor = new EAAPresentationProcessExecutor();
        executor.setDiagnosticData(diagnosticData);
        executor.setCurrentTime(diagnosticData.getValidationDate());
        executor.setValidationPolicy(validationPolicy);

        Reports reports = executor.execute();

        SimpleReport simpleReport = reports.getSimpleReport();
        assertNotNull(simpleReport);

        assertEquals(Indication.FAILED, simpleReport.getIndication(simpleReport.getFirstEAAId()));
        assertEquals(SubIndication.FORMAT_FAILURE, simpleReport.getSubIndication(simpleReport.getFirstEAAId()));
        assertFalse(Utils.isCollectionEmpty(simpleReport.getAdESValidationErrors(simpleReport.getFirstEAAId())));
        assertTrue(checkMessageValuePresence(simpleReport.getAdESValidationErrors(simpleReport.getFirstEAAId()), i18nProvider.getMessage(MessageTag.ADEST_IBSVPSC_ANS)));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationWarnings(simpleReport.getFirstEAAId())));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationInfo(simpleReport.getFirstEAAId())));

        List<XmlSignature> eaaSignatures = simpleReport.getEAASignatures(simpleReport.getFirstEAAId());
        assertEquals(1, eaaSignatures.size());
        XmlSignature eaaSignature = eaaSignatures.get(0);

        assertEquals(Indication.TOTAL_FAILED, simpleReport.getIndication(eaaSignature.getId()));
        assertEquals(SubIndication.FORMAT_FAILURE, simpleReport.getSubIndication(eaaSignature.getId()));
        assertFalse(Utils.isCollectionEmpty(simpleReport.getAdESValidationErrors(eaaSignature.getId())));
        assertTrue(checkMessageValuePresence(simpleReport.getAdESValidationErrors(eaaSignature.getId()), i18nProvider.getMessage(MessageTag.BBB_FC_IEFF_ANS)));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationWarnings(eaaSignature.getId())));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationInfo(eaaSignature.getId())));

        XmlSignature keyBindingSignature = simpleReport.getEAAKeyBindingSignature(simpleReport.getFirstEAAId());
        assertEquals(Indication.TOTAL_PASSED, simpleReport.getIndication(keyBindingSignature.getId()));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationErrors(keyBindingSignature.getId())));
        assertFalse(Utils.isCollectionEmpty(simpleReport.getAdESValidationWarnings(keyBindingSignature.getId())));
        assertTrue(checkMessageValuePresence(simpleReport.getAdESValidationWarnings(keyBindingSignature.getId()), i18nProvider.getMessage(MessageTag.BBB_ICS_ISCI_ANS)));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationInfo(keyBindingSignature.getId())));

        DetailedReport detailedReport = reports.getDetailedReport();
        assertEquals(Indication.FAILED, detailedReport.getFinalIndication(simpleReport.getFirstEAAId()));
        assertEquals(SubIndication.FORMAT_FAILURE, detailedReport.getFinalSubIndication(simpleReport.getFirstEAAId()));

        XmlEAA xmlEAA = detailedReport.getXmlEAAById(detailedReport.getFirstEAAId());
        assertNotNull(xmlEAA);

        XmlValidationProcessEAA validationProcessEAA = xmlEAA.getValidationProcessEAA();
        assertNotNull(validationProcessEAA);
        assertEquals(Indication.FAILED, validationProcessEAA.getConclusion().getIndication());
        assertEquals(SubIndication.FORMAT_FAILURE, validationProcessEAA.getConclusion().getSubIndication());

        boolean fcCheckFound = false;
        boolean sigValidationConclusiveCheckFound = false;
        boolean kbSigValidationConclusiveCheckFound = false;
        boolean cvCheckFound = false;
        boolean savCheckFound = false;
        for (XmlConstraint xmlConstraint : validationProcessEAA.getConstraint()) {
            if (MessageTag.BSV_IFCRC.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                fcCheckFound = true;
            } else if (MessageTag.ADEST_IBSVPSC.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.NOT_OK, xmlConstraint.getStatus());
                assertEquals(MessageTag.ADEST_IBSVPSC_ANS.getId(), xmlConstraint.getError().getKey());
                sigValidationConclusiveCheckFound = true;
            } else if (MessageTag.EAA_KBRC.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                kbSigValidationConclusiveCheckFound = true;
            } else if (MessageTag.BSV_ICVRC.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                cvCheckFound = true;
            } else if (MessageTag.BSV_IEAAAVRC.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                savCheckFound = true;
            }
        }
        assertTrue(fcCheckFound);
        assertTrue(sigValidationConclusiveCheckFound);
        assertFalse(kbSigValidationConclusiveCheckFound);
        assertFalse(cvCheckFound);
        assertFalse(savCheckFound);

        XmlBasicBuildingBlocks eaaBBB = detailedReport.getBasicBuildingBlockById(xmlEAA.getId());
        assertNotNull(eaaBBB);

        XmlFC xmlFC = eaaBBB.getFC();
        assertNotNull(xmlFC);
        assertEquals(Indication.PASSED, xmlFC.getConclusion().getIndication());

        boolean sigPresentCheckFound = false;
        boolean disclosuresPresentCheckFound = false;
        boolean kbSigPresentCheckFound = false;
        for (XmlConstraint xmlConstraint : xmlFC.getConstraint()) {
            if (MessageTag.EAA_SIG_PRESENT.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                sigPresentCheckFound = true;
            } else if (MessageTag.EAA_DPEAAP.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                disclosuresPresentCheckFound = true;
            } else if (MessageTag.EAA_KBSP.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                kbSigPresentCheckFound = true;
            }
        }
        assertTrue(sigPresentCheckFound);
        assertTrue(disclosuresPresentCheckFound);
        assertTrue(kbSigPresentCheckFound);

        XmlCV xmlCV = eaaBBB.getCV();
        assertNotNull(xmlCV);
        assertEquals(Indication.PASSED, xmlCV.getConclusion().getIndication());

        int disclosureFoundCounter = 0;
        int disclosureIntactCounter = 0;
        for (XmlConstraint xmlConstraint : xmlCV.getConstraint()) {
            assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
            if (MessageTag.BBB_CV_EAA_SDCBF.getId().equals(xmlConstraint.getName().getKey())) {
                ++disclosureFoundCounter;
            } else if (MessageTag.BBB_CV_EAA_SDCBI.getId().equals(xmlConstraint.getName().getKey())) {
                ++disclosureIntactCounter;
            }
        }
        assertEquals(10, disclosureFoundCounter);
        assertEquals(10, disclosureIntactCounter);

        XmlAOV xmlAOV = eaaBBB.getAOV();
        assertNotNull(xmlAOV);
        assertEquals(Indication.PASSED, xmlAOV.getConclusion().getIndication());

        XmlSAV xmlSAV = eaaBBB.getSAV();
        assertNotNull(xmlSAV);
        assertEquals(Indication.PASSED, xmlSAV.getConclusion().getIndication());

        checkReports(reports);
    }

    @Test
    void kbSigInvalidTest() throws Exception {
        XmlDiagnosticData diagnosticData = DiagnosticDataFacade.newFacade().unmarshall(
                new File("src/test/resources/diag-data/eaa-validation/diag_data_eaa.xml"));
        assertNotNull(diagnosticData);

        EtsiValidationPolicy validationPolicy = loadDefaultPolicy();
        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.getId().add(SignatureLevel.CB_AdES_BASELINE_B.toString());
        constraint.setLevel(Level.FAIL);
        validationPolicy.getKeyBindingSignatureConstraints().setAcceptableFormats(constraint);

        EAAPresentationProcessExecutor executor = new EAAPresentationProcessExecutor();
        executor.setDiagnosticData(diagnosticData);
        executor.setCurrentTime(diagnosticData.getValidationDate());
        executor.setValidationPolicy(validationPolicy);

        Reports reports = executor.execute();

        SimpleReport simpleReport = reports.getSimpleReport();
        assertNotNull(simpleReport);

        assertEquals(Indication.FAILED, simpleReport.getIndication(simpleReport.getFirstEAAId()));
        assertEquals(SubIndication.FORMAT_FAILURE, simpleReport.getSubIndication(simpleReport.getFirstEAAId()));
        assertFalse(Utils.isCollectionEmpty(simpleReport.getAdESValidationErrors(simpleReport.getFirstEAAId())));
        assertTrue(checkMessageValuePresence(simpleReport.getAdESValidationErrors(simpleReport.getFirstEAAId()), i18nProvider.getMessage(MessageTag.EAA_KBRC_ANS)));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationWarnings(simpleReport.getFirstEAAId())));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationInfo(simpleReport.getFirstEAAId())));

        List<XmlSignature> eaaSignatures = simpleReport.getEAASignatures(simpleReport.getFirstEAAId());
        assertEquals(1, eaaSignatures.size());
        XmlSignature eaaSignature = eaaSignatures.get(0);

        assertEquals(Indication.TOTAL_PASSED, simpleReport.getIndication(eaaSignature.getId()));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationErrors(eaaSignature.getId())));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationWarnings(eaaSignature.getId())));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationInfo(eaaSignature.getId())));

        XmlSignature keyBindingSignature = simpleReport.getEAAKeyBindingSignature(simpleReport.getFirstEAAId());
        assertEquals(Indication.TOTAL_FAILED, simpleReport.getIndication(keyBindingSignature.getId()));
        assertEquals(SubIndication.FORMAT_FAILURE, simpleReport.getSubIndication(keyBindingSignature.getId()));
        assertFalse(Utils.isCollectionEmpty(simpleReport.getAdESValidationErrors(keyBindingSignature.getId())));
        assertTrue(checkMessageValuePresence(simpleReport.getAdESValidationErrors(keyBindingSignature.getId()), i18nProvider.getMessage(MessageTag.BBB_FC_IEFF_ANS)));
        assertFalse(Utils.isCollectionEmpty(simpleReport.getAdESValidationWarnings(keyBindingSignature.getId())));
        assertTrue(checkMessageValuePresence(simpleReport.getAdESValidationWarnings(keyBindingSignature.getId()), i18nProvider.getMessage(MessageTag.BBB_ICS_ISCI_ANS)));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationInfo(keyBindingSignature.getId())));

        DetailedReport detailedReport = reports.getDetailedReport();
        assertEquals(Indication.FAILED, detailedReport.getFinalIndication(simpleReport.getFirstEAAId()));
        assertEquals(SubIndication.FORMAT_FAILURE, detailedReport.getFinalSubIndication(simpleReport.getFirstEAAId()));

        XmlEAA xmlEAA = detailedReport.getXmlEAAById(detailedReport.getFirstEAAId());
        assertNotNull(xmlEAA);

        XmlValidationProcessEAA validationProcessEAA = xmlEAA.getValidationProcessEAA();
        assertNotNull(validationProcessEAA);
        assertEquals(Indication.FAILED, validationProcessEAA.getConclusion().getIndication());
        assertEquals(SubIndication.FORMAT_FAILURE, validationProcessEAA.getConclusion().getSubIndication());

        boolean fcCheckFound = false;
        boolean sigValidationConclusiveCheckFound = false;
        boolean kbSigValidationConclusiveCheckFound = false;
        boolean cvCheckFound = false;
        boolean savCheckFound = false;
        for (XmlConstraint xmlConstraint : validationProcessEAA.getConstraint()) {
            if (MessageTag.BSV_IFCRC.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                fcCheckFound = true;
            } else if (MessageTag.ADEST_IBSVPSC.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                sigValidationConclusiveCheckFound = true;
            } else if (MessageTag.EAA_KBRC.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.NOT_OK, xmlConstraint.getStatus());
                assertEquals(MessageTag.EAA_KBRC_ANS.getId(), xmlConstraint.getError().getKey());
                kbSigValidationConclusiveCheckFound = true;
            } else if (MessageTag.BSV_ICVRC.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                cvCheckFound = true;
            } else if (MessageTag.BSV_IEAAAVRC.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                savCheckFound = true;
            }
        }
        assertTrue(fcCheckFound);
        assertTrue(sigValidationConclusiveCheckFound);
        assertTrue(kbSigValidationConclusiveCheckFound);
        assertFalse(cvCheckFound);
        assertFalse(savCheckFound);

        XmlBasicBuildingBlocks eaaBBB = detailedReport.getBasicBuildingBlockById(xmlEAA.getId());
        assertNotNull(eaaBBB);

        XmlFC xmlFC = eaaBBB.getFC();
        assertNotNull(xmlFC);
        assertEquals(Indication.PASSED, xmlFC.getConclusion().getIndication());

        boolean sigPresentCheckFound = false;
        boolean disclosuresPresentCheckFound = false;
        boolean kbSigPresentCheckFound = false;
        for (XmlConstraint xmlConstraint : xmlFC.getConstraint()) {
            if (MessageTag.EAA_SIG_PRESENT.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                sigPresentCheckFound = true;
            } else if (MessageTag.EAA_DPEAAP.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                disclosuresPresentCheckFound = true;
            } else if (MessageTag.EAA_KBSP.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                kbSigPresentCheckFound = true;
            }
        }
        assertTrue(sigPresentCheckFound);
        assertTrue(disclosuresPresentCheckFound);
        assertTrue(kbSigPresentCheckFound);

        XmlCV xmlCV = eaaBBB.getCV();
        assertNotNull(xmlCV);
        assertEquals(Indication.PASSED, xmlCV.getConclusion().getIndication());

        int disclosureFoundCounter = 0;
        int disclosureIntactCounter = 0;
        for (XmlConstraint xmlConstraint : xmlCV.getConstraint()) {
            assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
            if (MessageTag.BBB_CV_EAA_SDCBF.getId().equals(xmlConstraint.getName().getKey())) {
                ++disclosureFoundCounter;
            } else if (MessageTag.BBB_CV_EAA_SDCBI.getId().equals(xmlConstraint.getName().getKey())) {
                ++disclosureIntactCounter;
            }
        }
        assertEquals(10, disclosureFoundCounter);
        assertEquals(10, disclosureIntactCounter);

        XmlAOV xmlAOV = eaaBBB.getAOV();
        assertNotNull(xmlAOV);
        assertEquals(Indication.PASSED, xmlAOV.getConclusion().getIndication());

        XmlSAV xmlSAV = eaaBBB.getSAV();
        assertNotNull(xmlSAV);
        assertEquals(Indication.PASSED, xmlSAV.getConclusion().getIndication());

        checkReports(reports);
    }

    @Test
    void technicalPeriodExpiredFailTest() throws Exception {
        XmlDiagnosticData diagnosticData = DiagnosticDataFacade.newFacade().unmarshall(
                new File("src/test/resources/diag-data/eaa-validation/diag_data_eaa.xml"));
        assertNotNull(diagnosticData);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(diagnosticData.getValidationDate());
        calendar.set(Calendar.MINUTE, -1);

        XmlEAAPayload eaaPayload = diagnosticData.getEAAs().get(0).getEAAPayload();
        XmlClaim expirationClaim = new XmlClaim();
        expirationClaim.setDateTime(calendar.getTime());
        eaaPayload.setExpiration(expirationClaim);

        EtsiValidationPolicy validationPolicy = loadDefaultPolicy();
        LevelConstraint levelConstraint = new LevelConstraint();
        levelConstraint.setLevel(Level.FAIL);
        validationPolicy.getEAAConstraints().setEAANotExpired(levelConstraint);
        validationPolicy.getEAAConstraints().setEAAAdministrativePeriodNotExpired(levelConstraint);

        LevelConstraint etsiConstraint = new LevelConstraint();
        etsiConstraint.setLevel(Level.WARN);
        validationPolicy.getEAAConstraints().setETSI194721Conformance(etsiConstraint);

        EAAPresentationProcessExecutor executor = new EAAPresentationProcessExecutor();
        executor.setDiagnosticData(diagnosticData);
        executor.setCurrentTime(diagnosticData.getValidationDate());
        executor.setValidationPolicy(validationPolicy);

        Reports reports = executor.execute();

        SimpleReport simpleReport = reports.getSimpleReport();
        assertNotNull(simpleReport);

        assertEquals(Indication.INDETERMINATE, simpleReport.getIndication(simpleReport.getFirstEAAId()));
        assertEquals(SubIndication.OUT_OF_BOUNDS_NO_POE, simpleReport.getSubIndication(simpleReport.getFirstEAAId()));

        assertFalse(Utils.isCollectionEmpty(simpleReport.getAdESValidationErrors(simpleReport.getFirstEAAId())));
        assertTrue(checkMessageValuePresence(simpleReport.getAdESValidationErrors(simpleReport.getFirstEAAId()), i18nProvider.getMessage(MessageTag.EAA_VT_ITVR_ANS)));
        assertTrue(checkMessageValuePresence(simpleReport.getAdESValidationWarnings(simpleReport.getFirstEAAId()), i18nProvider.getMessage(MessageTag.EAA_ETSI194721_ANS)));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationInfo(simpleReport.getFirstEAAId())));

        DetailedReport detailedReport = reports.getDetailedReport();
        assertEquals(Indication.INDETERMINATE, detailedReport.getFinalIndication(simpleReport.getFirstEAAId()));
        assertEquals(SubIndication.OUT_OF_BOUNDS_NO_POE, detailedReport.getFinalSubIndication(simpleReport.getFirstEAAId()));

        XmlEAA xmlEAA = detailedReport.getXmlEAAById(detailedReport.getFirstEAAId());
        assertNotNull(xmlEAA);

        XmlValidationProcessEAA validationProcessEAA = xmlEAA.getValidationProcessEAA();
        assertNotNull(validationProcessEAA);
        assertEquals(Indication.INDETERMINATE, validationProcessEAA.getConclusion().getIndication());
        assertEquals(SubIndication.OUT_OF_BOUNDS_NO_POE, validationProcessEAA.getConclusion().getSubIndication());

        boolean fcCheckFound = false;
        boolean sigValidationConclusiveCheckFound = false;
        boolean kbSigValidationConclusiveCheckFound = false;
        boolean cvCheckFound = false;
        boolean savCheckFound = false;
        for (XmlConstraint xmlConstraint : validationProcessEAA.getConstraint()) {
            if (MessageTag.BSV_IFCRC.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                fcCheckFound = true;
            } else if (MessageTag.ADEST_IBSVPSC.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                sigValidationConclusiveCheckFound = true;
            } else if (MessageTag.EAA_KBRC.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                kbSigValidationConclusiveCheckFound = true;
            } else if (MessageTag.BSV_ICVRC.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                cvCheckFound = true;
            } else if (MessageTag.BSV_IEAAAVRC.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.NOT_OK, xmlConstraint.getStatus());
                assertEquals(MessageTag.BSV_IEAAAVRC_ANS.getId(), xmlConstraint.getError().getKey());
                savCheckFound = true;
            }
        }
        assertTrue(fcCheckFound);
        assertTrue(sigValidationConclusiveCheckFound);
        assertTrue(kbSigValidationConclusiveCheckFound);
        assertTrue(cvCheckFound);
        assertTrue(savCheckFound);

        XmlBasicBuildingBlocks eaaBBB = detailedReport.getBasicBuildingBlockById(xmlEAA.getId());
        assertNotNull(eaaBBB);

        XmlFC xmlFC = eaaBBB.getFC();
        assertNotNull(xmlFC);
        assertEquals(Indication.PASSED, xmlFC.getConclusion().getIndication());

        XmlCV xmlCV = eaaBBB.getCV();
        assertNotNull(xmlCV);
        assertEquals(Indication.PASSED, xmlCV.getConclusion().getIndication());

        XmlAOV xmlAOV = eaaBBB.getAOV();
        assertNotNull(xmlAOV);
        assertEquals(Indication.PASSED, xmlAOV.getConclusion().getIndication());

        XmlSAV xmlSAV = eaaBBB.getSAV();
        assertNotNull(xmlSAV);
        assertEquals(Indication.INDETERMINATE, xmlSAV.getConclusion().getIndication());
        assertEquals(SubIndication.OUT_OF_BOUNDS_NO_POE, xmlSAV.getConclusion().getSubIndication());

        boolean etsiConformanceCheckFound = false;
        boolean technicalValidityNotBeforeCheckFound = false;
        boolean technicalValidityExpirationCheckFound = false;
        boolean technicalValidityPeriodCheckFound = false;
        boolean administrativeValidityNotBeforeCheckFound = false;
        boolean administrativeValidityExpirationCheckFound = false;
        boolean administrativeValidityPeriodCheckFound = false;

        for (XmlConstraint xmlConstraint : xmlSAV.getConstraint()) {
            if (MessageTag.EAA_ETSI194721.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.WARNING, xmlConstraint.getStatus());
                assertEquals(MessageTag.EAA_ETSI194721_ANS.getId(), xmlConstraint.getWarning().getKey());
                assertEquals(i18nProvider.getMessage(MessageTag.EAA_NOW_AFTER_EXP,
                        ValidationProcessUtils.getFormattedDate(diagnosticData.getValidationDate()),
                        ValidationProcessUtils.getFormattedDate(eaaPayload.getExpiration().getDateTime())), xmlConstraint.getAdditionalInfo());
                etsiConformanceCheckFound = true;
            } else if (MessageTag.EAA_NBF_PRESENT.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                technicalValidityNotBeforeCheckFound = true;
            } else if (MessageTag.EAA_EXP_PRESENT.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                technicalValidityExpirationCheckFound = true;
            } else if (MessageTag.EAA_VT_ITVR.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.NOT_OK, xmlConstraint.getStatus());
                assertEquals(MessageTag.EAA_VT_ITVR_ANS.getId(), xmlConstraint.getError().getKey());
                assertEquals(i18nProvider.getMessage(MessageTag.EAA_VT_ITVR_VALIDITY,
                        ValidationProcessUtils.getFormattedDate(diagnosticData.getValidationDate()),
                        ValidationProcessUtils.getFormattedDate(eaaPayload.getNotBefore().getDateTime()),
                        ValidationProcessUtils.getFormattedDate(eaaPayload.getExpiration().getDateTime())), xmlConstraint.getAdditionalInfo());
                technicalValidityPeriodCheckFound = true;
            } else if (MessageTag.EAA_AID_PRESENT.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                administrativeValidityNotBeforeCheckFound = true;
            } else if (MessageTag.EAA_AED_PRESENT.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                administrativeValidityExpirationCheckFound = true;
            } else if (MessageTag.EAA_VT_IAVR.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                administrativeValidityPeriodCheckFound = true;
            }
        }
        assertTrue(etsiConformanceCheckFound);
        assertTrue(technicalValidityNotBeforeCheckFound);
        assertTrue(technicalValidityExpirationCheckFound);
        assertTrue(technicalValidityPeriodCheckFound);
        assertFalse(administrativeValidityNotBeforeCheckFound);
        assertFalse(administrativeValidityExpirationCheckFound);
        assertFalse(administrativeValidityPeriodCheckFound);

        checkReports(reports);
    }

    @Test
    void technicalPeriodExpiredWarnTest() throws Exception {
        XmlDiagnosticData diagnosticData = DiagnosticDataFacade.newFacade().unmarshall(
                new File("src/test/resources/diag-data/eaa-validation/diag_data_eaa.xml"));
        assertNotNull(diagnosticData);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(diagnosticData.getValidationDate());
        calendar.set(Calendar.MINUTE, -1);

        XmlEAAPayload eaaPayload = diagnosticData.getEAAs().get(0).getEAAPayload();
        XmlClaim expirationClaim = new XmlClaim();
        expirationClaim.setDateTime(calendar.getTime());
        eaaPayload.setExpiration(expirationClaim);

        EtsiValidationPolicy validationPolicy = loadDefaultPolicy();
        LevelConstraint levelConstraint = new LevelConstraint();
        levelConstraint.setLevel(Level.WARN);
        validationPolicy.getEAAConstraints().setEAANotExpired(levelConstraint);
        validationPolicy.getEAAConstraints().setEAAAdministrativePeriodNotExpired(levelConstraint);

        LevelConstraint etsiConstraint = new LevelConstraint();
        etsiConstraint.setLevel(Level.WARN);
        validationPolicy.getEAAConstraints().setETSI194721Conformance(etsiConstraint);

        EAAPresentationProcessExecutor executor = new EAAPresentationProcessExecutor();
        executor.setDiagnosticData(diagnosticData);
        executor.setCurrentTime(diagnosticData.getValidationDate());
        executor.setValidationPolicy(validationPolicy);

        Reports reports = executor.execute();

        SimpleReport simpleReport = reports.getSimpleReport();
        assertNotNull(simpleReport);

        assertEquals(Indication.PASSED, simpleReport.getIndication(simpleReport.getFirstEAAId()));

        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationErrors(simpleReport.getFirstEAAId())));
        assertTrue(checkMessageValuePresence(simpleReport.getAdESValidationWarnings(simpleReport.getFirstEAAId()), i18nProvider.getMessage(MessageTag.EAA_VT_ITVR_ANS)));
        assertTrue(checkMessageValuePresence(simpleReport.getAdESValidationWarnings(simpleReport.getFirstEAAId()), i18nProvider.getMessage(MessageTag.EAA_ETSI194721_ANS)));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationInfo(simpleReport.getFirstEAAId())));

        DetailedReport detailedReport = reports.getDetailedReport();
        assertEquals(Indication.PASSED, detailedReport.getFinalIndication(simpleReport.getFirstEAAId()));

        XmlEAA xmlEAA = detailedReport.getXmlEAAById(detailedReport.getFirstEAAId());
        assertNotNull(xmlEAA);

        XmlValidationProcessEAA validationProcessEAA = xmlEAA.getValidationProcessEAA();
        assertNotNull(validationProcessEAA);
        assertEquals(Indication.PASSED, validationProcessEAA.getConclusion().getIndication());

        XmlBasicBuildingBlocks eaaBBB = detailedReport.getBasicBuildingBlockById(xmlEAA.getId());
        assertNotNull(eaaBBB);

        XmlFC xmlFC = eaaBBB.getFC();
        assertNotNull(xmlFC);
        assertEquals(Indication.PASSED, xmlFC.getConclusion().getIndication());

        XmlCV xmlCV = eaaBBB.getCV();
        assertNotNull(xmlCV);
        assertEquals(Indication.PASSED, xmlCV.getConclusion().getIndication());

        XmlAOV xmlAOV = eaaBBB.getAOV();
        assertNotNull(xmlAOV);
        assertEquals(Indication.PASSED, xmlAOV.getConclusion().getIndication());

        XmlSAV xmlSAV = eaaBBB.getSAV();
        assertNotNull(xmlSAV);
        assertEquals(Indication.PASSED, xmlSAV.getConclusion().getIndication());

        boolean etsiConformanceCheckFound = false;
        boolean technicalValidityNotBeforeCheckFound = false;
        boolean technicalValidityExpirationCheckFound = false;
        boolean technicalValidityPeriodCheckFound = false;
        boolean administrativeValidityNotBeforeCheckFound = false;
        boolean administrativeValidityExpirationCheckFound = false;
        boolean administrativeValidityPeriodCheckFound = false;

        for (XmlConstraint xmlConstraint : xmlSAV.getConstraint()) {
            if (MessageTag.EAA_ETSI194721.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.WARNING, xmlConstraint.getStatus());
                assertEquals(MessageTag.EAA_ETSI194721_ANS.getId(), xmlConstraint.getWarning().getKey());
                assertEquals(i18nProvider.getMessage(MessageTag.EAA_NOW_AFTER_EXP,
                        ValidationProcessUtils.getFormattedDate(diagnosticData.getValidationDate()),
                        ValidationProcessUtils.getFormattedDate(eaaPayload.getExpiration().getDateTime())), xmlConstraint.getAdditionalInfo());
                etsiConformanceCheckFound = true;
            } else if (MessageTag.EAA_NBF_PRESENT.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                technicalValidityNotBeforeCheckFound = true;
            } else if (MessageTag.EAA_EXP_PRESENT.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                technicalValidityExpirationCheckFound = true;
            } else if (MessageTag.EAA_VT_ITVR.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.WARNING, xmlConstraint.getStatus());
                assertEquals(MessageTag.EAA_VT_ITVR_ANS.getId(), xmlConstraint.getWarning().getKey());
                assertEquals(i18nProvider.getMessage(MessageTag.EAA_VT_ITVR_VALIDITY,
                        ValidationProcessUtils.getFormattedDate(diagnosticData.getValidationDate()),
                        ValidationProcessUtils.getFormattedDate(eaaPayload.getNotBefore().getDateTime()),
                        ValidationProcessUtils.getFormattedDate(eaaPayload.getExpiration().getDateTime())), xmlConstraint.getAdditionalInfo());
                technicalValidityPeriodCheckFound = true;
            } else if (MessageTag.EAA_AID_PRESENT.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                administrativeValidityNotBeforeCheckFound = true;
            } else if (MessageTag.EAA_AED_PRESENT.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                administrativeValidityExpirationCheckFound = true;
            } else if (MessageTag.EAA_VT_IAVR.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                administrativeValidityPeriodCheckFound = true;
            }
        }
        assertTrue(etsiConformanceCheckFound);
        assertTrue(technicalValidityNotBeforeCheckFound);
        assertTrue(technicalValidityExpirationCheckFound);
        assertTrue(technicalValidityPeriodCheckFound);
        assertFalse(administrativeValidityNotBeforeCheckFound);
        assertFalse(administrativeValidityExpirationCheckFound);
        assertFalse(administrativeValidityPeriodCheckFound);

        checkReports(reports);
    }

    @Test
    void technicalPeriodExpiredWarnEtsiFailTest() throws Exception {
        XmlDiagnosticData diagnosticData = DiagnosticDataFacade.newFacade().unmarshall(
                new File("src/test/resources/diag-data/eaa-validation/diag_data_eaa.xml"));
        assertNotNull(diagnosticData);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(diagnosticData.getValidationDate());
        calendar.set(Calendar.MINUTE, -1);

        XmlEAAPayload eaaPayload = diagnosticData.getEAAs().get(0).getEAAPayload();
        XmlClaim expirationClaim = new XmlClaim();
        expirationClaim.setDateTime(calendar.getTime());
        eaaPayload.setExpiration(expirationClaim);

        EtsiValidationPolicy validationPolicy = loadDefaultPolicy();
        LevelConstraint levelConstraint = new LevelConstraint();
        levelConstraint.setLevel(Level.WARN);
        validationPolicy.getEAAConstraints().setEAANotExpired(levelConstraint);
        validationPolicy.getEAAConstraints().setEAAAdministrativePeriodNotExpired(levelConstraint);

        LevelConstraint etsiConstraint = new LevelConstraint();
        etsiConstraint.setLevel(Level.FAIL);
        validationPolicy.getEAAConstraints().setETSI194721Conformance(etsiConstraint);

        EAAPresentationProcessExecutor executor = new EAAPresentationProcessExecutor();
        executor.setDiagnosticData(diagnosticData);
        executor.setCurrentTime(diagnosticData.getValidationDate());
        executor.setValidationPolicy(validationPolicy);

        Reports reports = executor.execute();

        SimpleReport simpleReport = reports.getSimpleReport();
        assertNotNull(simpleReport);

        assertEquals(Indication.INDETERMINATE, simpleReport.getIndication(simpleReport.getFirstEAAId()));
        assertEquals(SubIndication.EAA_CONSTRAINTS_FAILURE, simpleReport.getSubIndication(simpleReport.getFirstEAAId()));

        assertTrue(checkMessageValuePresence(simpleReport.getAdESValidationErrors(simpleReport.getFirstEAAId()), i18nProvider.getMessage(MessageTag.EAA_ETSI194721_ANS)));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationWarnings(simpleReport.getFirstEAAId())));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationInfo(simpleReport.getFirstEAAId())));

        DetailedReport detailedReport = reports.getDetailedReport();
        assertEquals(Indication.INDETERMINATE, detailedReport.getFinalIndication(simpleReport.getFirstEAAId()));
        assertEquals(SubIndication.EAA_CONSTRAINTS_FAILURE, detailedReport.getFinalSubIndication(simpleReport.getFirstEAAId()));

        XmlEAA xmlEAA = detailedReport.getXmlEAAById(detailedReport.getFirstEAAId());
        assertNotNull(xmlEAA);

        XmlValidationProcessEAA validationProcessEAA = xmlEAA.getValidationProcessEAA();
        assertNotNull(validationProcessEAA);
        assertEquals(Indication.INDETERMINATE, validationProcessEAA.getConclusion().getIndication());
        assertEquals(SubIndication.EAA_CONSTRAINTS_FAILURE, validationProcessEAA.getConclusion().getSubIndication());

        XmlBasicBuildingBlocks eaaBBB = detailedReport.getBasicBuildingBlockById(xmlEAA.getId());
        assertNotNull(eaaBBB);

        XmlFC xmlFC = eaaBBB.getFC();
        assertNotNull(xmlFC);
        assertEquals(Indication.PASSED, xmlFC.getConclusion().getIndication());

        XmlCV xmlCV = eaaBBB.getCV();
        assertNotNull(xmlCV);
        assertEquals(Indication.PASSED, xmlCV.getConclusion().getIndication());

        XmlAOV xmlAOV = eaaBBB.getAOV();
        assertNotNull(xmlAOV);
        assertEquals(Indication.PASSED, xmlAOV.getConclusion().getIndication());

        XmlSAV xmlSAV = eaaBBB.getSAV();
        assertNotNull(xmlSAV);
        assertEquals(Indication.INDETERMINATE, xmlSAV.getConclusion().getIndication());
        assertEquals(SubIndication.EAA_CONSTRAINTS_FAILURE, xmlSAV.getConclusion().getSubIndication());

        boolean etsiConformanceCheckFound = false;
        boolean technicalValidityNotBeforeCheckFound = false;
        boolean technicalValidityExpirationCheckFound = false;
        boolean technicalValidityPeriodCheckFound = false;
        boolean administrativeValidityNotBeforeCheckFound = false;
        boolean administrativeValidityExpirationCheckFound = false;
        boolean administrativeValidityPeriodCheckFound = false;

        for (XmlConstraint xmlConstraint : xmlSAV.getConstraint()) {
            if (MessageTag.EAA_ETSI194721.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.NOT_OK, xmlConstraint.getStatus());
                assertEquals(MessageTag.EAA_ETSI194721_ANS.getId(), xmlConstraint.getError().getKey());
                assertEquals(i18nProvider.getMessage(MessageTag.EAA_NOW_AFTER_EXP,
                        ValidationProcessUtils.getFormattedDate(diagnosticData.getValidationDate()),
                        ValidationProcessUtils.getFormattedDate(eaaPayload.getExpiration().getDateTime())), xmlConstraint.getAdditionalInfo());
                etsiConformanceCheckFound = true;
            } else if (MessageTag.EAA_NBF_PRESENT.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                technicalValidityNotBeforeCheckFound = true;
            } else if (MessageTag.EAA_EXP_PRESENT.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                technicalValidityExpirationCheckFound = true;
            } else if (MessageTag.EAA_VT_ITVR.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                technicalValidityPeriodCheckFound = true;
            } else if (MessageTag.EAA_AID_PRESENT.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                administrativeValidityNotBeforeCheckFound = true;
            } else if (MessageTag.EAA_AED_PRESENT.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                administrativeValidityExpirationCheckFound = true;
            } else if (MessageTag.EAA_VT_IAVR.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                administrativeValidityPeriodCheckFound = true;
            }
        }
        assertTrue(etsiConformanceCheckFound);
        assertFalse(technicalValidityNotBeforeCheckFound);
        assertFalse(technicalValidityExpirationCheckFound);
        assertFalse(technicalValidityPeriodCheckFound);
        assertFalse(administrativeValidityNotBeforeCheckFound);
        assertFalse(administrativeValidityExpirationCheckFound);
        assertFalse(administrativeValidityPeriodCheckFound);

        checkReports(reports);
    }

    @Test
    void administrativePeriodExpiredFailTest() throws Exception {
        XmlDiagnosticData diagnosticData = DiagnosticDataFacade.newFacade().unmarshall(
                new File("src/test/resources/diag-data/eaa-validation/diag_data_eaa.xml"));
        assertNotNull(diagnosticData);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(diagnosticData.getValidationDate());
        calendar.set(Calendar.MINUTE, -1);

        XmlEAAPayload eaaPayload = diagnosticData.getEAAs().get(0).getEAAPayload();
        eaaPayload.setAdministrativeIssuanceDate(eaaPayload.getNotBefore());

        XmlClaim expirationClaim = new XmlClaim();
        expirationClaim.setDateTime(calendar.getTime());
        eaaPayload.setAdministrativeExpirationDate(expirationClaim);

        EtsiValidationPolicy validationPolicy = loadDefaultPolicy();
        LevelConstraint levelConstraint = new LevelConstraint();
        levelConstraint.setLevel(Level.FAIL);
        validationPolicy.getEAAConstraints().setEAANotExpired(levelConstraint);
        validationPolicy.getEAAConstraints().setEAAAdministrativePeriodNotExpired(levelConstraint);

        LevelConstraint etsiConstraint = new LevelConstraint();
        etsiConstraint.setLevel(Level.WARN);
        validationPolicy.getEAAConstraints().setETSI194721Conformance(etsiConstraint);

        EAAPresentationProcessExecutor executor = new EAAPresentationProcessExecutor();
        executor.setDiagnosticData(diagnosticData);
        executor.setCurrentTime(diagnosticData.getValidationDate());
        executor.setValidationPolicy(validationPolicy);

        Reports reports = executor.execute();

        SimpleReport simpleReport = reports.getSimpleReport();
        assertNotNull(simpleReport);

        assertEquals(Indication.INDETERMINATE, simpleReport.getIndication(simpleReport.getFirstEAAId()));
        assertEquals(SubIndication.OUT_OF_BOUNDS_NO_POE, simpleReport.getSubIndication(simpleReport.getFirstEAAId()));

        assertFalse(Utils.isCollectionEmpty(simpleReport.getAdESValidationErrors(simpleReport.getFirstEAAId())));
        assertTrue(checkMessageValuePresence(simpleReport.getAdESValidationErrors(simpleReport.getFirstEAAId()), i18nProvider.getMessage(MessageTag.EAA_VT_IAVR_ANS)));
        assertTrue(checkMessageValuePresence(simpleReport.getAdESValidationWarnings(simpleReport.getFirstEAAId()), i18nProvider.getMessage(MessageTag.EAA_ETSI194721_ANS)));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationInfo(simpleReport.getFirstEAAId())));

        DetailedReport detailedReport = reports.getDetailedReport();
        assertEquals(Indication.INDETERMINATE, detailedReport.getFinalIndication(simpleReport.getFirstEAAId()));
        assertEquals(SubIndication.OUT_OF_BOUNDS_NO_POE, detailedReport.getFinalSubIndication(simpleReport.getFirstEAAId()));

        XmlEAA xmlEAA = detailedReport.getXmlEAAById(detailedReport.getFirstEAAId());
        assertNotNull(xmlEAA);

        XmlValidationProcessEAA validationProcessEAA = xmlEAA.getValidationProcessEAA();
        assertNotNull(validationProcessEAA);
        assertEquals(Indication.INDETERMINATE, validationProcessEAA.getConclusion().getIndication());
        assertEquals(SubIndication.OUT_OF_BOUNDS_NO_POE, validationProcessEAA.getConclusion().getSubIndication());

        XmlBasicBuildingBlocks eaaBBB = detailedReport.getBasicBuildingBlockById(xmlEAA.getId());
        assertNotNull(eaaBBB);

        XmlFC xmlFC = eaaBBB.getFC();
        assertNotNull(xmlFC);
        assertEquals(Indication.PASSED, xmlFC.getConclusion().getIndication());

        XmlCV xmlCV = eaaBBB.getCV();
        assertNotNull(xmlCV);
        assertEquals(Indication.PASSED, xmlCV.getConclusion().getIndication());

        XmlAOV xmlAOV = eaaBBB.getAOV();
        assertNotNull(xmlAOV);
        assertEquals(Indication.PASSED, xmlAOV.getConclusion().getIndication());

        XmlSAV xmlSAV = eaaBBB.getSAV();
        assertNotNull(xmlSAV);
        assertEquals(Indication.INDETERMINATE, xmlSAV.getConclusion().getIndication());
        assertEquals(SubIndication.OUT_OF_BOUNDS_NO_POE, xmlSAV.getConclusion().getSubIndication());

        boolean etsiConformanceCheckFound = false;
        boolean technicalValidityNotBeforeCheckFound = false;
        boolean technicalValidityExpirationCheckFound = false;
        boolean technicalValidityPeriodCheckFound = false;
        boolean administrativeValidityNotBeforeCheckFound = false;
        boolean administrativeValidityExpirationCheckFound = false;
        boolean administrativeValidityPeriodCheckFound = false;

        for (XmlConstraint xmlConstraint : xmlSAV.getConstraint()) {
            if (MessageTag.EAA_ETSI194721.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.WARNING, xmlConstraint.getStatus());
                assertEquals(MessageTag.EAA_ETSI194721_ANS.getId(), xmlConstraint.getWarning().getKey());
                assertEquals(i18nProvider.getMessage(MessageTag.EAA_NOW_AFTER_ADE,
                        ValidationProcessUtils.getFormattedDate(diagnosticData.getValidationDate()),
                        ValidationProcessUtils.getFormattedDate(eaaPayload.getAdministrativeExpirationDate().getDateTime())), xmlConstraint.getAdditionalInfo());
                etsiConformanceCheckFound = true;
            } else if (MessageTag.EAA_NBF_PRESENT.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                technicalValidityNotBeforeCheckFound = true;
            } else if (MessageTag.EAA_EXP_PRESENT.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                technicalValidityExpirationCheckFound = true;
            } else if (MessageTag.EAA_VT_ITVR.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                technicalValidityPeriodCheckFound = true;
            } else if (MessageTag.EAA_AID_PRESENT.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                administrativeValidityNotBeforeCheckFound = true;
            } else if (MessageTag.EAA_AED_PRESENT.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                administrativeValidityExpirationCheckFound = true;
            } else if (MessageTag.EAA_VT_IAVR.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.NOT_OK, xmlConstraint.getStatus());
                assertEquals(MessageTag.EAA_VT_IAVR_ANS.getId(), xmlConstraint.getError().getKey());
                assertEquals(i18nProvider.getMessage(MessageTag.EAA_VT_IAVR_VALIDITY,
                        ValidationProcessUtils.getFormattedDate(diagnosticData.getValidationDate()),
                        ValidationProcessUtils.getFormattedDate(eaaPayload.getAdministrativeIssuanceDate().getDateTime()),
                        ValidationProcessUtils.getFormattedDate(eaaPayload.getAdministrativeExpirationDate().getDateTime())), xmlConstraint.getAdditionalInfo());
                administrativeValidityPeriodCheckFound = true;
            }
        }
        assertTrue(etsiConformanceCheckFound);
        assertTrue(technicalValidityNotBeforeCheckFound);
        assertTrue(technicalValidityExpirationCheckFound);
        assertTrue(technicalValidityPeriodCheckFound);
        assertFalse(administrativeValidityNotBeforeCheckFound);
        assertFalse(administrativeValidityExpirationCheckFound);
        assertTrue(administrativeValidityPeriodCheckFound);

        checkReports(reports);
    }

    @Test
    void administrativePeriodExpiredWarnAllChecksPresentTest() throws Exception {
        XmlDiagnosticData diagnosticData = DiagnosticDataFacade.newFacade().unmarshall(
                new File("src/test/resources/diag-data/eaa-validation/diag_data_eaa.xml"));
        assertNotNull(diagnosticData);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(diagnosticData.getValidationDate());
        calendar.set(Calendar.MINUTE, -1);

        XmlEAAPayload eaaPayload = diagnosticData.getEAAs().get(0).getEAAPayload();
        eaaPayload.setAdministrativeIssuanceDate(eaaPayload.getNotBefore());

        XmlClaim expirationClaim = new XmlClaim();
        expirationClaim.setDateTime(calendar.getTime());
        eaaPayload.setAdministrativeExpirationDate(expirationClaim);

        EtsiValidationPolicy validationPolicy = loadDefaultPolicy();
        LevelConstraint levelConstraint = new LevelConstraint();
        levelConstraint.setLevel(Level.WARN);
        validationPolicy.getEAAConstraints().setEAANotExpired(levelConstraint);
        validationPolicy.getEAAConstraints().setEAAAdministrativeIssuanceDatePresent(levelConstraint);
        validationPolicy.getEAAConstraints().setEAAAdministrativeExpirationDatePresent(levelConstraint);
        validationPolicy.getEAAConstraints().setEAAAdministrativePeriodNotExpired(levelConstraint);

        LevelConstraint etsiConstraint = new LevelConstraint();
        etsiConstraint.setLevel(Level.WARN);
        validationPolicy.getEAAConstraints().setETSI194721Conformance(etsiConstraint);

        EAAPresentationProcessExecutor executor = new EAAPresentationProcessExecutor();
        executor.setDiagnosticData(diagnosticData);
        executor.setCurrentTime(diagnosticData.getValidationDate());
        executor.setValidationPolicy(validationPolicy);

        Reports reports = executor.execute();

        SimpleReport simpleReport = reports.getSimpleReport();
        assertNotNull(simpleReport);

        assertEquals(Indication.PASSED, simpleReport.getIndication(simpleReport.getFirstEAAId()));

        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationErrors(simpleReport.getFirstEAAId())));
        assertTrue(checkMessageValuePresence(simpleReport.getAdESValidationWarnings(simpleReport.getFirstEAAId()), i18nProvider.getMessage(MessageTag.EAA_VT_IAVR_ANS)));
        assertTrue(checkMessageValuePresence(simpleReport.getAdESValidationWarnings(simpleReport.getFirstEAAId()), i18nProvider.getMessage(MessageTag.EAA_ETSI194721_ANS)));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationInfo(simpleReport.getFirstEAAId())));

        DetailedReport detailedReport = reports.getDetailedReport();
        assertEquals(Indication.PASSED, detailedReport.getFinalIndication(simpleReport.getFirstEAAId()));

        XmlEAA xmlEAA = detailedReport.getXmlEAAById(detailedReport.getFirstEAAId());
        assertNotNull(xmlEAA);

        XmlValidationProcessEAA validationProcessEAA = xmlEAA.getValidationProcessEAA();
        assertNotNull(validationProcessEAA);
        assertEquals(Indication.PASSED, validationProcessEAA.getConclusion().getIndication());

        XmlBasicBuildingBlocks eaaBBB = detailedReport.getBasicBuildingBlockById(xmlEAA.getId());
        assertNotNull(eaaBBB);

        XmlFC xmlFC = eaaBBB.getFC();
        assertNotNull(xmlFC);
        assertEquals(Indication.PASSED, xmlFC.getConclusion().getIndication());

        XmlCV xmlCV = eaaBBB.getCV();
        assertNotNull(xmlCV);
        assertEquals(Indication.PASSED, xmlCV.getConclusion().getIndication());

        XmlAOV xmlAOV = eaaBBB.getAOV();
        assertNotNull(xmlAOV);
        assertEquals(Indication.PASSED, xmlAOV.getConclusion().getIndication());

        XmlSAV xmlSAV = eaaBBB.getSAV();
        assertNotNull(xmlSAV);
        assertEquals(Indication.PASSED, xmlSAV.getConclusion().getIndication());

        boolean etsiConformanceCheckFound = false;
        boolean technicalValidityNotBeforeCheckFound = false;
        boolean technicalValidityExpirationCheckFound = false;
        boolean technicalValidityPeriodCheckFound = false;
        boolean administrativeValidityNotBeforeCheckFound = false;
        boolean administrativeValidityExpirationCheckFound = false;
        boolean administrativeValidityPeriodCheckFound = false;

        for (XmlConstraint xmlConstraint : xmlSAV.getConstraint()) {
            if (MessageTag.EAA_ETSI194721.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.WARNING, xmlConstraint.getStatus());
                assertEquals(MessageTag.EAA_ETSI194721_ANS.getId(), xmlConstraint.getWarning().getKey());
                assertEquals(i18nProvider.getMessage(MessageTag.EAA_NOW_AFTER_ADE,
                        ValidationProcessUtils.getFormattedDate(diagnosticData.getValidationDate()),
                        ValidationProcessUtils.getFormattedDate(eaaPayload.getAdministrativeExpirationDate().getDateTime())), xmlConstraint.getAdditionalInfo());
                etsiConformanceCheckFound = true;
            } else if (MessageTag.EAA_NBF_PRESENT.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                technicalValidityNotBeforeCheckFound = true;
            } else if (MessageTag.EAA_EXP_PRESENT.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                technicalValidityExpirationCheckFound = true;
            } else if (MessageTag.EAA_VT_ITVR.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                technicalValidityPeriodCheckFound = true;
            } else if (MessageTag.EAA_AID_PRESENT.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                administrativeValidityNotBeforeCheckFound = true;
            } else if (MessageTag.EAA_AED_PRESENT.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                administrativeValidityExpirationCheckFound = true;
            } else if (MessageTag.EAA_VT_IAVR.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.WARNING, xmlConstraint.getStatus());
                assertEquals(MessageTag.EAA_VT_IAVR_ANS.getId(), xmlConstraint.getWarning().getKey());
                assertEquals(i18nProvider.getMessage(MessageTag.EAA_VT_IAVR_VALIDITY,
                        ValidationProcessUtils.getFormattedDate(diagnosticData.getValidationDate()),
                        ValidationProcessUtils.getFormattedDate(eaaPayload.getAdministrativeIssuanceDate().getDateTime()),
                        ValidationProcessUtils.getFormattedDate(eaaPayload.getAdministrativeExpirationDate().getDateTime())), xmlConstraint.getAdditionalInfo());
                administrativeValidityPeriodCheckFound = true;
            }
        }
        assertTrue(etsiConformanceCheckFound);
        assertTrue(technicalValidityNotBeforeCheckFound);
        assertTrue(technicalValidityExpirationCheckFound);
        assertTrue(technicalValidityPeriodCheckFound);
        assertTrue(administrativeValidityNotBeforeCheckFound);
        assertTrue(administrativeValidityExpirationCheckFound);
        assertTrue(administrativeValidityPeriodCheckFound);

        checkReports(reports);
    }

    @Test
    void claimsValidTest() throws Exception {
        XmlDiagnosticData xmlDiagnosticData = DiagnosticDataFacade.newFacade().unmarshall(
                new File("src/test/resources/diag-data/eaa-validation/diag_data_eaa.xml"));
        assertNotNull(xmlDiagnosticData);

        EtsiValidationPolicy validationPolicy = loadDefaultPolicy();

        DiagnosticData diagnosticData = new DiagnosticData(xmlDiagnosticData);
        EAAWrapper eaaWrapper = diagnosticData.getEAAById(diagnosticData.getFirstEAAId());

        MultiValuesConstraint claims = new MultiValuesConstraint();
        claims.setLevel(Level.FAIL);
        claims.getId().add("given_name");
        claims.getId().add("family_name");
        claims.getId().add("birthdate");
        validationPolicy.getEAAConstraints().setEAAClaims(claims);

        MultiValuesConstraint supportedClaims = new MultiValuesConstraint();
        supportedClaims.setLevel(Level.FAIL);
        supportedClaims.getId().addAll(eaaWrapper.getAllEAAPayloadClaimNames());
        validationPolicy.getEAAConstraints().setEAASupportedClaims(supportedClaims);

        EAAPresentationProcessExecutor executor = new EAAPresentationProcessExecutor();
        executor.setDiagnosticData(xmlDiagnosticData);
        executor.setCurrentTime(xmlDiagnosticData.getValidationDate());
        executor.setValidationPolicy(validationPolicy);

        Reports reports = executor.execute();

        SimpleReport simpleReport = reports.getSimpleReport();
        assertNotNull(simpleReport);

        assertEquals(Indication.PASSED, simpleReport.getIndication(simpleReport.getFirstEAAId()));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationErrors(simpleReport.getFirstEAAId())));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationWarnings(simpleReport.getFirstEAAId())));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationInfo(simpleReport.getFirstEAAId())));

        DetailedReport detailedReport = reports.getDetailedReport();
        assertEquals(Indication.PASSED, detailedReport.getFinalIndication(simpleReport.getFirstEAAId()));

        XmlEAA xmlEAA = detailedReport.getXmlEAAById(detailedReport.getFirstEAAId());
        assertNotNull(xmlEAA);

        XmlValidationProcessEAA validationProcessEAA = xmlEAA.getValidationProcessEAA();
        assertNotNull(validationProcessEAA);
        assertEquals(Indication.PASSED, validationProcessEAA.getConclusion().getIndication());

        XmlBasicBuildingBlocks eaaBBB = detailedReport.getBasicBuildingBlockById(xmlEAA.getId());
        assertNotNull(eaaBBB);

        XmlFC xmlFC = eaaBBB.getFC();
        assertNotNull(xmlFC);
        assertEquals(Indication.PASSED, xmlFC.getConclusion().getIndication());

        XmlCV xmlCV = eaaBBB.getCV();
        assertNotNull(xmlCV);
        assertEquals(Indication.PASSED, xmlCV.getConclusion().getIndication());

        XmlAOV xmlAOV = eaaBBB.getAOV();
        assertNotNull(xmlAOV);
        assertEquals(Indication.PASSED, xmlAOV.getConclusion().getIndication());

        XmlSAV xmlSAV = eaaBBB.getSAV();
        assertNotNull(xmlSAV);
        assertEquals(Indication.PASSED, xmlSAV.getConclusion().getIndication());

        boolean claimsCheckFound = false;
        boolean supportedClaimsCheckFound = false;
        for (XmlConstraint xmlConstraint : xmlSAV.getConstraint()) {
            if (MessageTag.EAA_CLAIMS.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                claimsCheckFound = true;
            } else if (MessageTag.EAA_SUPPORTED_CLAIMS.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                supportedClaimsCheckFound = true;
            }
        }
        assertTrue(claimsCheckFound);
        assertTrue(supportedClaimsCheckFound);

        checkReports(reports);
    }

    @Test
    void claimsNotPresentTest() throws Exception {
        XmlDiagnosticData xmlDiagnosticData = DiagnosticDataFacade.newFacade().unmarshall(
                new File("src/test/resources/diag-data/eaa-validation/diag_data_eaa.xml"));
        assertNotNull(xmlDiagnosticData);

        EtsiValidationPolicy validationPolicy = loadDefaultPolicy();

        DiagnosticData diagnosticData = new DiagnosticData(xmlDiagnosticData);
        EAAWrapper eaaWrapper = diagnosticData.getEAAById(diagnosticData.getFirstEAAId());

        MultiValuesConstraint claims = new MultiValuesConstraint();
        claims.setLevel(Level.FAIL);
        claims.getId().add("given_name");
        claims.getId().add("family_name");
        claims.getId().add("middle_name");
        claims.getId().add("birthdate");
        validationPolicy.getEAAConstraints().setEAAClaims(claims);

        MultiValuesConstraint supportedClaims = new MultiValuesConstraint();
        supportedClaims.setLevel(Level.FAIL);
        supportedClaims.getId().addAll(eaaWrapper.getAllEAAPayloadClaimNames());
        validationPolicy.getEAAConstraints().setEAASupportedClaims(supportedClaims);

        EAAPresentationProcessExecutor executor = new EAAPresentationProcessExecutor();
        executor.setDiagnosticData(xmlDiagnosticData);
        executor.setCurrentTime(xmlDiagnosticData.getValidationDate());
        executor.setValidationPolicy(validationPolicy);

        Reports reports = executor.execute();

        SimpleReport simpleReport = reports.getSimpleReport();
        assertNotNull(simpleReport);

        assertEquals(Indication.INDETERMINATE, simpleReport.getIndication(simpleReport.getFirstEAAId()));
        assertEquals(SubIndication.EAA_CONSTRAINTS_FAILURE, simpleReport.getSubIndication(simpleReport.getFirstEAAId()));
        assertTrue(checkMessageValuePresence(simpleReport.getAdESValidationErrors(simpleReport.getFirstEAAId()), i18nProvider.getMessage(MessageTag.EAA_CLAIMS_ANS)));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationWarnings(simpleReport.getFirstEAAId())));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationInfo(simpleReport.getFirstEAAId())));

        DetailedReport detailedReport = reports.getDetailedReport();
        assertEquals(Indication.INDETERMINATE, detailedReport.getFinalIndication(simpleReport.getFirstEAAId()));
        assertEquals(SubIndication.EAA_CONSTRAINTS_FAILURE, detailedReport.getFinalSubIndication(simpleReport.getFirstEAAId()));

        XmlEAA xmlEAA = detailedReport.getXmlEAAById(detailedReport.getFirstEAAId());
        assertNotNull(xmlEAA);

        XmlValidationProcessEAA validationProcessEAA = xmlEAA.getValidationProcessEAA();
        assertNotNull(validationProcessEAA);
        assertEquals(Indication.INDETERMINATE, validationProcessEAA.getConclusion().getIndication());
        assertEquals(SubIndication.EAA_CONSTRAINTS_FAILURE, validationProcessEAA.getConclusion().getSubIndication());

        XmlBasicBuildingBlocks eaaBBB = detailedReport.getBasicBuildingBlockById(xmlEAA.getId());
        assertNotNull(eaaBBB);

        XmlFC xmlFC = eaaBBB.getFC();
        assertNotNull(xmlFC);
        assertEquals(Indication.PASSED, xmlFC.getConclusion().getIndication());

        XmlCV xmlCV = eaaBBB.getCV();
        assertNotNull(xmlCV);
        assertEquals(Indication.PASSED, xmlCV.getConclusion().getIndication());

        XmlAOV xmlAOV = eaaBBB.getAOV();
        assertNotNull(xmlAOV);
        assertEquals(Indication.PASSED, xmlAOV.getConclusion().getIndication());

        XmlSAV xmlSAV = eaaBBB.getSAV();
        assertNotNull(xmlSAV);
        assertEquals(Indication.INDETERMINATE, xmlSAV.getConclusion().getIndication());
        assertEquals(SubIndication.EAA_CONSTRAINTS_FAILURE, xmlSAV.getConclusion().getSubIndication());

        boolean claimsCheckFound = false;
        boolean supportedClaimsCheckFound = false;
        for (XmlConstraint xmlConstraint : xmlSAV.getConstraint()) {
            if (MessageTag.EAA_CLAIMS.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.NOT_OK, xmlConstraint.getStatus());
                assertEquals(MessageTag.EAA_CLAIMS_ANS.getId(), xmlConstraint.getError().getKey());
                assertEquals(i18nProvider.getMessage(MessageTag.EAA_CLAIMS_INFO, "middle_name"), xmlConstraint.getAdditionalInfo());
                claimsCheckFound = true;
            } else if (MessageTag.EAA_SUPPORTED_CLAIMS.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                supportedClaimsCheckFound = true;
            }
        }
        assertTrue(claimsCheckFound);
        assertFalse(supportedClaimsCheckFound);

        checkReports(reports);
    }

    @Test
    void claimsNotSupportedTest() throws Exception {
        XmlDiagnosticData xmlDiagnosticData = DiagnosticDataFacade.newFacade().unmarshall(
                new File("src/test/resources/diag-data/eaa-validation/diag_data_eaa.xml"));
        assertNotNull(xmlDiagnosticData);

        EtsiValidationPolicy validationPolicy = loadDefaultPolicy();

        DiagnosticData diagnosticData = new DiagnosticData(xmlDiagnosticData);
        EAAWrapper eaaWrapper = diagnosticData.getEAAById(diagnosticData.getFirstEAAId());

        MultiValuesConstraint claims = new MultiValuesConstraint();
        claims.setLevel(Level.FAIL);
        claims.getId().add("given_name");
        claims.getId().add("family_name");
        claims.getId().add("birthdate");
        validationPolicy.getEAAConstraints().setEAAClaims(claims);

        MultiValuesConstraint supportedClaims = new MultiValuesConstraint();
        supportedClaims.setLevel(Level.FAIL);
        supportedClaims.getId().addAll(eaaWrapper.getAllEAAPayloadClaimNames());
        supportedClaims.getId().remove("phone_number");
        supportedClaims.getId().remove("phone_number_verified");
        validationPolicy.getEAAConstraints().setEAASupportedClaims(supportedClaims);

        EAAPresentationProcessExecutor executor = new EAAPresentationProcessExecutor();
        executor.setDiagnosticData(xmlDiagnosticData);
        executor.setCurrentTime(xmlDiagnosticData.getValidationDate());
        executor.setValidationPolicy(validationPolicy);

        Reports reports = executor.execute();

        SimpleReport simpleReport = reports.getSimpleReport();
        assertNotNull(simpleReport);

        assertEquals(Indication.INDETERMINATE, simpleReport.getIndication(simpleReport.getFirstEAAId()));
        assertEquals(SubIndication.EAA_CONSTRAINTS_FAILURE, simpleReport.getSubIndication(simpleReport.getFirstEAAId()));
        assertTrue(checkMessageValuePresence(simpleReport.getAdESValidationErrors(simpleReport.getFirstEAAId()), i18nProvider.getMessage(MessageTag.EAA_SUPPORTED_CLAIMS_ANS)));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationWarnings(simpleReport.getFirstEAAId())));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationInfo(simpleReport.getFirstEAAId())));

        DetailedReport detailedReport = reports.getDetailedReport();
        assertEquals(Indication.INDETERMINATE, detailedReport.getFinalIndication(simpleReport.getFirstEAAId()));
        assertEquals(SubIndication.EAA_CONSTRAINTS_FAILURE, detailedReport.getFinalSubIndication(simpleReport.getFirstEAAId()));

        XmlEAA xmlEAA = detailedReport.getXmlEAAById(detailedReport.getFirstEAAId());
        assertNotNull(xmlEAA);

        XmlValidationProcessEAA validationProcessEAA = xmlEAA.getValidationProcessEAA();
        assertNotNull(validationProcessEAA);
        assertEquals(Indication.INDETERMINATE, validationProcessEAA.getConclusion().getIndication());
        assertEquals(SubIndication.EAA_CONSTRAINTS_FAILURE, validationProcessEAA.getConclusion().getSubIndication());

        XmlBasicBuildingBlocks eaaBBB = detailedReport.getBasicBuildingBlockById(xmlEAA.getId());
        assertNotNull(eaaBBB);

        XmlFC xmlFC = eaaBBB.getFC();
        assertNotNull(xmlFC);
        assertEquals(Indication.PASSED, xmlFC.getConclusion().getIndication());

        XmlCV xmlCV = eaaBBB.getCV();
        assertNotNull(xmlCV);
        assertEquals(Indication.PASSED, xmlCV.getConclusion().getIndication());

        XmlAOV xmlAOV = eaaBBB.getAOV();
        assertNotNull(xmlAOV);
        assertEquals(Indication.PASSED, xmlAOV.getConclusion().getIndication());

        XmlSAV xmlSAV = eaaBBB.getSAV();
        assertNotNull(xmlSAV);
        assertEquals(Indication.INDETERMINATE, xmlSAV.getConclusion().getIndication());
        assertEquals(SubIndication.EAA_CONSTRAINTS_FAILURE, xmlSAV.getConclusion().getSubIndication());

        boolean claimsCheckFound = false;
        boolean supportedClaimsCheckFound = false;
        for (XmlConstraint xmlConstraint : xmlSAV.getConstraint()) {
            if (MessageTag.EAA_CLAIMS.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                claimsCheckFound = true;
            } else if (MessageTag.EAA_SUPPORTED_CLAIMS.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.NOT_OK, xmlConstraint.getStatus());
                assertEquals(MessageTag.EAA_SUPPORTED_CLAIMS_ANS.getId(), xmlConstraint.getError().getKey());
                assertEquals(i18nProvider.getMessage(MessageTag.EAA_UNSUPPORTED_CLAIMS, "phone_number, phone_number_verified"), xmlConstraint.getAdditionalInfo());
                supportedClaimsCheckFound = true;
            }
        }
        assertTrue(claimsCheckFound);
        assertTrue(supportedClaimsCheckFound);

        checkReports(reports);
    }

    @Test
    void eaaCategoryTest() throws Exception {
        XmlDiagnosticData xmlDiagnosticData = DiagnosticDataFacade.newFacade().unmarshall(
                new File("src/test/resources/diag-data/eaa-validation/diag_data_eaa.xml"));
        assertNotNull(xmlDiagnosticData);

        EtsiValidationPolicy validationPolicy = loadDefaultPolicy();

        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.setLevel(Level.FAIL);
        constraint.getId().add("urn:etsi:esi:eaa:eu:qualified");
        validationPolicy.getEAAConstraints().setEAACategory(constraint);

        EAAPresentationProcessExecutor executor = new EAAPresentationProcessExecutor();
        executor.setDiagnosticData(xmlDiagnosticData);
        executor.setCurrentTime(xmlDiagnosticData.getValidationDate());
        executor.setValidationPolicy(validationPolicy);

        Reports reports = executor.execute();

        SimpleReport simpleReport = reports.getSimpleReport();
        assertNotNull(simpleReport);

        assertEquals(Indication.INDETERMINATE, simpleReport.getIndication(simpleReport.getFirstEAAId()));
        assertEquals(SubIndication.EAA_CONSTRAINTS_FAILURE, simpleReport.getSubIndication(simpleReport.getFirstEAAId()));
        assertTrue(checkMessageValuePresence(simpleReport.getAdESValidationErrors(simpleReport.getFirstEAAId()), i18nProvider.getMessage(MessageTag.EAA_CAT_ANS)));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationWarnings(simpleReport.getFirstEAAId())));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationInfo(simpleReport.getFirstEAAId())));

        DetailedReport detailedReport = reports.getDetailedReport();
        assertEquals(Indication.INDETERMINATE, detailedReport.getFinalIndication(simpleReport.getFirstEAAId()));
        assertEquals(SubIndication.EAA_CONSTRAINTS_FAILURE, detailedReport.getFinalSubIndication(simpleReport.getFirstEAAId()));

        XmlEAA xmlEAA = detailedReport.getXmlEAAById(detailedReport.getFirstEAAId());
        assertNotNull(xmlEAA);

        XmlValidationProcessEAA validationProcessEAA = xmlEAA.getValidationProcessEAA();
        assertNotNull(validationProcessEAA);
        assertEquals(Indication.INDETERMINATE, validationProcessEAA.getConclusion().getIndication());
        assertEquals(SubIndication.EAA_CONSTRAINTS_FAILURE, validationProcessEAA.getConclusion().getSubIndication());

        XmlBasicBuildingBlocks eaaBBB = detailedReport.getBasicBuildingBlockById(xmlEAA.getId());
        assertNotNull(eaaBBB);

        XmlFC xmlFC = eaaBBB.getFC();
        assertNotNull(xmlFC);
        assertEquals(Indication.PASSED, xmlFC.getConclusion().getIndication());

        XmlCV xmlCV = eaaBBB.getCV();
        assertNotNull(xmlCV);
        assertEquals(Indication.PASSED, xmlCV.getConclusion().getIndication());

        XmlAOV xmlAOV = eaaBBB.getAOV();
        assertNotNull(xmlAOV);
        assertEquals(Indication.PASSED, xmlAOV.getConclusion().getIndication());

        XmlSAV xmlSAV = eaaBBB.getSAV();
        assertNotNull(xmlSAV);
        assertEquals(Indication.INDETERMINATE, xmlSAV.getConclusion().getIndication());
        assertEquals(SubIndication.EAA_CONSTRAINTS_FAILURE, xmlSAV.getConclusion().getSubIndication());

        boolean checkFound = false;
        for (XmlConstraint xmlConstraint : xmlSAV.getConstraint()) {
            if (MessageTag.EAA_CAT.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.NOT_OK, xmlConstraint.getStatus());
                assertEquals(MessageTag.EAA_CAT_ANS.getId(), xmlConstraint.getError().getKey());
                checkFound = true;
            }
        }
        assertTrue(checkFound);

        checkReports(reports);
    }

    @Test
    void eaaSubjectTest() throws Exception {
        XmlDiagnosticData xmlDiagnosticData = DiagnosticDataFacade.newFacade().unmarshall(
                new File("src/test/resources/diag-data/eaa-validation/diag_data_eaa.xml"));
        assertNotNull(xmlDiagnosticData);

        EtsiValidationPolicy validationPolicy = loadDefaultPolicy();

        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.setLevel(Level.FAIL);
        constraint.getId().add("user_xx");
        validationPolicy.getEAAConstraints().setEAASubject(constraint);

        EAAPresentationProcessExecutor executor = new EAAPresentationProcessExecutor();
        executor.setDiagnosticData(xmlDiagnosticData);
        executor.setCurrentTime(xmlDiagnosticData.getValidationDate());
        executor.setValidationPolicy(validationPolicy);

        Reports reports = executor.execute();

        SimpleReport simpleReport = reports.getSimpleReport();
        assertNotNull(simpleReport);

        assertEquals(Indication.INDETERMINATE, simpleReport.getIndication(simpleReport.getFirstEAAId()));
        assertEquals(SubIndication.EAA_CONSTRAINTS_FAILURE, simpleReport.getSubIndication(simpleReport.getFirstEAAId()));
        assertTrue(checkMessageValuePresence(simpleReport.getAdESValidationErrors(simpleReport.getFirstEAAId()), i18nProvider.getMessage(MessageTag.EAA_SUB_ANS)));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationWarnings(simpleReport.getFirstEAAId())));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationInfo(simpleReport.getFirstEAAId())));

        DetailedReport detailedReport = reports.getDetailedReport();
        assertEquals(Indication.INDETERMINATE, detailedReport.getFinalIndication(simpleReport.getFirstEAAId()));
        assertEquals(SubIndication.EAA_CONSTRAINTS_FAILURE, detailedReport.getFinalSubIndication(simpleReport.getFirstEAAId()));

        XmlEAA xmlEAA = detailedReport.getXmlEAAById(detailedReport.getFirstEAAId());
        assertNotNull(xmlEAA);

        XmlValidationProcessEAA validationProcessEAA = xmlEAA.getValidationProcessEAA();
        assertNotNull(validationProcessEAA);
        assertEquals(Indication.INDETERMINATE, validationProcessEAA.getConclusion().getIndication());
        assertEquals(SubIndication.EAA_CONSTRAINTS_FAILURE, validationProcessEAA.getConclusion().getSubIndication());

        XmlBasicBuildingBlocks eaaBBB = detailedReport.getBasicBuildingBlockById(xmlEAA.getId());
        assertNotNull(eaaBBB);

        XmlFC xmlFC = eaaBBB.getFC();
        assertNotNull(xmlFC);
        assertEquals(Indication.PASSED, xmlFC.getConclusion().getIndication());

        XmlCV xmlCV = eaaBBB.getCV();
        assertNotNull(xmlCV);
        assertEquals(Indication.PASSED, xmlCV.getConclusion().getIndication());

        XmlAOV xmlAOV = eaaBBB.getAOV();
        assertNotNull(xmlAOV);
        assertEquals(Indication.PASSED, xmlAOV.getConclusion().getIndication());

        XmlSAV xmlSAV = eaaBBB.getSAV();
        assertNotNull(xmlSAV);
        assertEquals(Indication.INDETERMINATE, xmlSAV.getConclusion().getIndication());
        assertEquals(SubIndication.EAA_CONSTRAINTS_FAILURE, xmlSAV.getConclusion().getSubIndication());

        boolean checkFound = false;
        for (XmlConstraint xmlConstraint : xmlSAV.getConstraint()) {
            if (MessageTag.EAA_SUB.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.NOT_OK, xmlConstraint.getStatus());
                assertEquals(MessageTag.EAA_SUB_ANS.getId(), xmlConstraint.getError().getKey());
                checkFound = true;
            }
        }
        assertTrue(checkFound);

        checkReports(reports);
    }

    @Test
    void eaaSubjectPseudonymTest() throws Exception {
        XmlDiagnosticData xmlDiagnosticData = DiagnosticDataFacade.newFacade().unmarshall(
                new File("src/test/resources/diag-data/eaa-validation/diag_data_eaa.xml"));
        assertNotNull(xmlDiagnosticData);

        EtsiValidationPolicy validationPolicy = loadDefaultPolicy();

        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.setLevel(Level.FAIL);
        constraint.getId().add("pseudonym");
        validationPolicy.getEAAConstraints().setEAASubjectPseudonym(constraint);

        EAAPresentationProcessExecutor executor = new EAAPresentationProcessExecutor();
        executor.setDiagnosticData(xmlDiagnosticData);
        executor.setCurrentTime(xmlDiagnosticData.getValidationDate());
        executor.setValidationPolicy(validationPolicy);

        Reports reports = executor.execute();

        SimpleReport simpleReport = reports.getSimpleReport();
        assertNotNull(simpleReport);

        assertEquals(Indication.INDETERMINATE, simpleReport.getIndication(simpleReport.getFirstEAAId()));
        assertEquals(SubIndication.EAA_CONSTRAINTS_FAILURE, simpleReport.getSubIndication(simpleReport.getFirstEAAId()));
        assertTrue(checkMessageValuePresence(simpleReport.getAdESValidationErrors(simpleReport.getFirstEAAId()), i18nProvider.getMessage(MessageTag.EAA_SUB_PSE_ANS)));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationWarnings(simpleReport.getFirstEAAId())));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationInfo(simpleReport.getFirstEAAId())));

        DetailedReport detailedReport = reports.getDetailedReport();
        assertEquals(Indication.INDETERMINATE, detailedReport.getFinalIndication(simpleReport.getFirstEAAId()));
        assertEquals(SubIndication.EAA_CONSTRAINTS_FAILURE, detailedReport.getFinalSubIndication(simpleReport.getFirstEAAId()));

        XmlEAA xmlEAA = detailedReport.getXmlEAAById(detailedReport.getFirstEAAId());
        assertNotNull(xmlEAA);

        XmlValidationProcessEAA validationProcessEAA = xmlEAA.getValidationProcessEAA();
        assertNotNull(validationProcessEAA);
        assertEquals(Indication.INDETERMINATE, validationProcessEAA.getConclusion().getIndication());
        assertEquals(SubIndication.EAA_CONSTRAINTS_FAILURE, validationProcessEAA.getConclusion().getSubIndication());

        XmlBasicBuildingBlocks eaaBBB = detailedReport.getBasicBuildingBlockById(xmlEAA.getId());
        assertNotNull(eaaBBB);

        XmlFC xmlFC = eaaBBB.getFC();
        assertNotNull(xmlFC);
        assertEquals(Indication.PASSED, xmlFC.getConclusion().getIndication());

        XmlCV xmlCV = eaaBBB.getCV();
        assertNotNull(xmlCV);
        assertEquals(Indication.PASSED, xmlCV.getConclusion().getIndication());

        XmlAOV xmlAOV = eaaBBB.getAOV();
        assertNotNull(xmlAOV);
        assertEquals(Indication.PASSED, xmlAOV.getConclusion().getIndication());

        XmlSAV xmlSAV = eaaBBB.getSAV();
        assertNotNull(xmlSAV);
        assertEquals(Indication.INDETERMINATE, xmlSAV.getConclusion().getIndication());
        assertEquals(SubIndication.EAA_CONSTRAINTS_FAILURE, xmlSAV.getConclusion().getSubIndication());

        boolean checkFound = false;
        for (XmlConstraint xmlConstraint : xmlSAV.getConstraint()) {
            if (MessageTag.EAA_SUB_PSE.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.NOT_OK, xmlConstraint.getStatus());
                assertEquals(MessageTag.EAA_SUB_PSE_ANS.getId(), xmlConstraint.getError().getKey());
                checkFound = true;
            }
        }
        assertTrue(checkFound);

        checkReports(reports);
    }

    @Test
    void eaaIssuingCountryTest() throws Exception {
        XmlDiagnosticData xmlDiagnosticData = DiagnosticDataFacade.newFacade().unmarshall(
                new File("src/test/resources/diag-data/eaa-validation/diag_data_eaa.xml"));
        assertNotNull(xmlDiagnosticData);

        EtsiValidationPolicy validationPolicy = loadDefaultPolicy();

        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.setLevel(Level.FAIL);
        constraint.getId().add("LU");
        validationPolicy.getEAAConstraints().setEAAIssuingCountry(constraint);

        EAAPresentationProcessExecutor executor = new EAAPresentationProcessExecutor();
        executor.setDiagnosticData(xmlDiagnosticData);
        executor.setCurrentTime(xmlDiagnosticData.getValidationDate());
        executor.setValidationPolicy(validationPolicy);

        Reports reports = executor.execute();

        SimpleReport simpleReport = reports.getSimpleReport();
        assertNotNull(simpleReport);

        assertEquals(Indication.INDETERMINATE, simpleReport.getIndication(simpleReport.getFirstEAAId()));
        assertEquals(SubIndication.EAA_CONSTRAINTS_FAILURE, simpleReport.getSubIndication(simpleReport.getFirstEAAId()));
        assertTrue(checkMessageValuePresence(simpleReport.getAdESValidationErrors(simpleReport.getFirstEAAId()), i18nProvider.getMessage(MessageTag.EAA_ISS_COUN_ANS)));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationWarnings(simpleReport.getFirstEAAId())));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationInfo(simpleReport.getFirstEAAId())));

        DetailedReport detailedReport = reports.getDetailedReport();
        assertEquals(Indication.INDETERMINATE, detailedReport.getFinalIndication(simpleReport.getFirstEAAId()));
        assertEquals(SubIndication.EAA_CONSTRAINTS_FAILURE, detailedReport.getFinalSubIndication(simpleReport.getFirstEAAId()));

        XmlEAA xmlEAA = detailedReport.getXmlEAAById(detailedReport.getFirstEAAId());
        assertNotNull(xmlEAA);

        XmlValidationProcessEAA validationProcessEAA = xmlEAA.getValidationProcessEAA();
        assertNotNull(validationProcessEAA);
        assertEquals(Indication.INDETERMINATE, validationProcessEAA.getConclusion().getIndication());
        assertEquals(SubIndication.EAA_CONSTRAINTS_FAILURE, validationProcessEAA.getConclusion().getSubIndication());

        XmlBasicBuildingBlocks eaaBBB = detailedReport.getBasicBuildingBlockById(xmlEAA.getId());
        assertNotNull(eaaBBB);

        XmlFC xmlFC = eaaBBB.getFC();
        assertNotNull(xmlFC);
        assertEquals(Indication.PASSED, xmlFC.getConclusion().getIndication());

        XmlCV xmlCV = eaaBBB.getCV();
        assertNotNull(xmlCV);
        assertEquals(Indication.PASSED, xmlCV.getConclusion().getIndication());

        XmlAOV xmlAOV = eaaBBB.getAOV();
        assertNotNull(xmlAOV);
        assertEquals(Indication.PASSED, xmlAOV.getConclusion().getIndication());

        XmlSAV xmlSAV = eaaBBB.getSAV();
        assertNotNull(xmlSAV);
        assertEquals(Indication.INDETERMINATE, xmlSAV.getConclusion().getIndication());
        assertEquals(SubIndication.EAA_CONSTRAINTS_FAILURE, xmlSAV.getConclusion().getSubIndication());

        boolean checkFound = false;
        for (XmlConstraint xmlConstraint : xmlSAV.getConstraint()) {
            if (MessageTag.EAA_ISS_COUN.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.NOT_OK, xmlConstraint.getStatus());
                assertEquals(MessageTag.EAA_ISS_COUN_ANS.getId(), xmlConstraint.getError().getKey());
                checkFound = true;
            }
        }
        assertTrue(checkFound);

        checkReports(reports);
    }

    @Test
    void eaaIssuingAuthorityTest() throws Exception {
        XmlDiagnosticData xmlDiagnosticData = DiagnosticDataFacade.newFacade().unmarshall(
                new File("src/test/resources/diag-data/eaa-validation/diag_data_eaa.xml"));
        assertNotNull(xmlDiagnosticData);

        EtsiValidationPolicy validationPolicy = loadDefaultPolicy();

        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.setLevel(Level.FAIL);
        constraint.getId().add("Example Authority");
        validationPolicy.getEAAConstraints().setEAAIssuingAuthority(constraint);

        EAAPresentationProcessExecutor executor = new EAAPresentationProcessExecutor();
        executor.setDiagnosticData(xmlDiagnosticData);
        executor.setCurrentTime(xmlDiagnosticData.getValidationDate());
        executor.setValidationPolicy(validationPolicy);

        Reports reports = executor.execute();

        SimpleReport simpleReport = reports.getSimpleReport();
        assertNotNull(simpleReport);

        assertEquals(Indication.INDETERMINATE, simpleReport.getIndication(simpleReport.getFirstEAAId()));
        assertEquals(SubIndication.EAA_CONSTRAINTS_FAILURE, simpleReport.getSubIndication(simpleReport.getFirstEAAId()));
        assertTrue(checkMessageValuePresence(simpleReport.getAdESValidationErrors(simpleReport.getFirstEAAId()), i18nProvider.getMessage(MessageTag.EAA_ISS_AUTH_ANS)));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationWarnings(simpleReport.getFirstEAAId())));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationInfo(simpleReport.getFirstEAAId())));

        DetailedReport detailedReport = reports.getDetailedReport();
        assertEquals(Indication.INDETERMINATE, detailedReport.getFinalIndication(simpleReport.getFirstEAAId()));
        assertEquals(SubIndication.EAA_CONSTRAINTS_FAILURE, detailedReport.getFinalSubIndication(simpleReport.getFirstEAAId()));

        XmlEAA xmlEAA = detailedReport.getXmlEAAById(detailedReport.getFirstEAAId());
        assertNotNull(xmlEAA);

        XmlValidationProcessEAA validationProcessEAA = xmlEAA.getValidationProcessEAA();
        assertNotNull(validationProcessEAA);
        assertEquals(Indication.INDETERMINATE, validationProcessEAA.getConclusion().getIndication());
        assertEquals(SubIndication.EAA_CONSTRAINTS_FAILURE, validationProcessEAA.getConclusion().getSubIndication());

        XmlBasicBuildingBlocks eaaBBB = detailedReport.getBasicBuildingBlockById(xmlEAA.getId());
        assertNotNull(eaaBBB);

        XmlFC xmlFC = eaaBBB.getFC();
        assertNotNull(xmlFC);
        assertEquals(Indication.PASSED, xmlFC.getConclusion().getIndication());

        XmlCV xmlCV = eaaBBB.getCV();
        assertNotNull(xmlCV);
        assertEquals(Indication.PASSED, xmlCV.getConclusion().getIndication());

        XmlAOV xmlAOV = eaaBBB.getAOV();
        assertNotNull(xmlAOV);
        assertEquals(Indication.PASSED, xmlAOV.getConclusion().getIndication());

        XmlSAV xmlSAV = eaaBBB.getSAV();
        assertNotNull(xmlSAV);
        assertEquals(Indication.INDETERMINATE, xmlSAV.getConclusion().getIndication());
        assertEquals(SubIndication.EAA_CONSTRAINTS_FAILURE, xmlSAV.getConclusion().getSubIndication());

        boolean checkFound = false;
        for (XmlConstraint xmlConstraint : xmlSAV.getConstraint()) {
            if (MessageTag.EAA_ISS_AUTH.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.NOT_OK, xmlConstraint.getStatus());
                assertEquals(MessageTag.EAA_ISS_AUTH_ANS.getId(), xmlConstraint.getError().getKey());
                checkFound = true;
            }
        }
        assertTrue(checkFound);

        checkReports(reports);
    }

    @Test
    void eaaIssuingAuthorityRegistrationIdentifierTest() throws Exception {
        XmlDiagnosticData xmlDiagnosticData = DiagnosticDataFacade.newFacade().unmarshall(
                new File("src/test/resources/diag-data/eaa-validation/diag_data_eaa.xml"));
        assertNotNull(xmlDiagnosticData);

        EtsiValidationPolicy validationPolicy = loadDefaultPolicy();

        MultiValuesConstraint constraint = new MultiValuesConstraint();
        constraint.setLevel(Level.FAIL);
        constraint.getId().add("VAT-12345");
        validationPolicy.getEAAConstraints().setEAAIssuingAuthorityRegistrationIdentifier(constraint);

        EAAPresentationProcessExecutor executor = new EAAPresentationProcessExecutor();
        executor.setDiagnosticData(xmlDiagnosticData);
        executor.setCurrentTime(xmlDiagnosticData.getValidationDate());
        executor.setValidationPolicy(validationPolicy);

        Reports reports = executor.execute();

        SimpleReport simpleReport = reports.getSimpleReport();
        assertNotNull(simpleReport);

        assertEquals(Indication.INDETERMINATE, simpleReport.getIndication(simpleReport.getFirstEAAId()));
        assertEquals(SubIndication.EAA_CONSTRAINTS_FAILURE, simpleReport.getSubIndication(simpleReport.getFirstEAAId()));
        assertTrue(checkMessageValuePresence(simpleReport.getAdESValidationErrors(simpleReport.getFirstEAAId()), i18nProvider.getMessage(MessageTag.EAA_ISS_REG_ID_ANS)));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationWarnings(simpleReport.getFirstEAAId())));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationInfo(simpleReport.getFirstEAAId())));

        DetailedReport detailedReport = reports.getDetailedReport();
        assertEquals(Indication.INDETERMINATE, detailedReport.getFinalIndication(simpleReport.getFirstEAAId()));
        assertEquals(SubIndication.EAA_CONSTRAINTS_FAILURE, detailedReport.getFinalSubIndication(simpleReport.getFirstEAAId()));

        XmlEAA xmlEAA = detailedReport.getXmlEAAById(detailedReport.getFirstEAAId());
        assertNotNull(xmlEAA);

        XmlValidationProcessEAA validationProcessEAA = xmlEAA.getValidationProcessEAA();
        assertNotNull(validationProcessEAA);
        assertEquals(Indication.INDETERMINATE, validationProcessEAA.getConclusion().getIndication());
        assertEquals(SubIndication.EAA_CONSTRAINTS_FAILURE, validationProcessEAA.getConclusion().getSubIndication());

        XmlBasicBuildingBlocks eaaBBB = detailedReport.getBasicBuildingBlockById(xmlEAA.getId());
        assertNotNull(eaaBBB);

        XmlFC xmlFC = eaaBBB.getFC();
        assertNotNull(xmlFC);
        assertEquals(Indication.PASSED, xmlFC.getConclusion().getIndication());

        XmlCV xmlCV = eaaBBB.getCV();
        assertNotNull(xmlCV);
        assertEquals(Indication.PASSED, xmlCV.getConclusion().getIndication());

        XmlAOV xmlAOV = eaaBBB.getAOV();
        assertNotNull(xmlAOV);
        assertEquals(Indication.PASSED, xmlAOV.getConclusion().getIndication());

        XmlSAV xmlSAV = eaaBBB.getSAV();
        assertNotNull(xmlSAV);
        assertEquals(Indication.INDETERMINATE, xmlSAV.getConclusion().getIndication());
        assertEquals(SubIndication.EAA_CONSTRAINTS_FAILURE, xmlSAV.getConclusion().getSubIndication());

        boolean checkFound = false;
        for (XmlConstraint xmlConstraint : xmlSAV.getConstraint()) {
            if (MessageTag.EAA_ISS_REG_ID.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.NOT_OK, xmlConstraint.getStatus());
                assertEquals(MessageTag.EAA_ISS_REG_ID_ANS.getId(), xmlConstraint.getError().getKey());
                checkFound = true;
            }
        }
        assertTrue(checkFound);

        checkReports(reports);
    }

    @Test
    void eaaStatusPresentTest() throws Exception {
        XmlDiagnosticData xmlDiagnosticData = DiagnosticDataFacade.newFacade().unmarshall(
                new File("src/test/resources/diag-data/eaa-validation/diag_data_eaa.xml"));
        assertNotNull(xmlDiagnosticData);

        XmlEAAPayload eaaPayload = xmlDiagnosticData.getEAAs().get(0).getEAAPayload();
        eaaPayload.setStatus(null);

        EtsiValidationPolicy validationPolicy = loadDefaultPolicy();

        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);
        validationPolicy.getEAAConstraints().setEAAStatusPresent(constraint);

        LevelConstraint infoConstraint = new LevelConstraint();
        infoConstraint.setLevel(Level.INFORM);
        validationPolicy.getEAAConstraints().setEAAShortLived(infoConstraint);
        validationPolicy.getEAAConstraints().setEAAOneTimeUse(infoConstraint);

        EAAPresentationProcessExecutor executor = new EAAPresentationProcessExecutor();
        executor.setDiagnosticData(xmlDiagnosticData);
        executor.setCurrentTime(xmlDiagnosticData.getValidationDate());
        executor.setValidationPolicy(validationPolicy);

        Reports reports = executor.execute();

        SimpleReport simpleReport = reports.getSimpleReport();
        assertNotNull(simpleReport);

        assertEquals(Indication.INDETERMINATE, simpleReport.getIndication(simpleReport.getFirstEAAId()));
        assertEquals(SubIndication.EAA_CONSTRAINTS_FAILURE, simpleReport.getSubIndication(simpleReport.getFirstEAAId()));
        assertTrue(checkMessageValuePresence(simpleReport.getAdESValidationErrors(simpleReport.getFirstEAAId()), i18nProvider.getMessage(MessageTag.EAA_STATUS_PR_ANS)));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationWarnings(simpleReport.getFirstEAAId())));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationInfo(simpleReport.getFirstEAAId())));

        DetailedReport detailedReport = reports.getDetailedReport();
        assertEquals(Indication.INDETERMINATE, detailedReport.getFinalIndication(simpleReport.getFirstEAAId()));
        assertEquals(SubIndication.EAA_CONSTRAINTS_FAILURE, detailedReport.getFinalSubIndication(simpleReport.getFirstEAAId()));

        XmlEAA xmlEAA = detailedReport.getXmlEAAById(detailedReport.getFirstEAAId());
        assertNotNull(xmlEAA);

        XmlValidationProcessEAA validationProcessEAA = xmlEAA.getValidationProcessEAA();
        assertNotNull(validationProcessEAA);
        assertEquals(Indication.INDETERMINATE, validationProcessEAA.getConclusion().getIndication());
        assertEquals(SubIndication.EAA_CONSTRAINTS_FAILURE, validationProcessEAA.getConclusion().getSubIndication());

        XmlBasicBuildingBlocks eaaBBB = detailedReport.getBasicBuildingBlockById(xmlEAA.getId());
        assertNotNull(eaaBBB);

        XmlFC xmlFC = eaaBBB.getFC();
        assertNotNull(xmlFC);
        assertEquals(Indication.PASSED, xmlFC.getConclusion().getIndication());

        XmlCV xmlCV = eaaBBB.getCV();
        assertNotNull(xmlCV);
        assertEquals(Indication.PASSED, xmlCV.getConclusion().getIndication());

        XmlAOV xmlAOV = eaaBBB.getAOV();
        assertNotNull(xmlAOV);
        assertEquals(Indication.PASSED, xmlAOV.getConclusion().getIndication());

        XmlSAV xmlSAV = eaaBBB.getSAV();
        assertNotNull(xmlSAV);
        assertEquals(Indication.INDETERMINATE, xmlSAV.getConclusion().getIndication());
        assertEquals(SubIndication.EAA_CONSTRAINTS_FAILURE, xmlSAV.getConclusion().getSubIndication());

        boolean shortLivedCheckFound = false;
        boolean oneTimeCheckFound = false;
        boolean statusCheckFound = false;
        for (XmlConstraint xmlConstraint : xmlSAV.getConstraint()) {
            if (MessageTag.EAA_SH_LVD.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                shortLivedCheckFound = true;
            } else if (MessageTag.EAA_OTU.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                oneTimeCheckFound = true;
            } else if (MessageTag.EAA_STATUS_PR.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.NOT_OK, xmlConstraint.getStatus());
                assertEquals(MessageTag.EAA_STATUS_PR_ANS.getId(), xmlConstraint.getError().getKey());
                statusCheckFound = true;
            }
        }
        assertFalse(shortLivedCheckFound);
        assertFalse(oneTimeCheckFound);
        assertTrue(statusCheckFound);

        checkReports(reports);
    }

    @Test
    void eaaShortLivedTest() throws Exception {
        XmlDiagnosticData xmlDiagnosticData = DiagnosticDataFacade.newFacade().unmarshall(
                new File("src/test/resources/diag-data/eaa-validation/diag_data_eaa.xml"));
        assertNotNull(xmlDiagnosticData);

        XmlEAAPayload eaaPayload = xmlDiagnosticData.getEAAs().get(0).getEAAPayload();
        eaaPayload.setStatus(null);
        XmlClaim xmlClaim = new XmlClaim();
        eaaPayload.setShortLived(xmlClaim);

        EtsiValidationPolicy validationPolicy = loadDefaultPolicy();

        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);
        validationPolicy.getEAAConstraints().setEAAStatusPresent(constraint);

        LevelConstraint infoConstraint = new LevelConstraint();
        infoConstraint.setLevel(Level.INFORM);
        validationPolicy.getEAAConstraints().setEAAShortLived(infoConstraint);
        validationPolicy.getEAAConstraints().setEAAOneTimeUse(infoConstraint);

        EAAPresentationProcessExecutor executor = new EAAPresentationProcessExecutor();
        executor.setDiagnosticData(xmlDiagnosticData);
        executor.setCurrentTime(xmlDiagnosticData.getValidationDate());
        executor.setValidationPolicy(validationPolicy);

        Reports reports = executor.execute();

        SimpleReport simpleReport = reports.getSimpleReport();
        assertNotNull(simpleReport);

        assertEquals(Indication.PASSED, simpleReport.getIndication(simpleReport.getFirstEAAId()));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationErrors(simpleReport.getFirstEAAId())));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationWarnings(simpleReport.getFirstEAAId())));
        assertTrue(checkMessageValuePresence(simpleReport.getAdESValidationInfo(simpleReport.getFirstEAAId()), i18nProvider.getMessage(MessageTag.EAA_SH_LVD_ANS)));

        DetailedReport detailedReport = reports.getDetailedReport();
        assertEquals(Indication.PASSED, detailedReport.getFinalIndication(simpleReport.getFirstEAAId()));

        XmlEAA xmlEAA = detailedReport.getXmlEAAById(detailedReport.getFirstEAAId());
        assertNotNull(xmlEAA);

        XmlValidationProcessEAA validationProcessEAA = xmlEAA.getValidationProcessEAA();
        assertNotNull(validationProcessEAA);
        assertEquals(Indication.PASSED, validationProcessEAA.getConclusion().getIndication());

        XmlBasicBuildingBlocks eaaBBB = detailedReport.getBasicBuildingBlockById(xmlEAA.getId());
        assertNotNull(eaaBBB);

        XmlFC xmlFC = eaaBBB.getFC();
        assertNotNull(xmlFC);
        assertEquals(Indication.PASSED, xmlFC.getConclusion().getIndication());

        XmlCV xmlCV = eaaBBB.getCV();
        assertNotNull(xmlCV);
        assertEquals(Indication.PASSED, xmlCV.getConclusion().getIndication());

        XmlAOV xmlAOV = eaaBBB.getAOV();
        assertNotNull(xmlAOV);
        assertEquals(Indication.PASSED, xmlAOV.getConclusion().getIndication());

        XmlSAV xmlSAV = eaaBBB.getSAV();
        assertNotNull(xmlSAV);
        assertEquals(Indication.PASSED, xmlSAV.getConclusion().getIndication());

        boolean shortLivedCheckFound = false;
        boolean oneTimeCheckFound = false;
        boolean statusCheckFound = false;
        for (XmlConstraint xmlConstraint : xmlSAV.getConstraint()) {
            if (MessageTag.EAA_SH_LVD.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.INFORMATION, xmlConstraint.getStatus());
                assertEquals(MessageTag.EAA_SH_LVD_ANS.getId(), xmlConstraint.getInfo().getKey());
                shortLivedCheckFound = true;
            } else if (MessageTag.EAA_OTU.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                oneTimeCheckFound = true;
            } else if (MessageTag.EAA_STATUS_PR.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                statusCheckFound = true;
            }
        }
        assertTrue(shortLivedCheckFound);
        assertFalse(oneTimeCheckFound);
        assertFalse(statusCheckFound);

        checkReports(reports);
    }

    @Test
    void eaaOneTimeTest() throws Exception {
        XmlDiagnosticData xmlDiagnosticData = DiagnosticDataFacade.newFacade().unmarshall(
                new File("src/test/resources/diag-data/eaa-validation/diag_data_eaa.xml"));
        assertNotNull(xmlDiagnosticData);

        XmlEAAPayload eaaPayload = xmlDiagnosticData.getEAAs().get(0).getEAAPayload();
        eaaPayload.setStatus(null);
        XmlClaim xmlClaim = new XmlClaim();
        eaaPayload.setOneTimeUse(xmlClaim);

        EtsiValidationPolicy validationPolicy = loadDefaultPolicy();

        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.WARN);
        validationPolicy.getEAAConstraints().setEAAStatusPresent(constraint);

        LevelConstraint infoConstraint = new LevelConstraint();
        infoConstraint.setLevel(Level.INFORM);
        validationPolicy.getEAAConstraints().setEAAShortLived(infoConstraint);
        validationPolicy.getEAAConstraints().setEAAOneTimeUse(infoConstraint);

        EAAPresentationProcessExecutor executor = new EAAPresentationProcessExecutor();
        executor.setDiagnosticData(xmlDiagnosticData);
        executor.setCurrentTime(xmlDiagnosticData.getValidationDate());
        executor.setValidationPolicy(validationPolicy);

        Reports reports = executor.execute();

        SimpleReport simpleReport = reports.getSimpleReport();
        assertNotNull(simpleReport);

        assertEquals(Indication.PASSED, simpleReport.getIndication(simpleReport.getFirstEAAId()));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationErrors(simpleReport.getFirstEAAId())));
        assertTrue(checkMessageValuePresence(simpleReport.getAdESValidationWarnings(simpleReport.getFirstEAAId()), i18nProvider.getMessage(MessageTag.EAA_STATUS_PR_ANS)));
        assertTrue(checkMessageValuePresence(simpleReport.getAdESValidationInfo(simpleReport.getFirstEAAId()), i18nProvider.getMessage(MessageTag.EAA_OTU_ANS)));

        DetailedReport detailedReport = reports.getDetailedReport();
        assertEquals(Indication.PASSED, detailedReport.getFinalIndication(simpleReport.getFirstEAAId()));

        XmlEAA xmlEAA = detailedReport.getXmlEAAById(detailedReport.getFirstEAAId());
        assertNotNull(xmlEAA);

        XmlValidationProcessEAA validationProcessEAA = xmlEAA.getValidationProcessEAA();
        assertNotNull(validationProcessEAA);
        assertEquals(Indication.PASSED, validationProcessEAA.getConclusion().getIndication());

        XmlBasicBuildingBlocks eaaBBB = detailedReport.getBasicBuildingBlockById(xmlEAA.getId());
        assertNotNull(eaaBBB);

        XmlFC xmlFC = eaaBBB.getFC();
        assertNotNull(xmlFC);
        assertEquals(Indication.PASSED, xmlFC.getConclusion().getIndication());

        XmlCV xmlCV = eaaBBB.getCV();
        assertNotNull(xmlCV);
        assertEquals(Indication.PASSED, xmlCV.getConclusion().getIndication());

        XmlAOV xmlAOV = eaaBBB.getAOV();
        assertNotNull(xmlAOV);
        assertEquals(Indication.PASSED, xmlAOV.getConclusion().getIndication());

        XmlSAV xmlSAV = eaaBBB.getSAV();
        assertNotNull(xmlSAV);
        assertEquals(Indication.PASSED, xmlSAV.getConclusion().getIndication());

        boolean shortLivedCheckFound = false;
        boolean oneTimeCheckFound = false;
        boolean statusCheckFound = false;
        for (XmlConstraint xmlConstraint : xmlSAV.getConstraint()) {
            if (MessageTag.EAA_SH_LVD.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                shortLivedCheckFound = true;
            } else if (MessageTag.EAA_OTU.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.INFORMATION, xmlConstraint.getStatus());
                assertEquals(MessageTag.EAA_OTU_ANS.getId(), xmlConstraint.getInfo().getKey());
                oneTimeCheckFound = true;
            } else if (MessageTag.EAA_STATUS_PR.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.WARNING, xmlConstraint.getStatus());
                assertEquals(MessageTag.EAA_STATUS_PR_ANS.getId(), xmlConstraint.getWarning().getKey());
                statusCheckFound = true;
            }
        }
        assertFalse(shortLivedCheckFound);
        assertTrue(oneTimeCheckFound);
        assertTrue(statusCheckFound);

        checkReports(reports);
    }

    @Test
    void eaaNoPseudonymUsePresentTest() throws Exception {
        XmlDiagnosticData xmlDiagnosticData = DiagnosticDataFacade.newFacade().unmarshall(
                new File("src/test/resources/diag-data/eaa-validation/diag_data_eaa.xml"));
        assertNotNull(xmlDiagnosticData);

        EtsiValidationPolicy validationPolicy = loadDefaultPolicy();

        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.INFORM);
        validationPolicy.getEAAConstraints().setEAAUsePseudonym(constraint);

        EAAPresentationProcessExecutor executor = new EAAPresentationProcessExecutor();
        executor.setDiagnosticData(xmlDiagnosticData);
        executor.setCurrentTime(xmlDiagnosticData.getValidationDate());
        executor.setValidationPolicy(validationPolicy);

        Reports reports = executor.execute();

        SimpleReport simpleReport = reports.getSimpleReport();
        assertNotNull(simpleReport);

        assertEquals(Indication.PASSED, simpleReport.getIndication(simpleReport.getFirstEAAId()));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationErrors(simpleReport.getFirstEAAId())));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationWarnings(simpleReport.getFirstEAAId())));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationInfo(simpleReport.getFirstEAAId())));

        DetailedReport detailedReport = reports.getDetailedReport();
        assertEquals(Indication.PASSED, detailedReport.getFinalIndication(simpleReport.getFirstEAAId()));

        XmlEAA xmlEAA = detailedReport.getXmlEAAById(detailedReport.getFirstEAAId());
        assertNotNull(xmlEAA);

        XmlValidationProcessEAA validationProcessEAA = xmlEAA.getValidationProcessEAA();
        assertNotNull(validationProcessEAA);
        assertEquals(Indication.PASSED, validationProcessEAA.getConclusion().getIndication());

        XmlBasicBuildingBlocks eaaBBB = detailedReport.getBasicBuildingBlockById(xmlEAA.getId());
        assertNotNull(eaaBBB);

        XmlFC xmlFC = eaaBBB.getFC();
        assertNotNull(xmlFC);
        assertEquals(Indication.PASSED, xmlFC.getConclusion().getIndication());

        XmlCV xmlCV = eaaBBB.getCV();
        assertNotNull(xmlCV);
        assertEquals(Indication.PASSED, xmlCV.getConclusion().getIndication());

        XmlAOV xmlAOV = eaaBBB.getAOV();
        assertNotNull(xmlAOV);
        assertEquals(Indication.PASSED, xmlAOV.getConclusion().getIndication());

        XmlSAV xmlSAV = eaaBBB.getSAV();
        assertNotNull(xmlSAV);
        assertEquals(Indication.PASSED, xmlSAV.getConclusion().getIndication());

        boolean checkFound = false;
        for (XmlConstraint xmlConstraint : xmlSAV.getConstraint()) {
            if (MessageTag.EAA_PSEUDO_USED.getId().equals(xmlConstraint.getName().getKey())) {
                checkFound = true;
            }
        }
        assertFalse(checkFound);

        checkReports(reports);
    }

    @Test
    void eaaPseudonymUsePresentTest() throws Exception {
        XmlDiagnosticData xmlDiagnosticData = DiagnosticDataFacade.newFacade().unmarshall(
                new File("src/test/resources/diag-data/eaa-validation/diag_data_eaa.xml"));
        assertNotNull(xmlDiagnosticData);

        XmlEAAPayload eaaPayload = xmlDiagnosticData.getEAAs().get(0).getEAAPayload();
        XmlClaim xmlClaim = new XmlClaim();
        xmlClaim.setText("pseudonym");
        eaaPayload.setPseudonym(xmlClaim);

        EtsiValidationPolicy validationPolicy = loadDefaultPolicy();

        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.INFORM);
        validationPolicy.getEAAConstraints().setEAAUsePseudonym(constraint);

        EAAPresentationProcessExecutor executor = new EAAPresentationProcessExecutor();
        executor.setDiagnosticData(xmlDiagnosticData);
        executor.setCurrentTime(xmlDiagnosticData.getValidationDate());
        executor.setValidationPolicy(validationPolicy);

        Reports reports = executor.execute();

        SimpleReport simpleReport = reports.getSimpleReport();
        assertNotNull(simpleReport);

        assertEquals(Indication.PASSED, simpleReport.getIndication(simpleReport.getFirstEAAId()));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationErrors(simpleReport.getFirstEAAId())));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationWarnings(simpleReport.getFirstEAAId())));
        assertTrue(checkMessageValuePresence(simpleReport.getAdESValidationInfo(simpleReport.getFirstEAAId()), i18nProvider.getMessage(MessageTag.EAA_PSEUDO_USED_ANS)));

        DetailedReport detailedReport = reports.getDetailedReport();
        assertEquals(Indication.PASSED, detailedReport.getFinalIndication(simpleReport.getFirstEAAId()));

        XmlEAA xmlEAA = detailedReport.getXmlEAAById(detailedReport.getFirstEAAId());
        assertNotNull(xmlEAA);

        XmlValidationProcessEAA validationProcessEAA = xmlEAA.getValidationProcessEAA();
        assertNotNull(validationProcessEAA);
        assertEquals(Indication.PASSED, validationProcessEAA.getConclusion().getIndication());

        XmlBasicBuildingBlocks eaaBBB = detailedReport.getBasicBuildingBlockById(xmlEAA.getId());
        assertNotNull(eaaBBB);

        XmlFC xmlFC = eaaBBB.getFC();
        assertNotNull(xmlFC);
        assertEquals(Indication.PASSED, xmlFC.getConclusion().getIndication());

        XmlCV xmlCV = eaaBBB.getCV();
        assertNotNull(xmlCV);
        assertEquals(Indication.PASSED, xmlCV.getConclusion().getIndication());

        XmlAOV xmlAOV = eaaBBB.getAOV();
        assertNotNull(xmlAOV);
        assertEquals(Indication.PASSED, xmlAOV.getConclusion().getIndication());

        XmlSAV xmlSAV = eaaBBB.getSAV();
        assertNotNull(xmlSAV);
        assertEquals(Indication.PASSED, xmlSAV.getConclusion().getIndication());

        boolean checkFound = false;
        for (XmlConstraint xmlConstraint : xmlSAV.getConstraint()) {
            if (MessageTag.EAA_PSEUDO_USED.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.INFORMATION, xmlConstraint.getStatus());
                assertEquals(MessageTag.EAA_PSEUDO_USED_ANS.getId(), xmlConstraint.getInfo().getKey());
                assertEquals(i18nProvider.getMessage(MessageTag.PSEUDO, "pseudonym"), xmlConstraint.getAdditionalInfo());
                checkFound = true;
            }
        }
        assertTrue(checkFound);

        checkReports(reports);
    }

    @Override
    protected EtsiValidationPolicy loadDefaultPolicy() throws Exception {
        return (EtsiValidationPolicy) ValidationPolicyLoader.fromValidationPolicy(EAA_POLICY_LOCATION).create();
    }

}
