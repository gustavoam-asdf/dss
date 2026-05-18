package eu.europa.esig.dss.eaa.jwt.creation;

import eu.europa.esig.dss.eaa.common.creation.AbstractEAAService;
import eu.europa.esig.dss.eaa.common.creation.EAAService;
import eu.europa.esig.dss.eaa.jwt.SDJWTConstants;
import eu.europa.esig.dss.eaa.common.creation.claim.EAAClaim;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.jades.DSSJsonUtils;
import eu.europa.esig.dss.jades.JAdESSignatureParameters;
import eu.europa.esig.dss.jades.JWSCompactSerializationParser;
import eu.europa.esig.dss.jades.JWSJsonSerializationGenerator;
import eu.europa.esig.dss.jades.JWSJsonSerializationObject;
import eu.europa.esig.dss.jades.JWSJsonSerializationParser;
import eu.europa.esig.dss.jades.signature.JAdESService;
import eu.europa.esig.dss.jades.validation.JWS;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.model.SignatureValue;
import eu.europa.esig.dss.model.ToBeSigned;
import eu.europa.esig.dss.spi.validation.CertificateVerifier;
import eu.europa.esig.dss.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Implementation of {@link EAAService} to create SD-JWT EAA
 */
public class SDJWTEAAService extends AbstractEAAService<JAdESSignatureParameters, SDJWTPayloadBuilder> {

    private static final long serialVersionUID = 6514504397480840459L;

    private static final Logger LOG = LoggerFactory.getLogger(SDJWTEAAService.class);

    /**
     * Default constructor to instantiate an {@code SDJWTEAAService}
     *
     * @param certificateVerifier {@link CertificateVerifier}
     */
    public SDJWTEAAService(final CertificateVerifier certificateVerifier) {
        super(certificateVerifier);
        LOG.debug("+ SDJWTEAAService created");
    }

    @Override
    public ToBeSigned getDataToBeSigned(final DSSDocument payload, final JAdESSignatureParameters signatureParameters) {
        validatePayloadAndSignatureParameters(payload, signatureParameters);
        return getJAdESService().getDataToSign(payload, signatureParameters);
    }

    @Override
    public ToBeSigned getDataToBeSigned(final SDJWTPayloadBuilder payloadBuilder, final JAdESSignatureParameters signatureParameters) {
        return getDataToBeSigned(payloadBuilder.buildPayload(), signatureParameters);
    }

    @Override
    public DSSDocument signEAA(final DSSDocument payload, final JAdESSignatureParameters signatureParameters, final SignatureValue signatureValue) {
        validatePayloadAndSignatureParameters(payload, signatureParameters);
        return getJAdESService().signDocument(payload, signatureParameters, signatureValue);
    }

    @Override
    public DSSDocument signEAA(final SDJWTPayloadBuilder payloadBuilder, final JAdESSignatureParameters signatureParameters, final SignatureValue signatureValue) {
        return signEAA(payloadBuilder.buildPayload(), signatureParameters, signatureValue);
    }

    private void validatePayloadAndSignatureParameters(final DSSDocument payload, final JAdESSignatureParameters signatureParameters) {
        Objects.requireNonNull(payload, "payload cannot be null!");
        Objects.requireNonNull(signatureParameters, "signatureParameters cannot be null!");

        if (!DSSJsonUtils.isJsonDocument(payload)) {
            throw new DSSException("Payload is not a JSON document!");
        }

        if (signatureParameters.getSignatureLevel() == null) {
            signatureParameters.setSignatureLevel(SignatureLevel.JAdES_BASELINE_B);
        }

        if (SignatureLevel.JAdES_BASELINE_B != signatureParameters.getSignatureLevel()) {
            throw new DSSException("Signature level must be JAdES_BASELINE_B");
        }

        if (signatureParameters.getSignaturePackaging() == null) {
            signatureParameters.setSignaturePackaging(SignaturePackaging.ENVELOPING);
        }

        if (SignaturePackaging.ENVELOPING != signatureParameters.getSignaturePackaging()) {
            throw new DSSException("Signature packaging must be ENVELOPING");
        }

        if (signatureParameters.getSignatureType() == null) {
            signatureParameters.setSignatureType(SDJWTConstants.SIGNATURE_TYPE);
        }

        if (!Utils.areStringsEqual(SDJWTConstants.SIGNATURE_TYPE, signatureParameters.getSignatureType())) {
            throw new DSSException("Signature type must be " + SDJWTConstants.SIGNATURE_TYPE);
        }
    }

    @Override
    public ToBeSigned getDataToSignForKeybindingSignature(final DSSDocument eaa, final JAdESSignatureParameters signatureParameters) {
        return null;
    }

