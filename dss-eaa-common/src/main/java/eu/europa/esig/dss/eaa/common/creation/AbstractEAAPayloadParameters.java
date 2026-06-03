package eu.europa.esig.dss.eaa.common.creation;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.model.x509.CertificateToken;

import java.security.PublicKey;
import java.util.Date;
import java.util.Objects;

/**
 * Abstract implementation of the EAA Payload parameters
 *
 */
public abstract class AbstractEAAPayloadParameters implements EAAPayloadParameters {

    /** DigestAlgorithm used to compute hashes for selectively disclosable claims  */
    private DigestAlgorithm digestAlgorithm = DigestAlgorithm.SHA256;

    /** Date of the EAA issuance */
    private Date issuanceDate;

    /** Date of the EAA technical validity start */
    private Date notBeforeDate;

    /** Date of the EAA technical validity end */
    private Date expirationDate;

    /** Contains the public part of the key pair used for mdoc authentication. */
    private PublicKey deviceKey;

    /** (Optional) Contains an "identifier_list". */
    private EAARevocationList identifierList;

    /** (Optional) Contains a "status_list" as defined in IETF draft-ietf-oauth-status-list-20. */
    private EAARevocationList statusList;

    /* ETSI technical claims */

    /** Category of the EAA (e.g. QEAA, Pub-EAA, or other) */
    private String category;

    /** Whether the EAA is short-lived */
    private boolean shortLived;

    /** Whether the EAA is issued for a one time use */
    private boolean oneTime;

    /** The number of decoy digests that will be added */
    private int decoyDigestNumber;

    /** If the hashes in the EAA should be shuffled */
    private boolean shuffleHashes = true;

    /**
     * Default constructor
     */
    protected AbstractEAAPayloadParameters() {
        // empty
    }

    @Override
    public DigestAlgorithm getDigestAlgorithm() {
        return digestAlgorithm;
    }

    /**
     * Sets digest algorithm to be used for hashes computation of selectively disclosable claims
     *
     * @param digestAlgorithm {@link DigestAlgorithm}
     */
    public void setDigestAlgorithm(DigestAlgorithm digestAlgorithm) {
        Objects.requireNonNull(digestAlgorithm, "DigestAlgorithm cannot be null!");
        this.digestAlgorithm = digestAlgorithm;
    }

    /**
     * Gets the EAA issuance date
     *
     * @return {@link Date}
     */
    public Date getIssuanceDate() {
        return issuanceDate;
    }

    /**
     * Sets the EAA issuance date
     *
     * @param issuanceDate {@link Date}
     */
    public void setIssuanceDate(final Date issuanceDate) {
        this.issuanceDate = issuanceDate;
    }

    /**
     * Gets the EAA notBefore date
     *
     * @return {@link Date}
     */
    public Date getNotBeforeDate() {
        return notBeforeDate;
    }

    /**
     * Sets the EAA notBefore date (technical validity start date)
     *
     * @param notBeforeDate {@link Date}
     */
    public void setNotBeforeDate(Date notBeforeDate) {
        this.notBeforeDate = notBeforeDate;
    }

    /**
     * Gets the EAA expiration date
     *
     * @return {@link Date}
     */
    public Date getExpirationDate() {
        return expirationDate;
    }

