package eu.europa.esig.dss.validation.executor;

import eu.europa.esig.dss.detailedreport.DetailedReport;
import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraint;
import eu.europa.esig.dss.detailedreport.jaxb.XmlEAAPresentation;
import eu.europa.esig.dss.detailedreport.jaxb.XmlStatus;
import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationProcessEAAPresentation;
import eu.europa.esig.dss.diagnostic.DiagnosticDataFacade;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDiagnosticData;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDigestMatcher;
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
import eu.europa.esig.dss.validation.reports.Reports;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

        assertEquals(Indication.PASSED, simpleReport.getIndication(simpleReport.getFirstEAAPresentationId()));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationErrors(simpleReport.getFirstEAAPresentationId())));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationWarnings(simpleReport.getFirstEAAPresentationId())));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationInfo(simpleReport.getFirstEAAPresentationId())));

        DetailedReport detailedReport = reports.getDetailedReport();
        assertEquals(Indication.PASSED, detailedReport.getFinalIndication(simpleReport.getFirstEAAPresentationId()));

        XmlEAAPresentation xmlEAAPresentation = detailedReport.getXmlEAAPresentationById(detailedReport.getFirstEAAPresentationId());
        assertNotNull(xmlEAAPresentation);

        XmlValidationProcessEAAPresentation validationProcessEAAPresentation = xmlEAAPresentation.getValidationProcessEAAPresentation();
        assertNotNull(validationProcessEAAPresentation);
        assertEquals(Indication.PASSED, validationProcessEAAPresentation.getConclusion().getIndication());

        boolean sigPresentCheckFound = false;
        boolean sigValidationConclusiveCheckFound = false;
        boolean disclosuresPresentCheckFound = false;
        int disclosureFoundCounter = 0;
        int disclosureIntactCounter = 0;
        boolean kbSigPresentCheckFound = false;
        boolean kbSigValidationConclusiveCheckFound = false;
        for (XmlConstraint xmlConstraint : validationProcessEAAPresentation.getConstraint()) {
            assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
            if (MessageTag.EAA_SIG_PRESENT.getId().equals(xmlConstraint.getName().getKey())) {
                sigPresentCheckFound = true;
            } else if (MessageTag.ADEST_IBSVPSC.getId().equals(xmlConstraint.getName().getKey())) {
                sigValidationConclusiveCheckFound = true;
            } else if (MessageTag.EAA_DPEAAP.getId().equals(xmlConstraint.getName().getKey())) {
                disclosuresPresentCheckFound = true;
            } else if (MessageTag.BBB_CV_EAA_SDCBF.getId().equals(xmlConstraint.getName().getKey())) {
                ++disclosureFoundCounter;
            } else if (MessageTag.BBB_CV_EAA_SDCBI.getId().equals(xmlConstraint.getName().getKey())) {
                ++disclosureIntactCounter;
            } else if (MessageTag.EAA_KBSP.getId().equals(xmlConstraint.getName().getKey())) {
                kbSigPresentCheckFound = true;
            } else if (MessageTag.EAA_KBRC.getId().equals(xmlConstraint.getName().getKey())) {
                kbSigValidationConclusiveCheckFound = true;
            }
        }
        assertTrue(sigPresentCheckFound);
        assertTrue(sigValidationConclusiveCheckFound);
        assertTrue(disclosuresPresentCheckFound);
        assertEquals(10, disclosureFoundCounter);
        assertEquals(10, disclosureIntactCounter);
        assertTrue(kbSigPresentCheckFound);
        assertTrue(kbSigValidationConclusiveCheckFound);

        checkReports(reports);
    }

    @Test
    void validWithOrphanDisclosuresTest() throws Exception {
        XmlDiagnosticData diagnosticData = DiagnosticDataFacade.newFacade().unmarshall(
                new File("src/test/resources/diag-data/eaa-validation/diag_data_eaa.xml"));
        assertNotNull(diagnosticData);

        List<XmlDigestMatcher> digestMatchers = diagnosticData.getEAAPresentations().get(0).getDigestMatchers();

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

        assertEquals(Indication.PASSED, simpleReport.getIndication(simpleReport.getFirstEAAPresentationId()));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationErrors(simpleReport.getFirstEAAPresentationId())));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationWarnings(simpleReport.getFirstEAAPresentationId())));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationInfo(simpleReport.getFirstEAAPresentationId())));

        DetailedReport detailedReport = reports.getDetailedReport();
        assertEquals(Indication.PASSED, detailedReport.getFinalIndication(simpleReport.getFirstEAAPresentationId()));

        XmlEAAPresentation xmlEAAPresentation = detailedReport.getXmlEAAPresentationById(detailedReport.getFirstEAAPresentationId());
        assertNotNull(xmlEAAPresentation);

        XmlValidationProcessEAAPresentation validationProcessEAAPresentation = xmlEAAPresentation.getValidationProcessEAAPresentation();
        assertNotNull(validationProcessEAAPresentation);
        assertEquals(Indication.PASSED, validationProcessEAAPresentation.getConclusion().getIndication());

        boolean sigPresentCheckFound = false;
        boolean sigValidationConclusiveCheckFound = false;
        boolean disclosuresPresentCheckFound = false;
        int disclosureFoundCounter = 0;
        int disclosureIntactCounter = 0;
        boolean kbSigPresentCheckFound = false;
        boolean kbSigValidationConclusiveCheckFound = false;
        for (XmlConstraint xmlConstraint : validationProcessEAAPresentation.getConstraint()) {
            assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
            if (MessageTag.EAA_SIG_PRESENT.getId().equals(xmlConstraint.getName().getKey())) {
                sigPresentCheckFound = true;
            } else if (MessageTag.ADEST_IBSVPSC.getId().equals(xmlConstraint.getName().getKey())) {
                sigValidationConclusiveCheckFound = true;
            } else if (MessageTag.EAA_DPEAAP.getId().equals(xmlConstraint.getName().getKey())) {
                disclosuresPresentCheckFound = true;
            } else if (MessageTag.BBB_CV_EAA_SDCBF.getId().equals(xmlConstraint.getName().getKey())) {
                ++disclosureFoundCounter;
            } else if (MessageTag.BBB_CV_EAA_SDCBI.getId().equals(xmlConstraint.getName().getKey())) {
                ++disclosureIntactCounter;
            } else if (MessageTag.EAA_KBSP.getId().equals(xmlConstraint.getName().getKey())) {
                kbSigPresentCheckFound = true;
            } else if (MessageTag.EAA_KBRC.getId().equals(xmlConstraint.getName().getKey())) {
                kbSigValidationConclusiveCheckFound = true;
            }
        }
        assertTrue(sigPresentCheckFound);
        assertTrue(sigValidationConclusiveCheckFound);
        assertTrue(disclosuresPresentCheckFound);
        assertEquals(10, disclosureFoundCounter);
        assertEquals(10, disclosureIntactCounter);
        assertTrue(kbSigPresentCheckFound);
        assertTrue(kbSigValidationConclusiveCheckFound);

        checkReports(reports);
    }

    @Test
    void disclosureNotIntactTest() throws Exception {
        XmlDiagnosticData diagnosticData = DiagnosticDataFacade.newFacade().unmarshall(
                new File("src/test/resources/diag-data/eaa-validation/diag_data_eaa.xml"));
        assertNotNull(diagnosticData);

        List<XmlDigestMatcher> digestMatchers = diagnosticData.getEAAPresentations().get(0).getDigestMatchers();
        digestMatchers.get(0).setDataIntact(false);

        EAAPresentationProcessExecutor executor = new EAAPresentationProcessExecutor();
        executor.setDiagnosticData(diagnosticData);
        executor.setCurrentTime(diagnosticData.getValidationDate());
        executor.setValidationPolicy(loadDefaultPolicy());

        Reports reports = executor.execute();

        SimpleReport simpleReport = reports.getSimpleReport();
        assertNotNull(simpleReport);

        assertEquals(Indication.FAILED, simpleReport.getIndication(simpleReport.getFirstEAAPresentationId()));
        assertEquals(SubIndication.HASH_FAILURE, simpleReport.getSubIndication(simpleReport.getFirstEAAPresentationId()));
        assertFalse(Utils.isCollectionEmpty(simpleReport.getAdESValidationErrors(simpleReport.getFirstEAAPresentationId())));
        assertTrue(checkMessageValuePresence(simpleReport.getAdESValidationErrors(simpleReport.getFirstEAAPresentationId()), i18nProvider.getMessage(MessageTag.BBB_CV_EAA_SDCBI_ANS)));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationWarnings(simpleReport.getFirstEAAPresentationId())));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationInfo(simpleReport.getFirstEAAPresentationId())));

        DetailedReport detailedReport = reports.getDetailedReport();
        assertEquals(Indication.FAILED, detailedReport.getFinalIndication(simpleReport.getFirstEAAPresentationId()));
        assertEquals(SubIndication.HASH_FAILURE, detailedReport.getFinalSubIndication(simpleReport.getFirstEAAPresentationId()));

        XmlEAAPresentation xmlEAAPresentation = detailedReport.getXmlEAAPresentationById(detailedReport.getFirstEAAPresentationId());
        assertNotNull(xmlEAAPresentation);

        XmlValidationProcessEAAPresentation validationProcessEAAPresentation = xmlEAAPresentation.getValidationProcessEAAPresentation();
        assertNotNull(validationProcessEAAPresentation);
        assertEquals(Indication.FAILED, validationProcessEAAPresentation.getConclusion().getIndication());
        assertEquals(SubIndication.HASH_FAILURE, validationProcessEAAPresentation.getConclusion().getSubIndication());

        boolean sigPresentCheckFound = false;
        boolean sigValidationConclusiveCheckFound = false;
        boolean disclosuresPresentCheckFound = false;
        int disclosureFoundCounter = 0;
        int disclosureIntactCounter = 0;
        boolean kbSigPresentCheckFound = false;
        boolean kbSigValidationConclusiveCheckFound = false;
        for (XmlConstraint xmlConstraint : validationProcessEAAPresentation.getConstraint()) {
            if (MessageTag.EAA_SIG_PRESENT.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                sigPresentCheckFound = true;
            } else if (MessageTag.ADEST_IBSVPSC.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                sigValidationConclusiveCheckFound = true;
            } else if (MessageTag.EAA_DPEAAP.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                disclosuresPresentCheckFound = true;
            } else if (MessageTag.BBB_CV_EAA_SDCBF.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                ++disclosureFoundCounter;
            } else if (MessageTag.BBB_CV_EAA_SDCBI.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.NOT_OK, xmlConstraint.getStatus());
                assertEquals(MessageTag.BBB_CV_EAA_SDCBI_ANS.getId(), xmlConstraint.getError().getKey());
                assertEquals(i18nProvider.getMessage(MessageTag.REFERENCE, digestMatchers.get(0).getDisclosableClaim().getName()), xmlConstraint.getAdditionalInfo());
                ++disclosureIntactCounter;
            } else if (MessageTag.EAA_KBSP.getId().equals(xmlConstraint.getName().getKey())) {
                kbSigPresentCheckFound = true;
            } else if (MessageTag.EAA_KBRC.getId().equals(xmlConstraint.getName().getKey())) {
                kbSigValidationConclusiveCheckFound = true;
            }
        }
        assertTrue(sigPresentCheckFound);
        assertTrue(sigValidationConclusiveCheckFound);
        assertTrue(disclosuresPresentCheckFound);
        assertEquals(1, disclosureFoundCounter);
        assertEquals(1, disclosureIntactCounter);
        assertFalse(kbSigPresentCheckFound);
        assertFalse(kbSigValidationConclusiveCheckFound);

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
        validationPolicy.getEAAPresentationConstraints().setDisclosurePresent(levelConstraint);

        EAAPresentationProcessExecutor executor = new EAAPresentationProcessExecutor();
        executor.setDiagnosticData(diagnosticData);
        executor.setCurrentTime(diagnosticData.getValidationDate());
        executor.setValidationPolicy(validationPolicy);

        Reports reports = executor.execute();

        SimpleReport simpleReport = reports.getSimpleReport();
        assertNotNull(simpleReport);

        assertEquals(Indication.PASSED, simpleReport.getIndication(simpleReport.getFirstEAAPresentationId()));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationErrors(simpleReport.getFirstEAAPresentationId())));
        assertFalse(Utils.isCollectionEmpty(simpleReport.getAdESValidationWarnings(simpleReport.getFirstEAAPresentationId())));
        assertTrue(checkMessageValuePresence(simpleReport.getAdESValidationWarnings(simpleReport.getFirstEAAPresentationId()), i18nProvider.getMessage(MessageTag.EAA_DPEAAP_ANS)));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationInfo(simpleReport.getFirstEAAPresentationId())));

        DetailedReport detailedReport = reports.getDetailedReport();
        assertEquals(Indication.PASSED, detailedReport.getFinalIndication(simpleReport.getFirstEAAPresentationId()));

        XmlEAAPresentation xmlEAAPresentation = detailedReport.getXmlEAAPresentationById(detailedReport.getFirstEAAPresentationId());
        assertNotNull(xmlEAAPresentation);

        XmlValidationProcessEAAPresentation validationProcessEAAPresentation = xmlEAAPresentation.getValidationProcessEAAPresentation();
        assertNotNull(validationProcessEAAPresentation);
        assertEquals(Indication.PASSED, validationProcessEAAPresentation.getConclusion().getIndication());

        boolean sigPresentCheckFound = false;
        boolean sigValidationConclusiveCheckFound = false;
        boolean disclosuresPresentCheckFound = false;
        int disclosureFoundCounter = 0;
        int disclosureIntactCounter = 0;
        boolean kbSigPresentCheckFound = false;
        boolean kbSigValidationConclusiveCheckFound = false;

        for (XmlConstraint xmlConstraint : validationProcessEAAPresentation.getConstraint()) {
            if (MessageTag.EAA_SIG_PRESENT.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                sigPresentCheckFound = true;
            } else if (MessageTag.ADEST_IBSVPSC.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                sigValidationConclusiveCheckFound = true;
            } else if (MessageTag.EAA_DPEAAP.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.WARNING, xmlConstraint.getStatus());
                assertEquals(MessageTag.EAA_DPEAAP_ANS.getId(), xmlConstraint.getWarning().getKey());
                disclosuresPresentCheckFound = true;
            } else if (MessageTag.BBB_CV_EAA_SDCBF.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                ++disclosureFoundCounter;
            } else if (MessageTag.BBB_CV_EAA_SDCBI.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                ++disclosureIntactCounter;
            } else if (MessageTag.EAA_KBSP.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                kbSigPresentCheckFound = true;
            } else if (MessageTag.EAA_KBRC.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                kbSigValidationConclusiveCheckFound = true;
            }
        }
        assertTrue(sigPresentCheckFound);
        assertTrue(sigValidationConclusiveCheckFound);
        assertTrue(disclosuresPresentCheckFound);
        assertEquals(0, disclosureFoundCounter);
        assertEquals(0, disclosureIntactCounter);
        assertTrue(kbSigPresentCheckFound);
        assertTrue(kbSigValidationConclusiveCheckFound);

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
        validationPolicy.getEAAPresentationConstraints().setDisclosurePresent(levelConstraint);

        EAAPresentationProcessExecutor executor = new EAAPresentationProcessExecutor();
        executor.setDiagnosticData(diagnosticData);
        executor.setCurrentTime(diagnosticData.getValidationDate());
        executor.setValidationPolicy(validationPolicy);

        Reports reports = executor.execute();

        SimpleReport simpleReport = reports.getSimpleReport();
        assertNotNull(simpleReport);

        assertEquals(Indication.INDETERMINATE, simpleReport.getIndication(simpleReport.getFirstEAAPresentationId()));
        assertEquals(SubIndication.SIGNED_DATA_NOT_FOUND, simpleReport.getSubIndication(simpleReport.getFirstEAAPresentationId()));

        assertFalse(Utils.isCollectionEmpty(simpleReport.getAdESValidationErrors(simpleReport.getFirstEAAPresentationId())));
        assertTrue(checkMessageValuePresence(simpleReport.getAdESValidationErrors(simpleReport.getFirstEAAPresentationId()), i18nProvider.getMessage(MessageTag.EAA_DPEAAP_ANS)));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationWarnings(simpleReport.getFirstEAAPresentationId())));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationInfo(simpleReport.getFirstEAAPresentationId())));

        DetailedReport detailedReport = reports.getDetailedReport();
        assertEquals(Indication.INDETERMINATE, detailedReport.getFinalIndication(simpleReport.getFirstEAAPresentationId()));
        assertEquals(SubIndication.SIGNED_DATA_NOT_FOUND, detailedReport.getFinalSubIndication(simpleReport.getFirstEAAPresentationId()));

        XmlEAAPresentation xmlEAAPresentation = detailedReport.getXmlEAAPresentationById(detailedReport.getFirstEAAPresentationId());
        assertNotNull(xmlEAAPresentation);

        XmlValidationProcessEAAPresentation validationProcessEAAPresentation = xmlEAAPresentation.getValidationProcessEAAPresentation();
        assertNotNull(validationProcessEAAPresentation);
        assertEquals(Indication.INDETERMINATE, validationProcessEAAPresentation.getConclusion().getIndication());
        assertEquals(SubIndication.SIGNED_DATA_NOT_FOUND, validationProcessEAAPresentation.getConclusion().getSubIndication());

        boolean sigPresentCheckFound = false;
        boolean sigValidationConclusiveCheckFound = false;
        boolean disclosuresPresentCheckFound = false;
        int disclosureFoundCounter = 0;
        int disclosureIntactCounter = 0;
        boolean kbSigPresentCheckFound = false;
        boolean kbSigValidationConclusiveCheckFound = false;

        for (XmlConstraint xmlConstraint : validationProcessEAAPresentation.getConstraint()) {
            if (MessageTag.EAA_SIG_PRESENT.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                sigPresentCheckFound = true;
            } else if (MessageTag.ADEST_IBSVPSC.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                sigValidationConclusiveCheckFound = true;
            } else if (MessageTag.EAA_DPEAAP.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.NOT_OK, xmlConstraint.getStatus());
                assertEquals(MessageTag.EAA_DPEAAP_ANS.getId(), xmlConstraint.getError().getKey());
                disclosuresPresentCheckFound = true;
            } else if (MessageTag.BBB_CV_EAA_SDCBF.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                ++disclosureFoundCounter;
            } else if (MessageTag.BBB_CV_EAA_SDCBI.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                ++disclosureIntactCounter;
            } else if (MessageTag.EAA_KBSP.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                kbSigPresentCheckFound = true;
            } else if (MessageTag.EAA_KBRC.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                kbSigValidationConclusiveCheckFound = true;
            }
        }
        assertTrue(sigPresentCheckFound);
        assertTrue(sigValidationConclusiveCheckFound);
        assertTrue(disclosuresPresentCheckFound);
        assertEquals(0, disclosureFoundCounter);
        assertEquals(0, disclosureIntactCounter);
        assertFalse(kbSigPresentCheckFound);
        assertFalse(kbSigValidationConclusiveCheckFound);

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

        assertEquals(Indication.FAILED, simpleReport.getIndication(simpleReport.getFirstEAAPresentationId()));
        assertEquals(SubIndication.FORMAT_FAILURE, simpleReport.getSubIndication(simpleReport.getFirstEAAPresentationId()));
        assertFalse(Utils.isCollectionEmpty(simpleReport.getAdESValidationErrors(simpleReport.getFirstEAAPresentationId())));
        assertTrue(checkMessageValuePresence(simpleReport.getAdESValidationErrors(simpleReport.getFirstEAAPresentationId()), i18nProvider.getMessage(MessageTag.ADEST_IBSVPSC_ANS)));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationWarnings(simpleReport.getFirstEAAPresentationId())));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationInfo(simpleReport.getFirstEAAPresentationId())));

        List<XmlSignature> eaaPresentationSignatures = simpleReport.getEAAPresentationSignatures(simpleReport.getFirstEAAPresentationId());
        assertEquals(1, eaaPresentationSignatures.size());
        XmlSignature eaaSignature = eaaPresentationSignatures.get(0);

        assertEquals(Indication.TOTAL_FAILED, simpleReport.getIndication(eaaSignature.getId()));
        assertEquals(SubIndication.FORMAT_FAILURE, simpleReport.getSubIndication(eaaSignature.getId()));
        assertFalse(Utils.isCollectionEmpty(simpleReport.getAdESValidationErrors(eaaSignature.getId())));
        assertTrue(checkMessageValuePresence(simpleReport.getAdESValidationErrors(eaaSignature.getId()), i18nProvider.getMessage(MessageTag.BBB_FC_IEFF_ANS)));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationWarnings(eaaSignature.getId())));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationInfo(eaaSignature.getId())));

        XmlSignature keyBindingSignature = simpleReport.getEAAPresentationKeyBindingSignature(simpleReport.getFirstEAAPresentationId());
        assertEquals(Indication.TOTAL_PASSED, simpleReport.getIndication(keyBindingSignature.getId()));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationErrors(keyBindingSignature.getId())));
        assertFalse(Utils.isCollectionEmpty(simpleReport.getAdESValidationWarnings(keyBindingSignature.getId())));
        assertTrue(checkMessageValuePresence(simpleReport.getAdESValidationWarnings(keyBindingSignature.getId()), i18nProvider.getMessage(MessageTag.BBB_ICS_ISCI_ANS)));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationInfo(keyBindingSignature.getId())));

        DetailedReport detailedReport = reports.getDetailedReport();
        assertEquals(Indication.FAILED, detailedReport.getFinalIndication(simpleReport.getFirstEAAPresentationId()));
        assertEquals(SubIndication.FORMAT_FAILURE, detailedReport.getFinalSubIndication(simpleReport.getFirstEAAPresentationId()));

        XmlEAAPresentation xmlEAAPresentation = detailedReport.getXmlEAAPresentationById(detailedReport.getFirstEAAPresentationId());
        assertNotNull(xmlEAAPresentation);

        XmlValidationProcessEAAPresentation validationProcessEAAPresentation = xmlEAAPresentation.getValidationProcessEAAPresentation();
        assertNotNull(validationProcessEAAPresentation);
        assertEquals(Indication.FAILED, validationProcessEAAPresentation.getConclusion().getIndication());
        assertEquals(SubIndication.FORMAT_FAILURE, validationProcessEAAPresentation.getConclusion().getSubIndication());

        boolean sigPresentCheckFound = false;
        boolean sigValidationConclusiveCheckFound = false;
        boolean disclosuresPresentCheckFound = false;
        int disclosureFoundCounter = 0;
        int disclosureIntactCounter = 0;
        boolean kbSigPresentCheckFound = false;
        boolean kbSigValidationConclusiveCheckFound = false;
        for (XmlConstraint xmlConstraint : validationProcessEAAPresentation.getConstraint()) {
            if (MessageTag.EAA_SIG_PRESENT.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                sigPresentCheckFound = true;
            } else if (MessageTag.ADEST_IBSVPSC.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.NOT_OK, xmlConstraint.getStatus());
                assertEquals(MessageTag.ADEST_IBSVPSC_ANS.getId(), xmlConstraint.getError().getKey());
                sigValidationConclusiveCheckFound = true;
            } else if (MessageTag.EAA_DPEAAP.getId().equals(xmlConstraint.getName().getKey())) {
                disclosuresPresentCheckFound = true;
            } else if (MessageTag.BBB_CV_EAA_SDCBF.getId().equals(xmlConstraint.getName().getKey())) {
                ++disclosureFoundCounter;
            } else if (MessageTag.BBB_CV_EAA_SDCBI.getId().equals(xmlConstraint.getName().getKey())) {
                ++disclosureIntactCounter;
            } else if (MessageTag.EAA_KBSP.getId().equals(xmlConstraint.getName().getKey())) {
                kbSigPresentCheckFound = true;
            } else if (MessageTag.EAA_KBRC.getId().equals(xmlConstraint.getName().getKey())) {
                kbSigValidationConclusiveCheckFound = true;
            }
        }
        assertTrue(sigPresentCheckFound);
        assertTrue(sigValidationConclusiveCheckFound);
        assertFalse(disclosuresPresentCheckFound);
        assertEquals(0, disclosureFoundCounter);
        assertEquals(0, disclosureIntactCounter);
        assertFalse(kbSigPresentCheckFound);
        assertFalse(kbSigValidationConclusiveCheckFound);

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

        assertEquals(Indication.FAILED, simpleReport.getIndication(simpleReport.getFirstEAAPresentationId()));
        assertEquals(SubIndication.FORMAT_FAILURE, simpleReport.getSubIndication(simpleReport.getFirstEAAPresentationId()));
        assertFalse(Utils.isCollectionEmpty(simpleReport.getAdESValidationErrors(simpleReport.getFirstEAAPresentationId())));
        assertTrue(checkMessageValuePresence(simpleReport.getAdESValidationErrors(simpleReport.getFirstEAAPresentationId()), i18nProvider.getMessage(MessageTag.EAA_KBRC_ANS)));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationWarnings(simpleReport.getFirstEAAPresentationId())));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationInfo(simpleReport.getFirstEAAPresentationId())));

        List<XmlSignature> eaaPresentationSignatures = simpleReport.getEAAPresentationSignatures(simpleReport.getFirstEAAPresentationId());
        assertEquals(1, eaaPresentationSignatures.size());
        XmlSignature eaaSignature = eaaPresentationSignatures.get(0);

        assertEquals(Indication.TOTAL_PASSED, simpleReport.getIndication(eaaSignature.getId()));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationErrors(eaaSignature.getId())));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationWarnings(eaaSignature.getId())));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationInfo(eaaSignature.getId())));

        XmlSignature keyBindingSignature = simpleReport.getEAAPresentationKeyBindingSignature(simpleReport.getFirstEAAPresentationId());
        assertEquals(Indication.TOTAL_FAILED, simpleReport.getIndication(keyBindingSignature.getId()));
        assertEquals(SubIndication.FORMAT_FAILURE, simpleReport.getSubIndication(keyBindingSignature.getId()));
        assertFalse(Utils.isCollectionEmpty(simpleReport.getAdESValidationErrors(keyBindingSignature.getId())));
        assertTrue(checkMessageValuePresence(simpleReport.getAdESValidationErrors(keyBindingSignature.getId()), i18nProvider.getMessage(MessageTag.BBB_FC_IEFF_ANS)));
        assertFalse(Utils.isCollectionEmpty(simpleReport.getAdESValidationWarnings(keyBindingSignature.getId())));
        assertTrue(checkMessageValuePresence(simpleReport.getAdESValidationWarnings(keyBindingSignature.getId()), i18nProvider.getMessage(MessageTag.BBB_ICS_ISCI_ANS)));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationInfo(keyBindingSignature.getId())));

        DetailedReport detailedReport = reports.getDetailedReport();
        assertEquals(Indication.FAILED, detailedReport.getFinalIndication(simpleReport.getFirstEAAPresentationId()));
        assertEquals(SubIndication.FORMAT_FAILURE, detailedReport.getFinalSubIndication(simpleReport.getFirstEAAPresentationId()));

        XmlEAAPresentation xmlEAAPresentation = detailedReport.getXmlEAAPresentationById(detailedReport.getFirstEAAPresentationId());
        assertNotNull(xmlEAAPresentation);

        XmlValidationProcessEAAPresentation validationProcessEAAPresentation = xmlEAAPresentation.getValidationProcessEAAPresentation();
        assertNotNull(validationProcessEAAPresentation);
        assertEquals(Indication.FAILED, validationProcessEAAPresentation.getConclusion().getIndication());
        assertEquals(SubIndication.FORMAT_FAILURE, validationProcessEAAPresentation.getConclusion().getSubIndication());

        boolean sigPresentCheckFound = false;
        boolean sigValidationConclusiveCheckFound = false;
        boolean disclosuresPresentCheckFound = false;
        int disclosureFoundCounter = 0;
        int disclosureIntactCounter = 0;
        boolean kbSigPresentCheckFound = false;
        boolean kbSigValidationConclusiveCheckFound = false;
        for (XmlConstraint xmlConstraint : validationProcessEAAPresentation.getConstraint()) {
            if (MessageTag.EAA_SIG_PRESENT.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                sigPresentCheckFound = true;
            } else if (MessageTag.ADEST_IBSVPSC.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                sigValidationConclusiveCheckFound = true;
            } else if (MessageTag.EAA_DPEAAP.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                disclosuresPresentCheckFound = true;
            } else if (MessageTag.BBB_CV_EAA_SDCBF.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                ++disclosureFoundCounter;
            } else if (MessageTag.BBB_CV_EAA_SDCBI.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                ++disclosureIntactCounter;
            } else if (MessageTag.EAA_KBSP.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.OK, xmlConstraint.getStatus());
                kbSigPresentCheckFound = true;
            } else if (MessageTag.EAA_KBRC.getId().equals(xmlConstraint.getName().getKey())) {
                assertEquals(XmlStatus.NOT_OK, xmlConstraint.getStatus());
                assertEquals(MessageTag.EAA_KBRC_ANS.getId(), xmlConstraint.getError().getKey());
                kbSigValidationConclusiveCheckFound = true;
            }
        }
        assertTrue(sigPresentCheckFound);
        assertTrue(sigValidationConclusiveCheckFound);
        assertTrue(disclosuresPresentCheckFound);
        assertEquals(10, disclosureFoundCounter);
        assertEquals(10, disclosureIntactCounter);
        assertTrue(kbSigPresentCheckFound);
        assertTrue(kbSigValidationConclusiveCheckFound);

        checkReports(reports);
    }

    @Override
    protected EtsiValidationPolicy loadDefaultPolicy() throws Exception {
        return (EtsiValidationPolicy) ValidationPolicyLoader.fromValidationPolicy(EAA_POLICY_LOCATION).create();
    }

}
