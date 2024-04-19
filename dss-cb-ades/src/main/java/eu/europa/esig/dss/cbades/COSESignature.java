package eu.europa.esig.dss.cbades;

import co.nstant.in.cbor.model.ByteString;

public class COSESignature implements COSESignStructure {

    private COSEProtectedHeader protectedHeader;

    private COSEUnprotectedHeader unprotectedHeader;

    private ByteString signature;

    public COSESignature() {
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

    public ByteString getSignature() {
        return signature;
    }

    public void setSignature(ByteString signature) {
        this.signature = signature;
    }
}
