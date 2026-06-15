package eu.europa.esig.dss.cbades.signature;

import eu.europa.esig.dss.model.SerializableCounterSignatureParameters;

/**
 * Parameters to create a CB-AdES counter-signature
 *
 */
public class CBAdESCounterSignatureParameters extends CBAdESSignatureParameters implements SerializableCounterSignatureParameters {

    /**
     * Signature Id to be counter-signed
     */
    private String signatureIdToCounterSign;

    /**
     * Default constructor instantiating object with null signature id to be counter-signed
     */
    public CBAdESCounterSignatureParameters() {
        // empty
    }

    @Override
    public String getSignatureIdToCounterSign() {
        return signatureIdToCounterSign;
    }

    @Override
    public void setSignatureIdToCounterSign(String signatureId) {
        this.signatureIdToCounterSign = signatureId;
    }

}
