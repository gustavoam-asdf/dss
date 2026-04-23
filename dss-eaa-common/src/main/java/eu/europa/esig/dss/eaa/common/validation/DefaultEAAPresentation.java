package eu.europa.esig.dss.eaa.common.validation;

import eu.europa.esig.dss.eaa.common.validation.identifier.EAAPresentationIdentifierBuilder;
import eu.europa.esig.dss.model.eaa.Disclosure;
import eu.europa.esig.dss.model.eaa.DisclosureValidation;
import eu.europa.esig.dss.model.identifier.Identifier;
import eu.europa.esig.dss.spi.eaa.EAAPayload;
import eu.europa.esig.dss.spi.eaa.EAAPresentation;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import eu.europa.esig.dss.spi.x509.CertificateSource;
import eu.europa.esig.dss.spi.x509.ListCertificateSource;
import eu.europa.esig.dss.utils.Utils;

import java.util.List;

/**
 * Abstract implementation of an EAA Presentation
 *
 */
public abstract class DefaultEAAPresentation implements EAAPresentation {

    /** Cached signature objects used to create the EAA */
    private List<AdvancedSignature> signatures;

    /** List of disclosures attached to the EAA Presentation */
    private List<Disclosure> disclosures;

    /** Key binding signature (optional) */
    private AdvancedSignature keyBindingSignature;

    /** The name of the EAA document */
    private String filename;

    /** Unique EAA Presentation identifier */
    private Identifier identifier;

    /** Cached instance of an EAA Payload Verifier */
    private EAAPayloadVerifier eaaPayloadVerifier;

    /**
     * Default constructor
     */
    protected DefaultEAAPresentation() {
        // empty
    }

    @Override
    public String getFilename() {
        return filename;
    }

    @Override
    public List<AdvancedSignature> getSignatures() {
        return signatures;
    }

    /**
     * Gets a list of disclosures
     *
     * @return a list of {@link Disclosure}s
     */
    public List<Disclosure> getDisclosures() {
        return disclosures;
    }

    @Override
    public List<DisclosureValidation> getDisclosureValidations() {
        return getEAAPayloadVerifier().getDisclosureValidations();
    }

    @Override
    public AdvancedSignature getKeyBindingSignature() {
        return keyBindingSignature;
    }

    @Override
    public EAAPayload getPayload() {
        return getEAAPayloadVerifier().getVerifiedPayload();
    }

    /**
     * Gets the EAA Payload Verifier, performing a verification of the attached disclosures as well as
     * building a constructed version of the EAA Payload with the discloses values attached
     *
     * @return {@link EAAPayloadVerifier}
     */
    protected EAAPayloadVerifier getEAAPayloadVerifier() {
        if (eaaPayloadVerifier == null) {
            eaaPayloadVerifier = initEAAPayloadVerifier().setDisclosures(disclosures);
            eaaPayloadVerifier.verify();
        }
        return eaaPayloadVerifier;
    }

    /**
     * Creates a new instance of {@code EAAPayloadVerifier} relatively to the current implementation
     *
     * @return {@link EAAPayloadVerifier}
     */
    protected abstract EAAPayloadVerifier initEAAPayloadVerifier();

    @Override
    public String getId() {
        return getDSSId().asXmlId();
    }

    @Override
    public Identifier getDSSId() {
        if (identifier == null) {
            identifier = new EAAPresentationIdentifierBuilder().build(this);
        }
        return identifier;
    }

    /**
     * This class is used to build a DefaultEAAPresentation
     *
     */
    protected static abstract class DefaultEAAPresentationBuilder {

        /** Cached signature objects used to create the EAA */
        private List<AdvancedSignature> signatures;

        /** List of disclosures attached to the EAA Presentation */
        private List<Disclosure> disclosures;

        /** Key binding signature (optional) */
        private AdvancedSignature keyBindingSignature;

        /** The name of the EAA document */
        private String filename;

        /**
         * Default constructor
         */
        public DefaultEAAPresentationBuilder() {
            // empty
        }

        /**
         * Sets signatures list used to create the EAA
         *
         * @param signatures a list of {@link AdvancedSignature}s
         * @return this builder
         */
        public DefaultEAAPresentationBuilder setSignatures(List<AdvancedSignature> signatures) {
            this.signatures = signatures;
            return this;
        }

        /**
         * Sets a list of disclosures provided with the SD-JWT VC token
         *
         * @param disclosures a list of {@link Disclosure}s
         * @return this builder
         */
        public DefaultEAAPresentationBuilder setDisclosures(List<Disclosure> disclosures) {
            this.disclosures = disclosures;
            return this;
        }

        /**
         * Sets the key binding signature, when present
         *
         * @param keyBindingSignature {@link AdvancedSignature}
         * @return this builder
         */
        public DefaultEAAPresentationBuilder setKeyBindingSignature(AdvancedSignature keyBindingSignature) {
            this.keyBindingSignature = keyBindingSignature;
            return this;
        }

        /**
         * Sets the document filename
         *
         * @param filename {@link String}
         * @return this builder
         */
        public DefaultEAAPresentationBuilder setFilename(String filename) {
            this.filename = filename;
            return this;
        }

        /**
         * Builds a new EAA Presentation object
         *
         * @return {@link DefaultEAAPresentation}
         */
        public DefaultEAAPresentation build() {
            if (Utils.isCollectionEmpty(signatures)) {
                throw new NullPointerException("Signatures list cannot be null or empty!");
            }
            DefaultEAAPresentation eaaPresentation = initEAAPresentation();
            eaaPresentation.signatures = signatures;
            eaaPresentation.disclosures = disclosures;
            if (keyBindingSignature != null) {
                CertificateSource signingCertificateSource = new ListCertificateSource(
                        getHolderCertificateSource(eaaPresentation.getPayload()), getSigningCertificateSource(signatures));
                keyBindingSignature.setSigningCertificateSource(signingCertificateSource);
                eaaPresentation.keyBindingSignature = keyBindingSignature;
            }
            eaaPresentation.filename = filename;
            return eaaPresentation;
        }

        /**
         * Gets a certificate source containing a key of the EAA holder
         *
         * @param eaaPayload {@link EAAPayload}
         * @return {@link CertificateSource}
         */
        protected abstract CertificateSource getHolderCertificateSource(EAAPayload eaaPayload);

        private CertificateSource getSigningCertificateSource(List<AdvancedSignature> signatures) {
            AdvancedSignature signature = signatures.get(0);
            return signature.getSigningCertificateSource();
        }

        /**
         * Instantiates a new {@code DefaultEAAPresentation} object
         *
         * @return {@link DefaultEAAPresentation}
         */
        protected abstract DefaultEAAPresentation initEAAPresentation();

    }

}
