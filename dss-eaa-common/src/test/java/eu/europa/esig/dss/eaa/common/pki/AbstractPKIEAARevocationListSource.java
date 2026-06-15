package eu.europa.esig.dss.eaa.common.pki;

import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.SerializableSignatureParameters;
import eu.europa.esig.dss.model.SignatureValue;
import eu.europa.esig.dss.model.ToBeSigned;
import eu.europa.esig.dss.model.eaa.claim.ClaimStatus;
import eu.europa.esig.dss.pki.model.CertEntity;
import eu.europa.esig.dss.pki.model.CertEntityRepository;
import eu.europa.esig.dss.signature.DocumentSignatureService;
import eu.europa.esig.dss.spi.eaa.EAA;
import eu.europa.esig.dss.spi.eaa.status.EAARevocationSource;
import eu.europa.esig.dss.test.pki.CertEntitySignatureTokenConnection;

import java.util.Date;

public abstract class AbstractPKIEAARevocationListSource<T extends SerializableSignatureParameters> implements EAARevocationSource {

    /**
     * The repository managing the PKI
     */
    protected final CertEntityRepository certEntityRepository;

    /**
     * Cert Entity issuing the status list responses
     */
    protected CertEntity statusListIssuer;

    /**
     * Type of the token
     */
    protected String type;

    /**
     * Time of the status list issuance
     */
    protected Date issuanceTime;

    /**
     * Time of the status list expiration
     */
    protected Date expirationTime;

    /**
     * Time to live
     */
    protected Integer timeToLive;

    /**
     * Creates a PKIJWTStatusListSource instance with status list signer {@code CertEntity}
     *
     * @param certEntityRepository {@link CertEntityRepository}
     * @param statusListIssuer     {@link CertEntity} to issue status list
     */
    protected AbstractPKIEAARevocationListSource(CertEntityRepository<? extends CertEntity> certEntityRepository, CertEntity statusListIssuer) {
        this.certEntityRepository = certEntityRepository;
        this.statusListIssuer = statusListIssuer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getIssuanceTime() {
        if (issuanceTime == null) {
            issuanceTime = new Date();
        }
        return issuanceTime;
    }

    public void setIssuanceTime(Date issuanceTime) {
        this.issuanceTime = issuanceTime;
    }

    public Date getExpirationTime() {
        if (expirationTime == null) {
            expirationTime = statusListIssuer.getCertificateToken().getNotAfter();
        }
        return expirationTime;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    public Integer getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(Integer timeToLive) {
        this.timeToLive = timeToLive;
    }

    /**
     * Generates the status list token
     *
     * @param eaa {@link EAA} to get a status_list for
     * @return {@link DSSDocument}
     */
    protected DSSDocument generateIdentifierListToken(EAA eaa) {
        T signatureParameters = getSignatureParameters();
        DSSDocument payload = generatePayload(eaa.getPayload().getStatus());
        DocumentSignatureService<T, ?> service = getService();
        ToBeSigned dataToSign = service.getDataToSign(payload, signatureParameters);
        SignatureValue signatureValue;
        try (CertEntitySignatureTokenConnection token = new CertEntitySignatureTokenConnection(statusListIssuer)) {
            signatureValue = token.sign(dataToSign, signatureParameters.getSignatureAlgorithm(), token.getKeys().get(0));
        }
        return service.signDocument(payload, signatureParameters, signatureValue);
    }

    /**
     * Gets the parameters to create a signature
     *
     * @return {@link SerializableSignatureParameters}
     */
    protected abstract T getSignatureParameters();

    /**
     * Gets a service
     *
     * @return {@link DocumentSignatureService}
     */
    protected abstract DocumentSignatureService<T, ?> getService();

    /**
     * Generates payload
     *
     * @param claimStatus {@link ClaimStatus}
     * @return {@link DSSDocument}
     */
    protected abstract DSSDocument generatePayload(ClaimStatus claimStatus);

}
