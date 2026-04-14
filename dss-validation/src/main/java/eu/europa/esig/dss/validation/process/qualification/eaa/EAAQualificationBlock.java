package eu.europa.esig.dss.validation.process.qualification.eaa;

import eu.europa.esig.dss.detailedreport.jaxb.XmlConclusion;
import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraintsConclusion;
import eu.europa.esig.dss.detailedreport.jaxb.XmlLoTEAnalysis;
import eu.europa.esig.dss.detailedreport.jaxb.XmlSignature;
import eu.europa.esig.dss.detailedreport.jaxb.XmlTLAnalysis;
import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationEAAQualification;
import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationEAAQualificationProcess;
import eu.europa.esig.dss.detailedreport.jaxb.XmlValidationPIDQualificationProcess;
import eu.europa.esig.dss.diagnostic.CertificateWrapper;
import eu.europa.esig.dss.diagnostic.EAAPresentationWrapper;
import eu.europa.esig.dss.diagnostic.SignatureWrapper;
import eu.europa.esig.dss.enumerations.EAAQualification;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.validation.process.Chain;
import eu.europa.esig.dss.validation.process.ChainItem;
import eu.europa.esig.dss.validation.process.qualification.eaa.checks.EAAQualificationProcessConclusiveCheck;
import eu.europa.esig.dss.validation.process.qualification.eaa.checks.TrustAnchorListReachedForCertificateChainCheck;
import eu.europa.esig.dss.validation.process.qualification.eaa.pid.PIDQualificationProcessBlock;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * This class is used to verify qualification status of a signature used to create the EAA
 *
 */
public class EAAQualificationBlock extends Chain<XmlValidationEAAQualification> {

    /** The EAA Presentation to be validated */
    private final EAAPresentationWrapper eaaPresentation;

    /** The conclusion of EAA Presentation validation */
    private final XmlConclusion eaaPresentationConclusion;

    /** Map of signature validation processes */
    private final Map<String, XmlSignature> signatureMap;

    /** The list of all TL analyses */
    private final List<XmlTLAnalysis> tlAnalysis;

    /** List of List of Trusted Entities validations */
    private final List<XmlLoTEAnalysis> loteAnalysis;

    /** Validation time */
    private final Date currentTime;

    /**
     * Default constructor
     *
     * @param i18nProvider         {@link I18nProvider}
     * @param eaaPresentation      {@link EAAPresentationWrapper} for which qualification is to be determined
     * @param eaaPresentationConclusion {@link XmlConclusion}
     * @param signatureMap         a map of signature validations
     * @param tlAnalysis           a list of performed {@link XmlTLAnalysis}
     * @param loteAnalysis         a list of performed {@link XmlLoTEAnalysis}
     * @param currentTime          {@link Date}
     */
    public EAAQualificationBlock(final I18nProvider i18nProvider, final EAAPresentationWrapper eaaPresentation,
                                 final XmlConclusion eaaPresentationConclusion, final Map<String, XmlSignature> signatureMap,
                                 final List<XmlTLAnalysis> tlAnalysis, final List<XmlLoTEAnalysis> loteAnalysis, final Date currentTime) {
        super(i18nProvider, new XmlValidationEAAQualification());
        this.eaaPresentation = eaaPresentation;
        this.eaaPresentationConclusion = eaaPresentationConclusion;
        this.signatureMap = signatureMap;
        this.tlAnalysis = tlAnalysis;
        this.loteAnalysis = loteAnalysis;
        this.currentTime = currentTime;
    }

    @Override
    protected MessageTag getTitle() {
        return MessageTag.EAA_QUALIFICATION;
    }

