package eu.europa.esig.dss.eaa.mdoc.validation;

import eu.europa.esig.dss.cbades.validation.CBAdESSignature;
import eu.europa.esig.dss.eaa.common.validation.DefaultEAA;
import eu.europa.esig.dss.eaa.common.validation.EAAPayloadVerifier;
import eu.europa.esig.dss.eaa.mdoc.model.MdocDocument;
import eu.europa.esig.dss.enumerations.EAAType;
import eu.europa.esig.dss.model.eaa.ValidationDisclosure;
import eu.europa.esig.dss.spi.signature.AdvancedSignature;
import eu.europa.esig.dss.utils.Utils;

import java.util.List;

/**
 * Represents an EAA presentation embedded within an mdoc response
 *
 */
public class MdocEAA extends DefaultEAA {

    /** Document mdoc object enveloping the EAA */
    private MdocDocument document;

    /**
     * Default constructor
     */
    protected MdocEAA() {
        // empty
    }

    /**
     * Instantiates a builder to create an {@code SDJWTPresentation} object
     *
     * @return {@link MdocEAABuilder}
     */
    public static MdocEAABuilder initBuilder() {
        return new MdocEAABuilder();
    }

    /**
     * Gets mdoc Document
     *
     * @return {@link MdocDocument}
     */
    public MdocDocument getDocument() {
        return document;
    }

    /**
     * Sets the Document
     *
     * @param document {@link MdocDocument}
     */
    public void setDocument(MdocDocument document) {
        this.document = document;
    }

    @Override
    public EAAType getEAAType() {
        return EAAType.ISO_IEC_MDOC;
    }

    @Override
    protected EAAPayloadVerifier initEAAPayloadVerifier() {
        List<AdvancedSignature> signatures = getSignatures();
        if (Utils.isCollectionEmpty(signatures)) {
            throw new IllegalStateException("SD-JWT VC signatures cannot be empty!");
        }
        CBAdESSignature signature = (CBAdESSignature) signatures.get(0); // payload is the same for EAA signatures within the same mdoc Document
        MdocEAAPayloadVerifier payloadVerifier = new MdocEAAPayloadVerifier(signature.getCoseSignature().getPayload());
        if (document != null) {
            payloadVerifier.setDocType(document.getDocType());
        }
        return payloadVerifier;
    }

    /**
     * This class is used to build a {@code eu.europa.esig.dss.eaa.mdoc.validation.MdocEAA} object
     *
     */
    public static class MdocEAABuilder extends DefaultEAABuilder {

        /** Document mdoc object enveloping the EAA */
        private MdocDocument document;

        /**
         * Default constructor
         */
        public MdocEAABuilder() {
            // empty
        }

        @Override
        public MdocEAABuilder setSignatures(List<AdvancedSignature> signatures) {
            return (MdocEAABuilder) super.setSignatures(signatures);
        }

        @Override
        public MdocEAABuilder setDisclosures(List<ValidationDisclosure> disclosures) {
            return (MdocEAABuilder) super.setDisclosures(disclosures);
        }

        @Override
        public MdocEAABuilder setKeyBindingSignature(AdvancedSignature keyBindingSignature) {
            return (MdocEAABuilder) super.setKeyBindingSignature(keyBindingSignature);
        }

        @Override
        public MdocEAABuilder setFilename(String filename) {
            return (MdocEAABuilder) super.setFilename(filename);
        }

        /**
         * Sets the mdoc docType
         *
         * @param document {@link String}
         * @return {@link MdocEAABuilder}
         */
        public MdocEAABuilder setDocument(MdocDocument document) {
            this.document = document;
            return this;
        }

        @Override
        protected DefaultEAA initEAA() {
            return new MdocEAA();
        }

        @Override
        public MdocEAA build() {
            MdocEAA mdocEAA = (MdocEAA) super.build();
            mdocEAA.setDocument(document);
            return mdocEAA;
        }

    }

}
