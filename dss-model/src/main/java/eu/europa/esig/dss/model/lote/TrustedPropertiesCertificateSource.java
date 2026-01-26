package eu.europa.esig.dss.model.lote;

import eu.europa.esig.dss.model.tsl.CertificateTrustTime;
import eu.europa.esig.dss.model.tsl.TrustedCertificateSourceWithTime;
import eu.europa.esig.dss.model.x509.CertificateToken;

import java.util.List;
import java.util.Map;

/**
 * Contains trusted certificates and related trusted properties
 *
 */
public interface TrustedPropertiesCertificateSource extends TrustedCertificateSourceWithTime {

    /**
     * Gets TL Validation job summary
     *
     * @return {@link LoTEValidationJobSummary}
     */
    LoTEValidationJobSummary getSummary();

    /**
     * Sets TL Validation job summary
     *
     * @param summary {@link LoTEValidationJobSummary}
     */
    void setSummary(LoTEValidationJobSummary summary);

    /**
     * Returns TrustedProperties for the given certificate, when applicable
     *
     * @param token {@link CertificateToken}
     * @return a list of {@link TrustedProperties}
     */
    List<TrustedProperties> getTrustedProperties(CertificateToken token);

    /**
     * The method allows to fill the CertificateSource
     *
     * @param trustedPropertiesByCerts map between {@link CertificateToken}s and a list of {@link TrustedProperties}
     */
    void setTrustedPropertiesByCertificates(final Map<CertificateToken, List<TrustedProperties>> trustedPropertiesByCerts);

    /**
     * The method allows to fill the CertificateSource with trusted time periods
     *
     * @param trustedTimeByCertificate map between {@link CertificateToken}s and a list of {@link CertificateTrustTime}s
     */
    void setTrustedTimeByCertificates(final Map<CertificateToken, List<CertificateTrustTime>> trustedTimeByCertificate);

}
