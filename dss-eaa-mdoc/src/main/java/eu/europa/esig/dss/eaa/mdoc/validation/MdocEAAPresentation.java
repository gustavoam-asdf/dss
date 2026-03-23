package eu.europa.esig.dss.eaa.mdoc.validation;

import eu.europa.esig.dss.cbades.validation.CBAdESSignature;
import eu.europa.esig.dss.eaa.common.validation.DefaultEAAPresentation;
import eu.europa.esig.dss.eaa.common.validation.EAAPayloadVerifier;
import eu.europa.esig.dss.enumerations.EAAPresentationType;
import eu.europa.esig.dss.model.eaa.Disclosure;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import eu.europa.esig.dss.utils.Utils;

import java.util.List;

/**
 * Represents an EAA presentation embedded within an mdoc response
 *
 */
public class MdocEAAPresentation extends DefaultEAAPresentation {

    /** Type of the mdoc, represented in a form of a namespace */
    private String documentType;

    /**
     * Default constructor
     */
    protected MdocEAAPresentation() {
        // empty
    }

    /**
     * Instantiates a builder to create an {@code SDJWTPresentation} object
     *
     * @return {@link MdocEAAPresentationBuilder}
     */
    public static MdocEAAPresentationBuilder initBuilder() {
        return new MdocEAAPresentationBuilder();
    }

    /**
     * Gets mdoc document type
     *
     * @return {@link String}
     */
    public String getDocumentType() {
        return documentType;
    }

    /**
     * Sets the document type
     *
     * @param documentType {@link String}
     */
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    @Override
    public EAAPresentationType getEAAPresentationType() {
        return EAAPresentationType.ISO_IEC_MDOC;
    }

    @Override
    protected EAAPayloadVerifier initEAAPayloadVerifier() {
        List<AdvancedSignature> signatures = getSignatures();
        if (Utils.isCollectionEmpty(signatures)) {
            throw new IllegalStateException("SD-JWT VC signatures cannot be empty!");
        }
        CBAdESSignature signature = (CBAdESSignature) signatures.get(0); // payload is the same for EAA signatures within the same mdoc Document
        return new MdocEAAPayloadVerifier(signature.getCoseSignature().getPayload());
    }

    /**
     * This class is used to build a {@code eu.europa.esig.dss.eaa.mdoc.validation.MdocEAAPresentation} object
     *
     */
    public static class MdocEAAPresentationBuilder extends DefaultEAAPresentationBuilder {

        /** Type of the mdoc, represented in a form of a namespace */
        private String documentType;

        /**
         * Default constructor
         */
        public MdocEAAPresentationBuilder() {
            // empty
        }

        @Override
        public MdocEAAPresentationBuilder setSignatures(List<AdvancedSignature> signatures) {
            return (MdocEAAPresentationBuilder) super.setSignatures(signatures);
        }

        @Override
        public MdocEAAPresentationBuilder setDisclosures(List<Disclosure> disclosures) {
            return (MdocEAAPresentationBuilder) super.setDisclosures(disclosures);
        }

        @Override
        public MdocEAAPresentationBuilder setKeyBindingSignature(AdvancedSignature keyBindingSignature) {
            return (MdocEAAPresentationBuilder) super.setKeyBindingSignature(keyBindingSignature);
        }

        @Override
        public MdocEAAPresentationBuilder setFilename(String filename) {
            return (MdocEAAPresentationBuilder) super.setFilename(filename);
        }

        /**
         * Sets the mdoc docType
         *
         * @param documentType {@link String}
         * @return {@link MdocEAAPresentationBuilder}
         */
        public MdocEAAPresentationBuilder setDocumentType(String documentType) {
            this.documentType = documentType;
            return this;
        }

        @Override
        protected DefaultEAAPresentation initEAAPresentation() {
            return new MdocEAAPresentation();
        }

        @Override
        public MdocEAAPresentation build() {
            MdocEAAPresentation mdocEAAPresentation = (MdocEAAPresentation) super.build();
            mdocEAAPresentation.setDocumentType(documentType);
            return mdocEAAPresentation;
        }

    }

}
