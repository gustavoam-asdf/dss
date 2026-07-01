package eu.europa.esig.dss.validation.job.validation;

import eu.europa.esig.dss.enumerations.Indication;
import eu.europa.esig.dss.enumerations.SubIndication;
import eu.europa.esig.dss.model.x509.CertificateToken;
import eu.europa.esig.dss.validation.job.cache.CachedResult;

import java.util.Date;
import java.util.List;

/**
 * Provides an interface for extraction of information about validation task result
 *
 */
public interface ValidationResult extends CachedResult {

    /**
     * Gets validation Indication
     *
     * @return {@link Indication}
     */
    Indication getIndication();

    /**
     * Gets validation SubIndication
     *
     * @return {@link SubIndication}
     */
    SubIndication getSubIndication();

    /**
     * Gets the (claimed) signing time
     *
     * @return {@link Date}
     */
    Date getSigningTime();

    /**
     * Gets the signing certificate
     *
     * @return {@link CertificateToken}
     */
    CertificateToken getSigningCertificate();

    /**
     * Gets a list of signing candidates
     *
     * @return a list of {@link CertificateToken}s
     */
    List<CertificateToken> getPotentialSigners();

}
