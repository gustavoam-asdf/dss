package eu.europa.esig.dss.cbades.cose;

import eu.europa.esig.dss.cbades.COSEConstants;
import eu.europa.esig.dss.cbades.cbor.CBORMap;
import eu.europa.esig.dss.enumerations.EllipticCurve;
import eu.europa.esig.dss.spi.DSSSecurityProvider;
import eu.europa.esig.dss.utils.Utils;
import org.bouncycastle.jcajce.provider.asymmetric.edec.BCEdDSAPublicKey;
import org.bouncycastle.jcajce.provider.asymmetric.edec.BCXDHPublicKey;

import java.math.BigInteger;
import java.security.PublicKey;
import java.security.Security;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Objects;

/**
 * This class is used to build a COSE_Key representation of a {@code java.security.PublicKey}
 * as defined in RFC 9052 "7. Key Objects".
 * The implementation relies on JDK 8 and BouncyCastle utilities.
 * NOTE: The implementation support ECDSA and RSA keys using JDK 8 provider, while EdDSA keys using BC provider.
 * If different provider is used, you may need to use a custom implementation of the factory.
 *
 */
public class DefaultCOSEKeyFactory implements COSEKeyFactory {

    static {
        Security.addProvider(DSSSecurityProvider.getSecurityProvider());
    }

    /**
     * Default constructor
     */
    public DefaultCOSEKeyFactory() {
        // empty
    }

    @Override
    public CBORMap create(PublicKey publicKey) {
        Objects.requireNonNull(publicKey, "Public key cannot be null!");

        if (publicKey instanceof ECPublicKey) {
            return createECDSA((ECPublicKey) publicKey);
        } else if (publicKey instanceof BCEdDSAPublicKey) {
            return createEdDSA((BCEdDSAPublicKey) publicKey);
        } else if (publicKey instanceof BCXDHPublicKey) {
            return createXDH((BCXDHPublicKey) publicKey);
        } else if (publicKey instanceof RSAPublicKey) {
            return createRSA((RSAPublicKey) publicKey);
        } else {
            throw new UnsupportedOperationException(String.format("The key of type '%s' is not supported! " +
                    "Provide a custom COSEKeyFactory should you need to support the key.", publicKey.getClass().getSimpleName()));
        }
    }

    protected CBORMap createECDSA(ECPublicKey publicKey) {
        CBORMap coseKey = new CBORMap();
        coseKey.put(COSEConstants.COSE_KEY_KTY, COSEConstants.COSE_KEY_TYPE_EC2_VALUE); // 'kty' ECDSA
        EllipticCurve ellipticCurve = EllipticCurve.forParameter(publicKey.getParams());
        if (ellipticCurve == null) {
            throw new UnsupportedOperationException("Unknown algorithm curve parameter spec!");
        }
        coseKey.put(COSEConstants.COSE_KEY_TYPE_EC2_CRV, ellipticCurve.getCOSEValue()); // 'crv'
        coseKey.put(COSEConstants.COSE_KEY_TYPE_EC2_X, toECUnsignedBytes(publicKey.getW().getAffineX(), ellipticCurve.getSize())); // 'x'
        coseKey.put(COSEConstants.COSE_KEY_TYPE_EC2_Y, toECUnsignedBytes(publicKey.getW().getAffineY(), ellipticCurve.getSize())); // 'y'
        return coseKey;
    }

    protected CBORMap createEdDSA(BCEdDSAPublicKey publicKey) {
        CBORMap coseKey = new CBORMap();
        coseKey.put(COSEConstants.COSE_KEY_KTY, COSEConstants.COSE_KEY_TYPE_OKP_VALUE); // 'kty' OKP
        EllipticCurve ellipticCurve = EllipticCurve.forLabel(publicKey.getAlgorithm());
        if (ellipticCurve == null) {
            throw new UnsupportedOperationException(String.format("Unknown algorithm curve OID: '%s'!", publicKey.getAlgorithm()));
        }
        coseKey.put(COSEConstants.COSE_KEY_TYPE_OKP_CRV, ellipticCurve.getCOSEValue()); // 'crv'
        coseKey.put(COSEConstants.COSE_KEY_TYPE_OKP_X, publicKey.getPointEncoding()); // 'x'
        return coseKey;
    }

    protected CBORMap createXDH(BCXDHPublicKey publicKey) {
        CBORMap coseKey = new CBORMap();
        coseKey.put(COSEConstants.COSE_KEY_KTY, COSEConstants.COSE_KEY_TYPE_OKP_VALUE); // 'kty' OKP
        EllipticCurve ellipticCurve = EllipticCurve.forLabel(publicKey.getAlgorithm());
        if (ellipticCurve == null) {
            throw new UnsupportedOperationException(String.format("Unknown algorithm curve OID: '%s'!", publicKey.getAlgorithm()));
        }
        coseKey.put(COSEConstants.COSE_KEY_TYPE_OKP_CRV, ellipticCurve.getCOSEValue()); // 'crv'
        coseKey.put(COSEConstants.COSE_KEY_TYPE_OKP_X, publicKey.getUEncoding()); // 'U'
        return coseKey;
    }

    private CBORMap createRSA(RSAPublicKey publicKey) {
        CBORMap coseKey = new CBORMap();
        coseKey.put(COSEConstants.COSE_KEY_KTY, COSEConstants.COSE_KEY_TYPE_RSA_VALUE); // 'kty' RSA
        coseKey.put(COSEConstants.COSE_KEY_TYPE_RSA_N, toRSAUnsignedBytes(publicKey.getModulus())); // 'n'
        coseKey.put(COSEConstants.COSE_KEY_TYPE_RSA_E, toRSAUnsignedBytes(publicKey.getPublicExponent())); // 'e'

        return coseKey;
    }

    private byte[] toECUnsignedBytes(BigInteger bigInteger, int size) {
        byte[] bytes = bigInteger.toByteArray();
        if (bytes.length == size) {
            return bytes;
        }

        if (bytes.length == size + 1 && bytes[0] == 0x00) {
            // remove leading zero
            return Utils.subarray(bytes, 1, bytes.length);
        }

        byte[] result = new byte[size];
        System.arraycopy(bytes, 0, result, size - bytes.length, bytes.length);
        return result;
    }

    private byte[] toRSAUnsignedBytes(BigInteger bigInteger) {
        byte[] bytes = bigInteger.toByteArray();

        if (bytes.length > 1 && bytes[0] == 0x00) {
            byte[] tmp = new byte[bytes.length - 1];
            System.arraycopy(bytes, 1, tmp, 0, tmp.length);
            return tmp;
        }

        return bytes;
    }

}
