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
package eu.europa.esig.dss.pades.signature.visible;

import eu.europa.esig.dss.alert.exception.AlertException;
import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.enumerations.MimeTypeEnum;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.model.SignatureValue;
import eu.europa.esig.dss.model.ToBeSigned;
import eu.europa.esig.dss.model.x509.CertificateToken;
import eu.europa.esig.dss.pades.PAdESSignatureParameters;
import eu.europa.esig.dss.pades.SignatureFieldParameters;
import eu.europa.esig.dss.pades.SignatureImageParameters;
import eu.europa.esig.dss.pades.signature.PAdESService;
import eu.europa.esig.dss.pades.validation.suite.AbstractPAdESTestValidation;
import eu.europa.esig.dss.pki.model.CertEntity;
import eu.europa.esig.dss.test.pki.CertEntitySignatureTokenConnection;
import eu.europa.esig.dss.validation.reports.Reports;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests the creation of a multi-widget PAdES signature with the PDFBox implementation: a single signature whose
 * visual appearance is displayed in several positions and/or pages at once through several widgets sharing the
 * same signature field.
 */
class PAdESMultiWidgetSignatureTest extends AbstractPAdESTestValidation {

	private PAdESService service;
	private PAdESSignatureParameters signatureParameters;
	private DSSDocument documentToSign;
	private DSSDocument image;

	@BeforeEach
	void init() {
		documentToSign = new InMemoryDocument(getClass().getResourceAsStream("/empty-two-pages.pdf"));

		signatureParameters = new PAdESSignatureParameters();
		signatureParameters.bLevel().setSigningDate(new Date());
		signatureParameters.setSigningCertificate(getSigningCert());
		signatureParameters.setCertificateChain(getCertificateChain());
		signatureParameters.setSignatureLevel(SignatureLevel.PAdES_BASELINE_B);

		service = new PAdESService(getCompleteCertificateVerifier());

		image = new InMemoryDocument(getClass().getResourceAsStream("/small-red.jpg"), "small-red.jpg", MimeTypeEnum.JPEG);
	}

	@Test
	void multiWidgetAcrossPositionsAndPagesTest() throws IOException {
		SignatureImageParameters imageParameters = new SignatureImageParameters();
		imageParameters.setImage(image);
		imageParameters.setFieldParameters(fieldParameters(1, 50, 50));
		imageParameters.setAdditionalFieldParameters(Arrays.asList(
				fieldParameters(1, 50, 200), // additional widget on the same page
				fieldParameters(2, 50, 50))); // additional widget on another page
		signatureParameters.setImageParameters(imageParameters);

		DSSDocument signedDocument = sign();
		assertNotNull(signedDocument);

		Reports reports = verify(signedDocument);
		DiagnosticData diagnosticData = reports.getDiagnosticData();
		// a multi-widget signature remains a single signature
		assertEquals(1, diagnosticData.getSignatures().size());
		assertTrue(diagnosticData.isBLevelTechnicallyValid(diagnosticData.getFirstSignatureId()));
	}

	@Test
	void widgetsOverlapTest() {
		SignatureImageParameters imageParameters = new SignatureImageParameters();
		imageParameters.setImage(image);
		imageParameters.setFieldParameters(fieldParameters(1, 50, 50));
		// additional widget overlapping the primary one on the same page
		imageParameters.setAdditionalFieldParameters(Collections.singletonList(fieldParameters(1, 60, 60)));
		signatureParameters.setImageParameters(imageParameters);

		Exception exception = assertThrows(AlertException.class, this::sign);
		assertEquals("Two signature field widgets of the same signature overlap!", exception.getMessage());
	}

