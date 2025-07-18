/**
 * DSS - Digital Signature Services
 * Copyright (C) 2015 European Commission, provided under the CEF programme
 * <p>
 * This file is part of the "DSS - Digital Signature Services" project.
 * <p>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package eu.europa.esig.dss.cookbook.example.snippets;

import eu.europa.esig.dss.detailedreport.DetailedReportFacade;
import eu.europa.esig.dss.detailedreport.DetailedReportXmlDefiner;
import eu.europa.esig.dss.detailedreport.jaxb.ObjectFactory;
import eu.europa.esig.dss.detailedreport.jaxb.XmlDetailedReport;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.TokenExtractionStrategy;
import eu.europa.esig.dss.enumerations.ValidationLevel;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.FileDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.simplereport.SimpleReport;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.spi.policy.SignaturePolicyProvider;
import eu.europa.esig.dss.spi.validation.CommonCertificateVerifier;
import eu.europa.esig.dss.spi.validation.executor.CompleteValidationContextExecutor;
import eu.europa.esig.dss.spi.validation.executor.DefaultValidationContextExecutor;
import eu.europa.esig.dss.spi.validation.executor.SkipValidationContextExecutor;
import eu.europa.esig.dss.spi.x509.CertificateSource;
import eu.europa.esig.dss.spi.x509.CommonCertificateSource;
import eu.europa.esig.dss.validation.SignedDocumentValidator;
import eu.europa.esig.dss.validation.executor.signature.DefaultSignatureProcessExecutor;
import eu.europa.esig.dss.validation.identifier.UserFriendlyIdentifierProvider;
import eu.europa.esig.dss.validation.reports.Reports;
import eu.europa.esig.validationreport.jaxb.ValidationReportType;
import jakarta.xml.bind.JAXBContext;
import org.junit.jupiter.api.Test;

import javax.xml.transform.Templates;
import javax.xml.validation.Schema;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class SignedDocumentValidatorTest {

	@Test
	void test() throws Exception {

		// tag::demo[]
		// import java.util.Arrays;
		// import eu.europa.esig.dss.enumerations.TokenExtractionStrategy;
		// import eu.europa.esig.dss.enumerations.ValidationLevel;
		// import eu.europa.esig.dss.model.DSSDocument;
		// import eu.europa.esig.dss.model.FileDocument;
		// import eu.europa.esig.dss.model.InMemoryDocument;
		// import eu.europa.esig.dss.spi.DSSUtils;
		// import eu.europa.esig.dss.spi.x509.CertificateSource;
		// import eu.europa.esig.dss.spi.x509.CommonCertificateSource;
		// import eu.europa.esig.dss.spi.validation.CommonCertificateVerifier;
		// import eu.europa.esig.dss.spi.validation.SignaturePolicyProvider;
		// import eu.europa.esig.dss.validation.SignedDocumentValidator;
		// import eu.europa.esig.dss.validation.executor.signature.DefaultSignatureProcessExecutor;
		// import eu.europa.esig.dss.validation.identifier.UserFriendlyIdentifierProvider;
		// import eu.europa.esig.dss.validation.reports.Reports;
		// import eu.europa.esig.validationreport.jaxb.ValidationReportType;

		// Load document to validate
		DSSDocument document = new FileDocument("src/test/resources/signature-pool/signedXmlXadesLT.xml");
		
		// The method allows instantiation of a related validator for a provided document 
		// independently on its format (the target dss module must be added as dependency)
		SignedDocumentValidator documentValidator = SignedDocumentValidator.fromDocument(document);
		
		// Allows specifying a custom certificate verifier (online or offline)
		documentValidator.setCertificateVerifier(new CommonCertificateVerifier());
		
		// Allows specifying which tokens need to be extracted in the diagnostic data (Base64).
		// Default : NONE)
		documentValidator.setTokenExtractionStrategy(TokenExtractionStrategy.EXTRACT_CERTIFICATES_AND_TIMESTAMPS);

		// tag::demo-signing-certificate[]
		// Allows providing signing certificate(s) in the explicit way, in case if
		// the certificate is not provided in the signature itself (can be used for
		// non-ASiC signatures)
		CertificateSource signingCertificateSource = new CommonCertificateSource();
		signingCertificateSource.addCertificate(DSSUtils.loadCertificateFromBase64EncodedString(
				"MIIC9TCCAd2gAwIBAgIBAjANBgkqhkiG9w0BAQUFADArMQswCQYDVQQGEwJBQTEMMAoGA1UEChMDRFNTMQ4wDAYDVQQDEwVJQ0EgQTAeFw0xMzEyMDIxNzMzMTBaFw0xNTEyMDIxNzMzMTBaMDAxCzAJBgNVBAYTAkFBMQwwCgYDVQQKEwNEU1MxEzARBgNVBAMTCnVzZXIgQSBSU0EwgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBAJUHHAphmSDdQ1t62tppK+dLTANsE2nAj+HCpasS3ohlBsrhteRsvTAbrDyIzCmTYWu/nVI4TGvbzBESwV/QitlkoMLpYFw32MIBf2DLmECzGJ3vm5haw6u8S9quR1h8Vu7QWd+5KMabZuR+j91RiSuoY0xS2ZQxJw1vhvW9hRYjAgMBAAGjgaIwgZ8wCQYDVR0TBAIwADAdBgNVHQ4EFgQU9ESnTWfwg13c3LQZzqqwibY5WVYwUwYDVR0jBEwwSoAUIO1CDsBSUcEoFZxKaWf1PAL1U+uhL6QtMCsxDDAKBgNVBAoTA0RTUzELMAkGA1UEBhMCQUExDjAMBgNVBAMTBVJDQSBBggEBMAsGA1UdDwQEAwIHgDARBgNVHSAECjAIMAYGBFUdIAAwDQYJKoZIhvcNAQEFBQADggEBAGnhhnoyVUhDnr/BSbZ/uWfSuwzFPG+2V9K6WxdIaaXOORFGIdFwGlAwA/Qzpq9snfBxuTkAykxq0uEDhHTj0qXxWRjQ+Dop/DrmccoF/zDvgGusyY1YXaABd/kc3IYt7ns7z3tpiqIz4A7a/UHplBRXfqjyaZurZuJQRaSdxh6CNhdEUiUBxkbb1SdMjuOgjzSDjcDjcegjvDquMKdDetvtu2Qh4ConBBo3fUImwiFRWnbudS5H2HE18ikC7gY/QIuNr7USf1PNyUgcG2g31cMtemj7UTBHZ2V/jPf7ZXqwfnVSaYkNvM3weAI6R3PI0STjdxN6a9qjt9xld40YEdw="));
		documentValidator.setSigningCertificateSource(signingCertificateSource);
		// end::demo-signing-certificate[]

		// Sets the detached contents that were used for the detached signature creation
		documentValidator.setDetachedContents(Arrays.asList(new InMemoryDocument("Hello world!".getBytes())));
		
		// Allows defining a custom Process Executor
		// By default used {@code new DefaultSignatureProcessExecutor()}
		documentValidator.setProcessExecutor(new DefaultSignatureProcessExecutor());
		
		// Sets custom Signature Policy Provider
		documentValidator.setSignaturePolicyProvider(new SignaturePolicyProvider());
		
		// Sets an expected signature validation level
		// The recommended level is ARCHIVAL_DATA (maximal level of the validation)
		// Default : ValidationLevel.ARCHIVAL_DATA
		documentValidator.setValidationLevel(ValidationLevel.ARCHIVAL_DATA);
		
		// Sets if the ETSI validation report must be created 
		// If true, it will become accessible through the method below
		// Default : true
		documentValidator.setEnableEtsiValidationReport(true);

		// Sets the default digest algorithm that will be used for digest calculation
		// of tokens used during the validation process.
		// The values will be used in validation reports.
		// Default : DigestAlgorithm.SHA256
		documentValidator.setDefaultDigestAlgorithm(DigestAlgorithm.SHA512);
		
		// tag::demo-identifier-provider[]
		// Sets provider for token identifiers.
		// For example, UserFriendlyIdentifierProvider will create identifiers in
		// a human-readable form
		// Default : OriginalIdentifierProvider (creates identifiers based on SHA-256 digest)
		documentValidator.setTokenIdentifierProvider(new UserFriendlyIdentifierProvider());
		// end::demo-identifier-provider[]

		// tag::demo-semantics[]
		// Sets if the semantics for Indication / SubIndication must be included in the
		// Simple Report (see table 5 / 6 of the ETSI TS 119 102-1)
		// Default : false
		documentValidator.setIncludeSemantics(true);
		// end::demo-semantics[]

		// Executes the validation process and produces validation reports:
		// Simple report, Detailed report, Diagnostic data and ETSI Validation Report (if enabled)
		Reports reports = documentValidator.validateDocument();
		
		// Returns ETSI Validation Report (if enabled, NULL otherwise)
		ValidationReportType etsiValidationReport = reports.getEtsiValidationReportJaxb();

		// end::demo[]

		// tag::demo-extract-certificates[]
		// Extract base64-encoded certificates on validation (to be incorporated within
		// DiagnosticData)
		documentValidator.setTokenExtractionStrategy(TokenExtractionStrategy.EXTRACT_CERTIFICATES_ONLY);
		// end::demo-extract-certificates[]

		// tag::demo-extract-timestamps[]
		// Extract base64-encoded timestamps on validation (to be incorporated within
		// DiagnosticData)
		documentValidator.setTokenExtractionStrategy(TokenExtractionStrategy.EXTRACT_TIMESTAMPS_ONLY);
		// end::demo-extract-timestamps[]

		// tag::demo-extract-revocation[]
		// Extract base64-encoded revocation data on validation (to be incorporated within
		// DiagnosticData)
		documentValidator.setTokenExtractionStrategy(TokenExtractionStrategy.EXTRACT_REVOCATION_DATA_ONLY);
		// end::demo-extract-revocation[]

		// tag::demo-default-validation-context-executor[]
		documentValidator.setValidationContextExecutor(DefaultValidationContextExecutor.INSTANCE);
		// end::demo-default-validation-context-executor[]

		// tag::demo-complete-validation-context-executor[]
		documentValidator.setValidationContextExecutor(CompleteValidationContextExecutor.INSTANCE);
		// end::demo-complete-validation-context-executor[]

		// tag::demo-skip-validation-context-executor[]
		documentValidator.setValidationContextExecutor(SkipValidationContextExecutor.INSTANCE);
		// end::demo-skip-validation-context-executor[]
		
		assertNotNull(etsiValidationReport);

		// tag::demo-facade[]
		// import eu.europa.esig.dss.detailedreport.DetailedReportFacade;
		// import eu.europa.esig.dss.detailedreport.jaxb.XmlDetailedReport;
		// import eu.europa.esig.dss.validation.reports.Reports;

		Reports completeReports = documentValidator.validateDocument();
		
		DetailedReportFacade detailedReportFacade = DetailedReportFacade.newFacade();

		// Transforms the JAXB object to String (xml content)
		String marshalledDetailedReport = detailedReportFacade.marshall(completeReports.getDetailedReportJaxb());

		// Transforms the String (xml content) to a JAXB Object
		XmlDetailedReport xmlDetailedReport = detailedReportFacade.unmarshall(marshalledDetailedReport);

		// Generates the HTML content for the given Detailed Report (compatible with
		// BootStrap)
		// Similar method is available for PDF generation (requires Apache FOP)
		String htmlDetailedReport = detailedReportFacade.generateHtmlReport(completeReports.getDetailedReportJaxb());

		// end::demo-facade[]
		assertNotNull(xmlDetailedReport);
		assertNotNull(htmlDetailedReport);

		DSSDocument evidenceRecordDocument = null;
		// tag::detached-evidence-records[]
		documentValidator.setDetachedEvidenceRecordDocuments(Collections.singletonList(evidenceRecordDocument));
		// end::detached-evidence-records[]

		// tag::demo-xml-definer[]
		// import eu.europa.esig.dss.detailedreport.DetailedReportFacade;
		// import eu.europa.esig.dss.detailedreport.DetailedReportXmlDefiner;
		// import eu.europa.esig.dss.detailedreport.jaxb.ObjectFactory;
		// import jakarta.xml.bind.JAXBContext;
		// import javax.xml.transform.Templates;
		// import javax.xml.validation.Schema;

		// The JAXB Object Factory
		ObjectFactory objectFactory = DetailedReportXmlDefiner.OBJECT_FACTORY;

		// The JAXBContext (cached)
		JAXBContext jaxbContext = DetailedReportXmlDefiner.getJAXBContext();

		// The XML Schema to validate a XML content (cached)
		Schema schema = DetailedReportXmlDefiner.getSchema();

		// The Templates object with the loaded XML Stylesheet to generate the HTML
		// content from the JAXB Object (cached)
		Templates bootstrap4Templates = DetailedReportXmlDefiner.getHtmlBootstrap4Templates();

		// The Templates object with the loaded XML Stylesheet to generate the PDF
		// content from the JAXB Object (cached)
		Templates pdfTemplates = DetailedReportXmlDefiner.getPdfTemplates();

		// end::demo-xml-definer[]

		String signatureId = reports.getDiagnosticData().getFirstSignatureId();
		Date currentTime = new Date();

		// tag::demo-simple-report-min-and-max-dates[]
		// import eu.europa.esig.dss.enumerations.Indication;
		// import eu.europa.esig.dss.simplereport.SimpleReport;
		// import java.util.Date;

		// Access SimpleReport from Reports object
		SimpleReport simpleReport = reports.getSimpleReport();

		// Extracts the minimum possible signature augmentation time
		Date extensionPeriodMin = simpleReport.getExtensionPeriodMin(signatureId);

		// Extracts the maximum possible signature augmentation time
		Date extensionPeriodMax = simpleReport.getExtensionPeriodMax(signatureId);

		// Perform signature augmentation if applicable.
		// NOTE: Example below is provided for informational purposes only. Custom logic may apply.
		if (Indication.TOTAL_PASSED.equals(simpleReport.getIndication(signatureId))
				&& (extensionPeriodMin == null || extensionPeriodMin.before(currentTime))
				&& extensionPeriodMax.after(currentTime)) {
			// extend signature
		}
		// end::demo-simple-report-min-and-max-dates[]

		assertNotNull(objectFactory);
		assertNotNull(jaxbContext);
		assertNotNull(schema);
		assertNotNull(bootstrap4Templates);
		assertNotNull(pdfTemplates);
	}

}
