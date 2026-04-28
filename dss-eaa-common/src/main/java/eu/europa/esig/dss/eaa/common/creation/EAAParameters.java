package eu.europa.esig.dss.eaa.common.creation;

import java.util.Date;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;

public abstract class EAAParameters {

    private Date issuanceDate;

    private Date expirationDate;

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
}
