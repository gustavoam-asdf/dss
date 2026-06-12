package eu.europa.esig.dss.eaa.common;

import eu.europa.esig.dss.enumerations.EAAStatusOrigin;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.model.SerializableSignatureParameters;
import eu.europa.esig.dss.model.eaa.claim.ClaimNumber;
import eu.europa.esig.dss.model.eaa.claim.ClaimString;
import eu.europa.esig.dss.pki.model.CertEntity;
import eu.europa.esig.dss.pki.model.CertEntityRepository;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.spi.eaa.EAA;
import eu.europa.esig.dss.spi.eaa.EAAStatusToken;
import eu.europa.esig.dss.spi.eaa.status.statuslist.StatusListValidator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

public abstract class PKIEAAStatusListSource<T extends SerializableSignatureParameters> extends AbstractPKIEAARevocationListSource<T> {

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
        super(certEntityRepository, statusListIssuer);
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

            DSSDocument statusListToken = generateIdentifierListToken(eaa);

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
     * Creates a validator for the {@code statusListToken}
     *
     * @param statusListToken byte array
     * @return {@link StatusListValidator}
     */
    protected abstract StatusListValidator createValidator(byte[] statusListToken);

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
