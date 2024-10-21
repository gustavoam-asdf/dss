package eu.europa.esig.dss.cbades;

import eu.europa.esig.dss.cbades.cbor.CBORByteString;
import eu.europa.esig.dss.cbades.cbor.CBORUtils;

/**
 * This class represents an RFC 9338 Abbreviated COSE_Countersignature0 structure
 *
 */
public class COSECounterSignature0 implements COSECounterSignStructure  {

    /** The context of the counter signature */
    private COSESignatureContext context;

    /** Defines the encoding of the structure */
    private boolean tagged;

    /** The computed signature value of the signer */
    private CBORByteString signature;

    /** The master signature structure */
    private COSEStructure masterSignature;

    /**
     * Instantiates an empty COSE_Countersignature0 structure object
     */
    public COSECounterSignature0() {
        // empty
    }

    @Override
    public COSESignatureContext getContext() {
        return context;
    }

    /**
     * Sets the context of the COSE counter signature
     *
     * @param context {@link COSESignatureContext}
     */
    public void setContext(COSESignatureContext context) {
        this.context = context;
    }

    @Override
    public boolean isTagged() {
        return tagged;
    }

    /**
     * Sets whether the signature structure is encoded as tagged or untagged
     *
     * @param tagged whether the signature structure is encoded as tagged or untagged
     */
    public void setTagged(boolean tagged) {
        this.tagged = tagged;
    }

    /**
     * Sets a signature value of the signer
     *
     * @return {@link CBORByteString}
     */
    public CBORByteString getSignature() {
        return signature;
    }

    /**
     * Sets the signature value of the signer
     *
     * @param signature {@link CBORByteString}
     */
    public void setSignature(CBORByteString signature) {
        this.signature = signature;
    }

    @Override
    public COSEStructure getMasterSignature() {
        return masterSignature;
    }

    /**
     * Sets the master signature structure
     *
     * @param masterSignature {@link COSEStructure}
     */
    public void setMasterSignature(COSEStructure masterSignature) {
        this.masterSignature = masterSignature;
    }

    @Override
    public byte[] serialize() {
        CBORByteString bstrSignature = getSignature();
        return CBORUtils.serializeCborObject(bstrSignature);
    }

}
