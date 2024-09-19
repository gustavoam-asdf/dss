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

    /** Defines the encoding of the structure */
    private boolean tagged;

    /** Protected attributes of the body structure */
    private COSEProtectedHeader protectedHeader;

    /** Unprotected attributes of the body structure */
    private COSEUnprotectedHeader unprotectedHeader;

    /** Payload to be signed, when present */
    private CBORObject payload;

    /** List of signers */
    private List<COSESignature> signatures;

    /**
     * Instantiates an empty COSE_Sign structure object
     */
    public COSESign() {
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

    /**
     * Gets a protected attributes header of the body structure.
     * Instantiates an empty map when omitted.
     *
     * @return {@link COSEProtectedHeader}
     */
    public COSEProtectedHeader getProtectedHeader() {
        if (protectedHeader == null) {
            protectedHeader = new COSEProtectedHeader();
        }
        return protectedHeader;
    }

    /**
     * Sets a protected attributes header of the body structure.
     *
     * @param protectedHeader {@link COSEProtectedHeader}
     */
    public void setProtectedHeader(COSEProtectedHeader protectedHeader) {
        this.protectedHeader = protectedHeader;
    }

    /**
     * Gets an unprotected attributes header of the body structure.
     * Instantiates an empty map when omitted.
     *
     * @return {@link COSEUnprotectedHeader}
     */
    public COSEUnprotectedHeader getUnprotectedHeader() {
        if (unprotectedHeader == null) {
            unprotectedHeader = new COSEUnprotectedHeader();
        }
        return unprotectedHeader;
    }

    /**
     * Sets an unprotected attributes header of the body structure.
     *
     * @param unprotectedHeader {@link COSEUnprotectedHeader}
     */
    public void setUnprotectedHeader(COSEUnprotectedHeader unprotectedHeader) {
        this.unprotectedHeader = unprotectedHeader;
    }

    @Override
    public CBORObject getPayload() {
        if (payload == null) {
            payload = new CBORNull();
        }
        return payload;
    }

    /**
     * Sets a content to be signed.
     *
     * @param payload {@link CBORObject}
     */
    public void setPayload(CBORObject payload) {
        this.payload = payload;
    }

    /**
     * Gets a list of signers.
     * Instantiates an empty list, when value is absent.
     *
     * @return a list of {@link COSESignature}s
     */
    public List<COSESignature> getSignatures() {
        if (signatures == null) {
            signatures = new ArrayList<>();
        }
        return signatures;
    }

    /**
     * Sets a list of signers.
     *
     * @param signatures a list of {@link COSESignature}s
     */
    public void setSignatures(List<COSESignature> signatures) {
        this.signatures = new ArrayList<>(signatures);
    }

    @Override
    public byte[] serialize() {
        final CBORArray codeSign = new CBORArray(4);
        if (tagged) {
            codeSign.setTag(getContext().getTag());
        }
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
