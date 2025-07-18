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
package eu.europa.esig.dss.asic.xades.validation.evidencerecord;

import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.EvidenceRecordWrapper;
import eu.europa.esig.dss.diagnostic.SignatureWrapper;
import eu.europa.esig.dss.diagnostic.TimestampWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlDigestMatcher;
import eu.europa.esig.dss.enumerations.DigestMatcherType;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.FileDocument;
import eu.europa.esig.dss.model.ReferenceValidation;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.spi.x509.CertificateSource;
import eu.europa.esig.dss.spi.x509.CommonTrustedCertificateSource;
import eu.europa.esig.dss.spi.x509.evidencerecord.EvidenceRecord;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.validationreport.jaxb.ValidationReportType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ASiCSWithXAdESLevelLTAEvidenceRecordHashTreeValidationTest extends AbstractASiCWithXAdESWithEvidenceRecordTestValidation {

    @Override
    protected DSSDocument getSignedDocument() {
        return new FileDocument("src/test/resources/validation/evidencerecord/xades-lta-with-er-hashtree.scs");
    }

    @Override
    protected CertificateSource getTrustedCertificateSource() {
        CommonTrustedCertificateSource trustedCertificateSource = new CommonTrustedCertificateSource();
        trustedCertificateSource.addCertificate(DSSUtils.loadCertificateFromBase64EncodedString("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"));
        trustedCertificateSource.addCertificate(DSSUtils.loadCertificateFromBase64EncodedString("MIIFgTCCA2mgAwIBAgIUGTcivXeyQZ1vqc77RH9+3LWD7oowDQYJKoZIhvcNAQELBQAwUTELMAkGA1UEBhMCQkUxFTATBgNVBAoMDERTUyBURVNUIFBLSTERMA8GA1UECwwIVEVTVCBQS0kxGDAWBgNVBAMMD3NlbGYtc2lnbmVkLXRzYTAeFw0yMDEyMTYwNzAzMzZaFw0zMDEyMTQwNzAzMzZaMFExCzAJBgNVBAYTAkJFMRUwEwYDVQQKDAxEU1MgVEVTVCBQS0kxETAPBgNVBAsMCFRFU1QgUEtJMRgwFgYDVQQDDA9zZWxmLXNpZ25lZC10c2EwggIiMA0GCSqGSIb3DQEBAQUAA4ICDwAwggIKAoICAQDG01QyOzCXb7u/84JqrQJI4RpqPrVSNFn0QbKvFfufFjIhesqgbVrkYFUmQZNhIEKvXW/BPUdMLMdXgQsElV1nQrMGg8875GFBD8HCui+bAAIp72Hn390UmD2Z7KFV9+3MEpLqTuJwaKwQLcgci0fUjizbzkOknPI/oS678dGR4vO2cGDWf+h2uXE2vNdulCgC7K7ZVoeUJ4FkzNUrhhvWQTPGSrlrmF2kDmzMFMJniKaLrAcHfFphsOUpWKqGxPR24svCDbuifXxge4e4ndE570yl+qaKODNbU2afOLMA/UAkzsbBR9otwGTTvnIymjZPikhvimBSAosXkU6iY/3P4+buwCQKgA2pV/8tVEG3u/z+U77piR4QoT+0zEh+aFaKJoVby3Q5Bb0NlflIKpV7skf30WSe/7aKFR1dC2YUcenb3RUnQ2y/nFKTwm1NFkAXHy8Lt8Wz+q0NiBUrzW7JJQMZ/hmESMuj9yZ+stmLJUvco0gPA1SH4sepE2zS3HJQEitxzBHPiaDrIb2aboemKBT1J0MQL+ej/ReBsobJixuqsZ4rrTHptcSFLvXF+tjcYh+5jk8uNFKcZPV5fl99jw3pB7E19CE0hYQKQYZ/LxLaQSFx+58n3Gp2zHRurfz+7I0JYytLdd7Cnzg+RbTzTdSgpiiyhwuiBx1O4SAUMQIDAQABo1EwTzAJBgNVHRMEAjAAMAsGA1UdDwQEAwIHgDAWBgNVHSUBAf8EDDAKBggrBgEFBQcDCDAdBgNVHQ4EFgQUx/Bc6x+I3yHJnKMTunz62C1TlogwDQYJKoZIhvcNAQELBQADggIBAHLtUgoBdF0VNmgZ4p6FwmxLhCwZZ/RKZ5WrwnIQMt/AmPr4V6lvftyXSfRWC/UfWhrFnQnrJakublbp1stpctrA9v41qtaJDbFWD1nyXL/jYpbc7TPdMTUuh+S4YmrdlUSy7ncTgKaRwAVav8N0lr0AELSLpBd3lANBk0ScPB4OvzYd0g7rUK+Tsj8BOXYR38gLAojreBeRRFIhuXkgCL+i/OOZVl0dNMkqgYtbP3bPLYUm5Rkk+LX/iWETCudXcBLHncN4hd2s2yBmMULTG4lYaDFwL/VWOBrLSoa3tqIwpl8RFKBjMU8z1V10Ua+YtW9DMe6xNyZrvJgCR3ZS5kb1XLklFrtsOLMnLhh7qq1nwONpUCVEO0ZiqCl3M2qMDx14jWgtZE5xSUSGio+H1sjQj9imCWLuhcRnTwEpYEhziYhshrvC3v6QGejDl3RjxcWZG/RSo09O5DMjWoBFVLSZLFjShNt4zwuWKH0gtQEeW3Ya8WdgB5Ylrg1cGzxLKWxJY3u8c56IogklS6s2U10PBnsPVF7FyySF52Bg9OhOmSier9iyerngUz2ONxWRjvOl38AAh9e9Ko55LAtX15ak2jPnfYUxyxkGVdLz0ECWZ8jC7FQnSKxpZ4hwSClMOf+5+I8dChoDoGA/kaAWHh/TsP1DQwjeJTdQrtXUtAUg"));
        return trustedCertificateSource;
    }

    @Override
    protected void checkDetachedEvidenceRecords(List<EvidenceRecord> detachedEvidenceRecords) {
        assertEquals(1, detachedEvidenceRecords.size());
        EvidenceRecord evidenceRecord = detachedEvidenceRecords.get(0);

        List<ReferenceValidation> referenceValidation = evidenceRecord.getReferenceValidation();
        assertEquals(1, referenceValidation.size());

        assertEquals(DigestMatcherType.EVIDENCE_RECORD_ARCHIVE_OBJECT, referenceValidation.get(0).getType());
        assertTrue(referenceValidation.get(0).isFound());
        assertFalse(referenceValidation.get(0).isIntact());
    }

    @Override
    protected void checkEvidenceRecordDigestMatchers(DiagnosticData diagnosticData) {
        List<EvidenceRecordWrapper> evidenceRecords = diagnosticData.getEvidenceRecords();
        assertEquals(1, evidenceRecords.size());

        EvidenceRecordWrapper evidenceRecordWrapper = evidenceRecords.get(0);
        List<XmlDigestMatcher> digestMatchers = evidenceRecordWrapper.getDigestMatchers();
        assertEquals(1, digestMatchers.size());

        assertEquals(DigestMatcherType.EVIDENCE_RECORD_ARCHIVE_OBJECT, digestMatchers.get(0).getType());
        assertTrue(digestMatchers.get(0).isDataFound());
        assertFalse(digestMatchers.get(0).isDataIntact());
    }

    @Override
    protected void checkEvidenceRecordScopes(DiagnosticData diagnosticData) {
        List<EvidenceRecordWrapper> evidenceRecords = diagnosticData.getEvidenceRecords();
        assertEquals(1, evidenceRecords.size());

        EvidenceRecordWrapper evidenceRecordWrapper = evidenceRecords.get(0);
        assertEquals(0, evidenceRecordWrapper.getEvidenceRecordScopes().size());
    }

    @Override
    protected void checkEvidenceRecordTimestampedReferences(DiagnosticData diagnosticData) {
        List<EvidenceRecordWrapper> evidenceRecords = diagnosticData.getEvidenceRecords();
        EvidenceRecordWrapper evidenceRecordWrapper = evidenceRecords.get(0);
        assertFalse(Utils.isCollectionNotEmpty(evidenceRecordWrapper.getCoveredObjects())); // invalid ref
        assertFalse(Utils.isCollectionNotEmpty(evidenceRecordWrapper.getCoveredSignedData()));
        assertFalse(Utils.isCollectionNotEmpty(evidenceRecordWrapper.getCoveredSignatures()));
        assertFalse(Utils.isCollectionNotEmpty(evidenceRecordWrapper.getCoveredTimestamps()));
        assertFalse(Utils.isCollectionNotEmpty(evidenceRecordWrapper.getCoveredCertificates()));
        assertFalse(Utils.isCollectionNotEmpty(evidenceRecordWrapper.getCoveredRevocations()));
        assertFalse(Utils.isCollectionNotEmpty(evidenceRecordWrapper.getCoveredEvidenceRecords()));
    }

    @Override
    protected void checkEvidenceRecordCoverage(DiagnosticData diagnosticData, SignatureWrapper signature) {
        assertFalse(Utils.isCollectionNotEmpty(signature.getEvidenceRecords()));
    }

    @Override
    protected void checkEvidenceRecordTimestamps(DiagnosticData diagnosticData) {
        List<EvidenceRecordWrapper> evidenceRecords = diagnosticData.getEvidenceRecords();
        assertEquals(1, evidenceRecords.size());

        EvidenceRecordWrapper evidenceRecordWrapper = evidenceRecords.get(0);
        List<TimestampWrapper> timestampList = evidenceRecordWrapper.getTimestampList();
        assertEquals(1, timestampList.size());

        TimestampWrapper timestampWrapper = timestampList.get(0);
        assertTrue(timestampWrapper.isMessageImprintDataFound());
        assertTrue(timestampWrapper.isMessageImprintDataIntact());
        assertTrue(timestampWrapper.isSignatureIntact());
        assertTrue(timestampWrapper.isSignatureValid());
        assertEquals(0, timestampWrapper.getTimestampScopes().size());
    }

    @Override
    protected void verifyETSIValidationReport(ValidationReportType etsiValidationReportJaxb) {
        // skip
    }

    @Override
    protected int getNumberOfExpectedEvidenceScopes() {
        return 1;
    }

}
