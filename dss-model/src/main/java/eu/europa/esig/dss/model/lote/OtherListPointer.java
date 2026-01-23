package eu.europa.esig.dss.model.lote;

import eu.europa.esig.dss.model.x509.CertificateToken;

import java.util.List;
import java.util.Map;

/**
 * Contains information about a reference to another List, including URL and signing certificates
 *
 */
public interface OtherListPointer {

    /**
     * Gets a list of ServiceDigitalIdentity X509 certificates
     *
     * @return a list of {@link CertificateToken}s
     */
    List<CertificateToken> getSdiCertificates();

    /**
     * Gets List location url
     *
     * @return {@link String}
     */
    String getLocationURI();

    /**
     * Gets the scheme territory ISO country code
     *
     * @return {@link String}
     */
    String getSchemeTerritory();

    /**
     * Gets the List Type
     *
     * @return {@link String}
     */
    String getType();

    /**
     * Gets the MimeType of the referenced document
     *
     * @return {@link String}
     */
    String getMimeType();

    /**
     * Gets a map of scheme operator names
     *
     * @return a map of {@link String} language code and a list of corresponding {@link String} names
     */
    Map<String, List<String>> getSchemeOperatorNames();

    /**
     * Gets a map of scheme type community rules
     *
     * @return a map of {@link String} language code and a list of corresponding {@link String} names
     */
    Map<String, List<String>> getSchemeTypeCommunityRules();

}
