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
package eu.europa.esig.dss.asic.cades.validation.evidencerecord;

import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.EvidenceRecordWrapper;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.FileDocument;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.spi.x509.CertificateSource;
import eu.europa.esig.dss.spi.x509.CommonTrustedCertificateSource;
import eu.europa.esig.dss.spi.x509.evidencerecord.EvidenceRecord;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ASiCEWithCAdESLevelLTWithCopiedEvidenceRecordChainRenewalValidationTest extends AbstractASiCWithCAdESWithEvidenceRecordTestValidation {

    @Override
    protected DSSDocument getSignedDocument() {
        return new FileDocument("src/test/resources/validation/evidencerecord/cades-lt-with-er-chain-renewed-copied-tst.sce");
    }

    @Override
    protected CertificateSource getTrustedCertificateSource() {
        CommonTrustedCertificateSource trustedCertificateSource = new CommonTrustedCertificateSource();
        trustedCertificateSource.addCertificate(DSSUtils.loadCertificateFromBase64EncodedString("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"));
        trustedCertificateSource.addCertificate(DSSUtils.loadCertificateFromBase64EncodedString("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"));
        trustedCertificateSource.addCertificate(DSSUtils.loadCertificateFromBase64EncodedString("MIIDVzCCAj+gAwIBAgIBATANBgkqhkiG9w0BAQ0FADBNMRAwDgYDVQQDDAdyb290LWNhMRkwFwYDVQQKDBBOb3dpbmEgU29sdXRpb25zMREwDwYDVQQLDAhQS0ktVEVTVDELMAkGA1UEBhMCTFUwHhcNMjIwMTEzMTYwMzM1WhcNMjQwMTEzMTYwMzM1WjBNMRAwDgYDVQQDDAdyb290LWNhMRkwFwYDVQQKDBBOb3dpbmEgU29sdXRpb25zMREwDwYDVQQLDAhQS0ktVEVTVDELMAkGA1UEBhMCTFUwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCRHOEXneXmMs+kosfF6axk1fopOaqpG0CJV9oDY07hPH0lTUKX0WpeHvflF/X0crUWW9xybA0NOKHpmRp68v55R4nRLB5fHUu/bOddi/L/i6RZYrySE/47LfXAUEsvUbewSUdzJU+jKKQOTSmenSZQDC3a7U72WOcCmTtuNh1c1tu76ffWMx3CNoDDSJkucOI6vqmjAf0g2yObRXN/4umk8wOg81eiLV6T1pzCWNkuja07BqIi0tQcf8P9ZcbqnoIrsXZcaRZx4DfUVqQDa6WQY8iWqn28rChRF3XG4XRsW5SdeSU+HOhbQmfc1Zn6Xp94rMg/dc7ozMo/51n1OdrfAgMBAAGjQjBAMA4GA1UdDwEB/wQEAwIBBjAdBgNVHQ4EFgQUek0zqwFuoxiLJwjVOXDg6RFTKT4wDwYDVR0TAQH/BAUwAwEB/zANBgkqhkiG9w0BAQ0FAAOCAQEAhV8vxZzlLmW2FnO660dtQwlVbrpZSIrJY4q8XfYOeJ4lraJ1xV5XtS61lTL+PvBBlTRB8lBuNAtHPnq+qxG06fKfIaGkCcOH62WV/LA9qYnUpWgCWO5c4DUKlyaf9JrQksNUYd23HwJnJTRD7tSe2REpOrB2fUH1b6xvVsCZ8xsCt3SAnkGuu8l2oYtBBgfr/vZ2+k8vdhkQIhIyf7/YkYBLXikVItjZ064Q0oypXfsOd5xyCnYDkBKnMnj6QgPsayWZ/MAAxH+upmiQkmViMTm2GbLtSLzsAe/cU9Ym+9+Ci5pnB+heZ+LoZ6svBKaYWvHbl6yLvpV31XnuK/QPWQ=="));
        return trustedCertificateSource;
    }

    @Override
    protected int getNumberOfExpectedEvidenceScopes() {
        return 4; // signature + 3 data files
    }

    @Override
    protected void checkDetachedEvidenceRecords(List<EvidenceRecord> detachedEvidenceRecords) {
        super.checkDetachedEvidenceRecords(detachedEvidenceRecords);

        assertEquals(2, detachedEvidenceRecords.size());
        assertNotEquals(detachedEvidenceRecords.get(0).getId(), detachedEvidenceRecords.get(1).getId());
    }

    @Override
    protected void checkEvidenceRecords(DiagnosticData diagnosticData) {
        super.checkEvidenceRecords(diagnosticData);

        List<EvidenceRecordWrapper> evidenceRecords = diagnosticData.getEvidenceRecords();
        assertEquals(2, evidenceRecords.size());
        assertNotEquals(evidenceRecords.get(0).getId(), evidenceRecords.get(1).getId());
    }

    @Override
    protected void checkEvidenceRecordTimestamps(DiagnosticData diagnosticData) {
        super.checkEvidenceRecordTimestamps(diagnosticData);

        assertNotEquals(diagnosticData.getEvidenceRecords().get(0).getTimestampIdsList(),
                diagnosticData.getEvidenceRecords().get(1).getTimestampIdsList());
    }

}
