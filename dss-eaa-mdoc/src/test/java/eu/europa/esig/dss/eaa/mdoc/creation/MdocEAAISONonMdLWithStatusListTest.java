package eu.europa.esig.dss.eaa.mdoc.creation;

import eu.europa.esig.dss.cbades.signature.CBAdESSignatureParameters;
import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.EAARevocationWrapper;
import eu.europa.esig.dss.diagnostic.EAAWrapper;
import eu.europa.esig.dss.eaa.mdoc.MdocConstants;
import eu.europa.esig.dss.eaa.mdoc.pki.PKICWTStatusListSource;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.EAAStatus;
import eu.europa.esig.dss.spi.eaa.status.EAARevocationSource;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MdocEAAISONonMdLWithStatusListTest extends AbstractMdocEAAPresentationTestIssuance {

    private MdocEAAPayloadParameters payloadParameters;
    private CBAdESSignatureParameters signatureParameters;

    @BeforeEach
    void init() {
        payloadParameters = new MdocEAAPayloadParameters();
        payloadParameters.setDocType(MdocConstants.ISO23220_1_MID_DOC_TYPE);
        payloadParameters.setDeviceKey(getSigningCert());

        payloadParameters.setStatusList(0, "https://dss.nowina.lu/pki-factory/eaa/status_list");

        signatureParameters = new CBAdESSignatureParameters();
        signatureParameters.setDigestAlgorithm(DigestAlgorithm.SHA256);
        signatureParameters.setSigningCertificate(getSigningCert());
        signatureParameters.setCertificateChain(getCertificateChain());
    }

    @Override
    protected EAARevocationSource getEAAStatusSource() {
        return new PKICWTStatusListSource(getCertEntityRepository(), getCertEntity(GOOD_CA));
    }

    @Override
    protected void checkEAARevocations(DiagnosticData diagnosticData) {
        super.checkEAARevocations(diagnosticData);

        EAAWrapper eaa = diagnosticData.getEAAById(diagnosticData.getFirstEAAId());
        List<EAARevocationWrapper> eaaStatuses = eaa.getEAARevocations();
        assertEquals(1, eaaStatuses.size());
        assertEquals(EAAStatus.VALID, eaaStatuses.get(0).getStatus());
        assertEquals("application/statuslist+cwt", eaaStatuses.get(0).getType());
    }

    @Override
    protected MdocEAAPayloadParameters getPayloadParameters() {
        return payloadParameters;
    }

    @Override
    protected CBAdESSignatureParameters getSignatureParameters() {
        return signatureParameters;
    }

    @Override
    protected CBAdESSignatureParameters getKeyBindingSignatureParameters() {
        return null;
    }

    @Override
    protected MdocKeyBindingParameters getKeyBindingParameters() {
        return null;
    }

    @Override
    protected boolean disclosuresPresent() {
        return false;
    }

    @Override
    protected boolean keyBindingPresent() {
        return false;
    }

    @Override
    protected String getSigningAlias() {
        return ECDSA_USER;
    }

}
