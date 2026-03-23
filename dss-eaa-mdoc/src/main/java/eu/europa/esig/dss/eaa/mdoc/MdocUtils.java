package eu.europa.esig.dss.eaa.mdoc;

import eu.europa.esig.dss.cbades.cbor.CBORObject;
import eu.europa.esig.dss.eaa.mdoc.claim.MdocClaimArray;
import eu.europa.esig.dss.eaa.mdoc.claim.MdocClaimMap;
import eu.europa.esig.dss.model.eaa.claim.Claim;
import eu.europa.esig.dss.model.eaa.claim.ClaimArray;
import eu.europa.esig.dss.model.eaa.claim.ClaimBoolean;
import eu.europa.esig.dss.model.eaa.claim.ClaimByteString;
import eu.europa.esig.dss.model.eaa.claim.ClaimDate;
import eu.europa.esig.dss.model.eaa.claim.ClaimMap;
import eu.europa.esig.dss.model.eaa.claim.ClaimNull;
import eu.europa.esig.dss.model.eaa.claim.ClaimNumber;
import eu.europa.esig.dss.model.eaa.claim.ClaimString;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Contains common utility methods for processing mdoc documents
 *
 */
public final class MdocUtils {

    /**
     * Default constructor
     */
    private MdocUtils() {
        // singleton
    }

    /**
     * This method parses the {@code value} and wraps it into a {@code ClaimValue} according to its format.
     * This method can be used for non selectively disclosable claims, provided directly within EAA Payload.
     *
     * @param value {@link Object} containing the value of the object
     * @return {@link Claim}
     */
    public static Claim createClaim(Object value) {
        return createClaim(null, null, value);
    }

    /**
     * This method parses the {@code value} and wraps it into a {@code ClaimValue} according to its format.
     * This method allows providing of the claim parent, to be used within the claim's metadata.
     * When a value is of Claim type, the existing selectively discussable tag value is used,
     * otherwise it is set to false.
     *
     * @param claimName {@link String} representing the header name of the claim
     * @param parent {@link Claim} parent of the claim
     * @param value {@link Object} containing the value of the object
     * @return {@link Claim}
     */
    public static Claim createClaim(String claimName, Claim parent, Object value) {
        boolean selectivelyDisclosable = false;
        if (value instanceof Claim) {
            selectivelyDisclosable = ((Claim) value).isSelectivelyDisclosable();
        }
        return createClaim(claimName, parent, value, selectivelyDisclosable);
    }

    /**
     * This method parses the {@code value} and wraps it into a {@code ClaimValue} according to its format.
     * This method can be used for definition of claims used within provided disclosures.
     * This method allows providing of the claim parent, to be used within the claim's metadata.
     *
     * @param claimName {@link String} representing the header name of the claim
     * @param parent {@link Claim} parent of the claim
     * @param value {@link Object} containing the value of the object
     * @param selectivelyDisclosable whether the claim is selectively disclosable
     *                               (can be TRUE only when the value of claim is provided in a form of disclosure)
     * @return {@link Claim}
     */
    public static Claim createClaim(String claimName, Claim parent, Object value, boolean selectivelyDisclosable) {
        String namespace = null;
        if (value instanceof Claim) {
            namespace = ((Claim) value).getNamespace();
        }
        return createClaim(claimName, parent, value, selectivelyDisclosable, namespace);
    }