    @Override
    public ToBeSigned getDataToSignForKeybindingSignature(final DSSDocument eaa, final List<String> disclosures, final JAdESSignatureParameters signatureParameters) {
        return null;
    }

    @Override
    public DSSDocument createKeybindingSignature(final DSSDocument eea, final JAdESSignatureParameters signatureParameters, final SignatureValue signatureValue) {
        return null;
    }

    @Override
    public DSSDocument createKeybindingSignature(final DSSDocument eea, final List<String> disclosures, final JAdESSignatureParameters signatureParameters,
                                                 final SignatureValue signatureValue) {
        return null;
    }

    protected JAdESService getJAdESService() {
        return new JAdESService(certificateVerifier);
    }

    @Override
    public List<String> getDisclosures(final List<EAAClaim> claims, final SDJWTPayloadBuilder payloadBuilder) {
        DigestAlgorithm digestAlgorithm = payloadBuilder.getDigestAlgorithm() == null ? DigestAlgorithm.SHA256 : payloadBuilder.getDigestAlgorithm();

        if (claims == null || claims.isEmpty()) {
            return Collections.emptyList();
        }

        return claims.stream()
                .map(claim -> payloadBuilder.buildDisclosure(claim, digestAlgorithm))
                .collect(Collectors.toList());
    }

    @Override
    public DSSDocument issuePresentation(final DSSDocument eaa, final List<String> disclosures, final DSSDocument keyBinding) {
        Objects.requireNonNull(eaa, "The EAA cannot be null!");
        JWSCompactSerializationParser compactParser = new JWSCompactSerializationParser(eaa);
        if (compactParser.isSupported()) {
            return issueJWSCompactPresentation(eaa, disclosures, keyBinding);
        }

        JWSJsonSerializationParser jwsJsonSerializationParser = new JWSJsonSerializationParser(eaa);
        if (jwsJsonSerializationParser.isSupported()) {
            return issueJWSJsonSerializationPresentation(jwsJsonSerializationParser.parse(), disclosures, keyBinding);
        }

        throw new DSSException("The signed EAA must be a JWS Signature");
    }

    @Override
    public DSSDocument issuePresentation(final DSSDocument eaa, final List<String> disclosures) {
        return issuePresentation(eaa, disclosures, null);
    }

    @Override
    public DSSDocument issuePresentation(final DSSDocument eaa, final DSSDocument keybinding) {
        return issuePresentation(eaa, Collections.emptyList(), keybinding);
    }

    private DSSDocument issueJWSCompactPresentation(final DSSDocument eaa, final List<String> disclosures, final DSSDocument keyBinding) {
        String signedEaa = transformToString(eaa);

        String issuedEaa = signedEaa + "~";
        if (disclosures != null && !disclosures.isEmpty()) {
            issuedEaa = issuedEaa + String.join("~", disclosures) + "~";
        }

        if (keyBinding != null) {
            issuedEaa = issuedEaa + transformToString(keyBinding);
        }

        return new InMemoryDocument(issuedEaa.getBytes());
    }

    private DSSDocument issueJWSJsonSerializationPresentation(JWSJsonSerializationObject jwsJsonSerializationObject, final List<String> disclosures, final DSSDocument keyBinding) {
        if (jwsJsonSerializationObject.getSignatures().size() != 1) {
            throw new DSSException("The signed EAA can only contain one signature");
        }

        JWS jws = jwsJsonSerializationObject.getSignatures().get(0);
        Map<String, Object> unprotected = jws.getUnprotected();
        if (unprotected != null && (unprotected.containsKey(SDJWTConstants.DISCLOSURES) || unprotected.containsKey(SDJWTConstants.KB_JWT))) {
            throw new DSSException("The signed EAA is already an issued presentation");
        } else if (unprotected == null) {
            unprotected = new HashMap<>();
        }

        if (disclosures != null && !disclosures.isEmpty()) {
            unprotected.put(SDJWTConstants.DISCLOSURES, disclosures);
        }

        if (keyBinding != null) {
            unprotected.put(SDJWTConstants.KB_JWT, transformToString(keyBinding));
        }

        jws.setUnprotected(unprotected);

        JWSJsonSerializationGenerator generator = new JWSJsonSerializationGenerator(jwsJsonSerializationObject,
                jwsJsonSerializationObject.getJWSSerializationType());
        return generator.generate();
    }

    private String transformToString(final DSSDocument dssDocument) {
        if (dssDocument instanceof InMemoryDocument) {
            final InMemoryDocument document = (InMemoryDocument) dssDocument;
            return new String(document.getBytes());
        } else {
            try (final InputStream is = dssDocument.openStream()) {
                final InMemoryDocument inMemoryDocument = new InMemoryDocument(is);
                return new String(inMemoryDocument.getBytes());
            } catch (IOException e) {
                throw new DSSException((e));
            }
        }
    }
}
