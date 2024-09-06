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

    /**
     * Gets whether the signature structure is encoded as tagged
     *
     * @return TRUE if the signature structure is encoded as tagged, FALSE in case of untagged
     */
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
     * Gets the content to be signed.
     * Instantiates a nil value, when absent.
     *
     * @return {@link CBORObject}
     */
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
        final CBORArray codeSign1 = new CBORArray(4);
        if (tagged) {
            codeSign1.setTag(getContext().getTag());
        }
        codeSign1.add(getProtectedHeader().getByteString());
        codeSign1.add(getUnprotectedHeader());
        codeSign1.add(getPayload());
        codeSign1.add(getSignature());
        return CBORUtils.serializeCborObject(codeSign1);
    }

    @Override
    public COSESignatureContext getContext() {
        return COSESignatureContext.COSE_SIGN1;
    }

}
