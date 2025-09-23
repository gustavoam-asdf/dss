package eu.europa.esig.dss.cbades.cbor;

import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.ByteString;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.NegativeInteger;
import co.nstant.in.cbor.model.SimpleValue;
import co.nstant.in.cbor.model.SimpleValueType;
import co.nstant.in.cbor.model.Tag;
import co.nstant.in.cbor.model.UnicodeString;
import co.nstant.in.cbor.model.UnsignedInteger;
import eu.europa.esig.dss.cbades.validation.CBAdESUHeaders;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.spi.DSSASN1Utils;
import eu.europa.esig.dss.utils.Utils;
import org.bouncycastle.asn1.x509.IssuerSerial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static eu.europa.esig.dss.cbades.COSEConstants.ADO_TST;
import static eu.europa.esig.dss.cbades.COSEConstants.ALG;
import static eu.europa.esig.dss.cbades.COSEConstants.CONTENT_TYPE;
import static eu.europa.esig.dss.cbades.COSEConstants.COUNTER_SIGNATURE;
import static eu.europa.esig.dss.cbades.COSEConstants.CRIT;
import static eu.europa.esig.dss.cbades.COSEConstants.CWT_CLAIMS;
import static eu.europa.esig.dss.cbades.COSEConstants.IV;
import static eu.europa.esig.dss.cbades.COSEConstants.KID;
import static eu.europa.esig.dss.cbades.COSEConstants.PARTIAL_IV;
import static eu.europa.esig.dss.cbades.COSEConstants.SIG_D;
import static eu.europa.esig.dss.cbades.COSEConstants.SIG_PID;
import static eu.europa.esig.dss.cbades.COSEConstants.SIG_PL;
import static eu.europa.esig.dss.cbades.COSEConstants.SR_ATS;
import static eu.europa.esig.dss.cbades.COSEConstants.SR_CMS;
import static eu.europa.esig.dss.cbades.COSEConstants.X5BAG;
import static eu.europa.esig.dss.cbades.COSEConstants.X5CHAIN;
import static eu.europa.esig.dss.cbades.COSEConstants.X5T;
import static eu.europa.esig.dss.cbades.COSEConstants.X5TS;
import static eu.europa.esig.dss.cbades.COSEConstants.X5U;

/**
 * Contains common util methods for working with CBOR content
 *
 */
public final class CBORUtils {

    private static final Logger LOG = LoggerFactory.getLogger(CBORUtils.class);

    /** An empty btsr value */
    public static final CBORByteString EMPTY_BYTE_STRING;

    /** The binary content encoding (RFC 2045) */
    public static final String CONTENT_ENCODING_BINARY = "binary";

    /**
     * Contains protected header names that are supported and can be present in the critical ('crit') attribute
     */
    private static final Set<Long> supportedCriticalHeaders;

    /**
     * Contains protected header names that are required to be present in the critical ('crit') attribute, when used
     */
    // TODO : TS 119 152-1 does not mandate presence of 'crit' header, thus it is not yet enforced
    private static final Set<Long> requiredCriticalHeaders;

    static {
        EMPTY_BYTE_STRING = new CBORByteString();

        supportedCriticalHeaders = Stream.of(
                /* RFC 9052 */
                ALG, CRIT, CONTENT_TYPE, KID, IV, PARTIAL_IV,
                /* RFC 9360 */
                X5BAG, X5CHAIN, X5T, X5U,
                /* RFC 8152 */
                COUNTER_SIGNATURE,
                /* RFC 9597 */
                CWT_CLAIMS,
                /* CB-AdES TS 119 152-1 headers */
                X5TS, SR_CMS, SIG_PL, SR_ATS, ADO_TST, SIG_PID, SIG_D
        ).collect(Collectors.toSet());

        requiredCriticalHeaders = Stream.of(
                /* CB-AdES TS 119 152-1 headers */
                SIG_D
        ).collect(Collectors.toSet());
    }

    /**
     * Utils class
     */
    private CBORUtils() {
        // empty
    }

    /**
     * This method creates a Tag object from the given Long value
     *
     * @param longValue {@link Long} value to convert
     * @return {@link Tag}
     */
    public static Tag toTag(Long longValue) {
        if (longValue == null) {
            return null;
        }
        return new Tag(longValue);
    }

    /**
     * Instantiates a new CBOR object from the given {@code DataItem}
     *
     * @param dataItem {@link DataItem}
     * @return {@link CBORObject}
     */
    public static CBORObject toCBORObject(DataItem dataItem) {
        if (dataItem == null) {
            return null;
        }
        switch (dataItem.getMajorType()) {
            case MAP:
                return new CBORMap((co.nstant.in.cbor.model.Map) dataItem);
            case ARRAY:
                return new CBORArray((co.nstant.in.cbor.model.Array) dataItem);
            case BYTE_STRING:
                return new CBORByteString((co.nstant.in.cbor.model.ByteString) dataItem);
            case TAG:
                return new CBORTag((co.nstant.in.cbor.model.Tag) dataItem);
            case SPECIAL:
                if (dataItem instanceof SimpleValue) {
                    SimpleValue simpleValue = (SimpleValue) dataItem;
                    if (SimpleValueType.NULL == simpleValue.getSimpleValueType()) {
                        return new CBORNull(simpleValue);
                    }
                }
            default:
                return new CBORSimpleObject(dataItem);
        }
    }