    @Override
    protected void initChain() {

        XmlValidationEAAQualificationProcess eaaQualificationProcess = null;
        XmlValidationPIDQualificationProcess pidQualificationProcess = null;

        if (Utils.collectionSize(eaaPresentation.getEAAPresentationSignatures()) == 1) {

            CertificateWrapper signingCertificate = getSigningCertificate();

            ChainItem<XmlValidationEAAQualification> item = firstItem = isTrustAnchorListReachedForCertificateChain(signingCertificate);

            EAAQualificationProcessBlock eaaQualificationProcessBlock = new EAAQualificationProcessBlock(
                    i18nProvider, eaaPresentation, eaaPresentationConclusion, signatureMap, tlAnalysis, currentTime);
            eaaQualificationProcess = eaaQualificationProcessBlock.execute();
            result.setValidationEAAQualificationProcess(eaaQualificationProcess);

            PIDQualificationProcessBlock pidQualificationProcessBlock = new PIDQualificationProcessBlock(
                    i18nProvider, eaaPresentation, eaaPresentationConclusion, loteAnalysis, currentTime);
            pidQualificationProcess = pidQualificationProcessBlock.execute();
            result.setValidationPIDQualificationProcess(pidQualificationProcess);

            item = item.setNextItem(eaaQualificationProcessConclusiveCheck(eaaQualificationProcess, pidQualificationProcess));

        }

        determineFinalQualification(eaaQualificationProcess, pidQualificationProcess);

    }

    private ChainItem<XmlValidationEAAQualification> isTrustAnchorListReachedForCertificateChain(CertificateWrapper signingCertificate) {
        return new TrustAnchorListReachedForCertificateChainCheck(i18nProvider, result, signingCertificate, getFailLevelRule());
    }

    private ChainItem<XmlValidationEAAQualification> eaaQualificationProcessConclusiveCheck(XmlConstraintsConclusion... conclusions) {
        return new EAAQualificationProcessConclusiveCheck(i18nProvider, result, Arrays.asList(conclusions), getFailLevelRule());
    }

    private CertificateWrapper getSigningCertificate() {
        SignatureWrapper eaaPresentationSignature = eaaPresentation.getEAAPresentationSignatures().get(0);
        return eaaPresentationSignature.getSigningCertificate();
    }

    private void determineFinalQualification(XmlValidationEAAQualificationProcess eaaQualificationProcess,
                                             XmlValidationPIDQualificationProcess pidQualificationProcess) {
        EAAQualification eaaQualification = EAAQualification.NA;
        if (eaaQualificationProcess != null) {
            eaaQualification = eaaQualificationProcess.getEAAQualification();
        }
        if (EAAQualification.NA != eaaQualification) {
            result.getEAAQualification().add(eaaQualification);
        }
        EAAQualification pidQualification = EAAQualification.NA;
        if (pidQualificationProcess != null) {
            pidQualification = pidQualificationProcess.getEAAQualification();
        }
        if ((EAAQualification.PID == pidQualification || EAAQualification.INDETERMINATE_PID == pidQualification)
                && pidQualification != eaaQualification) {
            result.getEAAQualification().add(pidQualification);
        } else if ((EAAQualification.UNKNOWN == pidQualification || EAAQualification.INDETERMINATE_UNKNOWN == pidQualification)
                && EAAQualification.NA == eaaQualification) {
            result.getEAAQualification().add(pidQualification);
        }
        if (Utils.isCollectionEmpty(result.getEAAQualification())) {
            result.getEAAQualification().add(EAAQualification.NA);
        }
    }

    @Override
    protected void collectAdditionalMessages(XmlConclusion conclusion) {
        CertificateWrapper signingCertificate = getSigningCertificate();
        if (signingCertificate != null && (signingCertificate.isTrustedListReached() || signingCertificate.isListOfTrustedEntitiesReached())) {
            if (signingCertificate.isTrustedListReached()) {
                XmlValidationEAAQualificationProcess eaaQualificationProcess = result.getValidationEAAQualificationProcess();
                super.collectAllMessages(conclusion, eaaQualificationProcess.getConclusion());
            }
            if (signingCertificate.isListOfTrustedEntitiesReached()) {
                XmlValidationPIDQualificationProcess pidQualificationProcess = result.getValidationPIDQualificationProcess();
                super.collectAllMessages(conclusion, pidQualificationProcess.getConclusion());
            }
        }
    }
}
