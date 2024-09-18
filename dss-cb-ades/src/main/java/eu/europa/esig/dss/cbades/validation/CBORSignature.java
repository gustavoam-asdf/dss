package eu.europa.esig.dss.cbades.validation;

import co.nstant.in.cbor.model.UnicodeString;
import eu.europa.esig.dss.cbades.COSEConstants;
import eu.europa.esig.dss.cbades.COSEProtectedHeader;
import eu.europa.esig.dss.cbades.COSESign;
import eu.europa.esig.dss.cbades.COSESign1;
import eu.europa.esig.dss.cbades.COSESignStructure;
import eu.europa.esig.dss.cbades.COSESignature;
import eu.europa.esig.dss.cbades.COSESignatureContext;
import eu.europa.esig.dss.cbades.COSEUnprotectedHeader;
import eu.europa.esig.dss.cbades.cbor.CBORArray;
import eu.europa.esig.dss.cbades.cbor.CBORByteString;
import eu.europa.esig.dss.cbades.cbor.CBORMap;
import eu.europa.esig.dss.cbades.cbor.CBORObject;
import eu.europa.esig.dss.cbades.cbor.CBORSimpleObject;
import eu.europa.esig.dss.cbades.cbor.CBORUtils;
import eu.europa.esig.dss.enumerations.EncryptionAlgorithm;
import eu.europa.esig.dss.enumerations.SignatureAlgorithm;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.spi.DSSASN1Utils;
import eu.europa.esig.dss.spi.DSSSecurityProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Key;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * This class represents a single COSE signature.
 * The class is used for verification of a cryptographic validity of the COSE signature.
 *
 */
public class CBORSignature {

    private static final Logger LOG = LoggerFactory.getLogger(CBORSignature.class);

    /** Context of the signature */
    private COSESignatureContext context;

    /** Defines whether the signature container is tagged */
    private boolean tagged;

    /** Protected attributes of the body structure */
    private COSEProtectedHeader bodyProtectedHeader;

    /** Protected attributes of the signer structure (absent for COSE_Sign1) */
    private COSEProtectedHeader signerProtectedHeader;

    /** Unprotected attributes of the body structure */
    private COSEUnprotectedHeader bodyUnprotectedHeader;

    /** Unprotected attributes of the signer structure (absent for COSE_Sign1) */
    private COSEUnprotectedHeader signerUnprotectedHeader;

    /** Externally supplied data used as a part of a signed data */
    private CBORByteString externallySuppliedData;

    /** The payload to be signed */
    private CBORObject payload;

    /** The signer's signature value */
    private CBORByteString signature;

    /** Original COSE signature container structure */
    private COSESignStructure coseSignStructure;

    /** The original signer signature (applicable only for COSE_Sign) */
    private COSESignature signerSignature;

    /** The signer's key */
    private Key key;

    static {
        Security.addProvider(DSSSecurityProvider.getSecurityProvider());
    }

    /**
     * Default constructor to instantiate an empty object
     */
    protected CBORSignature() {
        // empty
    }

    /**
     * This method creates a list of {@code CBORSignature}s for the given {@code COSESignStructure},
     * representing a validation object for each embedded signer.
     *
     * @param coseSignStructure {@link COSESignStructure}
     * @return a list of {@link CBORSignature}
     */
    public static List<CBORSignature> fromCOSESignStructure(COSESignStructure coseSignStructure) {
        if (coseSignStructure instanceof COSESign) {
            return fromCOSESign((COSESign) coseSignStructure);
        } else if (coseSignStructure instanceof COSESign1) {
            return Collections.singletonList(fromCOSE1Sign((COSESign1) coseSignStructure));
        }
        throw new UnsupportedOperationException(String.format("Unsupported class '%s'", coseSignStructure.getClass()));
    }

