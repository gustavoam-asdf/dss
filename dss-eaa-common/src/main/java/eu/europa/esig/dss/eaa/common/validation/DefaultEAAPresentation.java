package eu.europa.esig.dss.eaa.common.validation;

import eu.europa.esig.dss.eaa.common.validation.identifier.EAAPresentationIdentifierBuilder;
import eu.europa.esig.dss.model.EAADisclosure;
import eu.europa.esig.dss.model.eaa.DisclosureValidation;
import eu.europa.esig.dss.model.identifier.Identifier;
import eu.europa.esig.dss.spi.eaa.EAAPresentation;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
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
    private List<EAADisclosure> disclosures;

    /** Key binding signature (optional) */
    private AdvancedSignature keyBindingSignature;

    /** The name of the EAA document */
    private String filename;

    /** Unique EAA Presentation identifier */
    private Identifier identifier;

    /** Cached list of the disclosure validation */
    private List<DisclosureValidation> disclosureValidations;

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
     * @return a list of {@link EAADisclosure}s
     */
    public List<EAADisclosure> getDisclosures() {
        return disclosures;
    }

    @Override
    public List<DisclosureValidation> getDisclosureValidations() {
        if (disclosureValidations == null) {
            disclosureValidations = validateDisclosures();
        }
        return disclosureValidations;
    }

    /**
     * Validates attached disclosures
     *
     * @return a list of {@link DisclosureValidation}s
     */
    protected abstract List<DisclosureValidation> validateDisclosures();

    @Override
    public AdvancedSignature getKeyBindingSignature() {
        return keyBindingSignature;
    }

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
        private List<EAADisclosure> disclosures;

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
         * @param disclosures a list of {@link EAADisclosure}s
         * @return this builder
         */
        public DefaultEAAPresentationBuilder setDisclosures(List<EAADisclosure> disclosures) {
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
            eaaPresentation.keyBindingSignature = keyBindingSignature;
            eaaPresentation.filename = filename;
            return eaaPresentation;
        }

        protected abstract DefaultEAAPresentation initEAAPresentation();

    }

}
