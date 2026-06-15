package eu.europa.esig.dss.eaa.mdoc.pki;

import eu.europa.esig.dss.cbades.cbor.CBORMap;
import eu.europa.esig.dss.cbades.cbor.CBORUtils;
import eu.europa.esig.dss.cbades.cwt.CWTClaims;
import eu.europa.esig.dss.cbades.signature.CBAdESService;
import eu.europa.esig.dss.cbades.signature.CBAdESSignatureParameters;
import eu.europa.esig.dss.eaa.common.pki.PKIEAAStatusListSource;
import eu.europa.esig.dss.eaa.revocation.cwt.model.statuslist.CWTStatusListClaims;
import eu.europa.esig.dss.enumerations.COSEStructureType;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.model.eaa.claim.ClaimStatus;
import eu.europa.esig.dss.pki.model.CertEntity;
import eu.europa.esig.dss.pki.model.CertEntityRepository;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.spi.validation.CommonCertificateVerifier;

import java.util.Collections;

/**
 * Test implementation for generation of CWT-encoded Token Status List
 *
 */
public class PKICWTStatusListSource extends PKIEAAStatusListSource<CBAdESSignatureParameters> {

    /**
     * Creates a PKIJWTStatusListSource instance with status list signer {@code CertEntity}
     *
     * @param certEntityRepository {@link CertEntityRepository}
     * @param statusListIssuer     {@link CertEntity} to issue status list
     */
    public PKICWTStatusListSource(CertEntityRepository<? extends CertEntity> certEntityRepository, CertEntity statusListIssuer) {
        super(certEntityRepository, statusListIssuer);
    }

    @Override
    public String getType() {
        String type = super.getType();
        if (type == null) {
            return "application/statuslist+cwt";
        }
        return type;
    }

    @Override
    protected CBAdESSignatureParameters getSignatureParameters() {
        CBAdESSignatureParameters signatureParameters = new CBAdESSignatureParameters();
        signatureParameters.bLevel().setSigningDate(getIssuanceTime());
        signatureParameters.setSignatureLevel(SignatureLevel.CB_AdES_BASELINE_B);
        signatureParameters.setSignaturePackaging(SignaturePackaging.ENVELOPING);
        signatureParameters.setCoseStructureType(COSEStructureType.COSE_SIGN1);
        signatureParameters.setTagged(true);
        signatureParameters.setSigningCertificate(statusListIssuer.getCertificateToken());
        signatureParameters.setCertificateChain(Collections.singletonList(statusListIssuer.getCertificateToken()));
        signatureParameters.setSignatureType(getType());
        return signatureParameters;
    }

    @Override
    protected CBAdESService getService() {
        return new CBAdESService(new CommonCertificateVerifier());
    }

    /**
     * Generates payload
     *
     * @param claimStatus {@link ClaimStatus}
     * @return {@link DSSDocument}
     */
    @Override
    protected DSSDocument generatePayload(ClaimStatus claimStatus) {
        CBORMap statusListPayload = new CBORMap();

        statusListPayload.put(CWTClaims.IAT.cbor(), DSSUtils.getTimeValueInSeconds(getIssuanceTime().getTime()));
        statusListPayload.put(CWTClaims.EXP.cbor(), DSSUtils.getTimeValueInSeconds(getExpirationTime().getTime()));
        if (getTimeToLive() != null) {
            statusListPayload.put(CWTStatusListClaims.TIME_TO_LIVE, getTimeToLive());
        }

        CBORMap statusList = new CBORMap();
        statusList.put(CWTStatusListClaims.STATUS_LIST_BITS.cbor(), 1);
        statusList.put(CWTStatusListClaims.STATUS_LIST_LST.cbor(), compressZlib(getStatusList()));
        statusListPayload.put(CWTStatusListClaims.STATUS_LIST.cbor(), statusList);

        if (claimStatus != null && claimStatus.getStatusList() != null && claimStatus.getStatusList().getUri() != null) {
            statusListPayload.put(CWTClaims.SUB.cbor(), claimStatus.getStatusList().getUri().getStringValue());
        }

        return new InMemoryDocument(CBORUtils.serializeCborObject(statusListPayload));
    }

}