    /**
     * Sets the EAA expiration date (technical validity end date)
     *
     * @param expirationDate {@link Date}
     */
    public void setExpirationDate(final Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * Gets the public part of the key pair used for mdoc authentication.
     *
     * @return {@link PublicKey}
     */
    public PublicKey getDeviceKey() {
        return deviceKey;
    }

    /**
     * Sets the public part of the key pair used for mdoc authentication.
     *
     * @param deviceKey {@link PublicKey}
     */
    public void setDeviceKey(PublicKey deviceKey) {
        this.deviceKey = deviceKey;
    }

    /**
     * Gets the identifier_list
     *
     * @return {@link EAARevocationList}
     */
    public EAARevocationList getIdentifierList() {
        return identifierList;
    }

    /**
     * Sets the identifier_list
     *
     * @param identifierList {@link EAARevocationList}
     */
    public void setIdentifierList(EAARevocationList identifierList) {
        this.identifierList = identifierList;
    }

    /**
     * Sets the identifier_list, by specifying an index of the EAA and a status distribution URL
     *
     * @param index integer representing an EAA identifier within the identifier_list
     * @param url {@link String} where the identifier_list can be accessed from
     */
    public void setIdentifierList(int index, String url) {
        this.identifierList = new EAARevocationList(index, url);
    }

    /**
     * Sets the identifier_list, by specifying an index of the EAA and a status distribution URL
     *
     * @param index integer representing an EAA identifier within the identifier_list
     * @param url {@link String} where the identifier_list can be accessed from
     * @param certificateToken {@link CertificateToken} containing the public key that signed or sealed
     *                         the top-level certificate in the x5chain element in the MSO revocation list structure
     */
    public void setIdentifierList(int index, String url, CertificateToken certificateToken) {
        this.identifierList = new EAARevocationList(index, url, certificateToken);
    }

    /**
     * Gets the status_list
     *
     * @return {@link EAARevocationList}
     */
    public EAARevocationList getStatusList() {
        return statusList;
    }

    /**
     * Sets the status_list
     *
     * @param statusList {@link EAARevocationList}
     */
    public void setStatusList(EAARevocationList statusList) {
        this.statusList = statusList;
    }

    /**
     * Sets the status_list, by specifying an index of the EAA and a status distribution URL
     *
     * @param index integer representing an EAA identifier within the status_list
     * @param url {@link String} where the status_list can be accessed from
     */
    public void setStatusList(int index, String url) {
        this.statusList = new EAARevocationList(index, url);
    }

    /**
     * Sets the status_list, by specifying an index of the EAA and a status distribution URL
     *
     * @param index integer representing an EAA identifier within the status_list
     * @param url {@link String} where the status_list can be accessed from
     * @param certificateToken {@link CertificateToken} containing the public key that signed or sealed
     *                         the top-level certificate in the x5chain element in the MSO revocation list structure
     */
    public void setStatusList(int index, String url, CertificateToken certificateToken) {
        this.statusList = new EAARevocationList(index, url, certificateToken);
    }

    /**
     * Gets the EAA category URN
     *
     * @return {@link String}
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the EAA category URN.
     * Example: "urn:etsi:esi:eaa:eu:qualified" for QEAA, "urn:etsi:esi:eaa:eu:pub" for Pub-EAA
     *
     * @param category {@link String}
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Gets whether the EAA is short-lived (no EAA status check applies)
     *
     * @return whether the EAA is short-lived
     */
    public boolean isShortLived() {
        return shortLived;
    }

    /**
     * Sets whether the EAA is short-lived (no EAA status check applies)
     *
     * @param shortLived whether the EAA is short-lived
     */
    public void setShortLived(final boolean shortLived) {
        this.shortLived = shortLived;
    }

    /**
     * Gets whether the EAA is for one time use
     *
     * @return whether the EAA is for one time use
     */
    public boolean isOneTime() {
        return oneTime;
    }

    /**
     * Sets whether the EAA is for one time use
     *
     * @param oneTime whether the EAA is for one time use
     */
    public void setOneTime(final boolean oneTime) {
        this.oneTime = oneTime;
    }

    @Override
    public int getDecoyDigestNumber() {
        return decoyDigestNumber;
    }

    /**
     * Sets the number of decoy digest to generate
     *
     * @param decoyDigestNumber the number of decoy digest to generate
     */
    public void setDecoyDigestNumber(final int decoyDigestNumber) {
        this.decoyDigestNumber = decoyDigestNumber;
    }

    @Override
    public boolean isShuffleHashes() {
        return shuffleHashes;
    }

    /**
     * Sets whether the digests of the selectively disclosable claims are to be shuffled
     *
     * @param shuffleHashes whether the digests of the selectively disclosable claims are to be shuffled
     */
    public void setShuffleHashes(final boolean shuffleHashes) {
        this.shuffleHashes = shuffleHashes;
    }

    @Override
    public String toString() {
        return "AbstractEAAPayloadParameters [" +
                "digestAlgorithm=" + digestAlgorithm +
                ", issuanceDate=" + issuanceDate +
                ", notBeforeDate=" + notBeforeDate +
                ", expirationDate=" + expirationDate +
                ", category='" + category + '\'' +
                ", shortLived=" + shortLived +
                ", oneTime=" + oneTime +
                ", decoyDigestNumber=" + decoyDigestNumber +
                ", shuffleHashes=" + shuffleHashes +
                ']';
    }

}
