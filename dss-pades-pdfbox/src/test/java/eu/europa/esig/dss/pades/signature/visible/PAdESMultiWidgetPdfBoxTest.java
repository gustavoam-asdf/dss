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

import eu.europa.esig.dss.enumerations.MimeTypeEnum;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.VisualSignatureRotation;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.model.SignatureValue;
import eu.europa.esig.dss.model.ToBeSigned;
import eu.europa.esig.dss.pades.PAdESSignatureParameters;
import eu.europa.esig.dss.pades.SignatureFieldParameters;
import eu.europa.esig.dss.pades.SignatureImageParameters;
import eu.europa.esig.dss.pades.signature.PAdESService;
import eu.europa.esig.dss.pdf.IPdfObjFactory;
import eu.europa.esig.dss.pdf.pdfbox.PdfBoxDefaultObjectFactory;
import eu.europa.esig.dss.pdf.pdfbox.PdfBoxNativeObjectFactory;
import eu.europa.esig.dss.test.PKIFactoryAccess;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.pdmodel.interactive.form.PDSignatureField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verifies the PDF structure produced by a multi-widget PAdES signature with the PDFBox implementation:
 * a single signature field and signature dictionary, with one widget per configured position/page.
 */
class PAdESMultiWidgetPdfBoxTest extends PKIFactoryAccess {

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

		image = new InMemoryDocument(getClass().getResourceAsStream("/small-red.jpg"), "small-red.jpg", MimeTypeEnum.JPEG);
	}

	@Test
	void defaultDrawerTest() throws IOException {
		assertMultiWidgetStructure(new PdfBoxDefaultObjectFactory());
	}

	@Test
	void nativeDrawerTest() throws IOException {
		assertMultiWidgetStructure(new PdfBoxNativeObjectFactory());
	}

	private void assertMultiWidgetStructure(IPdfObjFactory pdfObjFactory) throws IOException {
		SignatureImageParameters imageParameters = new SignatureImageParameters();
		imageParameters.setImage(image);
		imageParameters.setFieldParameters(fieldParameters(1, 50, 50, null));
		SignatureFieldParameters rotated = fieldParameters(1, 50, 250, VisualSignatureRotation.ROTATE_90);
		imageParameters.setAdditionalFieldParameters(Arrays.asList(
				rotated, // additional widget on the same page, rotated
				fieldParameters(2, 50, 50, null))); // additional widget on another page
		signatureParameters.setImageParameters(imageParameters);

		DSSDocument signedDocument = sign(pdfObjFactory);
		assertNotNull(signedDocument);

		try (InputStream is = signedDocument.openStream();
			 RandomAccessRead rar = new RandomAccessReadBuffer(is);
			 PDDocument pdDocument = Loader.loadPDF(rar)) {

			// a single signature dictionary and a single signature field
			List<PDSignature> signatures = pdDocument.getSignatureDictionaries();
			assertEquals(1, signatures.size());

			List<PDSignatureField> signatureFields = pdDocument.getSignatureFields();
			assertEquals(1, signatureFields.size());

			PDSignatureField signatureField = signatureFields.get(0);
			List<PDAnnotationWidget> widgets = signatureField.getWidgets();
			// one widget per configured position (primary + 2 additional)
			assertEquals(3, widgets.size());

			Set<Integer> pageIndexes = new HashSet<>();
			for (PDAnnotationWidget widget : widgets) {
				assertNotNull(widget.getAppearance(), "each widget shall have an appearance dictionary");
				assertNotNull(widget.getAppearance().getNormalAppearance(), "each widget shall have a normal appearance");
				assertNotNull(widget.getPage(), "each widget shall reference a page");
				pageIndexes.add(pdDocument.getPages().indexOf(widget.getPage()));
			}
			// widgets are spread over both pages
			assertTrue(pageIndexes.contains(0), "a widget shall be present on the first page");
			assertTrue(pageIndexes.contains(1), "a widget shall be present on the second page");
		}
	}

	private SignatureFieldParameters fieldParameters(int page, float originX, float originY, VisualSignatureRotation rotation) {
		SignatureFieldParameters fieldParameters = new SignatureFieldParameters();
		fieldParameters.setPage(page);
		fieldParameters.setOriginX(originX);
		fieldParameters.setOriginY(originY);
		fieldParameters.setWidth(100);
		fieldParameters.setHeight(50);
		fieldParameters.setRotation(rotation);
		return fieldParameters;
	}

	private DSSDocument sign(IPdfObjFactory pdfObjFactory) throws IOException {
		PAdESService service = new PAdESService(getCompleteCertificateVerifier());
		service.setPdfObjFactory(pdfObjFactory);
		ToBeSigned dataToSign = service.getDataToSign(documentToSign, signatureParameters);
		SignatureValue signatureValue = getToken().sign(dataToSign, signatureParameters.getDigestAlgorithm(), getPrivateKeyEntry());
		return service.signDocument(documentToSign, signatureParameters, signatureValue);
	}

	@Override
	protected String getSigningAlias() {
		return GOOD_USER;
	}

}