    /**
     * This method creates a list of {@code CBORSignature}s for the given {@code COSESign},
     * representing a validation object for each embedded signer.
     *
     * @param coseSign {@link COSESign}
     * @return a list of {@link CBORSignature}
     */
    public static List<CBORSignature> fromCOSESign(COSESign coseSign) {
        final List<CBORSignature> cborSignatures = new ArrayList<>();
        for (COSESignature coseSignature : coseSign.getSignatures()) {
            final CBORSignature cborSignature = new CBORSignature();
            cborSignature.context = coseSign.getContext();
            cborSignature.tagged = coseSign.isTagged();
            cborSignature.bodyProtectedHeader = coseSign.getProtectedHeader();
            cborSignature.bodyUnprotectedHeader = coseSign.getUnprotectedHeader();
            cborSignature.signerProtectedHeader = coseSignature.getProtectedHeader();
            cborSignature.signerUnprotectedHeader = coseSignature.getUnprotectedHeader();
            cborSignature.payload = coseSign.getPayload();
            cborSignature.signature = coseSignature.getSignature();
            cborSignature.coseSignStructure = coseSign;
            cborSignature.signerSignature = coseSignature;
            cborSignatures.add(cborSignature);
        }
        return cborSignatures;
    }

    /**
     * This method creates a {@code CBORSignature} for the given {@code COSESign1},
     * representing a validation object for the signer.
     *
     * @param coseSign1 {@link COSESign1}
     * @return {@link CBORSignature}
     */
    public static CBORSignature fromCOSE1Sign(COSESign1 coseSign1) {
        final CBORSignature cborSignature = new CBORSignature();
        cborSignature.context = coseSign1.getContext();
        cborSignature.tagged = coseSign1.isTagged();
        cborSignature.bodyProtectedHeader = coseSign1.getProtectedHeader();
        cborSignature.bodyUnprotectedHeader = coseSign1.getUnprotectedHeader();
        cborSignature.payload = coseSign1.getPayload();
        cborSignature.signature = coseSign1.getSignature();
        cborSignature.coseSignStructure = coseSign1;
        return cborSignature;
    }

    /**
     * Returns context of the COSE signature
     *
     * @return {@link COSESignatureContext}
     */
    public COSESignatureContext getContext() {
        return context;
    }

    /**
     * Gets whether the container of the signature is tagged
     *
     * @return TRUE if the COSE signature containe is tagged, FALSE otherwise
     */
    public boolean isTagged() {
        return tagged;
    }

    /**
     * Gets the body protected header
     *
     * @return {@link COSEProtectedHeader}
     */
    public COSEProtectedHeader getBodyProtectedHeader() {
        return bodyProtectedHeader;
    }

    /**
     * Gets the signer protected header.
     * NOTE: the field is present only within COSE_Sign signature structure.
     *
     * @return {@link COSEProtectedHeader}
     */
    public COSEProtectedHeader getSignerProtectedHeader() {
        return signerProtectedHeader;
    }

    /**
     * Gets the body unprotected header
     *
     * @return {@link COSEUnprotectedHeader}
     */
    public COSEUnprotectedHeader getBodyUnprotectedHeader() {
        return bodyUnprotectedHeader;
    }

    /**
     * Gets the signer unprotected header.
     * NOTE: the field is present only within COSE_Sign signature structure.
     *
     * @return {@link COSEUnprotectedHeader}
     */
    public COSEUnprotectedHeader getSignerUnprotectedHeader() {
        return signerUnprotectedHeader;
    }

    /**
     * Gets current CBOR representation of signature binaries
     *
     * @return {@link CBORByteString}
     */
    public CBORByteString getSignature() {
        return signature;
    }

    /**
     * Returns signature value
     *
     * @return byte array containing the signature bytes
     */
    public byte[] getSignatureValue() {
        Objects.requireNonNull(signature, "Signature bytes shall be set.");
        return signature.getBytes();
    }

    /**
     * This method sets externally supplied data binaries, when applicable
     *
     * @param externallySuppliedData byte array representing an externally supplied data
     */
    public void setExternalAttributesBytes(byte[] externallySuppliedData) {
        this.externallySuppliedData = new CBORByteString(externallySuppliedData);
    }

