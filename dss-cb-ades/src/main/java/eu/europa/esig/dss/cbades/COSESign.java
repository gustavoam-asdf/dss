package eu.europa.esig.dss.cbades;

import co.nstant.in.cbor.model.ByteString;

import java.util.List;

public class COSESign implements COSESignStructure {

    private COSEProtectedHeader protectedHeader;

    private COSEUnprotectedHeader unprotectedHeader;

    private ByteString payload;

    private List<COSESignature> signatures;

    public COSESign() {
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

    public List<COSESignature> getSignatures() {
        return signatures;
    }

    public void setSignatures(List<COSESignature> signatures) {
        this.signatures = signatures;
    }

}
