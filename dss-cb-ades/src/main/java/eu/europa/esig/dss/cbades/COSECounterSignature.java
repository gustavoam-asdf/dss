package eu.europa.esig.dss.cbades;

import eu.europa.esig.dss.cbades.cbor.CBORArray;
import eu.europa.esig.dss.cbades.cbor.CBORUtils;

import java.util.Objects;

/**
 * This class represents an RFC 9338 Full COSE_Countersignature structure (Tag '19')
 *
 */
public class COSECounterSignature extends COSESignature implements COSECounterSignStructure {

    /** The context of the counter signature */
    private COSESignatureContext context;

    /** Defines the encoding of the structure */
    private boolean tagged;

    /** The master signature structure */
    private COSEStructure masterSignature;

    /**
     * Instantiates an empty COSE_Countersignature structure object
     */
    public COSECounterSignature() {
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
     * Gets the master signature structure
     *
     * @return {@link COSEStructure}
     */
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
        final CBORArray coseCounterSignature = new CBORArray(3);
        if (tagged) {
            Objects.requireNonNull(getContext(), "Context shall be defined for a tagged CBOR object!");
            coseCounterSignature.setTag(getContext().getTag());
        }
        coseCounterSignature.add(getProtectedHeader().getByteString());
        coseCounterSignature.add(getUnprotectedHeader());
        coseCounterSignature.add(getSignature());
        return CBORUtils.serializeCborObject(coseCounterSignature);
    }

}
