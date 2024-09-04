package eu.europa.esig.dss.cbades;

import co.nstant.in.cbor.CborException;
import eu.europa.esig.dss.cbades.cbor.CBORArray;
import eu.europa.esig.dss.cbades.cbor.CBORByteString;
import eu.europa.esig.dss.cbades.cbor.CBORMap;
import eu.europa.esig.dss.cbades.cbor.CBORObject;
import eu.europa.esig.dss.cbades.cbor.CBORUtils;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.spi.exception.IllegalInputException;
import eu.europa.esig.dss.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to parse a COSE signature
 *
 */
public class COSEParser {

    /** The document to be parsed */
    private final DSSDocument document;

    /**
     * The default constructor
     *
     * @param document {@link DSSDocument} to parse
     */
    public COSEParser(DSSDocument document) {
        this.document = document;
    }

    /**
     * This method parses COSE signature present within a provided {@code DSSDocument}
     *
     * @return {@link COSESignStructure}
     */
    public COSESignStructure parse() {
        try {
            CBORObject cborObject = CBORUtils.parseCbor(document);
            if (!cborObject.isArray()) {
                throw new IllegalInputException("A COSE signature shall be represented by a CBOR Array type!");
            }
            CBORArray cborArray = (CBORArray) cborObject;
            if (cborArray.isTagged()) {
                if (COSESignatureContext.COSE_SIGN.getTag() == cborArray.getTag().getValue()) {
                    return parseCOSESign(cborArray);
                } else if (COSESignatureContext.COSE_SIGN1.getTag() == cborArray.getTag().getValue()) {
                    return parseCOSESign1(cborArray);
                }
            }
            // TODO : support of untagged entries is required
            throw new UnsupportedOperationException("Not implemented!");

        } catch (CborException e) {
            throw new DSSException(String.format("A parsing error of CBOR content occurred : %s", e.getMessage()), e);
        }
    }

    private COSESign parseCOSESign(CBORArray cborArray) {
        List<CBORObject> dataItems = cborArray.getItems();
        if (Utils.collectionSize(dataItems) != 4) {
            throw new IllegalInputException("COSE_Sign array must have 4 entries!");
        }
        CBORObject protectedHeaderItem = dataItems.get(0);
        if (!protectedHeaderItem.isByteString()) {
            throw new IllegalInputException("COSE_Sign.protected header must be a bstr!");
        }
        CBORObject unprotectedHeaderItem = dataItems.get(1);
        if (!unprotectedHeaderItem.isMap()) {
            throw new IllegalInputException("COSE_Sign.unprotected header must be a header map!");
        }
        CBORObject payloadItem = dataItems.get(2);
        if (payloadItem != null && !payloadItem.isByteString()) {
            throw new IllegalInputException("COSE_Sign.payload must be a bstr or nil!");
        }
        CBORObject signaturesArray = dataItems.get(3);
        if (!signaturesArray.isArray()) {
            throw new IllegalInputException("COSE_Sign1.signature must be an array!");
        }
        final COSESign coseSign = new COSESign();
        coseSign.setProtectedHeader(new COSEProtectedHeader((CBORByteString) protectedHeaderItem));
        coseSign.setUnprotectedHeader(new COSEUnprotectedHeader((CBORMap) unprotectedHeaderItem));
        coseSign.setPayload(payloadItem);
        coseSign.setSignatures(parseSignatures((CBORArray) signaturesArray));
        return coseSign;
    }

    private List<COSESignature> parseSignatures(CBORArray signaturesArray) {
        if (signaturesArray.isEmpty()) {
            throw new IllegalInputException("COSE_Sign.signatures array is empty!");
        }
        final List<COSESignature> signatures = new ArrayList<>();
        for (CBORObject cborObject : signaturesArray.getItems()) {
            signatures.add(parseSignature(cborObject));
        }
        return signatures;
    }

    private COSESignature parseSignature(CBORObject coseSignatureItem) {
        if (!coseSignatureItem.isArray()) {
            throw new IllegalInputException("COSE_Sign.signatures COSE_Signature entry must be an array!");
        }
        CBORArray cborArray = (CBORArray) coseSignatureItem;
        List<CBORObject> arrayItems = cborArray.getItems();
        if (Utils.collectionSize(arrayItems) != 3) {
            throw new IllegalInputException("COSE_Sign.signatures COSE_Signature array must have 3 entries!");
        }
        CBORObject protectedHeaderItem = arrayItems.get(0);
        if (!protectedHeaderItem.isByteString()) {
            throw new IllegalInputException("COSE_Signature.protected header must be a bstr!");
        }
        CBORObject unprotectedHeaderItem = arrayItems.get(1);
        if (!unprotectedHeaderItem.isMap()) {
            throw new IllegalInputException("COSE_Signature.unprotected header must be a header map!");
        }
        CBORObject signatureItem = arrayItems.get(2);
        if (!signatureItem.isByteString()) {
            throw new IllegalInputException("COSE_Signature.signature must be a bstr!");
        }
        final COSESignature coseSignature = new COSESignature();
        coseSignature.setProtectedHeader(new COSEProtectedHeader((CBORByteString) protectedHeaderItem));
        coseSignature.setUnprotectedHeader(new COSEUnprotectedHeader((CBORMap) unprotectedHeaderItem));
        coseSignature.setSignature((CBORByteString) signatureItem);
        return coseSignature;
    }

    private COSESign1 parseCOSESign1(CBORArray cborArray) {
        List<CBORObject> arrayItems = cborArray.getItems();
        if (Utils.collectionSize(arrayItems) != 4) {
            throw new IllegalInputException("COSE_Sign1 array must have 4 entries!");
        }
        CBORObject protectedHeaderItem = arrayItems.get(0);
        if (!protectedHeaderItem.isByteString()) {
            throw new IllegalInputException("COSE_Sign1.protected header must be a bstr!");
        }
        CBORObject unprotectedHeaderItem = arrayItems.get(1);
        if (!unprotectedHeaderItem.isMap()) {
            throw new IllegalInputException("COSE_Sign1.unprotected header must be a header map!");
        }
        CBORObject payloadItem = arrayItems.get(2);
        if (!payloadItem.isByteString() && !payloadItem.isNull()) {
            throw new IllegalInputException("COSE_Sign1.payload must be a bstr or nil!");
        }
        CBORObject signatureItem = arrayItems.get(3);
        if (!signatureItem.isByteString()) {
            throw new IllegalInputException("COSE_Sign1.signature must be a bstr!");
        }

        final COSESign1 coseSign1 = new COSESign1();
        coseSign1.setProtectedHeader(new COSEProtectedHeader((CBORByteString) protectedHeaderItem));
        coseSign1.setUnprotectedHeader(new COSEUnprotectedHeader((CBORMap) unprotectedHeaderItem));
        coseSign1.setPayload(payloadItem);
        coseSign1.setSignature((CBORByteString) signatureItem);
        return coseSign1;
    }

}
