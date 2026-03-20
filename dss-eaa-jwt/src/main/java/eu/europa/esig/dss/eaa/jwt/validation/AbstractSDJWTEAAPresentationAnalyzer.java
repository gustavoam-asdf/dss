package eu.europa.esig.dss.eaa.jwt.validation;

import eu.europa.esig.dss.eaa.common.validation.DefaultEAAPresentationAnalyzer;
import eu.europa.esig.dss.eaa.jwt.SDJWTSerializationObject;
import eu.europa.esig.dss.jades.JWSJsonSerializationObject;
import eu.europa.esig.dss.jades.validation.JAdESSignature;
import eu.europa.esig.dss.jades.validation.JWS;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.spi.eaa.EAAPresentation;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import eu.europa.esig.dss.utils.Utils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Performs validation of a JWT based presentation of Electronic Attestation of Attributes. e.g. SD-JWT VC
 *
 */
public abstract class AbstractSDJWTEAAPresentationAnalyzer extends DefaultEAAPresentationAnalyzer {

    /** Cached instance of a parsed SD-JWT VC object */
    private SDJWTSerializationObject sdJWTSerializationObject;

    /**
     * Default constructor
     */
    protected AbstractSDJWTEAAPresentationAnalyzer() {
        // empty
    }

    /**
     * Default constructor
     *
     * @param document {@link DSSDocument} to validate
     */
    protected AbstractSDJWTEAAPresentationAnalyzer(DSSDocument document) {
        Objects.requireNonNull(document, "Document to be validated cannot be null!");

        this.document = document;
        this.sdJWTSerializationObject = buildSDJWTSerializationObject();
    }

    /**
     * Builds a {@code SDJWTSerializationObject}
     *
     * @return {@link SDJWTSerializationObject}
     */
    protected abstract SDJWTSerializationObject buildSDJWTSerializationObject();

    @Override
    protected List<EAAPresentation> buildEAAPresentations() {
        SDJWTEAAPresentation sdJwtEaa = SDJWTEAAPresentation.initBuilder()
                .setSignatures(getSignatures(sdJWTSerializationObject))
                .setDisclosures(sdJWTSerializationObject.getDisclosures())
                .setKeyBindingSignature(getKeyBindingSignature(sdJWTSerializationObject))
                .setFilename(document.getName())
                .build();
        return Collections.singletonList(sdJwtEaa); // only one EAA is possible
    }

    /**
     * Gets a list of {@code AdvancedSignature}s from a {@code SDJWTSerializationObject} object
     *
     * @param sdJwtSerializationObject {@link SDJWTSerializationObject} to extract EAA Presentation signatures from
     * @return a list of {@link AdvancedSignature}s
     */
    protected List<AdvancedSignature> getSignatures(SDJWTSerializationObject sdJwtSerializationObject) {
        JWSJsonSerializationObject signature = sdJwtSerializationObject.getSignature();
        if (signature == null) {
            throw new IllegalStateException("Signature cannot be absent within SD-JWS VC token!");
        }
        List<JWS> jwsSignatures = signature.getSignatures();
        if (Utils.isCollectionEmpty(jwsSignatures)) {
            throw new IllegalStateException("Signatures cannot be null or empty within SD-JWS VC token!");
        }
        return jwsSignatures.stream().map(this::buildSignature).collect(Collectors.toList());
    }

    /**
     * Gets a key binding {@code AdvancedSignature}, when present
     *
     * @param sdJwtSerializationObject {@link SDJWTSerializationObject}
     * @return {@link AdvancedSignature}
     */
    protected AdvancedSignature getKeyBindingSignature(SDJWTSerializationObject sdJwtSerializationObject) {
        JWSJsonSerializationObject keyBindingSignature = sdJwtSerializationObject.getKeyBindingSignature();
        if (keyBindingSignature == null) {
            return null;
        }
        List<JWS> jwsKeyBindingList = keyBindingSignature.getSignatures();
        if (Utils.isCollectionEmpty(jwsKeyBindingList)) {
            // should not happen
            return null;
        } else if (Utils.collectionSize(jwsKeyBindingList) != 1) {
            throw new IllegalStateException("Only one Key Binding signature is expected within SD-JWT VC token!");
        }
        return buildSignature(jwsKeyBindingList.get(0));
    }

    /**
     * This method build a JAdES Signature from a {@code JWS} object
     *
     * @param jws {@link JWS}
     * @return {@link JAdESSignature}
     */
    protected JAdESSignature buildSignature(JWS jws) {
        JAdESSignature jadesSignature = new JAdESSignature(jws);
        jadesSignature.setFilename(document.getName());
        jadesSignature.setSigningCertificateSource(signingCertificateSource);
        jadesSignature.setDetachedContents(detachedContents);
        jadesSignature.initBaselineRequirementsChecker(certificateVerifier);
        validateSignaturePolicy(jadesSignature);
        return jadesSignature;
    }

}
