package eu.europa.esig.dss.eaa.jwt.validation;

import eu.europa.esig.dss.eaa.common.validation.DefaultEAA;
import eu.europa.esig.dss.eaa.common.validation.EAAPayloadVerifier;
import eu.europa.esig.dss.enumerations.EAAType;
import eu.europa.esig.dss.jades.validation.JAdESSignature;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.model.eaa.Disclosure;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import eu.europa.esig.dss.utils.Utils;

import java.util.List;

/**
 * This class represents an SD-JWT VC object, as per IETF draft-ietf-oauth-selective-disclosure-jwt-22.
 *
 */
public class SDJWTEAA extends DefaultEAA {

    /**
     * Default constructor
     */
    protected SDJWTEAA() {
        // empty
    }

    /**
     * Instantiates a builder to create an {@code SDJWTEAA} object
     *
     * @return {@link SDJWTEAABuilder}
     */
    public static SDJWTEAABuilder initBuilder() {
        return new SDJWTEAABuilder();
    }

    @Override
    public EAAType getEAAType() {
        return EAAType.SD_JWT_VC;
    }

    @Override
    protected EAAPayloadVerifier initEAAPayloadVerifier() {
        List<AdvancedSignature> signatures = getSignatures();
        if (Utils.isCollectionEmpty(signatures)) {
            throw new IllegalStateException("SD-JWT VC signatures cannot be empty!");
        }
        JAdESSignature signature = (JAdESSignature) signatures.get(0); // payload is the same for EAA signatures
        try {
            return new SDJWTPayloadVerifier(signature.getJws().getDecodedPayload());
        } catch (Exception e) {
            throw new DSSException(String.format("Unable to read SD-JWT payload : %s", e.getMessage()), e);
        }
    }

    /**
     * This class is used to build a {@code eu.europa.esig.dss.eaa.jwt.validation.SDJWTEAA} object
     *
     */
    public static class SDJWTEAABuilder extends DefaultEAABuilder {

        /**
         * Default constructor
         */
        public SDJWTEAABuilder() {
            // empty
        }

        @Override
        public SDJWTEAABuilder setSignatures(List<AdvancedSignature> signatures) {
            return (SDJWTEAABuilder) super.setSignatures(signatures);
        }

        @Override
        public SDJWTEAABuilder setDisclosures(List<Disclosure> disclosures) {
            return (SDJWTEAABuilder) super.setDisclosures(disclosures);
        }

        @Override
        public SDJWTEAABuilder setKeyBindingSignature(AdvancedSignature keyBindingSignature) {
            return (SDJWTEAABuilder) super.setKeyBindingSignature(keyBindingSignature);
        }

        @Override
        public SDJWTEAABuilder setFilename(String filename) {
            return (SDJWTEAABuilder) super.setFilename(filename);
        }

        @Override
        protected DefaultEAA initEAA() {
            return new SDJWTEAA();
        }

        @Override
        public SDJWTEAA build() {
            return (SDJWTEAA) super.build();
        }

    }

}
