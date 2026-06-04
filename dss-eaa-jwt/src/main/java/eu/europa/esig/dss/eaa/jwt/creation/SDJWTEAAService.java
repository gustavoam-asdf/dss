package eu.europa.esig.dss.eaa.jwt.creation;

import eu.europa.esig.dss.eaa.common.creation.AbstractEAAService;
import eu.europa.esig.dss.eaa.common.creation.EAAPayloadBuilder;
import eu.europa.esig.dss.eaa.common.creation.EAAService;
import eu.europa.esig.dss.eaa.jwt.SDJWTConstants;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.MimeType;
import eu.europa.esig.dss.enumerations.MimeTypeEnum;
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
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.spi.validation.CertificateVerifier;
import eu.europa.esig.dss.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Implementation of {@link EAAService} to create SD-JWT EAA
 */
public class SDJWTEAAService extends AbstractEAAService<JAdESSignatureParameters, SDJWTEAAPayloadParameters, SDJWTEAAClaim, SDJWTEAADisclosure> {

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
        validatePayload(payload);
        ensureSignatureParameters(signatureParameters);
        return getJAdESService().getDataToSign(payload, signatureParameters);
    }

    @Override
    public ToBeSigned getDataToBeSigned(final SDJWTEAAPayloadParameters payloadParameters, final JAdESSignatureParameters signatureParameters) {
        ensureSignatureParameters(signatureParameters);
        ensurePayloadParameters(payloadParameters, signatureParameters);
        return getDataToBeSigned(getPayloadBuilder().buildPayload(payloadParameters), signatureParameters);
    }

    @Override
    public DSSDocument signEAA(final DSSDocument payload, final JAdESSignatureParameters signatureParameters, final SignatureValue signatureValue) {
        validatePayload(payload);
        ensureSignatureParameters(signatureParameters);
        return getJAdESService().signDocument(payload, signatureParameters, signatureValue);
    }

    @Override
    public DSSDocument signEAA(final SDJWTEAAPayloadParameters payloadParameters, final JAdESSignatureParameters signatureParameters, final SignatureValue signatureValue) {
        ensureSignatureParameters(signatureParameters);
        ensurePayloadParameters(payloadParameters, signatureParameters);
        return signEAA(getPayloadBuilder().buildPayload(payloadParameters), signatureParameters, signatureValue);
    }

    /**
     * This method verifies validity of the payload
     *
     * @param payload {@link DSSDocument} to be verified
     */
    protected void validatePayload(final DSSDocument payload) {
        Objects.requireNonNull(payload, "payload cannot be null!");

        if (!DSSJsonUtils.isJsonDocument(payload)) {
            throw new DSSException("Payload is not a JSON document!");
        }
    }

    /**
     * This method verifies validity of the signature parameters and provides the necessary configuration, where applicable
     *
     * @param signatureParameters {@link JAdESSignatureParameters}
     */
    protected void ensureSignatureParameters(final JAdESSignatureParameters signatureParameters) {
        Objects.requireNonNull(signatureParameters, "signatureParameters cannot be null!");

        if (signatureParameters.getSignatureLevel() == null) {
            signatureParameters.setSignatureLevel(SignatureLevel.JAdES_BASELINE_B);
            LOG.debug("SignatureLevel is absent and was set to '{}'", SignatureLevel.JAdES_BASELINE_B);

        } else if (SignatureLevel.JAdES_BASELINE_B != signatureParameters.getSignatureLevel()) {
            throw new IllegalArgumentException("Signature level must be JAdES-BASELINE-B!");
        }

        if (signatureParameters.getSignaturePackaging() == null) {
            signatureParameters.setSignaturePackaging(SignaturePackaging.ENVELOPING);
            LOG.debug("SignaturePackaging is absent and was set to '{}'", SignaturePackaging.ENVELOPING);

        } else if (SignaturePackaging.ENVELOPING != signatureParameters.getSignaturePackaging()) {
            throw new IllegalArgumentException("Signature packaging must be ENVELOPING");
        }

        if (signatureParameters.getSignatureType() == null) {
            signatureParameters.setSignatureType(MimeTypeEnum.SD_JWT_VC.getMimeTypeString());
            LOG.debug("SignatureType is absent and was set to '{}'", MimeTypeEnum.SD_JWT_VC.getMimeTypeString());
        }

        ensureSigningCertificateDigestAlgorithm(signatureParameters);
    }

    /**
     * This method ensures compliance of the used digest algorithm for signing-certificate signed attribute definition
     *
     * @param signatureParameters {@link JAdESSignatureParameters}
     */
    protected void ensureSigningCertificateDigestAlgorithm(final JAdESSignatureParameters signatureParameters) {
        // TODO : remove the method should the ETSI TS 119 472-1 be updated
        if (DigestAlgorithm.SHA256 != signatureParameters.getSigningCertificateDigestMethod()) {
            LOG.info("ETSI TS 119 472-1 v1.2.1 requires SHA256 to be used for the signing-certificate signed attribute definition. " +
                    "The value is enforced to DigestAlgorithm.SHA256. Should you need to use a different algorithm, " +
                    "please override the MdocEAAService#ensureSigningCertificateDigestAlgorithm method.");
            signatureParameters.setSigningCertificateDigestMethod(DigestAlgorithm.SHA256);
        }
    }

    /**
     * This method verifies validity and/or provides some mandatory payload parameters for EAA creation
     *
     * @param payloadParameters {@link SDJWTEAAPayloadParameters}
     * @param signatureParameters {@link JAdESSignatureParameters}
     */
    protected void ensurePayloadParameters(final SDJWTEAAPayloadParameters payloadParameters, final JAdESSignatureParameters signatureParameters) {
        if (payloadParameters.getNotBeforeDate() == null) {
            payloadParameters.setNotBeforeDate(signatureParameters.bLevel().getSigningDate());
            LOG.debug("EAA 'nbf' date is absent and was set to {}", signatureParameters.bLevel().getSigningDate());
        }
        if (payloadParameters.getExpirationDate() == null && signatureParameters.getSigningCertificate() != null) {
            payloadParameters.setExpirationDate(signatureParameters.getSigningCertificate().getNotAfter());
            LOG.debug("EAA 'exp' date is absent and was set to {}", signatureParameters.getSigningCertificate().getNotAfter());
        }
        if (Utils.isStringBlank(payloadParameters.getVerifiableCredentialsType())) {
            LOG.warn("EAA 'vct' claim shall be defined! Absence of the value may lead to interoperability issued. " +
                    "Please use SDJWTEAAPayloadParameters#setVerifiableCredentialsType method to provide the value.");
        }
        if (payloadParameters.getVerifiableCredentialsTypeIntegrity() == null) {
            LOG.warn("EAA 'vct#integrity' claim shall be defined! Absence of the value may lead to interoperability issued. " +
                    "Please use SDJWTEAAPayloadParameters#setVerifiableCredentialsTypeIntegrity method to provide the value.");
        }
    }

    @Override
    public ToBeSigned getDataToSignForKeybindingSignature(final DSSDocument eaa, final JAdESSignatureParameters signatureParameters) {
        return null;
    }

    @Override
    public ToBeSigned getDataToSignForKeybindingSignature(final DSSDocument eaa, final List<SDJWTEAADisclosure> disclosures, final JAdESSignatureParameters signatureParameters) {
        return null;
    }

    @Override
    public DSSDocument createKeybindingSignature(final DSSDocument eea, final JAdESSignatureParameters signatureParameters, final SignatureValue signatureValue) {
        return null;
    }

    @Override
    public DSSDocument createKeybindingSignature(final DSSDocument eea, final List<SDJWTEAADisclosure> disclosures, final JAdESSignatureParameters signatureParameters,
                                                 final SignatureValue signatureValue) {
        return null;
    }

    /**
     * Gets the JAdES service for a signature creation
     *
     * @return {@link JAdESService}
     */
    protected JAdESService getJAdESService() {
        return new JAdESService(certificateVerifier);
    }

    @Override
    public List<SDJWTEAADisclosure> getDisclosures(final SDJWTEAAPayloadParameters payloadParameters) {
        Objects.requireNonNull(payloadParameters, "SDJWTEAAPayloadParameters cannot be null!");
        return getPayloadBuilder().buildDisclosures(payloadParameters);
    }

    @Override
    protected EAAPayloadBuilder<SDJWTEAAPayloadParameters, SDJWTEAAClaim, SDJWTEAADisclosure> initDefaultPayloadBuilder() {
        return new SDJWTPayloadBuilder();
    }

    @Override
    public DSSDocument issuePresentation(final DSSDocument eaa, final List<SDJWTEAADisclosure> disclosures, final DSSDocument keyBinding) {
        Objects.requireNonNull(eaa, "The EAA cannot be null!");
        JWSCompactSerializationParser compactParser = new JWSCompactSerializationParser(eaa);
        if (compactParser.isSupported()) {
            DSSDocument eaaPresentation = issueJWSCompactPresentation(eaa, disclosures, keyBinding);
            eaaPresentation.setName(getFinalDocumentName(eaa));
            eaaPresentation.setMimeType(getEAAPresentationMimeType());
            return eaaPresentation;
        }

        JWSJsonSerializationParser jwsJsonSerializationParser = new JWSJsonSerializationParser(eaa);
        if (jwsJsonSerializationParser.isSupported()) {
            DSSDocument eaaPresentation =  issueJWSJsonSerializationPresentation(jwsJsonSerializationParser.parse(), disclosures, keyBinding);
            eaaPresentation.setName(getFinalDocumentName(eaa));
            eaaPresentation.setMimeType(getEAAPresentationMimeType());
            return eaaPresentation;
        }

        throw new DSSException("The signed EAA must be a JWS Signature");
    }

    @Override
    public DSSDocument issuePresentation(final DSSDocument eaa, final List<SDJWTEAADisclosure> disclosures) {
        return issuePresentation(eaa, disclosures, null);
    }

    @Override
    public DSSDocument issuePresentation(final DSSDocument eaa, final DSSDocument keybinding) {
        return issuePresentation(eaa, Collections.emptyList(), keybinding);
    }

    private DSSDocument issueJWSCompactPresentation(final DSSDocument eaa, final List<SDJWTEAADisclosure> disclosures, final DSSDocument keyBinding) {
        String signedEaa = new String(DSSUtils.toByteArray(eaa));

        StringBuilder issuedEaa = new StringBuilder(signedEaa).append("~");
        if (disclosures != null && !disclosures.isEmpty()) {
            for (SDJWTEAADisclosure disclosure : disclosures) {
                issuedEaa.append(disclosure.getDisclosure()).append("~");
            }
        }

        if (keyBinding != null) {
            String keyBindingValue = new String(DSSUtils.toByteArray(keyBinding));
            issuedEaa.append(keyBindingValue);
        }

        return new InMemoryDocument(issuedEaa.toString().getBytes());
    }

    private DSSDocument issueJWSJsonSerializationPresentation(JWSJsonSerializationObject jwsJsonSerializationObject, final List<SDJWTEAADisclosure> disclosures, final DSSDocument keyBinding) {
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
            List<String> disclosureList = disclosures.stream().map(SDJWTEAADisclosure::getDisclosure).collect(Collectors.toList());
            unprotected.put(SDJWTConstants.DISCLOSURES, disclosureList);
        }

        if (keyBinding != null) {
            String keyBindingValue = new String(DSSUtils.toByteArray(keyBinding));
            unprotected.put(SDJWTConstants.KB_JWT, keyBindingValue);
        }

        jws.setUnprotected(unprotected);

        JWSJsonSerializationGenerator generator = new JWSJsonSerializationGenerator(jwsJsonSerializationObject,
                jwsJsonSerializationObject.getJWSSerializationType());
        return generator.generate();
    }

    @Override
    protected MimeType getEAAPresentationMimeType() {
        return MimeTypeEnum.JSON; // TODO : improve
    }

}
