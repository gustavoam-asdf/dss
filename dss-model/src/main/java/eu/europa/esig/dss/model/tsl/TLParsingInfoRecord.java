package eu.europa.esig.dss.model.tsl;

import eu.europa.esig.dss.enumerations.TSLType;
import eu.europa.esig.dss.model.job.ParsingInfoRecord;

import java.util.Date;
import java.util.List;

/**
 * Represents a Trusted List parsing result record
 *
 */
public interface TLParsingInfoRecord extends ParsingInfoRecord {

    /**
     * Gets the LOTL/TL TSLType
     *
     * @return {@link TSLType}
     */
    TSLType getTSLType();

    /**
     * Gets the LOTL/TL sequence number
     *
     * @return {@link Integer}
     */
    Integer getSequenceNumber();

    /**
     * Gets LOTL/TL version
     *
     * @return {@link Integer}
     */
    Integer getVersion();

    /**
     * Gets the LOTL/TL territory (country)
     *
     * @return {@link String}
     */
    String getTerritory();

    /**
     * Gets issuing date
     *
     * @return {@link Date}
     */
    Date getIssueDate();

    /**
     * Gets next update date
     *
     * @return {@link Date}
     */
    Date getNextUpdateDate();

    /**
     * Gets distribution points
     *
     * @return a list of {@link String}s
     */
    List<String> getDistributionPoints();

    /**
     * Gets trust service providers
     *
     * @return a list of {@link TrustServiceProvider}s
     */
    List<TrustServiceProvider> getTrustServiceProviders();

    /**
     * Gets LOTL other TSL pointers
     *
     * @return a list of {@link OtherTSLPointer}s
     */
    List<OtherTSLPointer> getLotlOtherPointers();

    /**
     * Gets TL other TSL pointers
     *
     * @return a list of {@link OtherTSLPointer}s
     */
    List<OtherTSLPointer> getTlOtherPointers();

    /**
     * Gets pivot URLs
     *
     * @return a list of {@link String}s
     */
    List<String> getPivotUrls();

    /**
     * Gets signing certificate announcement URL
     *
     * @return {@link String}
     */
    String getSigningCertificateAnnouncementUrl();

    /**
     * Returns a number of all {@code TrustServiceProvider}s present in the TL
     *
     * @return TSP number
     */
    int getTSPNumber();

    /**
     * Returns a number of all {@code TrustService}s present in the TL
     *
     * @return TS number
     */
    int getTSNumber();

    /**
     * Returns a number of all {@code CertificateToken}s present in the TL
     *
     * @return number of certificates
     */
    int getCertNumber();

}
