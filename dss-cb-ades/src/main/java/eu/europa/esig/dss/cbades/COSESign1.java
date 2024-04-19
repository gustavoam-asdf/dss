package eu.europa.esig.dss.cbades;

import co.nstant.in.cbor.model.ByteString;

/**
 * This class represents a COSE_Sign1 structure (Tag '18')
 *
 */
public class COSESign1 implements COSESignStructure {

    private COSEProtectedHeader protectedHeader;

    private COSEUnprotectedHeader unprotectedHeader;

    private ByteString payload;

    private ByteString signature;

    public COSESign1() {
    }

    public COSEProtectedHeader getProtectedHeader() {
        return protectedHeader;
    }

    public void setProtectedHeader(COSEProtectedHeader protectedHeader) {
        this.protectedHeader = protectedHeader;
    }

    public COSEUnprotectedHeader getUnprotectedHeader() {
        return unprotectedHeader;
    }

    public void setUnprotectedHeader(COSEUnprotectedHeader unprotectedHeader) {
        this.unprotectedHeader = unprotectedHeader;
    }

    public ByteString getPayload() {
        return payload;
    }

    public void setPayload(ByteString payload) {
        this.payload = payload;
    }

    public ByteString getSignature() {
        return signature;
    }

    public void setSignature(ByteString signature) {
        this.signature = signature;
    }

}
