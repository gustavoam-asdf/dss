package eu.europa.esig.dss.cbades.cose;

import eu.europa.esig.dss.cbades.cbor.CBORMap;

import java.security.PublicKey;

/**
 * Builds a COSE_Key representation of a {@code java.security.PublicKey} as defined in RFC 9052 "7. Key Objects".
 *
 */
public interface COSEKeyFactory {

    /**
     * Builds a CBOR representation of a {@code PublicKey}, if supported
     * <p>
     * {@code
     *     COSE_Key = {
     *         1 => tstr / int,          ; kty
     *         ? 2 => bstr,              ; kid
     *         ? 3 => tstr / int,        ; alg
     *         ? 4 => [+ (tstr / int) ], ; key_ops
     *         ? 5 => bstr,              ; Base IV
     *         * label => values
     *     }
     * }
     *
     * @param publicKey {@link PublicKey}
     * @return {@link CBORMap}
     */
    CBORMap create(PublicKey publicKey);

}
