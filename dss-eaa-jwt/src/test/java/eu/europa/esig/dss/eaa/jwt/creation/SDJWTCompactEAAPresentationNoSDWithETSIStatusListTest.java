package eu.europa.esig.dss.eaa.jwt.creation;

import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.EAARevocationWrapper;
import eu.europa.esig.dss.diagnostic.EAAWrapper;
import eu.europa.esig.dss.eaa.jwt.pki.PKIJWTStatusListSource;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.EAAStatus;
import eu.europa.esig.dss.jades.JAdESSignatureParameters;
import eu.europa.esig.dss.model.Digest;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.spi.eaa.status.EAARevocationSource;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SDJWTCompactEAAPresentationNoSDWithETSIStatusListTest extends AbstractSDJWTEAAPresentationTestIssuance {

    private SDJWTEAAPayloadParameters payloadParameters;
    private JAdESSignatureParameters signatureParameters;

    @BeforeEach
    void init() {
        payloadParameters = new SDJWTEAAPayloadParameters();
        payloadParameters.setIssuer("EAA provider");

        payloadParameters.setVerifiableCredentialsType("urn:eudi:eaa:1");
        Digest digest = new Digest(DigestAlgorithm.SHA256, DSSUtils.digest(DigestAlgorithm.SHA256, "vct".getBytes()));
        payloadParameters.setVerifiableCredentialsTypeIntegrity(digest);

        payloadParameters.setStatusList("TokenStatusList", "revocation", 0, "https://dss.nowina.lu/pki-factory/eaa/status_list");

        signatureParameters = new JAdESSignatureParameters();
        signatureParameters.setDigestAlgorithm(DigestAlgorithm.SHA256);
        signatureParameters.setSigningCertificate(getSigningCert());
        signatureParameters.setCertificateChain(getCertificateChain());
    }

    @Override
    protected EAARevocationSource getEAAStatusSource() {
        return new PKIJWTStatusListSource(getCertEntityRepository(), getCertEntity(GOOD_CA));
    }

    @Override
    protected void checkEAARevocations(DiagnosticData diagnosticData) {
        super.checkEAARevocations(diagnosticData);

        EAAWrapper eaa = diagnosticData.getEAAById(diagnosticData.getFirstEAAId());
        List<EAARevocationWrapper> eaaStatuses = eaa.getEAARevocations();
        assertEquals(1, eaaStatuses.size());
        assertEquals(EAAStatus.VALID, eaaStatuses.get(0).getStatus());
        assertEquals("application/statuslist+jwt", eaaStatuses.get(0).getType());
    }

    @Override
    protected void checkClaims(DiagnosticData diagnosticData) {
        super.checkClaims(diagnosticData);

        EAAWrapper eaa = diagnosticData.getEAAById(diagnosticData.getFirstEAAId());
        assertEquals("TokenStatusList", eaa.getEAAStatusType());
        assertEquals("revocation", eaa.getEAAStatusPurpose());
        assertEquals(0, eaa.getEAAStatusIndex());
        assertEquals("https://dss.nowina.lu/pki-factory/eaa/status_list", eaa.getEAAStatusUri());
    }

    @Override
    protected SDJWTEAAPayloadParameters getPayloadParameters() {
        return payloadParameters;
    }

    @Override
    protected JAdESSignatureParameters getSignatureParameters() {
        return signatureParameters;
    }

    @Override
    protected JAdESSignatureParameters getKeyBindingSignatureParameters() {
        return null;
    }

    @Override
    protected SDJWTKeyBindingParameters getKeyBindingParameters() {
        return null;
    }

    @Override
    protected boolean keyBindingPresent() {
        return false;
    }

    @Override
    protected boolean disclosuresPresent() {
        return false;
    }

    @Override
    protected String getSigningAlias() {
        return ECDSA_USER;
    }

}