    /**
     * Gets the payload
     *
     * @return {@link CBORObject}
     */
    public CBORObject getPayload() {
        return payload;
    }

    /**
     * Gets the payload bytes, when present
     *
     * @return byte array representing the payload bytes
     */
    public byte[] getPayloadBytes() {
        if (payload != null && payload.isByteString()) {
            return ((CBORByteString) payload).getBytes();
        }
        return null;
    }

    /**
     * This method allows supplying of a payload
     *
     * @param payload binaries of a payload
     */
    public void setPayloadBytes(byte[] payload) {
        this.payload = new CBORByteString(payload);
    }

    /**
     * Gets the original COSE signature structure
     *
     * @return {@link COSESignStructure}
     */
    public COSESignStructure getCoseSignStructure() {
        return coseSignStructure;
    }

    /**
     * Returns the original signer signature structure (applicable only for COSE_Sign signatures)
     *
     * @return {@link COSESignature}
     */
    public COSESignature getSignerSignature() {
        return signerSignature;
    }

    /**
     * Sets the original COSE signature structure
     *
     * @param coseSignStructure {@link COSESignStructure}
     */
    public void setCoseSignStructure(COSESignStructure coseSignStructure) {
        this.coseSignStructure = coseSignStructure;
    }

    private Key getKey() {
        if (key == null) {
            throw new IllegalStateException("No key has been supplied. COSE verification is not possible!");
        }
        return key;
    }

    /**
     * This method sets the signer's key in order to verify the signature
     *
     * @param key {@link Key} public key of the signer
     */
    public void setKey(Key key) {
        this.key = key;
    }

