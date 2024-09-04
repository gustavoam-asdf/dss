package eu.europa.esig.dss.cbades;

import eu.europa.esig.dss.cbades.cbor.CBORByteString;

/**
 * Represents a COSE_Signature object defined in RFC 9052 "4.1. Signing with One or More Signers"
 *
 */
public class COSESignature {

    private COSEProtectedHeader protectedHeader;

    private COSEUnprotectedHeader unprotectedHeader;

    private CBORByteString signature;

    public COSESignature() {
    }

    public COSEProtectedHeader getProtectedHeader() {
        if (protectedHeader == null) {
            protectedHeader = new COSEProtectedHeader();
        }
        return protectedHeader;
    }

    public void setProtectedHeader(COSEProtectedHeader protectedHeader) {
        this.protectedHeader = protectedHeader;
    }

    public COSEUnprotectedHeader getUnprotectedHeader() {
        if (unprotectedHeader == null) {
            unprotectedHeader = new COSEUnprotectedHeader();
        }
        return unprotectedHeader;
    }

    public void setUnprotectedHeader(COSEUnprotectedHeader unprotectedHeader) {
        this.unprotectedHeader = unprotectedHeader;
    }

    public CBORByteString getSignature() {
        if (signature == null) {
            signature = new CBORByteString();
        }
        return signature;
    }

    public void setSignature(CBORByteString signature) {
        this.signature = signature;
    }

}
