package eu.europa.esig.dss.cbades;

import eu.europa.esig.dss.cbades.cbor.CBORArray;
import eu.europa.esig.dss.cbades.cbor.CBORNull;
import eu.europa.esig.dss.cbades.cbor.CBORObject;
import eu.europa.esig.dss.cbades.cbor.CBORUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a COSESign (RFC 9052 "4.1. Signing with One or More Signers") signature structure,
 * allowing signing with one or multiple signers
 *
 */
public class COSESign implements COSESignStructure {

    private COSEProtectedHeader protectedHeader;

    private COSEUnprotectedHeader unprotectedHeader;

    private CBORObject payload;

    private List<COSESignature> signatures;

    public COSESign() {
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

    public CBORObject getPayload() {
        if (payload == null) {
            payload = new CBORNull();
        }
        return payload;
    }

    public void setPayload(CBORObject payload) {
        this.payload = payload;
    }

    public List<COSESignature> getSignatures() {
        if (signatures == null) {
            signatures = new ArrayList<>();
        }
        return signatures;
    }

    public void setSignatures(List<COSESignature> signatures) {
        this.signatures = new ArrayList<>(signatures);
    }

    @Override
    public byte[] serialize() {
        CBORArray codeSign = new CBORArray(4);
        codeSign.setTag(getContext().getTag());
        codeSign.add(getProtectedHeader().getByteString());
        codeSign.add(getUnprotectedHeader());
        codeSign.add(getPayload());

        List<COSESignature> signaturesList = getSignatures();
        CBORArray coseSignaturesArray = new CBORArray(signaturesList.size());
        for (COSESignature coseSignature : signaturesList) {
            CBORArray coseSignatureArray = new CBORArray(3);
            coseSignatureArray.add(coseSignature.getProtectedHeader().getByteString());
            coseSignatureArray.add(coseSignature.getUnprotectedHeader());
            coseSignatureArray.add(coseSignature.getSignature());
            coseSignaturesArray.add(coseSignatureArray);
        }
        codeSign.add(coseSignaturesArray.toDataItem());
        return CBORUtils.serializeCborObject(codeSign);
    }

    @Override
    public COSESignatureContext getContext() {
        return COSESignatureContext.COSE_SIGN;
    }

}
