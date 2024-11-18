package eu.europa.esig.dss.cbades.signature;

import eu.europa.esig.dss.cbades.COSEConstants;
import eu.europa.esig.dss.cbades.COSEParser;
import eu.europa.esig.dss.cbades.COSESignStructure;
import eu.europa.esig.dss.cbades.cbor.CBORMap;
import eu.europa.esig.dss.cbades.cbor.CBORObject;
import eu.europa.esig.dss.cbades.cbor.CBORUtils;
import eu.europa.esig.dss.cbades.validation.CBAdESUHeaders;
import eu.europa.esig.dss.cbades.validation.CBAdESUHeadersComponent;
import eu.europa.esig.dss.cbades.validation.CBORSignature;
import eu.europa.esig.dss.enumerations.MimeTypeEnum;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.model.InMemoryDocument;

import java.util.Arrays;
import java.util.List;

/**
 * This class contains utility method for a CB-AdES document conversion
 *
 */
public class CBAdESConverter {

    /** The name for a signature containing CBOR components in clear CBOR form */
    private static final String CLEAR_U_HEADERS_DOCUMENT_NAME = "uHeaders-clear-incorporation.json";

    /** The name for a signature containing CBOR components in their corresponding byte string encoded form */
    private static final String BTST_ENCODED_U_HEADERS_DOCUMENT_NAME = "uHeaders-btsr-incorporation.json";

    /** List of timestamp headers covering other 'uHeaders' headers */
    private static List<Long> timestampHeaderNames;

    static {
        timestampHeaderNames = Arrays.asList(COSEConstants.ARC_TST, COSEConstants.RFS_TST, COSEConstants.SIG_R_TST);
    }

    /**
     * Utility class
     */
    private CBAdESConverter() {
        // empty
    }

    /**
     * Converts unprotected content of 'uHeaders' header of CB-AdES signatures inside a
     * document to its clear CBOR incorporation form
     *
     * @param document {@link DSSDocument} containing CB-AdES signatures
     * @return {@link DSSDocument} containing signatures with 'uHeaders' header in its clear CBOR representation
     */
    public static DSSDocument fromUHeadersWithBtsrToClearIncorporation(DSSDocument document) {
        COSEParser coseParser = COSEParser.fromDocument(document);
        COSESignStructure coseSignStructure = coseParser.parse();

        List<CBORSignature> cborSignatures = CBORSignature.fromCOSESignStructure(coseSignStructure);
        for (CBORSignature cose : cborSignatures) {

            CBAdESUHeaders cbadesUHeaders = new CBAdESUHeaders(cose);
            if (!cbadesUHeaders.isExist()) {
                // do nothing
                continue;
            }

            assertConvertPossible(cbadesUHeaders);

            toClearIncorporation(cbadesUHeaders);
        }

        byte[] serializedCoseSignature = coseSignStructure.serialize();

        DSSDocument signatureDocument = new InMemoryDocument(serializedCoseSignature);
        signatureDocument.setName(CLEAR_U_HEADERS_DOCUMENT_NAME);
        signatureDocument.setMimeType(MimeTypeEnum.COSE);
        return signatureDocument;
    }

    private static void assertConvertPossible(CBAdESUHeaders cbadesUHeaders) {
        if (!CBORUtils.checkComponentsUnicity(cbadesUHeaders)) {
            throw new DSSException("Unable to convert the UHeaders content! All components shall have a common form.");
        }
    }

    private static void toClearIncorporation(CBAdESUHeaders cbadesUHeaders) {
        for (CBAdESUHeadersComponent item : cbadesUHeaders.getAttributes()) {
            CBORObject decodedValue = item.getValue();
            assertComponentSupportsConversion((CBORMap) decodedValue);

            CBAdESUHeadersComponent decodedHeader = CBAdESUHeadersComponent.build(
                    item.getHeaderId(), decodedValue, false, item.getIdentifier());

            cbadesUHeaders.replaceComponent(decodedHeader);
        }
    }

    /**
     * Converts unprotected content of 'uHeaders' header of CB-AdES signatures inside a
     * document to its Byte String CBOR incorporation form
     *
     * @param document {@link DSSDocument} containing CB-AdES signatures
     * @return {@link DSSDocument} containing signatures with 'uHeaders' header in its
     *         Byte String encoded representation
     */
    public static DSSDocument fromUHeadersWithClearToBtsrIncorporation(DSSDocument document) {
        COSEParser coseParser = COSEParser.fromDocument(document);
        COSESignStructure coseSignStructure = coseParser.parse();

        List<CBORSignature> cborSignatures = CBORSignature.fromCOSESignStructure(coseSignStructure);
        for (CBORSignature cose : cborSignatures) {

            CBAdESUHeaders cbadesUHeaders = new CBAdESUHeaders(cose);
            if (!cbadesUHeaders.isExist()) {
                // do nothing
                continue;
            }

            assertConvertPossible(cbadesUHeaders);

            toBase64UrlIncorporation(cbadesUHeaders);
        }

        byte[] serializedCoseSignature = coseSignStructure.serialize();

        DSSDocument signatureDocument = new InMemoryDocument(serializedCoseSignature);
        signatureDocument.setName(BTST_ENCODED_U_HEADERS_DOCUMENT_NAME);
        signatureDocument.setMimeType(MimeTypeEnum.COSE);
        return signatureDocument;
    }

    private static void toBase64UrlIncorporation(CBAdESUHeaders cbadesUHeaders) {
        for (CBAdESUHeadersComponent item : cbadesUHeaders.getAttributes()) {
            CBORObject decodedValue = item.getValue();
            assertComponentSupportsConversion((CBORMap) decodedValue);

            CBAdESUHeadersComponent encodedHeader = CBAdESUHeadersComponent.build(
                    item.getHeaderId(), decodedValue, true, item.getIdentifier());

            cbadesUHeaders.replaceComponent(encodedHeader);
        }
    }

    private static void assertComponentSupportsConversion(CBORMap decodedValue) {
        // only one is allowed
        Long componentId = decodedValue.getKeys().iterator().next();
        if (timestampHeaderNames.contains(componentId)) {
            throw new DSSException(String.format("Unable to convert a signature! "
                    + "'uHeaders' contains a component with Id '%s', which is sensible to a format change.", componentId));
        }
    }

}