	@Test
	void widgetOutsidePageTest() {
		SignatureImageParameters imageParameters = new SignatureImageParameters();
		imageParameters.setImage(image);
		imageParameters.setFieldParameters(fieldParameters(1, 50, 50));
		// additional widget placed far outside the page dimensions
		imageParameters.setAdditionalFieldParameters(Collections.singletonList(fieldParameters(1, 5000, 5000)));
		signatureParameters.setImageParameters(imageParameters);

		Exception exception = assertThrows(AlertException.class, this::sign);
		assertTrue(exception.getMessage().contains("outside the page dimensions"), exception.getMessage());
	}

	@Test
	void doubleMultiWidgetSignatureTest() throws IOException {
		// first multi-widget signature
		SignatureImageParameters imageParameters = new SignatureImageParameters();
		imageParameters.setImage(image);
		imageParameters.setFieldParameters(fieldParameters(1, 50, 50));
		imageParameters.setAdditionalFieldParameters(Arrays.asList(
				fieldParameters(1, 50, 200),
				fieldParameters(2, 50, 50)));
		signatureParameters.setImageParameters(imageParameters);

		DSSDocument firstSignedDocument = sign();
		assertNotNull(firstSignedDocument);

		// second multi-widget signature over the already (multi-widget) signed document, at non-overlapping positions
		PAdESSignatureParameters secondSignatureParameters = new PAdESSignatureParameters();
		secondSignatureParameters.bLevel().setSigningDate(new Date());
		secondSignatureParameters.setSigningCertificate(getSigningCert());
		secondSignatureParameters.setCertificateChain(getCertificateChain());
		secondSignatureParameters.setSignatureLevel(SignatureLevel.PAdES_BASELINE_B);

		SignatureImageParameters secondImageParameters = new SignatureImageParameters();
		secondImageParameters.setImage(image);
		secondImageParameters.setFieldParameters(fieldParameters(1, 300, 50));
		secondImageParameters.setAdditionalFieldParameters(Collections.singletonList(fieldParameters(2, 300, 50)));
		secondSignatureParameters.setImageParameters(secondImageParameters);

		DSSDocument secondSignedDocument = sign(firstSignedDocument, secondSignatureParameters);
		assertNotNull(secondSignedDocument);

		Reports reports = verify(secondSignedDocument);
		DiagnosticData diagnosticData = reports.getDiagnosticData();
		// two independent signatures, both valid
		assertEquals(2, diagnosticData.getSignatures().size());
		diagnosticData.getSignatures().forEach(signature ->
				assertTrue(diagnosticData.isBLevelTechnicallyValid(signature.getId())));
	}

	@Test
	void singleThenMultiWidgetDifferentSignersTest() throws IOException {
		// first signer: single-widget signature
		signatureParameters.setImageParameters(imageParameters(fieldParameters(1, 50, 50), null));
		DSSDocument firstSignedDocument = sign();
		assertNotNull(firstSignedDocument);

		// second, different signer: multi-widget signature at non-overlapping positions
		PAdESSignatureParameters secondSignatureParameters = signatureParameters(ECDSA_USER);
		secondSignatureParameters.setImageParameters(imageParameters(fieldParameters(1, 300, 50),
				Collections.singletonList(fieldParameters(2, 50, 50))));
		DSSDocument secondSignedDocument = signWith(firstSignedDocument, secondSignatureParameters, ECDSA_USER);

		assertTwoValidSignatures(secondSignedDocument);
	}

	@Test
	void multiThenSingleWidgetDifferentSignersTest() throws IOException {
		// first signer: multi-widget signature
		signatureParameters.setImageParameters(imageParameters(fieldParameters(1, 50, 50),
				Arrays.asList(fieldParameters(1, 50, 200), fieldParameters(2, 50, 50))));
		DSSDocument firstSignedDocument = sign();
		assertNotNull(firstSignedDocument);

		// second, different signer: single-widget signature at a non-overlapping position
		PAdESSignatureParameters secondSignatureParameters = signatureParameters(ECDSA_USER);
		secondSignatureParameters.setImageParameters(imageParameters(fieldParameters(1, 300, 50), null));
		DSSDocument secondSignedDocument = signWith(firstSignedDocument, secondSignatureParameters, ECDSA_USER);

		assertTwoValidSignatures(secondSignedDocument);
	}

