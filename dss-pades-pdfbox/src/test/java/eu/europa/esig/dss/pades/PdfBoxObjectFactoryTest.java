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
package eu.europa.esig.dss.pades;

import eu.europa.esig.dss.pdf.IPdfObjFactory;
import eu.europa.esig.dss.pdf.PDFSignatureService;
import eu.europa.esig.dss.pdf.PdfMemoryUsageSetting;
import eu.europa.esig.dss.pdf.PdfPermissionsChecker;
import eu.europa.esig.dss.pdf.PdfSignatureFieldPositionChecker;
import eu.europa.esig.dss.pdf.ServiceLoaderPdfObjFactory;
import eu.europa.esig.dss.pdf.modifications.PdfDifferencesFinder;
import eu.europa.esig.dss.pdf.modifications.PdfObjectModificationsFinder;
import eu.europa.esig.dss.pdf.pdfbox.PdfBoxDefaultObjectFactory;
import eu.europa.esig.dss.spi.signature.resources.DSSResourcesHandlerBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class PdfBoxObjectFactoryTest {

	private static final String PDFBOX_SIGNATURE_SERVICE = "PdfBoxSignatureService";

	@Test
	void testSystemProperty() {
		IPdfObjFactory ipof = new ServiceLoaderPdfObjFactory();

		PDFSignatureService signatureService = ipof.newPAdESSignatureService();
		assertNotNull(signatureService);
		assertEquals(PDFBOX_SIGNATURE_SERVICE, signatureService.getClass().getSimpleName());
		PDFSignatureService timestampService = ipof.newSignatureTimestampService();
		assertNotNull(timestampService);
		assertEquals(PDFBOX_SIGNATURE_SERVICE, timestampService.getClass().getSimpleName());
	}

	@Test
	void testRuntimeChange() {
		IPdfObjFactory ipof = new EmptyPdfObjectFactory();

		PDFSignatureService signatureService = ipof.newPAdESSignatureService();
		assertNull(signatureService);
		PDFSignatureService timestampService = ipof.newSignatureTimestampService();
		assertNull(timestampService);

		ipof = new PdfBoxDefaultObjectFactory();

		signatureService = ipof.newPAdESSignatureService();
		assertNotNull(signatureService);
		assertEquals(PDFBOX_SIGNATURE_SERVICE, signatureService.getClass().getSimpleName());
	}

	private static class EmptyPdfObjectFactory implements IPdfObjFactory {

		@Override
		public PDFSignatureService newPAdESSignatureService() {
			return null;
		}

		@Override
		public PDFSignatureService newContentTimestampService() {
			return null;
		}

		@Override
		public PDFSignatureService newSignatureTimestampService() {
			return null;
		}

		@Override
		public PDFSignatureService newArchiveTimestampService() {
			return null;
		}

		@Override
		public void setResourcesHandlerBuilder(DSSResourcesHandlerBuilder resourcesHandlerBuilder) {
			// do nothing
		}

		@Override
		public void setPdfDifferencesFinder(PdfDifferencesFinder pdfDifferencesFinder) {
			// do nothing
		}

		@Override
		public void setPdfObjectModificationsFinder(PdfObjectModificationsFinder pdfObjectModificationsFinder) {
			// do nothing
		}

		@Override
		public void setPdfPermissionsChecker(PdfPermissionsChecker pdfPermissionsChecker) {
			// do nothing
		}

		@Override
		public void setPdfSignatureFieldPositionChecker(PdfSignatureFieldPositionChecker pdfSignatureFieldPositionChecker) {
			// do nothing
		}

		@Override
		public void setPdfMemoryUsageSetting(PdfMemoryUsageSetting pdfMemoryUsageSetting) {
			// do nothing
		}		

	}

}
