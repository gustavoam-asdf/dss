package eu.europa.esig.dss.diagnostic;

import eu.europa.esig.dss.diagnostic.jaxb.XmlBasicSignature;
import eu.europa.esig.dss.diagnostic.jaxb.XmlChainItem;
import eu.europa.esig.dss.diagnostic.jaxb.XmlEAAStatusToken;
import eu.europa.esig.dss.diagnostic.jaxb.XmlSigningCertificate;
import eu.europa.esig.dss.enumerations.EAAStatusOrigin;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Wraps validation information of the EAA status token
 * 
 */
public class EAAStatusTokenWrapper extends AbstractTokenProxy {

    /** Wrapped {@code XmlEAAStatusToken} */
    private final XmlEAAStatusToken eaaStatusToken;

    /**
     * Default constructor
     *
     * @param eaaStatusToken {@link XmlEAAStatusToken}
     */
    public EAAStatusTokenWrapper(XmlEAAStatusToken eaaStatusToken) {
        Objects.requireNonNull(eaaStatusToken, "XmlEAAStatusToken cannot be null!");
        this.eaaStatusToken = eaaStatusToken;
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
     * Gets origin of the EAA status token (e.g. EXTERNAL or CACHED)
     *
     * @return {@link EAAStatusOrigin}
     */
    public EAAStatusOrigin getOrigin() {
        return eaaStatusToken.getOrigin();
    }

    /**
     * Gets the claimed type of the EAA status token
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
     * Gets time of the issuance of the EAA status token
     *
     * @return {@link Date}
     */
    public Date getIssuedAt() {
        return eaaStatusToken.getIssuedAt();
    }

    /**
     * Gets time of the expiration of the EAA status token
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
        if (!(obj instanceof EAAStatusTokenWrapper))
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
