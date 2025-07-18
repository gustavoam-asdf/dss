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
package eu.europa.esig.dss.cades.validation;

import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.SignatureWrapper;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.spi.client.http.IgnoreDataLoader;
import eu.europa.esig.dss.spi.policy.SignaturePolicyProvider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CAdESWithSPUserNoticeTest extends AbstractCAdESTestValidation {

    @Override
    protected DSSDocument getSignedDocument() {
        return new InMemoryDocument(CAdESWithSPUserNoticeTest.class.getResourceAsStream("/validation/Signature-C-B-B-8.p7m"));
    }

    @Override
    protected SignaturePolicyProvider getSignaturePolicyProvider() {
        SignaturePolicyProvider signaturePolicyProvider = new SignaturePolicyProvider();
        signaturePolicyProvider.setDataLoader(new IgnoreDataLoader());
        return signaturePolicyProvider;
    }

    @Override
    protected void checkSignaturePolicyIdentifier(DiagnosticData diagnosticData) {
        super.checkSignaturePolicyIdentifier(diagnosticData);

        SignatureWrapper signature = diagnosticData.getSignatureById(diagnosticData.getFirstSignatureId());
        assertEquals("1.2.3.4.5.1", signature.getPolicyId());
        assertEquals("../../Data/TARGET-SIGPOL-ETSI1.der", signature.getPolicyUrl());
        assertNotNull(signature.getPolicyUserNotice());
        assertEquals("../../Data/TARGET-SIGPOL-ETSI1.der", signature.getPolicyUserNotice().getExplicitText());
    }

}
