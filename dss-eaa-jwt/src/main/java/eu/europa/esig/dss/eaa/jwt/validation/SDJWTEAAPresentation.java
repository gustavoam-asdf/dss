package eu.europa.esig.dss.eaa.jwt.validation;

import eu.europa.esig.dss.eaa.common.validation.DefaultEAAPresentation;
import eu.europa.esig.dss.eaa.common.validation.EAAPayloadVerifier;
import eu.europa.esig.dss.eaa.jwt.claim.SDJWTClaimDeviceKey;
import eu.europa.esig.dss.enumerations.EAAPresentationType;
import eu.europa.esig.dss.jades.validation.JAdESSignature;
import eu.europa.esig.dss.model.eaa.Disclosure;
import eu.europa.esig.dss.model.eaa.claim.ClaimDeviceKey;
import eu.europa.esig.dss.spi.eaa.EAAPayload;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import eu.europa.esig.dss.spi.x509.CertificateSource;
import eu.europa.esig.dss.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * This class represents an SD-JWT VC object, as per IETF draft-ietf-oauth-selective-disclosure-jwt-22.
 *
 */
public class SDJWTEAAPresentation extends DefaultEAAPresentation {

    private static final Logger LOG = LoggerFactory.getLogger(SDJWTEAAPresentation.class);

    /**
     * Default constructor
     */
    protected SDJWTEAAPresentation() {
        // empty
    }

    /**
     * Instantiates a builder to create an {@code SDJWTPresentation} object
     *
     * @return {@link SDJWTEAAPresentationBuilder}
     */
    public static SDJWTEAAPresentationBuilder initBuilder() {
        return new SDJWTEAAPresentationBuilder();
    }

    @Override
    public EAAPresentationType getEAAPresentationType() {
        return EAAPresentationType.SD_JWT_VC;
    }

    @Override
    protected EAAPayloadVerifier initEAAPayloadVerifier() {
        List<AdvancedSignature> signatures = getSignatures();
        if (Utils.isCollectionEmpty(signatures)) {
            throw new IllegalStateException("SD-JWT VC signatures cannot be empty!");
        }
        JAdESSignature signature = (JAdESSignature) signatures.get(0); // payload is the same for EAA signatures
        return new SDJWTPayloadVerifier(signature.getJws().getDecodedPayload());
    }

    /**
     * This class is used to build a {@code eu.europa.esig.dss.eaa.jwt.validation.SDJWTPresentation} object
     *
     */
    public static class SDJWTEAAPresentationBuilder extends DefaultEAAPresentationBuilder {

        /**
         * Default constructor
         */
        public SDJWTEAAPresentationBuilder() {
            // empty
        }

        @Override
        public SDJWTEAAPresentationBuilder setSignatures(List<AdvancedSignature> signatures) {
            return (SDJWTEAAPresentationBuilder) super.setSignatures(signatures);
        }

        @Override
        public SDJWTEAAPresentationBuilder setDisclosures(List<Disclosure> disclosures) {
            return (SDJWTEAAPresentationBuilder) super.setDisclosures(disclosures);
        }

        @Override
        public SDJWTEAAPresentationBuilder setKeyBindingSignature(AdvancedSignature keyBindingSignature) {
            return (SDJWTEAAPresentationBuilder) super.setKeyBindingSignature(keyBindingSignature);
        }

        @Override
        public SDJWTEAAPresentationBuilder setFilename(String filename) {
            return (SDJWTEAAPresentationBuilder) super.setFilename(filename);
        }

        @Override
        protected DefaultEAAPresentation initEAAPresentation() {
            return new SDJWTEAAPresentation();
        }

        @Override
        public SDJWTEAAPresentation build() {
            return (SDJWTEAAPresentation) super.build();
        }

        @Override
        protected CertificateSource getHolderCertificateSource(EAAPayload eaaPayload) {
            ClaimDeviceKey claimDeviceKey = eaaPayload.getDeviceKey();
            if (claimDeviceKey != null) {
                try {
                    return new DeviceKeyClaimCertificateSource((SDJWTClaimDeviceKey) claimDeviceKey);
                } catch (Exception e) {
                    LOG.warn("Unable to read the device key claim : {}", e.getMessage(), e);
                }
            }
            return null;
        }

    }

}
