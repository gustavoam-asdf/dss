package eu.europa.esig.dss.cbades;

import eu.europa.esig.dss.cbades.cbor.CBORArray;
import eu.europa.esig.dss.cbades.cbor.CBORMap;
import eu.europa.esig.dss.enumerations.ObjectIdentifier;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.model.TimestampBinary;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Utility class containing methods for CB-AdES processing
 *
 */
public class CBAdESUtils {

    /**
     * Utils class
     */
    private CBAdESUtils() {
        // empty
    }

    /**
     * Creates an 'oid' object according to TS 119-152 ch. 5.4.1 The oId data type
     *
     * @param objectIdentifier {@link ObjectIdentifier} to create an 'oid' from
     * @return {@link CBORMap} 'oid' object
     */
    public static CBORMap getOidObject(ObjectIdentifier objectIdentifier) {
        return getOidObject(DSSUtils.getUriOrUrnOid(objectIdentifier), objectIdentifier.getDescription(),
                objectIdentifier.getDocumentationReferences());
    }

    /**
     * Creates an 'oid' JsonObject according to TS 119-182 ch. 5.4.1 The oId data type
     *
     * @param uri {@link String} URI defining the object. The property is REQUIRED.
     * @param desc {@link String} the object description. The property is OPTIONAL.
     * @param docRefs an array of {@link String} URIs containing any other additional information about the object.
     * 				The property is OPTIONAL.
     * @return {@link CBORMap} 'oid' object
     */
    public static CBORMap getOidObject(String uri, String desc, String[] docRefs) {
        Objects.requireNonNull(uri, "uri must be defined!");

        CBORMap oidParams = new CBORMap();
        oidParams.put(COSEConstants.OID_ID, uri);
        if (Utils.isStringNotEmpty(desc)) {
            oidParams.put(COSEConstants.OID_DESC, desc);
        }
        if (Utils.isArrayNotEmpty(docRefs)) {
            oidParams.put(COSEConstants.OID_DOC_REFS, new CBORArray(docRefs));
        }

        return oidParams;
    }

    /**
     * Creates a 'tstContainer' JsonObject according to TS 119-152 ch. 5.4.3.3 The tstContainer type
     *
     * @param timestampBinaries a list of {@link TimestampBinary}s to incorporate
     * @param canonicalizationMethodUri a canonicalization method (OPTIONAL, e.g. shall not be present for content timestamps)
     * @return {@link CBORMap} 'tstContainer' object
     */
    public static CBORMap getTstContainer(List<TimestampBinary> timestampBinaries, String canonicalizationMethodUri) {
        if (Utils.isCollectionEmpty(timestampBinaries)) {
            throw new IllegalArgumentException("Impossible to create 'tstContainer'. List of TimestampBinaries cannot be null or empty!");
        }

        CBORMap tstContainerParams = new CBORMap();

        CBORArray tstTokens = new CBORArray();
        for (TimestampBinary timestampBinary : timestampBinaries) {
            Map<Long, Object> tstToken = getTstToken(timestampBinary);
            tstTokens.add(tstToken);
        }
        tstContainerParams.put(COSEConstants.TST_CONTAINER_TST_TOKENS, tstTokens);

        if (canonicalizationMethodUri != null) {
            tstContainerParams.put(COSEConstants.TST_CONTAINER_CANON_ALG, canonicalizationMethodUri);
        }

        return tstContainerParams;
    }

    /**
     * Creates a 'tstToken' JsonObject according to TS 119-152 ch. 5.4.3.3 The tstContainer type
     *
     * @param timestampBinary {@link TimestampBinary}s to incorporate
     * @return 'tstToken' object
     */
    private static Map<Long, Object> getTstToken(TimestampBinary timestampBinary) {
        Objects.requireNonNull(timestampBinary, "timestampBinary cannot be null!");

        Map<Long, Object> tstTokenParams = new HashMap<>();
        // only RFC 3161 TimestampTokens are supported
        // 'type', 'encoding' and 'specRef' params are not need to be defined (see TS 119-182 ch. 5.4.3.3)
        tstTokenParams.put(COSEConstants.TST_TOKEN_VAL, timestampBinary.getBytes());

        return tstTokenParams;
    }

    /**
     * This method concatenates {@code DSSDocument} contents in a single byte array
     *
     * @param documents a list of {@link DSSDocument}s to concatenate
     * @return a byte array
     */
    public static byte[] concatenateDocumentContents(List<DSSDocument> documents) {
        if (Utils.isCollectionEmpty(documents)) {
            throw new IllegalArgumentException("Unable to build a COSE Payload. Reason : the detached content is not provided!");
        }
        if (documents.size() == 1) {
            return DSSUtils.toByteArray(documents.get(0));
        }
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            for (DSSDocument document : documents) {
                baos.write(DSSUtils.toByteArray(document));
            }
            return baos.toByteArray();

        } catch (IOException e) {
            throw new DSSException(String.format("Unable to build a COSE Payload. Reason : %s", e.getMessage()), e);
        }
    }

}
