package eu.europa.esig.dss.eaa.common.creation;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;

import java.util.Date;

/**
 *
 */
public abstract class AbstractEAAPayloadBuilder implements EAAPayloadBuilder {

    protected DigestAlgorithm digestAlgorithm = DigestAlgorithm.SHA256;

    protected EAASaltGenerator saltGenerator = new DefaultEAASaltGenerator();

    private Date issuanceDate;
    private Date expirationDate;
    private String issuer;
    private String subject;
    private boolean shortLived;
    private boolean oneTime;

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(final Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Date getIssuanceDate() {
        return issuanceDate;
    }

    public void setIssuanceDate(final Date issuanceDate) {
        this.issuanceDate = issuanceDate;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(final String issuer) {
        this.issuer = issuer;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(final String subject) {
        this.subject = subject;
    }

    public boolean isShortLived() {
        return shortLived;
    }

    public void setShortLived(final boolean shortLived) {
        this.shortLived = shortLived;
    }

    public boolean isOneTime() {
        return oneTime;
    }

    public void setOneTime(final boolean oneTime) {
        this.oneTime = oneTime;
    }
}
