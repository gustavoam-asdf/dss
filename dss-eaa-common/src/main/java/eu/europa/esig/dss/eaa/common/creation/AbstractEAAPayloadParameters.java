package eu.europa.esig.dss.eaa.common.creation;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;

import java.util.Date;
import java.util.Objects;

/**
 * Abstract implementation of the EAA Payload parameters
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

    /** EAA issuer subject */
    private String issuer;

    /** EAA subject */
    private String subject;

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
     * Gets the EAA issuer subject
     *
     * @return {@link String}
     */
    public String getIssuer() {
        return issuer;
    }

    /**
     * Sets the EAA issue subject
     *
     * @param issuer {@link String}
     */
    public void setIssuer(final String issuer) {
        this.issuer = issuer;
    }

    /**
     * Gets the EAA subject
     *
     * @return {@link String}
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets the EAA subject
     *
     * @param subject {@link String}
     */
    public void setSubject(final String subject) {
        this.subject = subject;
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
                ", expirationDate=" + expirationDate +
                ", issuer='" + issuer + '\'' +
                ", subject='" + subject + '\'' +
                ", shortLived=" + shortLived +
                ", oneTime=" + oneTime +
                ", decoyDigestNumber=" + decoyDigestNumber +
                ", shuffleHashes=" + shuffleHashes +
                ']';
    }

}
