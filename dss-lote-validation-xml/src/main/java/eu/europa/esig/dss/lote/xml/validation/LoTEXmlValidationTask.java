package eu.europa.esig.dss.lote.xml.validation;

import eu.europa.esig.dss.diagnostic.CertificateWrapper;
import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.SignatureWrapper;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.enumerations.TokenExtractionStrategy;
import eu.europa.esig.dss.enumerations.ValidationLevel;
import eu.europa.esig.dss.lote.validation.ValidationResult;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.model.policy.ValidationPolicy;
import eu.europa.esig.dss.model.x509.CertificateToken;
import eu.europa.esig.dss.simplereport.SimpleReport;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.spi.policy.SignaturePolicyProvider;
import eu.europa.esig.dss.spi.validation.CertificateVerifier;
import eu.europa.esig.dss.spi.validation.CommonCertificateVerifier;
import eu.europa.esig.dss.spi.validation.executor.SkipValidationContextExecutor;
import eu.europa.esig.dss.spi.x509.CertificateSource;
import eu.europa.esig.dss.spi.x509.CommonTrustedCertificateSource;
import eu.europa.esig.dss.spi.x509.TrustedCertificateSource;
import eu.europa.esig.dss.validation.policy.ValidationPolicyLoader;
import eu.europa.esig.dss.validation.reports.Reports;
import eu.europa.esig.dss.xades.definition.xades132.XAdES132Path;
import eu.europa.esig.dss.xades.validation.XMLDocumentValidator;

import java.util.Collections;
import java.util.Date;
import java.util.Objects;
import java.util.function.Supplier;

public class LoTEXmlValidationTask implements Supplier<ValidationResult> {

    /** The path for a LOTL/TL validation policy */
    private static final String TRUSTED_LIST_VALIDATION_POLICY_LOCATION = "/policy/tsl-constraint.xml"; // TODO : change policy ?

    /** The Trusted List document to validate */
    private final DSSDocument loteDocument;

    /** The certificate source to use */
    private final CertificateSource signingCertificateSource;

    /**
     * Constructor used to instantiate a validator for a LoTE
     *
     * @param loteDocument      the DSSDocument with a LoTE
     * @param signingCertificateSource a certificate source with the allowed certificates
     *                          to sign this TL
     */
    public LoTEXmlValidationTask(DSSDocument loteDocument, CertificateSource signingCertificateSource) {
        Objects.requireNonNull(loteDocument, "The document is null");
        Objects.requireNonNull(signingCertificateSource, "The certificate source is null");
        this.loteDocument = loteDocument;
        this.signingCertificateSource = signingCertificateSource;
    }

    @Override
    public ValidationResult get() {
        Reports reports = validate();
        return fillResult(reports);
    }

    private Reports validate() {
        final CertificateVerifier certificateVerifier = new CommonCertificateVerifier(true);
        certificateVerifier.setTrustedCertSources(buildTrustedCertificateSource(signingCertificateSource));

        // To increase the security: the default {@code XAdESPaths} is used.
        final XMLDocumentValidator xmlDocumentValidator = new XMLDocumentValidator(loteDocument, Collections.singletonList(new XAdES132Path()));

        xmlDocumentValidator.setCertificateVerifier(certificateVerifier);
        xmlDocumentValidator.setTokenExtractionStrategy(TokenExtractionStrategy.EXTRACT_CERTIFICATES_ONLY);
        xmlDocumentValidator.setEnableEtsiValidationReport(false); // Ignore ETSI VR
        xmlDocumentValidator.setValidationLevel(ValidationLevel.BASIC_SIGNATURES); // Timestamps,... are ignored
        xmlDocumentValidator.setValidationContextExecutor(SkipValidationContextExecutor.INSTANCE); // Only need to validate against the trusted certificate source
        xmlDocumentValidator.setSignaturePolicyProvider(new SignaturePolicyProvider()); // ignore signature policy loading

        return xmlDocumentValidator.validateDocument(getTrustedListValidationPolicy());
    }

    private ValidationResult fillResult(Reports reports) {
        SimpleReport simpleReport = reports.getSimpleReport();
        if (simpleReport.getSignaturesCount() != 1) {
            throw new DSSException(String.format("Number of signatures must be equal to 1 (currently : %s)", simpleReport.getSignaturesCount()));
        }

        Indication indication = simpleReport.getIndication(simpleReport.getFirstSignatureId());
        SubIndication subIndication = simpleReport.getSubIndication(simpleReport.getFirstSignatureId());

        DiagnosticData diagnosticData = reports.getDiagnosticData();
        SignatureWrapper signatureWrapper = diagnosticData.getSignatureById(diagnosticData.getFirstSignatureId());
        Date signingTime = signatureWrapper.getClaimedSigningTime();
        CertificateWrapper signingCertificateWrapper = signatureWrapper.getSigningCertificate();
        CertificateToken signingCertificate = null;
        if (signingCertificateWrapper != null) {
            signingCertificate = DSSUtils.loadCertificate(signingCertificateWrapper.getBinaries());
        }

        return new ValidationResult(indication, subIndication, signingTime, signingCertificate, signingCertificateSource);
    }

    private TrustedCertificateSource buildTrustedCertificateSource(CertificateSource certificateSource) {
        CommonTrustedCertificateSource commonTrustedCertificateSource = new CommonTrustedCertificateSource();
        commonTrustedCertificateSource.importAsTrusted(certificateSource);
        return commonTrustedCertificateSource;
    }

    private ValidationPolicy getTrustedListValidationPolicy() {
        try {
            return ValidationPolicyLoader.fromValidationPolicy(
                    LoTEXmlValidationTask.class.getResourceAsStream(TRUSTED_LIST_VALIDATION_POLICY_LOCATION)).create();
        } catch (Exception e) {
            throw new DSSException("Unable to load the validation policy for trusted list", e);
        }
    }

}