    /**
     * This method coverts the given object to a DataItem instance, corresponding to the object's format
     *
     * @param object to be converted
     */
    public static DataItem toDataItem(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof DataItem) {
            return (DataItem) object;

        } else if (object instanceof CBORObject) {
            CBORObject cborObject = (CBORObject) object;
            return cborObject.toDataItem();

        } else if (object instanceof Long) {
            long longNumber = (Long) object;
            return longNumber > 0 ? new UnsignedInteger(longNumber) : new NegativeInteger(longNumber);

        } else if (object instanceof String) {
            String str = (String) object;
            return new UnicodeString(str);

        } else if (object instanceof byte[]) {
            byte[] byteArray = (byte[]) object;
            return new ByteString(byteArray);

        } else if (object instanceof Boolean) {
            boolean value = (boolean) object;
            SimpleValueType simpleValueType = value ? SimpleValueType.TRUE : SimpleValueType.FALSE;
            return new SimpleValue(simpleValueType);

        } else {
            throw new UnsupportedOperationException(
                    String.format("The object of class '%s' is not yet supported!", object.getClass().getName()));
        }
    }

    /**
     * This method parses a {@code DSSDocument} and returns a {@code CBORObject},
     * representing the CBOR object structure
     *
     * @param document {@link DSSDocument} to parse
     * @return {@link CBORObject}
     * @throws CborException if an error occurs on document parsing
     */
    public static CBORObject parseCbor(DSSDocument document) throws CborException {
        try (InputStream is = document.openStream()) {
            CborDecoder cborDecoder = new CborDecoder(is);
            List<DataItem> dataItems = cborDecoder.decode();
            return toCBORObject(dataItems);
        } catch (IOException e) {
            throw new DSSException(String.format("Unable to read document with name '%s' : %s", document.getName(), e.getMessage()), e);
        }
    }

    /**
     * This method parses a byte array and returns a list of {@code DataItem}s,
     * representing the CBOR object structure
     *
     * @param bytes byte array to parse
     * @return a list of {@link DataItem}s
     */
    public static CBORObject parseCbor(byte[] bytes) {
        try {
            List<DataItem> dataItems = CborDecoder.decode(bytes);
            return toCBORObject(dataItems);
        } catch (CborException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Error on parsing binaries: {}", Utils.toBase64(bytes));
            }
            throw new DSSException(String.format("Unable to parse binaries : %s. More detail in debug mode.", e.getMessage()), e);
        }
    }

    private static CBORObject toCBORObject(List<DataItem> dataItems) {
        if (Utils.collectionSize(dataItems) == 1) {
            return toCBORObject(dataItems.iterator().next());
        } else {
            return new CBORArray(dataItems);
        }
    }

    /**
     * Checks if the given document is of CBOR format
     *
     * @param document {@link DSSDocument} to check
     * @return TRUE if the document is CBOR, FALSE otherwise
     */
    public static boolean isCbor(DSSDocument document) {
        try (InputStream is = document.openStream()) {
            return new CborDecoder(is).decode() != null;
        } catch (CborException e) {
            return false;
        } catch (IOException e) {
            throw new DSSException(String.format("Unable to read document with name '%s' : %s", document.getName(), e.getMessage()), e);
        }
    }

    /**
     * Serialized a given CBOR object to a byte array
     *
     * @param cborObject {@link CBORObject} to be serialized
     * @return serialized byte array
     */
    public static byte[] serializeCborObject(CBORObject cborObject) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            CborEncoder cborEncoder = new CborEncoder(baos);
            cborEncoder.encode(cborObject.toDataItem());
            return baos.toByteArray();
        } catch (CborException | IOException e) {
            throw new DSSException(String.format("Unable to serialize CBOR object : %s", e.getMessage()), e);
        }
    }

    /**
     * Returns a CBOR Byte String wrapped incorporation of the {@code cborObject}
     *
     * @param cborObject {@link CBORObject} to serialized and incroporate into CBOR Byte String type
     * @return {@link CBORByteString}
     */
    public static CBORByteString toCborBtsrWrapped(CBORObject cborObject) {
        return new CBORByteString(serializeCborObject(cborObject));
    }

    /**
     * Gets {@code DigestAlgorithm} safely for a given {@code digestAlgoId}
     *
     * @param digestAlgoId {@link Long} IANA COSE Digest Algorithm identifier
     * @return {@link DigestAlgorithm} if supported, NULL otherwise
     */
    public static DigestAlgorithm getDigestAlgorithmForCoseId(Long digestAlgoId) {
        if (digestAlgoId == null) {
            return null;
        }
        try {
            return DigestAlgorithm.forCOSE(digestAlgoId);
        } catch (IllegalArgumentException e) {
            LOG.warn("Unknown Digest Algorithm with Id '{}'.", digestAlgoId);
            return null;
        }
    }

    /**
     * Parses the 'kid' header value as in IETF RFC 9035
     *
     * @param value {@link String} IssuerSerial to parse
     * @return {@link IssuerSerial}
     */
    public static IssuerSerial getIssuerSerial(byte[] value) {
        if (value != null) {
            return DSSASN1Utils.getIssuerSerial(value);
        }
        return null;
    }

    /**
     * Returns a set of supported protected critical headers
     *
     * @return a set of supported protected critical header identifiers
     */
    public static Set<Long> getSupportedProtectedCriticalHeaders() {
        return supportedCriticalHeaders;
    }

    // TODO : ETSI TS 119 152-1 does not (yet) define a use of 'crit' dictionary
    /**
     * Checks if the given {@code headerId} is required to be incorporated within 'crit' header, when used
     *
     * @param headerId {@link String} header name to check
     * @return TRUE if the header is required within 'crit' header when used, FALSE otherwise
     */
    public static boolean isRequiredCriticalHeader(Long headerId) {
        return requiredCriticalHeaders.contains(headerId);
    }

    /**
     * Checks if all components have one type (CBOR btsr or clear objects)
     *
     * @param uHeaders {@link CBAdESUHeaders} of objects to check
     * @return TRUE if all components are uniform (CBOR btsr or clear objects), FALSE
     *         otherwise
     */
    public static boolean checkComponentsUnicity(CBAdESUHeaders uHeaders) {
        return checkComponentsUnicity(uHeaders.getCBORArray());
    }

    /**
     * Checks if all components have one type (CBOR btsr or clear objects)
     *
     * @param uHeaders {@link CBORArray} of objects to check
     * @return TRUE if all components are uniform (CBOR btsr or clear objects), FALSE
     *         otherwise
     */
    public static boolean checkComponentsUnicity(CBORArray uHeaders) {
        if (uHeaders != null && !uHeaders.isEmpty()) {
            Iterator<CBORObject> iterator = uHeaders.getItems().iterator();
            CBORObject uHeader = iterator.next();
            boolean stringFormat = isCborByteStringWrappedFormat(uHeader);
            while (iterator.hasNext()) {
                uHeader = iterator.next();
                if (stringFormat != isCborByteStringWrappedFormat(uHeader)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks of the object is an instance of a CBOR btsr type
     *
     * @param uHeader {@link CBORObject} to check
     * @return TRUE if the object is an instance of CBOR btsr type, FALSE otherwise
     */
    public static boolean isCborByteStringWrappedFormat(CBORObject uHeader) {
        return uHeader.isByteString();
    }

    /**
     * Checks if the all components are CBOR byte string encoded
     *
     * @param uHeaders {@link CBORArray} to check
     * @return TRUE if all the components are CBOR byte string encoded, FALSE otherwise
     */
    public static boolean areAllCborBtsrComponents(CBORArray uHeaders) {
        if (uHeaders != null && !uHeaders.isEmpty()) {
            for (CBORObject component : uHeaders.getItems()) {
                if (!isCborByteStringWrappedFormat(component)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * This method parses a {@code CBORObject} representing a component of 'uHeaders' unsigned header parameter,
     * returning a value of the entry in the unified form {@code CBORMap}
     *
     * @param uHeadersEntry {@link CBORObject} to parse
     * @return {@link CBORMap} if parsing is successful, NULL otherwise
     */
    public static CBORMap parseUHeadersEntry(CBORObject uHeadersEntry) {
        if (uHeadersEntry.isByteString()) {
            LOG.trace("CBOR Byte String encoded 'uHeader' component found. Parse to CBORMap.");
            byte[] componentSerialized = ((CBORByteString) uHeadersEntry).getBytes();
            try {
                uHeadersEntry = CBORUtils.parseCbor(componentSerialized);
            } catch (Exception e) {
                if (LOG.isDebugEnabled()) {
                    LOG.warn("An error occurred on parsing 'uHeaders' component with value (b64-encoded) '{}' : {}", Utils.toBase64(componentSerialized), e.getMessage(), e);
                } else {
                    LOG.warn("An error occurred on parsing 'uHeaders' component : {}", e.getMessage());
                }
            }
        }
        if (!uHeadersEntry.isMap()) {
            LOG.warn("Component of 'uHeaders' unsigned header parameter shall be of CBORMap type! Entry is skipped.");
            return null;
        }

        CBORMap uHeadersEntryMap = (CBORMap) uHeadersEntry;
        if (uHeadersEntryMap.getSize() != 1) {
            LOG.warn("Only one entry is allowed within an 'uHeaders' component! Entry is skipped.");
            return null;
        }
        return uHeadersEntryMap;
    }

}
