package eu.europa.esig.dss.cbades;

import eu.europa.esig.dss.cbades.cbor.CBORArray;
import eu.europa.esig.dss.cbades.cbor.CBORUtils;
import eu.europa.esig.dss.utils.Utils;

import java.util.List;
import java.util.Objects;

/**
 * Represents a [+ COSE_Countersignature] CBOR array structure
 */
public class COSECounterSignatureArray implements COSECounterSignStructure {

    /** The context of the counter signature */
    private COSESignatureContext context;

    /** Defines the encoding of the structure */
    private boolean tagged;

    /** Collection of embedded COSECounterSignature's */
    private List<COSECounterSignature> coseCounterSignatureList;

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
        if (Utils.isCollectionEmpty(coseCounterSignatureList)) {
            throw new IllegalStateException("Collection of COSECounterSignature's cannot be null or empty " +
                    "in COSECounterSignature CBOR Array!");
        }
        final CBORArray coseCounterSignatureArray = new CBORArray(coseCounterSignatureList.size());
        if (tagged) {
            Objects.requireNonNull(getContext(), "Context shall be defined for a tagged CBOR object!");
            coseCounterSignatureArray.setTag(getContext().getTag());
        }
        coseCounterSignatureList.forEach(coseCounterSignatureArray::add);
        return CBORUtils.serializeCborObject(coseCounterSignatureArray);
    }

}
