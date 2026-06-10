package eu.europa.esig.dss.eaa.common;

import eu.europa.esig.dss.enumerations.EAAStatusOrigin;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.model.SerializableSignatureParameters;
import eu.europa.esig.dss.model.SignatureValue;
import eu.europa.esig.dss.model.ToBeSigned;
import eu.europa.esig.dss.model.eaa.claim.ClaimNumber;
import eu.europa.esig.dss.model.eaa.claim.ClaimStatus;
import eu.europa.esig.dss.model.eaa.claim.ClaimString;
import eu.europa.esig.dss.pki.model.CertEntity;
import eu.europa.esig.dss.pki.model.CertEntityRepository;
import eu.europa.esig.dss.signature.DocumentSignatureService;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.spi.eaa.EAA;
import eu.europa.esig.dss.spi.eaa.EAAStatusToken;
import eu.europa.esig.dss.spi.eaa.statuslist.EAAStatusSource;
import eu.europa.esig.dss.spi.eaa.statuslist.StatusListValidator;
import eu.europa.esig.dss.test.pki.CertEntitySignatureTokenConnection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

public abstract class PKIEAAStatusListSource<T extends SerializableSignatureParameters> implements EAAStatusSource {

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
     * Status list array
     */
    protected byte[] statusList = new byte[8];

    /**
     * Creates a PKIJWTStatusListSource instance with status list signer {@code CertEntity}
     *
     * @param certEntityRepository {@link CertEntityRepository}
     * @param statusListIssuer     {@link CertEntity} to issue status list
     */
    protected PKIEAAStatusListSource(CertEntityRepository<? extends CertEntity> certEntityRepository, CertEntity statusListIssuer) {
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

    public byte[] getStatusList() {
        return statusList;
    }

    public void setStatusList(byte[] statusList) {
        this.statusList = statusList;
    }

    @Override
    public EAAStatusToken getEAAStatus(EAA eaa) {
        if (eaa != null && eaa.getPayload() != null && eaa.getPayload().getStatus() != null
                && eaa.getPayload().getStatus().getStatusList() != null) {

            DSSDocument statusListToken = generateStatusListToken(eaa);

            ClaimNumber index = eaa.getPayload().getStatus().getStatusList().getIndex();
            ClaimString uri = eaa.getPayload().getStatus().getStatusList().getUri();
            if (index != null) {
                EAAStatusToken statusToken = createValidator(DSSUtils.toByteArray(statusListToken))
                        .getStatusToken(index.getNumberValue().intValue());
                statusToken.setOrigin(EAAStatusOrigin.EXTERNAL);
                statusToken.setSourceURL(uri.getValueAsString());
                statusToken.setRelatedEAA(eaa);
                return statusToken;
            }
        }
        return null;
    }

    /**
     * Generates the status list token
     *
     * @param eaa {@link EAA} to get a status_list for
     * @return {@link DSSDocument}
     */
    protected DSSDocument generateStatusListToken(EAA eaa) {
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
     * Creates a validator for the {@code statusListToken}
     *
     * @param statusListToken byte array
     * @return {@link StatusListValidator}
     */
    protected abstract StatusListValidator createValidator(byte[] statusListToken);

    /**
     * Generates payload
     *
     * @param claimStatus {@link ClaimStatus}
     * @return {@link DSSDocument}
     */
    protected abstract DSSDocument generatePayload(ClaimStatus claimStatus);

    /**
     * Compresses the input bytes
     *
     * @param input bytes to compress
     * @return compressed byte array
     */
    protected byte[] compressZlib(byte[] input) {
        Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             DeflaterOutputStream dos = new DeflaterOutputStream(baos, deflater)) {
            dos.write(input);
            dos.finish();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new DSSException("Unable to compress", e);
        }
    }

}
