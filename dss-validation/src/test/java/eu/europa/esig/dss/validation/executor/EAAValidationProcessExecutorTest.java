package eu.europa.esig.dss.validation.executor;

import eu.europa.esig.dss.detailedreport.DetailedReport;
import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraint;
import eu.europa.esig.dss.detailedreport.jaxb.XmlEAA;
import eu.europa.esig.dss.detailedreport.jaxb.XmlStatus;
import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationProcessEAA;
import eu.europa.esig.dss.diagnostic.DiagnosticDataFacade;
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

        boolean sigPresentCheckFound = false;
        boolean sigValidationConclusiveCheckFound = false;
        boolean disclosuresPresentCheckFound = false;
        int disclosureFoundCounter = 0;
        int disclosureIntactCounter = 0;
        boolean kbSigPresentCheckFound = false;
        boolean kbSigValidationConclusiveCheckFound = false;
        for (XmlConstraint xmlConstraint : validationProcessEAA.getConstraint()) {
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

        boolean sigPresentCheckFound = false;
        boolean sigValidationConclusiveCheckFound = false;
        boolean disclosuresPresentCheckFound = false;
        int disclosureFoundCounter = 0;
        int disclosureIntactCounter = 0;
        boolean kbSigPresentCheckFound = false;
        boolean kbSigValidationConclusiveCheckFound = false;
        for (XmlConstraint xmlConstraint : validationProcessEAA.getConstraint()) {
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

        List<XmlDigestMatcher> digestMatchers = diagnosticData.getEAAs().get(0).getDigestMatchers();
        digestMatchers.get(0).setDataIntact(false);

        EAAPresentationProcessExecutor executor = new EAAPresentationProcessExecutor();
        executor.setDiagnosticData(diagnosticData);
        executor.setCurrentTime(diagnosticData.getValidationDate());
        executor.setValidationPolicy(loadDefaultPolicy());

        Reports reports = executor.execute();

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

        boolean sigPresentCheckFound = false;
        boolean sigValidationConclusiveCheckFound = false;
        boolean disclosuresPresentCheckFound = false;
        int disclosureFoundCounter = 0;
        int disclosureIntactCounter = 0;
        boolean kbSigPresentCheckFound = false;
        boolean kbSigValidationConclusiveCheckFound = false;
        for (XmlConstraint xmlConstraint : validationProcessEAA.getConstraint()) {
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

        boolean sigPresentCheckFound = false;
        boolean sigValidationConclusiveCheckFound = false;
        boolean disclosuresPresentCheckFound = false;
        int disclosureFoundCounter = 0;
        int disclosureIntactCounter = 0;
        boolean kbSigPresentCheckFound = false;
        boolean kbSigValidationConclusiveCheckFound = false;

        for (XmlConstraint xmlConstraint : validationProcessEAA.getConstraint()) {
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
        validationPolicy.getEAAConstraints().setDisclosurePresent(levelConstraint);

        EAAPresentationProcessExecutor executor = new EAAPresentationProcessExecutor();
        executor.setDiagnosticData(diagnosticData);
        executor.setCurrentTime(diagnosticData.getValidationDate());
        executor.setValidationPolicy(validationPolicy);

        Reports reports = executor.execute();

        SimpleReport simpleReport = reports.getSimpleReport();
        assertNotNull(simpleReport);

        assertEquals(Indication.INDETERMINATE, simpleReport.getIndication(simpleReport.getFirstEAAId()));
        assertEquals(SubIndication.SIGNED_DATA_NOT_FOUND, simpleReport.getSubIndication(simpleReport.getFirstEAAId()));

        assertFalse(Utils.isCollectionEmpty(simpleReport.getAdESValidationErrors(simpleReport.getFirstEAAId())));
        assertTrue(checkMessageValuePresence(simpleReport.getAdESValidationErrors(simpleReport.getFirstEAAId()), i18nProvider.getMessage(MessageTag.EAA_DPEAAP_ANS)));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationWarnings(simpleReport.getFirstEAAId())));
        assertTrue(Utils.isCollectionEmpty(simpleReport.getAdESValidationInfo(simpleReport.getFirstEAAId())));

        DetailedReport detailedReport = reports.getDetailedReport();
        assertEquals(Indication.INDETERMINATE, detailedReport.getFinalIndication(simpleReport.getFirstEAAId()));
        assertEquals(SubIndication.SIGNED_DATA_NOT_FOUND, detailedReport.getFinalSubIndication(simpleReport.getFirstEAAId()));

        XmlEAA xmlEAA = detailedReport.getXmlEAAById(detailedReport.getFirstEAAId());
        assertNotNull(xmlEAA);

        XmlValidationProcessEAA validationProcessEAA = xmlEAA.getValidationProcessEAA();
        assertNotNull(validationProcessEAA);
        assertEquals(Indication.INDETERMINATE, validationProcessEAA.getConclusion().getIndication());
        assertEquals(SubIndication.SIGNED_DATA_NOT_FOUND, validationProcessEAA.getConclusion().getSubIndication());

        boolean sigPresentCheckFound = false;
        boolean sigValidationConclusiveCheckFound = false;
        boolean disclosuresPresentCheckFound = false;
        int disclosureFoundCounter = 0;
        int disclosureIntactCounter = 0;
        boolean kbSigPresentCheckFound = false;
        boolean kbSigValidationConclusiveCheckFound = false;

        for (XmlConstraint xmlConstraint : validationProcessEAA.getConstraint()) {
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

        boolean sigPresentCheckFound = false;
        boolean sigValidationConclusiveCheckFound = false;
        boolean disclosuresPresentCheckFound = false;
        int disclosureFoundCounter = 0;
        int disclosureIntactCounter = 0;
        boolean kbSigPresentCheckFound = false;
        boolean kbSigValidationConclusiveCheckFound = false;
        for (XmlConstraint xmlConstraint : validationProcessEAA.getConstraint()) {
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

        boolean sigPresentCheckFound = false;
        boolean sigValidationConclusiveCheckFound = false;
        boolean disclosuresPresentCheckFound = false;
        int disclosureFoundCounter = 0;
        int disclosureIntactCounter = 0;
        boolean kbSigPresentCheckFound = false;
        boolean kbSigValidationConclusiveCheckFound = false;
        for (XmlConstraint xmlConstraint : validationProcessEAA.getConstraint()) {
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

        boolean etsiConformanceCheckFound = false;
        boolean technicalValidityNotBeforeCheckFound = false;
        boolean technicalValidityExpirationCheckFound = false;
        boolean technicalValidityPeriodCheckFound = false;
        boolean administrativeValidityNotBeforeCheckFound = false;
        boolean administrativeValidityExpirationCheckFound = false;
        boolean administrativeValidityPeriodCheckFound = false;

        for (XmlConstraint xmlConstraint : validationProcessEAA.getConstraint()) {
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

        boolean etsiConformanceCheckFound = false;
        boolean technicalValidityNotBeforeCheckFound = false;
        boolean technicalValidityExpirationCheckFound = false;
        boolean technicalValidityPeriodCheckFound = false;
        boolean administrativeValidityNotBeforeCheckFound = false;
        boolean administrativeValidityExpirationCheckFound = false;
        boolean administrativeValidityPeriodCheckFound = false;

        for (XmlConstraint xmlConstraint : validationProcessEAA.getConstraint()) {
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

        boolean etsiConformanceCheckFound = false;
        boolean technicalValidityNotBeforeCheckFound = false;
        boolean technicalValidityExpirationCheckFound = false;
        boolean technicalValidityPeriodCheckFound = false;
        boolean administrativeValidityNotBeforeCheckFound = false;
        boolean administrativeValidityExpirationCheckFound = false;
        boolean administrativeValidityPeriodCheckFound = false;

        for (XmlConstraint xmlConstraint : validationProcessEAA.getConstraint()) {
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

        boolean etsiConformanceCheckFound = false;
        boolean technicalValidityNotBeforeCheckFound = false;
        boolean technicalValidityExpirationCheckFound = false;
        boolean technicalValidityPeriodCheckFound = false;
        boolean administrativeValidityNotBeforeCheckFound = false;
        boolean administrativeValidityExpirationCheckFound = false;
        boolean administrativeValidityPeriodCheckFound = false;

        for (XmlConstraint xmlConstraint : validationProcessEAA.getConstraint()) {
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

        boolean etsiConformanceCheckFound = false;
        boolean technicalValidityNotBeforeCheckFound = false;
        boolean technicalValidityExpirationCheckFound = false;
        boolean technicalValidityPeriodCheckFound = false;
        boolean administrativeValidityNotBeforeCheckFound = false;
        boolean administrativeValidityExpirationCheckFound = false;
        boolean administrativeValidityPeriodCheckFound = false;

        for (XmlConstraint xmlConstraint : validationProcessEAA.getConstraint()) {
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

    @Override
    protected EtsiValidationPolicy loadDefaultPolicy() throws Exception {
        return (EtsiValidationPolicy) ValidationPolicyLoader.fromValidationPolicy(EAA_POLICY_LOCATION).create();
    }

}