    /**
     * This method parses the {@code value} and wraps it into a {@code ClaimValue} according to its format.
     * This method can be used for definition of claims used within provided disclosures.
     * This method allows providing of the claim parent, to be used within the claim's metadata.
     * This method allows providing of the claim's namespace.
     *
     * @param claimName {@link String} representing the header name of the claim
     * @param parent {@link Claim} parent of the claim
     * @param value {@link Object} containing the value of the object
     * @param selectivelyDisclosable whether the claim is selectively disclosable
     *                               (can be TRUE only when the value of claim is provided in a form of disclosure)
     * @param namespace {@link String} representing the original namespace
     * @return {@link Claim}
     */
    public static Claim createClaim(String claimName, Claim parent, Object value, boolean selectivelyDisclosable, String namespace) {
        if (value instanceof ClaimString) {
            return new ClaimString(claimName, namespace, ((ClaimString) value).getStringValue(), selectivelyDisclosable, parent);

        } else if (value instanceof ClaimNumber) {
            return new ClaimNumber(claimName, namespace, ((ClaimNumber) value).getNumberValue(), selectivelyDisclosable, parent);

        } else if (value instanceof ClaimBoolean) {
            return new ClaimBoolean(claimName, namespace, ((ClaimBoolean) value).getBooleanValue(), selectivelyDisclosable, parent);

        } else if (value instanceof ClaimByteString) {
            return new ClaimByteString(claimName, namespace, ((ClaimByteString) value).getBinaryValue(), selectivelyDisclosable, parent);

        } else if (value instanceof ClaimDate) {
            return new ClaimDate(claimName, namespace, ((ClaimDate) value).getDateValue(), selectivelyDisclosable, parent);

        } else if (value instanceof ClaimMap) {
            return new MdocClaimMap(claimName, namespace, ((ClaimMap) value).getMapValue(), selectivelyDisclosable, parent);

        } else if (value instanceof ClaimArray) {
            return new MdocClaimArray(claimName, namespace, ((ClaimArray) value).getListValue(), selectivelyDisclosable, parent);

        } else if (value instanceof CBORObject) {
            CBORObject cborObject = (CBORObject) value;
            if (cborObject.isArray()) {
                return new MdocClaimArray(claimName, namespace, cborObject.getValueAsList(), selectivelyDisclosable, parent);
            } else if (cborObject.isBoolean()) {
                return new ClaimBoolean(claimName, namespace, cborObject.getValueAsBoolean(), selectivelyDisclosable, parent);
            } else if (cborObject.isByteString()) {
                return new ClaimByteString(claimName, namespace, cborObject.getValueAsBytes(), selectivelyDisclosable, parent);
            } else if (cborObject.isFloatingPointNumber()) {
                return new ClaimNumber(claimName, namespace, cborObject.getValueAsDouble(), selectivelyDisclosable, parent);
            } else if (cborObject.isNegativeInteger() || cborObject.isUnsignedInteger()) {
                return new ClaimNumber(claimName, namespace, cborObject.getValueAsLong(), selectivelyDisclosable, parent);
            } else if (cborObject.isMap()) {
                return new MdocClaimMap(claimName, namespace, cborObject.getValueAsMap(), selectivelyDisclosable, parent);
            } else if (cborObject.isNull()) {
                return new ClaimNull(claimName, selectivelyDisclosable, parent);
            } else if (cborObject.isUnicodeString()) {
                return new ClaimString(claimName, namespace, cborObject.getValueAsString(), selectivelyDisclosable, parent);
            }
            throw new IllegalArgumentException(String.format("The claim value of type '%s' is not supported!", value.getClass().getSimpleName()));

        } else if (value instanceof String) {
            return new ClaimString(claimName, namespace, (String) value, selectivelyDisclosable, parent);

        } else if (value instanceof Number) {
            return new ClaimNumber(claimName, namespace, (Number) value, selectivelyDisclosable, parent);

        } else if (value instanceof Boolean) {
            return new ClaimBoolean(claimName, namespace, (Boolean) value, selectivelyDisclosable, parent);

        } else if (value instanceof Date) {
            return new ClaimDate(claimName, namespace, (Date) value, selectivelyDisclosable, parent);

        } else if (value instanceof Map) {
            return new MdocClaimMap(claimName, namespace, (Map<?,?>) value, selectivelyDisclosable, parent);

        } else if (value instanceof List) {
            return new MdocClaimArray(claimName, namespace, (List<?>) value, selectivelyDisclosable, parent);

        } else if (value == null) {
            return new ClaimNull(claimName, namespace, selectivelyDisclosable, parent);

        } else {
            throw new IllegalArgumentException(String.format("The claim value of type '%s' is not supported!", value.getClass().getSimpleName()));
        }
    }

}
