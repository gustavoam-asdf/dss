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
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.pades.PAdESSignatureParameters;
import eu.europa.esig.dss.pades.SignatureFieldParameters;
import eu.europa.esig.dss.pades.SignatureImageParameters;
import eu.europa.esig.dss.pades.signature.PAdESService;
import eu.europa.esig.dss.pdf.openpdf.ITextDefaultPdfObjFactory;
import eu.europa.esig.dss.test.PKIFactoryAccess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Multi-widget signatures are not supported by the OpenPDF implementation. This test verifies that a clear
 * {@code UnsupportedOperationException} is raised when additional signature field widgets are requested.
 */
class PAdESMultiWidgetOpenPdfUnsupportedTest extends PKIFactoryAccess {

	private PAdESService service;
	private PAdESSignatureParameters signatureParameters;
	private DSSDocument documentToSign;

	@BeforeEach
	void init() {
		documentToSign = new InMemoryDocument(getClass().getResourceAsStream("/sample.pdf"));

		signatureParameters = new PAdESSignatureParameters();
		signatureParameters.bLevel().setSigningDate(new Date());
		signatureParameters.setSigningCertificate(getSigningCert());
		signatureParameters.setCertificateChain(getCertificateChain());
		signatureParameters.setSignatureLevel(SignatureLevel.PAdES_BASELINE_B);

		service = new PAdESService(getCompleteCertificateVerifier());
		service.setPdfObjFactory(new ITextDefaultPdfObjFactory());

		SignatureImageParameters imageParameters = new SignatureImageParameters();
		imageParameters.setImage(new InMemoryDocument(getClass().getResourceAsStream("/small-red.jpg"), "small-red.jpg", MimeTypeEnum.JPEG));

		SignatureFieldParameters primary = new SignatureFieldParameters();
		primary.setOriginX(50);
		primary.setOriginY(50);
		primary.setWidth(100);
		primary.setHeight(50);
		imageParameters.setFieldParameters(primary);

		SignatureFieldParameters additional = new SignatureFieldParameters();
		additional.setOriginX(50);
		additional.setOriginY(200);
		additional.setWidth(100);
		additional.setHeight(50);
		imageParameters.setAdditionalFieldParameters(Collections.singletonList(additional));

		signatureParameters.setImageParameters(imageParameters);
	}

	@Test
	void additionalWidgetsUnsupportedTest() {
		Exception exception = assertThrows(UnsupportedOperationException.class,
				() -> service.getDataToSign(documentToSign, signatureParameters));
		assertTrue(exception.getMessage().contains("Multi-widget signatures"), exception.getMessage());
	}

	@Override
	protected String getSigningAlias() {
		return GOOD_USER;
	}

}
