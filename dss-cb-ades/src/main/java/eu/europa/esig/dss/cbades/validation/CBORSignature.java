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
import eu.europa.esig.dss.cbades.cbor.CBORObject;
import eu.europa.esig.dss.cbades.cbor.CBORUtils;
import eu.europa.esig.dss.enumerations.EncryptionAlgorithm;
import eu.europa.esig.dss.enumerations.SignatureAlgorithm;
import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.spi.DSSASN1Utils;
import eu.europa.esig.dss.spi.DSSSecurityProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represents a single COSE signature.
 * The class is used for verification of a cryptographic validity of the COSE signature.
 *
 */
public class CBORSignature {

    private static final Logger LOG = LoggerFactory.getLogger(CBORSignature.class);

    /** Context of the signature */
    private COSESignatureContext context;

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
            cborSignature.bodyProtectedHeader = coseSign.getProtectedHeader();
            cborSignature.bodyUnprotectedHeader = coseSign.getUnprotectedHeader();
            cborSignature.signerProtectedHeader = coseSignature.getProtectedHeader();
            cborSignature.signerUnprotectedHeader = coseSignature.getUnprotectedHeader();
            cborSignature.payload = coseSign.getPayload();
            cborSignature.signature = coseSignature.getSignature();
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
        cborSignature.bodyProtectedHeader = coseSign1.getProtectedHeader();
        cborSignature.bodyUnprotectedHeader = coseSign1.getUnprotectedHeader();
        cborSignature.payload = coseSign1.getPayload();
        cborSignature.signature = coseSign1.getSignature();
        return cborSignature;
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
     * This method allows supplying of a payload
     *
     * @param payload binaries of a payload
     */
    public void setPayloadBytes(byte[] payload) {
        this.payload = new CBORByteString(payload);
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
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
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

        } catch (Exception e) {
            throw new DSSException(String.format("Unable to build signature input bytes : %s", e.getMessage()), e);
        }
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
        COSEProtectedHeader coseProtectedHeader = getCOSEProtectedHeader();
        Long algNumber = coseProtectedHeader.getHeaderAsLong(COSEConstants.ALG);
        if (algNumber == null) {
            // It is usual for COSE signatures to have signature algorithm within unsigned header
            algNumber = coseProtectedHeader.getHeaderAsLong(COSEConstants.ALG);
            if (algNumber != null) {
                LOG.info("Alg header is present within unsigned header!");
            }
        }
        if (algNumber == null) {
            throw new DSSException("No 'alg' header found!");
        }
        return SignatureAlgorithm.forCOSE(algNumber, null);
    }

    private COSEProtectedHeader getCOSEProtectedHeader() {
        return signerProtectedHeader != null ? signerProtectedHeader : bodyProtectedHeader;
    }

}