    /**
     * This method verifies the signature, using the defined {@code key}
     *
     * @return TRUE if the signature is valid, FALSE otherwise
     */
    public boolean verifySignature() {
        // TODO : add additional verification (crit dict, etc.). See JWS validation
        try {
            // Create Signature object
            SignatureAlgorithm signatureAlgorithm = getAlgorithm();
            Signature signatureInstance = Signature.getInstance(signatureAlgorithm.getJCEId(), DSSSecurityProvider.getSecurityProviderName());

            // Initialize Signature object with the public key
            Key key = getKey();
            PublicKey publicKey = (PublicKey) key;
            signatureInstance.initVerify(publicKey);

            // Supply the signature input bytes
            byte[] signatureInputBytes = getSignatureInputBytes();
            signatureInstance.update(signatureInputBytes);

            // Verify the signature
            byte[] signatureBytes = signature.getBytes();
            signatureBytes = ensureDerEncodedSignature(signatureBytes, signatureAlgorithm);
            return signatureInstance.verify(signatureBytes);

        } catch (Exception e) {
            LOG.warn("An error occurred on signature validation: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Returns a DataToBeSigned input
     *
     * @return byte array
     */
    public byte[] getSignatureInputBytes() {
        /*
         * Sig_structure = [
         *     context : "Signature" / "Signature1" / "CounterSignature",
         *     body_protected : empty_or_serialized_map,
         *     ? sign_protected : empty_or_serialized_map,
         *     external_aad : bstr,
         *     payload : bstr
         * ]
         */
        final CBORArray array = new CBORArray();

        /*
         * 1. A text string identifying the context of the signature. The context string is:
         *   - "Signature" for signatures using the COSE_Signature structure.
         *   - "Signature1" for signatures using the COSE_Sign1 structure.
         *   - "CounterSignature" for signatures used as counter signature attributes.
         */
        array.add(new UnicodeString(context.getContext()));

        /*
         * 2. The protected attributes from the body structure, encoded in a
         * bstr type. If there are no protected attributes, a zero-length
         * byte string is used.
         */
        if (bodyProtectedHeader != null && !bodyProtectedHeader.isEmpty()) {
            array.add(bodyProtectedHeader.getByteString());
        } else {
            array.add(CBORUtils.EMPTY_BYTE_STRING);
        }
        /*
         * 3. The protected attributes from the signer structure, encoded in a
         * bstr type. If there are no protected attributes, a zero-length
         * byte string is used. This field is omitted for the COSE_Sign1
         * signature structure.
         */
        if (signerProtectedHeader != null && !signerProtectedHeader.isEmpty()) {
            array.add(signerProtectedHeader.getByteString());
        } else if (COSESignatureContext.COSE_SIGN1 != context) {
            array.add(CBORUtils.EMPTY_BYTE_STRING);
        }
        /*
         * 4. The externally supplied data from the application, encoded in a
         * bstr type. If this field is not supplied, it defaults to a zero-
         * length byte string. (See Section 4.3 for application guidance on
         * constructing this field.)
         */
        if (externallySuppliedData != null) {
            array.add(externallySuppliedData);
        } else {
            array.add(CBORUtils.EMPTY_BYTE_STRING);
        }
        /*
         * 5. The payload to be signed, encoded in a bstr type. The full
         *  payload is used here, independent of how it is transported.
         */
        if (payload != null && payload.isByteString()) {
            array.add(payload);
        } else {
            LOG.warn("No payload found for COSE signature!");
            array.add(CBORUtils.EMPTY_BYTE_STRING);
        }

        return CBORUtils.serializeCborObject(array);
    }

    private byte[] ensureDerEncodedSignature(byte[] signature, SignatureAlgorithm signatureAlgorithm) {
        EncryptionAlgorithm encryptionAlgorithm = signatureAlgorithm.getEncryptionAlgorithm();
        if (EncryptionAlgorithm.ECDSA.isEquivalent(encryptionAlgorithm)) {
            signature = DSSASN1Utils.toStandardDSASignatureValue(signature);
        }
        return signature;
    }

    /**
     * Gets the used SignatureAlgorithm, define within a protected header of the signer
     *
     * @return {@link SignatureAlgorithm}
     */
    public SignatureAlgorithm getAlgorithm() {
        Long algNumber = getAlgorithmHeaderValue();
        if (algNumber == null) {
            throw new DSSException("No 'alg' header found!");
        }
        return SignatureAlgorithm.forCOSE(algNumber, null);
    }

    /**
     * This method returns a value of the {@code 'alg'} header parameter.
     *
     * @return {@link Long}
     */
    public Long getAlgorithmHeaderValue() {
        return getProtectedHeaderValueAsLong(COSEConstants.ALG);
    }

    /**
     * This method returns a Long value extracted from protected header of the signature
     *
     * @param headerKey identifier of the header
     * @return {@link Long} value if header is identified, NULL otherwise
     */
    public Long getProtectedHeaderValueAsLong(long headerKey) {
        CBORObject protectedHeaderValue = getProtectedHeaderValue(headerKey);
        if (protectedHeaderValue != null && (protectedHeaderValue.isNegativeInteger() || protectedHeaderValue.isUnsignedInteger())) {
            return ((CBORSimpleObject) protectedHeaderValue).getValueAsLong();
        }
        return null;
    }

    /**
     * This method returns a String value extracted from protected header of the signature
     *
     * @param headerKey identifier of the header
     * @return {@link Long} value if header is identified, NULL otherwise
     */
    public String getProtectedHeaderValueAsString(long headerKey) {
        CBORObject protectedHeaderValue = getProtectedHeaderValue(headerKey);
        if (protectedHeaderValue != null && (protectedHeaderValue.isUnicodeString())) {
            return ((CBORSimpleObject) protectedHeaderValue).getValueAsString();
        }
        return null;
    }

    /**
     * This method returns a byte array value extracted from protected header of the signature
     *
     * @param headerKey identifier of the header
     * @return byte array value if header is identified, NULL otherwise
     */
    public byte[] getProtectedHeaderValueAsBinaries(long headerKey) {
        CBORObject protectedHeaderValue = getProtectedHeaderValue(headerKey);
        if (protectedHeaderValue != null && (protectedHeaderValue.isByteString())) {
            return ((CBORByteString) protectedHeaderValue).getBytes();
        }
        return null;
    }

    /**
     * This method returns a CBORArray value extracted from protected header of the signature
     *
     * @param headerKey identifier of the header
     * @return {@link CBORArray) value if header is identified, NULL otherwise
     */
    public CBORArray getProtectedHeaderValueAsArray(long headerKey) {
        CBORObject protectedHeaderValue = getProtectedHeaderValue(headerKey);
        if (protectedHeaderValue != null && (protectedHeaderValue.isArray())) {
            return ((CBORArray) protectedHeaderValue);
        }
        return null;
    }

    /**
     * This method returns a CBORMap value extracted from protected header of the signature
     *
     * @param headerKey identifier of the header
     * @return {@link CBORMap) value if header is identified, NULL otherwise
     */
    public CBORMap getProtectedHeaderValueAsMap(long headerKey) {
        CBORObject protectedHeaderValue = getProtectedHeaderValue(headerKey);
        if (protectedHeaderValue != null && (protectedHeaderValue.isMap())) {
            return ((CBORMap) protectedHeaderValue);
        }
        return null;
    }

    /**
     * This method returns a value extracted from protected header of the signature
     *
     * @param headerKey identifier of the header
     * @return {@link CBORObject}
     */
    protected CBORObject getProtectedHeaderValue(long headerKey) {
        CBORObject bodyProtectedHeaderValue = getBodyProtectedHeaderValue(headerKey);
        CBORObject signerProtectedHeaderValue = getSignerProtectedHeaderValue(headerKey);
        if (bodyProtectedHeaderValue != null && signerProtectedHeaderValue != null) {
            LOG.info("Same protected header '{}' is present in body and signer structure.", headerKey);
            if (bodyProtectedHeaderValue.equals(signerProtectedHeaderValue)) {
                LOG.warn("The value of protected header '{}' in body structure does not match the value in signer structure!", headerKey);
                return null;
            }
        }
        return bodyProtectedHeaderValue != null ? bodyProtectedHeaderValue : signerProtectedHeaderValue;
    }

    private CBORObject getBodyProtectedHeaderValue(long headerKey) {
        return bodyProtectedHeader != null ? bodyProtectedHeader.getHeader(headerKey) : null;
    }

    private CBORObject getSignerProtectedHeaderValue(long headerKey) {
        return signerProtectedHeader != null ? signerProtectedHeader.getHeader(headerKey) : null;
    }

    /**
     * This method returns a value extracted from unprotected header of the signature
     *
     * @param headerKey identifier of the header
     * @return {@link CBORObject}
     */
    protected CBORObject getUnprotectedHeaderValue(long headerKey) {
        CBORObject bodyUnprotectedHeaderValue = getBodyUnprotectedHeaderValue(headerKey);
        CBORObject signerUnprotectedHeaderValue = getSignerUnprotectedHeaderValue(headerKey);
        if (bodyUnprotectedHeaderValue != null && signerUnprotectedHeaderValue != null) {
            LOG.info("Same unprotected header '{}' is present in body and signer structure.", headerKey);
            if (bodyUnprotectedHeaderValue.equals(signerUnprotectedHeaderValue)) {
                LOG.warn("The value of unprotected header '{}' in body structure does not match the value in signer structure!", headerKey);
                return null;
            }
        }
        return bodyUnprotectedHeaderValue != null ? bodyUnprotectedHeaderValue : signerUnprotectedHeaderValue;
    }

    private CBORObject getBodyUnprotectedHeaderValue(long headerKey) {
        return bodyUnprotectedHeader != null ? bodyUnprotectedHeader.getHeader(headerKey) : null;
    }

    private CBORObject getSignerUnprotectedHeaderValue(long headerKey) {
        return signerUnprotectedHeader != null ? signerUnprotectedHeader.getHeader(headerKey) : null;
    }

}
