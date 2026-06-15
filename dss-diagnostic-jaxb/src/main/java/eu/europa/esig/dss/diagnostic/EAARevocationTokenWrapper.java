package eu.europa.esig.dss.diagnostic;

import eu.europa.esig.dss.diagnostic.jaxb.XmlBasicSignature;
import eu.europa.esig.dss.diagnostic.jaxb.XmlChainItem;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAARevocationToken;
import eu.europa.esig.dss.diagnostic.jaxb.XmlSigningCertificate;
import eu.europa.esig.dss.enumerations.EAARevocationOrigin;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Wraps validation information of the EAA revocation token
 * 
 */
public class EAARevocationTokenWrapper extends AbstractTokenProxy {

    /** Wrapped {@code XmlEAARevocationToken} */
    private final XmlEAARevocationToken eaaStatusToken;

    /**
     * Default constructor
     *
     * @param eaaRevocationToken {@link XmlEAARevocationToken}
     */
    public EAARevocationTokenWrapper(XmlEAARevocationToken eaaRevocationToken) {
        Objects.requireNonNull(eaaRevocationToken, "XmlEAARevocationToken cannot be null!");
        this.eaaStatusToken = eaaRevocationToken;
    }

    @Override
    protected XmlBasicSignature getCurrentBasicSignature() {
        return eaaStatusToken.getBasicSignature();
    }

    @Override
    protected List<XmlChainItem> getCurrentCertificateChain() {
        return eaaStatusToken.getCertificateChain();
    }

    @Override
    protected XmlSigningCertificate getCurrentSigningCertificate() {
        return eaaStatusToken.getSigningCertificate();
    }

    /**
     * Returns FoundCertificatesProxy to access embedded certificates
     *
     * @return {@link FoundCertificatesProxy}
     */
    @Override
    public FoundCertificatesProxy foundCertificates() {
        return new FoundCertificatesProxy(eaaStatusToken.getFoundCertificates());
    }

    /**
     * Gets origin of the EAA revocation token (e.g. EXTERNAL or CACHED)
     *
     * @return {@link EAARevocationOrigin}
     */
    public EAARevocationOrigin getOrigin() {
        return eaaStatusToken.getOrigin();
    }

    /**
     * Gets the claimed type of the EAA revocation token
     *
     * @return {@link String}
     */
    public String getType() {
        return eaaStatusToken.getType();
    }

    /**
     * Gets the location URI used to access the original EAA source token
     *
     * @return {@link String}
     */
    public String getSourceAddress() {
        return eaaStatusToken.getSourceAddress();
    }

    /**
     * Gets the subject of the EAA revocation token
     *
     * @return {@link String}
     */
    public String getSubject() {
        return eaaStatusToken.getSubject() != null ? eaaStatusToken.getSubject().getValue() : null;
    }

    /**
     * Gets whether the subject of the EAA revocation token matches the subject of the related EAA
     *
     * @return TRUE if the subject matches, FALSE otherwise
     */
    public boolean getSubjectMatch() {
        return eaaStatusToken.getSubject() != null && eaaStatusToken.getSubject().isMatch() != null ?
                eaaStatusToken.getSubject().isMatch() : false;
    }

    /**
     * Gets time of the issuance of the EAA revocation token
     *
     * @return {@link Date}
     */
    public Date getIssuedAt() {
        return eaaStatusToken.getIssuedAt();
    }

    /**
     * Gets time of the expiration of the EAA revocation token
     *
     * @return {@link Date}
     */
    public Date getExpirationTime() {
        return eaaStatusToken.getExpirationTime();
    }

    /**
     * Gets number of seconds after which a new EAA Status token should be requested
     *
     * @return {@link BigInteger}
     */
    public BigInteger getTimeToLive() {
        return eaaStatusToken.getTimeToLive();
    }

    @Override
    public byte[] getBinaries() {
        return eaaStatusToken.getBase64Encoded();
    }

    @Override
    public String getId() {
        return eaaStatusToken.getId();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof EAARevocationTokenWrapper))
            return false;
        AbstractTokenProxy other = (AbstractTokenProxy) obj;
        if (getId() == null) {
            if (other.getId() != null) {
                return false;
            }
        } else if (!getId().equals(other.getId())) {
            return false;
        }
        return true;
    }

}
