package eu.europa.esig.dss.cbades;

import eu.europa.esig.dss.cbades.cbor.CBORArray;
import eu.europa.esig.dss.cbades.cbor.CBORUtils;
import eu.europa.esig.dss.enumerations.COSESignatureType;
import eu.europa.esig.dss.utils.Utils;

import java.util.List;
import java.util.Objects;

/**
 * Represents a [+ COSE_Countersignature] CBOR array structure
 */
public class COSECounterSignatureArray implements COSECounterSignStructure {

    /** The context of the counter signature */
    private COSESignatureType context;

    /** Defines the encoding of the structure */
    private boolean tagged;

    /** The master signature structure */
    private COSEStructure masterSignature;

    /** Collection of embedded COSECounterSignature's */
    private List<COSECounterSignature> coseCounterSignatureList;

    @Override
    public COSESignatureType getContext() {
        return context;
    }

    /**
     * Default constructor
     */
    public COSECounterSignatureArray() {
        // empty
    }

    /**
     * Sets the context of the COSE counter signature
     *
     * @param context {@link COSESignatureType}
     */
    public void setContext(COSESignatureType context) {
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

    /**
     * Gets a list of COSECounterSignature's embedded within the Array
     *
     * @return a list of {@link COSECounterSignature}s
     */
    public List<COSECounterSignature> getCoseCounterSignatureList() {
        return coseCounterSignatureList;
    }

    /**
     * Sets a list of {@link COSECounterSignature}s
     *
     * @param coseCounterSignatureList a list of {@link COSECounterSignature}s
     */
    public void setCoseCounterSignatureList(List<COSECounterSignature> coseCounterSignatureList) {
        this.coseCounterSignatureList = coseCounterSignatureList;
    }

    @Override
    public byte[] serialize() {
        CBORArray cborArray = toCBORObject();
        return CBORUtils.serializeCborObject(cborArray);
    }

    @Override
    public CBORArray toCBORObject() {
        if (Utils.isCollectionEmpty(coseCounterSignatureList)) {
            throw new IllegalStateException("Collection of COSECounterSignature's cannot be null or empty " +
                    "in COSECounterSignature CBOR Array!");
        }
        final CBORArray coseCounterSignatureArray = new CBORArray(coseCounterSignatureList.size());
        if (tagged) {
            Objects.requireNonNull(getContext(), "Context shall be defined for a tagged CBOR object!");
            coseCounterSignatureArray.setTag(getContext().getTag());
        }
        for (COSECounterSignature counterSignature : coseCounterSignatureList) {
            coseCounterSignatureArray.add(counterSignature.toCBORObject());
        }
        return coseCounterSignatureArray;
    }

}
