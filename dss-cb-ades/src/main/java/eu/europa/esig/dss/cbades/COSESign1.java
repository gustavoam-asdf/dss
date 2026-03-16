package eu.europa.esig.dss.cbades;

import eu.europa.esig.dss.cbades.cbor.CBORArray;
import eu.europa.esig.dss.cbades.cbor.CBORNull;
import eu.europa.esig.dss.cbades.cbor.CBORObject;
import eu.europa.esig.dss.cbades.cbor.CBORUtils;

/**
 * This class represents a COSE_Sign1 structure (Tag '18')
 *
 */
public class COSESign1 extends COSESignature implements COSESignStructure {

    /** Defines the encoding of the structure */
    private boolean tagged;

    /** The signed content */
    private CBORObject payload;

    /**
     * Instantiates an empty COSE_Sign1 structure object
     */
    public COSESign1() {
        // empty
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
    public CBORObject getPayload() {
        if (payload == null) {
            payload = new CBORNull();
        }
        return payload;
    }

    /**
     * Sets the content to be signed
     *
     * @param payload {@link CBORObject}
     */
    public void setPayload(CBORObject payload) {
        this.payload = payload;
    }

    @Override
    public byte[] serialize() {
        final CBORArray coseSign1 = new CBORArray(4);
        if (tagged) {
            coseSign1.setTag(getContext().getTag());
        }
        coseSign1.add(getProtectedHeader().getByteString());
        coseSign1.add(getUnprotectedHeader());
        coseSign1.add(getPayload());
        coseSign1.add(getSignature());
        return CBORUtils.serializeCborObject(coseSign1);
    }

    @Override
    public COSESignatureContext getContext() {
        return COSESignatureContext.COSE_SIGN1;
    }

}
