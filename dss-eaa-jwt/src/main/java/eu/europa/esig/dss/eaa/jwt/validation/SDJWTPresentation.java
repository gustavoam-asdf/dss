package eu.europa.esig.dss.eaa.jwt.validation;

import eu.europa.esig.dss.eaa.common.validation.DefaultEAAPresentation;
import eu.europa.esig.dss.enumerations.EAAPresentationType;
import eu.europa.esig.dss.jades.validation.JAdESSignature;
import eu.europa.esig.dss.model.EAADisclosure;
import eu.europa.esig.dss.model.eaa.DisclosureValidation;
import eu.europa.esig.dss.spi.eaa.EAAPayload;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import eu.europa.esig.dss.utils.Utils;

import java.util.Collections;
import java.util.List;

/**
 * This class represents an SD-JWT VC object, as per IETF draft-ietf-oauth-selective-disclosure-jwt-22.
 *
 */
public class SDJWTPresentation extends DefaultEAAPresentation {

    /**
     * Default constructor
     */
    protected SDJWTPresentation() {
        // empty
    }

    /**
     * Instantiates a builder to create an {@code SDJWTPresentation} object
     *
     * @return {@link SDJWTPresentationBuilder}
     */
    public static SDJWTPresentationBuilder initBuilder() {
        return new SDJWTPresentationBuilder();
    }

    @Override
    public EAAPresentationType getEAAPresentationType() {
        return EAAPresentationType.SD_JWT_VC;
    }

    @Override
    protected List<DisclosureValidation> validateDisclosures() {
        // TODO : to be implemented
        return Collections.emptyList();
    }

    @Override
    protected EAAPayload buildPayload() {
        List<AdvancedSignature> signatures = getSignatures();
        if (Utils.isCollectionEmpty(signatures)) {
            throw new IllegalStateException("SD-JWT VC signatures cannot be empty!");
        }
        JAdESSignature signature = (JAdESSignature) signatures.get(0); // payload is the same for EAA signatures
        return new SDJWTPayload(signature.getJws().getUnverifiedPayload());
    }

    /**
     * This class is used to build a {@code eu.europa.esig.dss.eaa.jwt.validation.SDJWTPresentation} object
     *
     */
    public static class SDJWTPresentationBuilder extends DefaultEAAPresentationBuilder {

        /**
         * Default constructor
         */
        public SDJWTPresentationBuilder() {
            // empty
        }

        @Override
        public SDJWTPresentationBuilder setSignatures(List<AdvancedSignature> signatures) {
            return (SDJWTPresentationBuilder) super.setSignatures(signatures);
        }

        @Override
        public SDJWTPresentationBuilder setDisclosures(List<EAADisclosure> disclosures) {
            return (SDJWTPresentationBuilder) super.setDisclosures(disclosures);
        }

        @Override
        public SDJWTPresentationBuilder setKeyBindingSignature(AdvancedSignature keyBindingSignature) {
            return (SDJWTPresentationBuilder) super.setKeyBindingSignature(keyBindingSignature);
        }

        @Override
        public SDJWTPresentationBuilder setFilename(String filename) {
            return (SDJWTPresentationBuilder) super.setFilename(filename);
        }

        @Override
        protected DefaultEAAPresentation initEAAPresentation() {
            return new SDJWTPresentation();
        }

    }

}
