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
package eu.europa.esig.dss.cades.validation.dss1419;

import eu.europa.esig.dss.cades.validation.AbstractCAdESTestValidation;
import eu.europa.esig.dss.cades.validation.CAdESSignature;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.EncryptionAlgorithm;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DSS1419Sha3224RsaTest extends AbstractCAdESTestValidation {

	@Override
	protected DSSDocument getSignedDocument() {
		return new InMemoryDocument(DSS1419Sha3224RsaTest.class.getResourceAsStream("/validation/dss-1419/CAdES-BpB-att-SHA3_224-SHA3_224withRSA.p7m"));
	}
	
	@Override
	protected void checkAdvancedSignatures(List<AdvancedSignature> signatures) {
		super.checkAdvancedSignatures(signatures);
		
		assertEquals(1, signatures.size());
		CAdESSignature cades = (CAdESSignature) signatures.get(0);

		Set<DigestAlgorithm> messageDigestAlgorithms = cades.getMessageDigestAlgorithms();
		assertEquals(1, signatures.size());
		assertEquals(DigestAlgorithm.SHA3_224, messageDigestAlgorithms.iterator().next());
		assertNotNull(cades.getMessageDigestValue());

		assertEquals(EncryptionAlgorithm.RSA, cades.getEncryptionAlgorithm());
		assertEquals(DigestAlgorithm.SHA3_224, cades.getDigestAlgorithm());
	}

}