	private void assertTwoValidSignatures(DSSDocument signedDocument) {
		Reports reports = verify(signedDocument);
		DiagnosticData diagnosticData = reports.getDiagnosticData();
		assertEquals(2, diagnosticData.getSignatures().size());
		diagnosticData.getSignatures().forEach(signature ->
				assertTrue(diagnosticData.isBLevelTechnicallyValid(signature.getId())));
	}

	@Test
	void noAdditionalWidgetRemainsSingleWidgetTest() throws IOException {
		SignatureImageParameters imageParameters = new SignatureImageParameters();
		imageParameters.setImage(image);
		imageParameters.setFieldParameters(fieldParameters(1, 50, 50));
		signatureParameters.setImageParameters(imageParameters);

		DSSDocument signedDocument = sign();
		assertNotNull(signedDocument);

		Reports reports = verify(signedDocument);
		DiagnosticData diagnosticData = reports.getDiagnosticData();
		assertEquals(1, diagnosticData.getSignatures().size());
		assertTrue(diagnosticData.isBLevelTechnicallyValid(diagnosticData.getFirstSignatureId()));
	}

	private SignatureImageParameters imageParameters(SignatureFieldParameters primary,
													 List<SignatureFieldParameters> additional) {
		SignatureImageParameters imageParameters = new SignatureImageParameters();
		imageParameters.setImage(image);
		imageParameters.setFieldParameters(primary);
		if (additional != null) {
			imageParameters.setAdditionalFieldParameters(additional);
		}
		return imageParameters;
	}

	private PAdESSignatureParameters signatureParameters(String signerAlias) {
		CertEntity signer = getCertEntity(signerAlias);
		PAdESSignatureParameters parameters = new PAdESSignatureParameters();
		parameters.bLevel().setSigningDate(new Date());
		parameters.setSigningCertificate(signer.getCertificateToken());
		parameters.setCertificateChain(signer.getCertificateChain().toArray(new CertificateToken[0]));
		parameters.setSignatureLevel(SignatureLevel.PAdES_BASELINE_B);
		return parameters;
	}

	private SignatureFieldParameters fieldParameters(int page, float originX, float originY) {
		SignatureFieldParameters fieldParameters = new SignatureFieldParameters();
		fieldParameters.setPage(page);
		fieldParameters.setOriginX(originX);
		fieldParameters.setOriginY(originY);
		fieldParameters.setWidth(100);
		fieldParameters.setHeight(50);
		return fieldParameters;
	}

	private DSSDocument sign() throws IOException {
		return sign(documentToSign, signatureParameters);
	}

	private DSSDocument sign(DSSDocument document, PAdESSignatureParameters parameters) throws IOException {
		ToBeSigned dataToSign = service.getDataToSign(document, parameters);
		SignatureValue signatureValue = getToken().sign(dataToSign, parameters.getDigestAlgorithm(), getPrivateKeyEntry());
		return service.signDocument(document, parameters, signatureValue);
	}

	private DSSDocument signWith(DSSDocument document, PAdESSignatureParameters parameters, String signerAlias) throws IOException {
		CertEntitySignatureTokenConnection token = new CertEntitySignatureTokenConnection(getCertEntity(signerAlias));
		ToBeSigned dataToSign = service.getDataToSign(document, parameters);
		SignatureValue signatureValue = token.sign(dataToSign, parameters.getDigestAlgorithm(), token.getKeys().iterator().next());
		return service.signDocument(document, parameters, signatureValue);
	}

	@Override
	protected void checkPdfRevision(DiagnosticData diagnosticData) {
		// skip (different tests)
	}

	@Override
	public void validate() {
		// do nothing
	}

	@Override
	protected DSSDocument getSignedDocument() {
		return null;
	}

	@Override
	protected String getSigningAlias() {
		return GOOD_USER;
	}

}
