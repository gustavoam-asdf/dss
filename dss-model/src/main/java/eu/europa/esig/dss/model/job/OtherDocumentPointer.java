package eu.europa.esig.dss.model.job;

import eu.europa.esig.dss.model.x509.CertificateToken;

import java.util.List;

public interface OtherDocumentPointer {

    /**
     * Gets location url
     *
     * @return {@link String}
     */
    String getLocation();

    /**
     * Gets a list of ServiceDigitalIdentity X509 certificates
     *
     * @return a list of {@link CertificateToken}s
     */
    List<CertificateToken> getSdiCertificates();

}
