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

    private CBORObject payload;

    public COSESign1() {
    }

    public CBORObject getPayload() {
        if (payload == null) {
            payload = new CBORNull();
        }
        return payload;
    }

    public void setPayload(CBORObject payload) {
        this.payload = payload;
    }

    @Override
    public byte[] serialize() {
        CBORArray codeSign1 = new CBORArray();
        codeSign1.setTag(getContext().getTag());
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
