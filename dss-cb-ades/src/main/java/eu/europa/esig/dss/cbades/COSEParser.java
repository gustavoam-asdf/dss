package eu.europa.esig.dss.cbades;

import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.Array;
import co.nstant.in.cbor.model.ByteString;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.MajorType;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.spi.exception.IllegalInputException;
import eu.europa.esig.dss.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to parse a COSE signature
 *dataItems = {LinkedList@1891}  size = 1
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
        try (InputStream is = document.openStream()) {
            List<DataItem> dataItems = new CborDecoder(is).decode();
            if (Utils.collectionSize(dataItems) != 1) {
                throw new IllegalInputException("One and only one CBOR item shall be present at the root level!");
            }
            DataItem dataItem = dataItems.iterator().next();
            if (MajorType.ARRAY != dataItem.getMajorType()) {
                throw new IllegalInputException("Root element of CBOR signature shall be an array!");
            }
            Array arrayItem = (Array) dataItem;
            if (arrayItem.hasTag()) {
                if (COSESignatureContext.COSE_SIGN.getTag() == arrayItem.getTag().getValue()) {
                    return parseCOSESign(arrayItem);
                } else if (COSESignatureContext.COSE_SIGN1.getTag() == arrayItem.getTag().getValue()) {
                    return parseCOSESign1(arrayItem);
                }
            }
            // support of untagged entries is required
            throw new UnsupportedOperationException("Not implemented!");

        } catch (CborException e) {
            throw new DSSException(String.format("A parsing error of CBOR content occurred : %s", e.getMessage()), e);
        } catch (IOException e) {
            throw new DSSException(String.format("Unable to read document with name '%s' : %s", document.getName(), e.getMessage()), e);
        }
    }

    private COSESign parseCOSESign(Array arrayItem) {
        List<DataItem> dataItems = arrayItem.getDataItems();
        if (Utils.collectionSize(dataItems) != 4) {
            throw new IllegalInputException("COSE_Sign array must have 4 entries!");
        }
        DataItem protectedHeaderItem = dataItems.get(0);
        if (MajorType.BYTE_STRING != protectedHeaderItem.getMajorType()) {
            throw new IllegalInputException("COSE_Sign.protected header must be a bstr!");
        }
        DataItem unprotectedHeaderItem = dataItems.get(1);
        if (MajorType.MAP != unprotectedHeaderItem.getMajorType()) {
            throw new IllegalInputException("COSE_Sign.unprotected header must be a header map!");
        }
        DataItem payloadItem = dataItems.get(2);
        if (payloadItem != null && MajorType.BYTE_STRING != payloadItem.getMajorType()) {
            throw new IllegalInputException("COSE_Sign.payload must be a bstr or nil!");
        }
        DataItem signaturesItem = dataItems.get(3);
        if (MajorType.ARRAY != signaturesItem.getMajorType()) {
            throw new IllegalInputException("COSE_Sign1.signature must be an array!");
        }
        final COSESign coseSign = new COSESign();
        coseSign.setProtectedHeader(new COSEProtectedHeader((ByteString) protectedHeaderItem));
        coseSign.setUnprotectedHeader(new COSEUnprotectedHeader((co.nstant.in.cbor.model.Map) unprotectedHeaderItem));
        coseSign.setPayload((ByteString) payloadItem);
        coseSign.setSignatures(parseSignatures((Array) signaturesItem));
        return coseSign;
    }

    private List<COSESignature> parseSignatures(Array signaturesItem) {
        List<DataItem> dataItems = signaturesItem.getDataItems();
        if (Utils.isCollectionEmpty(dataItems)) {
            throw new IllegalInputException("COSE_Sign.signatures array is empty!");
        }
        final List<COSESignature> signatures = new ArrayList<>();
        for (DataItem dataItem : dataItems) {
            signatures.add(parseSignature(dataItem));
        }
        return signatures;
    }

    private COSESignature parseSignature(DataItem coseSignatureItem) {
        if (MajorType.ARRAY != coseSignatureItem.getMajorType()) {
            throw new IllegalInputException("COSE_Sign.signatures COSE_Signature entry must be an array!");
        }
        Array arrayItem = (Array) coseSignatureItem;

        List<DataItem> dataItems = arrayItem.getDataItems();
        if (Utils.collectionSize(dataItems) != 3) {
            throw new IllegalInputException("COSE_Sign.signatures COSE_Signature array must have 3 entries!");
        }
        DataItem protectedHeaderItem = dataItems.get(0);
        if (MajorType.BYTE_STRING != protectedHeaderItem.getMajorType()) {
            throw new IllegalInputException("COSE_Signature.protected header must be a bstr!");
        }
        DataItem unprotectedHeaderItem = dataItems.get(1);
        if (MajorType.MAP != unprotectedHeaderItem.getMajorType()) {
            throw new IllegalInputException("COSE_Signature.unprotected header must be a header map!");
        }
        DataItem signatureItem = dataItems.get(2);
        if (MajorType.BYTE_STRING != signatureItem.getMajorType()) {
            throw new IllegalInputException("COSE_Signature.signature must be a bstr!");
        }
        final COSESignature coseSignature = new COSESignature();
        coseSignature.setProtectedHeader(new COSEProtectedHeader((ByteString) protectedHeaderItem));
        coseSignature.setUnprotectedHeader(new COSEUnprotectedHeader((co.nstant.in.cbor.model.Map) unprotectedHeaderItem));
        coseSignature.setSignature((ByteString) signatureItem);
        return coseSignature;
    }

    private COSESign1 parseCOSESign1(Array arrayItem) {
        List<DataItem> dataItems = arrayItem.getDataItems();
        if (Utils.collectionSize(dataItems) != 4) {
            throw new IllegalInputException("COSE_Sign1 array must have 4 entries!");
        }
        DataItem protectedHeaderItem = dataItems.get(0);
        if (MajorType.BYTE_STRING != protectedHeaderItem.getMajorType()) {
            throw new IllegalInputException("COSE_Sign1.protected header must be a bstr!");
        }
        DataItem unprotectedHeaderItem = dataItems.get(1);
        if (MajorType.MAP != unprotectedHeaderItem.getMajorType()) {
            throw new IllegalInputException("COSE_Sign1.unprotected header must be a header map!");
        }
        DataItem payloadItem = dataItems.get(2);
        if (payloadItem != null && MajorType.BYTE_STRING != payloadItem.getMajorType()) {
            throw new IllegalInputException("COSE_Sign1.payload must be a bstr or nil!");
        }
        DataItem signatureItem = dataItems.get(3);
        if (MajorType.BYTE_STRING != signatureItem.getMajorType()) {
            throw new IllegalInputException("COSE_Sign1.signature must be a bstr!");
        }
        
        final COSESign1 coseSign1 = new COSESign1();
        coseSign1.setProtectedHeader(new COSEProtectedHeader((ByteString) protectedHeaderItem));
        coseSign1.setUnprotectedHeader(new COSEUnprotectedHeader((co.nstant.in.cbor.model.Map) unprotectedHeaderItem));
        coseSign1.setPayload((ByteString) payloadItem);
        coseSign1.setSignature((ByteString) signatureItem);
        return coseSign1;
    }

}
