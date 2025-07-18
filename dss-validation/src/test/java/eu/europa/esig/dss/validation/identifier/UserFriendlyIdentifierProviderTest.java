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
package eu.europa.esig.dss.validation.identifier;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.model.x509.CertificateToken;
import eu.europa.esig.dss.spi.DSSASN1Utils;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.spi.x509.CertificateRef;
import eu.europa.esig.dss.spi.x509.ResponderId;
import eu.europa.esig.dss.spi.x509.SignerIdentifier;
import eu.europa.esig.dss.utils.Utils;
import org.bouncycastle.asn1.x509.IssuerSerial;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.security.auth.x500.X500Principal;
import java.io.File;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserFriendlyIdentifierProviderTest {

    private static CertificateToken certificate;

    @BeforeAll
    static void init() {
        certificate = DSSUtils.loadCertificate(new File("src/test/resources/certificates/CZ.cer"));
    }

    @Test
    void certificateRefTest() {
        CertificateRef certificateRef = new CertificateRef();
        Exception exception = assertThrows(DSSException.class, () -> new UserFriendlyIdentifierProvider().getIdAsString(certificateRef));
        assertEquals("One of [certDigest, publicKeyDigest, issuerInfo, x509Uri] must be defined for a CertificateRef!", exception.getMessage());

        certificateRef.setResponderId(new ResponderId(null, null));
        exception = assertThrows(DSSException.class, () -> new UserFriendlyIdentifierProvider().getIdAsString(certificateRef));
        assertEquals("One of [certDigest, publicKeyDigest, issuerInfo, x509Uri] must be defined for a CertificateRef!", exception.getMessage());

        certificateRef.setResponderId(new ResponderId(new X500Principal("CN=CommonName"), null));
        assertEquals("CERTIFICATE_CommonName", new UserFriendlyIdentifierProvider().getIdAsString(certificateRef));

        certificateRef.setResponderId(new ResponderId(null, DSSUtils.digest(DigestAlgorithm.SHA1, "ski".getBytes())));
        assertEquals("CERTIFICATE", new UserFriendlyIdentifierProvider().getIdAsString(certificateRef));

        certificateRef.setCertificateIdentifier(new SignerIdentifier());
        assertEquals("CERTIFICATE", new UserFriendlyIdentifierProvider().getIdAsString(certificateRef));

        SignerIdentifier signerIdentifier = new SignerIdentifier();
        signerIdentifier.setIssuerName(new X500Principal("CN=IssuerName"));
        certificateRef.setCertificateIdentifier(signerIdentifier);
        assertEquals("CERTIFICATE_ISSUER-IssuerName", new UserFriendlyIdentifierProvider().getIdAsString(certificateRef));

        signerIdentifier.setSerialNumber(BigInteger.valueOf(123456879));
        certificateRef.setCertificateIdentifier(signerIdentifier);
        assertEquals("CERTIFICATE_ISSUER-IssuerName_SERIAL-123456879", new UserFriendlyIdentifierProvider().getIdAsString(certificateRef));

        signerIdentifier.setIssuerName(null);
        certificateRef.setCertificateIdentifier(signerIdentifier);
        assertEquals("CERTIFICATE_SERIAL-123456879", new UserFriendlyIdentifierProvider().getIdAsString(certificateRef));
    }

    @Test
    void userFriendlyIdentifierProviderTest() {
        UserFriendlyIdentifierProvider userFriendlyIdentifierProvider = new UserFriendlyIdentifierProvider();
        String id = userFriendlyIdentifierProvider.getIdAsStringForToken(certificate);
        assertTrue(id.contains("CERTIFICATE"));
        assertTrue(id.contains(DSSUtils.replaceAllNonAlphanumericCharacters(DSSASN1Utils.getSubjectCommonName(certificate), "-")));
        assertFalse(id.contains(DSSUtils.formatDateWithCustomFormat(certificate.getNotBefore(), "yyyyMMdd-hhmm")));
        assertTrue(id.contains(DSSUtils.formatDateWithCustomFormat(certificate.getNotBefore(), "yyyyMMdd-HHmm")));

        userFriendlyIdentifierProvider.setCertificatePrefix("CERT");
        id = userFriendlyIdentifierProvider.getIdAsStringForToken(certificate);
        assertTrue(id.contains("CERT"));
        assertFalse(id.contains("CERTIFICATE"));
        assertTrue(id.contains(DSSUtils.replaceAllNonAlphanumericCharacters(DSSASN1Utils.getSubjectCommonName(certificate), "-")));
        assertTrue(id.contains(DSSUtils.formatDateWithCustomFormat(certificate.getNotBefore(), "yyyyMMdd-HHmm")));

        userFriendlyIdentifierProvider.setDateFormat("yyyy-MM-dd");
        id = userFriendlyIdentifierProvider.getIdAsStringForToken(certificate);
        assertTrue(id.contains("CERT"));
        assertTrue(id.contains(DSSUtils.replaceAllNonAlphanumericCharacters(DSSASN1Utils.getSubjectCommonName(certificate), "-")));
        assertTrue(id.contains(DSSUtils.formatDateWithCustomFormat(certificate.getNotBefore(), "yyyy-MM-dd")));
        assertFalse(id.contains(DSSUtils.formatDateWithCustomFormat(certificate.getNotBefore(), "yyyyMMdd-HHmm")));
    }

    @Test
    void certRefWithSimilarNameTest() {
        UserFriendlyIdentifierProvider userFriendlyIdentifierProvider = new UserFriendlyIdentifierProvider();

        CertificateToken rootCA = DSSUtils.loadCertificateFromBase64EncodedString("MIIDVzCCAj+gAwIBAgIBATANBgkqhkiG9w0BAQ0FADBNMRAwDgYDVQQDDAdyb290LWNhMRkwFwYDVQQKDBBOb3dpbmEgU29sdXRpb25zMREwDwYDVQQLDAhQS0ktVEVTVDELMAkGA1UEBhMCTFUwHhcNMTkxMDE0MDUzODQ0WhcNMjExMDE0MDUzODQ0WjBNMRAwDgYDVQQDDAdyb290LWNhMRkwFwYDVQQKDBBOb3dpbmEgU29sdXRpb25zMREwDwYDVQQLDAhQS0ktVEVTVDELMAkGA1UEBhMCTFUwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDULeex4u8ebUQEfm0V0em+r1AqpR11+84XlxFJyEMDOhCbPOOQI68HVIVWt/GX7naFUoiAPm0IhlAYlq0/amBxg/Q8wW9a6KZc4o3DFgGIBFNEOYHCSwJPQ8EtcSmWZ/+Fgb7+lPffbTCucaOgax5VRFQp6c0fswCmcA9jukxeFCDOz8HNQqBiKvuRmkAj8NmwgQHx/Sndo7YdkalPr2qJ+gBRdg6JANIWuYahxixypqP5He+3pb0ghjWOjCnaIg2K2PQUy6i8YTnagwyGS/FxhXpdLatdUhjUdgkvLn1ZyxqvCbOZsiUx55p2FljR3fSUgt9+VOwC4WzZVLtZHZejAgMBAAGjQjBAMA4GA1UdDwEB/wQEAwIBBjAdBgNVHQ4EFgQUXB8V7Y9AxDcPJ5i36BC54z8jWyowDwYDVR0TAQH/BAUwAwEB/zANBgkqhkiG9w0BAQ0FAAOCAQEAOem7HjwO2cGZlFYSAGby13r8gTkY9Dtq1GbsB+kawdUt6d86tmAw3zNKaPb4qAuZtEeM5tVfW2bj1eN+FzI+T9ZDDEnU50Y9x+DC6q3ZBPk46x0XK+7frnyDkhikRyZ5yss6dqoo8nKgIQUEXdeOky6cK2ybUcGUwzgVn/GalLEcA6zILHp7NAsOxzbwsCEgeWY9CBW5/3GAp/2qo1NNPXukazd9/a5KOeRht2iRjXISUWWJKFHsAJtsmZrul+hfTGorjc6rG+PMNnWK7X5rB/6ZwSVG6naxuoaunIrp99rDuSw9k8pvcyXzofaXDlFYPe1vVyc14Bhtca8A4YI6Jw==");
        String certId = userFriendlyIdentifierProvider.getIdAsString(rootCA);
        assertEquals("CERTIFICATE_root-ca_20191014-0538", certId);

        CertificateRef certificateRef = new CertificateRef();
        IssuerSerial issuerSerial = DSSASN1Utils.getIssuerSerial(Utils.fromBase64("MFYwUaRPME0xEDAOBgNVBAMMB3Jvb3QtY2ExGTAXBgNVBAoMEE5vd2luYSBTb2x1dGlvbnMxETAPBgNVBAsMCFBLSS1URVNUMQswCQYDVQQGEwJMVQIBAg=="));
        SignerIdentifier signerIdentifier = DSSASN1Utils.toSignerIdentifier(issuerSerial);
        certificateRef.setCertificateIdentifier(signerIdentifier);

        // shall avoid adding of a duplicate id suffix
        String certRefId = userFriendlyIdentifierProvider.getIdAsString(certificateRef);
        assertEquals("CERTIFICATE_ISSUER-root-ca_SERIAL-2", certRefId); // issuer name + serial number
    }

}
