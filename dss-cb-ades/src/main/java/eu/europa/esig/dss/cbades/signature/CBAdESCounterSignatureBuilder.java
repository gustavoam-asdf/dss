package eu.europa.esig.dss.cbades.signature;

import eu.europa.esig.dss.model.DSSDocument;

/**
 * Creates a CB-AdES Counter signature
 * 
 */
public class CBAdESCounterSignatureBuilder extends CBAdESExtensionBuilder {

    /**
     * Default constructor
     */
    public CBAdESCounterSignatureBuilder() {
        // empty
    }

    /**
     * Extract SignatureValue binaries from the provided CB-AdES signature
     *
     * @param signatureDocument {@link DSSDocument} to be counter-signed
     * @param parameters {@link CBAdESCounterSignatureParameters}
     * @return {@link DSSDocument} extracted SignatureValue
     */
    public DSSDocument getSignatureValueToBeSigned(DSSDocument signatureDocument, CBAdESCounterSignatureParameters parameters) {
        // TODO : to be implemented
        return null;
    }

    /**
     * Embeds and returns the embedded counter signature into the original CBAdES signature
     *
     * @param signatureDocument {@link DSSDocument} the original document containing the signature to be counter signed
     * @param counterSignature {@link DSSDocument} the counter signature
     * @param parameters {@link CBAdESCounterSignatureParameters}
     * @return {@link DSSDocument} original signature enveloping the {@code counterSignature} in an unprotected header
     */
    public DSSDocument buildEmbeddedCounterSignature(DSSDocument signatureDocument, DSSDocument counterSignature,
                                                     CBAdESCounterSignatureParameters parameters) {
        // TODO : to be implemented
        return null;
    }

}
