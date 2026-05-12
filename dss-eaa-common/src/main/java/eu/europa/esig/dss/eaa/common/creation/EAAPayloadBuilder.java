package eu.europa.esig.dss.eaa.common.creation;

import java.util.Date;

import eu.europa.esig.dss.model.DSSDocument;

public abstract class EAAPayloadBuilder {
    private Date issuanceDate;
    private Date expirationDate;
    private String issuer;
    private String subject;

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

    public abstract DSSDocument buildPayload();
}
