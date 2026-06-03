package eu.europa.esig.dss.eaa.jwt.creation;

import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.EAAWrapper;
import eu.europa.esig.dss.diagnostic.claim.ClaimWrapper;
import eu.europa.esig.dss.eaa.jwt.SDJWTConstants;
import eu.europa.esig.dss.enumerations.JWSSerializationType;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.jades.JAdESSignatureParameters;
import org.junit.jupiter.api.BeforeEach;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SDJWTCompactEAAPresentationWithAgeEqualsOrOverClaimTest extends AbstractSDJWTEAAPresentationTestIssuance {

    private SDJWTEAAPayloadParameters payloadParameters;
    private JAdESSignatureParameters signatureParameters;

    @BeforeEach
    void init() {
        payloadParameters = new SDJWTEAAPayloadParameters();
        payloadParameters.setIssuer("https://issuer.example.com");
        payloadParameters.selectivelyDisclosable().setAgeOverNN(18, true);
        payloadParameters.selectivelyDisclosable().setAgeOverNN(30, true);
        payloadParameters.selectivelyDisclosable().setAgeOverNN(40, false);

        signatureParameters = new JAdESSignatureParameters();
        signatureParameters.setSigningCertificate(getSigningCert());
        signatureParameters.setCertificateChain(getCertificateChain());
        signatureParameters.setSignatureLevel(SignatureLevel.JAdES_BASELINE_B);
        signatureParameters.setSignaturePackaging(SignaturePackaging.ENVELOPING);
        signatureParameters.setJwsSerializationType(JWSSerializationType.COMPACT_SERIALIZATION);
        signatureParameters.setX509Url("http://nowina.lu/pki-factory/good-cert");
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
    protected void checkClaims(final DiagnosticData diagnosticData) {
        super.checkClaims(diagnosticData);

        EAAWrapper eaa = diagnosticData.getEAAs().get(0);
        assertEquals("https://issuer.example.com", eaa.getEAAIssuer());

        boolean claimAgeEqualOrOverFound = false;
        for (ClaimWrapper claimWrapper : eaa.getEAAPayload().getOtherClaims()) {
            if (claimWrapper.getName().equals(SDJWTConstants.AGE_EQUAL_OR_OVER)) {
                claimAgeEqualOrOverFound = true;
                Map<String, ClaimWrapper> map = claimWrapper.getMap();
                assertEquals(3, map.size());

                ClaimWrapper equalOrOver18Claim = map.get("18");
                assertNotNull(equalOrOver18Claim);
                assertTrue(equalOrOver18Claim.getBoolean());

                ClaimWrapper equalOrOver30Claim = map.get("30");
                assertNotNull(equalOrOver30Claim);
                assertTrue(equalOrOver30Claim.getBoolean());

                ClaimWrapper equalOrOver40Claim = map.get("40");
                assertNotNull(equalOrOver40Claim);
                assertFalse(equalOrOver40Claim.getBoolean());
            }
        }

        assertTrue(claimAgeEqualOrOverFound);
    }

    @Override
    protected boolean keyBindingPresent() {
        return false;
    }

    @Override
    protected String getSigningAlias() {
        return GOOD_USER;
    }

}
