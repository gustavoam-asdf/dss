package eu.europa.esig.dss.cbades;

import eu.europa.esig.dss.cbades.cbor.CBORByteString;

/**
 * Represents a COSE_Signature object defined in RFC 9052 "4.1. Signing with One or More Signers"
 *
 */
public class COSESignature {

    /** The protected attributes of the signer structure */
    private COSEProtectedHeader protectedHeader;

    /** The unprotected attributes of the signer structure */
    private COSEUnprotectedHeader unprotectedHeader;

    /** The computed signature value of the signer */
    private CBORByteString signature;

    /**
     * Default constructor to instantiate an empty COSESignature object
     */
    public COSESignature() {
        // empty
    }

    /**
     * Gets a protected attributes header of the signer.
     * Instantiates an empty map, when absent
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
     * Sets a protected attributes header of the signer
     *
     * @param protectedHeader {@link COSEProtectedHeader}
     */
    public void setProtectedHeader(COSEProtectedHeader protectedHeader) {
        this.protectedHeader = protectedHeader;
    }

    /**
     * Gets an unprotected attributes header of the signer.
     * Instantiates an empty map, when absent
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
     * Sets an unprotected attributes header of the signer
     *
     * @param unprotectedHeader {@link COSEUnprotectedHeader}
     */
    public void setUnprotectedHeader(COSEUnprotectedHeader unprotectedHeader) {
        this.unprotectedHeader = unprotectedHeader;
    }

    /**
     * Sets a signature value of the signer
     *
     * @return {@link CBORByteString}
     */
    public CBORByteString getSignature() {
        return signature;
    }

    /**
     * Sets the signature value of the signer
     *
     * @param signature {@link CBORByteString}
     */
    public void setSignature(CBORByteString signature) {
        this.signature = signature;
    }

}
