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
 * This class represents a single COSE signature
 *
 */
public class CBORSignature {

    private static final Logger LOG = LoggerFactory.getLogger(CBORSignature.class);

    private COSESignatureContext context;

    private COSEProtectedHeader bodyProtectedHeader;

    private COSEProtectedHeader signerProtectedHeader;

    private COSEUnprotectedHeader bodyUnprotectedHeader;

    private COSEUnprotectedHeader signerUnprotectedHeader;

    private CBORByteString externalAttributes;

    private CBORObject payload;

    private CBORByteString signature;

    private Key key;

    static {
        Security.addProvider(DSSSecurityProvider.getSecurityProvider());
    }

    protected CBORSignature() {
    }

    public static List<CBORSignature> fromCOSESignStructure(COSESignStructure coseSignStructure) {
        if (coseSignStructure instanceof COSESign) {
            return fromCOSESign((COSESign) coseSignStructure);
        } else if (coseSignStructure instanceof COSESign1) {
            return Collections.singletonList(fromCOSE1Sign((COSESign1) coseSignStructure));
        }
        throw new UnsupportedOperationException(String.format("Unsupported class '%s'", coseSignStructure.getClass()));
    }

    public static List<CBORSignature> fromCOSESign(COSESign coseSign) {
        final List<CBORSignature> cborSignatures = new ArrayList<>();
        for (COSESignature coseSignature : coseSign.getSignatures()) {
            final CBORSignature cborSignature = new CBORSignature();
            cborSignature.setContext(COSESignatureContext.COSE_SIGN);
            cborSignature.setBodyProtectedHeader(coseSign.getProtectedHeader());
            cborSignature.setBodyUnprotectedHeader(coseSign.getUnprotectedHeader());
            cborSignature.setSignerProtectedHeader(coseSignature.getProtectedHeader());
            cborSignature.setSignerUnprotectedHeader(coseSignature.getUnprotectedHeader());
            cborSignature.setPayload(coseSign.getPayload());
            cborSignature.setSignature(coseSignature.getSignature());
            cborSignatures.add(cborSignature);
        }
        return cborSignatures;
    }

    public static CBORSignature fromCOSE1Sign(COSESign1 coseSign1) {
        final CBORSignature cborSignature = new CBORSignature();
        cborSignature.setContext(COSESignatureContext.COSE_SIGN1);
        cborSignature.setSignerProtectedHeader(coseSign1.getProtectedHeader());
        cborSignature.setSignerUnprotectedHeader(coseSign1.getUnprotectedHeader());
        cborSignature.setPayload(coseSign1.getPayload());
        cborSignature.setSignature(coseSign1.getSignature());
        return cborSignature;
    }

    public COSESignatureContext getContext() {
        return context;
    }

    public void setContext(COSESignatureContext context) {
        this.context = context;
    }

    public COSEProtectedHeader getBodyProtectedHeader() {
        return bodyProtectedHeader;
    }

    public void setBodyProtectedHeader(COSEProtectedHeader bodyProtectedHeader) {
        this.bodyProtectedHeader = bodyProtectedHeader;
    }

    public COSEProtectedHeader getSignerProtectedHeader() {
        return signerProtectedHeader;
    }

    public void setSignerProtectedHeader(COSEProtectedHeader signerProtectedHeader) {
        this.signerProtectedHeader = signerProtectedHeader;
    }

    public COSEUnprotectedHeader getBodyUnprotectedHeader() {
        return bodyUnprotectedHeader;
    }

    public void setBodyUnprotectedHeader(COSEUnprotectedHeader bodyUnprotectedHeader) {
        this.bodyUnprotectedHeader = bodyUnprotectedHeader;
    }

    public COSEUnprotectedHeader getSignerUnprotectedHeader() {
        return signerUnprotectedHeader;
    }

    public void setSignerUnprotectedHeader(COSEUnprotectedHeader signerUnprotectedHeader) {
        this.signerUnprotectedHeader = signerUnprotectedHeader;
    }

    public CBORByteString getExternalAttributes() {
        return externalAttributes;
    }

    public void setExternalAttributes(CBORByteString externalAttributes) {
        this.externalAttributes = externalAttributes;
    }

    public CBORObject getPayload() {
        return payload;
    }

    public void setPayload(CBORObject payload) {
        this.payload = payload;
    }

    public CBORByteString getSignature() {
        return signature;
    }

    public void setSignature(CBORByteString signature) {
        this.signature = signature;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public boolean verifySignature() {
        // TODO : add additional verification (crit dict, etc.). See JWS validation
        try {
            // Create Signature object
            SignatureAlgorithm signatureAlgorithm = getAlgorithm();
            Signature signature = Signature.getInstance(signatureAlgorithm.getJCEId(), DSSSecurityProvider.getSecurityProviderName());

            // Initialize Signature object with the public key
            Key key = getKey();
            PublicKey publicKey = (PublicKey) key;
            signature.initVerify(publicKey);

            // Supply the signature input bytes
            byte[] signatureInputBytes = getSignatureInputBytes();
            signature.update(signatureInputBytes);

            // Verify the signature
            byte[] signatureBytes = getSignature().getValue();
            signatureBytes = ensureDerEncodedSignature(signatureBytes, signatureAlgorithm);
            return signature.verify(signatureBytes);

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
             * 2. The protected attributes from the body structure encoded in a bstr type.
             * If there are no protected attributes, a bstr of length zero is used.
             */
            if (bodyProtectedHeader != null) {
                array.add(bodyProtectedHeader.getByteString());
            } else if (COSESignatureContext.COSE_SIGN1 != context) {
                array.add(CBORUtils.EMPTY_BYTE_STRING);
            }
            /*
             * 3. The protected attributes from the signer structure encoded in a
             * bstr type. If there are no protected attributes, a bstr of
             * length zero is used. This field is omitted for the COSE_Sign1
             * signature structure.
             */
            if (signerProtectedHeader != null) {
                array.add(signerProtectedHeader.getByteString());
            } else {
                array.add(CBORUtils.EMPTY_BYTE_STRING);
            }
            /*
             * 4. The protected attributes from the application encoded in a bstr type.
             * If this field is not supplied, it defaults to a zero-
             * length binary string.  (See Section 4.3 for application guidance
             * on constructing this field.)
             */
            if (externalAttributes != null) {
                array.add(externalAttributes);
            } else {
                array.add(CBORUtils.EMPTY_BYTE_STRING);
            }
            /*
             * 5. The payload to be signed encoded in a bstr type.
             * The payload is placed here independent of how it is transported.
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

    public SignatureAlgorithm getAlgorithm() {
        Long algNumber = signerProtectedHeader.getHeaderAsLong(COSEConstants.ALG);
        if (algNumber == null) {
            // It is usual for COSE signatures to have signature algorithm within unsigned header
            algNumber = signerUnprotectedHeader.getHeaderAsLong(COSEConstants.ALG);
            if (algNumber != null) {
                LOG.info("Alg header is present within unsigned header!");
            }
        }
        if (algNumber == null) {
            throw new DSSException("No 'alg' header found!");
        }
        return SignatureAlgorithm.forCOSE(algNumber, null);
    }

}
