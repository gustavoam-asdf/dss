package eu.europa.esig.dss.eaa.jwt.pki;

import eu.europa.esig.dss.eaa.common.PKIEAAStatusListSource;
import eu.europa.esig.dss.enumerations.JWSSerializationType;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.jades.DSSJsonUtils;
import eu.europa.esig.dss.jades.JAdESSignatureParameters;
import eu.europa.esig.dss.jades.eaa.statuslist.JWTStatusListClaimNames;
import eu.europa.esig.dss.jades.eaa.statuslist.JWTStatusListValidator;
import eu.europa.esig.dss.jades.jwt.JWTClaimNames;
import eu.europa.esig.dss.jades.signature.JAdESService;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.model.eaa.claim.ClaimStatus;
import eu.europa.esig.dss.pki.model.CertEntity;
import eu.europa.esig.dss.pki.model.CertEntityRepository;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.spi.eaa.statuslist.StatusListValidator;
import eu.europa.esig.dss.spi.validation.CommonCertificateVerifier;
import org.jose4j.json.internal.json_simple.JSONObject;

import java.util.Collections;

public class PKIJWTStatusListSource extends PKIEAAStatusListSource<JAdESSignatureParameters> {

    /**
     * Creates a PKIJWTStatusListSource instance with status list signer {@code CertEntity}
     *
     * @param certEntityRepository {@link CertEntityRepository}
     * @param statusListIssuer     {@link CertEntity} to issue status list
     */
    public PKIJWTStatusListSource(CertEntityRepository<? extends CertEntity> certEntityRepository, CertEntity statusListIssuer) {
        super(certEntityRepository, statusListIssuer);
    }

    public String getType() {
        String type = super.getType();
        if (type == null) {
            return "statuslist+jwt";
        }
        return type;
    }

    @Override
    protected JAdESSignatureParameters getSignatureParameters() {
        JAdESSignatureParameters signatureParameters = new JAdESSignatureParameters();
        signatureParameters.bLevel().setSigningDate(getIssuanceTime());
        signatureParameters.setSignatureLevel(SignatureLevel.JAdES_BASELINE_B);
        signatureParameters.setSignaturePackaging(SignaturePackaging.ENVELOPING);
        signatureParameters.setJwsSerializationType(JWSSerializationType.COMPACT_SERIALIZATION);
        signatureParameters.setSigningCertificate(statusListIssuer.getCertificateToken());
        signatureParameters.setCertificateChain(Collections.singletonList(statusListIssuer.getCertificateToken()));
        signatureParameters.setSignatureType(getType());
        return signatureParameters;
    }

    @Override
    protected JAdESService getService() {
        return new JAdESService(new CommonCertificateVerifier());
    }

    @Override
    protected StatusListValidator createValidator(byte[] statusListToken) {
        return new JWTStatusListValidator(statusListToken);
    }

    /**
     * Generates payload
     *
     * @param claimStatus {@link ClaimStatus}
     * @return {@link DSSDocument}
     */
    @Override
    protected DSSDocument generatePayload(ClaimStatus claimStatus) {
        JSONObject statusListPayload = new JSONObject();

        statusListPayload.put(JWTClaimNames.IAT, DSSUtils.getTimeValueInSeconds(getIssuanceTime().getTime()));
        statusListPayload.put(JWTClaimNames.EXP, DSSUtils.getTimeValueInSeconds(getExpirationTime().getTime()));
        if (getTimeToLive() != null) {
            statusListPayload.put(JWTStatusListClaimNames.TTL, getTimeToLive());
        }

        JSONObject statusList = new JSONObject();
        statusList.put(JWTStatusListClaimNames.BITS, 1);
        statusList.put(JWTStatusListClaimNames.LST, createLst(getStatusList()));
        statusListPayload.put(JWTStatusListClaimNames.STATUS_LIST, statusList);

        if (claimStatus != null && claimStatus.getStatusList() != null && claimStatus.getStatusList().getUri() != null) {
            statusListPayload.put(JWTClaimNames.SUB, claimStatus.getStatusList().getUri().getStringValue());
        }

        return new InMemoryDocument(statusListPayload.toJSONString().getBytes());
    }

    /**
     * Creates "lst" entry
     *
     * @param bytes byte array
     * @return {@link String}
     */
    protected String createLst(byte[] bytes) {
        byte[] compressed = compressZlib(bytes);
        return DSSJsonUtils.toBase64Url(compressed);
    }

}
